package com.server2.model.entity.player.packets.impl;

import com.server2.content.minigames.DuelArena;
import com.server2.model.Item;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;

/**
 * Bank all items packet
 * 
 * @author Rene
 */
public class BankAll implements Packet {

	@Override
	public void handlePacket(Player client, GamePacket packet) {
		final int removeSlot = packet.getShortA();
		final int interfaceID = packet.getShortA();
		final int removeID = packet.getShortA();
		client.setStopRequired();
		if (interfaceID == 4936) {

			if (Item.itemStackable[removeID])
				Player.getContainerAssistant().bankItem(client,
						client.playerItems[removeSlot], removeSlot,
						client.playerItemsN[removeSlot]);
			else
				Player.getContainerAssistant().bankItem(
						client,
						client.playerItems[removeSlot],
						removeSlot,
						client.getActionAssistant().getItemAmount(
								client.playerItems[removeSlot] - 1));
		} else if (interfaceID == 5510)
			Player.getContainerAssistant().fromBank(client,
					client.bankItems[removeSlot], removeSlot,
					client.bankItemsN[removeSlot]);
		else if (interfaceID == 7295) {

			if (Item.itemStackable[removeID]) {

				Player.getContainerAssistant().bankItem(client,
						client.playerItems[removeSlot], removeSlot,
						client.playerItemsN[removeSlot]);
				client.getActionSender().sendItemReset(7423);
			} else {

				Player.getContainerAssistant().bankItem(
						client,
						client.playerItems[removeSlot],
						removeSlot,
						client.getActionAssistant().getItemAmount(
								client.playerItems[removeSlot] - 1));
				client.getActionSender().sendItemReset(7423);
			}

		} else if (interfaceID == 3695)
			Player.getContainerAssistant().sellItem(client, removeID,
					removeSlot, 10);
		else if (interfaceID == 4028)
			Player.getContainerAssistant().buyItem(client, removeID,
					removeSlot, 10);
		else if (interfaceID == 3823)
			Player.getContainerAssistant().sellItem(client, removeID,
					removeSlot, 10);
		else if (interfaceID == 3900)
			Player.getContainerAssistant().buyItem(client, removeID,
					removeSlot, 10);
		else if (interfaceID == 3194) {
			if (client.isDuelInterfaceOpen())
				DuelArena.getInstance().stakeItem(client, removeSlot, removeID,
						client.getActionAssistant().getItemAmount(removeID));
			else {
				final int amount = client.getActionAssistant().getItemAmount(
						removeID);
				client.getTrading().tradeItem(removeID, removeSlot, amount);
			}
		} else if (interfaceID == 3543)
			client.getTrading().fromTrade(removeID, Integer.MAX_VALUE);
		else if (interfaceID == 6797)
			DuelArena.getInstance().removeStakedItem(client, removeSlot,
					removeID, client.getDuel().getCount(removeID));
	}
}
