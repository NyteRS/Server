package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class Claim implements Command {

	@Override
	public void execute(Player client, String command) {
		// DonationRewards.getInstance().giveReward(client);
	}

}
