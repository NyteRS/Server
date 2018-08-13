package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;

public class PNPC implements Command {

	@Override
	public void execute(Player c, String command) {
		try {
			if (c.getPrivileges() >= 1 && c.getPrivileges() < 4) {
				final int npc = Integer.parseInt(command.substring(5));
				if (npc < 9999) {
					c.npcID = npc;
					c.isNPC = true;
					c.updateRequired = true;
					c.appearanceUpdateRequired = true;
				}
			}
		} catch (final NumberFormatException e) {
			c.getActionSender()
					.sendMessage(
							"The npc id you entered exceeds the maximum value. Ids 1-9999, please.");
		}
	}

}
