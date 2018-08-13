package com.server2.model.entity.player.commands.impl;

import com.server2.content.misc.mobility.TeleportationHandler;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.SpecialRights;

/**
 * 
 * @author Rene
 * 
 */
public class TeleToJail implements Command {

	@Override
	public void execute(Player client, String command) {
		if (client.getPrivileges() > 0 && client.getPrivileges() < 4
				|| SpecialRights.isSpecial(client.getUsername()))
			TeleportationHandler.addNewRequest(client, 2772, 2800, 0, 0);

	}

}
