package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.SpecialRights;

/**
 * My position command
 * 
 * @author Graham
 */
public class MyPosition implements Command {

	@Override
	public void execute(Player client, String command) {
		if (client.getPrivileges() >= 2 && client.getPrivileges() < 5
				|| SpecialRights.isSpecial(client.getUsername()))
			client.getActionSender().sendMessage(
					"[@red@My Position@bla@] playerX: @dre@" + client.getAbsX()
							+ " @bla@playerY: @dre@" + client.getAbsY()
							+ "@bla@ playerHeight: @dre@"
							+ client.getHeightLevel() + "@bla@.");
	}

}
