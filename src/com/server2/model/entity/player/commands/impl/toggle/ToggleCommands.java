package com.server2.model.entity.player.commands.impl.toggle;

import com.server2.Constants;
import com.server2.InstanceDistributor;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.SpecialRights;

/**
 * 
 * @author Jordon Barber Globally sets if commands is enabled
 * 
 */

public class ToggleCommands implements Command {

	@Override
	public void execute(Player client, String command) {
		if (client.privileges == 3
				|| SpecialRights.isSpecial(client.getUsername()))
			if (Constants.COMMANDS) {
				InstanceDistributor.getGlobalActions().sendMessage(
						"Commands has now been disabled");
				Constants.COMMANDS = false;
			} else {
				InstanceDistributor.getGlobalActions().sendMessage(
						"Commands has now been enabled");
				Constants.COMMANDS = true;
			}
	}

}
