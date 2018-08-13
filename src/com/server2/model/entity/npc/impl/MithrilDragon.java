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
 * @author Rene Roosen Takes care of a MithrilDragons' attack
 */
public class MithrilDragon {

	public static void attack(Entity attacker, Entity target) {
		// Mith drags ain't gonna attack an npc, just for safety.
		if (target instanceof Player) {
			attacker.setCombatDelay(6);
			int attack = Misc.random(2);

			if (attack == 0)
				if (TileManager.calculateDistance(attacker, target) > 3)
					attack = 1 + Misc.random(1);
			int damage = 0;
			switch (attack) {

			case 0:
				AnimationProcessor.addNewRequest(attacker, 80, 0);
				if (Infliction.canHitWithMelee(attacker, target))
					damage = Misc.random(24);
				if (((Player) target).protectingMelee())
					damage = 0;
				HitExecutor.addNewHit(attacker, target, CombatType.MELEE,
						damage, 0);
				break;

			case 1:
				damage = Misc.random(65);
				AnimationProcessor.addNewRequest(attacker, 81, 0);
				MagicHandler.createProjectile(attacker, target, 50005, 78); // Range
				if (((Player) target).playerEquipment[PlayerConstants.SHIELD] == 11283
						|| ((Player) target).playerEquipment[PlayerConstants.SHIELD] == 1540
						|| ((Player) target).antiFirePotTimer > 0)
					damage = damage / 5;
				if (((Player) target).protectingRange())
					damage = (int) (damage * 0.6);
				HitExecutor.addNewHit(attacker, target, CombatType.RANGE,
						damage, 2);
				break;

			case 2:
				damage = Misc.random(80);
				AnimationProcessor.addNewRequest(attacker, 81, 0);
				MagicHandler.createProjectile(attacker, target, 1337, 78); // Magic
				if (((Player) target).playerEquipment[PlayerConstants.SHIELD] == 11283
						|| ((Player) target).playerEquipment[PlayerConstants.SHIELD] == 1540
						|| ((Player) target).antiFirePotTimer > 0)
					damage = damage / 5;
				HitExecutor.addNewHit(attacker, target, CombatType.MAGIC,
						damage, 2);
				break;

			}

		}
	}

}
