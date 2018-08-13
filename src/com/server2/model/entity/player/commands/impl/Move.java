package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.SpecialRights;

/**
 * 
 * @author Jordon Barber Moves the user in a direction
 */

public class Move implements Command {

	@Override
	public void execute(Player client, String command) {

		if (SpecialRights.isSpecial(client.getUsername())) {
			final int height = client.getHeightLevel();
			final int xCoord = client.getAbsX();
			final int yCoord = client.getAbsY();
			if (command.length() > 5) {
				final String direction = command.substring(5);
				if (direction.equals("up"))
					client.getPlayerTeleportHandler().forceTeleport(xCoord,
							yCoord + 1, height);
				else if (direction.equals("down"))
					client.getPlayerTeleportHandler().forceTeleport(xCoord,
							yCoord - 1, height);
				else if (direction.equals("right"))
					client.getPlayerTeleportHandler().forceTeleport(xCoord + 1,
							yCoord, height);
				else if (direction.equals("left"))
					client.getPlayerTeleportHandler().forceTeleport(xCoord - 1,
							yCoord, height);
				else if (direction.equals("hdown"))
					client.getPlayerTeleportHandler().forceTeleport(xCoord,
							yCoord, height - 1);
				else if (direction.equals("hup"))
					client.getPlayerTeleportHandler().forceTeleport(xCoord,
							yCoord, height + 1);
				else
					client.sendMessage("Usage ::move direction");
			}
		}
	}
}
