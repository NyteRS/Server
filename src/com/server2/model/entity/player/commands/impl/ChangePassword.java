package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;

/**
 * Changing password command.
 * 
 * @author Graham
 */
public class ChangePassword implements Command {

	@Override
	public void execute(Player player, String command) {
		if (command.length() > 14) {
			player.setPassword(command.substring(15));
			player.getActionSender().sendMessage(
					"Your new pass is \"" + command.substring(15) + "\"");
		} else
			player.getActionSender().sendMessage(
					"Syntax is ::pass <new password>.");
	}

}