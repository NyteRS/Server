package com.server2.model.entity.npc.impl;

import com.server2.content.skills.prayer.PrayerHandler;
import com.server2.model.combat.HitExecutor;
import com.server2.model.combat.additions.Infliction;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class ApeAtollGuards {

	/**
	 * Max hit
	 */

	private static final int maxGuardHit = 22;

	/**
	 * Handles the ape atoll guards on the main floor
	 */
	public static void apeAtollGuards(Entity attacker, Entity target) {
		if (target instanceof Player && attacker instanceof NPC) {
			// Combat timer
			attacker.setCombatDelay(6);

			// Random healing
			if (((NPC) attacker).getHP() < ((NPC) attacker).maxHP)
				if (Misc.random(5) == 1) {
					AnimationProcessor.addNewRequest(attacker, 1405, 0);
					((Player) target).getActionSender().sendMessage(
							"Your monkey guard heals himself!");
					((NPC) attacker).heal(Misc.random(50));
					return;
				}

			// 1402 -attack 1403 - def 1404- death
			AnimationProcessor.addNewRequest(attacker, 1402, 0);
			int damage = Infliction.canHitWithMelee(attacker, target) ? Misc
					.random(maxGuardHit) - 1 + 1 : 0;

			if (((Player) target).getPrayerHandler().clicked[PrayerHandler.PROTECT_FROM_MELEE]
					|| ((Player) target).getPrayerHandler().clicked[PrayerHandler.DEFLECT_MELEE])
				damage = 0;
			// Adds the hit to the hit manager to be processed.
			HitExecutor
					.addNewHit(attacker, target, CombatType.MELEE, damage, 0);
		}
	}

}
