package com.server2.model.combat.additions;

import com.server2.model.combat.HitExecutor;
import com.server2.model.combat.melee.MaxHit;
import com.server2.model.combat.ranged.Ranged;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Misc;

/**
 * 
 * @author Rene/Lukas/Ipod touch
 * 
 */
public class KorasiSpecial {

	private static int calculateDamage(Entity target, Player client,
			boolean isItTrue) {
		if (target instanceof NPC)
			return Misc.random(75);
		final Player client2 = (Player) target;
		final double calculateBase = MaxHit.calculateBaseDamage(client, true);
		int chance;
		if (isItTrue)
			chance = (int) calculateBase / 3;
		else
			chance = 0;
		int finalDamage = chance + Misc.random((int) (calculateBase * 1.5));

		if (finalDamage > calculateBase * 1.5)
			finalDamage = (int) calculateBase + 10;
		if (client2.getPrayerHandler().clicked[12]
				|| client2.getPrayerHandler().clicked[33])
			finalDamage *= 0.6;

		if (client.getPrayerHandler().clicked[44])
			client.getActionAssistant().addHP(finalDamage / 5);
		if (finalDamage < 13)
			finalDamage = 13;
		else if (finalDamage > 70)
			finalDamage = 70;
		if (Misc.random(4) != 1 && finalDamage > 25)
			finalDamage = (int) (finalDamage * 0.8);
		return finalDamage;

	}

	private static int calculateNpcDamage(Player client, boolean isItTrue) {
		final double calculateBase = MaxHit.calculateBaseDamage(client, true);
		int chance;

		if (isItTrue)
			chance = (int) (calculateBase + calculateBase / 3);
		else
			chance = (int) calculateBase;
		int finalDamage = (int) (Misc.random(chance) + calculateBase / 2);

		if (finalDamage > calculateBase * 1.5)
			finalDamage = (int) calculateBase;
		client.getActionAssistant().addSkillXP(finalDamage * client.multiplier,
				PlayerConstants.STRENGTH);
		if (client.getPrayerHandler().clicked[44])
			client.getActionAssistant().addHP(finalDamage / 5);
		return finalDamage;

	}

	public static void korasiNpc(Player client, Entity attacker, Entity target,
			int damage) {
		GraphicsProcessor.addNewRequest(target, 1248, 100, 0);
		final boolean isItTrue = Hits.canHitMagic(attacker, target, 1189);
		HitExecutor.addNewHit(
				attacker,
				target,
				CombatType.MAGIC,
				calculateNpcDamage(client, isItTrue),
				Ranged.isUsingRange((Player) attacker) ? Ranged
						.getHitDelay(attacker) : 1);
	}

	public static void korasiSpecial(Player client, Entity attacker,
			Entity target, int damage) {
		GraphicsProcessor.addNewRequest(target, 1248, 100, 0);
		final boolean isItTrue = Hits.canHitMagic(attacker, target, 1189);
		HitExecutor.addNewHit(
				attacker,
				target,
				CombatType.MAGIC,
				calculateDamage(target, client, isItTrue),
				Ranged.isUsingRange((Player) attacker) ? Ranged
						.getHitDelay(attacker) : 1);
	}
}
