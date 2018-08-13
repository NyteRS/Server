package com.server2.content;

import java.util.ArrayList;

import com.server2.content.misc.mobility.TeleportationHandler;
import com.server2.model.entity.Location;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;

/**
 * 
 * @author Rene
 * 
 */
public class JailSystem {

	private static ArrayList<String> jailedPlayers = new ArrayList<String>();

	private static final int[] jailCellsY = { 2796, 2798, 2800 };

	private static final int[] jailCellsX = { 2775 };

	public static void add(String name) {
		if (!jailedPlayers.contains(name))
			jailedPlayers.add(name);
	}

	public static void addToJail(Player client) {
		client.getActionSender()
				.sendMessage(
						"You have been accused of rule breaking, A staff member will now deal with you.");
		client.getActionSender()
				.sendMessage(
						"Please remember to read @red@::rules@bla@ to avoid these situations!");
		final int randomCell = Misc.random(2);
		if (randomCell == 0)
			TeleportationHandler.addNewRequest(client, jailCellsX[0],
					jailCellsY[0], 0, 0);
		else if (randomCell == 1)
			TeleportationHandler.addNewRequest(client, jailCellsX[0],
					jailCellsY[1], 0, 0);
		else if (randomCell == 2)
			TeleportationHandler.addNewRequest(client, jailCellsX[0],
					jailCellsY[2], 0, 0);
		else
			TeleportationHandler.addNewRequest(client, 2775, 2798, 0, 0);
		add(client.getUsername());
	}

	public static boolean inJail(Player client) {
		return jailedPlayers.contains(client.getUsername());
	}

	public static void remove(String name) {
		if (jailedPlayers.contains(name))
			jailedPlayers.remove(name);
	}

	public static void removeFromJail(Player client) {
		remove(client.getUsername());
		client.getActionSender().sendMessage(
				"You have been released from jail, Let this be a warning.");
		client.getActionAssistant().forceChat(
				"I got released from jail, I better follow the rules now.");
		if (client.absX == 2776)
			client.forceMovement(new Location(client.getAbsX() - 3, client
					.getAbsY(), client.getHeightLevel()));
		else if (client.absX == 2775)
			client.forceMovement(new Location(client.getAbsX() - 2, client
					.getAbsY(), client.getHeightLevel()));
		else if (client.absX == 2774)
			client.forceMovement(new Location(client.getAbsX() - 1, client
					.getAbsY(), client.getHeightLevel()));
		else if (client.privileges > 0 && client.privileges < 4)
			client.getActionSender()
					.sendMessage(
							"You are not in jail, type ::teletojail to teleport to the jail bay");
	}
}
