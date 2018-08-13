package com.server2.model.entity.player.packets.impl;

import java.util.Date;

import com.server2.InstanceDistributor;
import com.server2.model.combat.CombatEngine;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;
import com.server2.util.Logger;
import com.server2.util.Misc;

/**
 * Pickup item packet
 * 
 * @author Rene
 */
public class PickupItem implements Packet {

	@Override
	public void handlePacket(Player client, GamePacket packet) {
		final int itemY = packet.getLEShort();
		final int itemID = packet.getShort();
		final int itemX = packet.getLEShort();
		if (client.isWalkfix())
			return;
		client.setStopRequired();
		client.println_debug("pickupItem: " + itemX + "," + itemY + " itemID: "
				+ itemID);
		client.pickup[0] = itemX;
		client.pickup[1] = itemY;
		client.pickup[2] = itemID;
		if (InstanceDistributor.getItemManager().getItemDefinition(itemID) != null) {
			final String name = InstanceDistributor.getItemManager()
					.getItemDefinition(itemID).getName();
			if (name != null) {
				final Date date = new Date();
				Logger.write(
						"["
								+ date
								+ "] "
								+ Misc.capitalizeFirstLetter(client
										.getUsername()) + " ["
								+ client.connectedFrom + "] picked up a "
								+ name + ", location: " + itemX + ", " + itemY,
						"pickup/" + client.getUsername());
			}
		}

		CombatEngine.resetAttack(client, false);
	}

}
