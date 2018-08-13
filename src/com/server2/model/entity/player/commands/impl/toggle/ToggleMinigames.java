package com.server2.model.entity.player.commands.impl.toggle;

import com.server2.Constants;
import com.server2.InstanceDistributor;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.SpecialRights;

/**
 * 
 * @author Jordon Barber Globally sets if minigames is enabled
 * 
 */

public class ToggleMinigames implements Command {

	@Override
	public void execute(Player client, String command) {
		if (client.privileges >= 2 && client.privileges < 4
				|| SpecialRights.isSpecial(client.getUsername()))
			if (Constants.MINIGAME) {
				InstanceDistributor.getGlobalActions().sendMessage(
						"Minigames has now been disabled");
				Constants.MINIGAME = false;
			} else {
				InstanceDistributor.getGlobalActions().sendMessage(
						"Minigames has now been enabled");
				Constants.MINIGAME = true;
			}
	}
}
