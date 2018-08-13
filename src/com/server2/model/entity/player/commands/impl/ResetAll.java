package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;

public class ResetAll implements Command {

	@Override
	public void execute(Player client, String command) {
		/*
		 * if (client.getPrivileges() == 3) { for (int skill = 0; skill <
		 * PlayerConstants.MAX_SKILLS; skill++) { client.playerLevel[skill] = 1;
		 * client.playerXP[skill] = 0;
		 * client.getActionAssistant().refreshSkill(skill);
		 * System.out.println("[" + System.currentTimeMillis() + "] reset all");
		 * } }
		 */
	}
}
