package com.server2.model.entity.player.packets.impl;

import com.server2.InstanceDistributor;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;
import com.server2.world.GroundItemManager;
import com.server2.world.objects.ObjectSystem;

/**
 * Loading complete packet.
 * 
 * @author Rene
 */
public class RegionChanging implements Packet {

	public static final int GAME_LOAD = 121, AREA_LOAD = 210;

	@Override
	public void handlePacket(final Player client, GamePacket packet) {

		ObjectSystem.getInstance().loadObjects(client);
		client.getMusicHandler().handleMusic();
		InstanceDistributor.getObjectManager().reloadObjects(client);
		GroundItemManager.getInstance().loadRegion(client);
	}
}
