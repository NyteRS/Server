package com.server2.content.misc.mobility;

import com.server2.model.entity.Entity;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class TeleportationHandler {

	/**
	 * Adds a single Teleportation request.
	 * 
	 * @param teleporter
	 */

	public static void addNewRequest(Entity teleporter, int x, int y, int z,
			int delay) {

		if (teleporter == null)
			return;

		if (teleporter instanceof NPC)
			((NPC) teleporter).getNPCTeleportHandler().teleportNpc(x, y, z,
					delay);
		else
			((Player) teleporter).getPlayerTeleportHandler()
					.forceDelayTeleport(x, y, z, delay);
	}

}
