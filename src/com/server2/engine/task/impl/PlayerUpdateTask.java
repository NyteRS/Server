package com.server2.engine.task.impl;

import java.util.Iterator;

import com.server2.GameEngine;
import com.server2.engine.task.Task;
import com.server2.model.entity.player.ChatMessage;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.net.GamePacket;
import com.server2.net.GamePacketBuilder;
import com.server2.util.Misc;
import com.server2.world.PlayerManager;

/**
 * A task which creates and sends the player update block.
 * 
 * @author Rene
 */

public class PlayerUpdateTask implements Task {

	private static void appendHit2Update(final Player p,
			final GamePacketBuilder updateBlock) {
		updateBlock.put((byte) p.hitDiff2);
		updateBlock.put((byte) (!p.poisonHit ? p.hitDiff2 > 0 ? 1 : 0 : 2));
		updateBlock.put((byte) (!p.poisonHit ? p.type2.ordinal() : -1)); // hitmask
		updateBlock.put((byte) p.hitpoints);
		updateBlock.put((byte) p.calculateMaxHP());
	}

	private static void appendHitUpdate(final Player p,
			final GamePacketBuilder updateBlock) {
		updateBlock.put((byte) p.hitDiff);
		updateBlock.put((byte) (p.hitDiff > 0 ? 1 : 0));
		updateBlock.put((byte) (p.type != null ? p.type.ordinal() : 1));
		updateBlock.put((byte) p.hitpoints);
		updateBlock.put((byte) p.calculateMaxHP());
	}

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Creates an update task.
	 * 
	 * @param player
	 *            The player.
	 */
	public PlayerUpdateTask(Player player) {
		this.player = player;
	}

	/**
	 * Adds a new player.
	 * 
	 * @param packet
	 *            The packet.
	 * @param otherPlayer
	 *            The player.
	 */
	public void addNewPlayer(GamePacketBuilder packet, Player otherPlayer) {
		/*
		 * Write the player index.
		 */
		packet.putBits(11, otherPlayer.getIndex());

		/*
		 * Write two flags here: the first indicates an update is required (this
		 * is always true as we add the appearance after adding a player) and
		 * the second to indicate we should discard client-side walk queues.
		 */
		packet.putBits(1, 1);
		packet.putBits(1, 1);

		/*
		 * Calculate the x and y offsets.
		 */

		final int yPos = otherPlayer.getAbsY() - player.getAbsY();
		final int xPos = otherPlayer.getAbsX() - player.getAbsX();

		/*
		 * Write the x and y offsets.
		 */
		packet.putBits(5, yPos);
		packet.putBits(5, xPos);
	}

	/**
	 * Appends an animation update.
	 * 
	 * @param block
	 *            The update block.
	 * @param otherPlayer
	 *            The player.
	 */
	private void appendAnimationUpdate(GamePacketBuilder block,
			Player otherPlayer) {
		block.putLEShort(otherPlayer.animationRequest == -1 ? 65535
				: otherPlayer.animationRequest);
		block.putByteC(otherPlayer.animationWaitCycles);
	}

	/**
	 * Appends a chat text update.
	 * 
	 * @param packet
	 *            The packet.
	 * @param otherPlayer
	 *            The player.
	 */
	private void appendChatUpdate(GamePacketBuilder packet, Player otherPlayer) {
		final ChatMessage chatMessage = otherPlayer.getCurrentChatMessage();
		final byte[] bytes = chatMessage.getText();
		packet.putLEShort((chatMessage.getColour() & 0xFF) << 8
				| chatMessage.getEffects() & 0xFF);
		packet.put((byte) otherPlayer.getPrivileges());
		packet.putByteC(bytes.length);
		for (int ptr = bytes.length - 1; ptr >= 0; ptr--)
			packet.put(bytes[ptr]);
	}

	/**
	 * Appends a graphics update.
	 * 
	 * @param block
	 *            The update block.
	 * @param otherPlayer
	 *            The player.
	 */
	private void appendGraphicsUpdate(GamePacketBuilder block,
			Player otherPlayer) {
		block.putLEShort(otherPlayer.gfxID);
		block.putInt(otherPlayer.gfxDelay);
	}

	private void appendPlayerAppearanceUpdate(GamePacketBuilder packet,
			Player otherPlayer) {
		int summoningAdd = 0;
		if (otherPlayer.inWilderness())
			summoningAdd = otherPlayer.combatLevel
					- otherPlayer.summoningCombatLevel;
		else
			summoningAdd = 0;

		final GamePacketBuilder playerProps = new GamePacketBuilder();

		playerProps.put((byte) otherPlayer.playerLook[PlayerConstants.SEX]);
		playerProps.put((byte) otherPlayer.headIcon);
		playerProps.put((byte) otherPlayer.skullIcon);

		if (!otherPlayer.isNPC
				&& otherPlayer.playerEquipment[PlayerConstants.RING] != 6583
				|| otherPlayer.getTarget() != null) {
			if (otherPlayer.playerEquipment[PlayerConstants.HELM] > 1)
				playerProps
						.putShort(0x200 + otherPlayer.playerEquipment[PlayerConstants.HELM]);
			else
				playerProps.put((byte) 0);
			if (otherPlayer.playerEquipment[PlayerConstants.CAPE] > 1)
				playerProps
						.putShort(0x200 + otherPlayer.playerEquipment[PlayerConstants.CAPE]);
			else
				playerProps.put((byte) 0);
			if (otherPlayer.playerEquipment[PlayerConstants.AMULET] > 1)
				playerProps
						.putShort(0x200 + otherPlayer.playerEquipment[PlayerConstants.AMULET]);
			else
				playerProps.put((byte) 0);
			if (otherPlayer.playerEquipment[PlayerConstants.WEAPON] > 1)
				playerProps
						.putShort(0x200 + otherPlayer.playerEquipment[PlayerConstants.WEAPON]);
			else
				playerProps.put((byte) 0);
			if (otherPlayer.playerEquipment[PlayerConstants.CHEST] > 1)
				playerProps
						.putShort(0x200 + otherPlayer.playerEquipment[PlayerConstants.CHEST]);
			else
				playerProps
						.putShort(0x100 + otherPlayer.playerLook[PlayerConstants.BODY]);
			if (otherPlayer.playerEquipment[PlayerConstants.SHIELD] > 1)
				playerProps
						.putShort(0x200 + otherPlayer.playerEquipment[PlayerConstants.SHIELD]);
			else
				playerProps.put((byte) 0);
			if (!otherPlayer.isFullBody)
				playerProps
						.putShort(0x100 + otherPlayer.playerLook[PlayerConstants.ARMS]);
			else
				playerProps.put((byte) 0);
			if (otherPlayer.playerEquipment[PlayerConstants.BOTTOMS] > 1)
				playerProps
						.putShort(0x200 + otherPlayer.playerEquipment[PlayerConstants.BOTTOMS]);
			else
				playerProps
						.putShort(0x100 + otherPlayer.playerLook[PlayerConstants.LEGS]);
			if (!otherPlayer.isFullHelm && !otherPlayer.isFullMask)
				playerProps
						.putShort(0x100 + otherPlayer.playerLook[PlayerConstants.HEAD]);
			else
				playerProps.put((byte) 0);
			if (otherPlayer.playerEquipment[PlayerConstants.GLOVES] > 1)
				playerProps
						.putShort(0x200 + otherPlayer.playerEquipment[PlayerConstants.GLOVES]);
			else
				playerProps
						.putShort(0x100 + otherPlayer.playerLook[PlayerConstants.HANDS]);
			if (otherPlayer.playerEquipment[PlayerConstants.BOOTS] > 1)
				playerProps
						.putShort(0x200 + otherPlayer.playerEquipment[PlayerConstants.BOOTS]);
			else
				playerProps
						.putShort(0x100 + otherPlayer.playerLook[PlayerConstants.FEET]);
			if (!otherPlayer.isFullMask
					&& otherPlayer.playerLook[PlayerConstants.SEX] != 1)
				playerProps
						.putShort(0x100 + otherPlayer.playerLook[PlayerConstants.BEARD]);
			else
				playerProps.put((byte) 0);
		} else {
			if (otherPlayer.playerEquipment[PlayerConstants.RING] == 6583
					&& otherPlayer.getTarget() == null)
				otherPlayer.npcID = 2626;
			else if (otherPlayer.npcID == 2626
					&& otherPlayer.playerEquipment[PlayerConstants.RING] != 6583)
				otherPlayer.npcID = -1;
			playerProps.putShort(-1);
			playerProps.putShort(otherPlayer.npcID);
		}

		playerProps
				.put((byte) otherPlayer.playerLook[PlayerConstants.HAIR_COLOUR]);
		playerProps
				.put((byte) otherPlayer.playerLook[PlayerConstants.BODY_COLOUR]);
		playerProps
				.put((byte) otherPlayer.playerLook[PlayerConstants.LEG_COLOUR]);
		playerProps
				.put((byte) otherPlayer.playerLook[PlayerConstants.FEET_COLOUR]);
		playerProps
				.put((byte) otherPlayer.playerLook[PlayerConstants.SKIN_COLOUR]);
		playerProps.putShort(otherPlayer.standEmote);
		playerProps.putShort(0x337);
		playerProps.putShort(otherPlayer.walkEmote);
		playerProps.putShort(0x334);
		playerProps.putShort(0x335);
		playerProps.putShort(0x336);
		playerProps.putShort(otherPlayer.runEmote);
		playerProps.putLong(Misc.playerNameToInt64(otherPlayer.getUsername()));
		int combatLevel = 0;
		if (!otherPlayer.inWilderness())
			combatLevel = otherPlayer.combatLevel;
		else
			combatLevel = otherPlayer.summoningCombatLevel;
		playerProps.put((byte) combatLevel);
		playerProps.putShort(summoningAdd);
		if (otherPlayer.outStreamRank > 0)
			playerProps.putShort(otherPlayer.outStreamRank);
		else
			playerProps.putShort(36);
		final GamePacket propsPacket = playerProps.toPacket();

		packet.putByteC(propsPacket.getLength());
		packet.put(propsPacket.getPayload());
	}

	@Override
	public void execute(GameEngine context) {

		/*
		 * If the map region changed send the new one. We do this immediately as
		 * the client can begin loading it before the actual packet is received.
		 */
		if (player.mapRegionDidChange)
			player.getActionSender().sendMapRegion();

		/*
		 * The update block packet holds update blocks and is send after the
		 * main packet.
		 */
		final GamePacketBuilder updateBlock = new GamePacketBuilder();

		/*
		 * The main packet is written in bits instead of bytes and holds
		 * information about the local list, players to add and remove, movement
		 * and which updates are required.
		 */
		final GamePacketBuilder packet = new GamePacketBuilder(81,
				GamePacket.Type.VARIABLE_SHORT);
		packet.startBitAccess();

		/*
		 * Updates this player.
		 */
		updateThisPlayerMovement(packet);
		updatePlayer(updateBlock, player, false, true);

		/*
		 * Write the current size of the player list.
		 */
		packet.putBits(8, player.getLocalPlayers().size());

		/*
		 * Iterate through the local player list.
		 */

		for (final Iterator<Integer> iterator = player.getLocalPlayers()
				.iterator(); iterator.hasNext();) {
			/*
			 * Get the next player.
			 */
			final int id = iterator.next();
			final Player otherPlayer = PlayerManager.getSingleton()
					.getPlayers()[id];

			/*
			 * If the player should still be in our list.
			 */
			if (otherPlayer != null
					&& PlayerManager.getSingleton().isPlayerOn(
							otherPlayer.getUsername())
					&& !otherPlayer.didTeleport
					&& otherPlayer.getPosition().isWithinDistance(
							player.getPosition()) && otherPlayer.isVisible()) {
				/*
				 * Update the movement.
				 */
				updatePlayerMovement(packet, otherPlayer);

				/*
				 * Check if an update is required, and if so, send the update.
				 */
				if (otherPlayer.updateRequired)
					updatePlayer(updateBlock, otherPlayer, false, false);
			} else {
				/*
				 * Otherwise, remove the player from the list.
				 */
				iterator.remove();

				/*
				 * Tell the client to remove the player from the list.
				 */
				packet.putBits(1, 1);
				packet.putBits(2, 3);
			}
		}

		/*
		 * Loop through every player.
		 */
		for (final Player otherPlayer : PlayerManager.getSingleton()
				.getPlayers()) {

			if (otherPlayer == null)
				continue;

			if (!player.withinDistance(otherPlayer))
				/*
				 * Check that the NPC is within good distance of the player
				 * before adding to local list.
				 */
				continue;

			/*
			 * Check if there is room left in the local list.
			 */
			if (player.getLocalPlayers().size() >= 255)
				/*
				 * There is no more room left in the local list. We cannot add
				 * more players, so we just ignore the extra ones. They will be
				 * added as other players get removed.
				 */
				break;

			if (!player.canUpdatePlayers)
				break;
			/*
			 * Do not add anymore data to the packet if it the packet exceeds
			 * the maximum packet size as this will cause the client to crash.
			 */
			if (packet.getLength() + updateBlock.getLength() >= 3072)
				break;

			/*
			 * If they should not be added ignore them.
			 */
			if (otherPlayer == player
					|| player.getLocalPlayers()
							.contains(otherPlayer.getIndex())
					|| !otherPlayer.isVisible())
				continue;

			/*
			 * Add the player to the local list if it is within distance.
			 */
			player.getLocalPlayers().add(otherPlayer.getIndex());

			/*
			 * Add the player in the packet.
			 */
			addNewPlayer(packet, otherPlayer);

			/*
			 * Update the player, forcing the appearance flag.
			 */
			updatePlayer(updateBlock, otherPlayer, true, false);
		}

		/*
		 * Check if the update block is not empty.
		 */
		if (!updateBlock.isEmpty()) {
			/*
			 * Write a magic id indicating an update block follows.
			 */
			packet.putBits(11, 2047);
			packet.finishBitAccess();

			/*
			 * Add the update block at the end of this packet.
			 */
			packet.put(updateBlock.toPacket().getPayload());
		} else
			/*
			 * Terminate the packet normally.
			 */
			packet.finishBitAccess();

		/*
		 * Write the packet.
		 */
		player.write(packet.toPacket());
	}

	/**
	 * Updates a player.
	 * 
	 * @param packet
	 *            The packet.
	 * @param otherPlayer
	 *            The other player.
	 * @param forceAppearance
	 *            The force appearance flag.
	 * @param noChat
	 *            Indicates chat should not be relayed to this player.
	 */
	public void updatePlayer(GamePacketBuilder packet, Player otherPlayer,
			boolean forceAppearance, boolean noChat) {

		/*
		 * If no update is required and we don't have to force an appearance
		 * update, don't write anything.
		 */
		if (!otherPlayer.updateRequired && !forceAppearance)
			return;

		/*
		 * We can used the cached update block!
		 */
		synchronized (otherPlayer) {
			/*
			 * if (otherPlayer.hasCachedUpdateBlock() && otherPlayer != player
			 * && !forceAppearance && !noChat) {
			 * packet.put(otherPlayer.getCachedUpdateBlock().getPayload());
			 * return; }
			 */

			/*
			 * We have to construct and cache our own block.
			 */
			final GamePacketBuilder block = new GamePacketBuilder();

			/*
			 * Calculate the bitmask.
			 */
			int mask = 0;

			// TODO mask 0x400
			if (otherPlayer.graphicsUpdateRequired)
				mask |= 0x100;
			if (otherPlayer.animationRequest != -1)
				mask |= 0x8;
			if (otherPlayer.forcedTextUpdateRequired)
				mask |= 0x4;
			if (otherPlayer.chatTextUpdateRequired && !noChat
					&& otherPlayer.getCurrentChatMessage() != null)
				mask |= 0x80;
			if (otherPlayer.turnPlayerToUpdateRequired)
				mask |= 0x1;
			if (otherPlayer.appearanceUpdateRequired || forceAppearance)
				mask |= 0x10;

			if (otherPlayer.faceUpdateRequired)
				mask |= 0x2;
			if (otherPlayer.hitUpdateRequired)
				mask |= 0x20;
			if (otherPlayer.hitUpdateRequired2)
				mask |= 0x200;
			/*
			 * Check if the bitmask would overflow a byte.
			 */
			if (mask >= 0x100) {
				/*
				 * Write it as a short and indicate we have done so.
				 */
				mask |= 0x40;
				block.put((byte) (mask & 0xFF));
				block.put((byte) (mask >> 8));
			} else
				/*
				 * Write it as a byte.
				 */
				block.put((byte) mask);
			/*
			 * Append the appropriate updates.
			 */
			if (otherPlayer.graphicsUpdateRequired)
				appendGraphicsUpdate(block, otherPlayer);
			if (otherPlayer.animationRequest != -1)
				appendAnimationUpdate(block, otherPlayer);
			if (otherPlayer.forcedTextUpdateRequired)
				block.putRS2String(otherPlayer.forcedText);
			if (otherPlayer.chatTextUpdateRequired && !noChat
					&& otherPlayer.getCurrentChatMessage() != null)
				appendChatUpdate(block, otherPlayer);
			if (otherPlayer.turnPlayerToUpdateRequired)
				block.putLEShort(otherPlayer.turnPlayerTo);
			if (otherPlayer.appearanceUpdateRequired || forceAppearance)
				appendPlayerAppearanceUpdate(block, otherPlayer);
			if (otherPlayer.faceUpdateRequired) {
				block.putLEShortA(otherPlayer.viewToX * 2 + 1);
				block.putLEShort(otherPlayer.viewToY * 2 + 1);
			}
			if (otherPlayer.hitUpdateRequired)
				appendHitUpdate(otherPlayer, block);
			if (otherPlayer.hitUpdateRequired2)
				appendHit2Update(otherPlayer, block);

			/*
			 * Convert the block builder to a packet.
			 */
			final GamePacket blockPacket = block.toPacket();

			/*
			 * Now it is over, cache the block if we can.
			 */
			if (otherPlayer != player && !forceAppearance && !noChat)
				otherPlayer.setCachedUpdateBlock(blockPacket);

			/*
			 * And finally append the block at the end.
			 */
			packet.put(blockPacket.getPayload());
		}
	}

	/**
	 * Updates a non-this player's movement.
	 * 
	 * @param packet
	 *            The packet.
	 * @param otherPlayer
	 *            The player.
	 */
	public void updatePlayerMovement(GamePacketBuilder packet,
			Player otherPlayer) {
		/*
		 * Check which type of movement took place.
		 */
		if (otherPlayer.getPrimaryDirection().toInteger() == -1) {
			/*
			 * If no movement did, check if an update is required.
			 */
			if (otherPlayer.updateRequired) {
				/*
				 * Signify that an update happened.
				 */
				packet.putBits(1, 1);

				/*
				 * Signify that there was no movement.
				 */
				packet.putBits(2, 0);
			} else
				/*
				 * Signify that nothing changed.
				 */
				packet.putBits(1, 0);
		} else if (otherPlayer.getSecondaryDirection().toInteger() == -1) {
			/*
			 * The player moved but didn't run. Signify that an update is
			 * required.
			 */
			packet.putBits(1, 1);

			/*
			 * Signify we moved one tile.
			 */
			packet.putBits(2, 1);

			/*
			 * Write the primary sprite (i.e. walk direction).
			 */
			packet.putBits(3, otherPlayer.getPrimaryDirection().toInteger());

			/*
			 * Write a flag indicating if a block update happened.
			 */
			packet.putBits(1, otherPlayer.updateRequired ? 1 : 0);
		} else {
			/*
			 * The player ran. Signify that an update happened.
			 */
			packet.putBits(1, 1);

			/*
			 * Signify that we moved two tiles.
			 */
			packet.putBits(2, 2);

			/*
			 * Write the primary sprite (i.e. walk direction).
			 */
			packet.putBits(3, otherPlayer.getPrimaryDirection().toInteger());

			/*
			 * Write the secondary sprite (i.e. run direction).
			 */
			packet.putBits(3, otherPlayer.getSecondaryDirection().toInteger());

			/*
			 * Write a flag indicating if a block update happened.
			 */
			packet.putBits(1, otherPlayer.updateRequired ? 1 : 0);
		}
	}

	/**
	 * Updates this player's movement.
	 * 
	 * @param packet
	 *            The packet.
	 */
	private void updateThisPlayerMovement(GamePacketBuilder packet) {
		/*
		 * Check if the player is teleporting.
		 */
		if (player.didTeleport || player.mapRegionDidChange) {
			/*
			 * They are, so an update is required.
			 */
			packet.putBits(1, 1);

			/*
			 * This value indicates the player teleported.
			 */
			packet.putBits(2, 3);

			/*
			 * This is the new player height.
			 */
			packet.putBits(2, player.getHeightLevel());

			/*
			 * This indicates that the client should discard the walking queue.
			 */
			packet.putBits(1, player.didTeleport ? 1 : 0);

			/*
			 * This flag indicates if an update block is appended.
			 */
			packet.putBits(1, player.updateRequired ? 1 : 0);

			/*
			 * These are the positions.
			 */
			packet.putBits(7,
					player.getPosition().getLocalY(player.getLastKnownRegion()));
			packet.putBits(7,
					player.getPosition().getLocalX(player.getLastKnownRegion()));
		} else /*
				 * Otherwise, check if the player moved.
				 */
		if (player.getPrimaryDirection().toInteger() == -1) {
			/*
			 * The player didn't move. Check if an update is required.
			 */
			if (player.updateRequired) {
				/*
				 * Signifies an update is required.
				 */
				packet.putBits(1, 1);

				/*
				 * But signifies that we didn't move.
				 */
				packet.putBits(2, 0);
			} else
				/*
				 * Signifies that nothing changed.
				 */
				packet.putBits(1, 0);
		} else /*
				 * Check if the player was running.
				 */
		if (player.getSecondaryDirection().toInteger() == -1) {

			/*
			 * The player walked, an update is required.
			 */
			packet.putBits(1, 1);

			/*
			 * This indicates the player only walked.
			 */
			packet.putBits(2, 1);

			/*
			 * This is the player's walking direction.
			 */

			packet.putBits(3, player.getPrimaryDirection().toInteger());

			/*
			 * This flag indicates an update block is appended.
			 */
			packet.putBits(1, player.updateRequired ? 1 : 0);
		} else {

			/*
			 * The player ran, so an update is required.
			 */
			packet.putBits(1, 1);

			/*
			 * This indicates the player ran.
			 */
			packet.putBits(2, 2);

			/*
			 * This is the walking direction.
			 */
			packet.putBits(3, player.getPrimaryDirection().toInteger());

			/*
			 * And this is the running direction.
			 */
			packet.putBits(3, player.getSecondaryDirection().toInteger());

			/*
			 * And this flag indicates an update block is appended.
			 */
			packet.putBits(1, player.updateRequired ? 1 : 0);
		}
	}

}