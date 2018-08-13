package com.server2.model.combat.additions;

import com.server2.content.Achievements;
import com.server2.model.Item;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene Roosen & Lukas
 * 
 */
public class PkingMaster {

	/**
	 * Set 1
	 */
	private static int[][] set1 = { // Hybrid Set
	{ 10828, 1 }, { 1704, 1 }, { 4091, 1 }, { 4093, 1 }, { 2497, 1 },
			{ 2503, 1 }, { 3105, 1 }, { 4675, 1 }, { 13734, 1 }, { 1725, 1 },
			{ 1127, 1 }, { 1079, 1 }, { 4587, 1 }, { 1187, 1 }, { 2412, 1 },
			{ 6568, 1 }, { 565, 200 }, { 560, 400 }, { 555, 600 },
			{ 6686, 15 }, { 3025, 25 }, { 3041, 15 }, { 2441, 15 },
			{ 2437, 15 }, { 1215, 1 }, };

	/**
	 * Set 2
	 */
	private static int[][] set2 = { // Pure Set
	{ 662, 1 }, { 1704, 1 }, { 6107, 1 }, { 6108, 1 }, { 3105, 1 },
			{ 4587, 1 }, { 3839, 1 }, { 2414, 1 }, { 11118, 1 }, { 2550, 1 },
			{ 392, 500 },

			{ 1215, 1 }, { 2441, 15 }, { 2437, 15 },

			{ 3025, 25 }, };

	/**
	 * Set 3
	 */
	private static int[][] set3 = { // Range Tank
	{ 10828, 1 }, { 1704, 1 }, { 10499, 1 }, { 4131, 1 }, { 1079, 1 },
			{ 2491, 1 }, { 2503, 1 }, { 9185, 1 }, { 9244, 50 }, { 2445, 15 },
			{ 3025, 25 }, { 2443, 15 }, { 392, 250 }, { 861, 1 }, { 892, 500 },
			{ 13734, 1 }, { 560, 100 }, { 9075, 200 }, { 557, 500 },

	};

	/**
	 * Set 4
	 */
	private static int[][] set4 = { // Melee Set
	{ 10828, 1 }, { 1725, 1 }, { 1127, 1 }, { 1079, 1 }, { 4131, 1 },
			{ 4587, 1 }, { 6568, 1 }, { 2550, 1 }, { 1187, 1 }, { 2441, 15 },
			{ 2437, 15 }, { 2443, 15 }, { 3025, 25 }, { 560, 100 },
			{ 9075, 200 }, { 557, 500 }, { 392, 500 }, };

	/**
	 * Set 5
	 */
	private static int[][] set5 = { // Magic Set
	{ 3755, 1 }, { 1704, 1 }, { 4091, 1 }, { 4093, 1 }, { 4097, 1 },
			{ 4675, 1 }, { 2550, 1 }, { 13734, 1 }, { 3041, 15 }, { 3025, 25 },
			{ 392, 500 }, { 565, 200 }, { 560, 400 }, { 555, 600 }, };

	/**
	 * The prices of the sets in order.
	 */
	private static double[] prices = { 1500000, 250000, 1000000, 800000,
			1500000 };

	/**
	 * The names of the sets
	 */
	private static String[] setNames = { "Hybriding set [Mains]", "Pure Set",
			"Range Tank", "Melee", "Magic Set" };

	/**
	 * Attempts to buy a set
	 * 
	 * @param client
	 * @param set
	 */
	public static void attemptSet(Player client, int set) {
		if (client.getActionAssistant().playerHasItem(995,
				(int) prices[set - 1])) {
			if (client.getActionAssistant().freeSlots() < requiredAmount(set)) {
				client.getActionSender().sendMessage(
						"You need more inventory space to buy this set!");
				client.getActionSender().sendWindowsRemoval();
				return;
			}
			client.getActionAssistant().deleteItem(995, (int) prices[set - 1]);
			buySet(client, set);
			client.getActionSender().sendWindowsRemoval();
		} else {
			client.getActionSender().sendWindowsRemoval();
			client.getActionSender().sendMessage(
					"You do not have enough money to buy this set, you need "
							+ prices[set - 1] / 1000000 + "M.");
		}
	}

	/**
	 * Initiates a purchase of a set.
	 * 
	 * @param client
	 * @param set
	 */
	public static void buySet(Player client, int set) {
		if (set == 1)
			for (final int[] element : set1)
				client.getActionSender().addItem(
						new Item(element[0], element[1]));
		else if (set == 2)
			for (final int[] element : set2)
				client.getActionSender().addItem(
						new Item(element[0], element[1]));
		else if (set == 3)
			for (final int[] element : set3)
				client.getActionSender().addItem(
						new Item(element[0], element[1]));
		else if (set == 4)
			for (final int[] element : set4)
				client.getActionSender().addItem(
						new Item(element[0], element[1]));
		else if (set == 5)
			for (final int[] element : set5)
				client.getActionSender().addItem(
						new Item(element[0], element[1]));
		Achievements.getInstance().complete(client, 22);
	}

	/**
	 * Sends the dialogue
	 * 
	 * @param client
	 */
	public static void handleDialogue(Player client) {
		client.getActionSender().selectOption("Select an option",
				setNames[0] + " @bla@(@dre@" + prices[0] / 1000 + "K@bla@)",
				setNames[1] + " @bla@(@dre@" + prices[1] / 1000 + "K@bla@)",
				setNames[2] + " @bla@(@dre@" + prices[2] / 1000 + "K@bla@)",
				setNames[3] + " @bla@(@dre@" + prices[3] / 1000 + "K@bla@)",
				setNames[4] + " @bla@(@dre@" + prices[4] / 1000 + "K@bla@)");
		client.dialogueAction = 46005;
	}

	/**
	 * Returns the required inventory space for the specific set.
	 * 
	 * @param set
	 * @return
	 */
	private static int requiredAmount(int set) {
		if (set == 1)
			return set1.length;
		else if (set == 2)
			return set2.length;
		else if (set == 3)
			return set3.length;
		else if (set == 4)
			return set4.length;
		else if (set == 5)
			return set5.length;
		return 1;
	}
}
