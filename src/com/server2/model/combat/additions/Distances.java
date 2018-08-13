package com.server2.model.combat.additions;

import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.world.map.tile.TileManager;

/**
 * 
 * @author Rene
 * 
 */
public class Distances {

	public static boolean inAttackableDistance(Entity attacker, Entity target) {

		if (attacker == null || target == null)
			return false;

		int actualDistance = 1;

		actualDistance = TileManager.calculateDistance(attacker, target);

		if (attacker.getCombatType() == CombatType.MELEE) {
			if (attacker instanceof Player) {
				if (target instanceof Player) {
					if (actualDistance > 2) // CHECK THIS OUT
						return false;
				} else if (actualDistance > 1)
					return false;
			} else if (attacker instanceof NPC)
				if (actualDistance > 1)
					return false;
			/*
			 * if (actualDistance <= 0) { AUCTION : IF EXTREME COMBAT BUGS
			 * OCCUR< SEE HERE!
			 * 
			 * return false; }
			 */
			if (attacker instanceof Player)
				if (actualDistance > 1 && attacker.getFreezeDelay() > 0)
					return false;
		} else if (attacker.getCombatType() == CombatType.RANGE
				|| attacker.getCombatType() == CombatType.MAGIC) {
			if (attacker instanceof Player)
				if (((Player) attacker).combatMode == 6)
					if (actualDistance > 8)
						return false;
			if (actualDistance > 32)
				return false;
			if (actualDistance <= 0)
				return false;
		} else if (attacker.getCombatType() == CombatType.NOTHING)
			return false;

		return true;
	}

}