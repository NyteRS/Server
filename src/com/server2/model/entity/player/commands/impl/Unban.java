package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.Misc;
import com.server2.util.SpecialRights;
import com.server2.world.World;

public class Unban implements Command {

	@Override
	public void execute(Player client, String command) {
		if (client.getPrivileges() > 1 && client.getPrivileges() <= 3
				|| SpecialRights.isSpecial(client.getUsername()))
			if (command.length() > 6) {
				final String name = command.substring(6);
				try {
					World.getGameDatabase().executeQuery(
							"delete from badplayers where username = '" + name
									+ "' and type = 0");
					client.getActionSender().sendMessage(
							"[@red@UnBan@bla@] You have unbanned @dre@"
									+ Misc.capitalizeFirstLetter(name)
									+ "@bla@.");
				} catch (final Exception e) {
					client.getActionSender()
							.sendMessage(
									"Error processing release of ban for "
											+ name + ".");
					e.printStackTrace();
				}
			} else
				client.getActionSender().sendMessage(
						"Syntax is ::unban <name>.");
	}
}
