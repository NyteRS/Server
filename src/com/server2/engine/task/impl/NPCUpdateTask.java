package com.server2.engine.task.impl;

import java.util.Iterator;

import com.server2.GameEngine;
import com.server2.InstanceDistributor;
import com.server2.engine.task.Task;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.net.GamePacket;
import com.server2.net.GamePacketBuilder;
import com.server2.util.Misc;

/**
 * A task which creates and sends the NPC update block.
 * 
 * @author Rene
 * 
 */
public class NPCUpdateTask implements Task {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Creates an npc update task.
	 * 
	 * @param player
	 *            The player.
	 */
	public NPCUpdateTask(Player player) {
		this.player = player;
	}

	/**
	 * Adds a new NPC.
	 * 
	 * @param packet
	 *            The main packet.
	 * @param npc
	 *            The npc to add.
	 */
	private void addNewNPC(GamePacketBuilder packet, NPC npc) {
		/*
		 * Write the NPC's index.
		 */
		packet.putBits(14, npc.getNpcId());

		/*
		 * Calculate the x and y offsets.
		 */
		final int yPos = npc.getAbsY() - player.getAbsY();
		final int xPos = npc.getAbsX() - player.getAbsX();

		/*
		 * And write them.
		 */
		packet.putBits(5, yPos);
		packet.putBits(5, xPos);

		/*
		 * TODO unsure, but probably discards the client-side walk queue.
		 */
		packet.putBits(1, 0);

		/*
		 * We now write the NPC type id.
		 */
		packet.putBits(14, npc.getDefinition().getType());
		//
		/*
		 * And indicate if an update is required.
		 */
		packet.putBits(1, npc.isUpdateRequired() ? 1 : 0);
	}

	@Override
	public void execute(GameEngine context) {

		/*
		 * The update block holds the update masks and data, and is written
		 * after the main block.
		 */
		final GamePacketBuilder updateBlock = new GamePacketBuilder();

		/*
		 * The main packet holds information about adding, moving and removing
		 * NPCs.
		 */
		final GamePacketBuilder packet = new GamePacketBuilder(65,
				GamePacket.Type.VARIABLE_SHORT);
		packet.startBitAccess();

		/*
		 * Write the current size of the npc list.
		 */
		packet.putBits(8, player.getLocalNPCs().size());

		/*
		 * Iterate through the local npc list.
		 */
		for (final Iterator<NPC> it$ = player.getLocalNPCs().iterator(); it$
				.hasNext();) {
			/*
			 * Get the next NPC.
			 */
			final NPC npc = it$.next();

			/*
			 * If the NPC should still be in our list.
			 */
			if (InstanceDistributor.getNPCManager().getNPCMap()
					.containsValue(npc)
					&& !npc.isHidden()
					&& npc.getPosition().isWithinDistance(player.getPosition())) {
				/*
				 * Update the movement.
				 */
				updateNPCMovement(packet, npc);

				/*
				 * Check if an update is required, and if so, send the update.
				 */
				if (npc.isUpdateRequired())
					updateNPC(updateBlock, npc);
			} else {
				/*
				 * Otherwise, remove the NPC from the list.
				 */
				it$.remove();

				/*
				 * Tell the client to remove the NPC from the list.
				 */
				packet.putBits(1, 1);
				packet.putBits(2, 3);
			}
		}

		/*
		 * Loop through all NPCs in the world.
		 */
		for (final NPC npc : InstanceDistributor.getNPCManager().getNPCMap()
				.values()) {

			if (npc == null)
				continue;

			/*
			 * if (!player.canNpcUpdate) { return;// TODO INCASE ISSUES }
			 */

			/*
			 * Check if there is room left in the local list.
			 */
			if (player.getLocalNPCs().size() >= 255)
				/*
				 * There is no more room left in the local list. We cannot add
				 * more NPCs, so we just ignore the extra ones. They will be
				 * added as other NPCs get removed.
				 */
				break;

			if (!player.withinDistance(npc))
				/*
				 * Check that the NPC is within good distance of the player
				 * before adding to local list.
				 */
				continue;

			/*
			 * If they should not be added ignore them.
			 */
			if (npc.isHidden())
				continue;
			// Index checking
			if (npc.lastIndex != npc.getNpcId())
				addNewNPC(packet, npc);
			npc.lastIndex = npc.getNpcId();

			if (!player.getLocalNPCs().contains(npc)) {

				/*
				 * Add the npc to the local list if it is within distance.
				 */
				player.getLocalNPCs().add(npc);
				boolean a = false;
				/*
				 * Add the npc in the packet.
				 */
				addNewNPC(packet, npc);
				a = true;

				/*
				 * Check if an update is required.
				 */
				if (npc.isUpdateRequired() || a) {

					/*
					 * If so, update the npc.
					 */
					npc.turnUpdateRequired = true;
					updateNPC(updateBlock, npc);
				}
			}
		}

		/*
		 * Check if the update block isn't empty.
		 */
		if (!updateBlock.isEmpty()) {
			/*
			 * If so, put a flag indicating that an update block follows.
			 */
			packet.putBits(14, 16383);
			packet.finishBitAccess();

			/*
			 * And append the update block.
			 */
			packet.put(updateBlock.toPacket().getPayload());
		} else
			/*
			 * Terminate the packet normally.
			 */
			packet.finishBitAccess();

		player.write(packet.toPacket());
	}

	/**
	 * Update an NPC.
	 * 
	 * @param packet
	 *            The update block.
	 * @param npc
	 *            The npc.
	 */
	private void updateNPC(GamePacketBuilder packet, NPC npc) {
		/*
		 * Calculate the mask.
		 */
		int mask = 0;

		if (npc.textUpdateRequired)
			mask |= 0x1;
		if (npc.isAnimUpdateRequired())
			mask |= 0x10;
		if (npc.hitUpdateRequired2)
			mask |= 0x8;
		if (npc.mask80update)
			mask |= 0x80;
		if (npc.dirUpdateRequired)
			mask |= 0x20;
		if (npc.hitUpdateRequired)
			mask |= 0x40;
		if (npc.turnUpdateRequired)
			mask |= 0x4;

		packet.put((byte) mask);
		if (npc.isAnimUpdateRequired()) {
			packet.putLEShort(npc.animNumber);
			packet.put((byte) 1);
		}
		if (npc.hitUpdateRequired2) {
			packet.putByteC((byte) npc.hit2);
			packet.putByteS((byte) npc.hitType2);
			packet.put((byte) npc.type2.ordinal()); // hit icon
			packet.putByteS((byte) Misc.getCurrentHP(npc.hp, npc.maxHP, 100));
			packet.putByteC((byte) 100);
		}
		if (npc.mask80update) {
			packet.putShort(npc.mask80var1);
			packet.putInt(npc.mask80var2);
		}// dafu
		if (npc.dirUpdateRequired)
			packet.putShort(npc.rawDirection);
		if (npc.textUpdateRequired)
			packet.putRS2String(npc.textUpdate);
		if (npc.hitUpdateRequired) {
			packet.putByteC((byte) npc.hit);
			packet.putByteS((byte) npc.hitType);
			packet.put((byte) npc.type.ordinal());
			packet.putByteS((byte) Misc.getCurrentHP(npc.hp, npc.maxHP, 100));
			packet.putByteC((byte) 100);
		}
		if (npc.turnUpdateRequired) {

			packet.putLEShort(npc.focusPointX);
			packet.putLEShort(npc.focusPointY);

		}
	}

	/**
	 * Update an NPC's movement.
	 * 
	 * @param packet
	 *            The main packet.
	 * @param npc
	 *            The npc.
	 */
	private void updateNPCMovement(GamePacketBuilder packet, NPC npc) {

		/*
		 * They are not, so check if they are walking.
		 */
		if (npc.direction == -1) {
			/*
			 * They are not walking, check if the NPC needs an update.
			 */
			if (npc.isUpdateRequired()) {
				/*
				 * Indicate an update is required.
				 */
				packet.putBits(1, 1);

				/*
				 * Indicate we didn't move.
				 */
				packet.putBits(2, 0);
			} else
				/*
				 * Indicate no update or movement is required.
				 */
				packet.putBits(1, 0);
		} else {
			/*
			 * They are walking, so indicate an update is required.
			 */
			packet.putBits(1, 1);

			/*
			 * Indicate the NPC is walking 1 tile.
			 */
			packet.putBits(2, 1);

			/*
			 * And write the direction.
			 */
			packet.putBits(3, Misc.xlateDirectionToClient[npc.direction]);

			/*
			 * And write the update flag.
			 */
			packet.putBits(1, npc.isUpdateRequired() ? 1 : 0);
		}
	}

}