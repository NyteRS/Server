package com.server2.content.misc;

import com.server2.content.Achievements;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Lukas P
 * 
 */
public class UpgradeSkillCape {

	public static final int[] capes = { 9747, 9750, 9753, 9756, 9759, 9762,
			9765, 9768, 9771, 9774, 9777, 9780, 9783, 9786, 9792, 9795, 9798,
			9801, 9804, 9807, 9810, 12169, 15706, 9948 };

	public boolean canUpgrade(Player client) {
		int skills99 = 0;
		for (int i = 0; i < 25; i++)
			if (client.playerLevel[i] >= 99)
				skills99++;
		if (skills99 >= 2)
			return true;
		return false;
	}

	public boolean isSkillcape(Player client, int item) {

		for (final int cape : capes)
			if (cape == item)
				return true;
		return false;
	}

	public void upgrade(Player client, int item) {
		if (!canUpgrade(client)) {
			client.getActionSender().sendMessage(
					"You need at least 2 levels 99 to upgrade your cape.");
			return;

		}
		if (!isSkillcape(client, item)) {
			client.getActionSender().sendMessage(
					"The Wise old man cannot upgrade that cape");
			return;
		}
		if (item == 19709) {
			client.getActionSender().sendMessage(
					"You need atleast 2 levels 120 to upgrade that cape.");
			return;
		}
		client.getActionAssistant().deleteItem(item, 1);
		client.getActionSender().addItem(item + 1, 1);
		client.getActionSender().sendMessage("You upgrade your cape to (t).");
		Achievements.getInstance().complete(client, 32);
	}
}
