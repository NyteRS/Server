package com.server2.model.combat.additions;

import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class PrayerDrainingBonus {

	public static double calculatePrayerBonus(Player client) {
		double prayerBonus = 0;
		if (client.getBonuses().bonus[11] > 0)
			prayerBonus = client.getBonuses().bonus[11];

		return prayerBonus;
	}
}
