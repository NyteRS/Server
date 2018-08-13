package com.server2.content.skills.prayer.curses.deflection.impl;

import com.server2.content.skills.prayer.curses.deflection.Deflection;
import com.server2.model.entity.Entity.CombatType;

/**
 * 
 * @author Rene Roosen
 * 
 */
public final class MagicDeflect implements Deflection {

	@Override
	public int animationID() {
		return 12573;
	}

	@Override
	public CombatType combatType() {
		return CombatType.MAGIC;
	}

	@Override
	public int graphicID() {
		return 2228;
	}

}
