package com.server2.model.combat.melee;

import com.server2.content.skills.Skill;
import com.server2.model.combat.additions.BarrowsEffects;
import com.server2.model.combat.additions.Infliction;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Misc;

/**
 * 
 * @author Killamess/ Lukas
 * 
 */

public class MaxHit {

	public static final double[][] special = { { 5698, 1.1 }, { 5680, 1.1 },
			{ 1231, 1.1 }, { 1215, 1.1 }, { 3204, 1.1 }, { 1305, 1.25 },
			{ 1434, 1.35 }, { 11694, 1.3 }, { 11696, 1.13 }, { 11698, 1.05 },
			{ 11700, 1.03 }, { 861, 1.1 }, { 13899, 1.3 }, { 13902, 1.4 },
			{ 14484, 1.00 }, { 10887, 1.25 }, { 19784, 1.0 }, { 15259, 1.1 },
			{ 1249, 1.1 }, { 1263, 1.1 }, { 6739, 1.1 }, { 13905, 1.1 },
			{ 11730, 1.1 }, { 11716, 1.1 }, { 859, 1.1 }, { 13879, 1.1 },
			{ 13880, 1.1 }, { 13881, 1.1 }, { 13882, 1.1 }, { 13883, 1.2 },
			{ 15486, 0 }, { 4587, 1.15 }, { 11235, 1.45 }, { 15241, 1.5 },
			{ 13879, 1.5 }, };

	public static final int[] obsidianWeapons = { 746, 747, 6523, 6525, 6526,
			6527, 6528 };

	public static double calculateBaseDamage(Player client, boolean special) {
		double specDamage = 1;
		if (special)
			specDamage = getSpecialStr(client);
		if (client.playerHasDharoks())
			specDamage = 1.8
					- client.hitpoints
					/ ((float) client
							.getLevelForXP(client.playerXP[Skill.HITPOINTS]) + 1);
		float effectiveStrength = Infliction.getEffectiveStrength(client);
		if (hasVoid(client))
			effectiveStrength *= 1.1F;
		else if (hasEliteVoid(client))
			effectiveStrength *= 1.15F;
		else if (client.playerEquipment[PlayerConstants.AMULET] == 11127)
			for (final int weapon : obsidianWeapons)
				if (client.playerEquipment[PlayerConstants.WEAPON] == weapon)
					effectiveStrength *= .2F;
		final double maxHit = (5 + effectiveStrength
				* (1 + client.getBonuses().bonus[PlayerConstants.STR_BONUS] / 64F))
				* specDamage;
		double damage = maxHit / 10;
		// damage=damage*dharoksMult(client);
		if (special) {
			if (client.playerEquipment[PlayerConstants.WEAPON] == 11694)
				if (Misc.random(5) != 1)
					damage = damage * 0.75;
			if (client.playerEquipment[PlayerConstants.WEAPON] == 1215
					|| client.playerEquipment[PlayerConstants.WEAPON] == 1231
					|| client.playerEquipment[PlayerConstants.WEAPON] == 5680
					|| client.playerEquipment[PlayerConstants.WEAPON] == 5698)
				if (Misc.random(5) != 1)
					damage = damage * 0.85;
		}
		return damage;
	}

	public static double dharoksMult(Player client) {
		if (!BarrowsEffects.fullDharoks(client))
			return 1.0;
		else {
			final double mult = 1;
			int lvl = client.getLevelForXP(client.playerXP[Skill.HITPOINTS]);
			int hp = client.playerLevel[Skill.HITPOINTS];
			if (hp > lvl)
				hp = lvl;
			final double i = 100 / lvl;
			lvl = (int) (lvl * i);
			hp = (int) (hp * i);
			// double percent = (hp/lvl)*100;
			// TODO: something, just clean this method up at the very least
			return mult;
		}

	}

	public static double getEffectiveStr(Player client) {
		int extraBonus = 0;
		if (client.getCombatLevelNoSummoning() < 20)
			extraBonus = 25;
		return client.playerLevel[PlayerConstants.STRENGTH]
				* getPrayerStr(client) + getStyleBonus(client) + extraBonus;
	}

	public static double getPrayerStr(Player client) {
		if (client.getPrayerHandler().clicked[1])
			return 1.05;
		else if (client.getPrayerHandler().clicked[4])
			return 1.1;
		else if (client.getPrayerHandler().clicked[10])
			return 1.15;
		else if (client.getPrayerHandler().clicked[18])
			return 1.18;
		else if (client.getPrayerHandler().clicked[19])
			return 1.23;
		else if (client.getPrayerHandler().clicked[45])
			return 1.23;
		return 1;
	}

	public static double getSpecialStr(Player client) {
		for (final double[] slot : special)
			if (client.playerEquipment[PlayerConstants.WEAPON] == slot[0])
				return slot[1];
		return 1;
	}

	public static int getStyleBonus(Player client) {
		return client.combatMode == 2 ? 3 : client.combatMode == 3 ? 1
				: client.combatMode == 4 ? 3 : 0;
	}

	public static boolean hasEliteVoid(Player client) {
		int counter = 00;
		if (client.playerEquipment[PlayerConstants.HELM] == 11665) {

			if (client.playerEquipment[PlayerConstants.CHEST] == 19787)
				counter++;

			if (client.playerEquipment[PlayerConstants.BOTTOMS] == 19786)
				counter++;

			if (client.playerEquipment[PlayerConstants.GLOVES] == 8842)
				counter++;

			if (counter >= 3)
				return true;
		}
		return false;

	}

	public static boolean hasObsidianEffect(Player client) {
		if (client.playerEquipment[PlayerConstants.AMULET] != 11128)
			return false;
		for (final int weapon : obsidianWeapons)
			if (client.playerEquipment[PlayerConstants.WEAPON] == weapon)
				return true;
		return false;
	}

	public static boolean hasVoid(Player client) {
		int counter = 00;
		if (client.playerEquipment[PlayerConstants.HELM] == 11665) {

			if (client.playerEquipment[PlayerConstants.CHEST] == 8839)
				counter++;

			if (client.playerEquipment[PlayerConstants.BOTTOMS] == 8840)
				counter++;

			if (client.playerEquipment[PlayerConstants.GLOVES] == 8842)
				counter++;

			if (client.playerEquipment[PlayerConstants.SHIELD] == 19711)
				counter++;

			if (counter >= 3)
				return true;
		}
		return false;

	}

	public static boolean isWearingSlayerHelmet(Player client) {
		if (client.playerEquipment[PlayerConstants.HELM] == 13263)
			return true;
		return false;

	}
}
