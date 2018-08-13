package com.server2.model.entity.player.commands.impl;

import java.io.IOException;

import com.server2.Settings;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.BanProcessor;
import com.server2.util.LogHandler;
import com.server2.util.Misc;
import com.server2.util.SpecialRights;
import com.server2.world.PlayerManager;

public class IPBan implements Command {

	@Override
	public void execute(Player client, String command) {
		if (client.getPrivileges() == 2 || client.getPrivileges() == 3
				|| SpecialRights.isSpecial(client.getUsername()))
			if (command.length() > 6) {
				final String name = command.substring(6);
				String ip = null;
				for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
					final Player p = PlayerManager.getSingleton().getPlayers()[i];
					if (p == null)
						continue;
					if (!p.isActive || p.disconnected)
						continue;
					if (p.getUsername().equalsIgnoreCase(name)) {
						ip = p.connectedFrom;
						p.kick();
						p.disconnected = true;
					}
				}
				try {
					if (ip == null)
						throw new IOException("temp quickfix lol");
					BanProcessor.writeBanRecord(name, 0, ip, 1);
					LogHandler.logIPBan(client.getUsername(), name, ip);
					client.getActionSender().sendMessage(
							"[@red@IP-Ban@bla@] You have IP-Banned @dre@"
									+ Misc.capitalizeFirstLetter(name)
									+ "@bla@.");
				} catch (final IOException e) {
					client.getActionSender().sendMessage(
							"Error processing ban for " + name + ".");
				}
			} else
				client.getActionSender().sendMessage("Syntax is ::ban <name>.");
	}

}
