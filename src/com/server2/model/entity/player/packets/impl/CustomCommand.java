package com.server2.model.entity.player.packets.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.CommandManager;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;

/**
 * Handles custom commands
 * 
 * @author Rene
 */
public class CustomCommand implements Packet {

	@Override
	public void handlePacket(Player client, GamePacket packet) {
		final String playerCommand = packet.getRS2String();
		client.println_debug("playerCommand: " + playerCommand);
		CommandManager.execute(client, playerCommand);
	}

}