package com.server2.model.entity.player.packets.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;

/**
 * Idle logout
 * 
 * @author Rene
 */

public class IdleLogout implements Packet {

	@Override
	public void handlePacket(Player client, GamePacket packet) {
		/*
		 * if (client.logoutDelay == 0) { client.getActionSender().sendLogout();
		 * }
		 */
	}

}