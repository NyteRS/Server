package com.server2.content.misc.mobility;

import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene & Lukas
 */
public class MenuTeleports {

	public static final String[][] places = {
			/** 9190 **/
			/** 9191 **/
			/** 9192 **/
			/** 9193 **/
			/** 9194 **/
			{ "Pking Areas", "Edgeville @bla@[@dre@High-Risk@bla@]",
					"East Dragons @bla@[@dre@High-Risk@bla@]",
					"Mage Bank @bla@[@dre@Hybrids@bla@]",
					"West Dragons @bla@[@dre@Low-Risk@bla@]",
					"Ice Plateau @bla@[@dre@Deep-Wildy@bla@]" },
			/** 0 **/
			{ "Training", "Rock Crabs", "Experiments", "Yaks", "Bandits",
					"Next" },
			/** 1 **/
			{ "City Areas", "Varrock", "Falador", "Camelot", "Ardougne",
					"Lumbridge" },
			/** 2 **/
			{ "Minigames", "Barrows", "Duel Arena", "Pest Control",
					"Warriors Guild", "Next" },
			/** 3 **/
			{ "Sail to?", "Port Sarim", "Port Karamja", "Port Khazard",
					"Home Port", "No where" },
			/** 4 **/
			{ "Training", "Ghouls", "Chaos Druids", "TzHaar", "Dust devils",
					"Next" },
			/** 5 **/
			{ "Slayer", "Fremennik Slayer Dungeon", "Slayer tower",
					"Frost dragons", "Brimhaven dungeon", "Next" },
			/** 6 **/
			{ "Slayer", "Asgarnian ice dungeon", "Taverley dungeon",
					"Light house", "Mysterious dungeon", "Water cave" },
			/** 7 **/
			{ "Skilling", "Woodcutting", "Fishing", "Farming", "Summoning",
					"Next" },
			/** 8 **/
			{ "Skilling", "Mining", "Smithing", "Agility", "Thieving", "Hunter" },
			/** 9 **/
			{ "Woodcutting", "Varrock east", "Draynor", "Seers village",
					"Tree Gnome Stronghold", "Isafdar" },
			/** 10 **/
			{ "Fishing", "Draynor", "Barbarian village", "Fishing guild",
					"Shilo village", "Next" },
			/** 11 **/
			{ "Fishing", "Catherby", "Piscatoris Fishing Coloney", "", "", "" },
			/** 12 **/
			{ "Mining", "Varrock east", "Al Kharid", "Dwarfen mine",
					"Lumbridge east", "Runite" },
			/** 13 **/
			{ "Runite", "Heroes guild", "Wilderness", "Keldarim", "Neitiznot",
					"" },
			/** 14 **/
			{ "Smithing", "Anvil", "Furnace", "", "", "" },
			/** 15 **/
			{ "Thieving", "Edgeville", "Ape Atoll", "Draynor", "Ardougne", "" },
			/** 16 **/
			{ "Farming", "Catherby", "Ardougne", "Canifis", "Falador", "Next" },
			/** 17 **/
			{ "Farming", "Taverly", "Tree Gnome Stronghold", "Lumbridge",
					"Brimhaven", "Tree Gnome Village" },
			/** 18 **/
			{ "Summoning", "Unicorns", "Waterfiends", "Rock Lobsters",
					"Giant Rock Crabs", "Wallasalki" },
			/** 19 **/
			{ "Bosses", "God wars dungeon", "Dagannoth kings", "Frost dragons",
					"Tormented demons", "Next" },
			/** 20 **/
			{ "Bosses", "KBD @bla@(@dre@Lvl-40 Wild@bla@)", "Nex", "Nomad",
					"Corporal Beast", "Next" },
			/** 21 */
			{ "Bosses", "Chaos Elemental", "Barrelchest Boss", "Glacors",
					"Avatar Of Destruction", "Back" },
			/** 22 */
			{ "Training", "Chickens", "Monkey skeletons", "Ape-Atoll Guards",
					"Armoured Zombies", "Back" },
			/** 23 **/
			{ "Hunter", "Red Chins", "Grey chins", "Birds", "", "" },
			/** 24 **/
			{ "Minigames", "Fight caves", "Castle wars", "Fight pits",
					"Riot Wars", "" },
			/*** * 25 **/

			{ "Multi Teleports", "Varrock Wilderness", "Greater Demons",
					"50 Portals", "Ruins", "" },
			/** 26 */

			{ "Frost Dragons", "Frost Dragons[Ice Path]",
					"Frost Dragons[Ice Plateau, @dre@Wilderness@bla@]",
					"Frost Dragons[White Wolf Mountain]",
					"Frost Dragons[Trollheim, Multi]", "" },

	};

	public static final int[][] teleportMenuLocation = {
			{ 9190, 0, 3087, 3516 }, { 9190, 1, 2673, 3712 },
			{ 9190, 2, 3209, 3425 }, { 9190, 3, 3565, 3309 },
			{ 9190, 4, 3029, 3217 }, { 9190, 5, 3419, 3511 },
			{ 9190, 6, 2796, 3615 }, { 9190, 7, 3048, 9582 },
			{ 9190, 8, 10, 10 }, { 9190, 9, 13, 13 }, { 9190, 10, 3282, 3419 },
			{ 9190, 11, 3088, 3229 }, { 9190, 12, 2844, 3431 },
			{ 9190, 13, 3285, 3365 }, { 9190, 14, 2937, 9884 },
			{ 9190, 15, 3187, 3425 }, { 9190, 16, 3086, 3485 },
			{ 9190, 17, 2808, 3463 }, { 9190, 18, 2932, 3437 },
			{ 9190, 19, 3091, 3449 }, { 9190, 20, 2871, 5320 },
			{ 9190, 21, 3003, 3850 }, { 9190, 22, 3259, 3924 },
			{ 9190, 23, 3235, 3295 }, { 9190, 24, 2813, 3028 },
			{ 9190, 25, 2445, 5177 }, { 9190, 26, 3243, 3517 },
			{ 9190, 27, 2867, 9955 }, { 9191, 0, 3370, 3696 },
			{ 9191, 1, 3555, 9944 }, { 9191, 2, 2964, 3378 },
			{ 9191, 3, 3362, 3264 }, { 9191, 4, 2907, 3191 },
			{ 9191, 5, 2928, 9844 }, { 9191, 6, 3428, 3536 },
			{ 9191, 7, 2884, 3395 }, { 9191, 8, 11, 11 }, { 9191, 9, 15, 15 },
			{ 9191, 10, 3086, 3234 }, { 9191, 11, 3103, 3433 },
			{ 9191, 12, 2334, 3690 }, { 9191, 13, 3300, 3300 },
			{ 9191, 14, 3058, 3885 }, { 9191, 15, 3276, 3186 },
			{ 9191, 16, 2757, 2773 }, { 9191, 17, 2664, 3376 },
			{ 9191, 18, 2472, 3445 }, { 9191, 19, 2967, 9504 },
			{ 9191, 20, 2907, 4449 }, { 9191, 21, 2470, 4775 },
			{ 9191, 22, 2596, 4666 }, { 9191, 23, 2805, 9142 },
			{ 9191, 24, 2684, 4667 }, { 9191, 25, 2442, 3089 },
			{ 9191, 26, 3288, 3886 }, { 9191, 27, 2975, 3919 },
			{ 9192, 0, 2539, 4716 }, { 9192, 1, 2325, 3804 },
			{ 9192, 2, 2757, 3478 }, { 9192, 3, 2657, 2644 },
			{ 9192, 4, 2676, 3170 }, { 9192, 5, 2480, 5173 },
			{ 9192, 6, 2867, 9955 }, { 9192, 7, 2507, 3630 },
			{ 9192, 8, 17, 17 }, { 9192, 9, 2473, 3438 },
			{ 9192, 10, 2725, 3484 }, { 9192, 11, 2597, 3410 },
			{ 9192, 12, 12, 12 }, { 9192, 13, 3045, 9781 },
			{ 9192, 14, 2935, 10229 }, { 9192, 15, 15, 15 },
			{ 9192, 16, 3079, 3251 }, { 9192, 17, 3603, 3527 },
			{ 9192, 18, 3196, 3230 }, { 9192, 19, 2492, 10147 },
			{ 9192, 20, 2867, 9955 }, { 9192, 21, 3201, 3169 },
			{ 9192, 22, 2834, 3822 }, { 9192, 23, 2794, 2774 },
			{ 9192, 24, 2834, 2920 }, { 9192, 25, 2399, 5179 },
			{ 9192, 26, 3292, 3924 }, { 9192, 27, 2848, 3498 },

			{ 9193, 0, 2984, 3591 }, { 9193, 1, 3170, 2983 },
			{ 9193, 2, 2662, 3307 }, { 9193, 3, 2868, 3546 },
			{ 9193, 4, 2804, 3421 }, { 9193, 5, 3274, 2937 },
			{ 9193, 6, 2744, 3152 }, { 9193, 7, 3297, 9825 },
			{ 9193, 8, 19, 19 }, { 9193, 9, 16, 16 }, { 9193, 10, 2493, 3399 },
			{ 9193, 11, 2863, 2968 }, { 9193, 12, 12, 12 },
			{ 9193, 13, 3241, 3165 }, { 9193, 14, 2347, 3894 },
			{ 9193, 15, 15, 15 }, { 9193, 16, 2662, 3305 },
			{ 9193, 17, 3007, 3363 }, { 9193, 18, 2765, 3216 },
			{ 9193, 19, 2492, 10131 }, { 9193, 20, 2337, 9799 },
			{ 9193, 21, 2086, 4429 }, { 9193, 23, 3112, 9684 },
			{ 9193, 22, 2910, 3612 }, { 9193, 25, 3311, 2800 },
			{ 9193, 26, 3372, 3891 }, { 9193, 27, 2839, 3739 },
			{ 9194, 0, 2975, 3919 }, { 9194, 2, 0, 0 }, { 9194, 3, 0, 0 },
			{ 9194, 4, 0, 0 }, { 9194, 5, 5, 1 }, { 9194, 6, 6, 7 },
			{ 9194, 7, 7, 7 }, { 9194, 8, 9, 9 }, { 9194, 9, 0, 0 },
			{ 9194, 10, 10, 10 }, { 9194, 11, 12, 12 }, { 9194, 13, 14, 14 },
			{ 9194, 17, 18, 18 }, { 9194, 18, 18, 18 },
			{ 9194, 19, 2492, 10163 }, { 9194, 20, 20, 20 },
			{ 9194, 21, 3259, 3924 }, { 9194, 23, 0, 0 } };

	private static int teleportNameConfiguration[] = { 1382,//
			1383, 1300,//
			1301, 1350,//
			1351, 1325,//
			1326, 1415, 1416, 1454, 1455, 7457, 7458, 13037,//
			13038, 13047, 13048, 13055,//
			13056, 13063, 13064, 13071,//
			13072, 13081, 13082, 13089,//
			13090, 13097, 13098,

	};

	private static String names[] = { "Slayer Teleports",
			"Teleports to slayer areas.", "Training Teleports",
			"Teleports you to training areas.", "Pking Teleports",
			"Teleports you to pking areas.", "Minigame Teleports",
			"Teleports you to minigames.", "City Teleports",
			"Teleports you to cities.", "Skilling Teleports",
			"Teleports you to skilling areas.", "Boss Teleports",
			"Teleports you to bosses.", "Training Teleports",
			"Teleports you to training areas.", "Minigame Teleports",
			"Teleports you to minigames.", "Pking Teleports",
			"Teleports you to pking areas.", "Slayer Teleports",
			"Teleports you to slayer areas.", "Skilling Teleport",
			"Teleports you to skilling areas.", "City Teleports",
			"Teleports you to Cities.", "Boss Teleports",
			"Teleports you to bosses.", "City Teleports",
			"Teleports you to Cities.", };

	/**
	 * Creates a new teleport menu on which a user can choose where he'd like to
	 * go.
	 * 
	 * @param client
	 * @param config
	 * @param again
	 */
	public static void createTeleportMenu(Player client, int config,
			boolean again) {
		if (client.isBusy()
				|| client.teleporting + 2400 > System.currentTimeMillis()
				&& !again)
			return;

		if (client.getPlayerTeleportHandler().canTeleport()) {
			for (int i = 0; i < client.teleport.length; i++)
				client.teleport[i] = i == config;
			client.teleportConfig = config;
			client.getActionSender().selectOption(places[config][0],
					places[config][1], places[config][2], places[config][3],
					places[config][4], places[config][5]);
		}
	}

	/**
	 * Initializes the chosen teleport
	 * 
	 * @param client
	 * @param frame
	 */
	public static void runTeleport(final Player client, int frame) {
		if (client.floor1() || client.floor2() || client.floor3())
			return;
		if (frame == 9194) {
			switch (client.teleportConfig) {
			case 0:
				client.getPlayerTeleportHandler().teleport(2975, 3919, 0);
				break;
			case 1:
				createTeleportMenu(client, 5, true);
				break;
			case 2:
				client.getPlayerTeleportHandler().teleport(3222, 3218, 0);
				break;
			case 3:
				createTeleportMenu(client, 25, true);
				break;
			case 5:
				createTeleportMenu(client, 23, true);
				break;
			case 6:
				createTeleportMenu(client, 7, true);
				break;
			case 7:
				client.getPlayerTeleportHandler().teleport(2973, 9500, 0);
				break;
			case 8:
				createTeleportMenu(client, 9, true);
				break;
			case 9:
				createTeleportMenu(client, 24, true);
				break;
			case 10:
				client.getPlayerTeleportHandler().teleport(2289, 3139, 0);
				break;
			case 11:
				createTeleportMenu(client, 12, true);
				break;
			case 18:
				client.getPlayerTeleportHandler().teleport(2489, 3175, 0);
				break;
			case 13:
				createTeleportMenu(client, 14, true);
				break;
			case 17:
				createTeleportMenu(client, 18, true);
				break;
			case 20:
				createTeleportMenu(client, 21, true);
				break;
			case 21:
				createTeleportMenu(client, 22, true);
				break;
			case 22:
				createTeleportMenu(client, 20, true);
				break;
			case 23:
				createTeleportMenu(client, 1, true);
				break;
			case 19:
				client.getPlayerTeleportHandler().teleport(2492, 10163, 0);
				break;
			default:
				client.getActionSender().sendWindowsRemoval();
				break;

			}
			return;
		}
		if (frame == 9190)
			switch (client.teleportConfig) {

			case 8:
				createTeleportMenu(client, 10, true);

				return;

			case 9:
				createTeleportMenu(client, 13, true);

				return;

			}
		if (frame == 9191)
			switch (client.teleportConfig) {

			case 8:
				createTeleportMenu(client, 11, true);
				return;
			case 9:
				createTeleportMenu(client, 15, true);
				return;
			}
		if (frame == 9192)
			switch (client.teleportConfig) {
			case 6:
			case 20:
				createTeleportMenu(client, 27, true);
				return;

			case 8:
				createTeleportMenu(client, 17, true);
				return;
			case 15:
				client.getActionSender().sendWindowsRemoval();
				return;
			case 12:
				client.getActionSender().sendWindowsRemoval();
				return;
			}
		if (frame == 9193)
			switch (client.teleportConfig) {
			case 8:
				createTeleportMenu(client, 19, true);
				return;
			case 9:
				createTeleportMenu(client, 16, true);
				return;

			case 12:
			case 15:
				// case 22:
			case 24:
				return;
			}
		for (final int[] teleports : teleportMenuLocation)
			if (teleports[0] == frame && teleports[1] == client.teleportConfig) {
				if (teleports[2] == 2649 && teleports[3] == 4569)
					client.getPlayerTeleportHandler().teleport(teleports[2],
							teleports[3], 1);
				else
					client.getPlayerTeleportHandler().teleport(teleports[2],
							teleports[3], 0);

				break;
			}
	}

	public static void sendTeleportNames(Player client) {
		for (int i = 0; i < teleportNameConfiguration.length; i++)
			client.getActionSender().sendString(names[i],
					teleportNameConfiguration[i]);
	}

}