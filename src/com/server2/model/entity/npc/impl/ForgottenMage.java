package com.server2.model.entity.npc.impl;

import com.server2.content.skills.prayer.PrayerHandler;
import com.server2.model.combat.HitExecutor;
import com.server2.model.combat.additions.Hits;
import com.server2.model.combat.magic.Magic;
import com.server2.model.combat.magic.MagicHandler;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;
import com.server2.world.map.tile.TileManager;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class ForgottenMage {

	private static int chooseSpell(int combatLevel) {
		int spell = 1154;
		if (combatLevel >= 5 && combatLevel <= 16)
			spell = 1150 + Misc.random(4) * 2;
		else if (combatLevel >= 23 && combatLevel <= 36)
			spell = 1157 + Misc.random(4) * 3;
		else if (combatLevel >= 44 && combatLevel <= 57) {
			final int chance = Misc.random(4);
			spell = 1172;
			if (chance == 1)
				spell = 1172;
			else if (chance == 2)
				spell = 1175;
			else if (chance == 3)
				spell = 1177;
			else if (chance == 4)
				spell = 1181;
		} else if (combatLevel >= 62 && combatLevel <= 77) {
			final int chance = Misc.random(4);
			spell = 1183;
			if (chance == 1)
				spell = 1183;
			else if (chance == 2)
				spell = 1185;
			else if (chance == 3)
				spell = 1188;
			else if (chance == 4)
				spell = 1189;
		} else if (combatLevel >= 82) {
			final int chance = Misc.random(3);
			spell = 1190;
			if (chance == 1)
				spell = 1183;
			else if (chance == 2)
				spell = 1191;
			else if (chance == 3)
				spell = 1192;

		}
		return spell;
	}

	/**
	 * Handles the Forgotten Mages
	 * 
	 * @param attacker
	 * @param target
	 */
	public static void handleMage(Entity attacker, Entity target) {

		if (target instanceof Player && attacker instanceof NPC)
			if (TileManager.calculateDistance(attacker, target) < 8
					&& !((Player) target).mainRoom1()) {
				attacker.setCombatDelay(5);
				final int combatLevel = ((NPC) attacker).getDefinition()
						.getCombat();
				final int spellToDo = chooseSpell(combatLevel);
				int damage = 0;

				MagicHandler.startAnimation(attacker, spellToDo);
				MagicHandler.createProjectile(attacker, target, spellToDo, 78);
				if (Hits.canHitMagic(attacker, target, spellToDo)) {
					if (Magic.spell(spellToDo) != null) {
						damage = Misc
								.random(Magic.spell(spellToDo).getDamage());
						if (damage > 10)
							damage = 10;
					} else
						damage = Misc.random(15);
					if (((Player) target).getPrayerHandler().clicked[PrayerHandler.PROTECT_FROM_MAGIC]
							|| ((Player) target).getPrayerHandler().clicked[PrayerHandler.DEFLECT_MAGIC])
						damage = 0;
					HitExecutor.addNewHit(attacker, target, CombatType.MAGIC,
							damage, 3);
				} else
					HitExecutor.addNewHit(attacker, target, CombatType.MAGIC,
							0, 1);

			}
	}
}
