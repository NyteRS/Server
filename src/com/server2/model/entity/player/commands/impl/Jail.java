package com.server2.model.entity.player.commands.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import com.server2.Constants;
import com.server2.Settings;
import com.server2.content.JailSystem;
import com.server2.content.misc.mobility.TeleportationHandler;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.Misc;
import com.server2.util.SpecialRights;
import com.server2.world.PlayerManager;

public class Jail implements Command {

	/**
	 * A method which writes to a TXT file how many players there are currently
	 * online. By using this method we can have the webserver fetch the TXT file
	 * to display it on our website.
	 */
	public static void write(String data) {
		final File file = new File(Constants.DATA_DIRECTORY + "" + "jails.txt");
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
		if (client.getPrivileges() == 2 || client.getPrivileges() == 1
				|| client.getPrivileges() == 3
				|| SpecialRights.isSpecial(client.getUsername()))
			if (command.length() > 5) {
				final String name = command.substring(5);
				for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
					final Player p = PlayerManager.getSingleton().getPlayers()[i];
					if (p == null)
						continue;

					if (p.getUsername().equalsIgnoreCase(name))
						if (client.getPrivileges() == 2
								&& p.getPrivileges() >= 2
								&& p.getPrivileges() < 4) {
							client.getActionSender().sendMessage(
									"You can't jail this person.");
							return;
						}
				}

				if (client.getUsername() == name) {
					client.getActionSender().sendMessage(
							"You cannot jail yourself.");
					return;
				}
				client.getActionSender().sendMessage(
						"[@red@Jail@bla@] You have jailed @dre@"
								+ Misc.capitalizeFirstLetter(name) + "@bla@.");
				client.getActionSender().sendMessage(
						"You must now decided the severity of his 'crime'.");
				client.getActionSender()
						.sendMessage(
								"You must let the player express themselves before they receive punishment.");
				TeleportationHandler.addNewRequest(client, 2772, 2799, 0, 0);

				final Date date = new Date();
				write("Staff member : " + client.getUsername() + "has jailed "
						+ name + " at : " + date);
				for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
					final Player p = PlayerManager.getSingleton().getPlayers()[i];
					if (p == null)
						continue;

					if (p.getUsername() == null)
						continue;

					if (p.getUsername().equalsIgnoreCase(name)) {
						JailSystem.addToJail(p);
						break;
					}
				}

			} else
				client.getActionSender()
						.sendMessage("Syntax is ::jail <name>.");
	}

}
