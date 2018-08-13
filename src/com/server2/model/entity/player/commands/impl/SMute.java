package com.server2.model.entity.player.commands.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Date;

import com.server2.Constants;
import com.server2.Settings;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.BanProcessor;
import com.server2.util.LogHandler;
import com.server2.world.PlayerManager;
import com.server2.world.World;

public class SMute implements Command {

	private static Mute mute = new Mute();

	public static Mute getDinosaur() {
		return mute;
	}

	public static void write(String data) {
		final File file = new File(Constants.DATA_DIRECTORY + "" + "mutes.txt");
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
		if (client.getPrivileges() >= 1 && client.getPrivileges() <= 3
				|| client.getUsername().equalsIgnoreCase("hustle"))
			if (command.length() > 5) {
				final String name = command.substring(6);
				for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
					final Player p = PlayerManager.getSingleton().getPlayers()[i];

					if (p == null)
						continue;
					if (!p.isActive || p.disconnected)
						continue;

					if (p.getUsername() == name) {
						if (client.getPrivileges() == 1
								&& p.getPrivileges() >= 1
								&& p.getPrivileges() < 4) {
							client.getActionSender().sendMessage(
									"You can't mute this person.");
							return;
						}
						if (client.getPrivileges() == 2
								&& p.getPrivileges() >= 2
								&& p.getPrivileges() < 4) {
							client.getActionSender().sendMessage(
									"You can't mute this person.");
							return;
						}
					}
					final Player player = p;
					if (p.getUsername().equalsIgnoreCase(name)) {
						player.setMuted(true);
						player.hideMute = true;
					}
				}
				try {
					BanProcessor.writeBanRecord(name, 0, name, 2);
					LogHandler.logMute(client.getUsername(), name);
					client.getActionSender().sendMessage(
							"You have muted " + name + ".");

					final Date date = new Date();

					write("Staff member : " + client.getUsername()
							+ "has muted " + name + "at : " + date);
				} catch (final IOException e) {
					client.getActionSender().sendMessage(
							"Error processing mute.");
				}
			} else
				client.getActionSender().sendMessage(
						"Syntax is ::mute <name> .");
	}

	public boolean isMuted(Player client) {
		try {

			final ResultSet rs = World.getGameDatabase().executeQuery(
					"SELECT * FROM badplayers where username = '"
							+ client.getUsername() + "' and type = 2");

			if (rs.next()) {
				if (System.currentTimeMillis() > rs.getLong("time")
						&& rs.getLong("time") > 10000) {
					World.getGameDatabase().executeQuery(
							"delete from badplayers where username = '"
									+ client.getUsername() + "' and type = 2");

					return false;
				}
				rs.close();
				return true;

			} else {
				rs.close();

				return false;
			}

		} catch (final Exception e) {
			return false;
		}

	}

}
