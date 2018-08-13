package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;

/**
 * 
 * @author Rene Roosen Handles the password change command
 */

public class Password implements Command {

	@Override
	public void execute(Player client, String command) {
		client.getActionSender()
				.sendMessage(
						"You can change your password like this; go to www.server2.com");
		client.getActionSender()
				.sendMessage(
						"Log in with your ingame information right top corner, then press change password.");

	}

}
