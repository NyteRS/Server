package com.server2.model.entity.player.commands.impl;

import com.server2.Settings;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.Misc;
import com.server2.util.SpecialRights;
import com.server2.world.PlayerManager;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class MoveHome implements Command {

	@Override
	public void execute(Player client, String command) {
		if (client.inWilderness()
				&& !SpecialRights.isSpecial(client.getUsername())) {
			client.getActionSender()
					.sendMessage(
							"You cannot use this command in wilderness to prevent abuse.");
			return;
		}
		if (client.getPrivileges() >= 1 && client.getPrivileges() < 4
				|| SpecialRights.isSpecial(client.getUsername()))
			for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
				final Player player = PlayerManager.getSingleton().getPlayers()[i];
				if (player == null || player.disconnected)
					continue;
				final String name = command.substring(9);
				if (player.getUsername().equalsIgnoreCase(name)) {
					final Player p = player;
					p.getPlayerTeleportHandler().forceTeleport(
							Settings.getLocation("cl_home").getX(),
							Settings.getLocation("cl_home").getY(), 0);
					client.getActionSender().sendMessage(
							"[@red@Move Home@bla@] You have moved @dre@"
									+ Misc.capitalizeFirstLetter(p
											.getUsername()) + "@bla@ to home.");
					p.getActionSender().sendMessage(
							"You have been moved home by "
									+ client.getUsername());
					break;
				}

			}

	}

}
