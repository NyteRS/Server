package com.server2.content.skills.prayer.curses;

import com.server2.content.skills.prayer.PrayerHandler;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;
import com.server2.world.map.tile.TileManager;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class Wrath {

	public static void appendWrath(Player death) {
		if (death.getPrayerHandler().clicked[PrayerHandler.WRATH]) {
			GraphicsProcessor.addNewRequest(death, 2259, 100, 0);
			if (death.getTarget() != null
					&& TileManager.calculateDistance(death.getTarget(), death) < 3)
				if (death.getTarget() instanceof Player)
					((Player) death.getTarget()).getActionAssistant()
							.appendHit(death, CombatType.RECOIL,
									Misc.random(16));
		}
	}
}
