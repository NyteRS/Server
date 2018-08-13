package com.server2.content.skills.crafting;

import com.server2.InstanceDistributor;
import com.server2.content.Achievements;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.world.map.tile.TileManager;

/**
 * @author Lukas Pinckers
 * @author Rene Roosen
 */
public class RuneCrafting {

	public static void craftRunesOnAltar(Player client, int requiredLevel,
			double exp, int item, int talismen) {
		int runeamount = 0;
		int essamount = 0;
		essamount = client.getActionAssistant().getItemCount(7936);
		runeamount = essamount;
		if (item != 9075)
			if (!client.getActionAssistant().isItemInBag(talismen)) {
				client.getActionSender().sendMessage(
						"You need "
								+ InstanceDistributor.getItemManager()
										.getItemDefinition(talismen).getName()
										.toLowerCase()
								+ " to craft "
								+ InstanceDistributor.getItemManager()
										.getItemDefinition(item).getName()
										.toLowerCase() + "s.");
				return;
			}
		if (client.playerLevel[PlayerConstants.RUNECRAFTING] < requiredLevel) {
			client.getActionSender().sendMessage(
					"You need a runecrafting level of " + requiredLevel + " .");
			return;
		}

		if (!client.getActionAssistant().isItemInBag(7936)) {
			client.getActionSender().sendMessage(
					"You need "
							+ InstanceDistributor.getItemManager()
									.getItemDefinition(7936).getName()
									.toLowerCase()
							+ " to craft "
							+ InstanceDistributor.getItemManager()
									.getItemDefinition(item).getName()
									.toLowerCase() + "s.");
			return;
		}
		GraphicsProcessor.addNewRequest(client, 186, 100, 0);
		AnimationProcessor.addNewRequest(client, 791, 0);

		if (item == 556) {
			if (client.playerLevel[PlayerConstants.RUNECRAFTING] > 10
					&& client.playerLevel[PlayerConstants.RUNECRAFTING] < 22)
				runeamount = runeamount * 2;
			if (client.playerLevel[PlayerConstants.RUNECRAFTING] > 22
					&& client.playerLevel[PlayerConstants.RUNECRAFTING] < 33)
				runeamount = runeamount * 3;
			if (client.playerLevel[PlayerConstants.RUNECRAFTING] > 33
					&& client.playerLevel[PlayerConstants.RUNECRAFTING] < 44)
				runeamount = runeamount * 4;
			if (client.playerLevel[PlayerConstants.RUNECRAFTING] > 44
					&& client.playerLevel[PlayerConstants.RUNECRAFTING] < 55)
				runeamount = runeamount * 5;
			if (client.playerLevel[PlayerConstants.RUNECRAFTING] > 55
					&& client.playerLevel[PlayerConstants.RUNECRAFTING] < 66)
				runeamount = runeamount * 6;
			if (client.playerLevel[PlayerConstants.RUNECRAFTING] > 66
					&& client.playerLevel[PlayerConstants.RUNECRAFTING] < 77)
				runeamount = runeamount * 7;
			if (client.playerLevel[PlayerConstants.RUNECRAFTING] > 77
					&& client.playerLevel[PlayerConstants.RUNECRAFTING] < 88)
				runeamount = runeamount * 8;
			if (client.playerLevel[PlayerConstants.RUNECRAFTING] > 88
					&& client.playerLevel[PlayerConstants.RUNECRAFTING] < 99)
				runeamount = runeamount * 9;
			if (client.playerLevel[PlayerConstants.RUNECRAFTING] > 98)
				runeamount = runeamount * 10;

		}

		if (item == 555) {
			if (client.playerLevel[PlayerConstants.RUNECRAFTING] > 18
					&& client.playerLevel[PlayerConstants.RUNECRAFTING] < 38)
				runeamount = runeamount * 2;
			if (client.playerLevel[PlayerConstants.RUNECRAFTING] > 37
					&& client.playerLevel[PlayerConstants.RUNECRAFTING] < 57)
				runeamount = runeamount * 3;
			if (client.playerLevel[PlayerConstants.RUNECRAFTING] > 56
					&& client.playerLevel[PlayerConstants.RUNECRAFTING] < 76)
				runeamount = runeamount * 4;
			if (client.playerLevel[PlayerConstants.RUNECRAFTING] > 75
					&& client.playerLevel[PlayerConstants.RUNECRAFTING] < 95)
				runeamount = runeamount * 5;
			if (client.playerLevel[PlayerConstants.RUNECRAFTING] > 94)
				runeamount = runeamount * 6;
		}

		if (item == 557) {
			if (client.playerLevel[PlayerConstants.RUNECRAFTING] > 25
					&& client.playerLevel[PlayerConstants.RUNECRAFTING] < 52)
				runeamount = runeamount * 2;
			if (client.playerLevel[PlayerConstants.RUNECRAFTING] > 51
					&& client.playerLevel[PlayerConstants.RUNECRAFTING] < 78)
				runeamount = runeamount * 3;
			if (client.playerLevel[PlayerConstants.RUNECRAFTING] > 77)
				runeamount = runeamount * 4;
		}

		if (item == 558) {
			if (client.playerLevel[PlayerConstants.RUNECRAFTING] > 13
					&& client.playerLevel[PlayerConstants.RUNECRAFTING] < 28)
				runeamount = runeamount * 2;
			if (client.playerLevel[PlayerConstants.RUNECRAFTING] > 27
					&& client.playerLevel[PlayerConstants.RUNECRAFTING] < 42)
				runeamount = runeamount * 3;
			if (client.playerLevel[PlayerConstants.RUNECRAFTING] > 41
					&& client.playerLevel[PlayerConstants.RUNECRAFTING] < 56)
				runeamount = runeamount * 4;
			if (client.playerLevel[PlayerConstants.RUNECRAFTING] > 55
					&& client.playerLevel[PlayerConstants.RUNECRAFTING] < 70)
				runeamount = runeamount * 5;
			if (client.playerLevel[PlayerConstants.RUNECRAFTING] > 69
					&& client.playerLevel[PlayerConstants.RUNECRAFTING] < 84)
				runeamount = runeamount * 6;
			if (client.playerLevel[PlayerConstants.RUNECRAFTING] > 83
					&& client.playerLevel[PlayerConstants.RUNECRAFTING] < 98)
				runeamount = runeamount * 7;
			if (client.playerLevel[PlayerConstants.RUNECRAFTING] > 97)
				runeamount = runeamount * 8;
		}

		if (item == 554) {
			if (client.playerLevel[PlayerConstants.RUNECRAFTING] > 34
					&& client.playerLevel[PlayerConstants.RUNECRAFTING] < 70)
				runeamount = runeamount * 2;

			if (client.playerLevel[PlayerConstants.RUNECRAFTING] > 69)
				runeamount = runeamount * 3;
		}

		if (item == 559) {
			if (client.playerLevel[PlayerConstants.RUNECRAFTING] > 45
					&& client.playerLevel[PlayerConstants.RUNECRAFTING] < 92)
				runeamount = runeamount * 2;

			if (client.playerLevel[PlayerConstants.RUNECRAFTING] > 91)
				runeamount = runeamount * 3;
		}

		if (item == 562
				&& client.playerLevel[PlayerConstants.RUNECRAFTING] > 74)
			runeamount = runeamount * 2;

		if (item == 561
				&& client.playerLevel[PlayerConstants.RUNECRAFTING] > 91)
			runeamount = runeamount * 2;

		if (item == 564
				&& client.playerLevel[PlayerConstants.RUNECRAFTING] > 58)
			runeamount = runeamount * 2;

		if (item == 9075
				&& client.playerLevel[PlayerConstants.RUNECRAFTING] > 81)
			runeamount = runeamount * 2;

		for (int i = 0; i < essamount; i++)
			client.getActionAssistant().deleteItem(7936,
					client.getActionAssistant().getItemSlot(7936), 1);
		client.getActionAssistant().addSkillXP(
				exp * essamount * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER
						* 1.5, PlayerConstants.RUNECRAFTING);
		client.getActionSender().addItem(item, runeamount);
		if (item == 561 && runeamount >= 50)
			Achievements.getInstance().complete(client, 58);
		client.getActionSender().sendMessage(
				"You craft "
						+ runeamount
						+ " "
						+ InstanceDistributor.getItemManager()
								.getItemDefinition(item).getName() + "s.");
		essamount = -1;
	}

	public static boolean runeCraftArea(Player client) {
		final int[] myLocation = TileManager.currentLocation(client);

		return myLocation[0] >= 3024 && myLocation[0] <= 3054
				&& myLocation[1] >= 4817 && myLocation[1] <= 4846;
	}

}
