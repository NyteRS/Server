package com.server2.model.combat.magic;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;

/**
 * 
 * @author Killamess
 * 
 */
public class AutoCast {

	public static final int OFF = 0, ON = 1, FRAME_ID = 185, CONFIG_ID = 108;

	public static int[] regularStaffs = { 1379, 1381, 1383, 1385, 1387, 1389,
			1391, 1393, 1395, 1397, 1399, 1401, 1403, 1405, 1407, 6912, 6910,
			6908, 6914, 4675, 18355, 15486 };

	public static boolean checkForCorrectStaff(Player client) {
		return true;
	}

	public static boolean hasStaff(Player client) {
		for (final int i : regularStaffs)
			if (client.playerEquipment[PlayerConstants.WEAPON] == i)
				return true;
		return false;
	}

	public static boolean isStaff(int j) {
		for (final int i : regularStaffs)
			if (i == j)
				return true;
		return false;
	}

	public static void newSpell(Player client, int id) {
		if (!MagicChecker.hasRunes(client, id)
				|| !MagicChecker.hasRequiredLevel(client, id)) {
			turnOff(client);
			return;
		}
		if (client.getAutoCastId() == id) {

			client.setAutoCastId(-1);
			turnOff(client);
			return;
		}
		final int spellBook = client.getSpellBook();
		if (spellBook == 2) {
			if (id > 0 && id < 1200) {
				client.getActionSender().sendMessage(
						"You're on the wrong mage book.");
				client.setAutoCastId(-1);
				turnOff(client);
				return;
			}
		} else if (spellBook == 1)
			if (id > 12000) {
				client.getActionSender().sendMessage(
						"You're on the wrong mage book.");
				client.setAutoCastId(-1);
				turnOff(client);
				return;
			}
		client.setAutoCastId(id);
		client.getActionSender().sendSidebar(0, 328);

		if (id != 0)
			turnOn(client);

		final String spellName = Magic.spell(id).getName();
		client.getActionSender().sendString(spellName, FRAME_ID);

	}

	public static void turnOff(Player client) {
		client.getActionSender().sendConfig(CONFIG_ID, OFF);
		client.setAutoCasting(false);
		client.getActionSender().sendString("Choose spell", FRAME_ID);
		client.getActionSender().sendMessage(":resetautocast:");

		client.setAutoCastId(0);
	}

	public static void turnOn(Player client) {
		client.getActionSender().sendConfig(CONFIG_ID, ON);
		client.setAutoCasting(true);
	}
}
