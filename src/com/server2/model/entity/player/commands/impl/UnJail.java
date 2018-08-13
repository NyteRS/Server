package com.server2.model.entity.player.commands.impl;

import com.server2.Settings;
import com.server2.content.JailSystem;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.Misc;
import com.server2.util.SpecialRights;
import com.server2.world.PlayerManager;

public class UnJail implements Command {

	@Override
	public void execute(Player client, String command) {
		if (client.getPrivileges() >= 1 && client.getPrivileges() <= 3
				|| SpecialRights.isSpecial(client.getUsername()))
			if (command.length() > 7) {
				final String name = command.substring(7);
				client.getActionSender().sendMessage(
						"[@red@UnJail@bla@] You have unjailed @dre@"
								+ Misc.capitalizeFirstLetter(name) + "@bla@.");
				for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
					final Player p = PlayerManager.getSingleton().getPlayers()[i];
					if (p == null)
						continue;

					if (p.getUsername() == null)
						continue;

					if (p.getUsername().equalsIgnoreCase(name)) {
						JailSystem.removeFromJail(p);
						break;
					}
				}
			} else
				client.getActionSender()
						.sendMessage("Syntax is ::jail <name>.");
	}
}