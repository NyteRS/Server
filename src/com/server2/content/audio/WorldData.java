package com.server2.content.audio;

import com.server2.model.entity.player.Player;

/**
 * 
 * @author Faris
 * 
 */
public class WorldData {

	// 1 = Tut
	// 2 = CW SPawn Room
	// 3 = Barrows under
	// 4 = Barrows top
	// 5 = Wildy
	// 6 = Ardy
	// 7 = Edgy
	// 8 = Wiz Tower
	// 9 = PC
	// 10 = Al Kharid
	// 11 = Lumbridge
	// 12 = Falador
	// 13 = Fight Caves
	// 14 = Pirate House
	// 15 = Barbarian Village
	// 16 = The Abyss
	// 17 = Ape Atoll
	// 18 = Bandit Camp
	// 19 = Barbarian Agility
	// 20 = Bedabin Camp
	// 21 = Crash Island
	// 22 = Catherby
	// 23 = White Wolf Moutain
	// 24 = Burthrope
	// 25 = Camelot
	// 26 = Canfis
	// 27 = Random Event Maze
	// 28 = Crandor
	// 29 = Draynor
	// 30 = Duel Arena
	// 31 = Entrana
	// 32 = Gnome Village
	// 33 = Goblin Village
	// 34 = HAM Base
	// 35 = Karamja
	// 36 = Lost & Found
	// 37 = Miscellania
	// 38 = Morton
	// 39 = Phasmatys
	// 40 = Port Sarim
	// 41 = Rimmington
	// 42 = Seers Village
	// 43 = Shilo Village
	// 44 = Taverly
	// 45 = Gnome Stronghold
	// 46 = Tzhaar
	// 47 = Varrock
	// 48 = Yanille
	// 49 = Castle Wars Game Area
	// 50 = Castle Wars Lobby
	// 51 = Castle Wars Underground
	// 52 = King Black Dragon Lair
	// 53 = Kalphite Lair
	/**
	 * Sets area id by integer in MusicHandler
	 * 
	 * @param c
	 * @return
	 */
	public int getAreaID(Player player) {
		if (player.getPosition().getX() >= 2625
				&& player.getPosition().getX() <= 2687
				&& player.getPosition().getY() >= 4670
				&& player.getPosition().getY() <= 4735)
			return 1;
		if (player.getPosition().getX() >= 2368
				&& player.getPosition().getX() <= 2376
				&& player.getPosition().getY() >= 3127
				&& player.getPosition().getY() <= 3135
				&& player.getPosition().getZ() == 1
				|| player.getPosition().getX() >= 2423
				&& player.getPosition().getX() <= 2431
				&& player.getPosition().getY() >= 3072
				&& player.getPosition().getY() <= 3080
				&& player.getPosition().getZ() == 1)
			return 2;
		if (player.getPosition().getX() > 3520
				&& player.getPosition().getX() < 3598
				&& player.getPosition().getY() > 9653
				&& player.getPosition().getY() < 9750)
			return 3;
		if (player.getPosition().getX() >= 3542
				&& player.getPosition().getX() <= 3583
				&& player.getPosition().getY() >= 3265
				&& player.getPosition().getY() <= 3322)
			return 4;
		if (player.getPosition().getX() > 2941
				&& player.getPosition().getX() < 3392
				&& player.getPosition().getY() > 3518
				&& player.getPosition().getY() < 3966
				|| player.getPosition().getX() > 3343
				&& player.getPosition().getX() < 3384
				&& player.getPosition().getY() > 9619
				&& player.getPosition().getY() < 9660
				|| player.getPosition().getX() > 2941
				&& player.getPosition().getX() < 3392
				&& player.getPosition().getY() > 9918
				&& player.getPosition().getY() < 10366)
			return 5;
		if (player.getPosition().getX() > 2558
				&& player.getPosition().getX() < 2729
				&& player.getPosition().getY() > 3263
				&& player.getPosition().getY() < 3343)
			return 6;
		if (player.getPosition().getX() > 3084
				&& player.getPosition().getX() < 3111
				&& player.getPosition().getY() > 3483
				&& player.getPosition().getY() < 3509)
			return 7;
		if (player.getPosition().getX() > 2935
				&& player.getPosition().getX() < 3061
				&& player.getPosition().getY() > 3308
				&& player.getPosition().getY() < 3396)
			return 8;
		if (player.getPosition().getX() >= 2659
				&& player.getPosition().getX() <= 2664
				&& player.getPosition().getY() >= 2637
				&& player.getPosition().getY() <= 2644
				|| player.getPosition().getX() >= 2623
				&& player.getPosition().getX() <= 2690
				&& player.getPosition().getY() >= 2561
				&& player.getPosition().getY() <= 2688)
			return 9;
		if (player.getPosition().getX() > 3270
				&& player.getPosition().getX() < 3455
				&& player.getPosition().getY() > 2880
				&& player.getPosition().getY() < 3330)
			return 10;
		if (player.getPosition().getX() > 3187
				&& player.getPosition().getX() < 3253
				&& player.getPosition().getY() > 3189
				&& player.getPosition().getY() < 3263)
			return 11;
		if (player.getPosition().getX() > 3002
				&& player.getPosition().getX() < 3004
				&& player.getPosition().getY() > 3002
				&& player.getPosition().getY() < 3004)
			return 12;
		if (player.getPosition().getX() >= 2360
				&& player.getPosition().getX() <= 2445
				&& player.getPosition().getY() >= 5045
				&& player.getPosition().getY() <= 5125)
			return 13;
		if (player.getPosition().getX() >= 3038
				&& player.getPosition().getX() <= 3044
				&& player.getPosition().getY() >= 3949
				&& player.getPosition().getY() <= 3959)
			return 14;
		if (player.getPosition().getX() >= 3060
				&& player.getPosition().getX() <= 3099
				&& player.getPosition().getY() >= 3399
				&& player.getPosition().getY() <= 3450)
			return 15;
		if (player.getPosition().getX() >= 3008
				&& player.getPosition().getX() <= 3071
				&& player.getPosition().getY() >= 4800
				&& player.getPosition().getY() <= 4863)
			return 16;
		if (player.getPosition().getX() >= 2691
				&& player.getPosition().getX() <= 2826
				&& player.getPosition().getY() >= 2690
				&& player.getPosition().getY() <= 2831)
			return 17;
		if (player.getPosition().getX() >= 3155
				&& player.getPosition().getX() <= 3192
				&& player.getPosition().getY() >= 2962
				&& player.getPosition().getY() <= 2994)
			return 18;
		if (player.getPosition().getX() >= 2526
				&& player.getPosition().getX() <= 2556
				&& player.getPosition().getY() >= 3538
				&& player.getPosition().getY() <= 3575)
			return 19;
		if (player.getPosition().getX() >= 3165
				&& player.getPosition().getX() <= 3199
				&& player.getPosition().getY() >= 3022
				&& player.getPosition().getY() <= 3054)
			return 20;
		if (player.getPosition().getX() >= 2785
				&& player.getPosition().getX() <= 2804
				&& player.getPosition().getY() >= 3312
				&& player.getPosition().getY() <= 3327)
			return 21;
		if (player.getPosition().getX() >= 2792
				&& player.getPosition().getX() <= 2829
				&& player.getPosition().getY() >= 3412
				&& player.getPosition().getY() <= 3472
				|| player.getPosition().getX() > 2828
				&& player.getPosition().getX() < 2841
				&& player.getPosition().getY() > 3430
				&& player.getPosition().getY() < 3459
				|| player.getPosition().getX() > 2839
				&& player.getPosition().getX() < 2861
				&& player.getPosition().getY() > 3415
				&& player.getPosition().getY() < 3441)
			return 22;
		if (player.getPosition().getX() >= 2850
				&& player.getPosition().getX() <= 2879
				&& player.getPosition().getY() >= 3446
				&& player.getPosition().getY() <= 3522)
			return 23;
		if (player.getPosition().getX() >= 2878
				&& player.getPosition().getX() <= 2937
				&& player.getPosition().getY() >= 3524
				&& player.getPosition().getY() <= 3582)
			return 24;
		if (player.getPosition().getX() >= 2744
				&& player.getPosition().getX() <= 2787
				&& player.getPosition().getY() >= 3457
				&& player.getPosition().getY() <= 3519)
			return 25;
		if (player.getPosition().getX() >= 3425
				&& player.getPosition().getX() <= 3589
				&& player.getPosition().getY() >= 3256
				&& player.getPosition().getY() <= 3582)
			return 26;
		if (player.getPosition().getX() >= 2883
				&& player.getPosition().getX() <= 2942
				&& player.getPosition().getY() >= 4547
				&& player.getPosition().getY() <= 4605)
			return 27;
		if (player.getPosition().getX() >= 2819
				&& player.getPosition().getX() <= 2859
				&& player.getPosition().getY() >= 3224
				&& player.getPosition().getY() <= 3312)
			return 28;
		if (player.getPosition().getX() >= 3067
				&& player.getPosition().getX() <= 3134
				&& player.getPosition().getY() >= 3223
				&& player.getPosition().getY() <= 3297)
			return 29;
		if (player.getPosition().getX() >= 3324
				&& player.getPosition().getX() <= 3392
				&& player.getPosition().getY() >= 3196
				&& player.getPosition().getY() <= 3329)
			return 30;
		if (player.getPosition().getX() >= 2800
				&& player.getPosition().getX() <= 2869
				&& player.getPosition().getY() >= 3324
				&& player.getPosition().getY() <= 3391)
			return 31;
		if (player.getPosition().getX() >= 2492
				&& player.getPosition().getX() <= 2563
				&& player.getPosition().getY() >= 3132
				&& player.getPosition().getY() <= 3203)
			return 32;
		if (player.getPosition().getX() >= 2945
				&& player.getPosition().getX() <= 2968
				&& player.getPosition().getY() >= 3477
				&& player.getPosition().getY() <= 3519)
			return 33;
		if (player.getPosition().getX() >= 3136
				&& player.getPosition().getX() <= 3193
				&& player.getPosition().getY() >= 9601
				&& player.getPosition().getY() <= 9664)
			return 34;
		if (player.getPosition().getX() >= 2816
				&& player.getPosition().getX() <= 2958
				&& player.getPosition().getY() >= 3139
				&& player.getPosition().getY() <= 3175)
			return 35;
		if (player.getPosition().getX() >= 2334
				&& player.getPosition().getX() <= 2341
				&& player.getPosition().getY() >= 4743
				&& player.getPosition().getY() <= 4751)
			return 36;
		if (player.getPosition().getX() >= 2495
				&& player.getPosition().getX() <= 2625
				&& player.getPosition().getY() >= 3836
				&& player.getPosition().getY() <= 3905)
			return 37;
		if (player.getPosition().getX() >= 3465
				&& player.getPosition().getX() <= 3520
				&& player.getPosition().getY() >= 3266
				&& player.getPosition().getY() <= 3309)
			return 38;
		if (player.getPosition().getX() >= 3585
				&& player.getPosition().getX() <= 3705
				&& player.getPosition().getY() >= 3462
				&& player.getPosition().getY() <= 3539)
			return 39;
		if (player.getPosition().getX() >= 2985
				&& player.getPosition().getX() <= 3064
				&& player.getPosition().getY() >= 3164
				&& player.getPosition().getY() <= 3261)
			return 40;
		if (player.getPosition().getX() >= 2913
				&& player.getPosition().getX() <= 2989
				&& player.getPosition().getY() >= 3185
				&& player.getPosition().getY() <= 3267)
			return 41;
		if (player.getPosition().getX() >= 2639
				&& player.getPosition().getX() <= 2740
				&& player.getPosition().getY() >= 3391
				&& player.getPosition().getY() <= 3503)
			return 42;
		if (player.getPosition().getX() >= 2816
				&& player.getPosition().getX() <= 2879
				&& player.getPosition().getY() >= 2946
				&& player.getPosition().getY() <= 3007)
			return 43;
		if (player.getPosition().getX() >= 2874
				&& player.getPosition().getX() <= 2934
				&& player.getPosition().getY() >= 3390
				&& player.getPosition().getY() <= 3492)
			return 44;
		if (player.getPosition().getX() >= 2413
				&& player.getPosition().getX() <= 2491
				&& player.getPosition().getY() >= 3386
				&& player.getPosition().getY() <= 3515)
			return 45;
		if (player.getPosition().getX() >= 2431
				&& player.getPosition().getX() <= 2495
				&& player.getPosition().getY() >= 5117
				&& player.getPosition().getY() <= 5180)
			return 46;
		if (player.getPosition().getX() >= 3168
				&& player.getPosition().getX() <= 3291
				&& player.getPosition().getY() >= 3349
				&& player.getPosition().getY() <= 3514)
			return 47;
		if (player.getPosition().getX() >= 2532
				&& player.getPosition().getX() <= 2621
				&& player.getPosition().getY() >= 3071
				&& player.getPosition().getY() <= 3112)
			return 48;
		if (player.getPosition().getX() >= 2368
				&& player.getPosition().getX() <= 2430
				&& player.getPosition().getY() >= 3073
				&& player.getPosition().getY() <= 3135)
			return 49;
		if (player.getPosition().getX() >= 2440
				&& player.getPosition().getX() <= 2444
				&& player.getPosition().getY() >= 3083
				&& player.getPosition().getY() <= 3095)
			return 50;
		if (player.getPosition().getX() >= 2359
				&& player.getPosition().getX() <= 2440
				&& player.getPosition().getY() >= 9466
				&& player.getPosition().getY() <= 9543)
			return 51;
		if (player.getPosition().getX() >= 2251
				&& player.getPosition().getX() <= 2295
				&& player.getPosition().getY() >= 4675
				&& player.getPosition().getY() <= 4719)
			return 52;
		if (player.getPosition().getX() >= 3463
				&& player.getPosition().getX() <= 3515
				&& player.getPosition().getY() >= 9469
				&& player.getPosition().getY() <= 9524)
			return 53;
		if (player.getPosition().getX() >= 3200
				&& player.getPosition().getX() <= 3303
				&& player.getPosition().getY() >= 3273
				&& player.getPosition().getY() <= 3353)
			return 54;
		;
		if (player.getPosition().getX() >= 3274
				&& player.getPosition().getX() <= 3328
				&& player.getPosition().getY() >= 3315
				&& player.getPosition().getY() <= 3353)
			return 55;
		if (player.getPosition().getX() >= 3274
				&& player.getPosition().getX() <= 3266
				&& player.getPosition().getY() >= 3323
				&& player.getPosition().getY() <= 3327)
			return 56;
		if (player.getPosition().getX() >= 3274
				&& player.getPosition().getX() <= 3200
				&& player.getPosition().getY() >= 3323
				&& player.getPosition().getY() <= 3265)
			return 57;
		if (player.getPosition().getX() >= 3324
				&& player.getPosition().getX() <= 3263
				&& player.getPosition().getY() >= 3408
				&& player.getPosition().getY() <= 3285)
			return 58;
		if (player.getPosition().getX() >= 3324
				&& player.getPosition().getX() <= 3286
				&& player.getPosition().getY() >= 3408
				&& player.getPosition().getY() <= 3327)
			return 59;
		if (player.getPosition().getX() >= 3136
				&& player.getPosition().getX() <= 3136
				&& player.getPosition().getY() >= 3193
				&& player.getPosition().getY() <= 3199)
			return 60;
		if (player.getPosition().getX() >= 3121
				&& player.getPosition().getX() <= 3200
				&& player.getPosition().getY() >= 3199
				&& player.getPosition().getY() <= 3268)
			return 61;
		if (player.getPosition().getX() >= 3121
				&& player.getPosition().getX() <= 3269
				&& player.getPosition().getY() >= 3199
				&& player.getPosition().getY() <= 3314)
			return 62;
		if (player.getPosition().getX() >= 3066
				&& player.getPosition().getX() <= 3315
				&& player.getPosition().getY() >= 3147
				&& player.getPosition().getY() <= 3394)
			return 63;
		if (player.getPosition().getX() >= 3200
				&& player.getPosition().getX() <= 3354
				&& player.getPosition().getY() >= 3315
				&& player.getPosition().getY() <= 3394)
			return 64;
		if (player.getPosition().getX() >= 3248
				&& player.getPosition().getX() <= 3395
				&& player.getPosition().getY() >= 3328
				&& player.getPosition().getY() <= 3468)
			return 65;
		if (player.getPosition().getX() >= 3111
				&& player.getPosition().getX() <= 3469
				&& player.getPosition().getY() >= 3264
				&& player.getPosition().getY() <= 3524)
			return 66;
		if (player.getPosition().getX() >= 3265
				&& player.getPosition().getX() <= 3469
				&& player.getPosition().getY() >= 3328
				&& player.getPosition().getY() <= 3524)
			return 67;
		if (player.getPosition().getX() >= 3329
				&& player.getPosition().getX() <= 3447
				&& player.getPosition().getY() >= 3418
				&& player.getPosition().getY() <= 3524)
			return 68;
		if (player.getPosition().getX() >= 2889
				&& player.getPosition().getX() <= 3265
				&& player.getPosition().getY() >= 2940
				&& player.getPosition().getY() <= 3324)
			return 69;
		if (player.getPosition().getX() >= 3014
				&& player.getPosition().getX() <= 3261
				&& player.getPosition().getY() >= 3065
				&& player.getPosition().getY() <= 3324)
			return 70;
		if (player.getPosition().getX() >= 2880
				&& player.getPosition().getX() <= 3325
				&& player.getPosition().getY() >= 2935
				&& player.getPosition().getY() <= 3394)
			return 71;
		return 0;
	}
}
