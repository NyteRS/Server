package com.server2.model.combat.additions;

import com.server2.model.combat.HitExecutor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Killamess Our ring effects.
 * 
 */

public class Rings {

	public static void applyRecoil(Entity attacker, Entity target,
			int[] damage, int delay) {
		if (attacker instanceof Player)
			if (!((Player) attacker).getEquipment().wearingRecoil())
				return;

		if (damage[0] < 1 && damage[1] < 1)
			return;

		final int[] finalDamage = new int[2];
		final boolean[] inUse = new boolean[2];

		if (damage[0] > 0)
			inUse[0] = true;
		if (damage[1] > 0)
			inUse[1] = true;

		if (attacker instanceof Player) {
			if (((Player) attacker).getRecoilCount() == 0)
				((Player) attacker).setRecoilCount(250);

			if (((Player) attacker).getRecoilCount() == 1) {
				((Player) attacker).setRecoilCount(0);
				breakRing(attacker);
			}
			if (((Player) attacker).getRecoilCount() > 1)
				for (final int amount : damage)
					if (((Player) attacker).getRecoilCount() - amount >= 1)
						((Player) attacker).setRecoilCount(((Player) attacker)
								.getRecoilCount() - amount);
					else
						((Player) attacker).setRecoilCount(1);
		} else if (attacker instanceof NPC) {

		}
		for (int i = 0; i < 2; i++)
			finalDamage[i] = damage[i] / 6 + 1;

		if (inUse[0])
			HitExecutor.addNewHit(attacker, target, CombatType.RECOIL,
					finalDamage[0], 0);
		if (inUse[1])
			HitExecutor.addNewHit(attacker, target, CombatType.RECOIL,
					finalDamage[1], 0);
	}

	public static void breakRing(Entity ent) {
		if (ent instanceof Player) {
			((Player) ent).getEquipment().deleteRing();
			((Player) ent).getActionSender().sendMessage(
					"Your ring crumbles to dust.");
		}
	}

	public static int[] pressDetails(int i, int j) {
		final int[] data = new int[2];
		data[0] = i;
		data[1] = j;
		return data;
	}

}
