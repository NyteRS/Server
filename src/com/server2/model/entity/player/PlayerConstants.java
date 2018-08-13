package com.server2.model.entity.player;

import com.server2.Server;

/**
 * A collection of player constants
 * 
 * @author Ultimate
 */

public class PlayerConstants {
	// TODO: remove unused stuff and move used stuff
	public static final int SPAWN_X = 3087;
	public static final int SPAWN_Y = 3497;
	public static final int MAX_ITEMS = 23000;
	public static final int MAX_ITEM_AMOUNT = Integer.MAX_VALUE;
	public static final int WALKING_QUEUE_SIZE = 50;
	public static int[] smelt_bars = { 2349, 2351, 2355, 2353, 2357, 2359,
			2361, 2363 };
	public static int[] smelt_frame = { 2405, 2406, 2407, 2409, 2410, 2411,
			2412, 2413 };
	public static final int MAX_SKILLS = 25;
	public static int FLETCHING_DELAY = 4;
	public final static int SEX = 0;
	public final static int HAIR_COLOUR = 1;
	public final static int BODY_COLOUR = 2;
	public final static int LEG_COLOUR = 3;
	public final static int FEET_COLOUR = 4;
	public final static int SKIN_COLOUR = 5;

	public final static int HEAD = 6;
	public final static int BODY = 7;
	public final static int ARMS = 8;
	public final static int HANDS = 9;
	public final static int LEGS = 10;
	public final static int FEET = 11;
	public final static int BEARD = 12;

	public static final int HELM = 0;
	public static final int CAPE = 1;
	public static final int AMULET = 2;
	public static final int WEAPON = 3;
	public static final int CHEST = 4;
	public static final int SHIELD = 5;
	public static final int BOTTOMS = 7;
	public static final int GLOVES = 9;
	public static final int BOOTS = 10;
	public static final int RING = 12;
	public static final int ARROWS = 13;

	public static final int ATTACK = 0;
	public static final int DEFENCE = 1;
	public static final int STRENGTH = 2;
	public static final int HITPOINTS = 3;
	public static final int RANGE = 4;
	public static final int PRAYER = 5;
	public static final int MAGIC = 6;
	public static final int COOKING = 7;
	public static final int WOODCUTTING = 8;
	public static final int FLETCHING = 9;
	public static final int FISHING = 10;
	public static final int FIREMAKING = 11;
	public static final int CRAFTING = 12;
	public static final int SMITHING = 13;
	public static final int MINING = 14;
	public static final int HERBLORE = 15;
	public static final int AGILITY = 16;
	public static final int THIEVING = 17;
	public static final int SLAYER = 18;
	public static final int FARMING = 19;
	public static final int RUNECRAFTING = 20;
	public static final int SUMMONING = 21;
	public static final int CONSTRUCTION = 24;
	public static final int HUNTER = 22;
	public static final int DUNGEONEERING = 23;
	public static final int COMBAT_EXPERIENCE_MULTIPLIER = 3;
	public static final int SKILL_EXPERIENCE_MULTIPLIER = calculateExpMultiplier();

	public final static String[] SKILL_NAMES = { "Attack", "Defence",
			"Strength", "Hitpoints", "Range", "Prayer", "Magic", "Cooking",
			"Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting",
			"Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer",
			"Farming", "Runecrafting", "Summoning", "Hunter", "Dungeoneering",
			"Construction" };

	public static final int STAB_ATT = 0;

	public static final int SLASH_ATT = 1;
	public static final int CRUSH_ATT = 2;
	public static final int MAGIC_ATT = 3;
	public static final int RANGE_ATT = 4;
	public static final int STAB_DEF = 5;

	public static final int SLASH_DEF = 6;
	public static final int CRUSH_DEF = 7;
	public static final int MAGIC_DEF = 8;
	public static final int RANGE_DEF = 9;
	public static final int STR_BONUS = 10;

	public static final int PRAYER_BONUS = 11;
	public static final String BONUS_NAME[] = { "Stab", "Slash", "Crush",
			"Magic", "Range", "Stab", "Slash", "Crush", "Magic", "Range",
			"Strength", "Prayer" };

	public static final int POISON_HIT = 0;

	public static final int RECOIL_HIT = 1;
	public static final int COMBAT_HIT = 2;
	public static final int MAGIC_HIT = 3;
	public static final int[] CURSE_GLOW = { 610, 611, 612, 613, 614, 615, 616,
			617, 618, 619, 620, 621, 622, 623, 624, 625, 626, 627, 628, 629 };

	public static int[] LOGS = { 1511, 1521, 1519, 1517, 1515, 1513 };

	public static int[] UNSTRUNG_BOWS = { 50, 48, 54, 56, 58, 60, 62, 64, 66,
			68, 70, 72 };
	public static int[] STRUNG_BOWS = { 841, 839, 843, 845, 847, 849, 851, 853,
			855, 857, 859, 861 };
	public static int[] FLETCHING_LEVELS = { 5, 10, 25, 20, 40, 35, 55, 50, 70,
			65, 85, 80 };
	public static int[] EXPERIENCE = { 5, 10, 17, 25, 42, 33, 58, 50, 75, 67,
			92, 83 };
	public static int[] ARROWSX = { 882, 884, 886, 888, 890, 892 };
	public static int[] ARROW_HEADS = { 39, 40, 41, 42, 43, 44 };
	public static int[] ARROW_LEVELS = { 1, 15, 30, 45, 60, 75 };
	public static int[] ARROW_EXPERIENCE = { 15, 20, 22, 25, 30, 40 };
	public static int[] DARTS = { 806, 807, 808, 809, 810, 811 };
	public static int[] DART_TIPS = { 819, 820, 821, 822, 823, 824 };
	public static int[] DART_LEVELS = { 1, 22, 37, 52, 67, 81 };
	public static int[] DART_EXPERIENCE = { 12, 14, 16, 18, 20, 25 };
	public static int[] LEFT_ITEM = { 50, 54, 60, 64, 68, 72 };
	public static int[] RIGHT_ITEM = { 48, 56, 58, 62, 66, 70 };
	public static String[] LEFT_ITEM_NAME = { "Longbow(u)", "Oak Longbow(u)",
			"Willow Longbow(u)", "Maple Longbow(u)", "Yew Longbow(u)",
			"Magic Longbow(u)" };
	public static String[] RIGHT_ITEM_NAME = { "Shortbow(u)",
			"Oak Shortbow(u)", "Willow Shortbow(u)", "Maple Shortbow(u)",
			"Yew Shortbow(u)", "Magic Shortbow(u)" };
	public static final int FIREMAKING_MULTIPLIER = 75;

	public static int calculateExpMultiplier() {
		int exp = 15;
		exp *= Server.bonusExp;
		return exp;
	}

}