package com.server2.model.entity.npc.impl;

import com.server2.model.combat.HitExecutor;
import com.server2.model.combat.additions.Infliction;
import com.server2.model.combat.magic.MagicFormula;
import com.server2.model.combat.magic.MagicHandler;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class SaradominRoom {
	/**
	 * Zilyana Timer
	 */
	public static final int maxTime = 2;

	/**
	 * Commander Zilyana's max hit and minions.
	 */
	public static final int maxZilyana = 32, maxStarlight = 16;

	/**
	 * Current Zilyana Timer.
	 */
	public static int timer = 2;

	/**
	 * Final minion timer.
	 */
	public static final int maxMinions = 5;

	/**
	 * Holds the current timer.
	 */
	public static int starTimer;

	/**
	 * Handles the current timer of Growler.
	 * 
	 */
	public static int growlerTimer;

	/**
	 * Handles the bree timer.
	 */
	public static int breeTimer;

	/**
	 * Handles Bree's attacks.
	 */

	public static void handleBree(Entity attacker, Entity target) {
		if (!BossAgression.getInstance().saraRoom(target.getAbsX(),
				target.getAbsY(), target.getHeightLevel()))
			return;
		if (target instanceof Player) {
			if (breeTimer < maxMinions) {
				breeTimer++;
				return;
			}
			breeTimer = 0;
			AnimationProcessor.addNewRequest(attacker, 7009, 0);
			MagicHandler.createProjectile(attacker, target, 50003, 78);
			int damage = 0;
			damage = Infliction.canHitWithRanged(attacker, target) ? Misc
					.random(17) - 1 + 1 : 0;
			if (target instanceof Player) {
				if (((Player) target).getPrayerHandler().clicked[34]
						|| ((Player) target).getPrayerHandler().clicked[13])
					damage = 0;
				HitExecutor.addNewHit(attacker, target, CombatType.RANGE,
						damage, 2);
			}
		}
	}

	/**
	 * Method that handles growler
	 * 
	 * @param attacker
	 * @param target
	 */
	public static void handleGrowler(Entity attacker, Entity target) {
		if (!BossAgression.getInstance().saraRoom(target.getAbsX(),
				target.getAbsY(), target.getHeightLevel()))
			return;
		if (target instanceof Player) {
			if (growlerTimer < maxMinions) {
				growlerTimer++;
				return;
			}
			growlerTimer = 0;
			int damage = 0;
			AnimationProcessor.addNewRequest(attacker, 7019, 0);
			MagicHandler.createProjectile(attacker, target, 50002, 78);
			damage = MagicFormula.canMagicAttack(attacker, target) ? Misc
					.random(maxStarlight) - 1 + 1 : 0;
			if (target instanceof Player)
				if (((Player) target).getPrayerHandler().clicked[33]
						|| ((Player) target).getPrayerHandler().clicked[12])
					damage = 0;
			HitExecutor
					.addNewHit(attacker, target, CombatType.MAGIC, damage, 2);
		}
	}

	/**
	 * Handles starlight.
	 * 
	 * @param attacker
	 * @param target
	 */
	public static void handleStarlight(Entity attacker, Entity target) {
		if (!BossAgression.getInstance().saraRoom(target.getAbsX(),
				target.getAbsY(), target.getHeightLevel()))
			return;
		if (target instanceof Player) {
			if (starTimer < maxMinions) {
				starTimer++;
				return;
			}

			starTimer = 0;
			AnimationProcessor.addNewRequest(attacker, 6376, 0);
			int damage = 0;
			damage = Infliction.canHitWithMelee(attacker, target) ? Misc
					.random(maxStarlight) - 1 + 1 : 0;
			if (target instanceof Player)
				if (((Player) target).getPrayerHandler().clicked[35]
						|| ((Player) target).getPrayerHandler().clicked[14])
					damage = 0;
			HitExecutor
					.addNewHit(attacker, target, CombatType.MELEE, damage, 0);
		}
	}

	/**
	 * Method to handle zilyana.
	 * 
	 * @param attacker
	 * @param target
	 */
	public static void handleZilyana(Entity attacker, Entity target) {
		if (!BossAgression.getInstance().saraRoom(target.getAbsX(),
				target.getAbsY(), target.getHeightLevel()))
			return;
		if (target instanceof Player) {
			if (timer < maxTime) {
				timer++;
				return;
			}

			timer = 0;
			if (target instanceof NPC) {
				HitExecutor.addNewHit(attacker, target, CombatType.MELEE,
						Misc.random(30), 0);
				AnimationProcessor.addNewRequest(attacker, 6964, 0);
				return;
			}
			final int whichAttack = Misc.random(4);
			int damage = 0;
			AnimationProcessor.addNewRequest(attacker, 6964, 0);

			damage = 0;
			if (whichAttack == 0) {
				if (MagicFormula.canMagicAttack(attacker, target))
					damage = Misc.random(maxZilyana);
				if (((Player) target).getPrayerHandler().clicked[35]
						|| ((Player) target).getPrayerHandler().clicked[14])
					damage = damage / 5;
			} else {
				if (Infliction.canHitWithMelee(attacker, target))
					damage = Misc.random(maxZilyana);
				if (((Player) target).getPrayerHandler().clicked[33]
						|| ((Player) target).getPrayerHandler().clicked[12])
					damage = damage / 5;
			}
			if (whichAttack == 0) {
				GraphicsProcessor.addNewRequest(target, 1207, 0, 0);
				HitExecutor.addNewHit(attacker, target, CombatType.MAGIC,
						damage, 0);
			} else
				HitExecutor.addNewHit(attacker, target, CombatType.MELEE,
						damage, 0);
		}
		((NPC) attacker).updateRequired = true;
	}
}
