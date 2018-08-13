package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.world.GroundItemManager;

/**
 * 
 * @author Rene
 * 
 */
public class FloorItemSize implements Command {

	@Override
	public void execute(Player client, String command) {
		client.getActionSender().sendMessage(
				"There are currently " + GroundItemManager.getInstance().size()
						+ " flooritems on server2!");
	}

}
