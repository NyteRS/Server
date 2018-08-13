package com.server2.model.combat.additions;

import com.server2.model.combat.HitExecutor;
import com.server2.model.combat.ranged.MaxHitRanged;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Misc;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class HandCannon {

	public static int calculateDamage(Player client, Player target) {
		int barse = Misc.random((int) MaxHitRanged.calculateBaseDamage(client,
				target, true));
		if (Misc.random(5) != 1)
			barse = (int) (barse * 0.75);
		if (Misc.random(2) != 1)
			barse = (int) (barse * 0.8);
		return barse;
	}

	public static void handCannon(Player client, Entity attacker,
			Entity target, boolean special) {
		final int chance = Misc
				.random(client.playerLevel[PlayerConstants.FIREMAKING] - 60) * 10;
		if (chance == 1) {
			client.getEquipment().deleteEquipment(3);
			client.getActionSender().sendMessage("Your hand cannon explodes!");
			HitExecutor.addNewHit(client, client, CombatType.RECOIL,
					Misc.random(16), 0);
			GraphicsProcessor.addNewRequest(client, 2140, 0, 0);
			return;

		}
		if (attacker instanceof Player)
			if (special && target instanceof Player)
				HitExecutor.addNewHit(
						attacker,
						target,
						CombatType.RANGE,
						calculateDamage(client, (Player) target)
								+ Misc.random(10), 1);
			else if (special && target instanceof NPC)
				HitExecutor.addNewHit(attacker, target, CombatType.RANGE,
						Misc.random(((Player) attacker).playerLevel[4] / 3), 1);

	}
}
