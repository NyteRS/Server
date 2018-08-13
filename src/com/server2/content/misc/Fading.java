package com.server2.content.misc;

import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class Fading {

	/**
	 * Instances the Fading class
	 */
	private static Fading INSTANCE = new Fading();

	/**
	 * Gets the instance
	 */
	public static Fading getInstance() {
		return INSTANCE;
	}

	/**
	 * Sets the fading
	 * 
	 * @param client
	 */
	public void setFading(Player client, int x, int y) {
		client.fadeX = x;
		client.fadeY = y;
		client.tStage = 10;
		client.tTime2 = 0;
	}

}
