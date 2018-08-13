package com.server2.content.actions;

import com.server2.content.minigames.FightPits;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class Skulling {

	/**
	 * Handles the skulling of a specific player.
	 * 
	 * @param client
	 */
	public static void init(Player client) {

		if (client.getSkullTimer() < 1)
			client.skullIcon = -1;
		else {
			client.setSkullTimer(client.getSkullTimer() - 1);
			client.skullIcon = 0;
		}
		if (FightPits.inFightArea(client) || FightPits.inWaitingArea(client))
			client.skullIcon = 1;
		if (client.isViewingOrb())
			client.skullIcon = -1;

	}

	public static void setSkulled(Player client, Player attacked) {
		if (attacked.skulledOn != client.getIndex()) {
			client.setSkullTimer(1200);
			client.skulledOn = attacked.getIndex();
			if (FightPits.inFightArea(client)
					|| FightPits.inWaitingArea(client))
				client.skullIcon = 1;
			else
				client.skullIcon = 0;
		}
	}

}
