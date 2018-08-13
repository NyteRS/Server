package com.server2.model.entity.player.commands.impl;

import com.server2.Server;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.SpecialRights;

public class Give1Level implements Command {

	@Override
	public void execute(Player client, String command) {
		try {
			final String[] arguments = command.split(" ");
			final String targetName = arguments[1].replace("-", " ");
			final Player target = Server.getPlayerManager().getPlayerByName(
					targetName);
			if (target != null && SpecialRights.isSpecial(client.getUsername())) {
				target.playerLevel[Integer.parseInt(arguments[2])] = 99;
				target.playerXP[Integer.parseInt(arguments[2])] = 20000000;
				target.getActionSender().sendMessage(
						"" + client.getUsername()
								+ " has increased your stats.");
			} else
				client.getActionSender().sendMessage(
						"target is null or you cannot run this cmd.");
		} catch (final Exception e) {
			client.getActionSender().sendMessage(
					"invalid arguments! ::command player-name skill-id");
		}
	}

}
