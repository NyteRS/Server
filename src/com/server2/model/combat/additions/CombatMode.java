package com.server2.model.combat.additions;

import com.server2.InstanceDistributor;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;

/**
 * 
 * @author killamess
 * 
 */
public class CombatMode {

	public static String[] combatModes = { "Attack", "Defence", "Strength",
			"Controlled", "Accurate", "Rapid", "LongRanged" };

	public static int findCombatMode(int weapon) {
		for (int i = 0; i < 6; i++)
			if (hasCombatMode(weapon, i))
				return i;
		return 0;
	}

	public static int findCombatMode(Player client, int weapon) {
		for (int i = 0; i < 6; i++) {
			if (i < 4 && hasCombatMode(weapon, i) && client.lastRangeMode == i)
				return i;
			else if (i < 4 && hasCombatMode(weapon, i)
					&& client.lastMeleeMode == -1) {
				client.lastMeleeMode = i;
				return i;
			}
			if (i > 3 && hasCombatMode(weapon, i) && client.lastRangeMode == i)
				return i;
			else if (i > 3 && hasCombatMode(weapon, i)
					&& client.lastRangeMode == -1) {
				client.lastRangeMode = i;
				return i;
			}
		}
		return -1;
	}

	public static int getBoxCount(int weapon, int mode) {
		String s = "";
		if (weapon <= 2)
			s = "Unarmed";
		else
			s = InstanceDistributor.getItemManager().getItemDefinition(weapon)
					.getName();
		if (s.equals("Unarmed"))
			return 3;
		else if (s.endsWith("whip"))
			return 3;
		else if (s.endsWith("Scythe"))
			return 4;
		else if (s.endsWith("bow") || s.startsWith("Crystal bow")
				|| s.startsWith("Seercull"))
			return 3;
		else if (s.startsWith("Staff") || s.endsWith("staff"))
			return 4;
		else if (s.endsWith("dart") || s.endsWith("knife")
				|| s.endsWith("javelin") || s.contains("thrownaxe"))
			return 3;
		else if (s.contains("dagger"))
			return 4;
		else if (s.endsWith("pickaxe"))
			return 3;
		else if (s.endsWith("axe") || s.endsWith("battleaxe"))
			return 3;
		else if (s.endsWith("halberd"))
			return 3;
		else if (s.contains("spear"))
			return 3;
		else if (s.contains("maul"))
			return 3;
		else if (s.endsWith("mace"))
			return 4;
		else if (s.endsWith("claws"))
			return 4;
		else if (s.endsWith("spear"))
			return 4;
		else
			return 4;
	}

	public static boolean hasCombatMode(int weapon, int mode) {
		if (InstanceDistributor.getItemManager().getItemDefinition(weapon) == null)
			return false;
		final String s = InstanceDistributor.getItemManager()
				.getItemDefinition(weapon).getName();
		if (s.equals("Unarmed"))
			return mode >= 0 && mode <= 2;
		else if (s.endsWith("whip"))
			return mode == 0 || mode == 1 || mode == 3;
		else if (s.endsWith("Scythe"))
			return mode >= 0 && mode <= 3;
		else if (s.endsWith("bow") || s.startsWith("Crystal bow")
				|| s.startsWith("Seercull"))
			return mode >= 4 && mode <= 6;
		else if (s.startsWith("Staff") || s.endsWith("staff"))
			return mode >= 0 && mode <= 3;
		else if (s.endsWith("dart") || s.endsWith("knife")
				|| s.endsWith("javelin") || s.contains("thrownaxe"))
			return mode >= 4 && mode <= 6;
		else if (s.contains("dagger"))
			return mode >= 0 && mode <= 3;
		else if (s.endsWith("pickaxe"))
			return mode >= 0 && mode <= 2;
		else if (s.endsWith("axe") || s.endsWith("battleaxe"))
			return mode >= 0 && mode <= 2;
		else if (s.endsWith("halberd"))
			return mode == 0 || mode == 1 || mode == 3;
		else if (s.contains("spear"))
			return mode == 0 || mode == 1 || mode == 3;
		else if (s.contains("maul"))
			return mode >= 0 && mode <= 2;
		else if (s.endsWith("mace"))
			return mode >= 0 && mode <= 3;
		else if (s.endsWith("claws"))
			return mode >= 0 && mode <= 3;
		else if (s.endsWith("spear"))
			return mode >= 0 && mode <= 3;
		else
			return mode >= 0 && mode <= 3;
	}

	public static int setCombatMode(Player client, int requestedMode) {

		final int weapon = client.playerEquipment[PlayerConstants.WEAPON];
		int newMode = -1;

		if (hasCombatMode(weapon, requestedMode)) {

			if (requestedMode > 3)
				client.lastRangeMode = requestedMode;
			else
				client.lastMeleeMode = requestedMode;

			setConfigId(client, requestedMode);
			return requestedMode;

		} else if (hasCombatMode(weapon, client.lastMeleeMode)) {
			if (client.lastMeleeMode < 4)
				newMode = client.lastMeleeMode;

		} else if (hasCombatMode(weapon, client.lastRangeMode))
			if (client.lastRangeMode > 3)
				newMode = client.lastRangeMode;

		if (newMode == -1)
			newMode = findCombatMode(weapon);

		setConfigId(client, newMode);

		return newMode;
	}

	public static void setConfigId(Player client, int mode) {

		final int boxCount = getBoxCount(
				client.playerEquipment[PlayerConstants.WEAPON], mode);

		if (client.playerEquipment[PlayerConstants.WEAPON] < 1) {
			if (mode == 0)
				mode = 1;
			else if (mode == 1)
				mode = 0;
			else if (mode == 2)
				mode = 2;
		} else if (mode == 3) { // controlled
			if (boxCount == 3)
				mode = 1;
			else
				mode = 2;

		} else if (mode == 1) { // defence
			if (boxCount == 3)
				mode = 2;
			else
				mode = 3;

		} else if (mode == 2)
			mode = 1;
		else if (mode > 3)
			if (mode == 4)
				mode = 0;
			else if (mode == 5)
				mode = 1;
			else if (mode == 6)
				mode = 2;
		client.getActionSender().sendConfig(43, mode);
		client.getEquipment().sendWeapon();
	}
}
