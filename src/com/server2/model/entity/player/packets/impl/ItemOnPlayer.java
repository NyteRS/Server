package com.server2.model.entity.player.packets.impl;

import com.server2.Settings;
import com.server2.content.skills.slayer.DuoSlayer;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;
import com.server2.world.PlayerManager;

/**
 * 
 * @author Rene
 * 
 */
public class ItemOnPlayer implements Packet {

	@Override
	public void handlePacket(Player client, GamePacket packet) {
		try {
			final int playerId = packet.getShort();
			final int z = packet.getLEShort();
			if (z >= 28 || z < 0)
				return;
			final int itemId = client.playerItems[z] - 1;
			if (System.currentTimeMillis() - client.lastSlayerInvite < 60000) {
				client.getActionSender().sendMessage(
						"You have to wait 1 minute before doing this again.");
				return;
			}
			if (client.inWilderness()) {
				client.getActionSender()
						.sendMessage("You cannot do this here.");
				return;
			}

			if (itemId == 4155) {
				client.lastSlayerInvite = System.currentTimeMillis();
				for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
					final Player p = PlayerManager.getSingleton().getPlayers()[i];
					if (p == null)
						continue;
					if (!p.isActive || p.disconnected)
						continue;

					if (p.getIndex() == playerId) {
						if (p.dialogueAction > 0) {
							client.getActionSender().sendMessage(
									"This player is currently busy.");
							return;
						}
						if (client == null || p == null)
							return;
						if (p.duoPartner == null)
							DuoSlayer.getInstance().invite(client, p);
						else if (p.getDuoPartner() == client)
							client.getActionSender().sendMessage(
									"You are already doing a slayer duo with "
											+ p.getUsername() + ".");
						else
							client.getActionSender()
									.sendMessage(
											"Your partner already has a slayer dual partner.");
					}

				}

			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
