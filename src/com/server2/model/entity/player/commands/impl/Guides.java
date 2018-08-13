package com.server2.model.entity.player.commands.impl;

import com.server2.Settings;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;

/**
 * 
 * @author Jordon Barber
 * 
 */

public class Guides implements Command {

	@Override
	public void execute(Player client, String command) {
		client.getActionSender().sendString(
				"http://" + Settings.getString("sv_site") + "/guides/", 5);
	}

}
