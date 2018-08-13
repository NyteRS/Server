package com.server2.model.entity.npc.impl;

import com.server2.model.combat.HitExecutor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Misc;

/**
 * 
 * @author Lukas
 * 
 */
public class Glacors {

	private final int maxHit = 31;
	private final int maxhitSpec = 69;

	private boolean freez(Entity target, Entity victim) {
		if (victim instanceof NPC)
			return false;
		final Player client = (Player) victim;
		victim.setFreezeDelay(20);

		HitExecutor.addNewHit(target, victim, CombatType.MAGIC,
				Misc.random(maxhitSpec), 0);
		client.resetWalkingQueue();
		GraphicsProcessor.addNewRequest(victim, 369, 0, 0);

		client.getActionSender().sendMessage("You have been frozen!");
		return true;
	}

	public void hit(Entity attacker, Entity target) {
		attacker.setCombatDelay(5);

		if (target == null || attacker == null)
			return;

		final NPC npc = (NPC) attacker;
		if (Misc.random(10) == 1) {
			freez(attacker, target);
			return;
		}
		CombatType combatType;
		if (Misc.random(5) == 1) {
			if (npc.lastCombatType == CombatType.MAGIC)
				combatType = CombatType.RANGE;
			else
				combatType = CombatType.MAGIC;
		} else
			combatType = npc.lastCombatType;
		npc.lastCombatType = combatType;
		int damage = Misc.random(maxHit);

		if (target instanceof Player) {
			final Player client = (Player) target;

			if (combatType == CombatType.RANGE)
				if (client.getPrayerHandler().clicked[13]
						|| client.getPrayerHandler().clicked[34])
					damage = 0;
			if (combatType == CombatType.MAGIC)
				if (client.getPrayerHandler().clicked[33]
						|| client.getPrayerHandler().clicked[12])
					damage = 0;
		}
		HitExecutor.addNewHit(attacker, target, combatType, damage, 0);

		final int totalHP = npc.maxHP;
		final int hp = npc.getHP();

		if (hp < totalHP / 2 && !npc.summoned) {
			// summon(attacker);
		}

	}

	public void hitSapping(Entity attacker, Entity victim) {

		if (((NPC) attacker).attacktimer < 3) {
			((NPC) attacker).attacktimer++;
			return;
		}
		((NPC) attacker).attacktimer = 0;
		final int damage = Misc.random(6);
		if (victim instanceof Player) {
			final Player client = (Player) victim;
			client.getActionAssistant().decreaseStat(PlayerConstants.PRAYER, 2);
			client.getActionSender().sendString(

			"" + client.playerLevel[PlayerConstants.PRAYER] + "", 4012);
		}
		HitExecutor.addNewHit(attacker, victim, CombatType.RECOIL, damage, 0);
	}
}
