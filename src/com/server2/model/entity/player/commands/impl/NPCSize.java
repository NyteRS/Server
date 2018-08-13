package com.server2.model.entity.player.commands.impl;

import com.server2.InstanceDistributor;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;

public class NPCSize implements Command {

	@Override
	public void execute(Player client, String command) {
		client.getActionSender().sendMessage(
				"There are currently "
						+ InstanceDistributor.getNPCManager().getNPCMap()
								.size() + " npcs on server2.");

	}

}
