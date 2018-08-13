package com.server2.model.entity.player.commands.impl.toggle;

import com.server2.Constants;
import com.server2.InstanceDistributor;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.SpecialRights;

/**
 * 
 * @author Jordon Barber Globally sets the drop rate
 * 
 */

public class ToggleDropRate implements Command {

	@Override
	public void execute(Player client, String command) {
		if (SpecialRights.isSpecial(client.getUsername()))
			if (Constants.DOUBLEDROPS) {
				InstanceDistributor.getGlobalActions().sendMessage(
						"Drop rates have now been doubled!");
				Constants.DOUBLEDROPS = false;
			} else {
				InstanceDistributor.getGlobalActions().sendMessage(
						"Drop rates have now been returned to normal rates!");
				Constants.DOUBLEDROPS = true;
			}
	}

}
