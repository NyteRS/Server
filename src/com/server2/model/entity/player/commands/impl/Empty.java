package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class Empty implements Command {

	@Override
	public void execute(Player client, String command) {
		if (client.isDead())
			return;
		if (client.inWilderness()) {
			client.getActionSender().sendMessage(
					"You cannot do this in the wilderness!");
			return;
		}
		for (int i = 0; i < client.playerItems.length; i++)
			client.playerItems[i] = 0;
		for (int i = 0; i < client.playerItemsN.length; i++)
			client.playerItemsN[i] = 0;
		client.getActionSender().sendItemReset(3214);
		client.getActionSender().sendMessage(
				"[@red@Empty@bla@] You've succesfully emptied your inventory.");
	}

}
