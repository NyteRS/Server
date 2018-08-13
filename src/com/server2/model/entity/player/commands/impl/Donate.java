package com.server2.model.entity.player.commands.impl;

import com.server2.Settings;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;

/**
 * 
 * @author Lukas Pinckers
 * 
 */
public class Donate implements Command {

	@Override
	public void execute(Player client, String command) {
		client.getActionSender().sendString(
				"www." + Settings.getString("sv_site") + "/buy.php", 0);
		client.getActionSender().sendMessage(
				"@red@You can donate by going to "
						+ Settings.getString("sv_site") + "/donate.");
		client.getActionSender()
				.sendMessage(
						"@red@When you donate, you will receive tokens, and with these tokens you can buy items.");
	}
}
