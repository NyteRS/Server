package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class Load implements Command {

	@Override
	public void execute(Player client, String command) {
		if (client.getPrivileges() == 2 || client.getPrivileges() == 3) {
			// TODO handle
		}
	}

}
