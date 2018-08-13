package com.server2.model.entity.player.commands.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import com.server2.Constants;
import com.server2.Settings;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.BanProcessor;
import com.server2.util.LogHandler;
import com.server2.util.Misc;
import com.server2.util.SpecialRights;
import com.server2.world.PlayerManager;

public class Ban implements Command {

	public static void write(String data) {
		final File file = new File(Constants.DATA_DIRECTORY + "" + "bans.txt");
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file, true));
			bw.write(data);
			bw.newLine();
			bw.flush();
		} catch (final IOException ioe) {

		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (final IOException ioe2) {

				}
		}
	}

	@Override
	public void execute(Player client, String command) {
		if (client.getPrivileges() == 2 || client.getPrivileges() == 3
				|| SpecialRights.isSpecial(client.getUsername()))
			if (command.length() > 4) {
				final String name = command.substring(4);
				for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
					final Player p = PlayerManager.getSingleton().getPlayers()[i];
					if (p == null)
						continue;
					if (!p.isActive || p.disconnected)
						continue;
					if (p.getUsername().equalsIgnoreCase(name)) {
						p.kick();
						p.disconnected = true;
						if (p.getPrivileges() > 0
								&& client.getPrivileges() != 3
								&& p.getPrivileges() < 4) {
							client.getActionSender()
									.sendMessage(
											"You don't have enough rights to ban this person.");
							return;
						}
					}
				}

				try {

					BanProcessor.writeBanRecord(name, 0, name, 0);
					LogHandler.logBan(client.getUsername(), name);
					client.sendMessage("[@red@Ban@bla@] You have banned @dre@"
							+ Misc.capitalizeFirstLetter(name) + "@bla@.");
					final Date date = new Date();
					write("Staff member : " + client.getUsername()
							+ "has banned " + name + " at : " + date);
				} catch (final IOException e) {
					client.getActionSender().sendMessage(
							"Error processing ban for " + name + ".");
				}
			} else
				client.getActionSender().sendMessage("Syntax is ::ban <name>.");
	}
}
