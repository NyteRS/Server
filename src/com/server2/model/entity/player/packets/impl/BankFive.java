package com.server2.model.entity.player.packets.impl;

import com.server2.content.minigames.DuelArena;
import com.server2.content.skills.crafting.GemCrafting;
import com.server2.content.skills.smithing.SmithingMakeItem;
import com.server2.model.Item;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;

/**
 * Bank ten items
 * 
 * @author Rene
 */
public class BankFive implements Packet {

	@Override
	public void handlePacket(Player client, GamePacket packet) {
		final int interfaceID = packet.getLEShortA();
		final int removeID = packet.getLEShortA();
		final int removeSlot = packet.getLEShort();
		client.setStopRequired();
		if (interfaceID == 5064)
			Player.getContainerAssistant().bankItem(client, removeID,
					removeSlot, 5);
		else if (interfaceID == 1119 || interfaceID == 1120
				|| interfaceID == 1121 || interfaceID == 1122
				|| interfaceID == 1123)
			new SmithingMakeItem(client, removeID, 5);
		else if (interfaceID == 5382)
			Player.getContainerAssistant().fromBank(client, removeID,
					removeSlot, 5);
		else if (interfaceID == 7423) {
			Player.getContainerAssistant().bankItem(client, removeID,
					removeSlot, 5);
			client.getActionSender().sendItemReset(7423);
		} else if (interfaceID == 3823)
			Player.getContainerAssistant().sellItem(client, removeID,
					removeSlot, 1);
		else if (interfaceID == 3900)
			Player.getContainerAssistant().buyItem(client, removeID,
					removeSlot, 1);
		else if (interfaceID == 3322) {
			if (client.isDuelInterfaceOpen())
				DuelArena.getInstance().stakeItem(client, removeSlot, removeID,
						5);
			else {
				final int theSlot = client.playerItems[removeSlot] - 1;
				if (theSlot == -1)
					return;
				if (Item.itemStackable[theSlot] || Item.itemIsNote[theSlot])
					client.getTrading().tradeItem(removeID, removeSlot, 5);
				else
					for (int i = 0; i < 5; i++)
						if (client.getActionAssistant().playerHasItem(removeID,
								1))
							client.getTrading().tradeItem(
									removeID,
									client.getActionAssistant().getItemSlot(
											removeID), 1);
			}
		} else if (interfaceID == 3415)
			client.getTrading().fromTrade(removeID, 5);
		else if (interfaceID == 6669)
			DuelArena.getInstance().removeStakedItem(client, removeSlot,
					removeID, 5);
		else if (interfaceID == 4233 || interfaceID == 4239
				|| interfaceID == 4245)
			for (final int[] element : GemCrafting.ITEMS)
				if (element[0] == removeID)
					GemCrafting.startCrafter(client, element[1], 5, element[2]);

	}

}
