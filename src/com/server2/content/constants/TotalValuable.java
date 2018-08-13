package com.server2.content.constants;

import com.server2.content.misc.GeneralStore;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Lukas Pinckers
 * 
 */

public class TotalValuable {

	public static int[] mostValuable(final Player client) {
		final int[] items = new int[4];

		final int[] playerValues = new int[40];
		final int[] playerItems = new int[40];

		for (int i = 0; i < 28; i++) {
			playerItems[i] = client.playerItems[i] - 1;
			playerValues[i] = GeneralStore.getInstance().itemPrice(
					playerItems[i]);

		}
		for (int i = 28; i < 39; i++) {
			playerItems[i] = client.playerEquipment[i - 28];
			playerValues[i] = GeneralStore.getInstance().itemPrice(
					playerItems[i]);
		}
		int temp1;
		int temp;
		for (int i = 0; i < 39; i++)
			if (playerValues[i] < playerValues[i + 1]) {
				temp1 = playerItems[i];
				temp = playerValues[i];
				playerValues[i] = playerValues[i + 1];
				playerValues[i + 1] = temp;
				playerItems[i] = playerItems[i + 1];
				playerItems[i + 1] = temp1;

				i = -1;
			}
		for (int i = 0; i < 4; i++)
			items[i] = playerItems[i];
		int PROTECTION_SIZE = 3;

		if (client.getSkullTimer() > 0)
			PROTECTION_SIZE = 0;

		if (client.getPrayerHandler().clicked[8]
				|| client.getPrayerHandler().clicked[26])
			PROTECTION_SIZE += 1;

		for (int i = PROTECTION_SIZE; i < items.length; i++)
			items[i] = 0;

		return items;
	}
}
