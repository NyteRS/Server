package com.server2.model.entity.player.commands.impl;

import com.server2.Settings;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.Misc;
import com.server2.world.PlayerManager;

public class Baws implements Command {

	@Override
	public void execute(Player client, String command) {

		for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
			final Player p = PlayerManager.getSingleton().getPlayers()[i];

			if (p == null || p == client)
				continue;

			final Player player = p;
			System.out.println("Distance = "
					+ Misc.distance(client.getAbsX(), client.getAbsY(),
							player.getAbsX(), player.getAbsY()));

		}
	}
}
