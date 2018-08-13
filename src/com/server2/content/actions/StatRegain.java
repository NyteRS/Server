package com.server2.content.actions;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;

/**
 * 
 * @author Rene Roosen
 * 
 */

public class StatRegain {

	public static void init(Player client) {

		if (client.hitpoints == 0 || client.disconnected)
			return;
		for (int i = 0; i < PlayerConstants.MAX_SKILLS; i++) {
			if (i != 3 && i != 23)
				if (client.playerLevel[i] > client
						.getLevelForXP(client.playerXP[i]))
					client.playerLevel[i]--;
			if (i == 3) {

				if (client.hitpoints < client.calculateMaxHP()) {
					if (client.getPrayerHandler().clicked[6])
						client.getActionAssistant().addHP(1);
					if (client.getPrayerHandler().clicked[46])
						client.getActionAssistant().addHP(5);
					client.getActionAssistant().addHP(1);
				}

			} else if (client.playerLevel[i] < client
					.getLevelForXP(client.playerXP[i])) {
				if (i == 5)
					continue;
				client.playerLevel[i]++;
			}
			client.getActionAssistant().refreshSkill(i);

		}

	}

}
