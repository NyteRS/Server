package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;

public class Commands implements Command {

	@Override
	public void execute(Player client, String command) {
		client.getActionSender()
				.sendMessage(
						"::yell, ::pin, ::changepin, ::changeyell, ::vote, ::donate, ::report");
		client.getActionSender()
				.sendMessage(
						"::staff, ::support,  ::dzone, ::auth, ::kdr, ::players, ::rules");

	}

}
