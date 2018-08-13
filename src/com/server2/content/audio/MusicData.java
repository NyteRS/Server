package com.server2.content.audio;

/**
 * 
 * @author Faris
 */
public class MusicData {

	WorldData a = new WorldData();

	protected final static int[] LEFTOVER_SONGS = { 469, 494, 96, 412, 683, 6,
			460, 209, 483, 453, 172, 208, 479, 114, 117, 9, 191, 374, 189, 164,
			60, 642, 293, 66, 320, 113, 74, 161, 12 };
	/**
	 * Array storing the Music objects which contain Song id, and boundary
	 * regions for each song area
	 */
	protected final static Music[] songs = {
			new Music(76, 3200, 3199, 3273, 3302), // Harmony
			new Music(2, 3200, 3303, 3273, 3353), // Autumn Voyage
			new Music(111, 3274, 3328, 3315, 3353), // Still Night
			new Music(123, 3274, 3266, 3323, 3327), // Arabian2
			new Music(36, 3274, 3200, 3323, 3265), // Arabian
			new Music(50, 3257, 3112, 3333, 3199), // Alkharid
			new Music(47, 3324, 3200, 3408, 3262), // Duel Arena
			new Music(122, 3324, 3263, 3408, 3285), // Shine
			new Music(541, 3324, 3286, 3408, 3327), // The Enchanter
			new Music(64, 3136, 3136, 3193, 3199), // Book of Spells
			new Music(3, 3066, 3200, 3120, 3272), // Unknown Land
			new Music(327, 3121, 3200, 3199, 3268), // Dream
			new Music(163, 3121, 3269, 3199, 3314), // Flute Salad
			new Music(151, 3066, 3273, 3120, 3314), // Start
			new Music(333, 3066, 3315, 3147, 3394), // Spooky
			new Music(116, 3148, 3315, 3199, 3394), // Greatness
			new Music(106, 3200, 3354, 3315, 3394), // Expanse
			new Music(157, 3248, 3395, 3328, 3468), // Medieval
			new Music(125, 3166, 3395, 3247, 3468), // Garden
			new Music(175, 3111, 3395, 3165, 3468), // Spirit
			new Music(177, 3111, 3469, 3264, 3524), // Adventure
			new Music(93, 3265, 3469, 3328, 3524), // Parade
			new Music(48, 3329, 3447, 3418, 3524), // Morytania
			new Music(35, 2993, 3186, 3065, 3260), // Sea Shanty 2
			new Music(107, 2889, 3265, 2940, 3324), // Miles Away
			new Music(127, 2941, 3261, 3013, 3324), // Nightfall
			new Music(49, 3014, 3261, 3065, 3324), // Wander
			new Music(186, 2880, 3325, 2935, 3394), // Arrival
			new Music(72, 2936, 3325, 3065, 3394), // Fanfare
			new Music(54, 2944, 3395, 3008, 3458), // Scape Soft
			new Music(54, 2944, 3459, 2987, 3474), // Scape Soft
			new Music(150, 3009, 3395, 3065, 3458), // Gnome Theme
			new Music(141, 3066, 3395, 3110, 3450), // Barbarianism
			new Music(23, 2937, 3475, 2987, 3524), // Goblin Village
			new Music(102, 2988, 3459, 3065, 3524), // Alone
			new Music(98, 3066, 3451, 3110, 3524), // Forever
			new Music(34, 2944, 3525, 2991, 3591), // Wonder
			new Music(96, 2992, 3525, 3034, 3555), // Witching
			new Music(96, 2992, 3556, 3124, 3605), // Inspiration
			new Music(182, 3035, 3525, 3124, 3555), // Dangerous
			new Music(169, 3125, 3525, 3264, 3579), // Crystal Sword
			new Music(121, 3265, 3563, 3392, 3619), // Forbidden
			new Music(113, 2944, 3592, 2991, 3655), // Lightness
			new Music(160, 2992, 3606, 3055, 3655), // Army of Darkness
			new Music(176, 3056, 3606, 3124, 3655), // Undercurrent
			new Music(10, 3125, 3581, 3205, 3655), // Moody
			new Music(179, 3206, 3580, 3264, 3655), // Underground
			new Music(183, 2944, 3656, 3003, 3722), // Troubled
			new Music(66, 3004, 3656, 3064, 3722), // Legion
			new Music(476, 3065, 3656, 3126, 3722), // Dead Can Dance
			new Music(43, 3127, 3656, 3197, 3714), // Wilderness3
			new Music(8, 3198, 3656, 3264, 3702), // Wildwood
			new Music(337, 3265, 3621, 3392, 3716), // Faithless
			new Music(435, 3048, 3723, 3126, 3799), // Wilderness
			new Music(68, 3127, 3715, 3197, 3758), // Cavern
			new Music(332, 3198, 3704, 3264, 3758), // Scape Wild
			new Music(182, 3265, 3717, 3392, 3842), // Dangerous
			new Music(37, 2944, 3800, 3003, 3903), // Deep Wildy
			new Music(331, 3212, 3843, 3392, 3903), // Scape Sad
			new Music(52, 2944, 3904, 3009, 3969), // Serene
			new Music(13, 3077, 3905, 3139, 3969), // Mage Arena
	};

	/**
	 * takes area IDs as switch, and returns the song ID for that area
	 * 
	 * @param area
	 * @return
	 */
	public int getSongID(int area) {
		switch (area) {
		case 1:
			return 62;
		case 2:
			return 318;
		case 3:
			return 381;
		case 4:
			return 380;
		case 5:
			return 96;
		case 6:
			return 99;
		case 7:
			return 98;
		case 8:
			return 3;
		case 9:
			return 587;
		case 10:
			return 50;
		case 11:
			return 76;
		case 12:
			return 72;
		case 13:
			return 473;
		case 14:
			return 602;
		case 15:
			return 141;
		case 16:
			return 234;
		case 17:
			return 172;
		case 18:
			return 263;
		case 19:
			return 66;
		case 20:
			return 248;
		case 21:
			return 48;
		case 22:
			return 119;
		case 23:
			return 87;
		case 24:
			return 149;
		case 25:
			return 104;
		case 26:
			return 61;
		case 27:
			return 138;
		case 28:
			return 170;
		case 29:
			return 151;
		case 30:
			return 47;
		case 31:
			return 179;
		case 32:
			return 150;
		case 33:
			return 23;
		case 34:
			return 156;
		case 35:
			return 114;
		case 36:
			return 412;
		case 37:
			return 57;
		case 38:
			return 286;
		case 39:
			return 775;
		case 40:
			return 35;
		case 41:
			return 12;
		case 42:
			return 7;
		case 43:
			return 90;
		case 44:
			return 18;
		case 45:
			return 23;
		case 46:
			return 469;
		case 47:
			return 125;
		case 48:
			return 185;
		case 49:
			return 314;
		case 50:
			return 318;
		case 51:
			return 318;
		case 52:
			return 28;
		case 53:
			return 309;
		case 54:
			return 2;
		case 55:
			return 111;
		case 56:
			return 123;
		case 57:
			return 36;
		case 58:
			return 122;
		case 59:
			return 541;
		case 60:
			return 64;
		case 61:
			return 327;
		case 62:
			return 163;
		case 63:
			return 333;
		case 64:
			return 116;
		case 65:
			return 157;
		case 66:
			return 177;
		case 67:
			return 93;
		case 68:
			return 48;
		case 69:
			return 107;
		case 70:
			return 49;
		case 71:
			return 186;
		default:
			return -1;
		}
	}
}