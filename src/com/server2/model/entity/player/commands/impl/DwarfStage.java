package com.server2.model.entity.player.commands.impl;

import com.server2.Settings;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.Misc;
import com.server2.util.SpecialRights;
import com.server2.world.PlayerManager;

/**
 * 
 * @author Jordon Barber Command used to set a users stage on the Dwarf Cannon
 *         quest.
 * 
 */
public class DwarfStage implements Command {

	@Override
	public void execute(Player client, String command) {
		if (SpecialRights.isSpecial(client.getUsername()))
			try {
				if (command.length() > 11) {
					final String str = command.substring(4);
					final String[] args = str.split(" ");

					String name = args[1];
					for (int i = 2; i < args.length - 1; i++)
						name = name + " " + args[i];
					final int stage = Integer.valueOf(args[args.length - 1]);
					for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
						final Player p = PlayerManager.getSingleton()
								.getPlayers()[i];
						if (p == null)
							continue;
						if (!p.isActive || p.disconnected)
							continue;
						if (p.getUsername().equalsIgnoreCase(name)) {
							p.dwarfStage = stage;
							client.getActionSender()
									.sendMessage(
											"[@red@Dwarf Stage@bla@] @dre@"
													+ Misc.capitalizeFirstLetter(name)
													+ "@bla@'s stage has been changed to @dre@"
													+ stage + "@bla@!");
							return;
						}
					}
					client.getActionSender().sendMessage(
							"You are currently on stage " + client.dwarfStage
									+ " ");
				}
			} catch (final NumberFormatException exception) {
				client.getActionSender()
						.sendMessage(
								"You are currently on stage "
										+ client.dwarfStage + " ");
			}
	}
}
