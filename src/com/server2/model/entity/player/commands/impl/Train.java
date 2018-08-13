package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class Train implements Command {

	@Override
	public void execute(Player client, String command) {
		client.getPlayerTeleportHandler().teleport(2673, 3712, 0);
	}

}
