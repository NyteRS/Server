package com.server2.model.entity.npc.impl;

import com.server2.content.skills.prayer.PrayerHandler;
import com.server2.model.combat.HitExecutor;
import com.server2.model.combat.additions.Infliction;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.player.Player;
import com.server2.util.Areas;
import com.server2.util.Misc;
import com.server2.world.PlayerManager;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class AvatarOfDestruction {

	/**
	 * Max hit of AOD
	 */
	private static final int maxHit = 65;

	/**
	 * Attack animation
	 */
	private static final int attackAnimation = 11197;

	/**
	 * Handles the Avatar of Destruction
	 * 
	 * @param attacker
	 */
	public static void handleAOD(Entity attacker) {
		if (attacker.isDead())
			return;
		attacker.setCombatDelay(6);

		AnimationProcessor.addNewRequest(attacker, attackAnimation, 1);

		for (int i = 0; i < PlayerManager.getSingleton().getPlayers().length; i++) {
			if (PlayerManager.getSingleton().getPlayers()[i] == null)
				continue;
			int damage = 0;
			if (Areas.isAOD(PlayerManager.getSingleton().getPlayers()[i]
					.getPosition()))
				damage = Infliction.canHitWithMelee(attacker, PlayerManager
						.getSingleton().getPlayers()[i]) ? Misc.random(maxHit) - 1 + 1
						: 0;
			else
				continue;
			if (PlayerManager.getSingleton().getPlayers()[i] instanceof Player) {
				final Player target = PlayerManager.getSingleton().getPlayers()[i];
				if (target.isDead())
					continue;
				if (Misc.random(7) == 1) {
					target.getPrayerHandler().resetAllPrayers();
					target.getActionSender()
							.sendMessage(
									"The avatar of destruction slams your prayers off!");
				}
				if (Misc.random(5) == 1)
					target.getPrayerHandler().updatePrayer(20);

				if (damage > 0)
					if (target.getPrayerHandler().clicked[PrayerHandler.PROTECT_FROM_MELEE]
							|| target.getPrayerHandler().clicked[PrayerHandler.DEFLECT_MELEE])
						damage = damage / 2;

				if (damage == 0)
					if (Misc.random(1) == 0)
						damage = Misc.random(3);

				HitExecutor.addNewHit(attacker, target, CombatType.MELEE,
						damage, 2);
			}

		}
	}
}
