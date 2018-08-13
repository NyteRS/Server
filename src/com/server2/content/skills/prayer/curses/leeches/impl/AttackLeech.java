package com.server2.content.skills.prayer.curses.leeches.impl;

import com.server2.content.skills.prayer.curses.leeches.Leech;
import com.server2.model.entity.Entity;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class AttackLeech implements Leech {

	@Override
	public String attackerMessage() {
		return "You leech some of your opponent's attack.";
	}

	@Override
	public int beginGraphic() {
		return 2234;
	}

	@Override
	public int endGraphic() {
		return 2233;
	}

	@Override
	public void extraEffect(Entity attacker, Entity target) {
		// Not applicable
	}

	@Override
	public int projectileID() {
		return 40000;
	}

	@Override
	public String targetMessage() {
		return "Your attack gets drained by an enemy curse!";
	}

}
