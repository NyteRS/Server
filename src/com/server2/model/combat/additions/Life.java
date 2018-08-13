package com.server2.model.combat.additions;

import com.server2.model.entity.Entity;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author killamess
 * 
 */
public class Life {

	public static boolean isAlive(Entity attacker) {

		if (attacker instanceof Player) {
			if (((Player) attacker).hitpoints > 0)
				return true;

		} else if (attacker instanceof NPC)
			if (((NPC) attacker).getHP() > 0)
				return true;

		return false;
	}

	public static boolean isAlive(Entity attacker, Entity victim) {

		if (attacker instanceof Player) {
			if (((Player) attacker).hitpoints < 1)
				return false;

		} else if (attacker instanceof NPC)
			if (((NPC) attacker).getHP() < 1)
				return false;
		if (victim instanceof Player) {
			if (((Player) victim).hitpoints < 1)
				return false;

		} else if (victim instanceof NPC)
			if (((NPC) victim).getHP() < 1)
				return false;
		return true;
	}

}
