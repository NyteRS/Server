package com.server2.content.skills.herblore;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class PotionResetting {

	public static void resetPotions(Player client) {
		if (client.inWilderness())
			for (int i = 0; i < 7; i++)
				if ((client.getLevelForXP(client.playerXP[i]) + 4) * 1.15 < client.playerLevel[i]) {
					if (i == 3)
						continue;
					if (i == 1
							&& System.currentTimeMillis() - client.lastBrew < 180000)
						continue;
					client.playerLevel[i] = (int) ((client
							.getLevelForXP(client.playerXP[i]) + 4) * 1.15);
					client.getActionSender()
							.sendMessage(
									"Your extreme "
											+ PlayerConstants.SKILL_NAMES[i]
											+ "  potion was reduced as you entered the wilderness.");
					client.getActionAssistant().refreshSkill(i);
				}
	}

}
