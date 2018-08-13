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
public class FrostDragons {

	/**
	 * Handles the frost dragons' max hit.
	 */
	public static int maxHitRange = 35, maxHitMage = 50, maxHitMelee = 40;

	/**
	 * Handles the frost dragon.
	 */

	public static void handleFrostDragon(Entity attacker, Entity target) {
		if (target instanceof Player)
			if (target instanceof Player) {
				attacker.setCombatDelay(5);
				int damage = 0;

				if (TileManager.calculateDistance(attacker, target) < 3
						&& Misc.random(3) == 1) {
					AnimationProcessor.addNewRequest(attacker, 13151, 0);
					damage = Infliction.canHitWithMelee(attacker, target) ? Misc
							.random(maxHitMelee) - 1 + 1 : 0;
					if (((Player) target).playerEquipment[PlayerConstants.SHIELD] == 11283
							|| ((Player) target).playerEquipment[PlayerConstants.SHIELD] == 1540
							|| ((Player) target).antiFirePotTimer > 0)
						damage = damage / 5;
					HitExecutor.addNewHit(attacker, target, CombatType.MELEE,
							damage, 0);
				} else {
					final int nextAttack = Misc.random(1);
					if (nextAttack == 0) {
						damage = Misc.random(70);
						AnimationProcessor.addNewRequest(attacker, 13152, 0);
						// Graphics.addNewRequest(attacker, 1, 0, 0);
						MagicHandler.createProjectile(attacker, target, 50005,
								78);
						if (((Player) target).playerEquipment[PlayerConstants.SHIELD] == 11283
								|| ((Player) target).playerEquipment[PlayerConstants.SHIELD] == 1540
								|| ((Player) target).antiFirePotTimer > 0)
							damage = damage / 5;
						HitExecutor.addNewHit(attacker, target,
								CombatType.MAGIC, damage, 2);
					} else if (nextAttack == 1) {
						AnimationProcessor.addNewRequest(attacker, 13155, 0);
						MagicHandler.createProjectile(attacker, target, 50006,
								78);
						damage = Infliction.canHitWithRanged(attacker, target) ? Misc
								.random(maxHitRange) - 1 + 1 : 0;
						if (((Player) target).playerEquipment[PlayerConstants.SHIELD] == 11283
								|| ((Player) target).playerEquipment[PlayerConstants.SHIELD] == 1540
								|| ((Player) target).antiFirePotTimer > 0)
							damage = damage / 5;
						HitExecutor.addNewHit(attacker, target,
								CombatType.RANGE, damage, 2);
					}
				}
			}
	}
}
