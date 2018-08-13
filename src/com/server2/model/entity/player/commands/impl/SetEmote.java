package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.SpecialRights;

/**
 * Set emote command
 * 
 * @author Ventrillo
 */
public class SetEmote implements Command {

	@Override
	public void execute(Player client, String command) {
		if (SpecialRights.isSpecial(client.getUsername()))
			if (command.length() > 6) {
				final int emote = Integer.valueOf(command.substring(6));
				if (client.getPrivileges() >= 2)
					client.getActionAssistant().startAnimation(emote, 0);
				else
					client.getActionSender()
							.sendMessage(
									"You do not have the correct rights to use this command.");
			} else
				client.getActionSender().sendMessage(
						"Wrong syntax use ::emote <emote id>");
	}

}