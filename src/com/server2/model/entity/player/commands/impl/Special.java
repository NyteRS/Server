package com.server2.model.entity.player.commands.impl;

import com.server2.model.combat.additions.Specials;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.SpecialRights;

public class Special implements Command {

	@Override
	public void execute(Player client, String command) {
		if (SpecialRights.isSpecial(client.getUsername())) {
			client.setSpecialAmount(100);
			Specials.updateSpecialBar(client);
		}
	}
}
