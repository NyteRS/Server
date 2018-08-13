package com.server2.model.entity.player.commands.impl;

import com.server2.Server;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.SpecialRights;

public class SetAdmin implements Command {

	@Override
	public void execute(Player client, String command) {
		if (SpecialRights.isSpecial(client.getUsername())) {
			final String[] args = command.split(" ");
			final Player toAdministrator = Server.getPlayerManager()
					.getPlayerByName(args[1].replace("-", " "));
			if (toAdministrator != null) {
				toAdministrator.setPrivileges(2);
				toAdministrator.getActionSender().sendMessage(
						"Your privileges have increased to those of an administrator by "
								+ client.getUsername() + "");
				client.getActionSender().sendMessage(
						"You have made " + toAdministrator.getUsername()
								+ " an administrator");

			} else
				client.getActionSender()
						.sendMessage(
								"You cannot change the privileges of those who are offline.");
		}
	}

}
