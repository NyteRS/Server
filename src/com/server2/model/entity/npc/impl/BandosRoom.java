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
public class BandosRoom {

	/**
	 * Max hit for graardor.
	 */
	public static final int maxGraardor = 65, maxStrongstack = 16,
			maxSteelwill = 17, maxGrimspike = 21;
	/**
	 * Timer to delay the bandos'boss hits.
	 */
	private static final int maxTimer = 5;
	/**
	 * Current timer.
	 */
	private static int timer;

	/**
	 * Handles maximum timer for Strongstack.
	 */
	private static final int maxTimer2 = 5;

	/**
	 * Current timer for strongstack.
	 */
	private static int timer2 = 5;
	/**
	 * Handles maximum timer for Strongstack
	 */
	private static final int maxTimer3 = 5;

	/**
	 * Current timer for steelwill.
	 */

	private static int timer3 = 5;

	/**
	 * Handles maximum timer for Strongstack
	 */
	private static final int maxTimer4 = 5;

	/**
	 * Current timer for grimspike
	 */

	private static int timer4 = 5;

	/**
	 * Handles Graardor.
	 * 
	 * @param attacker
	 * @param target
	 */
	public static void handleGraardor(Entity attacker, Entity target) {
		if (!BossAgression.getInstance().bandosRoom(target.getAbsX(),
				target.getAbsY(), target.getHeightLevel()))
			return;
		if (target instanceof Player) {
			if (timer < maxTimer) {
				timer++;
				return;
			}
			timer = 0;
			int damage = 0;
			damage = Infliction.canHitWithMelee(attacker, target) ? Misc
					.random(maxGraardor) - 1 + 1 : 0;
			if (((Player) target).getPrayerHandler().clicked[35]
					|| ((Player) target).getPrayerHandler().clicked[14])
				damage = damage / 5;
			AnimationProcessor.addNewRequest(attacker, 7060, 0);
			HitExecutor
					.addNewHit(attacker, target, CombatType.MELEE, damage, 0);
			((NPC) attacker).updateRequired = true;
		}
	}

	/**
	 * A method to handle grimspike
	 */

	public static void handleGrimspike(Entity attacker, Entity target) {
		if (!BossAgression.getInstance().bandosRoom(target.getAbsX(),
				target.getAbsY(), target.getHeightLevel()))
			return;
		if (target instanceof Player) {
			if (timer4 < maxTimer4) {
				timer4++;
				return;
			}
			timer4 = 0;
			int damage = 0;
			damage = Infliction.canHitWithRanged(attacker, target) ? Misc
					.random(maxGrimspike) - 1 + 1 : 0;
			AnimationProcessor.addNewRequest(attacker, 7027, 0);
			MagicHandler.createProjectile(attacker, target, 50001, 78);
			if (((Player) target).getPrayerHandler().clicked[34]
					|| ((Player) target).getPrayerHandler().clicked[14])
				HitExecutor.addNewHit(attacker, target, CombatType.RANGE, 0, 0);
			else
				HitExecutor.addNewHit(attacker, target, CombatType.RANGE,
						damage, 0);
		}
	}

	/**
	 * A method to handle steelWill
	 */

	public static void handleSteelWill(Entity attacker, Entity target) {
		if (!BossAgression.getInstance().bandosRoom(target.getAbsX(),
				target.getAbsY(), target.getHeightLevel()))
			return;
		if (target instanceof Player) {
			if (timer3 < maxTimer3) {
				timer3++;
				return;
			}
			timer3 = 0;
			int damage = 0;
			damage = MagicFormula.canMagicAttack(attacker, target) ? Misc
					.random(maxSteelwill) - 1 + 1 : 0;
			AnimationProcessor.addNewRequest(attacker, 7027, 0);
			MagicHandler.createProjectile(attacker, target, 50000, 78);
			if (((Player) target).getPrayerHandler().clicked[13]
					|| ((Player) target).getPrayerHandler().clicked[33])
				HitExecutor.addNewHit(attacker, target, CombatType.MAGIC, 0, 0);
			else
				HitExecutor.addNewHit(attacker, target, CombatType.MAGIC,
						damage, 0);
		}
	}

	/**
	 * A method to handle the Strongstack
	 */
	public static void handleStrongstack(Entity attacker, Entity target) {
		if (!BossAgression.getInstance().bandosRoom(target.getAbsX(),
				target.getAbsY(), target.getHeightLevel()))
			return;
		if (target instanceof Player) {
			if (timer2 < maxTimer2) {
				timer2++;
				return;
			}
			timer2 = 0;
			int damage = 0;

			damage = Infliction.canHitWithMelee(attacker, target) ? Misc
					.random(maxStrongstack) - 1 + 1 : 0;
			if (((Player) target).getPrayerHandler().clicked[35]
					|| ((Player) target).getPrayerHandler().clicked[14])
				damage = 0;
			AnimationProcessor.addNewRequest(attacker, 7027, 0);
			HitExecutor
					.addNewHit(attacker, target, CombatType.MELEE, damage, 0);
		}
	}

}
