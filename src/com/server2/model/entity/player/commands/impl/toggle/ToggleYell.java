package com.server2.model.entity.player.commands.impl.toggle;

import com.server2.Constants;
import com.server2.InstanceDistributor;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.SpecialRights;

/**
 * 
 * @author Jordon Barber Globally sets if yell is enabled
 * 
 */

public class ToggleYell implements Command {

	@Override
	public void execute(Player client, String command) {
		if (client.privileges >= 2 && client.privileges < 4
				|| SpecialRights.isSpecial(client.getUsername()))
			if (Constants.YELL) {
				InstanceDistributor.getGlobalActions().sendMessage(
						"Yell has now been disabled");
				Constants.YELL = false;
			} else {
				InstanceDistributor.getGlobalActions().sendMessage(
						"Yell has now been enabled");
				Constants.YELL = true;
			}
	}

}
