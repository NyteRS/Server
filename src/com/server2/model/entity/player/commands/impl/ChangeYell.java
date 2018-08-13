package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;

/**
 * 
 * @author Rene
 * 
 */
public class ChangeYell implements Command {

	@Override
	public void execute(Player client, String command) {
		client.displayYell = !client.displayYell;
		if (client.displayYell)
			client.getActionSender().sendMessage(
					"[@red@Toggle Yell@bla@] Yells will now be displayed.");
		else
			client.getActionSender().sendMessage(
					"[@red@Toggle Yell@bla@] Yells will now be hidden.");

	}

}
