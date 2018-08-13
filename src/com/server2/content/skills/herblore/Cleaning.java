package com.server2.content.skills.herblore;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;

/**
 * 
 * @author Killamess & Lukas
 * 
 */
public class Cleaning {

	public static int[][] clean = { { 199, 249, 1, (int) 2.5 },
			{ 201, 251, 5, (int) 3.8 }, { 203, 253, 11, 5 },
			{ 205, 255, 20, (int) 6.3 }, { 207, 257, 25, (int) 7.5 },
			{ 209, 259, 30, (int) 8.8 }, { 211, 261, 35, 10 },
			{ 213, 263, 40, (int) 11.3 }, { 215, 265, 48, (int) 12.5 },
			{ 217, 267, 70, (int) 13.8 }, { 219, 269, 75, 15 },
			{ 3049, 2998, 30, 8 }, { 2485, 2481, 73, 13 },
			{ 3051, 3000, 59, 12 }, { 21626, 21624, 91, 17 } };

	public static void cleanHerb(Player client, int itemId, int slot) {

		if (!client.getActionAssistant().isItemInBag(itemId))
			return;

		for (final int[] element : clean)
			if (element[0] == itemId) {

				if (client.playerLevel[PlayerConstants.HERBLORE] < element[2]) {
					client.getActionSender().sendMessage(
							"You need a herblore level of " + element[2]
									+ " to clean this.");
					return;
				}
				client.getActionAssistant().deleteItem(element[0], slot, 1);
				client.getActionSender().addItem(element[1], 1);
				client.getActionSender().sendMessage("You clean the herb.");
				client.getActionAssistant().addSkillXP(
						element[3] * client.multiplier,
						PlayerConstants.HERBLORE);
				break;
			}
	}
}
