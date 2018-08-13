package com.server2.model.combat.additions;

import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene Roosen & Lukas
 * 
 */
public class Mandrith {

	/**
	 * Instances the Mandrith class.
	 */
	private static Mandrith INSTANCE = new Mandrith();

	/**
	 * Gets the Mandrith Instance.
	 */
	public static Mandrith getInstance() {
		return INSTANCE;
	}

	/**
	 * Performs all actions of mandrith
	 * 
	 * @param player
	 * @param action
	 */
	public void mandrithAction(Player player, int action) {

		switch (action) {
		case 0:
			player.getActionAssistant().openUpShop(1001);
			break;
		case 1:
			player.getDM()
					.sendNpcChat2(
							"You have " + (int) player.killCount
									+ " kills and " + (int) player.deathCount
									+ " deaths. You're currently",
							"on a " + player.killStreak
									+ " killstreak and you have "
									+ player.pkPoints + " pk-points.", 8725,
							"Mandrith");
			break;

		case 2:
			player.getDM().sendNpcChat2(
					"Please use the artifact you want to sell on me", "", 8725,
					"Mandrith");
			break;
		}
	}

	/**
	 * Opens the dialogue with mandriths options
	 * 
	 * @param player
	 */
	public void startDialogue(Player player) {
		player.getActionSender().selectOption("Select an option",
				"Tell me my pvp statistics", "Open your store",
				"I want to sell my artifacs");
		player.dialogueAction = 443;
	}
}
