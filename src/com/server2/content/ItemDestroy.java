package com.server2.content;

import com.server2.InstanceDistributor;
import com.server2.model.entity.player.Player;

/**
 * @author lukas pinckers
 */

public class ItemDestroy {

	public static void destroyItem(Player client) {
		final String itemName = InstanceDistributor.getItemManager()
				.getItemDefinition(client.destroyItem).getName();
		client.getActionAssistant().deleteItem(client.destroyItem, 1);
		client.getActionSender().sendMessage(
				"Your " + itemName.toLowerCase()
						+ " vanishes as you drop it on the ground.");
		client.destroyItem = 0;
	}

	public static void option(Player client, int itemId) {
		final String itemName = InstanceDistributor.getItemManager()
				.getItemDefinition(itemId).getName();
		final String[][] info = {
				{ "Are you sure you want to destroy this item?", "14174" },
				{ "Yes.", "14175" }, { "No.", "14176" }, { "", "14177" },
				{ "", "14182" }, { "", "14183" }, { itemName, "14184" } };
		client.getActionSender().sendFrame34(itemId, 0, 14171, 1);

		for (final String[] element : info)
			client.getActionSender().sendString(element[0],
					Integer.parseInt(element[1]));
		// System.out.println("["+System.currentTimeMillis()+"] sendquest itemdestroy");

		client.destroyItem = itemId;
		client.getActionSender().sendFrame164(14170);
	}
}
