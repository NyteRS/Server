package com.server2.model.entity.player.packets.impl;

import com.server2.InstanceDistributor;
import com.server2.content.misc.ArmourSets;
import com.server2.content.misc.pets.PetHandler;
import com.server2.content.quests.Christmas;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;
import com.server2.world.NPCManager;

/**
 * Item on npc
 * 
 * @author Rene
 */
public class ItemOnNpc implements Packet {

	@Override
	public void handlePacket(Player client, GamePacket packet) {
		final int itemId = packet.getShortA();
		final int npcIndex = packet.getShortA();

		@SuppressWarnings("unused")
		final int itemSlot = packet.getLEShort();

		if (npcIndex <= 0 || npcIndex >= NPCManager.MAXIMUM_NPCS)
			return;
		final NPC npc = InstanceDistributor.getNPCManager().getNPC(npcIndex);
		/*
		 * if(npc != null) { System.out.println(client.getUsername()+
		 * " used item " +
		 * Server.getItemManager().getItemDefinition(itemId).getName() +
		 * " (ID : " +itemId+ ") with " + npc.getDefinition().getName() +
		 * " (NPC ID : "+npc.getDefinition().getType()+")."); }
		 */
		if (npc != null) {
			if (npc.getDefinition().getType() == 1053 && itemId == 756) {
				client.getHalloweenEvent().fuckThatNPCUp(npc);
				return;
			}
			if (npc.getDefinition().getType() == Christmas.RAT
					&& itemId == Christmas.RATPOISON) {
				Christmas.instance.killRat(client, npc);
				return;
			}
			if (PetHandler.isPetFood(itemId)
					&& PetHandler.isNpcPet(npc.getDefinition().getType()))
				if (client.pet != null) {
					client.pet.feedPet(client, itemId, npcIndex);
					return;
				} else
					client.getActionSender().sendMessage(
							"This is not your pet to feed.");
			if (npc.getDefinition().getType() == 3021)
				if (client.getFarmingTools().noteItem(itemId)) {
				}
			if (npc.getDefinition().getType() == 6970)
				InstanceDistributor.getShardTrading().swapToShards(client,
						itemId);
			if (npc.getDefinition().getType() == 2253)
				InstanceDistributor.getUpgradeSkillCape().upgrade(client,
						itemId);
			if (npc.getDefinition().getType() == 8725)
				InstanceDistributor.getArtifacts().giveReward(client, itemId);
			if (npc.getDefinition().getType() == 8948)
				ArmourSets.exchangeSets(client, itemId);
		}
	}
}
