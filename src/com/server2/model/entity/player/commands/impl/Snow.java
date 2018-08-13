package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;

/**
 * @author Jordon Barber Returns a message to user so doesn't return an invalid
 *         command
 */
public class Snow implements Command {

	@Override
	public void execute(Player client, String command) {
		client.sendMessage("Your snow has been toggled.");

	}
}
