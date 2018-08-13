package com.server2.content.skills.crafting;

import com.server2.InstanceDistributor;
import com.server2.content.Achievements;
import com.server2.model.entity.Entity;
import com.server2.model.entity.player.Player;

/**
 * Tanning class
 * 
 * @author killamess
 * 
 */
public class Tanning {

	private static final int[][] itemPrice = { { 1739, 1 }, { 1739, 15 },
			{ 1753, 200 }, { 1751, 350 }, { 1749, 450 }, { 1747, 600 } };

	private static final int[][] tanningInterfaceImage = { { 69, 41 },
			{ 73, 43 }, { 71, 45 }, { 72, 5 }, { 75, 7 }, { 76, 9 } };

	private static final String[] writeToInterface = { "Leather",
			"Unavialable", "Green", "Blue", "Hard Leather", "Unvaliable",
			"Red", "Black", "1gp", "N/A", "200gp", "350gp", "5gp", "N/A",
			"450gp", "600gp" };

	/**
	 * Opens the tan interface
	 */
	public static void openTanInterface(Entity ent) {

		if (ent instanceof Player) {

			((Player) ent).getActionSender().sendInterface(14670);

			for (int i = 0; i < 6; i++)
				((Player) ent).getActionSender().sendFrame246(
						14700 + tanningInterfaceImage[i][0], 250,
						(i < 3 ? 1700 : 2500) + tanningInterfaceImage[i][1]);
			for (int i = 0; i < 16; i++)
				((Player) ent).getActionSender().sendString(
						writeToInterface[i], 14777 + i);
			// System.out.println("["+System.currentTimeMillis()+"] sendquest tanning");
		}
	}

	/**
	 * Tan Item
	 */
	public static void tanHide(Entity ent, int originalId, int newItem,
			int amount) {

		if (ent instanceof Player) {

			final boolean hasItem = ((Player) ent).getActionAssistant()
					.getItemAmount(originalId) > 0;

			final String itemName = InstanceDistributor.getItemManager()
					.getItemDefinition(originalId).getName();
			final String itemName2 = InstanceDistributor.getItemManager()
					.getItemDefinition(newItem).getName();

			if (!hasItem) {
				((Player) ent).getActionSender().sendMessage(
						"You need " + itemName.toLowerCase() + " to tan "
								+ itemName2.toLowerCase() + ".");
				return;
			}

			((Player) ent).getActionSender().sendWindowsRemoval();

			for (int i = 0; i < itemPrice.length; i++)
				if (itemPrice[i][0] == originalId) {

					int count = 0;

					while (((Player) ent).getActionAssistant().getItemAmount(
							originalId) > 0) {

						if (!((Player) ent).getActionAssistant().playerHasItem(
								995, itemPrice[i][1])) {
							((Player) ent).getActionSender().sendMessage(
									"You don't have enough money.");
							return;
						}

						((Player) ent).getActionAssistant().deleteItem(995,
								itemPrice[i][1]);
						((Player) ent).getActionAssistant().deleteItem(
								originalId, 1);
						((Player) ent).getActionSender().addItem(newItem, 1);
						if (newItem == 1741)
							if (((Player) ent).stages[0] != 2)
								Achievements.getInstance().complete(
										(Player) ent, 0);
						count++;

						if (count == amount)
							break;
					}
					break;
				}
		}
	}
}
