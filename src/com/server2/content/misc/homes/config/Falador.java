package com.server2.content.misc.homes.config;

import com.server2.model.entity.Location;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Jordon Barber Used for handling the Home teleport for Falador
 * 
 */

public class Falador {

	public static Falador instance = new Falador();
	public static final String locationName = "Falador";

	/**
	 * 
	 * Handles button clicking
	 * 
	 * @param player
	 * @param actionButtonId
	 */

	public void actionClicking(Player player, int actionButtonId) {
		switch (actionButtonId) {

		}
	}

	public final boolean inFalador(Player player) {
		if (player.getAbsX() >= 2940 && player.getAbsX() <= 3065
				&& player.getAbsY() >= 3325 && player.getAbsY() <= 3395)
			return true;
		return false;
	}

	/**
	 * 
	 * Loads the object spawns of location
	 * 
	 * @param Player
	 */

	public void loadObjects(Player player) {
		player.getActionSender().sendObject(4875, 3007, 3368, 0, 0, 10);
		player.getActionSender().sendObject(4874, 3008, 3368, 0, 0, 10);
		player.getActionSender().sendObject(4876, 3009, 3368, 0, 0, 10);
		player.getActionSender().sendObject(4877, 3010, 3368, 0, 0, 10);
		player.getActionSender().sendObject(4878, 3011, 3368, 0, 0, 10);
	}

	/**
	 * 
	 * Handles object clicking
	 * 
	 * @param player
	 * @param objectId
	 * @param objectLocation
	 */

	public void objectClick(int objectId, Location objectLocation, Player player) {
		switch (objectId) {

		}
	}

}