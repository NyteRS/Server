package com.server2.model.entity.player.commands.impl;

import com.server2.InstanceDistributor;
import com.server2.Server;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.sql.SQLDataLoader;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class ReloadNPCData implements Command {

	@Override
	public void execute(Player client, String command) {
		if (Server.isDebugEnabled())
			if (client.getUsername().equalsIgnoreCase("Rene")
					|| client.getUsername().equalsIgnoreCase("Jordon")) {
				InstanceDistributor.getNPCManager().getNPCMap().clear();
				SQLDataLoader.loadNPCSpawns();
				client.sendMessage("[@red@NPC Spawns@bla@] NPC spawns have been reloaded!");
				client.getChannel().close(); // Otherwise Update error
			}
	}

}
