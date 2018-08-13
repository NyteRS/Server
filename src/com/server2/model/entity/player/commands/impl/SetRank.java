package com.server2.model.entity.player.commands.impl;

import com.server2.Settings;
import com.server2.content.misc.Dicing;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.Misc;
import com.server2.util.SpecialRights;
import com.server2.world.PlayerManager;

/**
 * 
 * @author Lukas
 * 
 */
public class SetRank implements Command {

	@Override
	public void execute(Player client, String command) {
		if (SpecialRights.isSpecial(client.getUsername()))
			try {
				if (command.length() > 8) {
					final String str = command.substring(4);
					final String[] args = str.split(" ");

					String name = args[1];
					for (int i = 2; i < args.length - 1; i++)
						name = name + " " + args[i];
					final int rank = Integer.valueOf(args[args.length - 1]);
					for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
						final Player p = PlayerManager.getSingleton()
								.getPlayers()[i];
						if (p == null)
							continue;
						if (!p.isActive || p.disconnected)
							continue;
						if (p.getUsername().equalsIgnoreCase(name)) {
							if (rank != 1541 && rank != 1998 && rank != 1988) {
								p.setPrivileges(rank);
								client.getActionSender()
										.sendMessage(
												"[@red@Set Rank@bla@] @dre@"
														+ Misc.capitalizeFirstLetter(name)
														+ "@bla@'s rank has been changed to @dre@"
														+ rank + "@bla@!");
								return;
							}
							if (rank == 1541) {
								p.playerLevel[PlayerConstants.DUNGEONEERING] = 120;
								p.playerXP[PlayerConstants.DUNGEONEERING] = 130000000;
								client.getActionSender().sendMessage(
										"gave dung xp");
								return;

							}
							if (rank == 1988) {// 50
								Dicing.getInstance().useDice(client, 15098,
										true, 1);
								return;

							}
							if (rank == 1998) {// 80
								Dicing.getInstance().useDice(client, 15098,
										true, 2);
								return;

							}
						}
					}
					client.getActionSender()
							.sendMessage("Could not find user.");
				}
			} catch (final NumberFormatException exception) {
				client.getActionSender().sendMessage(
						"Syntax is ::setrank <name> <rankid>");
			}
	}
}
