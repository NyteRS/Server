package com.server2.model.entity.npc.impl;

import com.server2.model.combat.HitExecutor;
import com.server2.model.combat.additions.Infliction;
import com.server2.model.combat.magic.MagicFormula;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.npc.NPCAttacks;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;
import com.server2.world.map.tile.TileManager;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class TormentedDemon {

	private static final int maxMelee = 19, maxRange = 27, maxMage = 27;

	public static void handleAttacks(Entity attacker, Entity target) {
		if (target instanceof Player) {
			NPCAttacks.npcArray[8349][7] = 80;
			attacker.setCombatDelay(6);

			int attackStyle = 0;
			int damage = 0;
			if (TileManager.calculateDistance(attacker, target) < 3)
				attackStyle = 0 + Misc.random(1);
			else
				attackStyle = 1 + Misc.random(1);

			switch (attackStyle) {

			case 0:
				NPCAttacks.npcArray[8349][0] = 1;
				AnimationProcessor.addNewRequest(attacker, 10922, 0);
				GraphicsProcessor.addNewRequest(attacker, 1886, 0, 0);
				if (((Player) target).tormentedDemonShield > 60) {
					GraphicsProcessor.addNewRequest(attacker, 1885, 0, 0);
					damage = Infliction.canHitWithMelee(attacker, target) ? Misc
							.random(maxMelee / 4) - 1 + 1 : 0;
					((Player) target)
							.getActionSender()
							.sendMessage(
									"Your tormented demon is using his shield, use darklight to destroy it.");
				} else
					damage = Infliction.canHitWithMelee(attacker, target) ? Misc
							.random(maxMelee) - 1 + 1 : 0;
				HitExecutor.addNewHit(attacker, target, CombatType.MELEE,
						damage, 0);
				break;

			case 1:
				NPCAttacks.npcArray[8349][0] = 2;
				AnimationProcessor.addNewRequest(attacker, 10919, 0);
				if (((Player) target).tormentedDemonShield > 60) {
					GraphicsProcessor.addNewRequest(attacker, 1885, 0, 0);
					damage = Infliction.canHitWithRanged(attacker, target) ? Misc
							.random(maxRange / 4) - 1 + 1 : 0;
					((Player) target)
							.getActionSender()
							.sendMessage(
									"Your tormented demon is using his shield, use darklight to destroy it.");

				} else
					damage = Infliction.canHitWithRanged(attacker, target) ? Misc
							.random(maxRange) - 1 + 1 : 0;
				HitExecutor.addNewHit(attacker, target, CombatType.RANGE,
						damage, 0);
				break;

			case 2:
				NPCAttacks.npcArray[8349][0] = 3;
				AnimationProcessor.addNewRequest(attacker, 10918, 0);
				if (((Player) target).tormentedDemonShield > 60) {
					GraphicsProcessor.addNewRequest(attacker, 1885, 0, 0);
					damage = MagicFormula.canMagicAttack(attacker, target) ? Misc
							.random(maxMage / 4) - 1 + 1 : 0;
					((Player) target)
							.getActionSender()
							.sendMessage(
									"Your tormented demon is using his shield, use darklight to destroy it.");
				} else
					damage = MagicFormula.canMagicAttack(attacker, target) ? Misc
							.random(maxMage) - 1 + 1 : 0;
				HitExecutor.addNewHit(attacker, target, CombatType.MAGIC,
						damage, 0);
				break;

			default:
				((Player) target)
						.getActionSender()
						.sendMessage(
								"Your tormented demon tried to do an unknown attack style, please");
				((Player) target).getActionSender().sendMessage(
						"Report this to Rene or Lukas.");
				break;
			}

		}
	}

}
