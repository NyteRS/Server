package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.world.Clan.ClanMember;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class Clansize implements Command {

	/**
	 * Clansize
	 */
	@Override
	public void execute(Player client, String command) {
		if (client.getClanDetails() == null) {
			client.getActionSender().sendMessage("You're not in a clan!");
			return;
		}
		int clanSize = 0;
		for (final ClanMember mb : client.getClanDetails().getClan()
				.getMembers()) {
			if (mb == null)
				continue;
			clanSize++;
		}
		client.getActionSender().sendMessage(
				"There are currently " + clanSize + " players in your clan.");

	}

}
