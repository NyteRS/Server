package com.server2.model.entity.player.commands.impl;

import com.server2.Server;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.Misc;
import com.server2.util.SpecialRights;

public class Kick implements Command {

	@Override
	public void execute(Player client, String command) {
		if (client.getPrivileges() == 2 || client.getPrivileges() == 3
				|| client.getPrivileges() == 1
				|| SpecialRights.isSpecial(client.getUsername()))
			if (command.length() > 5) {
				final String name = command.substring(5);
				Server.getPlayerManager().kick(name);
				client.getActionSender().sendMessage(
						"[@red@Kick@bla@] You have kicked @dre@"
								+ Misc.capitalizeFirstLetter(name) + "@bla@.");
			} else
				client.getActionSender()
						.sendMessage("Syntax is ::kick <name>.");
	}

}
