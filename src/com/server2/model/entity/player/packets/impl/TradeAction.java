package com.server2.model.entity.player.packets.impl;

import com.server2.Constants;
import com.server2.Settings;
import com.server2.content.JailSystem;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;
import com.server2.world.PlayerManager;

/**
 * Trade actions
 * 
 * @author Rene
 */
public class TradeAction implements Packet {

	public static final int REQUEST = 73, ANSWER = 139;

	@Override
	public void handlePacket(Player client, GamePacket packet) {
		client.setStopRequired();
		/*
		 * if (packet.getOpcode() == REQUEST) { int trade = packet.getLEShort();
		 * if (trade < 0 || trade >= Settings.getInt("sv_maxclients")) return;
		 * if (PlayerManager.getSingleton().getPlayers()[trade] != null) {
		 * Player c = (Player) PlayerManager.getSingleton().getPlayers()[trade];
		 * if (c.tradeStage != 0) { client.getActionSender().sendMessage(
		 * "This user is currently busy."); return; }
		 * client.getTradeHandler().requestTrade(c); }
		 * client.println_debug("Trade Request to: " + trade); } else if
		 * (packet.getOpcode() == ANSWER) { int trade = packet.getLEShort(); if
		 * (trade < 0 || trade >= Settings.getInt("sv_maxclients")) return; if
		 * (PlayerManager.getSingleton().getPlayers()[trade] != null) { Player c
		 * = (Player) PlayerManager.getSingleton().getPlayers()[trade]; //
		 * client.getTradeHandler().answerTrade(c);
		 * client.getTradeHandler().answerTrade(c); }
		 * client.println_debug("Trade Answer to: " + trade); }
		 */
		final int playerIndex = packet.getLEShort();
		if (!Constants.TRADE) {
			client.sendMessage("Trade has currently been disabled.");
			return;
		}
		if (playerIndex < 0 || playerIndex >= Settings.getLong("sv_maxclients"))
			return;
		if (playerIndex == client.getIndex())
			return;
		if (client.inEvent)
			return;
		final Player interactingPlayer = PlayerManager.getSingleton()
				.getPlayers()[playerIndex];
		if (interactingPlayer == null)
			return;
		if (JailSystem.inJail(client)) {
			client.sendMessage("You are in jail and can not trade");
			return;
		}

		client.getTrading().requestTrade(interactingPlayer);
	}
}
