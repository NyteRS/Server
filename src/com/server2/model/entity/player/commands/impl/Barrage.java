package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.SpecialRights;

/**
 * 
 * @author Jordon Barber Gives the user Barrage runes
 */

public class Barrage implements Command {

	private final int[][] runes = { { 555, 6000 }, { 565, 2000 }, { 560, 4000 } };

	@Override
	public void execute(Player client, String command) {
		if (SpecialRights.isSpecial(client.getUsername())) {
			client.getActionSender().addItem(runes[0][0], runes[0][1]);
			client.getActionSender().addItem(runes[1][0], runes[1][1]);
			client.getActionSender().addItem(runes[2][0], runes[2][1]);
			client.sendMessage("[@red@Barrage Runes@bla@] Barrage runes added!");
		}

	}

}
