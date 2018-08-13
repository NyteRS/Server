package com.server2.model.entity.player.commands.impl;

import com.server2.content.NPCStats;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;

public class LoadNPCStats implements Command {

	@Override
	public void execute(Player client, String command) {
		NPCStats.instance.execute(client);

	}

}
