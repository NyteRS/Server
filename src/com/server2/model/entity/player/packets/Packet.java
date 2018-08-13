package com.server2.model.entity.player.packets;

import com.server2.model.entity.player.Player;
import com.server2.net.GamePacket;

/**
 * Packet interface.
 * 
 * @author Graham
 */
public interface Packet {

	public void handlePacket(Player client, GamePacket packet);

}