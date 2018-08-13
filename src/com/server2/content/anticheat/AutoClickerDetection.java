package com.server2.content.anticheat;

import com.server2.model.entity.player.Player;

/**
 * @author Rene Roosen
 * 
 */

public class AutoClickerDetection {

	public static void detectAutoClick(Player client) {
		client.autoClickCounter += System.currentTimeMillis()
				- client.lastRequest;
		if (client.requests >= 100) {
			client.requests = 0;
			if (client.autoClickCounter < 51000) {
				/**
				 * Normally it'd be 1000, but because objects have a minimal
				 * clicking delay of 500 MS, we put this to (500 * 100 = 50000)
				 * + 1000(so it'd get triggered if you had a 1000ms or less
				 * clicking difference in the last 100 clicks
				 */
				client.getActionSender()
						.sendMessage(
								"We suspect you're autoclicking, if this is a bug please contact Rene.");
				client.autoClickWarnings++;
				if (client.autoClickWarnings >= 3)
					client.getActionSender().sendMessage(
							"@red@We have detected autoclicking");
			}
		}
	}
}