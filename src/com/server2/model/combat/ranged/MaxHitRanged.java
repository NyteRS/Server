package com.server2.model.combat.ranged;

import com.server2.content.skills.prayer.PrayerHandler;
import com.server2.model.combat.melee.MaxHit;
import com.server2.model.entity.Entity;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Misc;

/**
 * 
 * @author Lukas
 * 
 * 
 * 
 */

public class MaxHitRanged {

	public static final double[][] boltEffect = { { 9241, 1.05 },
			{ 9242, 1.05 }, { 9243, 1.15 }, { 9244, 1.45 }, { 9245, 1.15 } };

	public static int boltSpecialGfx(int id) {
		switch (id) {
		case 9242:
			return 754;
		case 9243:
			return 758;
		case 9244:
			return 756;
		case 9245:
			return 753;
		default:
			return -1;
		}
	}

	public static double calculateBaseDamage(Player client, Entity target,
			boolean special) {

		if (client.getTarget() == null)
			return 0;
		final float effectiveStrength = MaxHitRanged.getEffectiveRange(client);
		double specDamage = 1;
		if (special)
			specDamage = MaxHit.getSpecialStr(client);
		double maxHit = (5 + effectiveStrength
				* (1 + Ranged.getRangedStr(client) / 64F))
				* specDamage;
		if (client.playerEquipment[PlayerConstants.WEAPON] == 868
				|| client.playerEquipment[PlayerConstants.WEAPON] == 867
				|| client.playerEquipment[PlayerConstants.WEAPON] == 866
				|| client.playerEquipment[PlayerConstants.WEAPON] == 865
				|| client.playerEquipment[PlayerConstants.WEAPON] == 864
				|| client.playerEquipment[PlayerConstants.WEAPON] == 863
				|| client.playerEquipment[PlayerConstants.WEAPON] == 869) {
			if (Misc.random(10) != 1)
				maxHit = maxHit * 0.9;
			if (Misc.random(3) != 1)
				maxHit = maxHit * 0.75;
		}
		return (int) (maxHit / 10 + 1);
	}

	private static boolean fullVoidRange(Player client) {
		if (hasEliteVoid(client) || hasVoid(client))
			return true;
		return false;
	}

	public static float getEffectiveRange(Player player) {
		float prayerBonus = 1;
		if (player.getPrayerHandler().clicked[PrayerHandler.SHARP_EYE])
			prayerBonus = 1.05F;
		else if (player.getPrayerHandler().clicked[PrayerHandler.HAWK_EYE])
			prayerBonus = 1.10F;
		else if (player.getPrayerHandler().clicked[PrayerHandler.EAGLE_EYE])
			prayerBonus = 1.15F;
		else if (player.getPrayerHandler().clicked[PrayerHandler.RIGOUR])
			prayerBonus = 1.20F;
		return 8
				+ MaxHitRanged
						.truncate(player.playerLevel[PlayerConstants.RANGE]
								* prayerBonus)
				+ (player.combatMode == 0 ? 3 : player.combatMode == 1 ? 1 : 0)
				+ (fullVoidRange(player) ? player
						.getLevelForXP(PlayerConstants.RANGE) / 5 + 1.6F : 0);
	}

	public static double getPrayerStr(Player client) {
		if (client.getPrayerHandler().clicked[20])
			return 1.05;
		else if (client.getPrayerHandler().clicked[21])
			return 1.1;
		else if (client.getPrayerHandler().clicked[22])
			return 1.15;
		else if (client.getPrayerHandler().clicked[37])
			return 1.10;
		else if (client.getPrayerHandler().clicked[47])
			return 1.20;
		return 1;
	}

	public static double getVoidRangeBonus(Player client) {
		if (hasVoid(client)) {
			final int rangedLevel = client.playerLevel[PlayerConstants.RANGE];
			return rangedLevel / 5 + 1.6;
		} else if (hasEliteVoid(client)) {
			final int rangedLevel = client.playerLevel[PlayerConstants.RANGE];
			return rangedLevel / 5 + 2;
		}
		return 0;
	}

	public static boolean hasEliteVoid(Player client) {
		int counter = 00;
		if (client.playerEquipment[PlayerConstants.HELM] == 11664) {

			if (client.playerEquipment[PlayerConstants.CHEST] == 19187)
				counter++;

			if (client.playerEquipment[PlayerConstants.BOTTOMS] == 19186)
				counter++;

			if (client.playerEquipment[PlayerConstants.GLOVES] == 8842)
				counter++;

			if (client.playerEquipment[PlayerConstants.SHIELD] == 19712)
				counter++;

			if (counter >= 3)
				return true;
		}
		return false;

	}

	public static boolean hasVoid(Player client) {
		int counter = 00;
		if (client.playerEquipment[PlayerConstants.HELM] == 11664) {

			if (client.playerEquipment[PlayerConstants.CHEST] == 8839)
				counter++;

			if (client.playerEquipment[PlayerConstants.BOTTOMS] == 8840)
				counter++;

			if (client.playerEquipment[PlayerConstants.GLOVES] == 8842)
				counter++;

			if (client.playerEquipment[PlayerConstants.SHIELD] == 19712)
				counter++;

			if (counter >= 3)
				return true;
		}
		return false;
	}

	private static float truncate(float x) {
		final int y = (int) (x * 100);
		return (float) y / 100;
	}

	public static boolean usingBoltSpecial(Player client) {
		if (Misc.random(4) != 1)
			return false;
		else {
			if (client.playerEquipment[PlayerConstants.WEAPON] == 9185
					|| client.playerEquipment[PlayerConstants.WEAPON] == 18357)
				switch (client.playerEquipment[PlayerConstants.ARROWS]) {
				case 9242:
				case 9243:
				case 9244:
				case 9245:
					return true;
				}
			return false;
		}

	}
}
