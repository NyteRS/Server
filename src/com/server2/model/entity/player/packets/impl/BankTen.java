package com.server2.model.entity.player.packets.impl;

import com.server2.content.minigames.DuelArena;
import com.server2.content.skills.crafting.GemCrafting;
import com.server2.content.skills.smithing.SmithingMakeItem;
import com.server2.model.Item;
import com.server2.model.combat.additions.DragonFireLoader;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;

/**
 * Bank five items
 * 
 * @author Rene
 */
public class BankTen implements Packet {

	@Override
	public void handlePacket(Player client, GamePacket packet) {
		final int interfaceID = packet.getLEShort();
		final int removeID = packet.getShortA();
		final int removeSlot = packet.getShortA();

		client.setStopRequired();
		if (interfaceID == 51347)
			Player.getContainerAssistant().bankItem(client, removeID,
					removeSlot, 10);
		else if (interfaceID == 39046) {
			if (removeID == 11283)
				DragonFireLoader.getInstance().handleDFS(client);
		} else if (interfaceID == 24452 || interfaceID == 24708
				|| interfaceID == 24964 || interfaceID == 25220
				|| interfaceID == 25476)
			new SmithingMakeItem(client, removeID, 10);
		else if (interfaceID == 5382)
			Player.getContainerAssistant().fromBank(client, removeID,
					removeSlot, 10);
		else if (interfaceID == 3900)
			Player.getContainerAssistant().buyItem(client, removeID,
					removeSlot, 5);
		else if (interfaceID == 3823)
			Player.getContainerAssistant().sellItem(client, removeID,
					removeSlot, 5);
		else if (interfaceID == 64140) {
			if (client.isDuelInterfaceOpen())
				DuelArena.getInstance().stakeItem(client, removeSlot, removeID,
						10);
		} else if (interfaceID == 3482)
			DuelArena.getInstance().removeStakedItem(client, removeSlot,
					removeID, 10);
		else if (interfaceID == 22413)
			client.getTrading().tradeItem(removeID, removeSlot, 10);
		else if (interfaceID == 5064) {
			Player.getContainerAssistant().bankItem(client, removeID,
					removeSlot, 10);
			client.getActionSender().sendItemReset(7423);
		} else if (interfaceID == 3900)
			Player.getContainerAssistant().buyItem(client, removeID,
					removeSlot, 5);
		else if (interfaceID == 35216 || interfaceID == 36752
				|| interfaceID == 38288)
			for (final int[] element : GemCrafting.ITEMS)
				if (element[0] == removeID)
					GemCrafting
							.startCrafter(client, element[1], 10, element[2]);
		switch (interfaceID) {
		case 3322:
			if (Item.itemStackable[removeID] || Item.itemIsNote[removeID])
				client.getTrading().tradeItem(removeID, removeSlot, 10);
			else
				for (int i = 0; i < 10; i++)
					client.getTrading().tradeItem(removeID,
							client.getActionAssistant().getItemSlot(removeID),
							1);
			break;
		case 3415:
			client.getTrading().fromTrade(removeID, 10);
			break;
		}
	}

}
