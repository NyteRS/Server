package com.server2.content;

import com.server2.model.entity.player.Player;

/**
 * 
 * @author Lukas Pinckers
 * 
 */
public class Achievements {

	private static Achievements instance = new Achievements();

	public static final String npcName = "";

	// 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22
	protected static String[] easyTasks = { "Tan some leather",
			"Kill 20 chickens", "Craft a sapphire ring", "Eat 20 salmon",
			"Pick a banana", "Make 3 attack potions", "Farm a guam",
			"Make 15 spirit wolf pouches", "Summon a dreadfowl",
			"Chop 10 normal logs", "Make 30 bow strings",
			"Fletch 100 headless arrows", "Complete a floor 1 dungeon",
			"Fish 50 tunas", "Burn a fish",
			"Steal from the wine stall in draynor", "Mine 10 pure essence",
			"Perform a special attack", "Pick some flax",
			"Talk to the server2 Guide", "Setup and dwarf multicannon",
			"Drink a super restore in dung", "Buy a pk set" };

	// 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42
	protected static String[] mediumTasks = { "Kill a revenant",
			"Complete a slayer task from all 5 masters", "Die 15 times",
			"Defeat the dagannoth mother", "Pickpocket 50 master farmers",
			"Cure a diseased plant", "Summon 20 familiars",
			"Lose a game of pest control", "Defeat 500 monsters",
			"Upgrade a skillcape", "Offer a frost dragon bone on a altar",
			"Win a fight in the duel arena", "Use a Bull rush scroll",
			"Kill a black dragon", "Bury 100 bones of any kind",
			"Buy a lottery ticket", "Get a god cape", "Make a saradomin brew",
			"Fill a medium pouch with essence", "Buy something with tokkul" };
	// 43 44 45 46 47 48 49 50 51 52 53 54 55 56 57 58 59 60 61 62 63 64 65
	protected static String[] hardTasks = { "Cook 100 sharks",
			"Fish 500 monk fish", "Farm a palm tree", "Light 100 magic logs",
			"Defeat all godwars bosses", "Hit 700+ with magic",
			"Complete a floor 3 without eating", "Kill the chaos elemental",
			"Win 10 games of pest control", "Defeat the jad",
			"Make 10 overload potions", "Bury 15 ourg bones",
			"Cancel a slayer task", "Find a dark bow",
			"Complete 10 agility courses", "Smith a rune scimitar",
			"Craft 50 nature runes simultaniously",
			"Catch 75 red chinchompa's", "Craft 20 red leather chaps",
			"Teleport using a spirit kyatt", "Exchange a artifact",
			"Find a pair of dragon boots", "Recharge your dfs" };
	// 66 67 68 69 70 71 72 73 74 75 76 77 78 79 80 81 82 83 84 85 86 87 88 89
	// 90 91 92 93
	protected static String[] eliteTasks = { "Vote 100 times",
			"Capture 10 flags in castle wars",
			"Play 100 games of pest control",
			"Defeat 1000 players in the wildy", "Win 5 fight pit games",
			"Search 50 bird nests", "Farm all types of herbs",
			"Kill 10000 monsters", "Mine 300 runite ores", "Defeat nomad",
			"Defeat nex 15 times", "Defeat the avatar of destruction 20 times",
			"Complete all possible slayer tasks",
			"Kill a player with vengeance", "Earn 1B with alching",
			"Kill 150 tormented demons", "Find 10 effigies",
			"Find a dragon hatchet", "Fire 5000 cannon balls",
			"Make 1000 iron titan scrolls", "Kill 500 monsters in dung",
			"Bank 100 items using winter storage",
			"Complete 10 duo slayer tasks", "Loot 100 barrow chests",
			"Heal 50000hp", "Exchange a armour box", "Hit 900+ with melee",
			"Buy a amulet of fury with tokkul" };

	public static String[] easyString(final Player player) {
		final String[] copy = easyTasks;
		for (int j = 0; j < copy.length; j++) {
			String add = "@red@";
			if (player.stages[j] == 1)
				add = "@yel@";
			else if (player.stages[j] == 2)
				add = "@gre@";
			copy[j] = add + copy[j];

		}

		return copy;
	}

	public static String[] eliteString(final Player player) {
		final String[] copy = eliteTasks;
		for (int j = 0; j < copy.length; j++) {
			String add = "@red@";
			if (player.stages[j] == 1)
				add = "@yel@";
			else if (player.stages[j] == 2)
				add = "@gre@";
			copy[j] = add + copy[j];

		}

		return copy;
	}

	public static Achievements getInstance() {
		return instance;
	}

	public static String[] hardString(final Player player) {
		final String[] copy = hardTasks;
		for (int j = 0; j < copy.length; j++) {
			String add = "@red@";
			if (player.stages[j] == 1)
				add = "@yel@";
			else if (player.stages[j] == 2)
				add = "@gre@";
			copy[j] = add + copy[j];

		}

		return copy;
	}

	public static String[] mediumString(final Player player) {
		final String[] copy = mediumTasks;
		for (int j = 0; j < copy.length; j++) {
			String add = "@red@";
			if (player.stages[j] == 1)
				add = "@yel@";
			else if (player.stages[j] == 2)
				add = "@gre@";
			copy[j] = add + copy[j];

		}

		return copy;
	}

	public void complete(Player client, int key) {
		if (key > client.stages.length) {
			System.out.println("arrayoutofbounce complete " + key);
			return;
		}
		/*
		 * if(client.stages[key]==2){ return; }
		 * 
		 * client.stages[key]=2; String type="easy"; String name=""; int[]
		 * loopParams = {0, 23}; if(key<23){ name="@blu@"+easyTasks[key];
		 * 
		 * } else if(key>22 && key<43){ type="medium";
		 * name="@blu@"+easyTasks[key-23]; loopParams[0]=23; loopParams[1]=43; }
		 * else if(key>42 && key<66){ type="hard";
		 * name="@blu@"+easyTasks[key-23-20]; loopParams[0]=43;
		 * loopParams[1]=66; } else if(key>65){ type="elite";
		 * name="@blu@"+easyTasks[key-23-20-23]; loopParams[0]=66;
		 * loopParams[1]=client.stages.length; } String str =
		 * "You have completed the "+type+" task "+name+".";
		 * client.getActionSender().sendMessage(str); boolean completedAll=true;
		 * for(int i=loopParams[0]; i<loopParams[1]; i++){
		 * if(client.stages[i]!=2){ completedAll=false; } } if(completedAll){
		 * client
		 * .getActionSender().sendMessage("@red@ You have completed all "+type
		 * +" tasks!");
		 * client.getActionSender().sendMessage("Return to "+npcName
		 * +" to claim your reward."); }
		 */
	}

	public void turnYellow(Player client, int key) {
		if (key > client.stages.length) {
			System.out.println("arrayoutofbounce turnyellow " + key);
			return;
		}
		if (client.stages[key] == 0)
			client.stages[key] = 1;
		// TODO change color
	}
	/*
	 * client.progress[5]++; if (client.progress[5] >= 3) {
	 * Achievements.getInstance().complete(client, 5); } else {
	 * Achievements.getInstance().turnYellow(client, 5); }
	 */
}
