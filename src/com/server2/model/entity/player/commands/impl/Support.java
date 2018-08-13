package com.server2.model.entity.player.commands.impl;

/**
 * @author Rene
 */

import com.server2.Settings;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.world.PlayerManager;

public class Support implements Command {

	@Override
	public void execute(Player client, String command) {
		String staffOnline = "Support online : @red@";
		for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
			if (PlayerManager.getSingleton().getPlayers()[i] == null)
				continue;

			final Player p = PlayerManager.getSingleton().getPlayers()[i];
			if (p.getPrivileges() == 8)
				staffOnline += p.getUsername() + ", ";
		}
		client.getActionSender().sendMessage(staffOnline);
		client.getActionSender().sendMessage(
				"@red@You can ask these members for help");
	}

}
