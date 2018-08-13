package com.server2.model.entity.player.commands.impl.toggle;

import com.server2.Constants;
import com.server2.InstanceDistributor;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.SpecialRights;

/**
 * 
 * @author Jordon Barber Globally sets if Castle Wars is enabled
 * 
 */

public class ToggleCWars implements Command {

	@Override
	public void execute(Player client, String command) {
		if (client.privileges >= 2 && client.privileges < 4
				|| SpecialRights.isSpecial(client.getUsername()))
			if (Constants.CWARS) {
				InstanceDistributor.getGlobalActions().sendMessage(
						"Castle Wars has now been disabled");
				Constants.CWARS = false;
			} else {
				InstanceDistributor.getGlobalActions().sendMessage(
						"Castle Wars has now been enabled");
				Constants.CWARS = true;
			}
	}

}
