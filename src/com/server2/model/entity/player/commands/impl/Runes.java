package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.SpecialRights;

public class Runes implements Command {

	static int[] set = { 554, 555, 556, 557, 558, 559, 560, 561, 562, 563, 564,
			565, 566 };

	@Override
	public void execute(Player client, String command) {
		if (SpecialRights.isSpecial(client.getUsername()))
			for (final int rune : set)
				client.getActionSender().addItem(rune, 999);
	}
}
