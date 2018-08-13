package com.server2.model.entity.npc.impl;

import com.server2.model.combat.HitExecutor;
import com.server2.model.combat.additions.Infliction;
import com.server2.model.combat.magic.MagicFormula;
import com.server2.model.combat.magic.MagicHandler;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;

/**
 * 
 * @author Rene
 * 
 */
public class ZamorakRoom {

	/**
	 * Some final integers holding the maximum hits.
	 */

	public static final int krilMelee = 66, krilMage = 46;

	/**
	 * The timer for K'ril Tsutsaroth
	 */
	public static final int maxTime = 7;

	/**
	 * The current timer.
	 */
	public static int timer = 7;

	/**
	 * Holds the minion timer
	 */
	public static final int maxMinions = 5;

	/**
	 * Handles the current Zakl'n Gritch timer
	 */
	public static int zakTimer = 5;
	/**
	 * Handles the current timer for Tstanon.
	 */

	public static int tstanonTimer = 5;

	/**
	 * Handles the Balfrug Timer
	 */
	public static int balfrugTimer = 5;

	/**
	 * Handles Balfrug
	 */
	public static void handleBalfrug(Entity attacker, Entity target) {
		if (target instanceof Player) {
			if (balfrugTimer < maxMinions) {
				balfrugTimer++;
				return;
			}
			balfrugTimer = 0;
			int damage = 0;
			damage = MagicFormula.canMagicAttack(attacker, target) ? Misc
					.random(35) - 1 + 1 : 0;
			AnimationProcessor.addNewRequest(attacker, 6947, 0);
			MagicHandler.createProjectile(attacker, target, 1158, 78);
			if (target instanceof Player) {
				if (((Player) target).getPrayerHandler().clicked[33]
						|| ((Player) target).getPrayerHandler().clicked[12])
					damage = 0;
				HitExecutor.addNewHit(attacker, target, CombatType.MAGIC,
						damage, 4);
			}
		}

	}

	/**
	 * A method to handle K'ril Tsutsaroth
	 */
	public static void handleKril(Entity attacker, Entity target) {
		if (target instanceof Player) {
			if (timer < maxTime) {
				timer++;
				return;
			}
			timer = 0;
			if (target instanceof Player) {
				AnimationProcessor.addNewRequest(attacker, 6945, 0);
				int damage = 0;
				if (Misc.random(1) == 0) {
					damage = Infliction.canHitWithMelee(attacker, target) ? Misc
							.random(krilMelee) - 1 + 1 : 0;
					if (((Player) target).getPrayerHandler().clicked[35]
							|| ((Player) target).getPrayerHandler().clicked[14])
						damage = damage / 5;
					HitExecutor.addNewHit(attacker, target, CombatType.MELEE,
							damage, 0);
				} else {
					damage = MagicFormula.canMagicAttack(attacker, target) ? Misc
							.random(krilMage) - 1 + 1 : 0;
					if (((Player) target).getPrayerHandler().clicked[13]
							|| ((Player) target).getPrayerHandler().clicked[33])
						damage = damage / 5;
					HitExecutor.addNewHit(attacker, target, CombatType.MAGIC,
							damage, 2);
				}
				if (Misc.random(5) == 1)
					((Player) target).getPrayerHandler().updatePrayer(damage);

			} else {

			}
		}
		((NPC) attacker).updateRequired = true;
	}

	/**
	 * Handles the Tstanon Karlak
	 */

	public static void handleTstanon(Entity attacker, Entity target) {
		if (target instanceof Player) {
			if (tstanonTimer < maxMinions) {
				tstanonTimer++;
				return;
			}
			tstanonTimer = 0;
			int damage = 0;
			damage = Infliction.canHitWithMelee(attacker, target) ? Misc
					.random(21) - 1 + 1 : 0;
			AnimationProcessor.addNewRequest(attacker, 64, 0);
			if (target instanceof Player)
				if (((Player) target).getPrayerHandler().clicked[13]
						|| ((Player) target).getPrayerHandler().clicked[33])
					damage = 0;
			HitExecutor
					.addNewHit(attacker, target, CombatType.MELEE, damage, 0);
		}
	}

	/**
	 * Handles Zakl'n Gritch
	 */
	public static void handleZakl(Entity attacker, Entity target) {
		if (target instanceof Player) {
			if (zakTimer < maxMinions) {
				zakTimer++;
				return;
			}
			zakTimer = 0;
			int damage = 0;
			damage = Infliction.canHitWithRanged(attacker, target) ? Misc
					.random(21) - 1 + 1 : 0;
			if (target instanceof Player) {
				AnimationProcessor.addNewRequest(attacker, 64, 0);
				if (((Player) target).getPrayerHandler().clicked[34]
						|| ((Player) target).getPrayerHandler().clicked[13])
					damage = 0;
			}
			HitExecutor
					.addNewHit(attacker, target, CombatType.RANGE, damage, 2);
		}
	}

}
