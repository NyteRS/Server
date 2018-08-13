package com.server2.content.skills.prayer.curses.deflection;

import com.server2.model.entity.Entity.CombatType;

/**
 * 
 * @author Rene Roosen
 * 
 */
public interface Deflection {

	// The animation ID to execute
	public abstract int animationID();

	// The combat type this curse protects for
	public abstract CombatType combatType();

	// The graphic to do
	public abstract int graphicID();
}
