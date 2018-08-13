package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;

/**
 * 
 * @author Rene
 * 
 */
public class Forums implements Command {

	@Override
	public void execute(Player client, String command) {
		client.getActionSender().sendString("www.forum.server2.com", 10);

	}

}
