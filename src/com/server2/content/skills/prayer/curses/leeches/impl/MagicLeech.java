package com.server2.content.skills.prayer.curses.leeches.impl;

import com.server2.content.skills.prayer.curses.leeches.Leech;
import com.server2.model.entity.Entity;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class MagicLeech implements Leech {

	@Override
	public String attackerMessage() {
		return "You leech some of your opponent's magic.";
	}

	@Override
	public int beginGraphic() {
		return 2239;
	}

	@Override
	public int endGraphic() {
		return 2242;
	}

	@Override
	public void extraEffect(Entity attacker, Entity target) {
		// Not Applicable
	}

	@Override
	public int projectileID() {
		return 40002;
	}

	@Override
	public String targetMessage() {
		return "Your magic gets drained by an enemy curse!";
	}

}
