package com.server2.model.entity.player.packets.impl;

/**
 * Remove items     
 *
 * @author Rene
 *
 **/

import com.server2.InstanceDistributor;
import com.server2.content.minigames.DuelArena;
import com.server2.content.misc.GeneralStore;
import com.server2.content.skills.crafting.GemCrafting;
import com.server2.content.skills.smithing.SmithingMakeItem;
import com.server2.model.Shop;
import com.server2.model.Shop.Type;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;
import com.server2.util.Misc;

public class Remove implements Packet {

	@Override
	public void handlePacket(Player client, GamePacket packet) {
		final int interfaceID = packet.getShortA();
		final int removeSlot = packet.getShortA();
		final int removeID = packet.getShortA();

		if (removeID > 23000)
			return;
		if (InstanceDistributor.getItemManager().getItemDefinition(removeID) == null
				&& removeID != 20935)
			return;
		if (interfaceID == 1688) {
			if (client.playerEquipment[removeSlot] > 0)
				client.getEquipment().removeItem(removeID, removeSlot);
		} else if (interfaceID == 5064)
			Player.getContainerAssistant().bankItem(client, removeID,
					removeSlot, 1);
		else if (interfaceID == 1119 || interfaceID == 1120
				|| interfaceID == 1121 || interfaceID == 1122
				|| interfaceID == 1123)
			new SmithingMakeItem(client, removeID, 1);
		else if (interfaceID == 5382)
			Player.getContainerAssistant().fromBank(client, removeID,
					removeSlot, 1);
		else if (interfaceID == 7423) {
			Player.getContainerAssistant().bankItem(client, removeID,
					removeSlot, 1);
			client.getActionSender().sendItemReset(7423);
		} else if (interfaceID == 15682 || interfaceID == 15683)
			client.getFarmingTools().withdrawItems(removeID, 1);
		else if (interfaceID == 15594 || interfaceID == 15595)
			client.getFarmingTools().storeItems(removeID, 1);
		else if (interfaceID == 3823) {

			final String name = InstanceDistributor.getItemManager()
					.getItemDefinition(removeID).getName();

			if (!client.getExtraData().containsKey("shop"))
				return;

			final int shop = (Integer) client.getExtraData().get("shop");

			final Shop s = InstanceDistributor.getShopManager().getShops()
					.get(shop);

			if (s == null)
				return;

			final String currency = InstanceDistributor.getItemManager()
					.getItemDefinition(s.getCurrency()).getName();
			int value = s.getItemSellValue(removeID);
			if (s.getType() == Type.GENERAL)
				value = (int) (GeneralStore.getInstance().itemPrice(removeID) * 0.9);
			client.getActionSender().sendMessage(
					name + ": shop will buy for: " + value + " " + currency
							+ Misc.formatAmount(value));

		} else if (interfaceID == 3900) {

			if (InstanceDistributor.getItemManager()
					.getItemDefinition(removeID) == null) {
				client.getActionSender().sendMessage(
						"This itemDefinition is not set, please report to Rene : "
								+ removeID);

				return;
			}
			final String name = InstanceDistributor.getItemManager()
					.getItemDefinition(removeID).getName();
			if (!client.getExtraData().containsKey("shop"))
				return;

			final int shop = (Integer) client.getExtraData().get("shop");
			final Shop s = InstanceDistributor.getShopManager().getShops()
					.get(shop);
			if (s == null)
				return;

			int value = s.getItemBuyValue(removeID);
			if (s.getType() == Type.GENERAL)
				value = GeneralStore.getInstance().itemPrice(removeID);
			if (removeID == 4155)
				value = 0;
			if (value == 0)
				value = 1;
			String currency = InstanceDistributor.getItemManager()
					.getItemDefinition(s.getCurrency()).getName();
			if (s.getCurrency() == 123)
				currency = "Slayer points";
			if (s.getCurrency() == 125)
				currency = "Voting points";
			if (s.getCurrency() == 127)
				currency = "Pest Control points";
			if (s.getCurrency() == 128)
				currency = "Tokens";
			if (s.getCurrency() == 10)
				currency = "Duo Points";
			if (s.getCurrency() == 200)
				currency = "PK-Tokens";

			client.getActionSender().sendMessage(
					name + ": currently costs " + value + " " + currency + "."
							+ Misc.formatAmount(value));
		} else if (interfaceID == 3322) {
			if (client.isDuelInterfaceOpen())
				DuelArena.getInstance().stakeItem(client, removeSlot, removeID,
						1);
			else
				client.getTrading().tradeItem(removeID, removeSlot, 1);
		} else if (interfaceID == 3415)
			client.getTrading().fromTrade(removeID, 1);
		else if (interfaceID == 6669)
			DuelArena.getInstance().removeStakedItem(client, removeSlot,
					removeID, 1);
		else if (interfaceID == 4233 || interfaceID == 4239
				|| interfaceID == 4245)
			for (final int[] element : GemCrafting.ITEMS)
				if (element[0] == removeID)
					GemCrafting.startCrafter(client, element[1], 1, element[2]);
	}
}