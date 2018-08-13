package com.server2.content.misc;

import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class ExpLocking {

	/**
	 * A stringArray that Contains the different lock states
	 */
	private static String[] lockStates = { "Unlocked", "Locked" };

	/**
	 * Locks/Unlocks the users experience.
	 */
	public static void lockExperience(Player client) {
		if (client.expLocked == 0)
			client.expLocked = 1;
		else
			client.expLocked = 0;
		if (client.expLocked == 0)
			client.getActionSender()
					.sendMessage(
							"Your experience is now : @dre@" + lockStates[0]
									+ "@bla@.");
		else
			client.getActionSender()
					.sendMessage(
							"Your experience is now : @dre@" + lockStates[1]
									+ "@bla@.");
		client.getActionSender().sendWindowsRemoval();
	}
}
