package com.server2.content.misc;

import com.server2.model.entity.player.Player;

/**
 * 
 * @author Hoodlum
 * 
 */
public class WeaponRepair {

	/**
	 * An array to hold broken chaotics.
	 */
	public static final int[] BROKEN_CHAOTICS = { 18350, 18352, 18354, 18356,
			18358, 18360, 18364 };

	/**
	 * An array to store chaotics
	 */
	public static final int[] CHAOTICS = { 18349, 18351, 18353, 18355, 18357,
			18359 };

	public static void repair(Player player) {
		for (final int item : BROKEN_CHAOTICS)
			while (player.getActionAssistant().playerHasItem(
					BROKEN_CHAOTICS[item])) {
				player.getActionAssistant()
						.deleteItem(BROKEN_CHAOTICS[item], 1);
				player.getActionAssistant().deleteItem(995, 5000000);
				player.getActionSender().addItem(CHAOTICS[item], 1);
			}
		if (player.getActionAssistant().getItemAmount(995) < 5000000)
			player.getActionSender().sendMessage(
					"You need 5 million coins to repair your chaotics.");
	}

}
