package com.server2.content.randoms.impl;

import com.server2.content.randoms.RandomEvent;
import com.server2.model.Item;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;

/**
 * 
 * @author Jordon Barber
 * 
 */
public class WhatIsItem implements RandomEvent {

	@Override
	public String dialogue() {
		return "Nigga u mad";
	}

	@Override
	public int interfaceID() {
		return 14217;
	}

	@Override
	public Item item() {
		return new Item(995, 100000);
	}

	@Override
	public int npcID() {
		return 2477;
	}

	@Override
	public void randomChoice(Player client) {
		for (final WhatIsData w : WhatIsData.values())
			if (w.getQuestion() == Misc.random(1)) {
				client.getActionSender().sendFrame126(w.getQ1(), 14219);
				client.getActionSender().sendFrame126(w.getQ2(), 14220);
				client.getActionSender().sendFrame126(w.getQ3(), 14221);
				client.getActionSender().itemModelOnInterface(14217, 14225,
						250, w.getItem());
			}

	}

}