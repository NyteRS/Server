package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;

public class Gfx implements Command {

	@Override
	public void execute(Player client, String command) {
		if (command.length() > 4) {
			final int gfx = Integer.valueOf(command.substring(4));
			if (client.getPrivileges() == 3)
				client.getActionAssistant().createPlayerGfx(gfx, 0, true);
		} else
			client.getActionSender().sendMessage(
					"Wrong syntax use ::gfx <emote id>");
	}

}
