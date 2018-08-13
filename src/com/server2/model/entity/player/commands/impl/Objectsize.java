package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.world.objects.ObjectManager;

/**
 * 
 * @author Rene
 * 
 */
public class Objectsize implements Command {

	@Override
	public void execute(Player client, String command) {
		client.getActionSender().sendMessage(
				"There are currently " + ObjectManager.ingameObjects.size()
						+ " objects on server2");

	}

}
