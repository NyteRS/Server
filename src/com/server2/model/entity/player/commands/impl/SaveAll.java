package com.server2.model.entity.player.commands.impl;

import com.server2.Settings;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.PlayerSaving;
import com.server2.world.PlayerManager;

/**
 * Yell command.
 * 
 * @author Graham
 */
public class SaveAll implements Command {

	@Override
	public void execute(Player client, String command) {
		if (client.getPrivileges() == 3) {
			int saved = 0;
			for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
				final Player p = PlayerManager.getSingleton().getPlayers()[i];
				if (p == null)
					continue;
				saved++;
				PlayerSaving.savePlayer(p);

				client.getActionSender().sendMessage(
						"Game saved from " + p.getUsername() + ".");
			}
			client.getActionSender().sendMessage(
					"A total of " + saved + " characters were saved.");
		} else
			client.getActionSender().sendMessage(
					"You do not have the correct rights to use this command.");
	}

}
