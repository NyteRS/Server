package com.server2.content.anticheat;

import com.server2.InstanceDistributor;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Areas;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class WearItemCheating {

	public static final String[] DUNGEONEERING_ITEMS = { "primal", "novite",
			"Bathus", "Zephyrium", "Fractite", "Kratonite", "Marmaros",
			"Argonite", "Gorgonite", "Katagon", "Promethium" };

	public static boolean canWearThatItem(Player client, int item) {
		if (client.playerLook[PlayerConstants.SEX] == 1)
			if (item == 21790 || item == 21787 || item == 21793) {
				client.getActionSender().sendMessage(
						"You cannot wear these as a female.");
				return false;
			}
		if (client.tradeStage != 0) {
			client.getActionSender().sendMessage(
					"You cannot wear something while trading!");
			return false;
		}
		if (!client.floor1() && !client.floor2()
				&& !Areas.bossRoom1(client.getPosition()) && !client.floor3())
			for (final String dungName : DUNGEONEERING_ITEMS) {
				final String dungNameLower = dungName.toLowerCase();
				if (InstanceDistributor.getItemManager()
						.getItemDefinition(item).getName().toLowerCase()
						.contains(dungNameLower)) {
					client.getActionSender()
							.sendMessage(
									"You cannot use that item outside of dungeoneering!");
					client.getActionAssistant().deleteItem(item, 1);
					return false;
				}
			}

		return true;
	}
}
