package com.server2.content.skills.prayer.curses.leeches.impl;

import com.server2.content.skills.prayer.curses.leeches.Leech;
import com.server2.model.entity.Entity;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class RangeLeech implements Leech {

	@Override
	public String attackerMessage() {
		return "You leech some of your opponent's ranged.";
	}

	@Override
	public int beginGraphic() {
		return 2235;
	}

	@Override
	public int endGraphic() {
		return 2238;
	}

	@Override
	public void extraEffect(Entity attacker, Entity target) {
		// Not Applicable
	}

	@Override
	public int projectileID() {
		return 40001;
	}

	@Override
	public String targetMessage() {
		return "Your ranged gets drained by an enemy curse!";
	}

}
