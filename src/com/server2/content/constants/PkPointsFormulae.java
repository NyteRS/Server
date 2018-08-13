package com.server2.content.constants;

import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class PkPointsFormulae {

	/**
	 * Instances the PkPointsFormulae class
	 */
	public static PkPointsFormulae INSTANCE = new PkPointsFormulae();

	/**
	 * Gets the instance
	 */
	public static PkPointsFormulae getInstance() {
		return INSTANCE;
	}

	/**
	 * Returns the randomization value.
	 * 
	 * @param p
	 */
	public int calculateRandomization(Player p) {
		if (p.killCount >= 20 && p.killCount < 50)
			return 2;
		if (p.killCount >= 50 && p.killCount < 75)
			return 3;
		if (p.killCount >= 75 && p.killCount < 150)
			return 4;
		if (p.killCount >= 150 && p.killCount < 300)
			return 5;
		if (p.killCount >= 300 && p.killCount < 500)
			return 6;
		if (p.killCount >= 500 && p.killCount < 800)
			return 7;
		if (p.killCount >= 800 && p.killCount < 1200)
			return 8;
		if (p.killCount >= 1200 && p.killCount < 2000)
			return 9;
		if (p.killCount >= 2000 && p.killCount < 10000)
			return 10;
		if (p.killCount >= 10000)
			return 20;

		return 1;

	}
}
