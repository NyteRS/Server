package com.server2.model.entity.player.commands.impl;

import com.server2.Server;
import com.server2.Settings;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;

public class Players implements Command {

	@Override
	public void execute(Player client, String command) {
		if (Server.getPlayerManager().getPlayerCount() == 1)
			client.getActionSender().sendMessage(
					"[@red@Players Online@bla@] There is currently @dre@"
							+ Server.getPlayerManager().getPlayerCount()
							+ "@bla@ players playing "
							+ Settings.getString("sv_name"));
		else
			client.getActionSender().sendMessage(
					"[@red@Players Online@bla@] There are currently @dre@"
							+ Server.getPlayerManager().getPlayerCount()
							+ "@bla@ players playing "
							+ Settings.getString("sv_name"));
	}
}
