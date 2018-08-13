package com.server2.content.skills.thieving;

import com.server2.InstanceDistributor;
import com.server2.content.Achievements;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Areas;
import com.server2.util.Misc;

/**
 * 
 * @author Lukas
 * 
 */
public class StallThieving {

	public static final int[] seedStallReward1 = { 5318, 5096, 5319, 5324 };
	public static final int[] seedStallReward2 = { 5322, 5320, 5097, 5098 };
	public static final int[] runeStallRewards = { 556, 557, 555, 554 };

	public static void stallThieving(Player client, int objectID) {
		if (client.getStunnedTimer() > 0)
			return;
		if (System.currentTimeMillis() < client.thievingTimer)
			return;
		if (client.getActionAssistant().freeSlots() < 1) {
			client.getActionSender().sendMessage(
					"There is not enough space in your inventory.");
			return;
		}

		/*
		 * if(Misc.random(250)==1){ int event = 0; switch (event) { case 0:
		 * Random.activateEvent(client, Random.getRandomEvent(0)); break; } }
		 */
		// seed stall
		if (objectID == 7053) {
			if (client.playerLevel[PlayerConstants.THIEVING] < 27) {
				client.getActionSender()
						.sendMessage(
								"You need a thieving level of 27 to steal from this stall.");
				return;

			}
			if (client.playerLevel[PlayerConstants.THIEVING] >= 27) {
				final int chance = Misc.random(39);
				int reward = 0;
				if (chance <= 25) {
					int kans = Misc.random(4) - 1;
					if (kans < 0)
						kans = 0;
					reward = seedStallReward1[kans];
				}
				if (chance > 25 && chance <= 35) {
					int kans = Misc.random(4) - 1;
					if (kans < 0)
						kans = 0;
					reward = seedStallReward1[kans];
				}
				if (chance > 35 && chance <= 38)
					reward = 5323;
				if (chance == 39)
					reward = 5323;
				final String rewardName = InstanceDistributor.getItemManager()
						.getItemDefinition(reward).getName();
				client.thievingTimer = System.currentTimeMillis() + 600 * 7;
				AnimationProcessor.createAnimation(client, 881);
				client.getActionSender().addItem(reward, 1);
				client.getActionSender().sendMessage(
						"You steal a " + rewardName.toLowerCase()
								+ " from the seed stall.");
				client.getActionAssistant().addSkillXP(
						20 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
						PlayerConstants.THIEVING);
			}
		}
		// wine stall
		if (objectID == 14011) {

			if (client.playerLevel[PlayerConstants.THIEVING] < 22) {
				client.getActionSender()
						.sendMessage(
								"You need a thieving level of 22 to steal from this stall.");
				return;

			}
			if (client.playerLevel[PlayerConstants.THIEVING] >= 22) {
				int reward = 0;
				final int chance = Misc.random(5);
				if (chance <= 3)
					reward = 1993;
				if (chance == 5 || chance == 4)
					reward = 245;
				final String rewardName = InstanceDistributor.getItemManager()
						.getItemDefinition(reward).getName();
				client.thievingTimer = System.currentTimeMillis() + 600 * 7;
				AnimationProcessor.createAnimation(client, 881);
				client.getActionSender().addItem(reward, 1);
				if (reward == 245)
					Achievements.getInstance().complete(client, 15);
				client.getActionSender().sendMessage(
						"You steal a " + rewardName.toLowerCase()
								+ " from the wine stall.");
				client.getActionAssistant().addSkillXP(
						17.5 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
						PlayerConstants.THIEVING);

			}

		}
		// scimitar stall
		if (objectID == 4878)
			if (Areas.isEdgeville(client.getPosition())) {
				if (client.playerLevel[PlayerConstants.THIEVING] < 80) {
					client.getActionSender()
							.sendMessage(
									"You need a thieving level of 80 to steal from this stall.");
					return;
				}
				final int randomReward = 5000 + Misc.random(7000);
				client.thievingTimer = System.currentTimeMillis() + 600 * 4;
				AnimationProcessor.createAnimation(client, 881);
				client.getActionSender().addItem(995, randomReward);
				client.getActionSender().sendMessage(
						"You steal " + randomReward + " coins from the stall.");
				client.getActionAssistant().addSkillXP(
						100 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
						PlayerConstants.THIEVING);
			} else {
				if (client.playerLevel[PlayerConstants.THIEVING] < 65) {
					client.getActionSender()
							.sendMessage(
									"You need a thieving level of 65 to steal from this stall.");
					return;

				}
				if (client.playerLevel[PlayerConstants.THIEVING] >= 65) {
					int reward = 0;
					final int chance = Misc.random(110);
					if (chance <= 20)
						reward = 1321;
					else if (chance > 20 && chance <= 38)
						reward = 1323;
					else if (chance > 38 && chance <= 56)
						reward = 1325;
					else if (chance > 56 && chance <= 72)
						reward = 1327;
					else if (chance > 72 && chance <= 86)
						reward = 1329;
					else if (chance > 86 && chance <= 98)
						reward = 1331;
					else if (chance > 98 && chance <= 108)
						reward = 1333;
					else if (chance > 108)
						reward = 4587;
					final String rewardName = InstanceDistributor
							.getItemManager().getItemDefinition(reward)
							.getName();
					client.thievingTimer = System.currentTimeMillis() + 600 * 10;
					AnimationProcessor.createAnimation(client, 881);
					client.getActionSender().addItem(reward, 1);
					client.getActionSender().sendMessage(
							"You steal a " + rewardName.toLowerCase()
									+ " from the scimitar stall.");
					client.getActionAssistant().addSkillXP(
							100 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
							PlayerConstants.THIEVING);
				}
			}
		// rune stall
		if (objectID == 4877) {

			if (client.playerLevel[PlayerConstants.THIEVING] < 65) {
				client.getActionSender()
						.sendMessage(
								"You need a thieving level of 65 to steal from this stall.");
				return;

			}
			if (Areas.isEdgeville(client.getPosition())) {
				final int randomReward = 3000 + Misc.random(5000);
				client.thievingTimer = System.currentTimeMillis() + 600 * 4;
				AnimationProcessor.createAnimation(client, 881);
				client.getActionSender().addItem(995, randomReward);
				client.getActionSender().sendMessage(
						"You steal " + randomReward + " coins from the stall.");
				client.getActionAssistant().addSkillXP(
						80 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
						PlayerConstants.THIEVING);
			} else if (client.playerLevel[PlayerConstants.THIEVING] >= 65) {
				final int chance = Misc.random(6);
				int reward = 0;
				int amount = 0;
				if (chance <= 5) {
					int kans = Misc.random(4) - 1;
					if (kans < 0)
						kans = 0;
					reward = runeStallRewards[kans];
					amount = 10 + Misc.random(50);
				}
				if (chance == 6) {
					reward = 563;
					amount = 5 + Misc.random(10);
				}
				final String rewardName = InstanceDistributor.getItemManager()
						.getItemDefinition(reward).getName();
				client.thievingTimer = System.currentTimeMillis() + 600 * 10;
				AnimationProcessor.createAnimation(client, 881);
				client.getActionSender().addItem(reward, amount);
				client.getActionSender().sendMessage(
						"You steal some " + rewardName.toLowerCase()
								+ "s from the rune stall.");
				client.getActionAssistant().addSkillXP(
						100 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
						PlayerConstants.THIEVING);
			}
		}
		// gem stall
		if (objectID == 2562) {

			if (client.playerLevel[PlayerConstants.THIEVING] < 75) {
				client.getActionSender()
						.sendMessage(
								"You need a thieving level of 75 to steal from this stall.");
				return;

			}
			if (client.playerLevel[PlayerConstants.THIEVING] >= 75) {
				final int chance = Misc.random(40);
				int reward = 0;
				if (chance == 1)
					reward = 1615;
				else if (chance > 1 && chance <= 7)
					reward = 1631;
				else if (chance > 7 && chance <= 15)
					reward = 1619;
				else if (chance > 15 && chance <= 25)
					reward = 1621;
				else if (chance > 25 && chance <= 40)
					reward = 1623;
				else
					reward = 1621;
				final String rewardName = InstanceDistributor.getItemManager()
						.getItemDefinition(reward).getName();
				client.thievingTimer = System.currentTimeMillis() + 600 * 7;
				AnimationProcessor.createAnimation(client, 881);
				client.getActionSender().addItem(reward, 1);
				client.getActionSender().sendMessage(
						"You steal a " + rewardName.toLowerCase()
								+ " from the gem stall.");
				client.getActionAssistant().addSkillXP(
						160 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
						PlayerConstants.THIEVING);
			}
		}

		// coin stall 1
		if (objectID == 6163) {
			final int amount = 500 + Misc.random(2500);
			client.thievingTimer = System.currentTimeMillis() + 600 * 6;
			AnimationProcessor.createAnimation(client, 881);
			client.getActionSender().addItem(995, amount);
			client.getActionSender().sendMessage(
					"You steal some coins from the baker stall.");
			client.getActionAssistant().addSkillXP(
					10 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
					PlayerConstants.THIEVING);

		}

		// coin stall 2

		if (objectID == 6165) {
			if (client.playerLevel[PlayerConstants.THIEVING] < 40) {
				client.getActionSender()
						.sendMessage(
								"You need a thieving level of 40 to steal from this stall.");
				return;

			}
			if (client.playerLevel[PlayerConstants.THIEVING] >= 40) {
				final int amount = 2500 + Misc.random(4000);
				client.thievingTimer = System.currentTimeMillis() + 600 * 6;
				AnimationProcessor.createAnimation(client, 881);
				client.getActionSender().addItem(995, amount);
				client.getActionSender().sendMessage(
						"You steal some coins from the silk stall.");
				client.getActionAssistant().addSkillXP(
						42 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
						PlayerConstants.THIEVING);
			}

		}
		// coin stall 3
		if (objectID == 6164) {
			if (client.playerLevel[PlayerConstants.THIEVING] < 60) {
				client.getActionSender()
						.sendMessage(
								"You need a thieving level of 60 to steal from this stall.");
				return;

			}
			if (client.playerLevel[PlayerConstants.THIEVING] >= 60) {
				final int amount = 3000 + Misc.random(6000);
				client.thievingTimer = System.currentTimeMillis() + 600 * 7;
				AnimationProcessor.createAnimation(client, 881);
				client.getActionSender().addItem(995, amount);
				client.getActionSender().sendMessage(
						"You steal some coins from the fur stall.");
				client.getActionAssistant().addSkillXP(
						73 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
						PlayerConstants.THIEVING);
			}

		}
		// coin stall 4
		if (objectID == 6162) {
			if (client.playerLevel[PlayerConstants.THIEVING] < 90) {
				client.getActionSender()
						.sendMessage(
								"You need a thieving level of 90 to steal from this stall.");
				return;

			}
			if (client.playerLevel[PlayerConstants.THIEVING] >= 90) {
				final int amount = 3500 + Misc.random(8000);
				client.thievingTimer = System.currentTimeMillis() + 600 * 9;
				AnimationProcessor.createAnimation(client, 881);
				client.getActionSender().addItem(995, amount);
				client.getActionSender().sendMessage(
						"You steal some coins from the silver stall.");
				client.getActionAssistant().addSkillXP(
						260 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
						PlayerConstants.THIEVING);
			}

		}
		// coin stall 5
		if (objectID == 6166) {
			if (client.playerLevel[PlayerConstants.THIEVING] < 50) {
				client.getActionSender()
						.sendMessage(
								"You need a thieving level of 50 to steal from this stall.");
				return;

			}
			if (client.playerLevel[PlayerConstants.THIEVING] >= 50) {
				final int amount = 1000 + Misc.random(10000);
				client.thievingTimer = System.currentTimeMillis() + 600 * 9;
				AnimationProcessor.createAnimation(client, 881);
				client.getActionSender().addItem(995, amount);
				client.getActionSender().sendMessage(
						"You steal some coins from the spice stall.");
				client.getActionAssistant().addSkillXP(
						260 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
						PlayerConstants.THIEVING);
			}

		}
	}
}
