package com.server2.content.skills.summoning;

import com.server2.InstanceDistributor;
import com.server2.content.Achievements;
import com.server2.content.misc.mobility.TeleportationHandler;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.npc.NPCDefinition;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Areas;

/**
 * 
 * @author Rene Roosen
 * @author Lukas Pinckers
 */
public class Summoning {

	/**
	 * Instances the Summoning class
	 */
	public static Summoning INSTANCE = new Summoning();

	/**
	 * The summoning gfx which is used when the NPC gets spawned/re-spawned
	 */
	public static int summonGfx = 1315;

	private static long lastCall;

	/**
	 * A huge array which contains all required summoning data.
	 * 
	 * pouch id, scroll id, scroll xp, time in min, npc id, level needed, shars
	 * needed, charm (1=gold, 2=green, 3=crimson, 4=blue), second ingredient id,
	 * xp, shard return
	 */
	public static final double[][] summoningInfo = {
			{ 12047, 12425, 0.2, 6, 6829, 1, 7, 1, 2859, 4.8, 5 },
			{ 12043, 12445, 0.2, 4, 6825, 4, 8, 1, 2138, 9.3, 6 },
			{ 12059, 12428, 0.2, 15, 6841, 10, 8, 1, 6291, 12.6, 6 },
			{ 12019, 12459, 0.2, 16, 6806, 13, 9, 1, 3363, 12.6, 7, 3 },
			{ 12009, 12533, 0.2, 18, 6796, 16, 7, 1, 440, 21.6, 5 },
			{ 12778, 12838, 0.5, 12, 7331, 17, 1, 1, 6319, 46.5, 1 },
			{ 12094, 12460, 0.4, 19, 6831, 18, 45, 2, 1783, 31.2, 32 },
			{ 12055, 12432, 0.9, 17, 6837, 19, 57, 3, 3095, 83.2, 40 },
			{ 12808, 12839, 1.1, 18, 7361, 22, 64, 3, 12168, 96.8, 45 },
			{ 12067, 12430, 2.3, 23, 6847, 23, 75, 4, 2134, 202.4, 53 },
			{ 12063, 12446, 2.3, 22, 6994, 25, 51, 4, 3138, 220, 36, 6 },
			{ 12091, 12440, 0.6, 24, 6871, 28, 47, 2, 6032, 49.8, 33 },
			{ 12800, 12834, 2.9, 31, 7353, 29, 84, 4, 9976, 225.2, 59 },
			{ 12053, 12447, 1.6, 33, 6835, 31, 81, 3, 3325, 136, 57 },
			{ 12065, 12433, 2.4, 25, 6845, 32, 84, 3, 12156, 140.8, 59 },
			{ 12021, 12429, 0.7, 27, 6808, 33, 72, 2, 1519, 57.6, 51 },
			{ 12818, 12443, 0.7, 27, 7370, 34, 74, 2, 12164, 59.6, 52 },
			{ 12814, 12443, 0.7, 94, 7367, 34, 74, 4, 12165, 59.6, 52 },
			{ 12782, 12443, 0.7, 27, 7333, 34, 74, 4, 12166, 59.6, 52 },
			{ 12798, 12443, 0.7, 94, 7351, 34, 74, 4, 12167, 59.6, 52 },
			{ 12073, 12461, 0.36, 30, 6853, 36, 102, 4, 2349, 316.8, 72 },
			{ 12087, 12431, 0.4, 30, 6867, 40, 11, 1, 6010, 52.8, 8, 9 },
			{ 12071, 12422, 0.8, 31, 6851, 41, 78, 2, 13572, 72.4, 55 },
			{ 12051, 12448, 2.1, 30, 6833, 42, 104, 3, 12153, 184.8, 73 },
			{ 12095, 12458, 0.9, 36, 6875, 43, 88, 2, 12109, 75.2, 62 },
			{ 12097, 12458, 0.9, 36, 6877, 43, 88, 2, 12111, 75.2, 62 },
			{ 12099, 12458, 0.9, 36, 6879, 43, 88, 2, 12113, 75.2, 50 },
			{ 12101, 12458, 0.9, 36, 6881, 43, 88, 2, 12115, 75.2, 50 },
			{ 12103, 12458, 0.9, 36, 6883, 43, 88, 2, 12117, 75.2, 50 },
			{ 12105, 12458, 0.9, 36, 6885, 43, 88, 2, 12119, 75.2, 50 },
			{ 12107, 12458, 0.9, 36, 6887, 43, 88, 2, 12121, 75.2, 50 },
			{ 12075, 12462, 4.6, 37, 6855, 46, 125, 4, 2351, 404.8, 88 },
			{ 12816, 12829, 2.3, 32, 7377, 46, 111, 3, 590, 202.4, 78 },
			{ 12041, 12426, 5.7, 34, 6824, 47, 88, 2, 1635, 83.2, 62 },
			{ 12061, 12444, 2.4, 34, 6843, 49, 117, 3, 2132, 215.2, 82 },
			{ 12007, 12441, 0.8, 36, 6794, 52, 12, 1, 9978, 68.4, 9, 12 },
			{ 12035, 12454, 1.1, 30, 6818, 54, 106, 2, 12161, 94.8, 75 },
			{ 12027, 12453, 5.5, 43, 6992, 55, 151, 4, 1937, 484, 106 },
			{ 12531, 12424, 1.1, 38, 6991, 56, 109, 2, 311, 98.8, 77 },
			{ 12077, 12463, 5.6, 46, 6857, 56, 141, 4, 2353, 492.8, 99 },
			{ 12810, 12835, 5.6, 49, 7363, 57, 154, 4, 10099, 501.6, 108 },
			{ 12812, 12836, 5.6, 49, 7365, 57, 153, 4, 10103, 501.6, 108 },
			{ 12784, 12840, 5.7, 49, 7337, 57, 155, 4, 10095, 501.6, 109 },
			{ 12023, 12455, 5.8, 44, 6809, 58, 144, 4, 6667, 510.4, 101 },
			{ 12085, 12468, 3.1, 48, 6865, 61, 141, 3, 9736, 268, 99 },
			{ 12037, 12427, 0.1, 41, 6820, 62, 119, 2, 12161, 109.6, 84 },
			{ 12015, 12436, 3.1, 56, 6802, 63, 116, 3, 6287, 276.8, 82 },
			{ 12045, 12467, 3.2, 49, 6827, 64, 128, 3, 8431, 281.6, 90 },
			{ 12123, 12452, 1, 8, 6889, 66, 11, 1, 2150, 87, 7 },
			{ 12079, 12464, 6.6, 55, 6859, 66, 152, 4, 2359, 107 },
			{ 12031, 12439, 0.7, 43, 6815, 67, 1, 1, 7939, 58.6, 1, 18 },
			{ 12029, 12438, 1.4, 44, 6813, 68, 110, 2, 383, 119.2, 77 },
			{ 12033, 12423, 1.4, 45, 6817, 69, 130, 2, 1963, 121.2, 91 },
			{ 12820, 12830, 1.5, 24, 7372, 70, 79, 3, 1933, 132, 56 },
			{ 12057, 12451, 1.1, 28, 6839, 71, 14, 1, 10117, 93.2, 8 },
			{ 14623, 14622, 8, 30, 8548, 72, 165, 3, 14616, 301, 116 },
			{ 12792, 12826, 7.3, 55, 7345, 73, 195, 4, 12168, 642.4, 137 },
			{ 12069, 12449, 3.7, 47, 6849, 74, 166, 2, 6979, 325.6, 117 },
			{ 12011, 12450, 3.6, 69, 6798, 75, 168, 3, 2462, 329.6, 188 },
			{ 12782, 12449, 1.5, 45, 10020, 76, 141, 2, 10020, 134, 99 },
			{ 12081, 12465, 7.6, 66, 6861, 76, 144, 4, 2361, 668.8, 101 },
			{ 12162, 12831, 11.4, 49, 7347, 77, 174, 3, 12795, 1015.2, 122 },
			{ 12013, 12457, 1.6, 49, 6800, 78, 124, 2, 5933, 136.8, 87 },
			{ 12802, 12854, 7.9, 62, 7355, 79, 198, 4, 1442, 695.2, 139 },
			{ 12804, 12854, 7.9, 79, 7357, 79, 202, 4, 1440, 695.2, 142 },
			{ 12806, 12854, 7.9, 64, 7359, 79, 198, 4, 1444, 1438, 695.2, 139 },
			{ 12025, 12442, 1.6, 19, 9488, 80, 128, 2, 571, 140.8, 90 },
			{ 12017, 12456, 4.1, 57, 6804, 83, 1, 3, 6255, 364.8, 1 },
			{ 12788, 12837, 8.3, 61, 7341, 83, 219, 4, 12168, 730.4, 154 },
			{ 12776, 12832, 4.1, 56, 7329, 85, 150, 3, 10149, 373.6, 105 },
			{ 12083, 12466, 8.6, 51, 6863, 86, 1, 4, 2363, 756.8, 1 },
			{ 12039, 12434, 1.8, 54, 6822, 88, 140, 2, 237, 154.4, 98 },
			{ 12786, 12833, 8.9, 69, 7339, 89, 222, 4, 1444, 783.2, 156 },
			{ 12089, 12437, 4.6, 62, 6869, 92, 203, 3, 2859, 3226, 404.8, 143 },
			{ 12796, 12827, 1.9, 32, 7349, 93, 112, 2, 12161, 163.2, 79 },
			{ 12822, 12828, 4.7, 60, 7375, 95, 198, 3, 1115, 417.6, 139 },
			{ 12093, 12435, 4.8, 58, 6873, 96, 211, 3, 10818, 422.4, 148, 30 },
			{ 12790, 12825, 4.9, 64, 7343, 99, 178, 3, 1119, 435.2, 1 } };

	/**
	 * A boolean which returns if the familiar is a BoB
	 */
	public static boolean BoB(Player client) {
		if (client.familiarId == 6873 || client.familiarId == 6806
				|| client.familiarId == 6815 || client.familiarId == 6867
				|| client.familiarId == 6794 || client.familiarId == 6994)
			return true;

		return false;
	}

	public static boolean canSummon(Player client, boolean flag) {
		if (!BoB(client))
			return true;
		if (client.getFamiliar() == null) {
			client.canSummon = true;
			return true;
		}
		if (!client.canSummon) {
			if (flag)
				client.getActionSender()
						.sendMessage(
								"@red@You cannot use your familiar below lvl 25 in a single combat zone.");
			return false;

		}
		if (client.getWildernessLevel() > 0 && !client.multiZone()
				&& client.getWildernessLevel() < 25) {
			client.canSummon = false;
			if (flag)
				client.getActionSender()
						.sendMessage(
								"@red@You cannot use your familiar below lvl 25 in a single combat zone.");
			return false;
		}
		client.canSummon = true;
		return true;
	}

	/**
	 * Gets the Summoning Instance.
	 */
	public static Summoning getInstance() {
		return INSTANCE;
	}

	public void attack(Player client) {

		if (!client.multiZone()) {
			client.getActionSender()
					.sendMessage(
							"This is a non-multi zone, your familiar cannot help here.");
			return;
		}
		if (client.familiarId == 7343)
			NPC.hasToAttack = true;
		else if (client.familiarId == 7375)
			NPC.hasToAttack = true;
		else if (client.familiarId == 6853)
			NPC.hasToAttack = true;
		else if (client.familiarId == 6857)
			NPC.hasToAttack = true;
		else if (client.familiarId == 6859)
			NPC.hasToAttack = true;
		else if (client.familiarId == 6861)
			NPC.hasToAttack = true;
		else if (client.familiarId == 6863)
			NPC.hasToAttack = true;
		else
			client.getActionSender().sendMessage(
					"This is a non-combat familiar.");
	}

	public void bunyip(Player client) {
		if (!client.canSummon)
			return;
		client.getActionAssistant().addHP(2);
		client.getActionSender().sendMessage("Your bunyip slightly heals you.");
		GraphicsProcessor.addNewRequest(client, 1507, 0, 0);
		client.bunnyIpCounter = 0;
	}

	public void call(Player client) {
		if (!Summoning.canSummon(client, true))
			return;
		if (client.familiarId > 0) {
			if (System.currentTimeMillis() - lastCall < 5000)
				return;
			if (client.floor1()) {
				client.getActionSender().sendMessage(
						"You cannot bring familiars into dungeoneering.");
				return;
			}
			lastCall = System.currentTimeMillis();
			NPC.removeNPC(client.getFamiliar(), 465654);
			respawnFamiliar(client);
			client.getActionSender().sendMessage("You call your familiar.");
		} else
			client.getActionSender().sendMessage(
					"You do not have a familiar to call.");
	}

	public String determineName(int food) {
		if (food == 15272)
			return "Rocktails";
		if (food == 391)
			return "Manta Rays";
		if (food == 385)
			return "Sharks";
		if (food == 7946)
			return "Monkfish";
		if (food == 379)
			return "Lobsters";

		return "";
	}

	/**
	 * A method which dismisses the user's familiar.
	 * 
	 * @param client
	 */
	public void dismiss(Player client) {
		if (!Summoning.canSummon(client, true))
			return;

		if (client.familiarId > 0) {

			client.getActionSender().sendString(" " + client.familiarTime,
					18043);

			if (client.familiarTime == 0)
				client.getActionSender().sendMessage(
						"Your familiar has vanished.");
			else
				client.getActionSender().sendMessage(
						"You dismiss your familiar.");
			NPC.removeNPC(client.getFamiliar(), 9898798);
			client.familiarId = 0;
			client.pouchUsed = 0;
			client.familiarTime = 0;
			client.foodAmountStored = 0;
			client.foodIdStored = 0;
			client.getActionSender().sendFrame75(4000, 18021);
			client.getActionSender().sendString("No familiar", 18028);

			client.getActionSender().sendString(" " + client.familiarTime,
					18043);
			// //system.out.println("["+System.currentTimeMillis()+"] sendquest summoning");
		} else
			client.getActionSender().sendMessage(
					"You do not have a familiar to dismiss.");
	}

	public int getLevel(int itemID) {

		for (final double[] slot : summoningInfo)
			if (itemID == slot[0])
				return (int) slot[5];
		return 1;

	}

	/**
	 * A double which collects the npcID
	 */
	public int getNpcID(int itemID) {

		for (final double[] slot : summoningInfo)
			if (itemID == slot[0])
				// //system.out.println(Math.round(slot[4]));
				return (int) Math.round(slot[4]);
		return 1;

	}

	/**
	 * A double which collects the scroll of the familiar.
	 */
	public int getScroll(int itemID) {

		for (final double[] slot : summoningInfo)
			if (itemID == slot[0])
				return (int) slot[1];
		return 1;

	}

	/**
	 * A double which collects the scroll XP of the familiar.
	 */
	public int getScrollXP(int itemID) {

		for (final double[] slot : summoningInfo)
			if (itemID == slot[0])
				return (int) slot[2];
		return 1;

	}

	/**
	 * A double which collects the amounts of BoBStorageSpots
	 */
	public int getSpots(int itemID) {

		for (final double[] slot : summoningInfo)
			if (itemID == slot[0])
				// //system.out.println(Math.round(slot[4]));
				return (int) Math.round(slot[11]);
		return 1;

	}

	/**
	 * A double which collects the time of the familiar.
	 */
	public int getTime(int itemID) {

		for (final double[] slot : summoningInfo)
			if (itemID == slot[0])
				// //system.out.println((slot[3]) * 1.5);
				return (int) (slot[3] * 1.5);
		return 1;

	}

	/**
	 * Handles the re-spawning of familiars when a player signs in.
	 * 
	 * @param client
	 */
	public void loginRespawn(final Player client) {
		if (client.familiarId > 0) {
			client.login = true;
			InstanceDistributor.getSummoning().summonNpc(client,
					client.pouchUsed);
			client.login = false;
		} else {
			client.getActionSender().sendString("No Familiar", 18028);
			client.getActionSender().sendFrame75(4000, 18021);
			client.getActionSender().sendString(" " + client.familiarTime,
					18043);
		}

	}

	public void makePouch(Player client, int number) {
		number = number - 1;
		int charm = 0;

		if (Summoning.summoningInfo[number][7] == 1)
			charm = 12158;
		else {
			InstanceDistributor.getSummoning();
			if (Summoning.summoningInfo[number][7] == 2)
				charm = 12159;
			else {
				InstanceDistributor.getSummoning();
				if (Summoning.summoningInfo[number][7] == 3)
					charm = 12160;
				else {
					InstanceDistributor.getSummoning();
					if (Summoning.summoningInfo[number][7] == 4)
						charm = 12163;
				}
			}
		}

		if (number != 74 && number != 66) {
			int lowest = client.getActionAssistant().getItemAmount(12155);
			// system.out.println("lowest 1" + lowest);
			if (client.getActionAssistant().getItemAmount(12183)
					/ Summoning.summoningInfo[number][6] < lowest)
				lowest = (int) (client.getActionAssistant()
						.getItemAmount(12183) / Summoning.summoningInfo[number][6]);
			// system.out.println("lowest 2" + lowest);
			if (client.getActionAssistant().getItemAmount(
					(int) Summoning.summoningInfo[number][8]) < lowest)
				lowest = client.getActionAssistant().getItemAmount(
						(int) Summoning.summoningInfo[number][8]);
			// system.out.println("lowest 3" + lowest);
			if (client.getActionAssistant().getItemAmount(charm) < lowest)
				lowest = client.getActionAssistant().getItemAmount(charm);
			// system.out.println("lowest 4" + lowest);
			if (client.playerLevel[PlayerConstants.SUMMONING] < Summoning.summoningInfo[number][5])
				lowest = 0;

			if (lowest > 0) {
				AnimationProcessor.addNewRequest(client, 8500, 0);

				client.getActionAssistant().deleteItem(12183,
						(int) Summoning.summoningInfo[number][6] * lowest);
				client.getActionAssistant().deleteItem(charm, lowest);
				client.getActionAssistant().deleteItem(12155, lowest);
				for (int i = 0; i < lowest; i++) {
					client.getActionAssistant().deleteItem(
							(int) Summoning.summoningInfo[number][8], 1);
					client.getActionSender().addItem(
							(int) Summoning.summoningInfo[number][0], 1);
				}
				// system.out.println("deleted " + lowest);

				if (Summoning.summoningInfo[number][0] == 12047) {
					client.progress[7] = client.progress[7] + lowest;
					if (client.progress[7] >= 15)
						Achievements.getInstance().complete(client, 7);
					else
						Achievements.getInstance().complete(client, 7);
				}

				client.getActionAssistant().addSkillXP(
						Summoning.summoningInfo[number][9] / 2
								* PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER
								* lowest, PlayerConstants.SUMMONING);
				client.getActionSender().sendWindowsRemoval();
			} else if (lowest == 0) {
				client.getActionSender().sendMessage(
						"You don't have the right items to create this pouch.");
				InstanceDistributor.getSummoning();
				InstanceDistributor.getSummoning();
				final String secin = InstanceDistributor
						.getItemManager()
						.getItemDefinition(
								(int) Summoning.summoningInfo[number][8])
						.getName();
				client.getActionSender()
						.sendMessage(
								"You need : "
										+ Math.round(Summoning.summoningInfo[number][6])
										+ " shards, an empty pouch, a "
										+ whichCharm(charm) + " and a " + secin
										+ ".");

			}
		}

		if (number == 74) {
			int lowest = client.getActionAssistant().getItemAmount(12155);
			if (client.getActionAssistant().getItemAmount(12183)
					/ Summoning.summoningInfo[number][6] < lowest)
				lowest = (int) (client.getActionAssistant()
						.getItemAmount(12183) / Summoning.summoningInfo[number][6]);
			if (client.getActionAssistant().getItemAmount(
					(int) Summoning.summoningInfo[number][8]) < lowest)
				lowest = client.getActionAssistant().getItemAmount(
						(int) Summoning.summoningInfo[number][8]);
			if (client.getActionAssistant().getItemAmount(charm) < lowest)
				lowest = client.getActionAssistant().getItemAmount(charm);
			if (client.getActionAssistant().getItemAmount(2859) < lowest)
				lowest = client.getActionAssistant().getItemAmount(2859);
			if (client.playerLevel[PlayerConstants.SUMMONING] < Summoning.summoningInfo[number][5])
				lowest = 0;

			if (lowest > 0) {

				AnimationProcessor.addNewRequest(client, 8500, 0);

				client.getActionAssistant().deleteItem(12183,
						(int) Summoning.summoningInfo[number][6] * lowest);
				client.getActionAssistant().deleteItem(charm, lowest);
				client.getActionAssistant().deleteItem(12155, lowest);
				client.getActionAssistant().deleteItem(2859, lowest);

				client.getActionAssistant().deleteItem(
						(int) Summoning.summoningInfo[number][8], lowest);
				InstanceDistributor.getSummoning();
				client.getActionSender().addItem(
						(int) Summoning.summoningInfo[number][0], lowest);
				InstanceDistributor.getSummoning();
				client.getActionAssistant().addSkillXP(
						Summoning.summoningInfo[number][9]
								* PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER
								* lowest, PlayerConstants.SUMMONING);
				client.getActionSender().sendWindowsRemoval();
			} else if (lowest == 0)
				client.getActionSender()
						.sendMessage(
								"You need : "
										+ Math.round(Summoning.summoningInfo[number][6])
										+ " shards, an empty pouch, a wolf bone,  a "
										+ whichCharm(charm)
										+ " and a "
										+ whichSecondIngredient(Summoning.summoningInfo[number][8])
										+ ".");
		}

		if (number == 66) {
			int lowest = client.getActionAssistant().getItemAmount(12155);
			if (client.getActionAssistant().getItemAmount(12183)
					/ Summoning.summoningInfo[number][6] < lowest)
				lowest = (int) (client.getActionAssistant()
						.getItemAmount(12183) / Summoning.summoningInfo[number][6]);
			if (client.getActionAssistant().getItemAmount(
					(int) Summoning.summoningInfo[number][8]) < lowest)
				lowest = client.getActionAssistant().getItemAmount(
						(int) Summoning.summoningInfo[number][8]);
			if (client.getActionAssistant().getItemAmount(charm) < lowest)
				lowest = client.getActionAssistant().getItemAmount(charm);
			if (client.getActionAssistant().getItemAmount(1438) < lowest)
				lowest = client.getActionAssistant().getItemAmount(1438);
			if (client.playerLevel[PlayerConstants.SUMMONING] < Summoning.summoningInfo[number][5])
				lowest = 0;
			if (lowest > 0) {
				AnimationProcessor.addNewRequest(client, 8500, 0);
				InstanceDistributor.getSummoning();
				client.getActionAssistant().deleteItem(12183,
						(int) Summoning.summoningInfo[number][6] * lowest);
				client.getActionAssistant().deleteItem(charm, lowest);
				client.getActionAssistant().deleteItem(12155, lowest);
				client.getActionAssistant().deleteItem(1438, lowest);

				client.getActionAssistant().deleteItem(
						(int) Summoning.summoningInfo[number][8], lowest);
				InstanceDistributor.getSummoning();
				client.getActionSender().addItem(
						(int) Summoning.summoningInfo[number][0], lowest);
				InstanceDistributor.getSummoning();
				client.getActionAssistant().addSkillXP(
						Summoning.summoningInfo[number][9]
								* PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER
								* lowest, PlayerConstants.SUMMONING);
				client.getActionSender().sendWindowsRemoval();

			} else if (lowest < 1) {

				if (client.playerLevel[PlayerConstants.SUMMONING] < Summoning.summoningInfo[number][5]) {
					InstanceDistributor.getSummoning();
					client.getActionSender().sendMessage(
							"You need a summoning level of @red@"
									+ Summoning.summoningInfo[number][5]
									+ "@bla@ to make this.");
				}

				client.getActionSender()
						.sendMessage(
								"You need : "
										+ Math.round(Summoning.summoningInfo[number][6])
										+ " shards, an empty pouch, a "
										+ whichCharm(charm)
										+ " and a "
										+ whichSecondIngredient(Summoning.summoningInfo[number][8])
										+ ".");

			}
		}

	}

	public void makeScroll(Player client, int number) {
		number = number - 1;
		for (int i = 0; i < 28; i++) {
			InstanceDistributor.getSummoning();
			InstanceDistributor.getSummoning();
			if (client.playerLevel[PlayerConstants.SUMMONING] >= Summoning.summoningInfo[number][5]
					&& client.getActionAssistant().playerHasItem(
							(int) Summoning.summoningInfo[number][0], 1)) {
				AnimationProcessor.addNewRequest(client, 8500, 0);
				InstanceDistributor.getSummoning();
				client.getActionAssistant().deleteItem(
						(int) Summoning.summoningInfo[number][0], 1);
				InstanceDistributor.getSummoning();
				client.getActionSender().addItem(
						(int) Summoning.summoningInfo[number][1], 10);
				if ((int) Summoning.summoningInfo[number][1] == 12828) {
					client.progress[85]++;
					if (client.progress[85] >= 100)
						Achievements.getInstance().complete(client, 85);
					else
						Achievements.getInstance().turnYellow(client, 85);
				}
				InstanceDistributor.getSummoning();
				client.getActionAssistant().addSkillXP(
						Summoning.summoningInfo[number][2] / 2
								* PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
						PlayerConstants.SUMMONING);
				client.getActionSender().sendWindowsRemoval();
			} else {
				InstanceDistributor.getSummoning();
				if (client.playerLevel[PlayerConstants.SUMMONING] < Summoning.summoningInfo[number][5]) {
					InstanceDistributor.getSummoning();
					client.getActionSender().sendMessage(
							"You need a summoning level of @red@"
									+ Summoning.summoningInfo[number][5]
									+ "@bla@ to make this.");
				} else if (i == 0)
					client.getActionSender()
							.sendMessage(
									"You don't have the right pouch to create this scroll.");
			}
		}
	}

	public void makeSpecialSummoningScroll(Player client, int caseNumber) {
		int expMultiplier = 0;
		boolean ok = true;
		switch (caseNumber) {
		case 0: // Iron Titan
			if (client.playerLevel[PlayerConstants.SUMMONING] >= 95) {
				for (int i = 0; i < 28; i++)
					if (client.getActionAssistant().playerHasItem(12183, 198)
							&& client.getActionAssistant().playerHasItem(12160,
									1)
							&& client.getActionAssistant().playerHasItem(1115,
									1)
							&& client.getActionAssistant().playerHasItem(12155,
									1)) {
						client.getActionAssistant().deleteItem(12183, 198);
						client.getActionAssistant().deleteItem(12160, 1);
						client.getActionAssistant().deleteItem(1115, 1);
						client.getActionAssistant().deleteItem(12155, 1);
						client.getActionSender().addItem(12822, 1);
						expMultiplier++;
						ok = false;

					} else if (ok) {
						client.getActionSender()
								.sendMessage(
										"You need : 198 shards, an empty pouch, a Crimson charm and an iron platebody.");
						return;
					}

			} else
				client.getActionSender()
						.sendMessage(
								"You need a summoning level of : @red@95@bla@ to make this.");

			if (expMultiplier > 0)
				client.getActionAssistant().addSkillXP(
						417.6 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER
								* expMultiplier, PlayerConstants.SUMMONING);
			client.getActionSender().sendWindowsRemoval();
			break;

		case 1:
			if (client.playerLevel[PlayerConstants.SUMMONING] >= 96) {
				for (int i = 0; i < 28; i++)
					if (client.getActionAssistant().playerHasItem(12155, 1)
							&& client.getActionAssistant().playerHasItem(10818,
									1)
							&& client.getActionAssistant().playerHasItem(12160,
									1)
							&& client.getActionAssistant().playerHasItem(12183,
									211)) {
						client.getActionAssistant().deleteItem(12155, 1);
						client.getActionAssistant().deleteItem(10818, 1);
						client.getActionAssistant().deleteItem(12160, 1);
						client.getActionAssistant().deleteItem(12183, 211);
						client.getActionSender().addItem(12093, 1);
						expMultiplier++;
						ok = false;
					} else if (ok) {
						client.getActionSender()
								.sendMessage(
										"You need : 211 shards, an empty pouch, a Crimson charm, and yak-hide.");
						return;
					}

			} else
				client.getActionSender()
						.sendMessage(
								"You need a summoning level of : @red@99@bla@ to make this.");
			client.getActionSender().sendWindowsRemoval();
			if (expMultiplier > 0)
				client.getActionAssistant().addSkillXP(
						expMultiplier
								* PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER
								* 422.4, PlayerConstants.SUMMONING);
			break;

		case 2:
			if (client.playerLevel[PlayerConstants.SUMMONING] >= 99) {
				for (int i = 0; i < 28; i++)
					if (client.getActionAssistant().playerHasItem(12183, 178)
							&& client.getActionAssistant().playerHasItem(12160,
									1)
							&& client.getActionAssistant().playerHasItem(1119,
									1)
							&& client.getActionAssistant().playerHasItem(12155,
									1)) {
						client.getActionAssistant().deleteItem(12183, 178);
						client.getActionAssistant().deleteItem(12160, 1);
						client.getActionAssistant().deleteItem(1119, 1);
						client.getActionAssistant().deleteItem(12155, 1);
						client.getActionSender().addItem(12790, 1);
						expMultiplier++;
						ok = false;
					} else if (ok) {
						client.getActionSender()
								.sendMessage(
										"You need : 178 shards, an empty pouch, a Crimson charm and a steel platebody.");
						return;
					}

			} else
				client.getActionSender()
						.sendMessage(
								"You need a summoning level of : @red@99@bla@ to make this.");
			if (expMultiplier > 0)
				client.getActionAssistant().addSkillXP(
						435.2 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER
								* expMultiplier, PlayerConstants.SUMMONING);
			client.getActionSender().sendWindowsRemoval();
			break;
		}
	}

	public void npcInteraction(Player client) {
		if (client.familiarId == 6873)
			client.getDM().sendDialogue(15, -1);

		if (client.familiarId == 7365)
			if (client.getPlayerTeleportHandler().canTeleport()) {
				TeleportationHandler.addNewRequest(client, 2327, 3635, 0, 4);
				Achievements.getInstance().complete(client, 61);
				client.enteredGwdRoom = false;
				client.resetFaceDirection();
				client.resetWalkingQueue();
				client.setBusyTimer(5);
				AnimationProcessor.addNewRequest(client, 8939, 0);
				GraphicsProcessor.addNewRequest(client, 1576, 0, 0);
				GraphicsProcessor.addNewRequest(client, 1577, 100, 4);

				AnimationProcessor.addNewRequest(client, 8941, 5);
			}
		if (client.familiarId == 7363)
			if (client.getPlayerTeleportHandler().canTeleport()) {
				TeleportationHandler.addNewRequest(client, 2780, 3006, 0, 4);
				client.enteredGwdRoom = false;
				client.resetFaceDirection();
				client.resetWalkingQueue();
				client.setBusyTimer(5);
				AnimationProcessor.addNewRequest(client, 8939, 0);
				GraphicsProcessor.addNewRequest(client, 1576, 0, 0);
				GraphicsProcessor.addNewRequest(client, 1577, 100, 4);

				AnimationProcessor.addNewRequest(client, 8941, 5);
			}
	}

	/**
	 * A method which renews your familiar.
	 */
	public void renew(Player client) {
		if (!Summoning.canSummon(client, true))
			return;
		if (client.familiarId == 0) {
			client.getActionSender().sendMessage(
					"You do not have a familiar to renew.");
			return;
		}
		if (client.familiarTime > 5) {
			client.getActionSender()
					.sendMessage(
							"Your familiar still has too much time left, you can do this at five minutes or less.");
			return;
		}
		if (client.getActionAssistant().isItemInBag(client.pouchUsed)) {
			client.getActionAssistant().deleteItem(client.pouchUsed, 1);
			client.familiarTime = getTime(client.pouchUsed);
			client.getActionSender().sendMessage("You renew your familiar.");
			client.getActionSender().sendString(" " + client.familiarTime,
					18043);

		} else
			client.getActionSender().sendMessage(
					"You don't have the required pouch.");
	}

	/**
	 * 
	 * A method which is used to bring the familiar close to the owner Entity.
	 * 
	 */
	public void respawnFamiliar(final Player client) {

		if (!Summoning.canSummon(client, false))
			return;
		if (client.floor1()) {
			client.getActionSender().sendMessage(
					"You cannot bring familiars into dungeoneering.");
			return;
		}

		final int itemID = client.pouchUsed;
		final int slot = InstanceDistributor.getNPCManager().freeSlot();

		final NPCDefinition def = InstanceDistributor.getNPCManager().npcDefinitions
				.get(getNpcID(itemID));
		if (def == null)
			return;

		client.getActionAssistant().deleteItem(itemID, 1);

		final NPC familiar = new NPC(slot, def, client.getAbsX(),
				client.getAbsY(), client.getHeightLevel());
		InstanceDistributor.getNPCManager().npcMap.put(familiar.getNpcId(),
				familiar);
		familiar.setWalking(true);
		familiar.setOwner(client);
		familiar.setFollowing(client);
		if (!Summoning.canSummon(client, false)) {
			familiar.isHidden = true;
			familiar.updateRequired = true;
			client.canSummon = false;
		}
		client.setFamiliar(familiar);
		client.familiarId = getNpcID(itemID);
		GraphicsProcessor.addNewRequest(familiar, summonGfx, 0, 0);
		client.pouchUsed = itemID;

		if (BoB(client))
			client.familiarSpotsTotal = getSpots(itemID);

	}

	public void sendStorageMenu(Player client, String food) {
		if (client.familiarId == 0) {
			client.getActionSender().sendMessage("You cannot do that!");
			return;
		}
		if (food == "Rocktail")
			client.dialogueAction = 1000;
		if (food == "Manta Ray")
			client.dialogueAction = 1001;
		if (food == "Shark")
			client.dialogueAction = 1002;
		if (food == "Monkfish")
			client.dialogueAction = 1003;
		if (food == "Lobster")
			client.dialogueAction = 1004;
		client.getDM().sendDialogue(12, -1);
	}

	/**
	 * A method which handles the special attacks of summoning familiars.
	 * 
	 * @param client
	 */
	public void specialAttack(Player client) {
		if (!Summoning.canSummon(client, true))
			return;
		if (client.familiarId == 0) {
			client.getActionSender().sendMessage(
					"You currently do not have a familiar.");
			return;
		}
		if (!client.getActionAssistant().playerHasItem(
				getScroll(client.pouchUsed), 1)) {
			client.getActionSender().sendMessage(
					"You don't have the required special scroll.");
			return;
		}
		if (System.currentTimeMillis() - client.lastSummoningSpecial < 10000) {
			client.getActionSender().sendMessage(
					"You have to wait some more to do this again.");
			return;
		}
		client.lastSummoningSpecial = System.currentTimeMillis();
		if (client.familiarId == 6873) {
			client.getActionSender().sendMessage(
					"Please use the scroll on the item you which to bank");
			// client.getActionAssistant().deleteItem(getScroll(client.pouchUsed),
			// 1);

			return;
		} else if (client.familiarId == 6869) {
			client.getActionAssistant().increaseStat(PlayerConstants.MAGIC, 8);
			AnimationProcessor.addNewRequest(client, 7660, 0);
			GraphicsProcessor.addNewRequest(client, 1303, 0, 3);
			client.getActionAssistant().deleteItem(getScroll(client.pouchUsed),
					1);
			AnimationProcessor.addNewRequest(client, 7660, 0);

		} else if (client.familiarId == 6822) {
			client.getActionAssistant().addHP(
					client.getLevelForXP(client.playerXP[3]) / 10 * 3 / 2);
			GraphicsProcessor.addNewRequest(client, 1303, 0, 0);
			client.getActionAssistant().deleteItem(getScroll(client.pouchUsed),
					1);
		} else if (client.familiarId == 7343) {
			attack(client);
			client.steelTitanSpec = true;
			client.getActionAssistant().deleteItem(getScroll(client.pouchUsed),
					1);
			AnimationProcessor.addNewRequest(client, 7660, 0);
		} else if (client.familiarId == 7375) {
			attack(client);
			client.ironTitanSpec = true;
			client.getActionAssistant().deleteItem(getScroll(client.pouchUsed),
					1);
			AnimationProcessor.addNewRequest(client, 7660, 0);
		} else if (client.familiarId == 6853) {
			attack(client);
			client.bronzeMinotaurSpec = true;
			Achievements.getInstance().complete(client, 35);
			client.getActionAssistant().deleteItem(getScroll(client.pouchUsed),
					1);
			AnimationProcessor.addNewRequest(client, 7660, 0);
		} else if (client.familiarId == 6855) {
			attack(client);
			client.ironMinotaurSpec = true;
			Achievements.getInstance().complete(client, 35);
			client.getActionAssistant().deleteItem(getScroll(client.pouchUsed),
					1);
			AnimationProcessor.addNewRequest(client, 7660, 0);
		} else if (client.familiarId == 6857) {
			attack(client);
			client.steelMinotaurSpec = true;
			client.getActionAssistant().deleteItem(getScroll(client.pouchUsed),
					1);
			AnimationProcessor.addNewRequest(client, 7660, 0);
		} else if (client.familiarId == 6859) {
			attack(client);
			client.mithrilMinotaurSpec = true;
			Achievements.getInstance().complete(client, 35);
			client.getActionAssistant().deleteItem(getScroll(client.pouchUsed),
					1);
			AnimationProcessor.addNewRequest(client, 7660, 0);
		} else if (client.familiarId == 6861) {
			attack(client);
			client.adamantMinotaurSpec = true;
			Achievements.getInstance().complete(client, 35);
			client.getActionAssistant().deleteItem(getScroll(client.pouchUsed),
					1);
			AnimationProcessor.addNewRequest(client, 7660, 0);
		} else if (client.familiarId == 6863) {
			attack(client);
			client.runeMinotaurSpec = true;
			Achievements.getInstance().complete(client, 35);
			client.getActionAssistant().deleteItem(getScroll(client.pouchUsed),
					1);
			AnimationProcessor.addNewRequest(client, 7660, 0);

		} else
			client.getActionSender().sendMessage(
					"This familiar doesn't have a special attack yet.");
	}

	public void store(Player client, int food, int amount) {
		if (!Summoning.canSummon(client, true))
			return;
		final String foodName = determineName(food);
		client.dialogueAction = 0;
		if (amount == 0) {
			client.getActionSender().sendMessage(
					"You don't have any " + foodName + ".");
			client.getActionSender().sendWindowsRemoval();
			return;
		}
		if (client.foodIdStored > 0 && client.foodAmountStored == 0)
			client.foodIdStored = 0;
		if (food != client.foodIdStored && client.foodIdStored != 0) {
			client.getActionSender().sendMessage(
					"You familiar is already carrying "
							+ determineName(client.foodIdStored)
							+ ". You can only store one type");
			client.getActionSender()
					.sendMessage(
							"of food at the same time, please take the old ones out to store this new type.");
			client.getActionSender().sendWindowsRemoval();
			return;
		}

		if (!client.getActionAssistant().playerHasItem(food, amount)) {
			client.getActionSender().sendWindowsRemoval();
			client.getActionSender().sendMessage(
					"You do not have the required amount of " + foodName + ".");
			return;
		}
		if (client.foodAmountStored == client.familiarSpotsTotal) {
			client.getActionSender().sendMessage(
					"Your BoB is already carrying @red@"
							+ client.familiarSpotsTotal + "@bla@ " + foodName
							+ ".");
			client.getActionSender().sendWindowsRemoval();
			return;
		}
		final int availableWahns = client.familiarSpotsTotal
				- client.foodAmountStored;
		if (client.foodAmountStored + amount > client.familiarSpotsTotal)
			for (int i = 0; i < availableWahns; i++)
				client.getActionAssistant().deleteItem(food, 1);
		else
			for (int i = 0; i < amount; i++)
				client.getActionAssistant().deleteItem(food, 1);

		client.foodIdStored = food;
		if (client.foodAmountStored + amount > client.familiarSpotsTotal)
			client.foodAmountStored = client.familiarSpotsTotal;
		else
			client.foodAmountStored += amount;

		if (client.foodAmountStored == client.familiarSpotsTotal)
			client.getActionSender().sendMessage(
					"Your BoB now is on it's maximum load of @red@"
							+ client.familiarSpotsTotal + "@bla@ slots.");
		else
			client.getActionSender().sendMessage(
					"Your BoB has now used @red@" + client.foodAmountStored
							+ "@bla@ of @red@" + client.familiarSpotsTotal
							+ "@bla@ available slots.");
		client.getActionSender().sendWindowsRemoval();
		client.dialogueAction = 0;

	}

	/**
	 * A method which summons the npc, and also does various checks.
	 * 
	 * @param client
	 * @param npcId
	 * @param itemID
	 */
	public void summonNpc(Player client, int itemID) {
		if (client.familiarId > 0 && !client.login) {
			client.getActionSender()
					.sendMessage("You already have a familiar.");
			return;
		}
		if (client.floor1() || client.floor2() || client.floor3()) {
			client.getActionSender().sendMessage(
					"You cannot bring familiars into dungeoneering.");
			return;
		}
		if (client.inFightCaves()) {
			client.getActionSender().sendMessage("You cannot do this here.");
			return;
		}
		if (Areas.isInDuelArenaFight(client.getCoordinates())) {
			client.getActionSender().sendMessage(
					"You cannot summon familiars here.");
			return;
		}
		if (client.inPcBoat() || client.inPcGame() || client.doingHFTD()
				|| Areas.isInCastleWarsGame(client.getPosition())
				|| Areas.isInCastleWarsSafeZone(client.getPosition())
				|| Areas.isInCastleWarsLobby(client.getPosition())) {
			client.getActionSender().sendMessage(
					"You cannot summon familiars here.");
			return;
		}
		if (client.playerLevel[PlayerConstants.SUMMONING] < getLevel(itemID)) {
			client.getActionSender().sendMessage(
					"You need atleast " + getLevel(itemID)
							+ " to summon this familiar.");
			return;
		}

		final int slot = InstanceDistributor.getNPCManager().freeSlot();
		final NPCDefinition def = InstanceDistributor.getNPCManager().npcDefinitions
				.get(getNpcID(itemID));
		if (def == null)
			// system.out.println("Unknown Summoning def " + getNpcID(itemID));
			return;
		if (!client.getActionAssistant().playerHasItem(itemID, 1)
				&& !client.login)
			return;
		client.getActionAssistant().deleteItem(itemID, 1);
		if (itemID == 12043)
			Achievements.getInstance().complete(client, 8);
		final NPC familiar = new NPC(slot, def, client.getAbsX(),
				client.getAbsY(), client.getHeightLevel());
		InstanceDistributor.getNPCManager().npcMap.put(familiar.getNpcId(),
				familiar);

		client.setFamiliar(familiar);
		familiar.setWalking(true);
		familiar.setOwner(client);
		familiar.setFollowing(client);

		familiar.setHeightLevel(client.getHeightLevel());
		client.familiarId = getNpcID(itemID);

		GraphicsProcessor.addNewRequest(familiar, summonGfx, 0, 0);
		if (!Summoning.canSummon(client, true)) {
			familiar.isHidden = true;
			familiar.updateRequired = true;
			client.canSummon = false;
		}
		client.pouchUsed = itemID;

		client.getActionSender().sendFrame200(18021, 9579);
		client.getActionSender().sendFrame75(getNpcID(itemID), 18021);
		client.progress[29]++;
		if (client.progress[29] >= 20)
			Achievements.getInstance().complete(client, 29);
		else
			Achievements.getInstance().turnYellow(client, 29);
		if (client.familiarTime == 0) {
			client.familiarTime = getTime(itemID);
			client.getActionSender().sendString(" " + client.familiarTime,
					18043);

			client.getActionSender().sendString(
					"" + familiar.getDefinition().getName(), 18028);

		}
		if (BoB(client))
			client.familiarSpotsTotal = getSpots(itemID);

	}

	public String whichCharm(int charm) {
		if (charm == 12158)
			return "Gold Charm";
		else if (charm == 12159)
			return "Green Charm";
		else if (charm == 12160)
			return "Chrimson charm";
		else if (charm == 12163)
			return "Blue Charm";
		return "";
	}

	public String whichSecondIngredient(double itemId) {
		if (itemId == 2859.0)
			return "Wolf bone";
		else if (itemId == 2138.0)
			return "Raw chicken";
		else if (itemId == 6291.0)
			return "Spider carcass";
		else if (itemId == 3363.0)
			return "Thin snail";
		else if (itemId == 440.0)
			return "Iron ore";
		else if (itemId == 6319.0)
			return "Proboscis";
		else if (itemId == 1783.0)
			return "Bucket of sand";
		else if (itemId == 3095.0)
			return "Bronze claws";
		else if (itemId == 12168.0)
			return "Obsidian charm";
		else if (itemId == 2134.0)
			return "Raw rat meat";
		else if (itemId == 3138.0)
			return "Potato cactus";
		else if (itemId == 6032.0)
			return "Compost";
		else if (itemId == 9976.0)
			return "Chinchompa";
		else if (itemId == 3325.0)
			return "Vampire dust";
		else if (itemId == 12156.0)
			return "Honeycomb";
		else if (itemId == 1519.0)
			return "Willow log";
		else if (itemId == 12164.0)
			return "Ravager charm";
		else if (itemId == 12165.0)
			return "Shifter charm";
		else if (itemId == 12166.0)
			return "Spinner charm";
		else if (itemId == 12167.0)
			return "Torcher charm";
		else if (itemId == 2349.0)
			return "Bronze bar";
		else if (itemId == 6010.0)
			return "Marigolds";
		else if (itemId == 13572.0)
			return "Clean guam";
		else if (itemId == 12153.0)
			return "Carved evil turnip";
		else if (itemId == 12109.0)
			return "Cockatrice egg";
		else if (itemId == 12111.0)
			return "Guthatrice egg";
		else if (itemId == 12113.0)
			return "Saratrice egg";
		else if (itemId == 12115.0)
			return "Zamatrice egg";
		else if (itemId == 12117.0)
			return "Pengatrice egg";
		else if (itemId == 12119.0)
			return "Coraxatrice egg";
		else if (itemId == 12121.0)
			return "Vulatrice egg";
		else if (itemId == 2351.0)
			return "Iron bar";
		else if (itemId == 590.0)
			return "Tinderbox";
		else if (itemId == 1635.0)
			return "Gold ring";
		else if (itemId == 2132.0)
			return "Raw beef";
		else if (itemId == 9978.0)
			return "Raw bird meat";
		else if (itemId == 12161.0)
			return "Abyssal charm";
		else if (itemId == 1937.0)
			return "Jug of water";
		else if (itemId == 311.0)
			return "Harpoon";
		else if (itemId == 2353.0)
			return "Steel bar";
		else if (itemId == 10099.0)
			return "Graahk fur";
		else if (itemId == 10101.0)
			return "Tatty kyatt fur";
		else if (itemId == 10103.0)
			return "Larupia fur";
		else if (itemId == 6667.0)
			return "Fishbowl";
		else if (itemId == 9736.0)
			return "Goat horn dust";
		else if (itemId == 12161.0)
			return "Abyssal charm";
		else if (itemId == 6287.0)
			return "Snake hide";
		else if (itemId == 8431.0)
			return "Bagged plant 1";
		else if (itemId == 2150.0)
			return "Swamp toad";
		else if (itemId == 2359.0)
			return "Mithril bar";
		else if (itemId == 7939.0)
			return "Tortoise shell";
		else if (itemId == 383.0)
			return "Raw shark";
		else if (itemId == 1963.0)
			return "Banana";
		else if (itemId == 1933.0)
			return "Pot of flour";
		else if (itemId == 10117.0)
			return "Polar kebbit fur";
		else if (itemId == 14616.0)
			return "Phoenix quill";
		else if (itemId == 12168.0)
			return "Obsidian charm";
		else if (itemId == 6979.0)
			return "Granite (500g)";
		else if (itemId == 2462.0)
			return "Red flower";
		else if (itemId == 10020.0)
			return "Ruby harvest";
		else if (itemId == 2361.0)
			return "Adamant bar";
		else if (itemId == 12795.0)
			return "Talon beast pouch";
		else if (itemId == 5933.0)
			return "Willow branch";
		else if (itemId == 1442.0)
			return "Fire talisman";
		else if (itemId == 1440.0)
			return "Earth talisman";
		else if (itemId == 1444.0)
			return "Fire talisman";
		else if (itemId == 571.0)
			return "Water orb";
		else if (itemId == 6255.0)
			return "Broodoo shield";
		else if (itemId == 12168.0)
			return "Obsidian charm";
		else if (itemId == 10149.0)
			return "Swamp lizard";
		else if (itemId == 2363.0)
			return "Rune bar";
		else if (itemId == 237.0)
			return "Unicorn horn";
		else if (itemId == 1444.0)
			return "Water talisman";
		else if (itemId == 2859.0)
			return "Wolf bones";
		else if (itemId == 12161.0)
			return "Abysall charm";
		else if (itemId == 1115.0)
			return "Iron platebody";
		else if (itemId == 10818.0)
			return "Yak-hide";
		return "";
	}

	/**
	 * A method which withdraws items from the BoB
	 * 
	 */
	public void withdraw(Player client) {
		if (!Summoning.canSummon(client, true))
			return;
		if (client.familiarId == 0) {
			client.getActionSender().sendMessage("You don't have a familiar.");
			return;
		}
		if (client.foodIdStored > 0 && client.foodAmountStored == 0)
			client.foodIdStored = 0;
		if (!BoB(client) && client.familiarId > 0) {
			client.getActionSender().sendMessage(
					"This familiar doesn't have a storage function.");
			return;
		}
		if (client.foodAmountStored == 0)
			client.getActionSender().sendMessage(
					"Your familiar isn't carrying anything.");
		int withdrawAmount = client.getActionAssistant().freeSlots();
		if (withdrawAmount > client.foodAmountStored)
			withdrawAmount = client.foodAmountStored;
		client.foodAmountStored -= withdrawAmount;
		for (int i = 0; i < withdrawAmount; i++)
			client.getActionSender().addItem(client.foodIdStored, 1);

		if (client.foodAmountStored != 0)
			client.getActionSender().sendMessage(
					"Your familiar has@red@ " + client.foodAmountStored
							+ "@bla@ " + determineName(client.foodIdStored)
							+ " left.");
	}
}
