package com.server2.content.actions;

import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.GroundItem;
import com.server2.model.entity.player.Player;
import com.server2.world.GroundItemManager;
import com.server2.world.map.tile.TileManager;

/**
 * 
 * @author Rene Roosen
 * 
 */

public class ItemPickup {

	/**
	 * Performs Walking to a drop and eventually picking it up.
	 * 
	 * @param p
	 */
	public static void init(Player p) {
		if (p.pickup[0] < 1)
			p.resetFaceDirection();

		final GroundItem groundItem = GroundItemManager.getInstance()
				.getGroundItem(p, p.pickup[2], p.pickup[0], p.pickup[1]);
		if (groundItem == null || groundItem.isRemoved())
			return;
		if (TileManager.calculateDistance(p.pickup, p) == 0) {
			if (p.getActionSender().addItem(groundItem.getItem().getId(),
					groundItem.getItem().getCount()))
				groundItem.setRemoved();
			for (int pickupReset = 0; pickupReset < p.pickup.length; pickupReset++)
				p.pickup[pickupReset] = -1;
		} else if (TileManager.calculateDistance(p.pickup, p) == 1
				&& p.getFreezeDelay() > 0) {
			if (p.getActionSender().addItem(groundItem.getItem().getId(),
					groundItem.getItem().getCount()))
				groundItem.setRemoved();
			AnimationProcessor.addNewRequest(p, 832, 1);
			for (int pickupReset = 0; pickupReset < p.pickup.length; pickupReset++)
				p.pickup[pickupReset] = -1;
		}

	}
}
