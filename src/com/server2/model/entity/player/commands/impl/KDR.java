package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;

/**
 * 
 * @author Rene Roosen
 * 
 */

public class KDR implements Command {

	@Override
	public void execute(Player client, String command) {
		if (client.killCount == 0 || client.deathCount == 0) {
			client.getActionSender()
					.sendMessage(
							"You either have 0 kills or 0 deaths, therefore we cannot calculate your KDR.");
			return;
		}
		client.getActionSender().sendMessage(
				"[@red@KDR@bla@] Kills : @dre@" + client.killCount
						+ " @bla@Deaths : @dre@" + client.deathCount);
		client.forcedText = "My Kill Death Ratio is " + client.killCount + "/"
				+ client.deathCount + " = " + client.killCount
				/ client.deathCount + ".";
		client.forcedTextUpdateRequired = true;
		client.updateRequired = true;
	}

}
