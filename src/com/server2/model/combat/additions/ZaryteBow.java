package com.server2.model.combat.additions;

import com.server2.model.entity.Entity;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class ZaryteBow {

	/**
	 * Instances the class
	 */

	public static ZaryteBow INSTANCE = new ZaryteBow();

	/**
	 * Gets the instance
	 */
	public static ZaryteBow getInstance() {
		return INSTANCE;
	}

	/**
	 * Handles the zaryte bow special
	 */
	public void handleSpecial(Entity attacker, Entity target) {
		if (target instanceof Player && attacker instanceof Player) {
			((Player) target).playerLevel[5] = 0;
			((Player) target).getActionAssistant().refreshSkill(5);
			((Player) target).getActionSender().sendMessage(
					Misc.capitalizeFirstLetter(((Player) attacker)
							.getUsername())
							+ "'s zaryte bow drained your prayer!");
		}
	}
}
