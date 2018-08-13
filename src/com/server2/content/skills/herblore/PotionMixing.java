package com.server2.content.skills.herblore;

import com.server2.InstanceDistributor;
import com.server2.content.Achievements;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;

/**
 * 
 * @author Rene/ Lukas
 * 
 */
public class PotionMixing {

	public static final int VIAL = 227;

	/**
	 * Herb - Secondary Ingredient - Unf potion - Finished Potion - level
	 * Requirement - exp gained.
	 */
	public static int[][] mixtures = {
			{ 2481, 241, 2483, 2454, 69, 157, 5 },
			{ 249, 221, 91, 121, 1, 25 }, // attack potion
			{ 251, 235, 93, 175, 5, (int) 37.5 }, // antipoison potion
			{ 253, 225, 95, 133, 12, 50 }, // strength potion
			{ 255, 1975, 97, 3010, 26, 68 }, // energy potion
			{ 255, 9736, 97, 9741, 36, 84 }, // combat potion
			{ 255, 223, 97, 127, 22, (int) 62.5 }, // Restore_potion
			{ 257, 231, 99, 139, 38, (int) 87.5 }, // prayer potion
			{ 259, 221, 101, 145, 45, 100 }, // super attack
			{ 261, 2970, 103, 3018, 52, (int) 117.5 },// Super_energy
			{ 263, 225, 105, 157, 55, 125 }, // super strength
			{ 265, 239, 107, 163, 66, 150 }, // super def
			{ 267, 245, 109, 169, 72, (int) 162.5 },// ranging potion
			{ 269, 247, 111, 189, 78, 175 }, // zamorak brew
			{ 3000, 223, 3004, 3026, 63, (int) 142.5 }, // Super restore
			{ 2481, 3138, 2483, 3042, 76, 173 }, // magic potion
			{ 2998, 6693, 3002, 6687, 81, 180 }, // saradomin brew
			{ 123456, 5972, 3018, 15301, 84, 200 }, // recover special potion
			{ 124578, 261, 145, 15309, 88, 220 }, // extreme attack
			{ 124575, 267, 157, 15313, 89, 230 }, // extreme strength
			{ 124574, 2481, 163, 15317, 90, 240 }, // extreme defence
			{ 457541, 9594, 3042, 15321, 91, 250 }, // extreme defence
			{ 457855, 6812, 139, 15329, 94, 270 }, // super prayer
			{ 457827, 12539, 169, 15325, 92, 260 }, // extreme range
			{ 45457, 15317, 269, 15333, 96, 1000 }, // overload
			{ 21624, 21622, 21628, 21632, 94, 190 },
			{ 45648, 4621, 2454, 15305, 85, 210 },

	};

	public static boolean isExtraMixture(Player client, int item) {
		for (final int[] mixture : mixtures)
			if (mixture[1] == item || 15309 == item || 15313 == item
					|| 15321 == item || 15325 == item)
				return true;
		return false;
	}

	public static boolean isUnFinishedPotion(Player client, int item) {
		for (final int[] mixture : mixtures)
			if (mixture[2] == item)
				return true;
		return false;
	}

	public static void itemGrinding(Player client, int itemUsed, int useWith) {
		int theItem = 0;
		if (itemUsed == 233)
			theItem = useWith;
		else if (useWith == 233)
			theItem = itemUsed;
		if (theItem == 9735) {
			client.getActionAssistant().deleteItem(9735, 1);
			client.getActionSender().addItem(9736, 1);
			AnimationProcessor.addNewRequest(client, 364, 0);
			client.getActionSender().sendMessage(
					"You grind the desert goat horn to dust.");

		}
		if (theItem == 5075) {
			client.getActionAssistant().deleteItem(5075, 1);
			client.getActionSender().addItem(6693, 1);
			AnimationProcessor.addNewRequest(client, 364, 0);
			client.getActionSender().sendMessage(
					"You grind the bird nest to dust.");

		}
		if (theItem == 237) {
			client.getActionAssistant().deleteItem(237, 1);
			client.getActionSender().addItem(235, 1);
			AnimationProcessor.addNewRequest(client, 364, 0);
			client.getActionSender().sendMessage(
					"You grind the unicorn horn to dust.");

		}
		if (theItem == 4698) {
			client.getActionAssistant().deleteItem(4698, 1);
			client.getActionSender().addItem(9594, 1);
			AnimationProcessor.addNewRequest(client, 364, 0);
			client.getActionSender().sendMessage(
					"You grind the mud rune to dust.");

		}
		if (theItem == 243) {
			client.getActionAssistant().deleteItem(243, 1);
			client.getActionSender().addItem(241, 1);
			AnimationProcessor.addNewRequest(client, 364, 0);
			client.getActionSender().sendMessage(
					"You grind the dragon scale to dust.");

		}
	}

	public static void makeFinished(Player client, int item) {
		int i;
		for (i = 0; i < mixtures.length; i++)
			if (mixtures[i][2] == item) {

				if (client.playerLevel[PlayerConstants.HERBLORE] < mixtures[i][4]) {

					client.getActionSender().sendMessage(
							"You need a herblore level of " + mixtures[i][4]
									+ " to make this potion.");
					return;
				}
				if (mixtures[i][3] != 15325 && mixtures[i][3] != 15333)
					if (client.getActionAssistant().isItemInBag(mixtures[i][1])
							&& client.getActionAssistant().isItemInBag(
									mixtures[i][2])) {
						client.getActionAssistant().deleteItem(mixtures[i][1],
								1);
						client.getActionAssistant().deleteItem(mixtures[i][2],
								1);
						client.getActionSender().addItem(mixtures[i][3], 1);
						if (mixtures[i][3] == 2454) {
							client.progress[5]++;
							if (client.progress[5] >= 3)
								Achievements.getInstance().complete(client, 5);
							else
								Achievements.getInstance()
										.turnYellow(client, 5);
						}
						if (mixtures[i][3] == 668)
							Achievements.getInstance().complete(client, 40);
						AnimationProcessor.addNewRequest(client, 363, 0);
						final String potionName = InstanceDistributor
								.getItemManager()
								.getItemDefinition(mixtures[i][3]).getName();
						client.getActionSender().sendMessage(
								"You make a " + potionName.toLowerCase() + ".");
						client.getActionAssistant().addSkillXP(
								mixtures[i][5] * client.multiplier,
								PlayerConstants.HERBLORE);
						break;
					}
				if (mixtures[i][3] == 15325)
					if (client.getActionAssistant().playerHasItem(12539, 5)) {
						client.getActionAssistant().deleteItem(12539, 5);
						client.getActionAssistant().deleteItem(mixtures[i][2],
								1);
						client.getActionSender().addItem(mixtures[i][3], 1);
						AnimationProcessor.addNewRequest(client, 363, 0);
						final String potionName = InstanceDistributor
								.getItemManager()
								.getItemDefinition(mixtures[i][3]).getName();
						client.getActionSender().sendMessage(
								"You make a " + potionName.toLowerCase() + ".");
						client.getActionAssistant().addSkillXP(
								mixtures[i][5] * client.multiplier,
								PlayerConstants.HERBLORE);

					} else {
						client.getActionSender()
								.sendMessage(
										"You need 5 grenwall spikes to make a extreme range potion");
						return;
					}

				if (mixtures[i][3] == 15333)
					if (client.getActionAssistant().isItemInBag(15309)
							&& client.getActionAssistant().isItemInBag(15313)
							&& client.getActionAssistant().isItemInBag(15317)
							&& client.getActionAssistant().isItemInBag(15321)
							&& client.getActionAssistant().isItemInBag(15325)
							&& client.getActionAssistant().isItemInBag(
									mixtures[i][2])) {
						client.getActionAssistant().deleteItem(15309, 1);
						client.getActionAssistant().deleteItem(15313, 1);
						client.getActionAssistant().deleteItem(15317, 1);
						client.getActionAssistant().deleteItem(15321, 1);
						client.getActionAssistant().deleteItem(15325, 1);
						AnimationProcessor.addNewRequest(client, 363, 0);
						client.getActionAssistant().deleteItem(mixtures[i][2],
								1);
						client.getActionSender().addItem(mixtures[i][3], 1);
						final String potionName = InstanceDistributor
								.getItemManager()
								.getItemDefinition(mixtures[i][3]).getName();
						client.getActionSender().sendMessage(
								"You make a " + potionName.toLowerCase() + ".");
						client.getActionSender().sendMessage(
								"You make a " + potionName.toLowerCase() + ".");
						client.getActionAssistant().addSkillXP(
								mixtures[i][5] * client.multiplier,
								PlayerConstants.HERBLORE);
						client.progress[53]++;
						if (client.progress[53] >= 10)
							Achievements.getInstance().complete(client, 53);
						else
							Achievements.getInstance().turnYellow(client, 53);
						break;
					}
			}
	}

	public static void makeUnfinished(Player client, int itemUsed, int onItem) {

		if (itemUsed == VIAL)
			for (final int[] mixture : mixtures)
				if (mixture[0] == onItem) {
					if (client.playerLevel[PlayerConstants.HERBLORE] < mixture[4]) {
						client.getActionSender().sendMessage(
								"You need a herblore level of " + mixture[4]
										+ " to make this potion.");
						return;
					}

					if (client.getActionAssistant().isItemInBag(mixture[0])
							&& client.getActionAssistant().isItemInBag(VIAL)) {
						AnimationProcessor.addNewRequest(client, 363, 0);
						client.getActionAssistant().deleteItem(mixture[0], 1);
						client.getActionAssistant().deleteItem(VIAL, 1);
						client.getActionSender().addItem(mixture[2], 1);
						final String herbName = InstanceDistributor
								.getItemManager().getItemDefinition(mixture[0])
								.getName();
						client.getActionSender().sendMessage(
								"You mix the " + herbName.toLowerCase()
										+ " with vial of water.");
						break;

					}
				}
	}

	public void grind(Player client) {

	}
}