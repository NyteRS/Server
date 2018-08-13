package com.server2.model.entity.player.commands.impl;

import com.server2.Settings;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.Misc;
import com.server2.util.SpecialRights;
import com.server2.world.PlayerManager;

/**
 * 
 * @author Jordon Barber
 * 
 */

public class CheckIP implements Command {

	@Override
	public void execute(Player client, String command) {
		if (client.getPrivileges() >= 2 && client.getPrivileges() <= 3
				|| SpecialRights.isSpecial(client.getUsername())) {
			if (command.length() > 8) {
				final String name = command.substring(8);
				for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
					final Player p = PlayerManager.getSingleton().getPlayers()[i];
					if (p == null)
						continue;
					if (p.getUsername().equalsIgnoreCase(name)) {
						client.getActionSender().sendMessage(
								"[@red@CheckIP@bla@] @dre@"
										+ Misc.capitalizeFirstLetter(name)
										+ "@bla@'s IP address is: @dre@"
										+ p.connectedFrom + "");
						break;
					}
				}

			} else
				client.getActionSender().sendMessage(
						"Syntax is ::checkip <name>.");
		} else
			client.getActionSender()
					.sendMessage("You cannot use this command!");
	}
}
