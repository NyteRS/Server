package com.server2.model.entity.player.packets.impl;

import com.server2.content.TradingConstants;
import com.server2.content.minigames.DuelArena;
import com.server2.content.skills.smithing.SmithingMakeItem;
import com.server2.model.Item;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;
import com.server2.net.GamePacketBuilder;

/**
 * Bank x items packets
 * 
 * @author Rene
 */
public class BankX implements Packet {

	public static final int PART1 = 135, PART2 = 208;

	@Override
	public void handlePacket(Player client, GamePacket packet) {
		client.setStopRequired();
		if (packet.getOpcode() == PART1) {
			client.setBankXremoveSlot(packet.getLEShort());
			client.setBankXinterfaceID(packet.getShortA());
			client.setBankXremoveID(packet.getLEShort());
			if (client.getBankXinterfaceID() != 3900)
				client.write(new GamePacketBuilder(27).toPacket());
		}
		if (client.getBankXinterfaceID() == 3900) {
			Player.getContainerAssistant()
					.buyItem(client, client.getBankXremoveID(),
							client.getBankXremoveSlot(), 100);// 100
			return;
		}
		if (packet.getOpcode() == PART2) {
			final int bankXamount = packet.getInt();
			if (bankXamount < 1 || bankXamount > Integer.MAX_VALUE - 1)
				return;
			if (client.getBankXinterfaceID() == 0) {
				if (client.getVault().getStoring()) {
					client.getVault().deposit(bankXamount, false);
					return;

				}
				if (client.getVault().getRemoving()) {
					client.getVault().withdraw(bankXamount);
					return;
				}
			}
			if (client.getBankXinterfaceID() == 5064)
				Player.getContainerAssistant().bankItem(client,
						client.playerItems[client.getBankXremoveSlot()],
						client.getBankXremoveSlot(), bankXamount);
			else if (client.getBankXinterfaceID() == 24452
					|| client.getBankXinterfaceID() == 24708
					|| client.getBankXinterfaceID() == 24964
					|| client.getBankXinterfaceID() == 25220
					|| client.getBankXinterfaceID() == 25476)
				new SmithingMakeItem(client, client.getBankXremoveID(),
						bankXamount);
			else if (client.getBankXinterfaceID() == 5382)
				Player.getContainerAssistant().fromBank(client,
						client.bankItems[client.getBankXremoveSlot()],
						client.getBankXremoveSlot(), bankXamount);
			else if (client.getBankXinterfaceID() == 7423) {
				Player.getContainerAssistant().bankItem(client,
						client.playerItems[client.getBankXremoveSlot()],
						client.getBankXremoveSlot(), bankXamount);
				client.getActionSender().sendItemReset(7423);

			} else if (client.getBankXinterfaceID() == 3322) {
				if (client.isDuelInterfaceOpen())
					DuelArena.getInstance().stakeItem(client,
							client.getBankXremoveSlot(),
							client.getBankXremoveID(), bankXamount);
				else {
					final int slot = client.playerItems[client
							.getBankXremoveSlot()] - 1;
					if (slot == -1)
						return;
					if (TradingConstants.isUntradable(slot)) {
						client.getActionSender().sendMessage(
								"That item is untradable!");
						return;
					}
					if (Item.itemStackable[slot] || Item.itemIsNote[slot])
						client.getTrading().tradeItem(
								client.getBankXremoveID(),
								client.getBankXremoveSlot(), bankXamount);
					else
						for (int i = 0; i < bankXamount; i++)
							if (client.getActionAssistant().playerHasItem(
									client.getBankXremoveID(), 1))
								client.getTrading()
										.tradeItem(
												client.getBankXremoveID(),
												client.getActionAssistant()
														.getItemSlot(
																client.getBankXremoveID()),
												1);
				}
			} else if (client.getBankXinterfaceID() == 3415)
				/*
				 * if(Item.itemStackable[client.getBankXremoveID() - 1]||
				 * Item.itemIsNote[client.getBankXremoveID() - 1]) {
				 * client.getTradeHandler
				 * ().fromTrade(client.getTradeHandler().getOffer
				 * ()[client.getBankXremoveSlot()] - 1,
				 * client.getBankXremoveSlot(), bankXamount); } else { for(int i
				 * = 0; i < bankXamount; i++) {
				 * client.getTradeHandler().fromTrade(client.getBankXremoveID(),
				 * client
				 * .getTradeHandler().getSlotForItem(client.getBankXremoveID() +
				 * 1), 1); } }
				 */
				client.getActionSender().sendMessage(
						"Trade-X Has been temporarily disabled.");
			else if (client.getBankXinterfaceID() == 6669)
				DuelArena.getInstance().removeStakedItem(client,
						client.getBankXremoveSlot(), client.getBankXremoveID(),
						bankXamount);
			else if (client.getBankXinterfaceID() == 3900)
				Player.getContainerAssistant().buyItem(client,
						client.getBankXremoveID(), client.getBankXremoveSlot(),
						bankXamount);
		}
	}

}
