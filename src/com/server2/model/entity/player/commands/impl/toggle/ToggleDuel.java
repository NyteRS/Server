package com.server2.model.entity.player.commands.impl.toggle;

import com.server2.Constants;
import com.server2.InstanceDistributor;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.SpecialRights;

/**
 * 
 * @author Jordon Barber Globally sets if dueling is allowed
 * 
 */

public class ToggleDuel implements Command {

	String[] commandName = { "Toggle Duel" };

	@Override
	public void execute(Player client, String command) {
		if (client.privileges >= 2 && client.privileges < 4
				|| SpecialRights.isSpecial(client.getUsername()))
			if (Constants.DUEL) {
				InstanceDistributor.getGlobalActions().sendMessage(
						"Dueling has now been disabled");
				Constants.DUEL = false;
			} else {
				InstanceDistributor.getGlobalActions().sendMessage(
						"Dueling has now been enabled");
				Constants.DUEL = true;
			}
	}

}
