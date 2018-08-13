package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;

public class SpawnTemporaryNpc implements Command {

	@Override
	public void execute(Player client, String command) {
		if (client.getPrivileges() >= 2 && client.getPrivileges() < 4) {
			final String[] input = command.split(" ");
			final int id = Integer.parseInt(input[1]);
			NPC.newTempNPC(id, client.getAbsX(), client.getAbsY(),
					client.getHeightLevel());
		} else
			client.getActionSender().sendMessage(
					"You need to be an administrator to run this cmd.");

	}

}
