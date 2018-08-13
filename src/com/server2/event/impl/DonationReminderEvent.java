package com.server2.event.impl;

import com.server2.Settings;
import com.server2.event.Event;
import com.server2.model.entity.player.Player;
import com.server2.world.PlayerManager;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class DonationReminderEvent extends Event {

	public DonationReminderEvent() {
		super(3600000);
	}

	@Override
	public void execute() {
		for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
			final Player player = PlayerManager.getSingleton().getPlayers()[i];
			if (player != null)
				player.getActionSender().sendMessage(
						"@red@Have you ever wanted to be rich? You can buy items at www."
								+ Settings.getString("sv_site") + "/donate");
		}

	}

}
