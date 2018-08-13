package com.server2.model.entity.npc.impl;

import com.server2.model.combat.HitExecutor;
import com.server2.model.combat.additions.Infliction;
import com.server2.model.combat.magic.MagicHandler;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.npc.NPCAttacks;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Misc;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class KingBlackDragon {

	private static final int maxMelee = 25;

	private static int timer = 5;

	public static void handleKBD(Entity attacker, Entity target) {
		if (target instanceof Player) {
			if (timer < 5) {
				timer++;

				return;
			}

			timer = 0;
			final int attackStyle = Misc.random(1);
			int damage = 0;

			switch (attackStyle) {

			case 0:
				NPCAttacks.npcArray[50][0] = 1;
				AnimationProcessor.addNewRequest(attacker, 80, 0);
				damage = Infliction.canHitWithMelee(attacker, target) ? Misc
						.random(maxMelee) - 1 + 1 : 0;
				if (((Player) target).getPrayerHandler().clicked[14]
						|| ((Player) target).getPrayerHandler().clicked[35])
					HitExecutor.addNewHit(attacker, target, CombatType.MELEE,
							0, 0);
				else
					HitExecutor.addNewHit(attacker, target, CombatType.MELEE,
							damage, 0);
				break;

			case 1:
				NPCAttacks.npcArray[50][0] = 3;
				AnimationProcessor.addNewRequest(attacker, 81, 0);
				if (target.getFreezeDelay() == 0) {
					target.setFreezeDelay(30);
					((Player) target).getActionSender().sendMessage(
							"You're frozen by the dragon breath.");
				}
				target.setFreezeDelay(30);
				damage = 10 + Misc.random(65);
				if (((Player) target).playerEquipment[PlayerConstants.SHIELD] == 11283
						|| ((Player) target).playerEquipment[PlayerConstants.SHIELD] == 1540
						|| ((Player) target).antiFirePotTimer > 0) { // TODO:
																		// remove
																		// all
																		// the
																		// casts,
																		// only
																		// cast
																		// once
																		// etc.
					if (((Player) target).antiFirePotTimer > 0)
						((Player) target).getActionSender().sendMessage(
								"Your antifire potion protects you.");
					if (damage > 20)
						damage = 20;
					HitExecutor.addNewHit(attacker, target, CombatType.MAGIC,
							damage / 6, 2);
					((Player) target)
							.getActionSender()
							.sendMessage(
									"Your dragonfire shield protects you from the dragonfire.");
				} else {
					if (damage > 75)
						damage = 75;
					HitExecutor.addNewHit(attacker, target, CombatType.MAGIC,
							damage, 2);
					((Player) target).getActionSender().sendMessage(
							"You're badly burnt by the dragonfire.");
				}
				MagicHandler.createProjectile(attacker, target, 1338, 78);

				break;
			}
		}
		((NPC) attacker).updateRequired = true;
	}
}
