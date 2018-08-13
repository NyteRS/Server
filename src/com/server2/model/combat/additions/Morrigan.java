package com.server2.model.combat.additions;

import com.server2.model.combat.HitExecutor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;

/**
 * 
 * @author Rene
 * 
 */
public class Morrigan {
	// is it really needed to create a class for this?
	public static void morrigansJavelinSpecial(Entity attacker, Entity target,
			int hit) {
		final double loopTime = hit / 5;
		for (int i = 0; i < loopTime; i++)
			HitExecutor.addNewHit(attacker, target, CombatType.RANGE, 5, i + 5);
	}

}
