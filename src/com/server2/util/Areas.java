package com.server2.util;

import com.server2.content.minigames.FightPits;
import com.server2.model.entity.Location;
import com.server2.model.entity.player.Player;

//It errors due to the errors in the other classes :P just  start from top 

public class Areas {

	public static boolean Ardounge(Player client) {
		return client.getAbsX() > 2583 && client.getAbsX() < 2729
				&& client.getAbsY() > 3255 && client.getAbsY() < 3343;
	}

	public static boolean bossRoom1(Location pos) {
		return pos.getX() > 2851 && pos.getX() < 2870 && pos.getY() > 9730
				&& pos.getY() < 9743;
	}

	/**
	 * Checks player coordinates on login.
	 * 
	 * @param player
	 */
	public static void checkCoordinates(Player player) {
		player.setHeightLevel(0);
		if (isInDuelArenaFight(player.getCoordinates())) {
			player.teleportToX = 3368;
			player.teleportToY = 3267;
		} else if (isInCastleWarsLobby(player.getCoordinates())
				|| isInCastleWarsGame(player.getCoordinates())) {
			player.teleportToX = 2440;
			player.teleportToY = 3089;
		}
	}

	public static boolean Corp(Location pos) {
		return pos.getX() >= 2070 && pos.getX() <= 2110 && pos.getY() >= 4410
				&& pos.getY() <= 4440;
	}

	public static boolean doublePKPointArea(Player client) {
		if (client.getWildernessLevel() > 35)
			return true;
		return false;
	}

	public static boolean dwarfBase(Player client) {
		if (client.inBase())
			return true;
		return false;
	}

	public static boolean Falador(Player client) {
		return client.getAbsX() > 2941 && client.getAbsX() < 3060
				&& client.getAbsY() > 3314 && client.getAbsY() < 3399;
	}

	public static boolean ignoreClipping(Player client) {
		if (client.gwdCoords() || client.inWarriorG() || client.neitiznot())
			return true;
		return false;
	}

	public static boolean inFrostMulti(Location position) {
		return position.getX() > 2839 && position.getX() < 2868
				&& position.getY() > 3718 && position.getY() < 3745;
	}

	public static boolean inMiniGame(Player client) {
		if (client.getDuelOpponent() != null
				|| Areas.isInDuelArenaFight(client.getPosition()))
			return true;
		if (client.castleWarsTeam != null
				|| Areas.isInCastleWarsLobby(client.getPosition())
				|| client.inUndergroundOfCastleWars()
				|| Areas.isInCastleWarsGame(client.getPosition()))
			return true;
		if (FightPits.inFightPits(client) || FightPits.inFightArea(client))
			return true;
		if (client.floor1() || client.floor2() || client.floor3()
				|| bossRoom1(client.getPosition()))
			return true;
		if (client.inRFD())
			return true;
		if (client.doingHFTD())
			return true;
		if (client.inFightCaves())
			return true;
		if (FightPits.inWaitingRoom(client) || FightPits.inWaitingArea(client))
			return true;
		if (Areas.isInPestControl(client.getPosition())
				|| Areas.isInPestControlBoat(client.getPosition()))
			return true;
		return false;
	}

	public static boolean inTutorial(Location position) {
		return position.getY() < 4695 && position.getY() > 4675
				&& position.getX() < 2655 && position.getX() > 2630;
	}

	public static boolean isAntiPJ(Location pos) {
		return pos.getX() >= 2952 && pos.getX() <= 3002 && pos.getY() >= 3587
				&& pos.getY() <= 3619;
	}

	public static boolean isAOD(Location pos) {
		return pos.getX() > 2896 && pos.getX() < 2927 && pos.getY() > 3596
				&& pos.getY() < 3629;
	}

	public static boolean isApeAtollGuard(Location pos) {
		return pos.getX() > 2783 && pos.getX() < 2811 && pos.getY() > 2770
				&& pos.getY() < 2800;

	}

	public static boolean isArmouredCave(Location pos) {
		return pos.getX() > 3077 && pos.getX() < 3128 && pos.getY() > 9644
				&& pos.getY() < 9677;
	}

	public static boolean isAtDZone(Location pos) {
		return pos.getX() >= 2368 && pos.getX() <= 2384 && pos.getY() >= 3118
				&& pos.getY() <= 3135;
	}

	public static boolean isAtSaradominBase(Location pos) {
		return pos.getX() >= 2414 && pos.getX() <= 2431 && pos.getY() >= 3072
				&& pos.getY() <= 3088;
	}

	public static boolean isAtZamorakBase(Location pos) {
		return pos.getX() >= 2368 && pos.getX() <= 2384 && pos.getY() >= 3118
				&& pos.getY() <= 3135;
	}

	public static boolean isEdgeville(Location pos) {
		return pos.getX() > 3070 && pos.getX() < 3135 && pos.getY() > 3420
				&& pos.getY() < 3550;
	}

	public static boolean isGlacors(Location pos) {
		return pos.getX() > 2829 && pos.getX() < 2871 && pos.getY() > 3794
				&& pos.getY() < 3841;

	}

	public static boolean isInBrimhavenDung(Location pos) {
		return pos.getX() > 2620 && pos.getX() < 2780 && pos.getY() > 9400
				&& pos.getY() < 9600;
	}

	public static boolean isInCastleWarsGame(Location pos) {
		return pos.getX() >= 2366 && pos.getX() <= 2431 && pos.getY() >= 3071
				&& pos.getY() <= 3137 || pos.getX() >= 2354
				&& pos.getX() <= 2410 && pos.getY() >= 9497
				&& pos.getY() <= 9545 || pos.getX() >= 2395
				&& pos.getX() <= 2435 && pos.getY() >= 9474
				&& pos.getY() <= 9511;
	}

	public static boolean isInCastleWarsLobby(Location pos) {
		return isInZamorakLobby(pos) || isInSaradominLobby(pos);
	}

	public static boolean isInCastleWarsSafeZone(Location pos) {
		return (pos.getX() >= 2423 && pos.getX() <= 2431 && pos.getY() >= 3072
				&& pos.getY() <= 3080 || pos.getX() >= 2368
				&& pos.getX() <= 2376 && pos.getY() >= 3127
				&& pos.getY() <= 3135)
				&& pos.getZ() == 1;
	}

	public static boolean isInDuelArena(Location pos) {
		if (pos.getX() >= 3313 && pos.getY() <= 3247 && pos.getX() <= 3328
				&& pos.getY() >= 3223)
			return true;
		if (pos.getX() >= 3324 && pos.getY() <= 3222 && pos.getX() <= 3328
				&& pos.getY() >= 3199)
			return true;
		if (pos.getX() >= 3322 && pos.getY() <= 3264 && pos.getX() <= 3328
				&& pos.getY() >= 3247)
			return true;
		if (pos.getX() >= 3325 && pos.getY() <= 3270 && pos.getX() <= 3349
				&& pos.getY() >= 3262)
			return true;
		if (pos.getX() >= 3347 && pos.getY() <= 3280 && pos.getX() <= 3380
				&& pos.getY() >= 3262)
			return true;
		if (pos.getX() >= 3385 && pos.getY() <= 3266 && pos.getX() <= 3392
				&& pos.getY() >= 3260)
			return true;
		if (pos.getX() >= 3325 && pos.getY() <= 3263 && pos.getX() <= 3395
				&& pos.getY() >= 3200)
			return true;
		return false;
	}

	public static boolean isInDuelArenaFight(Location pos) {
		return pos.getX() > 3331 && pos.getX() < 3391 && pos.getY() > 3242
				&& pos.getY() < 3260;
	}

	public static boolean isInDZone(Player client) {
		if (client.isInDZone())
			return true;
		return false;
	}

	public static boolean isInEdge(Location pos) {
		return pos.getX() > 3079 && pos.getX() < 3120 && pos.getY() > 3460
				&& pos.getY() < 3522;
	}

	public static boolean isInFremDung(Location pos) {
		return pos.getX() > 2670 && pos.getX() < 2820 && pos.getY() > 9080
				&& pos.getY() < 10040;
	}

	public static boolean isInJail(Player client) {
		if (client.isInJail())
			return true;
		return false;
	}

	public static boolean isInMysteriousDung(Location pos) {
		return pos.getX() > 3250 && pos.getX() < 3330 && pos.getY() > 9790
				&& pos.getY() < 9860;
	}

	public static boolean isInPestControl(Location pos) {
		return pos.getX() >= 2624 && pos.getX() <= 2690 && pos.getY() >= 2550
				&& pos.getY() <= 2619;
	}

	public static boolean isInPestControlBoat(Location pos) {
		return pos.getX() >= 2659 && pos.getX() <= 2664 && pos.getY() >= 2637
				&& pos.getY() <= 2644;
	}

	public static boolean isInRiotLobby(Player client) {
		if (client.inRiotWarsLobby())
			return true;
		return false;
	}

	public static boolean isInRiotWar(Player client) {
		if (client.inRiotWars())
			return true;
		return false;
	}

	public static boolean isInSaradominLobby(Location pos) {
		return pos.getX() >= 2368 && pos.getX() <= 2392 && pos.getY() >= 9479
				&& pos.getY() <= 9498;
	}

	public static boolean isInSlayerTower(Location pos) {
		return pos.getX() > 3400 && pos.getX() < 3456 && pos.getY() > 3526
				&& pos.getY() < 3580;
	}

	public static boolean isInTaverlyDung(Location pos) {
		return pos.getX() > 2800 && pos.getX() < 2950 && pos.getY() > 9700
				&& pos.getY() < 9900;
	}

	public static boolean isInZamorakLobby(Location pos) {
		return pos.getX() >= 2409 && pos.getX() <= 2431 && pos.getY() >= 9511
				&& pos.getY() <= 9535;
	}

	public static boolean isMonkey(Location pos) {
		return pos.getX() > 2685 && pos.getX() < 2815 && pos.getY() > 9090
				&& pos.getY() < 9155;

	}

	public static boolean isRevenants(Location pos) {
		return pos.getX() >= 2615 && pos.getX() <= 2760 && pos.getY() >= 5050
				&& pos.getY() <= 5125;

	}

	public static boolean isRockLobster(Location pos) {
		return pos.getX() > 2492 && pos.getX() < 2550 && pos.getY() > 10100
				&& pos.getY() < 10185;
	}

	public static boolean isWaterCave(Location pos) {
		return pos.getX() > 2950 && pos.getX() < 3002 && pos.getY() > 9475
				&& pos.getY() < 9550;
	}

	// go to wilderness class

	public static boolean Lumbridge(Player client) {
		return client.getAbsX() > 3206 && client.getAbsX() < 3221
				&& client.getAbsY() > 3220 && client.getAbsY() < 3340;
	}

	public static boolean Torms(Player client) {
		return client.getAbsX() > 2200 && client.getAbsX() < 2400
				&& client.getAbsY() > 9799 && client.getAbsY() < 9960;
	}

	public static boolean Varrock(Player client) {
		return client.getAbsX() > 3169 && client.getAbsX() < 3327
				&& client.getAbsY() > 3372 && client.getAbsY() < 3517;
	}

}