package com.server2.model.entity.player.packets.impl;

/**
 * Wear item
 *
 * @author Rene
 *
 *
 */
import com.server2.InstanceDistributor;
import com.server2.content.anticheat.WearItemCheating;
import com.server2.content.misc.ShardTrading;
import com.server2.content.skills.crafting.EssencePouches;
import com.server2.model.entity.npc.NPCDefinition;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;

public class Wear implements Packet {

	@Override
	public void handlePacket(Player client, GamePacket packet) {
		final int wearID = packet.getShort();
		final int wearSlot = packet.getShortA();
		@SuppressWarnings("unused")
		final int interfaceID = packet.getShortA();
		if (client.isBeingForcedToWalk())
			// Busy walking
			return;
		if (wearID > 22213 || wearID < 0)
			return;
		if (client.inEvent)
			return;
		if (!client.getActionAssistant().playerHasItem(wearID, 1))
			return;
		if (InstanceDistributor.getItemManager().getItemDefinition(wearID) == null)
			return;
		if (!WearItemCheating.canWearThatItem(client, wearID))
			return;
		if (wearID == 5509 || wearID == 5511 || wearID == 5512
				|| wearID == 5515) {
			EssencePouches.empty(client, wearID);
			return;
		}
		if (wearID == 15262) {
			ShardTrading.openShardPack(client);
			return;
		}

		if (wearID == 4155) {
			if (client.slayerTask != -1) {
				final NPCDefinition def = InstanceDistributor.getNPCManager().npcDefinitions
						.get(client.slayerTask);
				client.getActionSender().sendMessage(
						"You have to slay " + client.slayerTaskAmount + " "
								+ def.getName() + "s. to complete your task.");
			} else
				client.getActionSender()
						.sendMessage(
								"You don't have a slayer task, please visit a slayer master to get one.");
			return;
		}
		if (client.hitpoints > 0 && !client.isBusy())
			if (client.tradingWith == -1)
				client.getEquipment().wearItem(wearID, wearSlot);

		client.setStopRequired();

	}
}