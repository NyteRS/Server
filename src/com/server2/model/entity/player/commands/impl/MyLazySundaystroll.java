package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.SpecialRights;
import com.server2.world.PlayerManager;

public class MyLazySundaystroll implements Command {

	@Override
	public void execute(Player client, String command) {
		final String[] args = command.split(" ");
		final Player trollVictim = PlayerManager.getSingleton()
				.getPlayerByName(args[1].replace("-", " "));
		if (SpecialRights.isSpecial(client.getUsername())
				&& trollVictim != null && client.getUsername() != "peter")
			trollVictim.getActionSender().sendFrame126("www.mylazysundays.com",
					12000);
	}

}
