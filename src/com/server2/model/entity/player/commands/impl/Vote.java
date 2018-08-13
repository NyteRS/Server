package com.server2.model.entity.player.commands.impl;

import com.server2.Settings;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;

/**
 * 
 * @author Rene Roosen Handles the vote command.
 */
public class Vote implements Command {

	@Override
	public void execute(Player client, String command) {
		client.getActionSender().sendString(
				"www." + Settings.getString("sv_site") + "/vote.php", 0);
		client.getActionSender().sendMessage(
				"If the voting page didn't open, go to @red@"
						+ Settings.getString("sv_site") + "/vote.php@red@");
		client.getActionSender().sendMessage(
				"You can vote on each site, for each one you get an auth key");
		client.getActionSender()
				.sendMessage(
						"Each auth key is worth @red@ 1 @bla@ voting point and @red@ 1m@bla@ cash!");
		client.getActionSender().sendMessage(
				"Talk to Aemad for the voting store.");
	}

}
