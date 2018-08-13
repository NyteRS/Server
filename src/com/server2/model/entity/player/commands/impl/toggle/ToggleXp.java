package com.server2.model.entity.player.commands.impl.toggle;

import com.server2.InstanceDistributor;
import com.server2.Server;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.SpecialRights;

/**
 * 
 * @author Jordon Barber Globally sets the experience rate
 * 
 */

public class ToggleXp implements Command {

	@Override
	public void execute(Player client, String command) {
		if (SpecialRights.isSpecial(client.getUsername()))
			if (command.length() > 9) {
				final int bonus = Integer.valueOf(command.substring(9));
				Server.bonusExp = bonus;
				InstanceDistributor.getGlobalActions().sendMessage(
						"The experience rate modifier is now "
								+ Server.bonusExp + "x experience");
			} else
				client.getActionSender().sendMessage(
						"Wrong syntax use ::togglexp bonus");
	}

}
