package com.server2.model.entity.player.packets.impl;

import com.server2.InstanceDistributor;
import com.server2.content.JailSystem;
import com.server2.content.WalkToNPC;
import com.server2.content.misc.pets.PetHandler;
import com.server2.content.skills.hunter.ImplingHunting;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;

/**
 * Click NPC packet.
 * 
 * @author Rene
 */
public class ClickNPC implements Packet {
	// 155 17 14

	public static final int FIRST_CLICK = 155, SECOND_CLICK = 17,
			THIRD_CLICK = 21;

	private void handleFirstClick(NPC npc, Player client) {
		if (PetHandler.isNpcPet(npc.getDefinition().getType())) {
			if (client.pet != null)
				client.pet.pickUpPet(client);
			return;
		}
	}

	@Override
	public void handlePacket(Player client, GamePacket packet) {
		Entity npc;
		if (client.doingTutorial) {
			client.getActionSender().sendMessage(
					"You cannot walk during the tutorial.");
			return;
		}
		if (JailSystem.inJail(client))
			return;
		int clickType = 0;
		switch (packet.getOpcode()) {
		case FIRST_CLICK:
			npc = InstanceDistributor.getNPCManager().getNPC(
					packet.getLEShort());
			clickType = 1;
			break;

		case SECOND_CLICK:
			npc = InstanceDistributor.getNPCManager().getNPC(
					packet.getLEShortA());
			clickType = 2;
			break;

		case THIRD_CLICK:
			npc = InstanceDistributor.getNPCManager().getNPC(packet.getShort());// packet.readUnsignedWordA()
			clickType = 3;
			break;

		default:
			npc = null;
			break;
		}
		client.setStopRequired();
		if (npc == null)
			return;
		client.lastClickedNpc = ((NPC) npc).getNpcId();
		AnimationProcessor.face(client, npc);
		WalkToNPC.setDestination(client, (NPC) npc,
				WalkToNPC.getNpcWalkType((NPC) npc, clickType));
		switch (((NPC) npc).getDefinition().getType()) {
		case 6055:
		case 6056:
		case 6057:
		case 6058:
		case 6059:
		case 6060:
		case 6061:
		case 6062:
		case 6063:
		case 6064:
			ImplingHunting.CatchImpling(client, ((NPC) npc).getDefinition()
					.getType());
			break;
		case 5085:
		case 5084:
		case 5083:
		case 5082:
			ImplingHunting.CatchButterfly(client, ((NPC) npc).getDefinition()
					.getType());
			break;
		}

		switch (clickType) {
		case 1:
			handleFirstClick((NPC) npc, client);
			break;
		case 2:
			handleSecondClick((NPC) npc, client);
			break;
		case 3:
			handleThirdClick((NPC) npc, client);
			break;
		default:
			handleSpecialClick((NPC) npc, client);
			break;
		}
	}

	private void handleSecondClick(NPC npc, Player client) {
		if (PetHandler.isNpcPet(npc.getDefinition().getType())) {
			if (client.pet != null)
				client.pet.talkToPet(client, npc);
			return;
		}
	}

	private void handleSpecialClick(NPC npC, Player client) {
		// throw new UnsupportedOperationException("Not yet implemented");
	}

	private void handleThirdClick(NPC npc, Player client) {
		if (PetHandler.isNpcPet(npc.getDefinition().getType())) {
			if (client.pet != null)
				client.pet.interactWithPet(client);
			return;
		}
	}
}
