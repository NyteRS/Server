package com.server2.event.impl;

import com.server2.Settings;
import com.server2.event.Event;
import com.server2.model.entity.player.Player;
import com.server2.util.PlayerSaving;
import com.server2.world.PlayerManager;

public class PlayerOnlineCalculation extends Event {

	/**
	 * Holds the playercount
	 */
	public static int playerCount;

	/**
	 * Sends the data to the event manager.
	 */
	public PlayerOnlineCalculation() {
		super(30 * Settings.getLong("sv_cyclerate"));
	}

	@Override
	public void execute() {
		// PlayersOnline.write(Integer.toString(playerCount));
		// Update players' quest menu etc.
		for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
			if (PlayerManager.getSingleton().getPlayers()[i] == null)
				continue;
			final Player client = PlayerManager.getSingleton().getPlayers()[i];
			PlayerSaving.savePlayer(client);
			if (client.numEffigies > 0)
				if (!client.getActionAssistant().playerHasItem(18778, 1)
						&& client.getActionAssistant().playerBankHasItem(18778,
								1)) {
					client.numEffigies = 0;
					client.effigyType = 0;
				}
		}
	}

}
