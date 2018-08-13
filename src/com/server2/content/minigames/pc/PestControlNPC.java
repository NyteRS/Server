package com.server2.content.minigames.pc;

import java.util.HashMap;
import java.util.Map;

import com.server2.InstanceDistributor;
import com.server2.model.combat.HitExecutor;
import com.server2.model.combat.additions.Infliction;
import com.server2.model.combat.magic.MagicFormula;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Location;
import com.server2.model.entity.Projectile;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.npc.NPCAttacks;
import com.server2.model.entity.npc.NPCDefinition;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;

/**
 * Represents a Pest control NPC.
 * 
 * @author Ultimate1 322 973 285 http://hell-army.clan.su/forum/9-4-1
 * 
 */

public class PestControlNPC extends NPC {

	/**
	 * The type of NPC
	 */
	// private NPCType type;

	private static class Attack {

		private static Map<NPCType, Attack> attacks = new HashMap<NPCType, Attack>();

		public static Attack forNPCType(NPCType type) {
			return attacks.get(type);
		}

		public static void init() {
			attacks.put(NPCType.DEFILER, new Attack(3920, 657, 15,
					CombatType.RANGE, 6));
			attacks.put(NPCType.BRAWLER, new Attack(3897, 1, CombatType.MELEE,
					5));
			attacks.put(NPCType.SHIFTER, new Attack(3901, 1, CombatType.MELEE,
					5));
			attacks.put(NPCType.SPINNER, new Attack(3908, 1, CombatType.MELEE,
					4));
			attacks.put(NPCType.TORCHER, new Attack(8234, 647, 15,
					CombatType.MAGIC, 5));
			// TODO find Ravager and Torcher anims
		}

		private final int animation;
		private final int startProjectile;
		private final int distanceRequired;

		private final CombatType combatType;

		private final int attackSpeed;

		public Attack(int animation, int distanceRequired,
				CombatType combatType, int attackDelay) {
			this(animation, -1, distanceRequired, combatType, attackDelay);
		}

		public Attack(int animation, int startProjectile, int distanceRequired,
				CombatType combatType, int attackDelay) {
			this.animation = animation;
			this.startProjectile = startProjectile;
			this.distanceRequired = distanceRequired;
			this.combatType = combatType;
			attackSpeed = attackDelay;
		}

	}

	/**
	 * Types of npcs
	 */
	private enum NPCType {
		DEFILER, SPINNER, RAVENGER, TORCHER, BRAWLER, SHIFTER
	}

	/**
	 * Calculates the damage the entity should receive.
	 * 
	 * @param npc
	 * @param victim
	 * @return
	 */
	private static int calculateDamage(NPC npc, Entity victim) {
		int returnValue = Misc.random(NPCAttacks.npcArray[npc.getDefinition()
				.getType()][1]);
		final CombatType combatType = npc.getCombatType();
		if (victim instanceof Player)
			if (combatType == CombatType.MELEE) {
				if (((Player) victim).protectingMelee())
					returnValue /= 2;
			} else if (combatType == CombatType.RANGE) {
				if (((Player) victim).protectingRange())
					returnValue /= 2;
			} else if (combatType == CombatType.MAGIC)
				if (((Player) victim).protectingMagic())
					returnValue /= 2;
		return returnValue;
	}

	/**
	 * Gets the attacks for the type of npc.
	 * 
	 * @param npcName
	 * @return
	 */
	private static Attack getAttackForType(String npcName) {
		NPCType npcType = null;
		for (final NPCType t : NPCType.values()) {
			final String name = t.toString().toLowerCase();
			if (name.contains(npcName)) {
				npcType = t;
				break;
			}
		}
		return Attack.forNPCType(npcType);
	}

	/**
	 * Handles the attack of a pest control npc.
	 * 
	 * @param npc
	 * @param victim
	 *            TODO make this more generic (use for adding other npcs)!
	 */
	public static boolean handleAttack(Entity attacker, Entity victim) {
		if (attacker instanceof Player)
			return false;
		final NPC npc = (NPC) attacker;
		final NPCDefinition def = npc.getDefinition();
		if (def == null)
			return false;
		final String name = def.getName().toLowerCase();
		if (name.contains("defiler") || name.contains("spinner")
				|| name.contains("splatter") || name.contains("ravager")) {
			final Attack attack = getAttackForType(name);
			if (attack != null) {
				int damage = 0;
				final int distance = Misc.getDistance(npc.getPosition(),
						victim.getPosition());
				AnimationProcessor.addNewRequest(attacker, attack.animation, 0);
				switch (attack.combatType) {
				case MELEE:
					if (Infliction.canHitWithMelee(attacker, victim))
						damage = Misc.random(calculateDamage(npc, victim));
					HitExecutor.addNewHit(attacker, victim, attack.combatType,
							damage, 1);
					break;
				case RANGE:
				case MAGIC:
					if (distance <= attack.distanceRequired) {
						if (attack.combatType.equals(CombatType.RANGE)
								&& Infliction
										.canHitWithRanged(attacker, victim))
							damage = Misc.random(calculateDamage(npc, victim));
						else if (attack.combatType.equals(CombatType.MAGIC)
								&& MagicFormula
										.canMagicAttack(attacker, victim))
							damage = Misc.random(calculateDamage(npc, victim));
						final int ticks = Misc.calculateDamageDelay(
								npc.getPosition(), victim.getPosition(),
								attack.combatType);
						final Projectile projectile = new Projectile(
								attack.startProjectile, 41, 41, 37, -1, 15)
								.set(attacker, victim);
						InstanceDistributor.getGlobalActions().sendProjectile(
								projectile);
						HitExecutor.addNewHit(attacker, victim,
								attack.combatType, damage, ticks - 1);
					}
					break;
				default:
					break;
				}
				npc.setCombatDelay(attack.attackSpeed);
				return true;
			}
		}
		return false;
	}

	/**
	 * Initialise NPC attacks.
	 */
	public static void init() {
		Attack.init();
	}

	/**
	 * Initialise the NPC.
	 * 
	 * @param definition
	 * @param location
	 */
	public PestControlNPC(NPCDefinition definition, Location location) {
		super(definition, location);
	}

	/**
	 * Get the npcs health.
	 * 
	 * @param npc
	 * @return a formatted version of the NPC's health.
	 */
	public String formatHealth() {
		return hp < maxHP / 2 ? "@red@" + hp : "@gre@" + hp;
	}

}