package com.server2.content.misc;

import com.server2.InstanceDistributor;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;

/**
 * 
 * @author Lukas Pinckers
 * 
 */
public class ExperienceLamps {

	/**
	 * Handles the dungeoneering XP lamp.
	 * 
	 * @param client
	 */
	public static void handleDungLamp(Player client) {
		if (client.getActionAssistant().playerHasItem(15389, 1)) {
			client.getActionAssistant().deleteItem(15389, 1);
			client.getActionAssistant().addSkillXP(
					75 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
					PlayerConstants.DUNGEONEERING);
			client.getActionSender().sendMessage(
					"You rub the lamp and gain some dungeoneering experience.");

		}
	}

	/**
	 * Handles the slayer lamp.
	 * 
	 * @param client
	 */
	public static void handleSlayerLamp(Player client) {
		if (client.getActionAssistant().playerHasItem(11137, 1)) {
			client.getActionAssistant().deleteItem(11137, 1);
			client.getActionAssistant().addSkillXP(1000 * 100,
					PlayerConstants.SLAYER);
			client.getActionSender().sendMessage(
					"You rub the lamp and gain some slayer experience.");

		}
	}

	public static void unLockPrayer(Player client, int itemID) {
		if (client.getActionAssistant().playerHasItem(itemID, 1)) {
			client.getActionAssistant().deleteItem(itemID, 1);
			if (itemID == 18839)
				client.rigour = 1;
			else if (itemID == 18343)
				client.renewal = 1;
			else if (itemID == 18344)
				client.augury = 1;
			client.getActionSender().sendMessage(
					"You read the "
							+ InstanceDistributor.getItemManager()
									.getItemDefinition(itemID).getName()
							+ " and learn a new prayer.");
		}
	}

}
