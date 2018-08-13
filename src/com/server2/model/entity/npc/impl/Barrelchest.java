package com.server2.model.entity.npc.impl;

import com.server2.model.combat.HitExecutor;
import com.server2.model.combat.additions.Infliction;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;

/**
 * 
 * @author Rene Roosen Handles the Barrelchest boss.
 */

public class Barrelchest {
	/**
	 * The animations it does.
	 */
	private static int[] animations = { 5894, 5895 };

	/**
	 * The current timer.
	 */
	private static int currentTimer = 5;

	/**
	 * The max hit
	 */
	private static final int maxHit = 70;
	/**
	 * The attack timer.
	 */
	private static final int attackTimer = 5;

	/**
	 * Handles the Barrelchest boss.
	 * 
	 * @param attacker
	 * @param target
	 */
	public static void handleBarrelchest(Entity attacker, Entity target) {
		if (currentTimer < attackTimer) {
			currentTimer++;
			return;
		}
		currentTimer = 0;
		if (target instanceof Player) {
			final int attack = Misc.random(1);
			int damage = Infliction.canHitWithMelee(attacker, target) ? Misc
					.random(maxHit) - 1 + 1 : 0;
			if (((Player) target).getPrayerHandler().clicked[35]
					|| ((Player) target).getPrayerHandler().clicked[14])
				damage = damage / 3;
			if (Misc.random(3) == 1) {
				((Player) target).getPrayerHandler().updatePrayer(30);
				((Player) target).getActionSender().sendMessage(
						"The barrelchest takes some of your prayer points!");
			}
			if (Misc.random(5) == 1)
				if (((Player) target).getPrayerHandler().getActivePrayers() > 0) {
					((Player) target).getPrayerHandler().resetAllPrayers();
					((Player) target).getActionSender().sendMessage(
							"The barrelchest hits off your prayers!");
				}
			if (attack == 0)
				AnimationProcessor.addNewRequest(attacker, animations[0], 0);
			else if (attack == 1)
				AnimationProcessor.addNewRequest(attacker, animations[1], 0);
			HitExecutor
					.addNewHit(attacker, target, CombatType.MELEE, damage, 1);
		}
	}
}
