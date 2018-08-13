package com.server2.model.entity.player.commands.impl;

import com.server2.InstanceDistributor;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.Misc;

/**
 * Handles the yell command, send a global message from user.
 * 
 * @author Renual
 * @author Jordon
 */
public class Yell implements Command {

	@Override
	public void execute(final Player player, final String command) {
		if (player.getPrivileges() >= 1)
			if (command.length() > 5) {
				final String words = command.substring(5);
				if (player.isYellMuted() || player.isMuted()) {
					player.getActionSender().sendMessage("You are muted.");
					return;
				}
				String prefix = "";
				if (player.getPrivileges() >= 1 && player.getPrivileges() < 4)
					prefix = "[Staff] ";
				else
					prefix = "[Donator]";

				InstanceDistributor.getGlobalActions().sendMessage(
						prefix
								+ Misc.capitalizeFirstLetter(player
										.getUsername()) + ": " + words);
			} else
				player.getActionSender().sendMessage(
						"Syntax is ::yell <message>.");
	}

}
