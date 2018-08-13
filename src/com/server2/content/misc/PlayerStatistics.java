package com.server2.content.misc;

import com.server2.model.entity.player.Player;
import com.server2.util.Misc;

/**
 * Loads player stats
 * @author LT Smith
 *
 */
public class PlayerStatistics {

	private static final PlayerStatistics INSTANCE = new PlayerStatistics();

	public static PlayerStatistics getInstance() {
		return INSTANCE;
	}

	public void formatQuestJournal(final Player player) {
		final int[] frames = { 88144, 8145, 8146, 8147, 8148, 8149, 8150, 8151,
				8152, 8153, 8154, 8155, 8156, 8157, 8158, 8159, 8160 };
		for (final int i : frames)
			player.getActionSender().sendString("", i);
	}

	public void loadStats(Player player) {
		player.getActionSender().sendInterface(8134);
		formatQuestJournal(player);
		player.getActionSender().sendString(
				"@red@Statistics of " + player.getUsername() + "", 8144);
		player.getActionSender().sendString(
				"Vote points: " + player.votePoints + "", 8147);
		player.getActionSender().sendString(
				"Slayer points: " + player.slayerPoints + "", 8148);
		player.getActionSender().sendString(
				"PK points: " + player.pkPoints + "", 8149);
		player.getActionSender().sendString(
				"Dungeoneering tokens: "
						+ player.dungTokens + "", 8150);
		player.getActionSender().sendString(
				"Duo slayer points: " + player.duoPoints + "", 8151);
		player.getActionSender().sendString(
				"Pest control zeal: " + player.getPestControlZeal() + "", 8152);
		player.getActionSender().sendString(
				"To view NPC stats, type ::npcstats", 8153);

	}
}