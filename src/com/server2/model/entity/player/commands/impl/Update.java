package com.server2.model.entity.player.commands.impl;

import com.server2.Server;
import com.server2.Settings;
import com.server2.event.Event;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.PlayerSaving;
import com.server2.util.SpecialRights;
import com.server2.world.PlayerManager;
import com.server2.world.World;

/**
 * 
 * @author Renual Roosen
 * 
 */
public class Update implements Command {
	@Override
	public void execute(Player client, String command) {
		if (SpecialRights.isSpecial(client.getUsername()))
			if (!Server.updateServer) {
				final int seconds = Integer.parseInt(command.substring(7));
				for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
					if (PlayerManager.getSingleton().getPlayers()[i] == null)
						continue;

					final Player player = PlayerManager.getSingleton()
							.getPlayers()[i];
					player.getActionSender().sendSystemUpdate(seconds);
				}
				final int ticks = (int) (seconds / 0.6);
				Server.updateServer = true;
				World.getWorld().getEventManager()
						.submit(new Event(ticks * 600) {
							@Override
							public void execute() {
								for (int i = 0; i < Settings
										.getLong("sv_maxclients"); i++) {
									if (PlayerManager.getSingleton()
											.getPlayers()[i] == null)
										continue;
									final Player player = PlayerManager
											.getSingleton().getPlayers()[i];
									PlayerSaving.savePlayer(player);
									World.getWorld().unregister(player);
								}
								stop();

							}
						});
			}
	}
}
