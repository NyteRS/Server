package com.server2.content.minigames;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.server2.Constants;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Paymon
 * 
 */

public class SpiritsOfWar {

	public int nextWave = 10;
	public int numberOfWaves = 100;
	public int spiritsKilled = 0, spiritsToKill = 0;

	/*
	 * private final int Spiritual_Warrior1 = 6219, Spiritual_Warrior2 = 6255,
	 * Spiritual_Warrior3 = 6229, Spiritual_Warrior4 = 6277, Spiritual_Ranger1 =
	 * 6220, Spiritual_Ranger2 = 6276, Spiritual_Ranger3 = 6256,
	 * Spiritual_Ranger4 = 6230, Spiritual_Mage1 = 6221, Spiritual_Mage2 = 6231,
	 * Spiritual_Mage3 = 6257, Spiritual_Mage4 = 6278;
	 */

	public final int[] spirits = { 6219, 6255, 6229, 6277, 6220, 6276, 6256,
			6230, 6221, 6231, 6257, 6278 };

	private final int[][] coordinates = { { 3175, 9755 }, { 3175, 9761 },
			{ 3173, 9763 }, { 3163, 9763 }, { 3160, 9761 }, { 3160, 9755 },
			{ 3163, 9753 }, { 3173, 9753 } };

	private static List<String> playersInRoom = new ArrayList<String>();

	public static void add(Player c) {
		if (c == null)
			return;
		final int i = playersInRoom.size();
		if (i >= 4)
			return;
		if (playersInRoom.contains(c.getUsername()))
			return;
		playersInRoom.add(c.getUsername());

	}

	public static void remove(Player c) {

		if (c == null)
			return;
		if (playersInRoom.contains(c.getUsername()))
			playersInRoom.remove(c.getUsername());
	}

	public void spawnNextRound(Player c) {
		final int numberOfSpirits = c.waveCount * 100;
		if (c != null) {
			if (c.waveCount >= numberOfWaves || c.waveCount < 0) {
				c.waveCount = 0;
				return;
			}
			if (!Constants.MINIGAME)
				return;

			final Random rand = new Random();
			for (int i = 0; i < numberOfSpirits; i++) { // spawns the spirits
				final int n = rand.nextInt(10);
				final int s = rand.nextInt(12);
				NPC.newNPCWithTempOwner(spirits[s], coordinates[n][0],
						coordinates[n][1], c.getIndex() * 4, c);

			}
			c.spiritsToKill = numberOfSpirits;
			c.spiritsKilled = 0;

		}

	}
}
