package com.server2.model.entity.player.commands.impl;

import com.server2.content.DonatorZone;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;

/**
 * 
 * @author Renual Productions
 * 
 */
public class DZone implements Command {

	@Override
	public void execute(Player client, String command) {
		DonatorZone.getInstance().enterZone(client);
	}
}
