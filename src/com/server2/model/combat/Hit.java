package com.server2.model.combat;

import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;

/**
 * 
 * @author Rene Represents a single hit
 */

public class Hit {

	/**
	 * The attacker
	 */
	private Entity attacker;

	/**
	 * The victim
	 */
	private Entity victim;

	/**
	 * The CombatType
	 */
	private CombatType combatType;

	/**
	 * The damage dealt
	 */
	private int damage;

	/**
	 * The delay
	 */
	private int delay;

	/**
	 * The flag
	 */
	private boolean flag = false;

	/**
	 * Constructs a new hit
	 * 
	 * @param attacker
	 *            - the attacker
	 * @param victim
	 *            - the victim
	 * @param combatType
	 *            - the combattype of this hit
	 * @param damage
	 *            - the amount of damage
	 * @param delay
	 *            - the delay in ticks
	 */
	public Hit(Entity attacker, Entity victim, CombatType combatType,
			int damage, int delay) {
		this.attacker = attacker;
		this.victim = victim;
		this.combatType = combatType;
		this.damage = damage;
		this.delay = delay;
	}

	/**
	 * Constructs a new hit
	 * 
	 * @param attacker
	 * @param victim
	 * @param combatType
	 * @param damage
	 * @param delay
	 * @param flag
	 */
	public Hit(Entity attacker, Entity victim, CombatType combatType,
			int damage, int delay, boolean flag) {
		this.attacker = attacker;
		this.victim = victim;
		this.combatType = combatType;
		this.damage = damage;
		this.delay = delay;
		this.flag = flag;
	}

	/**
	 * Get the attacker
	 * 
	 * @return
	 */
	public Entity getAttacker() {
		return attacker;
	}

	/**
	 * Get the combat type
	 * 
	 * @return
	 */
	public CombatType getCombatType() {
		return combatType;
	}

	/**
	 * Get the damage
	 * 
	 * @return
	 */
	public int getDamage() {
		return damage;
	}

	/**
	 * Gets the delay
	 * 
	 * @return
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * Returns the flag value
	 * 
	 * @return
	 */
	public boolean getFlag() {
		return flag;
	}

	/**
	 * Get the victim
	 * 
	 * @return
	 */
	public Entity getVictim() {
		return victim;
	}

	/**
	 * Set the attacker
	 * 
	 * @param attacker
	 */
	public void setAttacker(Entity attacker) {
		this.attacker = attacker;
	}

	/**
	 * Set the combat type
	 * 
	 * @param combatType
	 */
	public void setCombatType(CombatType combatType) {
		this.combatType = combatType;
	}

	/**
	 * Set the damage
	 * 
	 * @param damage
	 */
	public void setDamage(int damage) {
		this.damage = damage;
	}

	/**
	 * Sets the delay.
	 * 
	 * @param delay
	 */
	public void setDelay(int delay) {
		this.delay = delay;
	}

	/**
	 * Set the victim
	 * 
	 * @param victim
	 */
	public void setVictim(Entity victim) {
		this.victim = victim;
	}

}
