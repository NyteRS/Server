package com.server2.model.entity.player.commands.impl;

import com.server2.Settings;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.SpecialRights;
import com.server2.world.PlayerManager;

/**
 * 
 * @author Lukas
 * 
 */
public class GiveDonator implements Command {

	@Override
	public void execute(Player client, String command) {
		if (SpecialRights.isSpecial(client.getUsername()))
			try {
				if (command.length() > 12) {
					final String name = command.substring(12);

					for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
						final Player p = PlayerManager.getSingleton()
								.getPlayers()[i];
						if (p == null)
							continue;
						if (!p.isActive || p.disconnected)
							continue;
						if (p.getUsername().equalsIgnoreCase(name)) {
							p.setPrivileges(7);
							p.donatorRights = 1;
							p.saved = false;
						}
					}
					client.getActionSender()
							.sendMessage("Could not find user.");
				}
			} catch (final NumberFormatException exception) {
				client.getActionSender().sendMessage(
						"Syntax is ::setrank <name> <rankid>");
			}
	}
}
