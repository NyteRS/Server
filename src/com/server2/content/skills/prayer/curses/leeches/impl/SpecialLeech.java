package com.server2.content.skills.prayer.curses.leeches.impl;

import com.server2.content.skills.prayer.curses.leeches.Leech;
import com.server2.model.combat.additions.Specials;
import com.server2.model.entity.Entity;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class SpecialLeech implements Leech {

	@Override
	public String attackerMessage() {
		return "You leech some of your opponent's special.";
	}

	@Override
	public int beginGraphic() {
		return 2255;
	}

	@Override
	public int endGraphic() {
		return 2258;
	}

	@Override
	public void extraEffect(Entity attacker, Entity target) {
		if (target instanceof Player) {
			((Player) target).setSpecialAmount(((Player) target)
					.getSpecialAmount() - 10);
			((Player) attacker).setSpecialAmount(((Player) attacker)
					.getSpecialAmount() + 10);
			Specials.updateSpecialBar((Player) attacker);
			Specials.updateSpecialBar((Player) target);
		}
	}

	@Override
	public int projectileID() {
		return 40005;
	}

	@Override
	public String targetMessage() {
		return "Your special gets drained by an enemy curse!";
	}

}
