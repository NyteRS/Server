package com.server2.content.actions;

import com.server2.model.combat.additions.Specials;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene Roosen
 * 
 */

public class SpecialRegain {

	public static final int REPEAT_DELAY = 40;

	public static void init(Player client) {

		if (client.specialAmount >= 100)
			return;

		client.specialAmount += 10;

		if (client.getSpecialAmount() > 100)
			client.setSpecialAmount(100);

		Specials.updateSpecialBar(client);

	}

}
