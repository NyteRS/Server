package com.server2.content.skills.prayer.curses;

import com.server2.content.skills.prayer.PrayerHandler;
import com.server2.model.combat.magic.MagicHandler;
import com.server2.model.entity.Entity;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene Roosen Handles the SoulSplit curse.
 */
public class SoulSplit {

	/**
	 * Finalizes the curse's effects
	 * 
	 * @param attacker
	 * @param target
	 * @param damage
	 */
	public static void finalize(final Player attacker, final Entity target,
			int damage) {
		if (target instanceof Player)
			((Player) target).getPrayerHandler().updatePrayer(damage / 4);
		attacker.heal(damage / 5);
		GraphicsProcessor.addNewRequest(target, 2264, 0, 0);
		MagicHandler.createProjectile(target, attacker, 40007, 110);
	}

	/**
	 * Initiates the SoulSplit curse between two entities
	 * 
	 * @param attacker
	 * @param target
	 */
	public static void start(final Player attacker, final Entity target) {
		if (attacker.getPrayerHandler().clicked[PrayerHandler.SOULSPLIT])
			MagicHandler.createProjectile(attacker, target, 40006, 95);
	}

}
