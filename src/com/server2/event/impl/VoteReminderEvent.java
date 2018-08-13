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
public class VoteReminderEvent extends Event {

	public VoteReminderEvent() {
		super(800000);
	}

	@Override
	public void execute() {
		for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
			final Player player = PlayerManager.getSingleton().getPlayers()[i];
			if (player == null)
				continue;
			player.getActionSender()
					.sendMessage(
							"[@red@SERVER@bla@] @red@Remember to vote for more updates and players do ::vote");
		}

	}

}
