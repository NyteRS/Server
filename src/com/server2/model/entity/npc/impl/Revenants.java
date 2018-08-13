package com.server2.model.entity.npc.impl;

import com.server2.model.combat.HitExecutor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;
import com.server2.world.map.tile.TileManager;

/**
 * 
 * @author Lukas
 * 
 */
public class Revenants {

	private static int[] maxHits = { 6609, 23, 6645, 24, 6646, 25, 6647, 26,
			6610, 27, 6649, 31, 6650, 30, 6998, 30 };

	private static int maxHit(int id) {
		for (int i = 0; i < maxHits.length; i = i + 2)
			if (maxHits[i] == id)
				return maxHits[i + 1];
		return id;
	}

	private boolean canHit(Entity attacker, Entity target) {
		final int combat = ((NPC) attacker).getDefinition().getCombat();
		int combatOther = 0;
		if (target instanceof Player)
			combatOther = ((Player) target).getCombatLevel();
		else
			combatOther = 100;
		final int lvl = combat + 50 + Misc.random(200);
		if (Misc.random(combatOther) > Misc.random(lvl) + 15)
			return false;

		return true;
	}

	private boolean canMelee(Entity attacker, Entity target) {
		if (TileManager.calculateDistance(attacker, target) <= 1)
			return true;
		return false;
	}

	private CombatType forbiddenOne(Entity target) {
		if (target instanceof Player) {
			final Player client = (Player) target;
			if (client.getPrayerHandler().clicked[33]
					|| client.getPrayerHandler().clicked[12])
				return CombatType.MAGIC;

			if (client.getPrayerHandler().clicked[14]
					|| client.getPrayerHandler().clicked[35])
				return CombatType.MELEE;

			if (client.getPrayerHandler().clicked[13]
					|| client.getPrayerHandler().clicked[34])
				return CombatType.RANGE;
		}
		return CombatType.CANNON;
	}

	private CombatType getCombatType(Entity attacker, Entity target,
			CombatType cannotBeThis) {
		if (canMelee(attacker, target)) {
			final int change = Misc.random(3);
			if (change <= 1) {
				if (CombatType.MELEE != cannotBeThis)
					return CombatType.MELEE;

				return CombatType.MAGIC;

			}
			if (change == 2)
				return CombatType.MAGIC;
			else
				return CombatType.RANGE;

		} else {
			final int chance = Misc.random(2);

			if (chance == 1) {
				if (CombatType.RANGE != cannotBeThis)
					return CombatType.RANGE;

				return CombatType.MAGIC;

			} else {
				if (CombatType.MAGIC != cannotBeThis)
					return CombatType.MAGIC;

				return CombatType.RANGE;
			}
		}

	}

	private boolean heal(Entity attacker) {
		final NPC npc = (NPC) attacker;
		final int totalHP = npc.getDefinition().getHealth();
		final int hp = npc.getHP();

		if (npc.healed > 15)
			return false;
		if (hp > totalHP / 2)
			return false;

		int healAmount = 8;

		int combat = ((NPC) attacker).getDefinition().getCombat();
		combat = combat / 15;
		healAmount = healAmount + combat;
		npc.setHP(hp + healAmount);
		npc.healed++;

		return true;
	}

	public void hit(Entity attacker, Entity victim) {
		if (TileManager.calculateDistance(attacker, victim) < 15) {
			attacker.setCombatDelay(5);

			if (victim == null || attacker == null)
				return;

			int damage = Misc.random(maxHit(((NPC) attacker).getDefinition()
					.getType()));
			if (!canHit(attacker, victim))
				damage = 0;

			CombatType forbidden = CombatType.CANNON;
			if (Misc.random(3) == 1)
				forbidden = forbiddenOne(victim);
			final CombatType combatType = getCombatType(attacker, victim,
					forbidden);
			if (victim instanceof Player) {
				final Player client = (Player) victim;
				if (combatType == CombatType.MELEE)
					if (client.getPrayerHandler().clicked[14]
							|| client.getPrayerHandler().clicked[35])
						damage = 0;
				if (combatType == CombatType.RANGE)
					if (client.getPrayerHandler().clicked[13]
							|| client.getPrayerHandler().clicked[34])
						damage = 0;
				if (combatType == CombatType.MAGIC)
					if (client.getPrayerHandler().clicked[33]
							|| client.getPrayerHandler().clicked[12])
						damage = 0;
			}
			HitExecutor.addNewHit(attacker, victim, combatType, damage, 0);
			if (Misc.random(2) <= 1)
				heal(attacker);

		}

	}
}
