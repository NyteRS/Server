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
import com.server2.world.map.tile.TileManager;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class ArmouredZombie {

	/**
	 * Holds the max hit
	 */
	private static final int maxHit = 9;

	/**
	 * Handles an Armoured Zombie
	 */
	public static void armouredZombie(Entity attacker, Entity target) {

		if (target instanceof Player && attacker instanceof NPC) {
			// Combat timer
			attacker.setCombatDelay(6);

			int damage = 0;

			// Decides which type of armoured zombie we're dealing with, Melee
			// or Range?
			if (((NPC) attacker).getDefinition().getType() == 8150) {
				if (TileManager.calculateDistance(attacker, target) > 2)
					return;
				if (Infliction.canHitWithMelee(attacker, target))
					damage = Misc.random(maxHit);

				if (((Player) target).getPrayerHandler().clicked[PrayerHandler.DEFLECT_MELEE]
						|| ((Player) target).getPrayerHandler().clicked[PrayerHandler.PROTECT_FROM_MELEE])
					damage = 0;

				// Death - 5580 -Attack 5571 - Block 5574
				AnimationProcessor.addNewRequest(attacker, 5571, 0);

				HitExecutor.addNewHit(attacker, target, CombatType.MELEE,
						damage, 1);
			} else if (((NPC) attacker).getDefinition().getType() == 8152) {
				if (Infliction.canHitWithRanged(attacker, target))
					damage = Misc.random(maxHit);
				if (((Player) target).getPrayerHandler().clicked[PrayerHandler.DEFLECT_MISSILES]
						|| ((Player) target).getPrayerHandler().clicked[PrayerHandler.PROTECT_FROM_MISSILES])
					damage = 0;
				AnimationProcessor.addNewRequest(attacker, 5568, 0);
				HitExecutor.addNewHit(attacker, target, CombatType.RANGE,
						damage, 2);
			}
		}
	}
}
