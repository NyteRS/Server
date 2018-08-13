package com.server2.content.anticheat;

import com.server2.InstanceDistributor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class GodwarsOutsideAttack {

	public static boolean canAttack(Entity attacker, Entity target) {
		if (target instanceof NPC) {
			final NPC theTarget = (NPC) target;
			if (theTarget.getDefinition().getType() == 6222
					|| theTarget.getDefinition().getType() == 6260
					|| theTarget.getDefinition().getType() == 6203
					|| theTarget.getDefinition().getType() == 6247)
				if (attacker instanceof Player)
					if (!((Player) attacker).enteredGwdRoom) {
						((Player) attacker)
								.getActionSender()
								.sendMessage(
										"You cannot attack "
												+ InstanceDistributor
														.getNPCManager()
														.getNPCDefinition(
																theTarget
																		.getDefinition()
																		.getType())
														.getName()
												+ " from here.");
						return false;
					}
		}
		return true;

	}
}
