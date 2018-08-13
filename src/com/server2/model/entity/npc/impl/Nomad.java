package com.server2.model.entity.npc.impl;

import com.server2.content.misc.mobility.TeleportationHandler;
import com.server2.model.combat.HitExecutor;
import com.server2.model.combat.additions.Infliction;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class Nomad {

	/**
	 * Instance
	 */

	public static Nomad INSTANCE = new Nomad();

	/**
	 * Holds the max hit for Nomad.
	 */
	private static final int maxHit = 50;

	/**
	 * Gets the instance
	 */
	public static Nomad getInstance() {
		return INSTANCE;
	}

	/**
	 * Handles nomad
	 * 
	 * @param attacker
	 * @param target
	 */

	public static void handleNomad(Entity attacker, Entity target) {
		if (target instanceof Player)
			if (target instanceof Player) {
				attacker.setCombatDelay(5);

				AnimationProcessor.addNewRequest(attacker, 12696, 0);

				int damage = Infliction.canHitWithMelee(attacker, target) ? Misc
						.random(maxHit) - 1 + 1 : 0;
				if (target instanceof Player)
					if (((Player) target).getPrayerHandler().clicked[14]
							|| ((Player) target).getPrayerHandler().clicked[35])
						damage = damage / 2;
				HitExecutor.addNewHit(attacker, target, CombatType.MELEE,
						damage, 0);
			}
	}

	/**
	 * Handles the traveling.
	 * 
	 * @param client
	 */
	public void nomadTraveling(Player client) {
		if (client.nomadNeedsSpawn) {
			NPC.newLoginNPC(8528, 3048, 4976, client.getIndex() * 4 + 1);
			client.nomadNeedsSpawn = false;
		}
		TeleportationHandler.addNewRequest(client, 3048, 4976,
				client.getIndex() * 4 + 1, 0);

	}
}
