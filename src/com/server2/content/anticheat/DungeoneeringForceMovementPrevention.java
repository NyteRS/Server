package com.server2.content.anticheat;

import com.server2.model.entity.player.Player;
import com.server2.util.Areas;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class DungeoneeringForceMovementPrevention {

	/**
	 * Prevents people from dungeoneering on different height levels then their
	 * index allows
	 */
	public static boolean correctHeight(Player client) {
		if (System.currentTimeMillis() - client.lastDungEntry < 5000)
			return true;
		if (client.floor1() || Areas.bossRoom1(client.getPosition())) {
			if (client.getIndex() * 4 == client.getHeightLevel())
				return true;
		} else if (client.floor2()) {
			final int heightRequired = client.getIndex() * 4;
			final int heightRequired2 = client.getIndex() * 4 + 1;
			if (client.getHeightLevel() == heightRequired
					|| client.getHeightLevel() == heightRequired2)
				return true;
		} else if (client.floor3()) {
			final int heightRequired = client.getIndex() * 4 + 1;
			if (client.getHeightLevel() == heightRequired)
				return true;
		}

		return false;
	}
}
