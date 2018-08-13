package com.server2.model.combat.additions;

import com.server2.content.constants.TotalValuable;
import com.server2.model.Item;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene Roosen Handles Items Kept On Death
 */
public class ItemsKeptOnDeath {

	/**
	 * Gets the items kept on death
	 */
	private static int[] getItemsOnDeath(Player client) {
		return TotalValuable.mostValuable(client);
	}

	/**
	 * Opens the interface and draws the items on
	 */
	public static void itemsOnDeath(Player client) {
		final int[] itemsOnDeath = getItemsOnDeath(client);
		final Item[] items = new Item[4];
		for (int i = 0; i < items.length; i++)
			if (itemsOnDeath[i] > 0)
				items[i] = new Item(itemsOnDeath[i]);
		client.getActionSender().sendUpdateItems(6963, items);
		client.getActionSender().sendInterface(6960);
	}

}
