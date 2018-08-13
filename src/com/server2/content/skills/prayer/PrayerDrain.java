package com.server2.content.skills.prayer;

import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene
 * 
 */

public class PrayerDrain {

	/**
	 * Handles Prayer Draining
	 * 
	 * @param client
	 */
	public static void init(Player client) {
		if (client.prayerActivated)
			client.getPrayerHandler().prayerEvent();

	}
}
