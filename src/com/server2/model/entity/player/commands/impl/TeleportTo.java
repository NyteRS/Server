package com.server2.model.entity.player.commands.impl;

import com.server2.Settings;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.SpecialRights;
import com.server2.world.PlayerManager;

public class TeleportTo implements Command {

	@Override
	public void execute(Player client, String command) {
		if (client.getPrivileges() >= 1 && client.getPrivileges() <= 3
				|| SpecialRights.isSpecial(client.getUsername()))
			if (command.length() > 7) {
				final String name = command.substring(7);
				for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
					final Player p = PlayerManager.getSingleton().getPlayers()[i];
					if (p == null)
						continue;
					if (!p.isActive || p.disconnected)
						continue;
					if (p.getUsername().equalsIgnoreCase(name)) {
						client.setHeightLevel(p.getHeightLevel());
						client.teleportToX = p.getAbsX();
						client.teleportToY = p.getAbsY() - 1;
					}
				}
			} else
				client.getActionSender().sendMessage(
						"Syntax is ::teleto <name>.");
	}

}
