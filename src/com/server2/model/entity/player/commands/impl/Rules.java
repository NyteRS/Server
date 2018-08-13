package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;

/**
 * 
 * @author Jordon Barber
 * 
 */
public class Rules implements Command {

	@Override
	public void execute(Player client, String command) {
		client.getActionSender().sendMessage(
				"The rules of server2 are now loading, please wait..");
		client.getActionSender()
				.sendString(
						"www.forum.server2.com/index.php?/topic/465-the-server2-official-rules-11-26-12/#entry2585",
						0);
		client.getActionSender()
				.sendMessage(
						"Please abide to these rules to avoid any conflicts with your game account.");
	}
}