package com.server2.content.misc.homes.config;

import com.server2.content.misc.mobility.TeleportationHandler;
import com.server2.model.entity.Location;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.world.objects.ObjectConstants;
import com.server2.world.objects.ObjectSystem;

/**
 * 
 * @author Jordon Barber Used for handling the Home teleport for Keldagrim
 * 
 */

public class Keldagrim {

	public static Keldagrim instance = new Keldagrim();// it's about money,
														// ignore pls
	public static final String locationName = "Keldagrim";

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

	public final boolean inKeldagrim(Player player) {
		if (player.getAbsX() >= 2800 && player.getAbsX() <= 2950
				&& player.getAbsY() >= 10150 && player.getAbsY() <= 10240)
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
		player.getActionSender().sendObject(3192, 2842, 10184, 0, 0, 10);
		/*
		 * Thieving Stalls
		 */
		player.getActionSender().sendObject(6163, 2842, 10221, 0, 0, 10);
		player.getActionSender().sendObject(6165, 2843, 10221, 0, 0, 10);
		player.getActionSender().sendObject(6166, 2844, 10221, 0, 0, 10);
		player.getActionSender().sendObject(6164, 2845, 10221, 0, 0, 10);
		player.getActionSender().sendObject(6162, 2846, 10221, 0, 0, 10);
		/*
		 * Woodcutting Trees
		 */
		// Magic Tree
		player.getActionSender().sendObject(1306, 2906, 10233, 0, 0, 10);
		// Yew Tree
		player.getActionSender().sendObject(1309, 2909, 10233, 0, 0, 10);
		player.getActionSender().sendObject(1309, 2901, 10234, 0, 0, 10);
		// Maple Tree
		player.getActionSender().sendObject(1307, 2913, 10231, 0, 0, 10);
		player.getActionSender().sendObject(1307, 2898, 10232, 0, 0, 10);
		// Willow Tree
		player.getActionSender().sendObject(1308, 2906, 10229, 0, 0, 10);
		player.getActionSender().sendObject(1308, 2895, 10234, 0, 0, 10);
		player.getActionSender().sendObject(1308, 2892, 10234, 0, 0, 10);
		// Oak Tree
		player.getActionSender().sendObject(1281, 2909, 10228, 0, 0, 10);
		player.getActionSender().sendObject(1281, 2919, 10224, 0, 0, 10);
		// Tree
		player.getActionSender().sendObject(1276, 2908, 10225, 0, 0, 10);
		player.getActionSender().sendObject(1276, 2913, 10227, 0, 0, 10);
		player.getActionSender().sendObject(1276, 2906, 10221, 0, 0, 10);
		/*
		 * Mining Rocks
		 */
		// Rune
		player.getActionSender().sendObject(2107, 2890, 10201, 0, 0, 10);
		player.getActionSender().sendObject(2107, 2890, 10195, 0, 0, 10);
		// Adamant
		player.getActionSender().sendObject(2105, 2891, 10202, 0, 0, 10);
		player.getActionSender().sendObject(2105, 2891, 10195, 0, 0, 10);
		// Mithril
		player.getActionSender().sendObject(2102, 2889, 10202, 0, 0, 10);
		player.getActionSender().sendObject(2102, 2889, 10195, 0, 0, 10);
		player.getActionSender().sendObject(2102, 2894, 10202, 0, 0, 10);
		player.getActionSender().sendObject(2102, 2894, 10203, 0, 0, 10);
		player.getActionSender().sendObject(2102, 2894, 10204, 0, 0, 10);
		player.getActionSender().sendObject(2102, 2895, 10201, 0, 0, 10);
		player.getActionSender().sendObject(2102, 2895, 10205, 0, 0, 10);
		// Gold
		player.getActionSender().sendObject(2098, 2889, 10203, 0, 0, 10);
		player.getActionSender().sendObject(2098, 2889, 10194, 0, 0, 10);
		player.getActionSender().sendObject(2098, 2887, 10203, 0, 0, 10);
		// Silver
		player.getActionSender().sendObject(2100, 2891, 10203, 0, 0, 10);
		player.getActionSender().sendObject(2100, 2891, 10194, 0, 0, 10);
		player.getActionSender().sendObject(2100, 2887, 10204, 0, 0, 10);
		player.getActionSender().sendObject(2100, 2887, 10202, 0, 0, 10);
		// Iron
		player.getActionSender().sendObject(2093, 2891, 10204, 0, 0, 10);
		player.getActionSender().sendObject(2093, 2891, 10193, 0, 0, 10);
		player.getActionSender().sendObject(2093, 2894, 10194, 0, 0, 10);
		player.getActionSender().sendObject(2093, 2894, 10193, 0, 0, 10);
		player.getActionSender().sendObject(2093, 2894, 10195, 0, 0, 10);
		player.getActionSender().sendObject(2093, 2895, 10196, 0, 0, 10);
		player.getActionSender().sendObject(2093, 2895, 10192, 0, 0, 10);
		// Coal
		player.getActionSender().sendObject(2097, 2889, 10197, 0, 0, 10);
		player.getActionSender().sendObject(2097, 2890, 10197, 0, 0, 10);
		player.getActionSender().sendObject(2097, 2891, 10197, 0, 0, 10);
		player.getActionSender().sendObject(2097, 2889, 10199, 0, 0, 10);
		player.getActionSender().sendObject(2097, 2890, 10199, 0, 0, 10);
		player.getActionSender().sendObject(2097, 2891, 10199, 0, 0, 10);
		// Tin
		player.getActionSender().sendObject(2095, 2889, 10204, 0, 0, 10);
		player.getActionSender().sendObject(2095, 2889, 10193, 0, 0, 10);
		player.getActionSender().sendObject(2095, 2892, 10207, 0, 0, 10);
		player.getActionSender().sendObject(2095, 2891, 10207, 0, 0, 10);
		// Copper
		player.getActionSender().sendObject(2091, 2890, 10204, 0, 0, 10);
		player.getActionSender().sendObject(2091, 2890, 10193, 0, 0, 10);
		player.getActionSender().sendObject(2091, 2890, 10207, 0, 0, 10);
		player.getActionSender().sendObject(2091, 2889, 10207, 0, 0, 10);
		// Empty Ore
		player.getActionSender().sendObject(453, 2890, 10202, 0, 0, 10);
		player.getActionSender().sendObject(453, 2890, 10203, 0, 0, 10);
		player.getActionSender().sendObject(453, 2890, 10194, 0, 0, 10);
		/*
		 * Cooking - Furnace & removes room objects
		 */
		player.getActionSender().sendObject(8750, 2837, 10226, 0, 1, 10);
		player.getActionSender().sendObject(-1, 2838, 10226, 0, 1, 10);
		player.getActionSender().sendObject(-1, 2839, 10226, 0, 1, 10);
		player.getActionSender().sendObject(-1, 2836, 10226, 0, 1, 10);
		player.getActionSender().sendObject(-1, 2840, 10226, 0, 1, 10);
		player.getActionSender().sendObject(-1, 2841, 10226, 0, 1, 10);
		player.getActionSender().sendObject(-1, 2839, 10224, 0, 1, 10);
		player.getActionSender().sendObject(-1, 2839, 10223, 0, 1, 10);
		player.getActionSender().sendObject(-1, 2836, 10223, 0, 1, 10);
		player.getActionSender().sendObject(-1, 2837, 10222, 0, 1, 10);
		player.getActionSender().sendObject(-1, 2837, 10223, 0, 1, 10);
		/*
		 * Custom Bank
		 */
		player.getActionSender().sendObject(-1, 2912, 10224, 0, 1, 10);
		player.getActionSender().sendObject(-1, 2911, 10224, 0, 1, 10);
		player.getActionSender().sendObject(2213, 2911, 10223, 0, 2, 10);
		player.getActionSender().sendObject(2213, 2912, 10223, 0, 2, 10);
		player.getActionSender().sendObject(2213, 2913, 10223, 0, 2, 10);
		player.getActionSender().sendObject(2213, 2907, 10204, 0, 2, 10);
		player.getActionSender().sendObject(2213, 2906, 10204, 0, 2, 10);
		player.getActionSender().sendObject(2213, 2905, 10204, 0, 2, 10);
		player.getActionSender().sendObject(2213, 2904, 10204, 0, 2, 10);
		/*
		 * Removes Doors
		 */
		player.getActionSender().sendObject(-1, 2872, 10203, 1, -1, 10);
		player.getActionSender().sendObject(-1, 2872, 10195, 1, -1, 10);
		player.getActionSender().sendObject(-1, 2887, 10195, 1, -1, 10);
		player.getActionSender().sendObject(-1, 2887, 10202, 1, -1, 10);
		player.getActionSender().sendObject(-1, 2893, 10193, 1, -1, 10);
		player.getActionSender().sendObject(-1, 2893, 10207, 1, -1, 10);
		player.getActionSender().sendObject(-1, 2888, 10199, 2, -1, 10);
		player.getActionSender().sendObject(-1, 2870, 10199, 2, -1, 10);
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
		case ObjectConstants.keldaBank:
			player.getActionSender().sendBankInterface();
			break;
		case ObjectConstants.keldaStairs:
			if (player.getHeightLevel() == 0)
				TeleportationHandler.addNewRequest(player, player.getAbsX(),
						player.getAbsY(), player.getHeightLevel() + 1, 0);
			break;
		case ObjectConstants.keldaStairs1:
			if (player.getHeightLevel() == 1)
				TeleportationHandler.addNewRequest(player, player.getAbsX(),
						player.getAbsY(), player.getHeightLevel() - 1, 0);
			break;
		case ObjectConstants.keldaStairs2:
			if (player.getHeightLevel() == 1)
				TeleportationHandler.addNewRequest(player, player.getAbsX(),
						player.getAbsY(), player.getHeightLevel() + 1, 0);
			break;
		case ObjectConstants.keldaStairs3:
			if (player.getHeightLevel() == 2)
				TeleportationHandler.addNewRequest(player, player.getAbsX(),
						player.getAbsY(), player.getHeightLevel() - 1, 0);
			break;
		case ObjectConstants.keldaStairs4:
			if (player.getAbsX() == 2833 || player.getAbsY() == 10209)
				return;
			if (player.getHeightLevel() == 0)
				TeleportationHandler.addNewRequest(player, player.getAbsX(),
						player.getAbsY(), player.getHeightLevel() + 1, 0);
			break;
		case ObjectConstants.keldaStairs5:
			if (player.getHeightLevel() == 1)
				TeleportationHandler.addNewRequest(player, player.getAbsX(),
						player.getAbsY(), player.getHeightLevel() - 1, 0);
			break;
		case ObjectConstants.keldaDoor:
			if (player.getHeightLevel() == 2)
				if (player.getAbsX() == 2888 && player.getAbsY() == 10199)
					TeleportationHandler.addNewRequest(player,
							player.getAbsX() - 1, player.getAbsY(),
							player.getHeightLevel() - 1, 0);
				else if (player.getAbsX() == 2887 && player.getAbsY() == 10199)
					TeleportationHandler.addNewRequest(player,
							player.getAbsX() - 1, player.getAbsY(),
							player.getHeightLevel() + 1, 0);
			break;
		case ObjectConstants.keldaSmelt:
			for (int fi = 0; fi < PlayerConstants.smelt_frame.length; fi++) {
				player.getActionSender().sendFrame246(
						PlayerConstants.smelt_frame[fi], 150,
						PlayerConstants.smelt_bars[fi]);
				player.getActionSender().sendFrame164(2400);
			}
			break;

		}
	}

	public void registerObjects() {
		/*
		 * Removes doors
		 */
		ObjectSystem.registerObject(-1, 2872, 10203, 1, -1, 10);
		ObjectSystem.registerObject(-1, 2872, 10195, 1, -1, 10);
		ObjectSystem.registerObject(-1, 2887, 10195, 1, -1, 10);
		ObjectSystem.registerObject(-1, 2887, 10202, 1, -1, 10);
		ObjectSystem.registerObject(-1, 2893, 10193, 1, -1, 10);
		ObjectSystem.registerObject(-1, 2893, 10207, 1, -1, 10);
		ObjectSystem.registerObject(-1, 2888, 10199, 2, -1, 10);
		ObjectSystem.registerObject(-1, 2870, 10199, 2, -1, 10);
	}

}