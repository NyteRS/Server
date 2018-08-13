package com.server2.model.entity.npc.impl;

import com.server2.model.combat.HitExecutor;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class Karamel {

	/**
	 * Handles karamel
	 * 
	 * @param attacker
	 * @param target
	 */
	public static void handleKaramel(Entity attacker, Entity target) {
		if (target instanceof Player)
			if (target instanceof Player) {
				attacker.setCombatDelay(7);
				AnimationProcessor.addNewRequest(attacker, 1979, 0);
				if (target.getFreezeDelay() > 0)
					GraphicsProcessor.addNewRequest(target, 1677, 0, 2);
				else
					GraphicsProcessor.addNewRequest(target, 369, 0, 2);
				if (target.getFreezeDelay() == 0)
					target.setFreezeDelay(20);
				HitExecutor.addNewHit(attacker, target, CombatType.MAGIC,
						Misc.random(15), 3);
				attacker.setCombatDelay(6);
			}
	}
}
