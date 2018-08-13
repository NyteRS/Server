package com.server2.model.entity.npc.impl;

import com.server2.model.combat.HitExecutor;
import com.server2.model.combat.additions.Infliction;
import com.server2.model.combat.magic.MagicFormula;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;

/**
 * 
 * @author Rene Roosen
 * 
 */

public class Strykewyrms {
	/**
	 * Instances the strykewyrm handler.
	 */
	public static Strykewyrms INSTANCE = new Strykewyrms();

	/**
	 * Gets the aforedeclared instance.
	 */
	public static Strykewyrms getInstance() {
		return INSTANCE;
	}

	/**
	 * A few final integers which handle the max hits.
	 */
	public final int jungle = 9, ice = 18, desert = 10;

	/**
	 * A method to handle the Desert strykewyrms.
	 * 
	 * @param attacker
	 * @param target
	 */
	public void desertWyrm(Entity attacker, Entity target) {
		if (target instanceof Player)
			if (target instanceof Player) {
				attacker.setCombatDelay(4);
				int damage = 0;
				if (Misc.random(5) == 1) {
					damage = MagicFormula.canMagicAttack(attacker, target) ? Misc
							.random(30) - 1 + 1 : 0;
					AnimationProcessor.addNewRequest(attacker, 12796, 0);
					AnimationProcessor.addNewRequest(attacker, 12791, 5);
					HitExecutor.addNewHit(attacker, target, CombatType.MAGIC,
							damage, 5);
				} else {
					damage = Infliction.canHitWithMelee(attacker, target) ? Misc
							.random(desert) - 1 + 1 : 0;
					AnimationProcessor.addNewRequest(attacker, 12791, 0);
					HitExecutor.addNewHit(attacker, target, CombatType.MELEE,
							damage, 0);
				}

			}

	}

	/**
	 * A method to handle the ice strykewyrms.
	 * 
	 * @param attacker
	 * @param target
	 */
	public void iceWyrm(Entity attacker, Entity target) {
		if (target instanceof Player)
			if (target instanceof Player) {
				attacker.setCombatDelay(4);
				int damage = 0;
				if (Misc.random(5) == 1) {
					damage = MagicFormula.canMagicAttack(attacker, target) ? Misc
							.random(30) - 1 + 1 : 0;
					AnimationProcessor.addNewRequest(attacker, 12796, 0);
					AnimationProcessor.addNewRequest(attacker, 12791, 5);
					HitExecutor.addNewHit(attacker, target, CombatType.MAGIC,
							damage, 5);
				} else {
					damage = Infliction.canHitWithMelee(attacker, target) ? Misc
							.random(ice) - 1 + 1 : 0;
					AnimationProcessor.addNewRequest(attacker, 12791, 0);
					HitExecutor.addNewHit(attacker, target, CombatType.MELEE,
							damage, 0);
				}

			}

	}

	/**
	 * A method to handle the Jungle strykewyrms.
	 * 
	 * @param attacker
	 * @param target
	 */
	public void jungleWyrm(Entity attacker, Entity target) {
		if (target instanceof Player)
			if (target instanceof Player) {
				attacker.setCombatDelay(4);
				int damage = 0;
				if (Misc.random(5) == 1) {
					damage = MagicFormula.canMagicAttack(attacker, target) ? Misc
							.random(30) - 1 + 1 : 0;
					AnimationProcessor.addNewRequest(attacker, 12796, 0);
					AnimationProcessor.addNewRequest(attacker, 12791, 5);
					HitExecutor.addNewHit(attacker, target, CombatType.MAGIC,
							damage, 5);
				} else {
					damage = Infliction.canHitWithMelee(attacker, target) ? Misc
							.random(jungle) - 1 + 1 : 0;
					AnimationProcessor.addNewRequest(attacker, 12791, 0);
					HitExecutor.addNewHit(attacker, target, CombatType.MELEE,
							damage, 0);
				}

			}

	}
}
