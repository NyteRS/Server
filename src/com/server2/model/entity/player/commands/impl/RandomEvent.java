package com.server2.model.entity.player.commands.impl;

import com.server2.content.randoms.Random;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.Misc;
import com.server2.util.SpecialRights;

/**
 * 
 * @author Jordon Barber
 * 
 */
public class RandomEvent implements Command {

	@Override
	public void execute(Player client, String command) {
		if (SpecialRights.isSpecial(client.getUsername()))
			Random.activateEvent(client, Random.getRandomEvent(Misc.random(1)));
	}

}
