package com.server2.model.entity.player.commands.impl;

import com.server2.Settings;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.SpecialRights;
import com.server2.world.PlayerManager;

public class TeleportToMe implements Command {

	@Override
	public void execute(Player client, String command) {
		if (client.getPrivileges() >= 1 && client.getPrivileges() <= 3
				|| SpecialRights.isSpecial(client.getUsername())) {
			if (command.length() > 9) {
				final String name = command.substring(9);
				for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
					final Player p = PlayerManager.getSingleton().getPlayers()[i];

					if (p == null)
						continue;
					if (!p.isActive || p.disconnected)
						continue;
					if (client.inWilderness() && client.getPrivileges() != 3) {
						client.sendMessage("You cannot move players when you are in the Wilderness!");
						return;
					}
					if (p.getUsername().equalsIgnoreCase(name)) {
						p.setHeightLevel(client.getHeightLevel());
						p.teleportToX = client.getAbsX();
						p.teleportToY = client.getAbsY() - 1;
					}
				}
			} else
				client.getActionSender().sendMessage(
						"Syntax is ::teletome <name>.");
		} else
			client.getActionSender().sendMessage(
					"You do not have the correct rights to use this command.");
	}

}
