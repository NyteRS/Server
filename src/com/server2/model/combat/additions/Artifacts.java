package com.server2.model.combat.additions;

import com.server2.content.Achievements;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Lukas
 * 
 */
public class Artifacts {

	private static int[] values = { 14892, 5000, 14891, 10000, 14890, 20000,
			14889, 30000, 14888, 40000, 14887, 50000, 14886, 75000, 14885,
			100000, 14884, 150000, 14883, 200000, 14882, 250000, 14881, 300000,
			14880, 400000, 14879, 500000, 14878, 750000, 14877, 1000000, 14876,
			5000000 };

	private int getValue(int id) {
		int value = 0;
		for (int i = 0; i < values.length; i = i + 2)
			if (values[i] == id)
				value = values[i + 1];
		return value;
	}

	public void giveReward(Player client, int itemId) {
		final int toGive = getValue(itemId) * 3;
		if (toGive < 5000) {
			client.getActionSender().sendMessage("That is not an artifact.");
			return;
		}
		if (client.getActionAssistant().playerHasItem(itemId, 1)) {
			client.getActionAssistant().deleteItem(itemId, 1);
			client.getActionSender().addItem(995, toGive);
			Achievements.getInstance().complete(client, 63);

		}
	}

}
