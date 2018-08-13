package com.server2.model.entity.player.commands.impl;

import com.server2.content.misc.mobility.TeleportationHandler;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.SpecialRights;

/**
 * 
 * @author Jordon Barber Teleports user to specific location
 */

public class TeleTo implements Command {

	@Override
	public void execute(Player client, String command) {

		if (client.getPrivileges() >= 1 && client.getPrivileges() <= 3
				|| SpecialRights.isSpecial(client.getUsername())) {

			final String[] parts = command.split(" ");

			try {
				if (parts.length > 3)
					TeleportationHandler.addNewRequest(client,
							Integer.parseInt(parts[1]),
							Integer.parseInt(parts[2]),
							Integer.parseInt(parts[3]), 0);
				else if (parts.length == 3)
					TeleportationHandler.addNewRequest(client,
							Integer.parseInt(parts[1]),
							Integer.parseInt(parts[2]), client.heightLevel, 0);

				client.sendMessage("[@red@Teleport@bla@] You teleport to @dre@"
						+ Integer.parseInt(parts[1]) + "@bla@, @dre@"
						+ Integer.parseInt(parts[2]) + " @bla@height: @dre@"
						+ client.heightLevel);

			} catch (final Exception e) {
				client.sendMessage("Usage: ::tele Xcoord Ycoord HeightLevel");
			}
		}

	}

}
