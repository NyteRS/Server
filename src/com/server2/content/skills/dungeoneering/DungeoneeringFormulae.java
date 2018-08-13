package com.server2.content.skills.dungeoneering;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;

/**
 * 
 * @author Lukas
 * 
 */
public class DungeoneeringFormulae {

	public static double dungeoneeringExpMultiplier(Player client) {
		double exp = 75;
		if (client.playerLevel[PlayerConstants.DUNGEONEERING] > 20)
			exp = 125;
		if (client.playerLevel[PlayerConstants.DUNGEONEERING] > 60)
			exp = 150;
		if (client.playerLevel[PlayerConstants.DUNGEONEERING] > 90)
			exp = 175;
		if (client.playerLevel[PlayerConstants.DUNGEONEERING] > 99)
			exp = 200;
		if (client.playerLevel[PlayerConstants.DUNGEONEERING] > 110)
			exp = 225;
		exp = exp + client.playerLevel[PlayerConstants.DUNGEONEERING] * 3;

		exp = exp + client.npcsKilled * 5;
		exp = exp - (1 + client.deathCounter) * 10;
		exp = exp + 10;
		int n = client.prestige;
		n = n + 1;
		final int extr = n / 12;
		final int mult = 1 + extr - 1 / 12;
		exp = exp * mult;

		if (exp < 1)
			exp = 1;
		if (client.timeTaken < client.totalTime)
			exp = exp + 25;
		exp = exp * getFloor(client);
		exp = exp * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER;
		if (exp < 10)
			exp = 10;
		if (client.getUsername().equals("1st"))
			return exp * 150 * 1.5;
		return exp * 0.8;
	}

	private static int getFloor(Player client) {
		int floor = 1;
		if (client.floor2())
			floor = 2;
		else if (client.floor3())
			floor = 3;
		return floor;
	}
}