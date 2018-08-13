package com.server2.model.entity.player.commands.impl;

import com.server2.Server;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.SpecialRights;

public class AddPrestige implements Command {

	@Override
	public void execute(Player client, String command) {
		try {
			final String[] arguments = command.split(" ");
			final String targetName = arguments[1].replace("-", " ");
			final Player target = Server.getPlayerManager().getPlayerByName(
					targetName);
			if (target != null && SpecialRights.isSpecial(client.getUsername())) {
				final int prestigeAmount = Integer.parseInt(arguments[2]);
				target.prestige = prestigeAmount;
				target.sendMessage("" + client.getUsername()
						+ " has increased your prestige by " + prestigeAmount
						+ "");
			} else
				client.getActionSender().sendMessage(
						"target is null or you cannot run this cmd.");
		} catch (final Exception e) {
			client.getActionSender().sendMessage(
					"invalid format! ::command player-name prestige-amount");
		}
	}

}
