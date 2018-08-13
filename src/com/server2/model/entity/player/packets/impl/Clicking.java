package com.server2.model.entity.player.packets.impl;

import com.server2.content.minigames.DuelArena;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.Player.DuelStage;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;
import com.server2.util.Areas;

/**
 * Clicking
 * 
 * @author Rene
 */
public class Clicking implements Packet {

	@Override
	public void handlePacket(Player client, GamePacket packet) {

		if (!client.getDuelStage().equals(DuelStage.WAITING)
				&& Areas.isInDuelArena(client.getCoordinates())
				&& !client.getDuelStage().equals(DuelStage.INSIDE_DUEL))
			DuelArena.getInstance().declineDuel(client);
		client.cancelTasks();

	}
}