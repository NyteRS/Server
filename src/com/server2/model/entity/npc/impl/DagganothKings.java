package com.server2.model.entity.npc.impl;

import com.server2.model.combat.HitExecutor;
import com.server2.model.combat.additions.Infliction;
import com.server2.model.combat.magic.MagicFormula;
import com.server2.model.combat.magic.MagicHandler;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.npc.NPCAttacks;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class DagganothKings {

	public static final int maxRange = 30, maxMelee = 28, maxMage = 61;

	public static final int mageTime = 6, rangeTime = 6, meleeTime = 6;
	private static int mageTimer = 6, rangeTimer = 6, meleeTimer = 6;

	public static void handlePrime(Entity attacker, Entity target) {
		if (target instanceof Player) {
			if (mageTimer < mageTime) {
				mageTimer++;
				return;
			}

			AnimationProcessor.addNewRequest(attacker, 2854, 0);
			MagicHandler.createProjectile(attacker, target, 1154, 78);
			mageTimer = 0;
			int damage = 0;
			NPCAttacks.npcArray[2882][0] = 3;
			damage = MagicFormula.canMagicAttack(attacker, target) ? Misc
					.random(maxMage) - 1 + 1 : 0;
			if (((Player) target).getPrayerHandler().clicked[33]
					|| ((Player) target).getPrayerHandler().clicked[12])
				HitExecutor.addNewHit(attacker, target, CombatType.MAGIC, 0, 2);
			else
				HitExecutor.addNewHit(attacker, target, CombatType.MAGIC,
						damage, 2);
		}
	}

	public static void handleRex(Entity attacker, Entity target) {
		if (target instanceof Player) {
			if (meleeTimer < meleeTime) {
				meleeTimer++;
				return;
			}
			AnimationProcessor.addNewRequest(attacker, 2853, 0);
			meleeTimer = 0;
			int damage = 0;
			NPCAttacks.npcArray[2883][0] = 1;
			damage = Infliction.canHitWithMelee(attacker, target) ? Misc
					.random(maxMelee) - 1 + 1 : 0;
			if (((Player) target).getPrayerHandler().clicked[14]
					|| ((Player) target).getPrayerHandler().clicked[35])
				HitExecutor.addNewHit(attacker, target, CombatType.MELEE, 0, 0);
			else
				HitExecutor.addNewHit(attacker, target, CombatType.MELEE,
						damage, 0);
		}
	}

	public static void handleSurpreme(Entity attacker, Entity target) {
		if (target instanceof Player) {
			if (rangeTimer < rangeTime) {
				rangeTimer++;
				return;
			}
			AnimationProcessor.addNewRequest(attacker, 2852, 0);
			rangeTimer = 0;
			int damage = 0;
			NPCAttacks.npcArray[2881][0] = 2;
			damage = Infliction.canHitWithRanged(attacker, target) ? Misc
					.random(maxRange) - 1 + 1 : 0;
			MagicHandler.createProjectile(attacker, target, 40008, 78);
			if (((Player) target).getPrayerHandler().clicked[13]
					|| ((Player) target).getPrayerHandler().clicked[34])
				HitExecutor.addNewHit(attacker, target, CombatType.RANGE, 0, 2);
			else
				HitExecutor.addNewHit(attacker, target, CombatType.RANGE,
						damage, 2);
		}
	}
}
