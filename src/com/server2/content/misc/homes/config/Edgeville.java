package com.server2.content.misc.homes.config;

import com.server2.InstanceDistributor;
import com.server2.content.skills.thieving.HomeThieving;
import com.server2.model.entity.Location;
import com.server2.model.entity.player.Player;
import com.server2.world.objects.ObjectConstants;

/**
 * 
 * @author Jordon Barber Used for handling the Home teleport for Edgeville
 * 
 */

public class Edgeville {

	public static Edgeville instance = new Edgeville();
	public static final String locationName = "Edgeville";

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

	/**
	 * 
	 * Loads the object spawns of location
	 * 
	 * @param Player
	 */

	public void loadObjects(Player player) {
		/*
		 * Altars
		 */
		player.getActionSender().sendObject(411, 3091, 3506, 0, 0, 10);
		player.getActionSender().sendObject(409, 3093, 3506, 0, 0, 10);
		player.getActionSender().sendObject(6552, 3095, 3506, 0, 0, 10);
		player.getActionSender().sendObject(14000, 2327, 3635, 0, -3, 10);
		player.getActionSender().sendObject(17010, 3073, 3504, 0, -2, 10);
		/*
		 * Recipie for Disaster
		 */
		player.getActionSender().sendObject(12356, 3100, 3506, 0, -3, 10);
		/*
		 * Thieving Stalls
		 */
		player.getActionSender().sendObject(4875, 3095, 3500, 0, 0, 10);
		player.getActionSender().sendObject(4874, 3096, 3500, 0, 0, 10);
		player.getActionSender().sendObject(4876, 3097, 3500, 0, 0, 10);
	}

	/**
	 * 
	 * Handles object clicking
	 * 
	 * @param objectId
	 * @param objectLocation
	 * @param player
	 */

	public void objectClick(int objectId, Location objectLocation, Player player) {
		switch (objectId) {
		case ObjectConstants.generalstall:
			HomeThieving.getInstance().thieveStall(player, 0);
			break;
		case ObjectConstants.homeThieving2:
			HomeThieving.getInstance().thieveStall(player, 1);
			break;
		case ObjectConstants.homeThieving3:
			HomeThieving.getInstance().thieveStall(player, 2);
			break;
		case ObjectConstants.rfdPortal:
			if (player.rfdProgress > 0)
				InstanceDistributor.getRecipeForDisaster().startRFD(player);
			else
				player.getActionSender().sendMessage(
						"You need to talk to Gypsy first.");
			break;
		case ObjectConstants.rfdChest:
			InstanceDistributor.getRecipeForDisaster().openRFDShop(player);
			break;
		}
	}

}