package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.SpecialRights;

/**
 * 
 * @author Jordon Barber Creates the specified animation
 */

public class Anim implements Command {

	@Override
	public void execute(Player client, String command) {
		if (SpecialRights.isSpecial(client.getUsername()))
			if (command.length() > 5) {
				final int anim = Integer.valueOf(command.substring(5));
				client.getActionAssistant().startAnimation(anim);
			} else
				client.getActionSender().sendMessage(
						"Wrong syntax use ::anim id");
	}
}
