package com.server2.content.misc.homes.config;

import com.server2.model.entity.Location;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.world.objects.ObjectConstants;

/**
 * 
 * @author Jordon Barber Used for handling the Home teleport for Sophanem
 * 
 */

public class Sophanem {

	public static Sophanem instance = new Sophanem();
	public static final String locationName = "Sophanem";

	public static final int JUMPEMOTE = 3068;
	public static final int MUMMY = 4476;

	public static final int coords[][] = { { 3294, 2765 }, // Outside Pyramid
			{ 3277, 9171 }, // Under Pyramid Dungeon
			{ 2384, 4721 }, // Dungeon Entrance
			{ 3291, 2787 }, // Outside Dungeon Entrance
			{ 2420, 4690 }, // Barrier passing
	};

	/**
	 * 
	 * Handles button clicking
	 * 
	 * @param player
	 * @param actionButtonId
	 */

	public void actionClicking(Player player, int actionButtonId) {
		switch (actionButtonId) {

		case 9157:
			if (player.dialogueAction == MUMMY) {
				player.getPlayerTeleportHandler().forceTeleport(coords[1][0],
						coords[1][1], 0);
				player.sendMessage("..You venture into the pyramid.");
			}
			break;
		case 9158:
			break;

		}
	}

	public final boolean inDungeon1(Player player) {
		if (player.getAbsX() >= 3270 && player.getAbsX() <= 3320
				&& player.getAbsY() >= 9165 && player.getAbsY() <= 9210)
			return true;
		return false;
	}

	public final boolean inDungeon2(Player player) {
		if (player.getAbsX() >= 2365 && player.getAbsX() <= 2415
				&& player.getAbsY() >= 4650 && player.getAbsY() <= 4740)
			return true;
		return false;
	}

	/**
	 * 
	 * Checks if player is in area
	 * 
	 * @param player
	 * @return
	 */

	public final boolean inSophanem(Player player) {
		if (player.getAbsX() >= 2765 && player.getAbsX() <= 3320
				&& player.getAbsY() >= 2750 && player.getAbsY() <= 2810)
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
		/*
		 * Bank Stalls
		 */
		player.getActionSender().sendObject(2213, 3278, 2775, 0, 2, 10);
		player.getActionSender().sendObject(2213, 3279, 2775, 0, 2, 10);
		player.getActionSender().sendObject(2213, 3280, 2775, 0, 2, 10);
		player.getActionSender().sendObject(2213, 3281, 2775, 0, 2, 10);
		player.getActionSender().sendObject(2213, 3282, 2775, 0, 2, 10);
		player.getActionSender().sendObject(2213, 3283, 2775, 0, 2, 10);
		player.getActionSender().sendObject(2213, 3284, 2775, 0, 2, 10);
		/*
		 * Thieving Stalls
		 */
		player.getActionSender().sendObject(4875, 3290, 2765, 0, 0, 10);
		player.getActionSender().sendObject(4874, 3291, 2765, 0, 0, 10);
		player.getActionSender().sendObject(4876, 3292, 2765, 0, 0, 10);
		player.getActionSender().sendObject(4877, 3296, 2765, 0, 0, 10);
		player.getActionSender().sendObject(4878, 3297, 2765, 0, 0, 10);
		/*
		 * Dungeon Entrance and exit
		 */
		player.getActionSender().sendObject(5082, 3288, 2788, 0, 0, 10);
		player.getActionSender().sendObject(8956, 2384, 4723, 0, 1, 10);
		/*
		 * Altars
		 */
		player.getActionSender().sendObject(409, 3281, 2778, 0, 0, 10);
		player.getActionSender().sendObject(411, 3283, 2778, 0, 0, 10);
		player.getActionSender().sendObject(6552, 3278, 2778, 0, 0, 10);
		player.getActionSender().sendObject(17010, 3284, 2786, 0, -2, 10);
	}

	/**
	 * 
	 * Handles Dialogue
	 * 
	 * @param player
	 */

	public void Mummy(Player player) {
		player.getDM().sendDialogue(2578, MUMMY);
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
		case ObjectConstants.dungeonPit:
			if (player.playerLevel[PlayerConstants.AGILITY] >= 40) {
				if (player.getAbsY() > 9196) {
					player.getActionAssistant().startAnimation(JUMPEMOTE);
					player.getPlayerTeleportHandler().forceDelayTeleport(
							player.getAbsX(), 9193, 0, 2);
				} else if (player.getAbsY() < 9195) {
					player.getActionAssistant().startAnimation(JUMPEMOTE);
					player.getPlayerTeleportHandler().forceDelayTeleport(
							player.getAbsX(), 9197, 0, 2);
				}
			} else
				player.sendMessage("You need an agility level of atleast 40 to jump this pit!");
			break;
		case ObjectConstants.dungeonLadder:
			player.getPlayerTeleportHandler().forceTeleport(coords[0][0],
					coords[0][1], 0);
			player.sendMessage("You climb the ladder and exit the pyramid.");
			break;
		case ObjectConstants.dungExit:
			player.getPlayerTeleportHandler().forceTeleport(coords[3][0],
					coords[3][1], 0);
			player.sendMessage("You climb the ladder and exit the dungeon.");
			break;
		case ObjectConstants.dungeonEntrance:
			player.getPlayerTeleportHandler().forceTeleport(coords[1][0],
					coords[1][1], 0);
			player.sendMessage("..You venture into the pyramid.");
			break;
		case ObjectConstants.sophEntrance:
			if (player.combatLevel >= 110
					&& player.playerLevel[PlayerConstants.SLAYER] >= 75) {
				player.getPlayerTeleportHandler().forceTeleport(coords[2][0],
						coords[2][1], 0);
				player.sendMessage("..You venture into the dungeon.");
			} else
				player.sendMessage("You need a combat level of atleast 110 and a slayer level of atleast 75 to enter.");
			break;
		case ObjectConstants.dungDoor:
			if (player.playerLevel[PlayerConstants.AGILITY] >= 90
					|| player.playerLevel[PlayerConstants.MAGIC] >= 90) {
				player.sendMessage("..You pass through the barrier");
				if (player.getAbsY() <= 4690)
					player.getPlayerTeleportHandler().forceTeleport(
							player.getAbsX(), coords[4][1] + 1, 0);
				else if (player.getAbsY() >= 4691)
					player.getPlayerTeleportHandler().forceTeleport(
							player.getAbsX(), coords[4][1], 0);
			} else
				player.sendMessage("You need an agility & magic level of atleast 90 to pass through this barrier!");
			break;
		}
	}
}