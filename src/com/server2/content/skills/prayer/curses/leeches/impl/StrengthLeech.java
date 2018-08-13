package com.server2.content.skills.prayer.curses.leeches.impl;

import com.server2.content.skills.prayer.curses.leeches.Leech;
import com.server2.model.entity.Entity;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class StrengthLeech implements Leech {

	@Override
	public String attackerMessage() {
		return "You leech some of your opponent's strength.";
	}

	@Override
	public int beginGraphic() {
		return 2247;
	}

	@Override
	public int endGraphic() {
		return 2250;
	}

	@Override
	public void extraEffect(Entity attacker, Entity target) {
		// Not applicable
	}

	@Override
	public int projectileID() {
		return 40004;
	}

	@Override
	public String targetMessage() {
		return "Your strength gets drained by an enemy curse!";
	}

}
