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
public class Dragons {

	/**
	 * The max of a green dragon using melee
	 */
	private static final int greenMaxMelee = 9;

	/**
	 * The max of a green dragon using magic.
	 */
	private static final int greenMaxMagic = 70;

	/**
	 * Max melee hit of a blue dragon
	 */
	private static final int maxBlueMelee = 11;

	/**
	 * Max mage hit of a blue dragon
	 */
	private static final int maxBlueMage = 75;

	/**
	 * Handles a blue dragon
	 * 
	 * @param attacker
	 * @param target
	 */
	public static void blueDragon(Entity attacker, Entity target) {
		if (TileManager.calculateDistance(attacker, target) > 20) {
			attacker.setTarget(null);
			return;
		}
		if (target instanceof Player) {
			attacker.setCombatDelay(5);
			final int randomizer = Misc.random(5);
			int damage = 0;
			if (randomizer <= 3
					&& TileManager.calculateDistance(attacker, target) <= 2) {
				damage = Infliction.canHitWithMelee(attacker, target) ? Misc
						.random(maxBlueMelee) - 1 + 1 : 0;
				AnimationProcessor.addNewRequest(attacker, 80, 0);
				HitExecutor.addNewHit(attacker, target, CombatType.MELEE,
						damage, 1);
			} else {
				damage = Misc.random(maxBlueMage);
				if (((Player) target).playerEquipment[PlayerConstants.SHIELD] != 1540
						&& ((Player) target).playerEquipment[PlayerConstants.SHIELD] != 11283
						&& ((Player) target).antiFirePotTimer == 0)
					((Player) target).getActionSender().sendMessage(
							"The dragons breath burns you.");
				else {
					if (((Player) target).antiFirePotTimer > 0) {
						((Player) target).getActionSender().sendMessage(
								"Your antifire potion protects you.");
						damage = damage / 3;
					}
					if (((Player) target).playerEquipment[PlayerConstants.SHIELD] == 1540
							|| ((Player) target).playerEquipment[PlayerConstants.SHIELD] == 11283) {
						((Player) target).getActionSender().sendMessage(
								"Your anti-dragonfire shield protects you.");
						damage = damage / 5;
					}
					if (((Player) target).playerEquipment[PlayerConstants.SHIELD] == 11283)
						if (((Player) target).dfsCharge < 20) {
							((Player) target).dfsCharge++;
							((Player) target).getActionSender().sendMessage(
									"Your dfs now has :@red@ "
											+ ((Player) target).dfsCharge
											+ "@bla@ charges.");
							AnimationProcessor.createAnimation(target, 6695);
						}
				}
				AnimationProcessor.addNewRequest(attacker, 81, 0);
				MagicHandler.createProjectile(attacker, target, 1337, 78);
				HitExecutor.addNewHit(attacker, target, CombatType.MAGIC,
						damage, 2);
			}

		}
	}

	/**
	 * Handles a green dragon
	 * 
	 * @param attacker
	 * @param target
	 */
	public static void greenDragon(Entity attacker, Entity target) {
		if (TileManager.calculateDistance(attacker, target) > 20) {
			attacker.setTarget(null);
			return;
		}
		if (target instanceof Player) {
			attacker.setCombatDelay(5);
			final int randomizer = Misc.random(5);
			int damage = 0;
			if (randomizer <= 3
					&& TileManager.calculateDistance(attacker, target) <= 2) {
				damage = Infliction.canHitWithMelee(attacker, target) ? Misc
						.random(greenMaxMelee) - 1 + 1 : 0;
				AnimationProcessor.addNewRequest(attacker, 80, 0);
				HitExecutor.addNewHit(attacker, target, CombatType.MELEE,
						damage, 1);
			} else {
				damage = Misc.random(greenMaxMagic);
				if (((Player) target).playerEquipment[PlayerConstants.SHIELD] != 1540
						&& ((Player) target).playerEquipment[PlayerConstants.SHIELD] != 11283
						&& ((Player) target).antiFirePotTimer == 0
						&& ((Player) target).sAntiFirePotTimer == 0)
					((Player) target).getActionSender().sendMessage(
							"The dragons breath burns you.");
				else {

					if (((Player) target).sAntiFirePotTimer > 0) {
						((Player) target).getActionSender().sendMessage(
								"Your superantifire potion absorts the heat.");
						damage = 0;
					} else if (((Player) target).playerEquipment[PlayerConstants.SHIELD] == 1540
							&& ((Player) target).antiFirePotTimer > 0
							|| ((Player) target).playerEquipment[PlayerConstants.SHIELD] == 11283
							&& ((Player) target).antiFirePotTimer > 0) {
						((Player) target)
								.getActionSender()
								.sendMessage(
										"Your anti-dragonfire shield and potin fully protect you.");
						damage = 0;
					} else if (((Player) target).antiFirePotTimer > 0) {
						((Player) target).getActionSender().sendMessage(
								"Your antifire potion protects you.");
						damage = Misc.random(10);
					}

					else if (((Player) target).playerEquipment[PlayerConstants.SHIELD] == 1540
							|| ((Player) target).playerEquipment[PlayerConstants.SHIELD] == 11283) {
						((Player) target).getActionSender().sendMessage(
								"Your anti-dragonfire shield protects you.");
						damage = Misc.random(5);
					}
					if (((Player) target).playerEquipment[PlayerConstants.SHIELD] == 11283)
						if (((Player) target).dfsCharge < 20) {
							((Player) target).dfsCharge++;
							((Player) target).getActionSender().sendMessage(
									"Your dfs now has :@red@ "
											+ ((Player) target).dfsCharge
											+ "@bla@charges.");
							AnimationProcessor.createAnimation(target, 6695);
						}
				}
				AnimationProcessor.addNewRequest(attacker, 81, 0);
				MagicHandler.createProjectile(attacker, target, 1337, 78);
				HitExecutor.addNewHit(attacker, target, CombatType.MAGIC,
						damage, 2);
			}

		}
	}
}
