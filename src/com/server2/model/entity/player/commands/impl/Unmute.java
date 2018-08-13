package com.server2.model.entity.player.commands.impl;

import com.server2.Settings;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.Misc;
import com.server2.util.SpecialRights;
import com.server2.world.PlayerManager;
import com.server2.world.World;

public class Unmute implements Command {

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
					final Player player = p;
					if (p.getUsername().equalsIgnoreCase(name))
						player.setMuted(false);
				}
				try {
					World.getGameDatabase().executeQuery(
							"delete from badplayers where username = '" + name
									+ "' and type = 2");
					client.getActionSender().sendMessage(
							"[@red@UnMute@bla@] You have unmuted @dre@"
									+ Misc.capitalizeFirstLetter(name)
									+ "@bla@.");
				} catch (final Exception e) {
					client.getActionSender().sendMessage(
							"Error processing unmute for " + name + ".");
				}
			} else
				client.getActionSender()
						.sendMessage("Syntax is ::mute <name>.");
	}
}
