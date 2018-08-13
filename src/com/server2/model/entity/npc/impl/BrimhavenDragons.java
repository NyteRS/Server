package com.server2.model.entity.npc.impl;

import com.server2.model.combat.HitExecutor;
import com.server2.model.combat.additions.Infliction;
import com.server2.model.combat.magic.MagicHandler;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Misc;
import com.server2.world.map.tile.TileManager;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class BrimhavenDragons {

	/**
	 * Instances the BrimhavenDragons class.
	 */
	public static BrimhavenDragons INSTANCE = new BrimhavenDragons();

	/**
	 * An intArray holding the dragons' max melee hits.
	 */
	private static int[] maxHitsMelee = { 23, 20 };

	/**
	 * An intArray holding the dragons' max dragonfire hits.
	 */
	private static int[] maxHitsMage = { 65, 50 };
	/**
	 * An intArray holding a dragon's animations.
	 */
	private static int[] animations = { 91, 81 };// TODO fill in animations.

	/**
	 * Gets the instance.
	 * 
	 * @return INSTANCE
	 */
	public static BrimhavenDragons getInstance() {
		return INSTANCE;
	}

	/**
	 * Handles an iron dragon.
	 */
	public void handleIronDragon(Entity attacker, Entity target) {
		if (target instanceof Player) {
			attacker.setCombatDelay(5);
			int damage = 0;
			if (TileManager.calculateDistance(attacker, target) < 3
					&& Misc.random(2) == 1) {
				AnimationProcessor.addNewRequest(attacker, animations[0], 0);
				damage = Infliction.canHitWithMelee(attacker, target) ? Misc
						.random(maxHitsMelee[1]) - 1 + 1 : 0;
				final Player target2 = (Player) target;
				if (target2.protectingMelee())
					damage = 0;
				HitExecutor.addNewHit(attacker, target, CombatType.MELEE,
						damage, 0);
			} else {
				AnimationProcessor.addNewRequest(attacker, animations[1], 0);
				MagicHandler.createProjectile(attacker, target, 1337, 78);
				damage = Misc.random(maxHitsMage[1]);
				if (((Player) target).playerEquipment[PlayerConstants.SHIELD] == 1540
						|| ((Player) target).playerEquipment[PlayerConstants.SHIELD] == 11283) {
					damage = damage / 5;
					((Player) target)
							.getActionSender()
							.sendMessage(
									"Your antidragon shield protects you from the dragon breath.");
					if (((Player) target).antiFirePotTimer > 0) {
						((Player) target)
								.getActionSender()
								.sendMessage(
										"Your antifire potion protects you from the dragon breath.");
						damage = 0;
					}
				} else if (((Player) target).antiFirePotTimer > 0) {
					damage = damage / 3;
					((Player) target)
							.getActionSender()
							.sendMessage(
									"Your antifire potion protects you from the dragon breath.");

				} else if (((Player) target).sAntiFirePotTimer > 0) {
					damage = 0;
					((Player) target).getActionSender().sendMessage(
							"Your superantifire potion protects you fully.");

				}
				HitExecutor.addNewHit(attacker, target, CombatType.MAGIC,
						damage, 2);
			}
		}
	}

	/**
	 * Handles a steel dragon.
	 */
	public void handleSteelDragon(Entity attacker, Entity target) {
		if (target instanceof Player) {
			attacker.setCombatDelay(5);
			int damage = 0;
			if (TileManager.calculateDistance(attacker, target) < 3
					&& Misc.random(2) == 1) {
				AnimationProcessor.addNewRequest(attacker, animations[0], 0);
				damage = Infliction.canHitWithMelee(attacker, target) ? Misc
						.random(maxHitsMelee[0]) - 1 + 1 : 0;
				final Player target2 = (Player) target;
				if (target2.protectingMelee())
					damage = 0;
				HitExecutor.addNewHit(attacker, target, CombatType.MELEE,
						damage, 0);
			} else {
				AnimationProcessor.addNewRequest(attacker, animations[1], 0);
				MagicHandler.createProjectile(attacker, target, 1337, 78);
				damage = Misc.random(maxHitsMage[0]);
				if (((Player) target).playerEquipment[PlayerConstants.SHIELD] == 1540
						|| ((Player) target).playerEquipment[PlayerConstants.SHIELD] == 11283) {
					damage = damage / 5;
					((Player) target)
							.getActionSender()
							.sendMessage(
									"Your antidragon shield protects you from the dragon breath.");
					if (((Player) target).antiFirePotTimer > 0) {
						((Player) target)
								.getActionSender()
								.sendMessage(
										"Your antifire potion protects you from the dragon breath.");
						damage = 0;
					}
				} else if (((Player) target).antiFirePotTimer > 0) {
					damage = damage / 3;
					((Player) target)
							.getActionSender()
							.sendMessage(
									"Your antifire potion protects you from the dragon breath.");

				} else if (((Player) target).sAntiFirePotTimer > 0) {
					damage = 0;
					((Player) target).getActionSender().sendMessage(
							"Your superantifire potion protects you fully.");

				}

				HitExecutor.addNewHit(attacker, target, CombatType.MAGIC,
						damage, 2);
			}
		}
	}
}
