package com.server2.model.entity.player.commands.impl;

import com.server2.Settings;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.model.entity.player.commands.Command;
import com.server2.world.PlayerManager;

/**
 * 
 * @author Killamess Just a max stat command.
 * 
 */
public class Master implements Command {

	@Override
	public void execute(Player c, String command) {
		Player client = c;
		if (client.getPrivileges() == 2) {
			if (command.length() > 7)
				command = command.substring(7);
			for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
				final Player p = PlayerManager.getSingleton().getPlayers()[i];
				if (p == null)
					continue;
				if (!p.isActive || p.disconnected)
					continue;
				if (p.getUsername().equalsIgnoreCase(command)) {

					client = p;

					break;
				}
			}
			for (int skill = 0; skill < PlayerConstants.MAX_SKILLS; skill++)
				if (skill != 23) {
					client.playerLevel[skill] = client.getLevelForXP(14000000);
					client.playerXP[skill] = 14000000;
					client.getActionAssistant().refreshSkill(skill);
				} else {
					client.playerLevel[skill] = client
							.getDungLevelForXp(200000000);
					client.playerXP[skill] = 110000000;
					client.getActionAssistant().refreshSkill(skill);
				}
		}
	}

}
