package com.server2.model.combat.additions;

import com.server2.content.skills.Skill;
import com.server2.content.skills.prayer.PrayerHandler;
import com.server2.model.combat.CombatEngine;
import com.server2.model.combat.melee.MaxHit;
import com.server2.model.combat.ranged.MaxHitRanged;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.Location;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Misc;

/**
 * 
 * @author Lukas P / Rene Roosen
 * 
 */
public class Infliction {

	public static final int STAB_ATTACK_BONUS = 0;
	public static final int SLASH_ATTACK_BONUS = 1;
	public static final int CRUSH_ATTACK_BONUS = 2;
	public static final int MAGIC_ATTACK_BONUS = 3;
	public static final int RANGE_ATTACK_BONUS = 4;
	public static final int STAB_DEFENCE_BONUS = 5;
	public static final int SLASH_DEFENCE_BONUS = 6;
	public static final int CRUSH_DEFENCE_BONUS = 7;
	public static final int MAGIC_DEFENCE_BONUS = 8;
	public static final int RANGE_DEFENCE_BONUS = 9;

	public static int bestAttackBonus(Player client) {
		if (client.getBonuses().bonus[0] > client.getBonuses().bonus[1]
				&& client.getBonuses().bonus[0] > client.getBonuses().bonus[2])
			return 0;
		if (client.getBonuses().bonus[1] > client.getBonuses().bonus[0]
				&& client.getBonuses().bonus[1] > client.getBonuses().bonus[2])
			return 1;
		else
			return client.getBonuses().bonus[2] > client.getBonuses().bonus[1]
					&& client.getBonuses().bonus[2] > client.getBonuses().bonus[0] ? 2
					: 0;
	}

	public static int bestDefenceBonus(Player client) {
		if (client.getBonuses().bonus[5] > client.getBonuses().bonus[6]
				&& client.getBonuses().bonus[5] > client.getBonuses().bonus[7])
			return 5;
		if (client.getBonuses().bonus[6] > client.getBonuses().bonus[5]
				&& client.getBonuses().bonus[6] > client.getBonuses().bonus[7])
			return 6;
		else
			return client.getBonuses().bonus[7] > client.getBonuses().bonus[5]
					&& client.getBonuses().bonus[7] > client.getBonuses().bonus[6] ? 7
					: 5;
	}

	public static boolean canAttack(Entity attacker, int effectiveAttack,
			int attackBonus, int effectiveDefense, int defenseBonus,
			double modifier) {

		attackBonus *= 1.6;

		int attackerRoll = (int) (effectiveAttack * (attackBonus + 64F) / 10);
		final int defenseRoll = (int) (effectiveDefense * (defenseBonus + 64F) / 10);

		attackerRoll *= modifier;

		if (attackerRoll < defenseRoll) {
			final float b = (attackerRoll - 1F) / (2F * defenseRoll) * 100F;
			return Misc.random(100) <= b;
		}
		final float b = (1F - (defenseRoll + 1F) / (2F * attackerRoll)) * 100F;

		return Misc.random(100) <= b;
	}

	public static boolean canHitWithMelee(Entity attacker, Entity victim) {
		int defenseBonus;

		if (victim instanceof Player) {
			final Player client2 = (Player) victim;
			defenseBonus = client2.getBonuses().bonus[attacker instanceof Player ? 5 + ((Player) attacker)
					.bestMeleeAttack() : client2.bestMeleeDefense()];

		} else
			defenseBonus = 0;
		final double d = 1.0D;
		if (attacker instanceof Player && victim instanceof Player) {
			final Player Player = (Player) attacker;
			final Player Client2 = (Player) victim;
			final double effectiveAttack = Infliction.getEffectiveLevel(Player,
					PlayerConstants.ATTACK);
			final int attackBonus = Infliction.getAttackBonusForStyle(Player);
			final double effectiveDefence = Infliction.getEffectiveLevel(
					Client2, PlayerConstants.DEFENCE);
			final int defBonus = Infliction.getDefensiveBonusForStyle(Client2,
					Player.getFightStyle());
			return Infliction.hasHit(attacker, effectiveAttack + 1,
					(int) (attackBonus * 0.95) + 1, effectiveDefence, defBonus);
		}

		return Infliction.canAttack(
				attacker,
				attacker.meleeAttack() + 1,
				attacker instanceof Player ? (int) (((Player) attacker)
						.getBonuses().bonus[((Player) attacker)
						.bestMeleeAttack()] * 0.95) + 1 : 0, victim
						.meleeDefense(), defenseBonus, d);
	}

	public static boolean canHitWithMelee(Entity attacker, Entity victim,
			double acc) {
		int defenseBonus;

		if (victim instanceof Player) {
			final Player client2 = (Player) victim;
			defenseBonus = client2.getBonuses().bonus[attacker instanceof Player ? 5 + ((Player) attacker)
					.bestMeleeAttack() : client2.bestMeleeDefense()];

		} else
			defenseBonus = 0;
		final double d = 1.0D;
		if (attacker instanceof Player && victim instanceof Player) {
			final Player Player = (Player) attacker;
			final Player Client2 = (Player) victim;
			final double effectiveAttack = Infliction.getEffectiveLevel(Player,
					PlayerConstants.ATTACK);
			final int attackBonus = Infliction.getAttackBonusForStyle(Player);
			final double effectiveDefence = Infliction.getEffectiveLevel(
					Client2, PlayerConstants.DEFENCE);
			final int defBonus = Infliction.getDefensiveBonusForStyle(Client2,
					Player.getFightStyle());
			return Infliction.hasHit(attacker,
					effectiveAttack * acc * 0.95 + 1,
					(int) (attackBonus * acc) + 1, effectiveDefence, defBonus);
		}

		return Infliction.canAttack(
				attacker,
				(int) (attacker.meleeAttack() * acc * 1.1),
				attacker instanceof Player ? (int) (((Player) attacker)
						.getBonuses().bonus[((Player) attacker)
						.bestMeleeAttack()] * acc) : 0, victim.meleeDefense(),
				defenseBonus, d);
	}

	public static boolean canHitWithRanged(Entity attacker, Entity target) {
		double mult = 1.0;
		if (attacker.isPlayer()) {
			final Player client = (Player) attacker;
			if (client.playerEquipment[PlayerConstants.WEAPON] == 868
					|| client.playerEquipment[PlayerConstants.WEAPON] == 867
					|| client.playerEquipment[PlayerConstants.WEAPON] == 866
					|| client.playerEquipment[PlayerConstants.WEAPON] == 865
					|| client.playerEquipment[PlayerConstants.WEAPON] == 864
					|| client.playerEquipment[PlayerConstants.WEAPON] == 863
					|| client.playerEquipment[PlayerConstants.WEAPON] == 869)
				if (Misc.random(10) != 1)
					mult = 0.75;
		}

		if (attacker.isPlayer() && target.isPlayer()) {
			final Player player = (Player) attacker;
			final Player player2 = (Player) target;

			final double effectiveAttack = Infliction.getEffectiveLevel(player,
					PlayerConstants.RANGE);
			final int attackBonus = player.getBonuses().bonus[RANGE_ATTACK_BONUS];

			final double effectiveDefence = Infliction.getEffectiveLevel(
					player2, PlayerConstants.DEFENCE);
			final int defenseBonus = player2.getBonuses().bonus[RANGE_DEFENCE_BONUS];
			return Infliction.hasHit(attacker, effectiveAttack * mult,
					(int) (attackBonus * mult), effectiveDefence, defenseBonus);
		}

		return Infliction
				.canAttack(
						attacker,
						(int) (attacker.rangeAttack() * mult),
						attacker.isPlayer() ? (int) (((Player) attacker)
								.getBonuses().bonus[RANGE_ATTACK_BONUS] * mult)
								: 0,
						target.rangeDefense(),
						target.isPlayer() ? ((Player) target).getBonuses().bonus[RANGE_DEFENCE_BONUS]
								: 0, 1.0D);
	}

	public static boolean canHitWithRanged(Entity attacker, Entity target,
			double acc) {
		if (attacker.isPlayer() && target.isPlayer()) {
			final Player player = (Player) attacker;
			final Player player2 = (Player) target;
			final double effectiveAttack = Infliction.getEffectiveLevel(player,
					PlayerConstants.RANGE);
			final int attackBonus = player.getBonuses().bonus[RANGE_ATTACK_BONUS];

			final double effectiveDefence = Infliction.getEffectiveLevel(
					player2, PlayerConstants.DEFENCE);
			final int defenseBonus = player2.getBonuses().bonus[RANGE_DEFENCE_BONUS];
			return Infliction.hasHit(attacker, effectiveAttack * acc * 0.9,
					(int) (attackBonus * acc * 1.55), effectiveDefence * 1,
					(int) (defenseBonus * 1.23 * 1.5));
		}

		return Infliction
				.canAttack(
						attacker,
						(int) (attacker.rangeAttack() * acc),
						attacker.isPlayer() ? (int) (((Player) attacker)
								.getBonuses().bonus[RANGE_ATTACK_BONUS] * acc)
								: 0,
						target.rangeDefense(),
						target.isPlayer() ? ((Player) target).getBonuses().bonus[RANGE_DEFENCE_BONUS]
								: 0, 1.0D);
	}

	public static int getAttackBonusForStyle(Player player) {
		if (player.getFightStyle().equals("SLASH"))
			return player.getBonuses().bonus[SLASH_ATTACK_BONUS];
		else if (player.getFightStyle().equals("CRUSH"))
			return player.getBonuses().bonus[CRUSH_ATTACK_BONUS];
		else if (player.getFightStyle().equals("STAB"))
			return player.getBonuses().bonus[STAB_ATTACK_BONUS];
		return player.getBonuses().bonus[player.bestMeleeDefense()];
	}

	public static int getDefensiveBonusForStyle(Player attacked,
			String attackerStyle) {
		if (attackerStyle.equals("SLASH"))
			return attacked.getBonuses().bonus[SLASH_DEFENCE_BONUS];
		else if (attackerStyle.equals("CRUSH"))
			return attacked.getBonuses().bonus[CRUSH_DEFENCE_BONUS];
		else if (attackerStyle.equals("STAB"))
			return attacked.getBonuses().bonus[STAB_DEFENCE_BONUS];
		return attacked.getBonuses().bonus[attacked.bestMeleeDefense()];
	}

	public static double getEffectiveLevel(Player client, int skill) {
		double toReturn = 0.0;
		double skillModifier = client.playerLevel[skill];
		switch (skill) {
		case PlayerConstants.MAGIC:
			if (client.getPrayerHandler().clicked[PrayerHandler.MYSTIC_LORE])
				skillModifier = skillModifier + skillModifier * 0.05;
			else if (client.getPrayerHandler().clicked[PrayerHandler.MYSTIC_MIGHT])
				skillModifier = skillModifier + skillModifier * 0.1;
			else if (client.getPrayerHandler().clicked[PrayerHandler.MYSTIC_WILL])
				skillModifier = skillModifier + skillModifier * 0.15;
			else if (client.getPrayerHandler().clicked[PrayerHandler.AUGURY])
				skillModifier = skillModifier + skillModifier * 0.20;
			skillModifier += 8;
			if (hasVoid(client, 11663) || hasEliteVoid(client, 11663))
				skillModifier *= 1.3;
			toReturn = Math.floor(skillModifier);
			break;
		case PlayerConstants.RANGE:
			if (client.getPrayerHandler().clicked[PrayerHandler.SHARP_EYE])
				skillModifier = skillModifier + skillModifier * 0.05;
			else if (client.getPrayerHandler().clicked[PrayerHandler.HAWK_EYE])
				skillModifier = skillModifier + skillModifier * 0.10;
			else if (client.getPrayerHandler().clicked[PrayerHandler.EAGLE_EYE])
				skillModifier = skillModifier + skillModifier * 0.15;
			else if (client.getPrayerHandler().clicked[PrayerHandler.RIGOUR])
				skillModifier = skillModifier + skillModifier * 0.20;
			skillModifier += 8;
			skillModifier += MaxHit.getStyleBonus(client);
			if (hasVoid(client, 11664) || hasEliteVoid(client, 11664))
				skillModifier *= 1.1;
			toReturn = Math.floor(skillModifier);
			break;
		case PlayerConstants.ATTACK:
			if (client.getPrayerHandler().clicked[PrayerHandler.CLARITY_OF_THOUGHT])
				skillModifier = skillModifier + skillModifier * 0.05;
			else if (client.getPrayerHandler().clicked[PrayerHandler.IMPROVED_REFLEXES])
				skillModifier = skillModifier + skillModifier * 0.10;
			else if (client.getPrayerHandler().clicked[PrayerHandler.INCREDIBLE_REFLEXES])
				skillModifier = skillModifier + skillModifier * 0.15;
			else if (client.getPrayerHandler().clicked[PrayerHandler.TURMOIL])
				skillModifier = skillModifier + skillModifier * 0.30;
			else if (client.getPrayerHandler().clicked[18])
				skillModifier = skillModifier + skillModifier * 0.15;
			else if (client.getPrayerHandler().clicked[PrayerHandler.PIETY])
				skillModifier = skillModifier + skillModifier * 0.20;
			skillModifier += 8;
			skillModifier += MaxHit.getStyleBonus(client);
			if (hasVoid(client, 11665) || hasEliteVoid(client, 11665))
				skillModifier *= 1.1;
			toReturn = Math.floor(skillModifier);
			break;
		case PlayerConstants.DEFENCE:
			if (client.getPrayerHandler().clicked[PrayerHandler.THICK_SKIN])
				skillModifier = skillModifier + skillModifier * 0.05;
			else if (client.getPrayerHandler().clicked[PrayerHandler.ROCK_SKIN])
				skillModifier = skillModifier + skillModifier * 0.10;
			else if (client.getPrayerHandler().clicked[PrayerHandler.STEEL_SKIN])
				skillModifier = skillModifier + skillModifier * 0.15;
			else if (client.getPrayerHandler().clicked[PrayerHandler.TURMOIL])
				skillModifier = skillModifier + skillModifier * 0.29;
			else if (client.getPrayerHandler().clicked[18])
				skillModifier = skillModifier + skillModifier * 0.20;
			else if (client.getPrayerHandler().clicked[PrayerHandler.PIETY])
				skillModifier = skillModifier + skillModifier * 0.25;
			else if (client.getPrayerHandler().clicked[PrayerHandler.RIGOUR])
				skillModifier = skillModifier + skillModifier * 0.25;
			else if (client.getPrayerHandler().clicked[PrayerHandler.AUGURY])
				skillModifier = skillModifier + skillModifier * 0.25;
			skillModifier += 8;
			skillModifier += MaxHit.getStyleBonus(client);
			toReturn = Math.floor(skillModifier);
			break;
		}
		return toReturn;
	}

	public static int getEffectiveLevel(Player player, int skill, float prayer,
			int bonus, float voidBonus) {
		int effective = player.playerLevel[skill];
		effective *= prayer;
		effective += 8;
		effective += bonus;
		effective *= voidBonus;
		return effective;
	}

	public static int getEffectiveMagicDefense(Player player) {
		final int effective = player.playerLevel[6];
		return effective;
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
				+ truncate(player.playerLevel[PlayerConstants.RANGE]
						* prayerBonus)
				+ (player.combatMode == 0 ? 3 : player.combatMode == 1 ? 1 : 0)
				+ (MaxHitRanged.hasVoid(player) ? player
						.getLevelForXP(player.playerXP[PlayerConstants.RANGE]) / 5 + 1.6F
						: 0);
	}

	public static float getEffectiveStrength(Player player) {
		float prayerBonus = 1;
		if (player.getPrayerHandler().clicked[PrayerHandler.BURST_OF_STRENGTH])
			prayerBonus = 1.05F;
		else if (player.getPrayerHandler().clicked[PrayerHandler.SUPERHUMAN_STRENGTH])
			prayerBonus = 1.10F;
		else if (player.getPrayerHandler().clicked[PrayerHandler.ULTIMATE_STRENGTH])
			prayerBonus = 1.15F;
		else if (player.getPrayerHandler().clicked[PrayerHandler.CHIVALRY])
			prayerBonus = 1.18F;
		else if (player.getPrayerHandler().clicked[PrayerHandler.PIETY])
			prayerBonus = 1.23F;
		else if (player.getPrayerHandler().clicked[PrayerHandler.LEECH_STRENGTH])
			prayerBonus = 1.10F;
		else if (player.getPrayerHandler().clicked[PrayerHandler.TURMOIL])
			prayerBonus = 1.27F;
		return 8
				+ Infliction
						.truncate(player.playerLevel[PlayerConstants.STRENGTH]
								* prayerBonus)
				+ (player.combatMode == 2 ? 3 : 0);
	}

	public static double getMagicDefence(Player player) {
		final double defence = getEffectiveLevel(player,
				PlayerConstants.DEFENCE) * 0.3;
		final double magic = getEffectiveLevel(player, PlayerConstants.MAGIC) * 0.7;
		return Math.floor(defence + magic);
	}

	public static double getPrayerAttack(Player client) {
		if (client.getPrayerHandler().clicked[2])
			return 1.05;
		else if (client.getPrayerHandler().clicked[5])
			return 1.1;
		else if (client.getPrayerHandler().clicked[11])
			return 1.15;
		else if (client.getPrayerHandler().clicked[18])
			return 1.15;
		else if (client.getPrayerHandler().clicked[19])
			return 1.20;
		else if (client.getPrayerHandler().clicked[45])
			return 1.23;

		return 1;
	}

	public static double getPrayerDefence(Player client) {
		if (client.getPrayerHandler().clicked[0])
			return 1.05;
		else if (client.getPrayerHandler().clicked[3])
			return 1.1;
		else if (client.getPrayerHandler().clicked[9])
			return 1.15;
		else if (client.getPrayerHandler().clicked[18])
			return 1.20;
		else if (client.getPrayerHandler().clicked[19])
			return 1.25;
		else if (client.getPrayerHandler().clicked[45])
			return 1.23;
		return 1;
	}

	public static boolean hasEliteVoid(Player client, int i) {
		int counter = 00;
		if (client.playerEquipment[PlayerConstants.HELM] == i) {

			if (client.playerEquipment[PlayerConstants.CHEST] == 19187)
				counter++;

			if (client.playerEquipment[PlayerConstants.BOTTOMS] == 19186)
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

	public static boolean hasHit(Entity attacker, double attackerEffective,
			int attackerBonus, double defenderEffective, int defenderBonus) {

		double attackerRoll = attackerEffective * (1 + attackerBonus / 64);
		final double defenceRoll = defenderEffective * (1 + defenderBonus / 64);
		double modifier = 1;
		if (attacker.isPlayer()) {
			final Player player = (Player) attacker;
			if (player.playerHasDharoks())
				modifier = 3.5
						- player.playerLevel[PlayerConstants.HITPOINTS]
						/ ((float) player
								.getLevelForXP(player.playerXP[Skill.HITPOINTS]) + 1);
		}
		attackerRoll *= modifier;

		attackerRoll *= 1.08; // More accuracy
		double chance;
		if (attackerRoll < defenceRoll)
			chance = (attackerRoll - 1.0) / (2.0 * defenceRoll);
		else
			chance = 1.0 - (defenceRoll + 1.0) / (2.0 * attackerRoll);
		chance *= 100;
		chance = Math.ceil(chance);
		final double random = Misc.random(100);
		return random <= chance;
	}

	public static boolean hasVoid(Player client, int i) {
		int counter = 00;
		if (client.playerEquipment[PlayerConstants.HELM] == i) {

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

	/**
	 * Is the player in the diagonal block: formula.
	 * 
	 * @param attacked
	 * @param attacker
	 * @return
	 */
	public final static boolean isInDiagonalBlock(Player attacked,
			Player attacker) {
		return attacked.absX - 1 == attacker.absX
				&& attacked.absY + 1 == attacker.absY
				|| attacker.absX - 1 == attacked.absX
				&& attacker.absY + 1 == attacked.absY
				|| attacked.absX + 1 == attacker.absX
				&& attacked.absY - 1 == attacker.absY
				|| attacker.absX + 1 == attacked.absX
				&& attacker.absY - 1 == attacked.absY
				|| attacked.absX + 1 == attacker.absX
				&& attacked.absY + 1 == attacker.absY
				|| attacker.absX + 1 == attacked.absX
				&& attacker.absY + 1 == attacked.absY;
	}

	public static boolean properCombatType(CombatType type) {
		return type == CombatType.MELEE || type == CombatType.RANGE
				|| type == CombatType.MAGIC;
	}

	/**
	 * Stops the diagonal attack, if they're in that region.
	 * 
	 * @param attacked
	 * @param attacker
	 */
	public static void stopDiagonalAttack(Player attacked, Player attacker) {
		if (attacker.getFreezeDelay() > 0)
			CombatEngine.resetAttack(attacker, false);
		else {
			final int xMove = attacked.getAbsX() - attacker.getAbsX();
			int yMove = 0;
			if (xMove == 0)
				yMove = attacked.getAbsY() - attacker.getAbsY();
			int k = attacker.getAbsX() + xMove;
			k -= attacker.mapRegionX * 8;
			attacker.getWalkingQueue().reset();
			int l = attacker.getAbsY() + yMove;
			l -= attacker.mapRegionY * 8;
			attacker.getWalkingQueue().addStep(new Location(k, l), false);
		}
	}

	private static float truncate(float x) {
		final int y = (int) (x * 100);
		return (float) y / 100;
	}

}
