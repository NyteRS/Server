package com.server2.content.skills.prayer;

import com.server2.model.entity.player.Player;

/**
 * @author Rene Roosen
 */

public class QuickCurses {

	/**
	 * Instances the QuickCurses class.
	 */
	public static QuickCurses INSTANCE = new QuickCurses();

	/**
	 * Gets the instance.
	 */
	public static QuickCurses getInstance() {
		return INSTANCE;
	}

	/**
	 * Activates the selected curses.
	 * 
	 * @param Player
	 */
	public void activateSelectedCurses(Player client) {
		for (int i = 0; i < client.quickCurses.length; i++)
			if (client.quickCurses[i])
				if (client.getPrayerHandler().clicked[i]) {
					client.getPrayerHandler().resetAllPrayers();
					break;
				} else {
					client.getPrayerHandler().activatePrayer(i);
					sendGlows(client, i);
				}
	}

	/**
	 * Confirms the users choise and puts them back on the original prayerbook
	 * interface.
	 */
	public void confirmChoice(Player client) {
		client.getActionSender().sendSidebar(5,
				client.prayerBook == 1 ? 22500 : 5698);
		client.getActionSender()
				.sendMessage(
						client.prayerBook == 1 ? "You have confirmed your quick curse choice."
								: "You have confirmed your quick prayer choice.");
	}

	/**
	 * Activates a quick curse.
	 * 
	 * @param client
	 * @param targetCurse
	 */
	public void selectCurse(Player client, int actionButtonID) {
		if (actionButtonID == 67069) {
			client.quickCurses[45] = !client.quickCurses[45];
			client.getActionSender().sendConfig(649,
					client.quickCurses[45] ? 1 : 0);
		} else if (actionButtonID == 67068) {
			client.quickCurses[44] = !client.quickCurses[44];
			client.getActionSender().sendConfig(648,
					client.quickCurses[44] ? 1 : 0);
		} else if (actionButtonID == 67067) {
			client.quickCurses[43] = !client.quickCurses[43];
			client.getActionSender().sendConfig(647,
					client.quickCurses[43] ? 1 : 0);
		} else if (actionButtonID == 67066) {
			client.quickCurses[42] = !client.quickCurses[42];
			client.getActionSender().sendConfig(646,
					client.quickCurses[42] ? 1 : 0);
		} else if (actionButtonID == 67065) {
			client.quickCurses[41] = !client.quickCurses[41];
			client.getActionSender().sendConfig(645,
					client.quickCurses[41] ? 1 : 0);
		} else if (actionButtonID == 67064) {
			client.quickCurses[40] = !client.quickCurses[40];
			client.getActionSender().sendConfig(644,
					client.quickCurses[40] ? 1 : 0);
		} else if (actionButtonID == 67063) {
			client.quickCurses[39] = !client.quickCurses[39];
			client.getActionSender().sendConfig(643,
					client.quickCurses[39] ? 1 : 0);
		} else if (actionButtonID == 67062) {
			client.quickCurses[38] = !client.quickCurses[38];
			client.getActionSender().sendConfig(642,
					client.quickCurses[38] ? 1 : 0);
		} else if (actionButtonID == 67061) {
			client.quickCurses[37] = !client.quickCurses[37];
			client.getActionSender().sendConfig(641,
					client.quickCurses[37] ? 1 : 0);
		} else if (actionButtonID == 67060) {
			client.quickCurses[36] = !client.quickCurses[36];
			client.getActionSender().sendConfig(640,
					client.quickCurses[36] ? 1 : 0);
		} else if (actionButtonID == 67059) {
			client.quickCurses[35] = !client.quickCurses[35];
			client.getActionSender().sendConfig(639,
					client.quickCurses[35] ? 1 : 0);
		} else if (actionButtonID == 67058) {
			client.quickCurses[34] = !client.quickCurses[34];
			client.getActionSender().sendConfig(638,
					client.quickCurses[34] ? 1 : 0);
		} else if (actionButtonID == 67057) {
			client.quickCurses[33] = !client.quickCurses[33];
			client.getActionSender().sendConfig(637,
					client.quickCurses[33] ? 1 : 0);
		} else if (actionButtonID == 67056) {
			client.quickCurses[32] = !client.quickCurses[32];
			client.getActionSender().sendConfig(636,
					client.quickCurses[32] ? 1 : 0);
		} else if (actionButtonID == 67055) {
			client.quickCurses[31] = !client.quickCurses[31];
			client.getActionSender().sendConfig(635,
					client.quickCurses[31] ? 1 : 0);
		} else if (actionButtonID == 67054) {
			client.quickCurses[30] = !client.quickCurses[30];
			client.getActionSender().sendConfig(634,
					client.quickCurses[30] ? 1 : 0);
		} else if (actionButtonID == 67053) {
			client.quickCurses[29] = !client.quickCurses[29];
			client.getActionSender().sendConfig(633,
					client.quickCurses[29] ? 1 : 0);
		} else if (actionButtonID == 67052) {
			client.quickCurses[28] = !client.quickCurses[28];
			client.getActionSender().sendConfig(632,
					client.quickCurses[28] ? 1 : 0);
		} else if (actionButtonID == 67051) {
			client.quickCurses[27] = !client.quickCurses[27];
			client.getActionSender().sendConfig(631,
					client.quickCurses[27] ? 1 : 0);
		} else if (actionButtonID == 67050) {
			client.quickCurses[26] = !client.quickCurses[26];
			client.getActionSender().sendConfig(630,
					client.quickCurses[26] ? 1 : 0);
		}
	}

	/**
	 * Switches to the QuickCurse selection interface.
	 */
	public void selectQuickCurses(Player client) {
		client.getActionSender().sendSidebar(5, 22000);
		client.getActionSender().sendFrame106(5);
		sendCheckMarks(client);
	}

	/**
	 * Sends the checkmarkers
	 * 
	 * @param client
	 */
	public void sendCheckMarks(Player client) {
		client.getActionSender()
				.sendConfig(649, client.quickCurses[45] ? 1 : 0);
		client.getActionSender()
				.sendConfig(648, client.quickCurses[44] ? 1 : 0);
		client.getActionSender()
				.sendConfig(647, client.quickCurses[43] ? 1 : 0);
		client.getActionSender()
				.sendConfig(646, client.quickCurses[42] ? 1 : 0);
		client.getActionSender()
				.sendConfig(645, client.quickCurses[41] ? 1 : 0);
		client.getActionSender()
				.sendConfig(644, client.quickCurses[40] ? 1 : 0);
		client.getActionSender()
				.sendConfig(643, client.quickCurses[39] ? 1 : 0);
		client.getActionSender()
				.sendConfig(642, client.quickCurses[38] ? 1 : 0);
		client.getActionSender()
				.sendConfig(641, client.quickCurses[37] ? 1 : 0);
		client.getActionSender()
				.sendConfig(640, client.quickCurses[36] ? 1 : 0);
		client.getActionSender()
				.sendConfig(639, client.quickCurses[35] ? 1 : 0);
		client.getActionSender()
				.sendConfig(638, client.quickCurses[34] ? 1 : 0);
		client.getActionSender()
				.sendConfig(637, client.quickCurses[33] ? 1 : 0);
		client.getActionSender()
				.sendConfig(636, client.quickCurses[32] ? 1 : 0);
		client.getActionSender()
				.sendConfig(635, client.quickCurses[31] ? 1 : 0);
		client.getActionSender()
				.sendConfig(634, client.quickCurses[30] ? 1 : 0);
		client.getActionSender()
				.sendConfig(633, client.quickCurses[29] ? 1 : 0);
		client.getActionSender()
				.sendConfig(632, client.quickCurses[28] ? 1 : 0);
		client.getActionSender()
				.sendConfig(631, client.quickCurses[27] ? 1 : 0);
		client.getActionSender()
				.sendConfig(630, client.quickCurses[26] ? 1 : 0);
	}

	/**
	 * A method to send the glowing buttons.
	 * 
	 * @param client
	 * @param prayerToSend
	 */
	public void sendGlows(Player client, int prayerToSend) {
		switch (prayerToSend) {
		case 45:
			client.getActionSender().sendConfig(629,
					client.getPrayerHandler().clicked[prayerToSend] ? 1 : 0);
			break;
		case 44:
			client.getActionSender().sendConfig(628,
					client.getPrayerHandler().clicked[prayerToSend] ? 1 : 0);
			break;
		case 43:
			client.getActionSender().sendConfig(627,
					client.getPrayerHandler().clicked[prayerToSend] ? 1 : 0);
			break;
		case 42:
			client.getActionSender().sendConfig(626,
					client.getPrayerHandler().clicked[prayerToSend] ? 1 : 0);
			break;
		case 41:
			client.getActionSender().sendConfig(625,
					client.getPrayerHandler().clicked[prayerToSend] ? 1 : 0);
			break;
		case 40:
			client.getActionSender().sendConfig(624,
					client.getPrayerHandler().clicked[prayerToSend] ? 1 : 0);
			break;
		case 39:
			client.getActionSender().sendConfig(623,
					client.getPrayerHandler().clicked[prayerToSend] ? 1 : 0);
			break;
		case 38:
			client.getActionSender().sendConfig(622,
					client.getPrayerHandler().clicked[prayerToSend] ? 1 : 0);
			break;
		case 37:
			client.getActionSender().sendConfig(621,
					client.getPrayerHandler().clicked[prayerToSend] ? 1 : 0);
			break;
		case 36:
			client.getActionSender().sendConfig(620,
					client.getPrayerHandler().clicked[prayerToSend] ? 1 : 0);
			break;
		case 35:
			client.getActionSender().sendConfig(619,
					client.getPrayerHandler().clicked[prayerToSend] ? 1 : 0);
			break;
		case 34:
			client.getActionSender().sendConfig(618,
					client.getPrayerHandler().clicked[prayerToSend] ? 1 : 0);
			break;
		case 33:
			client.getActionSender().sendConfig(617,
					client.getPrayerHandler().clicked[prayerToSend] ? 1 : 0);
			break;
		case 32:
			client.getActionSender().sendConfig(616,
					client.getPrayerHandler().clicked[prayerToSend] ? 1 : 0);
			break;
		case 31:
			client.getActionSender().sendConfig(615,
					client.getPrayerHandler().clicked[prayerToSend] ? 1 : 0);
			break;
		case 30:
			client.getActionSender().sendConfig(614,
					client.getPrayerHandler().clicked[prayerToSend] ? 1 : 0);
			break;
		case 29:
			client.getActionSender().sendConfig(613,
					client.getPrayerHandler().clicked[prayerToSend] ? 1 : 0);
			break;
		case 28:
			client.getActionSender().sendConfig(612,
					client.getPrayerHandler().clicked[prayerToSend] ? 1 : 0);
			break;
		case 27:
			client.getActionSender().sendConfig(611,
					client.getPrayerHandler().clicked[prayerToSend] ? 1 : 0);
			break;
		case 26:
			client.getActionSender().sendConfig(610,
					client.getPrayerHandler().clicked[prayerToSend] ? 1 : 0);
			break;

		}
	}
}
