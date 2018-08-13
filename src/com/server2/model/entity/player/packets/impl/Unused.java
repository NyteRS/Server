package com.server2.model.entity.player.packets.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;

/**
 * Unused packet
 * 
 * @author Rene
 */

public class Unused implements Packet {

	@Override
	public void handlePacket(Player client, GamePacket packet) {

	}

}