package com.server2.content.skills.dungeoneering;

import java.util.ArrayList;

import com.server2.InstanceDistributor;
import com.server2.content.Achievements;
import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.Item;
import com.server2.model.combat.HitExecutor;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Language;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Areas;
import com.server2.util.Misc;
import com.server2.world.objects.GameObject;
import com.server2.world.objects.GameObject.Face;
import com.server2.world.objects.ObjectManager;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class Dungeoneering {

	/**
	 * Instances the Dungeoneering Class
	 */
	public static Dungeoneering INSTANCE = new Dungeoneering();

	/**
	 * An ArrayList which holds the players which are playing dungeoneering.
	 * 
	 */
	public static ArrayList<String> playersDunging = new ArrayList<String>();

	public static int[] coords = { 3233, 9315, 1879, 4620, 3056, 4993 };

	private static int[] x1 = { 3246, 3260, 3255, 3255, 3252, 3252, 3243, 3234,
			3223, 3219, 3206, 3216, 3212, 3204, 3220, 3225, 3222, 3207, 3202,
			3215, 3242, 3241, 3261, 3257, 3251, 3249, 3244, 3255, 3258, 3242 };

	private static int[] y1 = { 9326, 9327, 9332, 9335, 9332, 9335, 9331, 9331,
			9324, 9331, 9332, 9321, 9318, 9310, 9302, 9289, 9284, 9287, 9298,
			9295, 9303, 9284, 9286, 9301, 9300, 9312, 9316, 9309, 9319, 9322 };

	private static int[] floor2Npcs = { 10545, 10546, 10547, 10548, 10508,
			10509, 10510, 10511, 10246, 10247, 10248, 10249, 10251, 10252,
			10253, 10254, 10255, 10258, 10259, 10260, 10261, 10262, 10264,
			10265, 10266, 10267, 10269, 10271, 10272, 10273, 10275, 10276,
			10277, 10278, 10279, 10280, 10281, 10283, 10286, 10287, 10289,
			10290, 10293, 10294, 10296, 10297, 10299, 10300, 10301, 10302,
			10303, 10304, 10305, 10306, 10307, 10320, 10321, 10322, 10323,
			10324, 10325, 10326, 10327, 10328, 10329, 10330, 10331, 10332,
			10333, 10334, 10335, 10336, 10337, 10338, 10339, 10340, 10341,
			10342, 10343, 10344, 10345, 10346, 10347, 10348, 10349, 10350,
			10351, 10352, 10353, 10354, 10355, 10356, 10357, 10358, 10359,
			10360, 10361, 10362, 10549, 10550, 10551, 10552, 10553, 10554,
			10555, 10556, 10557, 10558, 10559, 10560, 10561, 10562, 10563,
			10564, 10565, 10566, 10567, 10568, 10569, 10570, 10571, 10572,
			10573, 10574, 10575, 10576, 10577, 10578, 10579, 10580, 10581,
			10582, 10583, 10584, 10585, 10586, 10587, 10588, 10589, 10590,
			10591, 10592, 10593, 10594, 10595, 10596, 10597, 10598, 10599,
			10600, 10601, 10602, };

	/**
	 * Gets the Dungeoneering Instance.
	 */
	public static Dungeoneering getInstance() {
		return INSTANCE;
	}

	public long lastDungTele;

	private static int[] floor1LowNpcs = { 10397, 10400, 10402, 10409, 10420,
			10422, 10430, 10439, 10441, 10443, 10320, 10324, 10325, 10335,
			10336, 10560, 10564, 10568, 10573, 10583 };

	private static int[] x2 = { 1860, 1862, 1871, 1871, 1883, 1887, 1899, 1907,
			1908, 1897, 1887, 1886, 1871, 1862, 1862, 1862, 1860, 1860, 1877 };

	private static int[] y2 = { 4623, 4613, 4614, 4612, 4613, 4621, 4620, 4616,
			4611, 4650, 4648, 4665, 4664, 4664, 4665, 4666, 4659, 4652, 4659 };

	private static int[] dungBossesFloor1 = { 10110, 10044 /*
															 * , 9916, 9934,
															 * 10064, 10116,
															 * 9989
															 */};

	private static int[] dungBossesFloor2 = { 10110, 10044, 9916, 9934, 10064,
			10116, 9989 };

	/**
	 * The npcs of floor 3
	 */
	public static int[] floor3Npcs = { 10545, 10546, 10547, 10548, 10508,
			10509, 10510, 10511, 10360, 10361, 10362, 10600, 10601, 10602 };

	/**
	 * The X and Y Coords of Floor 3 npcs
	 */
	public static int[][] floor3Coords = { { 3045, 4999 }, { 3045, 4997 },
			{ 3042, 4997 }, { 3042, 4999 }, { 3035, 4997 }, { 3034, 5001 },
			{ 3031, 5001 }, { 3031, 4997 }, { 3023, 5001 }, { 3017, 5002 },
			{ 3011, 5002 }, { 3001, 5000 }, { 2995, 4998 }, { 2985, 5000 },
			{ 2978, 5005 }, { 2964, 5002 }, { 2958, 5011 }, { 2963, 5022 },
			{ 2962, 5026 }, { 2964, 5026 }, { 2958, 2026 }, { 2956, 2026 },
			{ 2953, 5026 }, { 2958, 5039 }, { 2959, 5039 }, { 2965, 5046 },
			{ 2962, 5046 }, { 2962, 5052 }, { 2965, 5057 }, { 2964, 5062 },
			{ 2959, 5061 }, { 2956, 5081 },

	};

	/**
	 * The dungeoneering bosses for floor 3
	 */
	public static int[] dungBossesFloor3 = { 10110, 10044, 9916, 9934, 10064,
			10116, 9989 };

	public void bind(Player client, int bindId) {
		if (bindId == 11696 || bindId == 11694 || bindId == 11698
				|| bindId == 11700) {
			client.getActionSender().sendMessage("You cannot bind these.");
			return;
		}
		if (bindId == client.bound1 || bindId == client.bound2
				|| bindId == client.bound3 || bindId == client.bound4) {
			client.getActionSender().sendMessage(
					"You already have this item binded.");
			return;
		}
		if (client.bound1 == 0) {
			client.bound1 = bindId;
			client.getActionSender().sendMessage(
					"You succesfully bind your "
							+ InstanceDistributor.getItemManager()
									.getItemDefinition(bindId).getName() + ".");
		} else if (client.bound2 == 0) {
			client.bound2 = bindId;
			client.getActionSender().sendMessage(
					"You succesfully bind your "
							+ InstanceDistributor.getItemManager()
									.getItemDefinition(bindId).getName() + ".");
		} else if (client.bound3 == 0) {
			client.bound3 = bindId;
			client.getActionSender().sendMessage(
					"You succesfully bind your "
							+ InstanceDistributor.getItemManager()
									.getItemDefinition(bindId).getName() + ".");
		} else if (client.bound4 == 0) {
			client.bound4 = bindId;
			client.getActionSender().sendMessage(
					"You succesfully bind your "
							+ InstanceDistributor.getItemManager()
									.getItemDefinition(bindId).getName() + ".");
		} else {
			client.getActionSender().sendMessage(
					"You do not have anymore binding space available.");
			client.getActionSender().sendMessage(
					"You can reset binding items via your quest menu.");
		}

	}

	public void completeDungeon(final Player client) {
		if (playersDunging.contains(client.getUsername()))
			playersDunging.remove(client.getUsername());
		client.lastDungEntry = System.currentTimeMillis();
		double exp = DungeoneeringFormulae.dungeoneeringExpMultiplier(client);
		if (exp <= 1) {
			System.out.println("below 0");
			exp = 1 + Misc.random(150000);
		}
		client.dungTokens = (int) (client.dungTokens + exp / 10);
		;
		client.getActionSender().sendMessage(
				"Congratulations, you completed a dungeon of level : "
						+ client.getFloor() + ".");
		client.getActionSender().sendMessage(
				"You received @red@" + Math.round(exp)
						+ "@bla@ dungeoneering exp. You received @red@"
						+ Math.round(exp / 20) + " @bla@ tokens as a reward.");
		if (client.getFloor() == 1)
			Achievements.getInstance().complete(client, 12);
		client.getActionAssistant().addSkillXP(exp,
				PlayerConstants.DUNGEONEERING);
		if (client.floor1() || client.floor3())
			NPC.removeDungeoneeringNPC(client);
		else if (client.floor2())
			NPC.removeDungeoneeringNPC2(client);
		if (client.floor3())
			if (!client.eaten)
				Achievements.getInstance().complete(client, 49);
		deleteDungEquipment(client);
		client.setFloor(0);
		client.getPlayerTeleportHandler().forceTeleport(3486, 3090, 0);
		client.setHeightLevel(0);
		client.prestige++;
		if (client.prestige > 100) {
			client.prestige = 100;
			client.getActionSender()
					.sendMessage(
							"Congratulations, you've received the maximum of 100 prestige!");
		}
		client.essTaken = false;
		client.foodTaken = false;
		client.runesTaken = false;
		client.attempted1 = false;
		client.failedOnes = 0;
		client.bawsed = false;
		client.npcsKilled = 0;
		client.lighted = false;
		client.getPrayerHandler().resetAllPrayers();
		client.hitpoints = client.calculateMaxHP();
		client.playerLevel[3] = client.calculateMaxHP();
		client.playerLevel[5] = client.getLevelForXP(client.playerXP[5]);
		client.getActionAssistant().refreshSkill(3);
		client.getActionAssistant().refreshSkill(5);
		client.isDunging = false;
		client.portalEnter = 0;
		client.setSpecialAmount(100);

		client.sendDungQuestInterface = false;
		client.getActionSender().sendString(
				"@whi@Dungeoneering tokens : @gre@"
						+ Misc.formatAmount2(client.dungTokens), 16042);

		client.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				client.sendQuestInterface();
				container.stop();
			}

			@Override
			public void stop() {

			}
		}, 4);
	}

	/**
	 * Handles objects which deal damage
	 */
	public void damageObjectProcession(Player client) {
		if (client.getHeightLevel() != client.getIndex() * 4 + 1)
			return;
		if (client.getAbsX() == 2969)
			if (client.getAbsY() == 5019 || client.getAbsY() == 5018
					|| client.getAbsY() == 5017 || client.getAbsY() == 5016)
				client.getPlayerTeleportHandler().forceTeleport(2967,
						client.getAbsY(), client.getIndex() * 4 + 1);
		if (client.getAbsY() == 5025)
			if (client.getAbsX() > 2953 && client.getAbsX() < 2966)
				client.getPlayerTeleportHandler().forceTeleport(
						client.getAbsX(), 5027, client.getIndex() * 4 + 1);
		if (client.getAbsX() == 2957 && client.getAbsY() == 5073)
			client.getPlayerTeleportHandler().forceTeleport(client.getAbsX(),
					5076, client.getIndex() * 4 + 1);
		if (client.getAbsX() == 3041 && client.getAbsY() == 4997
				|| client.getAbsX() == 3041 && client.getAbsY() == 4999) {
			client.getPlayerTeleportHandler().forceTeleport(3040, 4997,
					client.getIndex() * 4 + 1);
			client.dungObjectHitCounter++;
			if (client.dungObjectHitCounter == 2) {
				HitExecutor.addNewHit(client, client, CombatType.RECOIL,
						5 + Misc.random(10), 0);
				client.dungObjectHitCounter = 0;
			}
		}
	}

	public void deleteDungEquipment(Player client) {
		for (int i = 0; i < 14; i++)
			client.getEquipment().deleteEquipment(i);
		for (int i = 0; i < client.playerItems.length; i++)
			client.getActionAssistant().deleteItem(client.playerItems[i] - 1,
					client.playerItemsN[i]);
		client.appearanceUpdateRequired = true;
	}

	/**
	 * Handles the chest on floor 3
	 */
	public void dungChestFloor3(Player client) {
		if (client.floor3()) {
			final int looper = client.getActionAssistant().freeSlots();
			client.getActionSender().addItem(new Item(3024));
			client.getActionSender().addItem(new Item(3024));
			for (int i = 3; i < looper; i++)
				client.getActionSender().addItem(new Item(18155));
			client.getActionSender().addItem(new Item(15332));
			client.getActionSender().sendMessage(
					"You take some "
							+ InstanceDistributor.getItemManager()
									.getItemDefinition(18155).getName()
							+ " and an overload potion.");
		}
	}

	/**
	 * Spawns a boss for Dungeoneering floor 3.
	 * 
	 * @param client
	 */
	public void dungFloor3Boss(Player client) {
		final int randomBaws = dungBossesFloor3[Misc
				.random(dungBossesFloor3.length - 1)];
		NPC.spawnDungeoneeringNPC(randomBaws, 2953, 5107,
				client.getIndex() * 4 + 1, client);
		client.getActionSender().sendMessage(
				"Your boss is : @red@"
						+ InstanceDistributor.getNPCManager()
								.getNPCDefinition(randomBaws).getName()
						+ "@bla@.");
		client.getActionSender().sendMessage(
				"Don't forget to bind any items if neccesary!");
		client.dungeoneeringBaws = randomBaws;
	}

	public void forceLeaveDungeon(final Player client) {
		if (client == null)
			return;
		if (System.currentTimeMillis() - client.lastDungEntry < 4000)
			return;
		if (System.currentTimeMillis() - client.dungLeaveTimer < 10000) {
			client.getActionSender().sendMessage(
					"You cannot leave in the first 10 seconds.");
			return;
		}
		if (System.currentTimeMillis() - client.dungForceQuitSpam < 600)
			return;
		client.portalEnter = 0;
		client.dungForceQuitSpam = System.currentTimeMillis();
		client.getActionSender()
				.sendMessage(
						"You force left the dungeon, therefore you didn't receive any exp.");

		client.setFloor(0);
		client.getPlayerTeleportHandler().forceTeleport(3486, 3090, 0);
		client.prestige = 0;
		client.essTaken = false;
		client.foodTaken = false;
		client.runesTaken = false;
		client.attempted1 = false;
		client.failedOnes = 0;
		client.bawsed = false;
		client.lighted = false;
		client.isDunging = false;
		client.getPrayerHandler().resetAllPrayers();
		if (client.floor1() || client.floor3())
			NPC.removeDungeoneeringNPC(client);
		else if (client.floor2())
			NPC.removeDungeoneeringNPC2(client);
		deleteDungEquipment(client);
		client.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				client.sendQuestInterface();
				container.stop();
			}

			@Override
			public void stop() {
				// TODO Auto-generated method stub

			}
		}, 5);

	}

	/*
	 * private void spawnFloor3Npcs(Player client) {
	 * 
	 * for(int i = 0; i < x3.length; i++) {
	 * NPC.spawnDungeoneeringNPC(floor3Npcs[Misc.random(floor3Npcs.length - 1)],
	 * x3[i], y3[i], client.getUserID() * 4, client); }
	 * 
	 * 
	 * 
	 * }
	 */

	public void giveRunes(Player client) {
		if (client.runesTaken) {
			client.getActionSender().sendMessage(
					"You've already taken your load of supplies.");
			return;
		}
		for (int i = 0; i < 2; i++)
			client.getActionSender().addItem(3024, 1);
		client.getActionSender().addItem(157, 1);
		client.getActionSender().addItem(145, 1);
		client.getActionSender().addItem(169, 1);
		client.getActionSender().addItem(3042, 1);
		client.getActionSender().addItem(555, 1000 + Misc.random(3000));
		client.getActionSender().addItem(556, 1000 + Misc.random(3000));
		client.getActionSender().addItem(560, 200 + Misc.random(1500));
		client.getActionSender().addItem(565, 200 + Misc.random(1500));
		client.getActionSender().addItem(9075, 200 + Misc.random(500));
		client.getActionSender().addItem(557, 200 + Misc.random(1500));
		client.getActionSender().addItem(554, 200 + Misc.random(1500));
		client.getActionSender().addItem(892, 200 + Misc.random(1500));
		client.getActionSender().addItem(9241, 200 + Misc.random(150));
		client.getActionSender().addItem(861, 1);
		client.getActionSender().addItem(9185, 1);
		client.getActionSender().addItem(4675, 1);
		client.getActionSender().sendMessage("You take your load of supplies.");
		client.dialogueAction = 0;
		client.getActionSender().sendWindowsRemoval();
		client.runesTaken = true;
	}

	public void handleBookCase(Player client) {
		if (!client.floor2())
			return;
		client.getActionSender().selectOption("Select", "Food",
				"Ammunition & Runes");
		client.dialogueAction = 145857;

	}

	/**
	 * A method that handles the buying from the shop.
	 */
	public void handleBuying(Player client, int price, int itemId) {
		if (client.getActionAssistant().freeSlots() == 0) {
			client.getActionSender().sendMessage(
					"You need to have atleast one free inventory spot.");
			client.getActionSender().sendWindowsRemoval();
			client.dialogueAction = 0;
			return;
		}
		if (client.dungTokens < price) {
			client.getActionSender().sendMessage(
					"You need atleast @red@"
							+ price
							+ "@bla@ tokens to purchase a "
							+ InstanceDistributor.getItemManager()
									.getItemDefinition(itemId).getName()
							+ ". You only have " + client.dungTokens + ".");
			client.getActionSender().sendWindowsRemoval();
			client.dialogueAction = 0;
			return;
		}
		if (client.dungTokens >= price) {
			client.getActionSender().addItem(itemId, 1);
			client.dungTokens = client.dungTokens - price;
			client.getActionSender().sendMessage(
					"You succesfully purchased a "
							+ InstanceDistributor.getItemManager()
									.getItemDefinition(itemId).getName()
							+ ". You have " + client.dungTokens
							+ " tokens left.");
			client.getActionSender().sendWindowsRemoval();
		}
		client.dialogueAction = 0;
	}

	public void handleEssence(Player client, int amount) {
		if (client.essTaken) {
			client.getActionSender().sendMessage(
					"You've already taken your load of essence.");
			return;
		}
		for (int i = 0; i < amount; i++)
			client.getActionSender().addItem(7936, amount);
		client.getActionSender().sendMessage(
				"You take " + amount + " pure essence.");
		client.essTaken = true;
	}

	@SuppressWarnings("unused")
	public void handleFloor2Portal(Player client) {
		if (System.currentTimeMillis() - client.spamClickDungPortal < 5000)
			return;
		final int chance = 2;
		if (chance == 1 && client.npcsKilled < x2.length) {
			HitExecutor.addNewHit(client, client, CombatType.RECOIL,
					Misc.random(30), 0);
			client.teleportToX = coords[2];
			client.teleportToY = coords[3];
			client.getActionSender()
					.sendMessage(
							"The portal denies your acces, perhaps you need to kill more npcs.");
			client.spamClickDungPortal = System.currentTimeMillis();
		} else if (client.floor2()) {
			final Player shit = client;
			client.getPlayerEventHandler().addEvent(new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					shit.tempLock = System.currentTimeMillis();
					shit.teleportToX = 1882;
					shit.teleportToY = 4658;
					shit.setHeightLevel(shit.getIndex() * 4 + 1);
					shit.getActionSender().sendMessage(
							"You get transported to the boss room.");
					spawnDungeoneeringBossFloor2(shit);
					shit.spamClickDungPortal = System.currentTimeMillis();
					container.stop();
				}

				@Override
				public void stop() {
					// TODO Auto-generated method stub

				}
			}, 2);

		}
	}

	/**
	 * Handles floor 3's object movements.
	 * 
	 * @param client
	 * @param objectID
	 */

	public void handleFloor3Move(Player client, int objectID) {
		switch (objectID) {
		case 7251:
			client.teleportToX = 3048;
			client.teleportToY = 4997;
			client.setHeightLevel(client.getHeightLevel());
			break;
		}
	}

	public void handleGateWays(Player client) {
		if (client.attempted1) {
			client.getActionSender().sendMessage(
					"You don't find anything interesting.");
			return;
		}
		final int succeeded = Misc.random(4);

		if (succeeded == 1) {
			client.getActionSender().sendMessage("You found a hidden passage.");
			client.teleportToX = client.getAbsX();
			client.teleportToY = 9320;
		} else
			client.attempted1 = true;

	}

	public void handleLadder(Player client) {
		if (!client.bawsed) {
			client.getActionSender().sendMessage(
					"You haven't killed your boss yet, you may not leave.");
			return;
		}
		completeDungeon(client);
	}

	public void handlePortal(Player client) {
		if (!client.floor1())
			return;
		if (client.portalEnter == 0) {
			if (client.failedOnes > 1) {
				client.tempLock = System.currentTimeMillis();
				client.teleportToX = 2860;
				client.teleportToY = 9739;
				client.setHeightLevel(client.getIndex() * 4);
				spawnDungeoneeringBossFloor1(client);
				return;
			}
			client.tempLock = System.currentTimeMillis();
			client.teleportToX = 2860;
			client.teleportToY = 9739;
			client.setHeightLevel(client.getIndex() * 4);
			spawnDungeoneeringBossFloor1(client);
			client.portalEnter = 1;
		} else
			client.sendMessage("You have already spawned the boss");
	}

	public void handleRandomizedBossAcces(Player client) {

	}

	public void handleTools(Player client, int level) {
		if (client.toolsTaken) {
			client.getActionSender().sendMessage(
					"You have already taken your set of tools.");
			return;
		}
		if (Misc.random(0) == 0 && client.getAbsY() > 9324) {
			client.teleportToX = 3233;
			client.teleportToY = 9315;
			HitExecutor.addNewHit(client, client, CombatType.MELEE,
					Misc.random(15), 0);
			client.getActionSender().sendMessage("It's a trap!");
		} else {
			client.getActionSender().addItem(2347, 1);
			client.getActionSender().sendMessage(
					"You take some tools from the sarcophagus.");
			if (level <= 20) {

				client.getActionSender().addItem(16296, 1);
				client.getActionSender().addItem(16361, 1);
			} else if (level > 20 && level <= 40) {
				client.getActionSender().addItem(16297, 1);
				client.getActionSender().addItem(16363, 1);

			} else if (level > 40) {
				client.getActionSender().addItem(16299, 1);
				client.getActionSender().addItem(16365, 1);
			}

		}
	}

	public void lightShit(Player client) {
		if (client.lighted) {
			client.getActionSender().sendMessage(
					"You've already used this device.");
			return;

		}
		final int light = Misc.random(3);
		if (light == 1) {
			client.teleportToX = 1887;
			client.teleportToY = 4627;
			client.getActionSender().sendMessage(
					"You transport with the mysterious power of light.");
		} else {
			client.teleportToX = coords[2];
			client.teleportToY = coords[3];
			HitExecutor.addNewHit(client, client, CombatType.RECOIL,
					Misc.random(70), 0);
			client.getActionSender().sendMessage(
					"A powerful force damages you.");

		}
		client.lighted = true;

	}

	/**
	 * Handles user disconnection.
	 * 
	 * @param client
	 */
	public void onDisconnect(Player client) {
		boolean did = false;
		if (playersDunging.contains(client.getUsername()))
			playersDunging.remove(client.getUsername());
		if (client.floor1() || client.floor3()) {
			did = true;
			NPC.removeDungeoneeringNPC(client);
		} else {
			if (client.floor2()) {
				did = true;
				NPC.removeDungeoneeringNPC2(client);
			}
			if (did)
				Dungeoneering.getInstance().deleteDungEquipment(client);
		}
	}

	public void removeBind(Player client) {
		client.dialogueAction = 140000;
		client.getActionSender()
				.selectOption(
						"Which one?",
						""
								+ InstanceDistributor.getItemManager()
										.getItemDefinition(client.bound1)
										.getName(),
						""
								+ InstanceDistributor.getItemManager()
										.getItemDefinition(client.bound2)
										.getName(),
						""
								+ InstanceDistributor.getItemManager()
										.getItemDefinition(client.bound3)
										.getName(),
						""
								+ InstanceDistributor.getItemManager()
										.getItemDefinition(client.bound4)
										.getName(), "");
	}

	public void sendFood(Player client) {
		if (client.foodTaken) {
			client.getActionSender().sendMessage(
					"You have already taken your load of food.");
			client.getActionSender().sendWindowsRemoval();
			return;
		}
		if (client.getFloor() == 1) {
			for (int i = 0; i < 1 + Misc.random(2); i++)
				client.getActionSender().addItem(18165, 1);
			for (int i = 0; i < 5; i++)
				client.getActionSender().addItem(18159, 1);
			client.foodTaken = true;

		} else if (client.getFloor() == 2) {
			for (int i = 0; i < 3 + Misc.random(2); i++)
				client.getActionSender().addItem(18145, 1);
			for (int i = 0; i < 5; i++)
				client.getActionSender().addItem(18165, 1);

			client.foodTaken = true;
		}
		client.dialogueAction = 0;
		client.getActionSender().sendWindowsRemoval();

	}

	private void sendStartItems(Player client, int floorLevel) {

		if (floorLevel == 1) {
			if (client.getLevelForXP(client.playerXP[23]) <= 20) {
				client.getActionSender().addItem(16669, 1);
				client.getActionSender().addItem(17239, 1);
				client.getActionSender().addItem(16691, 1);
				client.getActionSender().addItem(16935, 1);
				client.getActionSender().addItem(16383, 1);
				client.getActionSender().addItem(16405, 1);
				client.getActionSender().addItem(16889, 1);
			} else if (client.getLevelForXP(client.playerXP[23]) >= 21
					&& client.getLevelForXP(client.playerXP[23]) < 40) {
				client.getActionSender().addItem(17241, 1);
				client.getActionSender().addItem(16671, 1);
				client.getActionSender().addItem(16693, 1);
				client.getActionSender().addItem(16937, 1);
				client.getActionSender().addItem(16385, 1);
				client.getActionSender().addItem(16407, 1);
				client.getActionSender().addItem(16891, 1);
			} else if (client.getLevelForXP(client.playerXP[23]) >= 40) {
				client.getActionSender().addItem(17243, 1);
				client.getActionSender().addItem(16673, 1);
				client.getActionSender().addItem(16695, 1);
				client.getActionSender().addItem(16939, 1);
				client.getActionSender().addItem(16387, 1);
				client.getActionSender().addItem(16409, 1);
				client.getActionSender().addItem(16893, 1);
			}
		} else if (floorLevel == 2) {
			if (client.getLevelForXP(client.playerXP[23]) >= 50
					&& client.getLevelForXP(client.playerXP[23]) < 65) {
				client.getActionSender().addItem(16697, 1);
				client.getActionSender().addItem(17245, 1);
				client.getActionSender().addItem(16675, 1);
				client.getActionSender().addItem(16941, 1);
				client.getActionSender().addItem(16389, 1);
				client.getActionSender().addItem(16411, 1);
				client.getActionSender().addItem(16895, 1);
			} else if (client.getLevelForXP(client.playerXP[23]) >= 65
					&& client.getLevelForXP(client.playerXP[23]) < 75) {
				client.getActionSender().addItem(16699, 1);
				client.getActionSender().addItem(17247, 1);
				client.getActionSender().addItem(16677, 1);
				client.getActionSender().addItem(16943, 1);
				client.getActionSender().addItem(16391, 1);
				client.getActionSender().addItem(16413, 1);
				client.getActionSender().addItem(16897, 1);
			} else if (client.getLevelForXP(client.playerXP[23]) >= 75) {
				client.getActionSender().addItem(16701, 1);
				client.getActionSender().addItem(17249, 1);
				client.getActionSender().addItem(16679, 1);
				client.getActionSender().addItem(16945, 1);
				client.getActionSender().addItem(16393, 1);
				client.getActionSender().addItem(16415, 1);
				client.getActionSender().addItem(16899, 1);
			}
		} else if (floorLevel == 3)
			if (client.getDungLevelForXp(client.playerXP[23]) >= 80
					&& client.getDungLevelForXp(client.playerXP[23]) < 90) {
				client.getActionSender().addItem(16703, 1);
				client.getActionSender().addItem(17251, 1);
				client.getActionSender().addItem(16681, 1);
				client.getActionSender().addItem(16947, 1);
				client.getActionSender().addItem(16395, 1);
				client.getActionSender().addItem(16417, 1);
				client.getActionSender().addItem(16901, 1);
			} else if (client.getDungLevelForXp(client.playerXP[23]) >= 90
					&& client.getDungLevelForXp(client.playerXP[23]) <= 99) {
				client.getActionSender().addItem(16705, 1);
				client.getActionSender().addItem(17253, 1);
				client.getActionSender().addItem(16683, 1);
				client.getActionSender().addItem(16949, 1);
				client.getActionSender().addItem(16397, 1);
				client.getActionSender().addItem(16419, 1);
				client.getActionSender().addItem(16903, 1);
			} else if (client.getDungLevelForXp(client.playerXP[23]) >= 100) {
				client.getActionSender().addItem(16707, 1);
				client.getActionSender().addItem(17255, 1);
				client.getActionSender().addItem(16685, 1);
				client.getActionSender().addItem(16951, 1);
				client.getActionSender().addItem(16399, 1);
				client.getActionSender().addItem(16421, 1);
				client.getActionSender().addItem(16905, 1);
			}

	}

	public void spawnDungeoneeringBossFloor1(final Player client) {
		for (final NPC npc : InstanceDistributor.getNPCManager().getNPCMap()
				.values()) {
			if (npc == null)
				continue;
			if (Areas.bossRoom1(npc.getPosition()))
				if (npc.getHeightLevel() == client.getIndex() * 4
						&& npc.getHeightLevel() != 0)
					NPC.removeNPC(npc, 22);
		}
		client.tempLock = System.currentTimeMillis();
		final int randomBaws = dungBossesFloor1[Misc
				.random(dungBossesFloor1.length - 1)];
		NPC.spawnDungeoneeringNPC(randomBaws, 2861, 9733,
				client.getIndex() * 4, client);
		client.getActionSender().sendMessage(
				"Your boss is : @red@"
						+ InstanceDistributor.getNPCManager()
								.getNPCDefinition(randomBaws).getName()
						+ "@bla@.");
		client.getActionSender().sendMessage(
				"Don't forget to bind any items if neccesary!");
		client.dungeoneeringBaws = randomBaws;
		client.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				ObjectManager.submitPublicObject(new GameObject(6501, 2853,
						9735, client.getIndex() * 4, Face.SOUTH, 10));
				ObjectManager.submitPublicObject(new GameObject(359, 2859,
						9742, client.getIndex() * 4, Face.SOUTH, 10));
				ObjectManager.submitPublicObject(new GameObject(359, 2860,
						9742, client.getIndex() * 4, Face.SOUTH, 10));
				ObjectManager.submitPublicObject(new GameObject(359, 2861,
						9742, client.getIndex() * 4, Face.SOUTH, 10));
				container.stop();
			}

			@Override
			public void stop() {
				// TODO Auto-generated method stub

			}
		}, 2);
	}

	public void spawnDungeoneeringBossFloor2(final Player client) {
		for (final NPC npc : InstanceDistributor.getNPCManager().getNPCMap()
				.values()) {
			if (npc == null)
				continue;
			final int playerHeight = client.getIndex() * 4 + 1;
			if (npc.inFloor2BossRoom() && npc.getHeightLevel() == playerHeight
					&& playerHeight != 0)
				NPC.removeNPC(npc, 24);

		}

		final int randomBaws = dungBossesFloor2[Misc
				.random(dungBossesFloor2.length - 1)];
		NPC.spawnDungeoneeringNPC(randomBaws, 1877, 4658,
				client.getIndex() * 4 + 1, client);
		client.getActionSender().sendMessage(
				"Your boss is : @red@"
						+ InstanceDistributor.getNPCManager()
								.getNPCDefinition(randomBaws).getName()
						+ "@bla@.");
		client.getActionSender().sendMessage(
				"Don't forget to bind any items if neccesary!");
		client.dungeoneeringBaws = randomBaws;
		client.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				ObjectManager.submitPublicObject(new GameObject(6501, 1877,
						4661, client.getIndex() * 4 + 1, Face.EAST, 10));
				container.stop();
			}

			@Override
			public void stop() {
				// TODO Auto-generated method stub

			}
		}, 2);
	}

	// bossroom - 1882 4658
	public void spawnDungObjects(final Player client, int floorLevel) {
		if (floorLevel == 1)
			client.getPlayerEventHandler().addEvent(new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {

					ObjectManager.submitPublicObject(new GameObject(2782, 3237,
							9317, client.getIndex() * 4, Face.SOUTH, 10));
					ObjectManager.submitPublicObject(new GameObject(-1, 3232,
							9311, client.getIndex() * 4, Face.EAST, 10));
					ObjectManager.submitPublicObject(new GameObject(-1, 3228,
							9311, client.getIndex() * 4, Face.SOUTH, 10));
					ObjectManager.submitPublicObject(new GameObject(-1, 3228,
							9315, client.getIndex() * 4, Face.SOUTH, 10));
					ObjectManager.submitPublicObject(new GameObject(-1, 3228,
							9319, client.getIndex() * 4, Face.SOUTH, 10));
					ObjectManager.submitPublicObject(new GameObject(-1, 3234,
							9314, client.getIndex() * 4, Face.SOUTH, 10));
					ObjectManager.submitPublicObject(new GameObject(-1, 3232,
							9314, client.getIndex() * 4, Face.SOUTH, 10));
					ObjectManager.submitPublicObject(new GameObject(-1, 3233,
							9314, client.getIndex() * 4, Face.SOUTH, 10));
					ObjectManager.submitPublicObject(new GameObject(-1, 3238,
							9311, client.getIndex() * 4, Face.SOUTH, 10));
					ObjectManager.submitPublicObject(new GameObject(-1, 3238,
							9315, client.getIndex() * 4, Face.SOUTH, 10));
					ObjectManager.submitPublicObject(new GameObject(-1, 3238,
							9319, client.getIndex() * 4, Face.SOUTH, 10));
					ObjectManager.submitPublicObject(new GameObject(410, 3227,
							9310, client.getIndex() * 4, Face.SOUTH, 10));
					ObjectManager.submitPublicObject(new GameObject(-1, 3232,
							9294, client.getIndex() * 4, Face.SOUTH, 10));

					spawnPortals(client);
					container.stop();
				}

				@Override
				public void stop() {
					// TODO Auto-generated method stub

				}
			}, 5);
		else if (floorLevel == 2)
			client.getPlayerEventHandler().addEvent(new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					ObjectManager.submitPublicObject(new GameObject(-1, 1860,
							4616, client.getIndex() * 4, Face.SOUTH, 10));
					ObjectManager.submitPublicObject(new GameObject(-1, 1863,
							4613, client.getIndex() * 4, Face.SOUTH, 10));
					ObjectManager.submitPublicObject(new GameObject(-1, 1863,
							4665, client.getIndex() * 4, Face.SOUTH, 10));
					ObjectManager.submitPublicObject(new GameObject(-1, 1860,
							4662, client.getIndex() * 4, Face.SOUTH, 10));
					ObjectManager.submitPublicObject(new GameObject(7272, 1860,
							4657, client.getIndex() * 4, Face.SOUTH, 10));
					ObjectManager.submitPublicObject(new GameObject(380, 1878,
							4618, client.getIndex() * 4, Face.NORTH, 10));
					/** Walk prevention */
					ObjectManager.submitPublicObject(new GameObject(359, 1901,
							4629, client.getIndex() * 4, Face.SOUTH, 10));
					ObjectManager.submitPublicObject(new GameObject(359, 1901,
							4628, client.getIndex() * 4, Face.SOUTH, 10));
					ObjectManager.submitPublicObject(new GameObject(359, 1901,
							4627, client.getIndex() * 4, Face.SOUTH, 10));
					ObjectManager.submitPublicObject(new GameObject(359, 1901,
							4649, client.getIndex() * 4, Face.SOUTH, 10));
					ObjectManager.submitPublicObject(new GameObject(359, 1901,
							4650, client.getIndex() * 4, Face.SOUTH, 10));
					ObjectManager.submitPublicObject(new GameObject(359, 1901,
							4651, client.getIndex() * 4, Face.SOUTH, 10));
					ObjectManager.submitPublicObject(new GameObject(359, 1890,
							4664, client.getIndex() * 4, Face.SOUTH, 10));
					ObjectManager.submitPublicObject(new GameObject(359, 1890,
							4665, client.getIndex() * 4, Face.SOUTH, 10));
					ObjectManager.submitPublicObject(new GameObject(359, 1890,
							4666, client.getIndex() * 4, Face.SOUTH, 10));
					container.stop();
				}

				@Override
				public void stop() {
					// TODO Auto-generated method stub

				}
			}, 5);
		/** End of walk prevention */
		else if (floorLevel == 3) {
			/**
			 * Walking prevention.
			 */
			ObjectManager.submitPublicObject(new GameObject(2079, 3052, 4998,
					client.getIndex() * 4 + 1, Face.SOUTH, 10));
			ObjectManager.submitPublicObject(new GameObject(359, 3050, 5005,
					client.getIndex() * 4 + 1, Face.SOUTH, 10));
			ObjectManager.submitPublicObject(new GameObject(359, 3050, 5006,
					client.getIndex() * 4 + 1, Face.SOUTH, 10));
			ObjectManager.submitPublicObject(new GameObject(359, 3050, 5007,
					client.getIndex() * 4 + 1, Face.SOUTH, 10));
			ObjectManager.submitPublicObject(new GameObject(359, 2959, 5109,
					client.getIndex() * 4 + 1, Face.WEST, 10));
			ObjectManager.submitPublicObject(new GameObject(359, 2959, 5108,
					client.getIndex() * 4 + 1, Face.WEST, 10));
			ObjectManager.submitPublicObject(new GameObject(359, 2959, 5107,
					client.getIndex() * 4 + 1, Face.WEST, 10));
			ObjectManager.submitPublicObject(new GameObject(359, 2959, 5106,
					client.getIndex() * 4 + 1, Face.WEST, 10));
			ObjectManager.submitPublicObject(new GameObject(359, 2959, 5105,
					client.getIndex() * 4 + 1, Face.WEST, 10));
		} else {
			client.getActionSender().sendMessage(
					"Invalid Dungeoneering Request, ending dungeon.");
			forceLeaveDungeon(client);
		}
	}

	/*
	 * private static int[] x3 = {}; private static int[] y3 = {}; private
	 * static int[] floor3Npcs =
	 * {10507,10508,10509,10510,10511,10512,10513,10514
	 * ,10515,10516,10517,10518,10519
	 * ,10520,10521,10522,10523,10524,10525,10526,10527
	 * ,10528,10529,10530,10531,10532
	 * ,10533,10534,10535,10536,10537,10538,10539,10540
	 * ,10541,10542,10543,10544,10545,10546,10547,10548,
	 * 10320,10321,10322,10323,
	 * 10324,10325,10326,10327,10328,10329,10330,10331,10332
	 * ,10333,10334,10335,10336
	 * ,10337,10338,10339,10340,10341,10342,10343,10344,10345
	 * ,10346,10347,10348,10349
	 * ,10350,10351,10352,10353,10354,10355,10356,10357,10358
	 * ,10359,10360,10361,10362
	 * ,10558,10559,10560,10561,10562,10563,10564,10565,10566
	 * ,10567,10568,10569,10570
	 * ,10571,10572,10573,10574,10575,10576,10577,10578,10579
	 * ,10580,10581,10582,10583
	 * ,10584,10585,10586,10587,10588,10589,10590,10591,10592
	 * ,10593,10594,10595,10596,10597,10598,10599,10600,10601,10602 };
	 */
	private void spawnFloor1Npcs(Player client) {
		for (int i = 0; i < x1.length; i++)
			NPC.spawnDungeoneeringNPC(
					floor1LowNpcs[Misc.random(floor1LowNpcs.length - 1)],
					x1[i], y1[i], client.getIndex() * 4, client);

	}

	private void spawnFloor2Npcs(Player client) {
		for (int i = 0; i < x2.length; i++)
			NPC.spawnDungeoneeringNPC(
					floor2Npcs[Misc.random(floor2Npcs.length - 1)], x2[i],
					y2[i], client.getIndex() * 4, client);
	}

	/**
	 * Spawns the npcs for floor 3.
	 */
	private void spawnFloor3Npcs(Player client) {
		for (final int[] floor3Coord : floor3Coords)
			NPC.spawnDungeoneeringNPC(
					floor3Npcs[Misc.random(floor3Npcs.length - 1)],
					floor3Coord[0], floor3Coord[1], client.getIndex() * 4 + 1,
					client);
	}

	public void spawnPortals(Player client) {
		ObjectManager.submitPublicObject(new GameObject(359, 3232, 9293, client
				.getIndex() * 4, Face.SOUTH, 10));
		ObjectManager.submitPublicObject(new GameObject(2156, 3232, 9296,
				client.getIndex() * 4, Face.SOUTH, 10));
		ObjectManager.submitPublicObject(new GameObject(-1, 3212, 9283, client
				.getIndex() * 4, Face.SOUTH, 10));
		ObjectManager.submitPublicObject(new GameObject(-1, 3251, 9293, client
				.getIndex() * 4, Face.SOUTH, 10));
		client.getActionSender()
				.sendMessage(
						"[Dungeoneering Hint] The portal has moved, it's somewhere in the middle.");

	}

	public void startDungeon(final Player client, final int floorLevel,
			int requiredLevel) {
		if (client.playerLevel[PlayerConstants.DUNGEONEERING] < requiredLevel) {
			client.getActionSender().sendWindowsRemoval();
			client.getActionSender().sendMessage(
					"You need a dungeoneering level of " + requiredLevel
							+ " to use this floor.");
			return;
		}
		if (client.getActionAssistant().freeSlots() != 28
				|| client.playerEquipment[0] > 0
				|| client.playerEquipment[1] > 0
				|| client.playerEquipment[2] > 0
				|| client.playerEquipment[3] > 0
				|| client.playerEquipment[4] > 0
				|| client.playerEquipment[5] > 0
				|| client.playerEquipment[6] > 0
				|| client.playerEquipment[7] > 0
				|| client.playerEquipment[8] > 0
				|| client.playerEquipment[9] > 0
				|| client.playerEquipment[10] > 0
				|| client.playerEquipment[11] > 0
				|| client.playerEquipment[12] > 0
				|| client.playerEquipment[13] > 0) {
			client.getActionSender().sendWindowsRemoval();
			client.getActionSender()
					.sendMessage(
							"@red@You cannot bring any items to dungeoneering, also not your dungeoneering ring.");

			return;
		}
		if (client.familiarId > 0) {
			client.getActionSender().sendWindowsRemoval();
			client.getActionSender().sendMessage(
					"You cannot bring familiars to dungeoneering.");
			return;
		}
		client.lastDungEntry = System.currentTimeMillis();
		if (floorLevel == 1) {
			client.teleportToX = coords[0];
			client.teleportToY = coords[1];
			client.setHeightLevel(client.getIndex() * 4);
			client.getPlayerTeleportHandler().forceTeleport(coords[0],
					coords[1], client.getIndex() * 4);
			spawnFloor1Npcs(client);
			client.getActionSender()
					.sendMessage(
							"@red@ Search the sarcophagus's in the wall to obtain materials like food and runes.");
			client.getActionSender().sendMessage(
					"To find your boss, you have to find the magical portal!");
			client.getActionSender()
					.sendMessage(
							"If you get stuck you can leave at any time by clicking force quit in quest menu.");

			client.totalNpcs = x1.length;
			client.totalTime = 300000;
		} else if (floorLevel == 2) {
			client.getPlayerTeleportHandler().forceTeleport(coords[2],
					coords[3], client.getIndex() * 4);
			spawnFloor2Npcs(client);
			client.totalNpcs = x2.length;
			client.totalTime = 500000;

		} else if (floorLevel == 3) {
			client.eaten = false;
			final int datHeightLevel = client.getIndex() * 4 + 1;
			for (final NPC npc : InstanceDistributor.getNPCManager()
					.getNPCMap().values()) {
				if (npc == null)
					continue;
				if (npc.getHeightLevel() == datHeightLevel
						&& datHeightLevel != 0)
					NPC.removeNPC(npc, 20);
			}
			client.getPlayerTeleportHandler().forceTeleport(coords[4],
					coords[5], client.getIndex() * 4 + 1);
			spawnFloor3Npcs(client);
			dungFloor3Boss(client);
			client.getActionSender().sendMessage(
					"@red@Click the chest for food and other supplies!");
		}
		client.setBusyTimer(15);
		client.isDunging = true;
		client.dungLeaveTimer = System.currentTimeMillis();
		playersDunging.add(client.getUsername());
		client.sendQuestInterface();
		client.npcsKilled = 0;
		client.getActionSender().sendWindowsRemoval();
		client.dialogueAction = 0;
		client.setFloor(floorLevel);
		client.hitpoints = client.calculateMaxHP();
		client.playerLevel[3] = client.calculateMaxHP();
		client.playerLevel[5] = client.getLevelForXP(client.playerXP[5]);
		client.getActionAssistant().refreshSkill(PlayerConstants.HITPOINTS);
		client.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				client.sendQuestInterface();
				if (client.bound1 > 0 && client.bound1 != 11694
						&& client.bound1 != 11696 && client.bound1 != 11698
						&& client.bound1 != 11700 && client.bound1 != 14484
						&& client.bound1 != 10887)
					client.getActionSender().addItem(client.bound1, 1);
				if (client.bound2 > 0 && client.bound2 != 11694
						&& client.bound1 != 11696 && client.bound2 != 11698
						&& client.bound2 != 11700 && client.bound2 != 14484
						&& client.bound2 != 10887)
					client.getActionSender().addItem(client.bound2, 1);
				if (client.bound3 > 0 && client.bound3 != 11696
						&& client.bound3 != 11694 && client.bound3 != 11698
						&& client.bound3 != 11700 && client.bound3 != 14484
						&& client.bound3 != 10887)
					client.getActionSender().addItem(client.bound3, 1);
				if (client.bound4 > 0 && client.bound1 != 11696
						&& client.bound4 != 11694 && client.bound4 != 11698
						&& client.bound4 != 11700 && client.bound4 != 14484
						&& client.bound4 != 10887)
					client.getActionSender().addItem(client.bound4, 1);
				client.timeTaken = System.currentTimeMillis();
				spawnDungObjects(client, floorLevel);
				sendStartItems(client, floorLevel);
				if (client.getHeightLevel() != client.getIndex() * 4)
					client.getPlayerTeleportHandler().forceTeleport(
							client.getAbsX(), client.getAbsY(),
							client.getHeightLevel());
				container.stop();
			}

			@Override
			public void stop() {
				// TODO Auto-generated method stub

			}
		}, 2);

	}

	public void teleToMaster(Player client) {
		client.getPlayerTeleportHandler().teleport(3486, 3090, 0);
		client.getActionSender().sendMessage(Language.thok);
	}

}