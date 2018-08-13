package com.server2.model.entity.npc.impl;

import com.server2.model.combat.HitExecutor;
import com.server2.model.combat.additions.Hits;
import com.server2.model.combat.additions.Infliction;
import com.server2.model.combat.magic.MagicHandler;
import com.server2.model.combat.magic.Multi;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.npc.NPCAttacks;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;

/**
 * 
 * @author Lukas P
 * 
 */
public class ArmadylRoom {

	/**
	 * Final integer which handles the Armadyl Boss timer
	 */

	public static final int attackSpeed = 3;
	public static final int emote = 6976;
	public static final int wingmanMax = 25;
	public static final int geerinMax = 25;
	public static final int kilisaMax = 20;

	public static final int rangeMax = 72;
	public static final int magicMax = 21;

	/**
	 * Integer which handles the current attack timer
	 */
	private static int timer = 3;

	private static final int maxTimer2 = 5;

	private static int timer2 = 5;
	private static final int maxTimer3 = 5;

	private static int timer3 = 5;

	private static final int maxTimer4 = 5;
	private static int timer4 = 5;

	public static void handleGeerin(Entity attacker, Entity target) {
		if (target instanceof Player) {
			if (timer4 < maxTimer4) {
				timer4++;
				return;
			}
			timer4 = 0;
			int damage = 0;
			damage = Infliction.canHitWithRanged(attacker, target) ? Misc
					.random(geerinMax) - 1 + 1 : 0;
			if (target instanceof Player)
				if (((Player) target).getPrayerHandler().clicked[34]
						|| ((Player) target).getPrayerHandler().clicked[13])
					damage = 0;
			AnimationProcessor.addNewRequest(attacker, 6953, 0);
			MagicHandler.createProjectile(attacker, target, 50007, 78);
			HitExecutor
					.addNewHit(attacker, target, CombatType.RANGE, damage, 0);
		}
	}

	public static void handleKilisa(Entity attacker, Entity target) {
		if (target instanceof Player) {
			if (timer2 < maxTimer2) {
				timer2++;
				return;
			}
			timer2 = 0;
			int damage = 0;
			damage = Infliction.canHitWithMelee(attacker, target) ? Misc
					.random(kilisaMax) - 1 + 1 : 0;
			AnimationProcessor.addNewRequest(attacker, 6954, 0);
			HitExecutor
					.addNewHit(attacker, target, CombatType.MELEE, damage, 0);
		}

	}

	/**
	 * Method which handles
	 */
	public static void handleKree(Entity attacker, Entity target) {
		if (target instanceof Player) {
			if (timer < attackSpeed) {
				timer++;
				return;
			}
			timer = 0;

			final int chance = Misc.random(3);
			if (chance <= 2) {
				NPCAttacks.npcArray[6222][0] = 2;
				AnimationProcessor.addNewRequest(attacker, emote, 0);
				Multi.multiAttack1(attacker, target, 5, 6, rangeMax,
						CombatType.RANGE, 2, 1, 50008);

			} else {
				NPCAttacks.npcArray[6222][0] = 3;
				AnimationProcessor.addNewRequest(attacker, emote, 0);
				Multi.multiAttack1(attacker, target, 5, 6, magicMax,
						CombatType.MAGIC, 2, 1, 50008);

			}
		}
		((NPC) attacker).updateRequired = true;

	}

	public static void handleWingman(Entity attacker, Entity target) {
		if (target instanceof Player) {
			if (timer3 < maxTimer3) {
				timer3++;
				return;
			}
			timer3 = 0;
			int damage = 0;
			damage = Hits.canHitMagic(attacker, target, 1189) ? Misc
					.random(wingmanMax) - 1 + 1 : 0;
			if (target instanceof Player)
				if (((Player) target).getPrayerHandler().clicked[33]
						|| ((Player) target).getPrayerHandler().clicked[12])
					damage = 0;
			AnimationProcessor.addNewRequest(attacker, 6953, 0);
			MagicHandler.createProjectile(attacker, target, 50007, 78);
			HitExecutor
					.addNewHit(attacker, target, CombatType.MAGIC, damage, 0);
		}
	}

}
