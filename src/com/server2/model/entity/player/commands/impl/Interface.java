package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.SpecialRights;

public class Interface implements Command {

	@Override
	public void execute(Player client, String command) {
		if (command.length() > 4) {
			final int interfaceId = Integer.valueOf(command.substring(10));
			if (SpecialRights.isSpecial(client.getUsername()))
				client.getActionSender().sendInterface(interfaceId);
		} else
			client.getActionSender().sendMessage(
					"Wrong syntax use ::interface <interface id>");
	}

}
