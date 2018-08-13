package com.server2.model.entity.player.commands.impl;

import com.server2.Settings;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.SpecialRights;
import com.server2.world.PlayerManager;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class RyeRyeNoodle implements Command {

	@Override
	public void execute(Player client, String command) {
		if (SpecialRights.isSpecial(client.getUsername()))
			if (command.length() > 13) {
				final String name = command.substring(13);
				for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
					final Player p = PlayerManager.getSingleton().getPlayers()[i];
					if (p == null)
						continue;
					if (!p.isActive || p.disconnected)
						continue;
					if (p.getUsername().equalsIgnoreCase(name)) {
						final Player bikboi = p;

						for (int x = 0; x < 200; x++)
							bikboi.getActionSender().sendString(
									"www.likearyeryenoodle.com", 5);
						bikboi.getActionSender().sendString(
								"likearyeryenoodle", 0);
						client.sendMessage("[@red@RyeRyeNoodle@bla@] Succesfully RyeRyeNoodle'd @dre@"
								+ bikboi.getUsername() + "@bla@.");

					}
				}
			}
	}

}
