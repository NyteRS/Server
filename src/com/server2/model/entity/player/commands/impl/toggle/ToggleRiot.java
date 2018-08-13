package com.server2.model.entity.player.commands.impl.toggle;

import com.server2.Constants;
import com.server2.InstanceDistributor;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.SpecialRights;

/**
 * 
 * @author Jordon Barber Globally sets if Riot Wars is enabled
 * 
 */

public class ToggleRiot implements Command {

	@Override
	public void execute(Player client, String command) {
		if (client.privileges >= 2 && client.privileges < 4
				|| SpecialRights.isSpecial(client.getUsername()))
			if (Constants.RIOTWARS) {
				InstanceDistributor.getGlobalActions().sendMessage(
						"Riot Wars has now been disabled");
				Constants.RIOTWARS = false;
			} else {
				InstanceDistributor.getGlobalActions().sendMessage(
						"Riot Wars has now been enabled");
				Constants.RIOTWARS = true;
			}
	}

}
