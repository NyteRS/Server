package com.server2.model.combat.magic;

import com.server2.content.skills.prayer.PrayerHandler;
import com.server2.model.combat.additions.Infliction;
import com.server2.model.entity.Entity;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class MagicFormula {

	/**
	 * 
	 * @param attacker
	 * @param target
	 * @return
	 */
	public static boolean canMagicAttack(Entity attacker, Entity target) {
		if (attacker.isPlayer() && target.isPlayer()) {
			final Player player = (Player) attacker;
			final Player player2 = (Player) target;
			final double effectiveAttack = Infliction.getEffectiveLevel(player,
					PlayerConstants.MAGIC);
			final int attackBonus = player.getBonuses().bonus[3];
			final double effectiveDefence = Infliction.getMagicDefence(player2);
			final int defenseBonus = player2.getBonuses().bonus[8];
			return Infliction.hasHit(attacker, (int) effectiveAttack * 2,
					(int) (10 + attackBonus * 3.25), effectiveDefence * 2,
					defenseBonus * 3);
		}

		return Infliction
				.canAttack(
						attacker,
						attacker.mageAttack(),
						attacker.isPlayer() ? ((Player) attacker).getBonuses().bonus[8]
								: 0,
						target.mageDefense(),
						target.isPlayer() ? ((Player) target).getBonuses().bonus[8]
								: 0, 1.4D);
	}

	public static boolean canMagicAttack(Entity attacker, Entity target,
			boolean extra) {

		if (attacker.isPlayer() && target.isPlayer()) {
			final Player player = (Player) attacker;
			final Player player2 = (Player) target;
			final double effectiveAttack = Infliction.getEffectiveLevel(player,
					PlayerConstants.MAGIC) * 2;
			final int attackBonus = player.getBonuses().bonus[3] * 2;
			final double effectiveDefence = Infliction.getMagicDefence(player2);
			final int defenseBonus = player2.getBonuses().bonus[8];
			return Infliction.hasHit(attacker, (int) effectiveAttack * 2,
					(int) (10 + attackBonus * 3.25), effectiveDefence * 2,
					defenseBonus * 3);
		}

		return Infliction
				.canAttack(
						attacker,
						attacker.mageAttack(),
						attacker.isPlayer() ? ((Player) attacker).getBonuses().bonus[8]
								: 0,
						target.mageDefense(),
						target.isPlayer() ? ((Player) target).getBonuses().bonus[8]
								: 0, 1.4D);
	}

	/**
	 * The magic defence
	 * 
	 * @param player
	 * @return
	 */
	public static double getMagicDefence(Player player) {
		final double defence = Infliction.getEffectiveLevel(player,
				PlayerConstants.DEFENCE) * 0.3;
		final double magic = Infliction.getEffectiveLevel(player,
				PlayerConstants.MAGIC) * 0.7;
		return Math.floor(defence + magic);
	}

	/**
	 * Calculates the magic max hit
	 * 
	 * @param player
	 * @param attacked
	 * @param spellId
	 * @param shortFormula
	 * @return
	 */
	public static int maxMagicHit(Player player, Entity attacked, int spellId,
			boolean shortFormula) {
		if (player == null || attacked == null)
			return 0;
		if (spellId == 0)
			return 0;
		if (spellId == 12445)
			return 0;
		if (Magic.spell(spellId) == null)
			return 0;
		int damage = Magic.spell(spellId).getDamage();
		if (shortFormula) {
			if (attacked.isPlayer())
				if (((Player) attacked).getPrayerHandler().clicked[PrayerHandler.DEFLECT_MAGIC])
					damage *= .6;
			return damage;
		}
		float damageMultiplier = 1;
		final int realLevel = player.playerLevel[PlayerConstants.MAGIC];
		if (player.playerLevel[PlayerConstants.MAGIC] > realLevel)
			damageMultiplier += .03 * (player.playerLevel[PlayerConstants.MAGIC] - realLevel);
		switch (player.playerEquipment[PlayerConstants.AMULET]) {
		case 18335:
			damageMultiplier += .15;
			break;
		}
		switch (player.playerEquipment[PlayerConstants.WEAPON]) {
		case 18355: // chaotic Staff
			damageMultiplier += .20;
			break;
		case 4675: // Ancient Staff
		case 4710: // Ahrim's Staff
		case 4862: // Ahrim's Staff
		case 4864: // Ahrim's Staff
		case 4865: // Ahrim's Staff
		case 6914: // Master Wand
		case 8841: // Void Knight Mace
		case 13867: // Zuriel's Staff
			damageMultiplier += .10;
			break;
		case 15486: // Staff of Light
			damageMultiplier += .15;
			break;
		}
		damage *= damageMultiplier;
		if (attacked.isPlayer())
			if (((Player) attacked).protectingMagic())
				damage *= .6;
		return damage;
	}
}
