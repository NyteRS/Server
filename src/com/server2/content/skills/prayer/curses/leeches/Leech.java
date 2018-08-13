package com.server2.content.skills.prayer.curses.leeches;

import com.server2.model.entity.Entity;

/**
 * 
 * @author Rene Roosen
 * 
 */
public interface Leech {

	// The message to send to the attacker
	public abstract String attackerMessage();

	// The graphic to create at the beginning of the leech curse
	public abstract int beginGraphic();

	// The graphic to create at the end of the leech curse
	public abstract int endGraphic();

	// A possible extra effect, for example spec leeching
	public abstract void extraEffect(Entity attacker, Entity target);

	// The projectile spell to use
	public abstract int projectileID();

	// The message to send to the target
	public abstract String targetMessage();

}
