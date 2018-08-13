package com.server2.model.combat.additions;

import com.server2.model.combat.HitExecutor;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.player.Player;

public class Poison {

	public static int[] poisonWeapons = { 812, 813, 814, 815, 816, 817, 831,
			832, 833, 834, 835, 836, 870, 871, 872, 873, 874, 875, 876, 883,
			885, 887, 889, 891, 893, 1219, 1221, 1223, 1225, 1227, 1229, 1231,
			1233, 1235, 1251, 1253, 1255, 1257, 1259, 1261, 1263, 5616, 5617,
			5618, 5619, 5620, 5621, 5628, 5629, 5630, 5631, 5632, 5633, 5634,
			5642, 5643, 5644, 5645, 5646, 5647, 5654, 5655, 5656, 5657, 5658,
			5659, 5660, 5668, 5670, 5672, 5674, 5676, 5678, 5680, 5682, 5704,
			5706, 5708, 5710, 5712, 5714, 5716, 5622, 5623, 5624, 5625, 5626,
			5627, 5635, 5636, 5637, 5638, 5639, 5640, 5641, 5648, 5649, 5650,
			5651, 5652, 5653, 5661, 5662, 5663, 5664, 5665, 5666, 5667, 5686,
			5688, 5690, 5692, 5694, 5696, 5698, 5700, 5718, 5720, 5722, 5724,
			8878, 5726, 5728, 5730 };

	public static void applyPoisonDesease(Player client, int amount) {
		/*
		 * if(!client.overLoaded && !client.poisonProtected) {
		 * client.poisonDamage = amount; client.poisonDelay = 1 +
		 * Misc.random(20); client.poisonHits[0] = 0; client.poisonHits[1] = 0;
		 * client.poisonHits[2] = 0;
		 * client.getActionSender().sendMessage("You have been poisoned!"); }
		 */
	}

	public static boolean canPoison(int weapon) {
		for (final int poisonWeapon : poisonWeapons)
			if (poisonWeapon == weapon)
				return true;
		return false;
	}

	public static int getPoisonSeverity(int weapon) {
		for (int i = 0; i < poisonWeapons.length; i++)
			if (poisonWeapons[i] == weapon)
				return i < 41 ? 2 : i < 82 ? 4 : 6;
		return -1;
	}

	public static boolean isPoisoned(Player client) {
		return client.poisonDamage > 0;
	}

	public static void poison(Player client) {

		if (client.poisonDamage > 0 && client.poisonDelay == 0) {

			client.poisonDelay = 60;

			int count = 0;

			for (int ticks = 0; ticks < client.poisonHits.length; ticks++)
				if (client.poisonHits[ticks] < 1) {
					client.poisonHits[ticks] = 1;
					break;
				}
			for (final int poisonHit : client.poisonHits)
				if (poisonHit == 1)
					count++;
			HitExecutor.addNewHit(client, client, CombatType.POISON,
					client.poisonDamage, 0);
			if (count >= 3) {
				client.poisonDamage--;
				client.poisonHits[0] = 0;
				client.poisonHits[1] = 0;
				client.poisonHits[2] = 0;
			}
			if (client.poisonDamage < 1 && client.getPoisonDelay() > 0) {
				client.poisonDamage = 0;
				client.poisonDelay = 0;
				client.poisonHits[0] = 0;
				client.poisonHits[1] = 0;
				client.poisonHits[2] = 0;
				client.getActionSender()
						.sendMessage("The poison has worn off.");
			}
		}
	}
}
