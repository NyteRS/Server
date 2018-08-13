package com.server2.content.skills.crafting;

import com.server2.model.entity.player.Player;

/**
 * 
 * @author killamess
 * 
 */

public class Pottery {

	public static void makeSoftClay(Player client) {
		if (client.getActionAssistant().isItemInBag(434)
				&& client.getActionAssistant().isItemInBag(1937)) {
			client.getActionAssistant().deleteItem(1937, 1);
			client.getActionSender().addItem(1935, 1);
			client.getActionAssistant().deleteItem(434, 1);
			client.getActionSender().addItem(1761, 1);
			client.getActionSender().sendMessage(
					"You mix the clay and the water.");
			client.getActionSender().sendMessage(
					"You now have some soft, workable clay.");
		}
	}

}
