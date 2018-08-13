package com.server2.content.skills.prayer.curses.deflection;

import com.server2.model.combat.Hit;
import com.server2.model.combat.HitExecutor;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class ActivateDeflection {

	/**
	 * Executes a Deflection curse between two entities
	 * 
	 * @param attacker
	 * @param victim
	 * @param deflection
	 * @param damage
	 */
	public static void execute(Hit hit, Deflection deflection) {
		if (!(hit.getVictim() instanceof Player))
			return;
		if (hit.getCombatType() != deflection.combatType())
			return;
		final Player target = (Player) hit.getVictim();
		if (System.currentTimeMillis() - target.lastDeflection > 1500) {
			GraphicsProcessor.addNewRequest(target, deflection.graphicID(), 0,
					0);
			AnimationProcessor.addNewRequest(target, deflection.animationID(),
					0);
			target.lastDeflection = System.currentTimeMillis();
		}
		HitExecutor.addNewHit(hit.getVictim(), hit.getAttacker(),
				CombatType.RECOIL, (int) (0.10 * hit.getDamage()), 0);
		hit.setDamage((int) (hit.getDamage() * 0.6));
	}
}
