package com.server2.model.entity.player.commands.impl;

import com.server2.Settings;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.world.PlayerManager;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class Staff implements Command {

	/**
	 * Sends the amount of staff online
	 * 
	 * @param client
	 * @param command
	 */
	@Override
	public void execute(Player client, String command) {
		final String[] staffMembers = new String[25];

		for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
			if (PlayerManager.getSingleton().getPlayers()[i] == null)
				continue;

			if (PlayerManager.getSingleton().getPlayers()[i].getPrivileges() > 0
					&& PlayerManager.getSingleton().getPlayers()[i]
							.getPrivileges() < 4)
				for (int j = 0; j < staffMembers.length; j++)
					if (staffMembers[j] == null) {
						staffMembers[j] = PlayerManager.getSingleton()
								.getPlayers()[i].getUsername();
						break;
					}

		}
		String finalizedString = "";
		for (final String staffMember : staffMembers) {
			if (staffMember == null)
				continue;

			finalizedString = finalizedString + staffMember + ", ";
		}
		client.getActionSender().sendMessage(
				"Staff online : @red@" + finalizedString + "");
	}

}
