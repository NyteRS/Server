package com.server2.content.misc;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class SkillResetting {

	/**
	 * Resets the skill in case.
	 * 
	 * @param client
	 * @param skillId
	 */
	public static void resetSkill(Player client, int skillId) {
		if (client == null)
			return;

		for (final int element : client.playerEquipment)
			if (element > 0) {
				client.getActionSender()
						.sendMessage(
								"You must take off all your equipment before you do this");
				client.getActionSender().sendWindowsRemoval();
				return;

			}
		if (skillId == 3) {
			client.playerLevel[skillId] = 10;
			client.hitpoints = 10;
			client.playerXP[skillId] = 1155;
		} else {
			client.playerXP[skillId] = 0;
			client.playerLevel[skillId] = 1;
		}
		client.getActionAssistant().refreshSkill(skillId);
		client.getActionSender().sendMessage(
				"You've succesfully reset your "
						+ PlayerConstants.SKILL_NAMES[skillId] + " level.");
		client.getActionSender().sendWindowsRemoval();
		client.dialogueAction = -1;
		client.nDialogue = -1;

	}

	/**
	 * Opens the dialogue menu with skills that are available to be reset.
	 */

	public static void sendSkillDialogue(Player client, int stage) {
		switch (stage) {
		case 0:
			client.getActionSender().selectOption("Which skill", "Attack",
					"Defence", "Strength", "Ranged", "Next");
			client.dialogueAction = 11004;

			break;
		case 1:
			client.getActionSender().selectOption("Which skill", "Prayer",
					"Magic", "Hitpoints", "Summoning", "Nevermind");
			client.dialogueAction = 11005;
			break;
		}
	}

}
