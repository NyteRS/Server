package com.server2.model.combat.additions;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class ArmourDegradation {

	public static final int DEFAULT = 500;
	private static final int[] PVP_HELMS = new int[] { 13920, 13950, 13938,
			15000 };
	private static final int[] PVP_HELMSDegraded = new int[] { 13922, 13952,
			13940, 15000 };
	private static final int[] PVP_CHESTS = new int[] { 13908, 13944, 13932,
			13911 };
	private static final int[] PVP_CHESTSDegraded = new int[] { 13910, 13946,
			13934, 13913 };
	private static final int[] PVP_LEGS = new int[] { 13914, 13947, 13935,
			13917 };
	private static final int[] PVP_LEGSDegraded = new int[] { 13916, 13949,
			13937, 13919 };
	private static final int[] PVP_WEAPONS = new int[] { 13926, 13929, 13941,
			13923 };
	private static final int[] PVP_WEAPONSDegraded = new int[] { 13928, 13931,
			13943, 13925 };

	public static void degradeArmour(Player client) {
		if (!client.floor1() && !client.floor2() && !client.floor3()) {
			int i;
			for (i = 0; i < PVP_HELMS.length; ++i)
				if (client.playerEquipment[PlayerConstants.HELM] == PVP_HELMS[i]
						&& (client.pvpHelms -= 1) == 0) {
					client.pvpHelms = 500;
					client.getEquipment().deleteEquipment(PlayerConstants.HELM);
					client.getActionSender().sendMessage(
							"Your helmet crumbles to dust.");
				} else if (client.playerEquipment[PlayerConstants.HELM] == PVP_HELMS[i]
						&& (client.pvpHelms -= 1) != 0
						|| client.playerEquipment[PlayerConstants.HELM] == PVP_HELMSDegraded[i]
						&& client.pvpHelms != 0) {
					client.pvpHelms--;
					if (client.playerEquipment[PlayerConstants.HELM] == PVP_HELMS[i]) {
						client.getActionSender().sendMessage(
								"Your helm degrades.");
						client.getEquipment().setEquipment(
								PVP_HELMSDegraded[i], 1, PlayerConstants.HELM);
					}

				}

			for (i = 0; i < PVP_CHESTS.length; ++i)
				if (client.playerEquipment[PlayerConstants.CHEST] == PVP_CHESTS[i]
						&& (client.pvpChests -= 1) == 0) {
					client.pvpChests = 500;
					client.getEquipment()
							.deleteEquipment(PlayerConstants.CHEST);
					client.getActionSender().sendMessage(
							"Your chest armour crumbles to dust.");
				} else if (client.playerEquipment[PlayerConstants.CHEST] == PVP_CHESTS[i]
						&& (client.pvpChests -= 1) != 0
						|| client.playerEquipment[PlayerConstants.CHEST] == PVP_CHESTSDegraded[i]
						&& client.pvpChests != 0) {
					client.pvpChests--;
					if (client.playerEquipment[PlayerConstants.CHEST] == PVP_CHESTS[i]) {
						client.getActionSender().sendMessage(
								"Your chest degrades.");
						client.getEquipment()
								.setEquipment(PVP_CHESTSDegraded[i], 1,
										PlayerConstants.CHEST);
					}
				}
		}
		for (int i = 0; i < PVP_LEGS.length; ++i)
			if (client.playerEquipment[PlayerConstants.BOTTOMS] == PVP_LEGS[i]
					&& (client.pvpLegs -= 1) <= 0) {
				client.pvpLegs = 500;
				client.getEquipment().deleteEquipment(PlayerConstants.BOTTOMS);
				client.getActionSender().sendMessage(
						"Your legs crumbles to dust.");
			} else if (client.playerEquipment[PlayerConstants.BOTTOMS] == PVP_LEGS[i]
					&& client.pvpLegs != 0
					|| client.playerEquipment[PlayerConstants.BOTTOMS] == PVP_LEGSDegraded[i]
					&& client.pvpLegs != 0) {
				client.pvpLegs--;
				if (client.playerEquipment[PlayerConstants.BOTTOMS] == PVP_LEGS[i]) {
					client.getActionSender().sendMessage("Your legs degrade.");
					client.getEquipment().setEquipment(PVP_LEGSDegraded[i], 1,
							PlayerConstants.BOTTOMS);
				}
			}
	}

	public static void degradeWeapon(Player client) {
		if (!client.floor1() && !client.floor2() && !client.floor3())
			for (int i = 0; i < PVP_WEAPONS.length; ++i)
				if (client.playerEquipment[PlayerConstants.WEAPON] == PVP_WEAPONS[i]
						&& (client.pvpWeapons -= 1) <= 0) {
					client.pvpWeapons = 500;
					client.getEquipment().deleteEquipment(
							PlayerConstants.WEAPON);
					client.getActionSender().sendMessage(
							"Your weapon crumbles to dust.");
				} else if (client.playerEquipment[PlayerConstants.WEAPON] == PVP_WEAPONS[i]
						&& client.pvpWeapons != 0
						|| client.playerEquipment[PlayerConstants.WEAPON] == PVP_WEAPONSDegraded[i]
						&& client.pvpWeapons != 0) {
					client.pvpWeapons--;
					if (client.playerEquipment[PlayerConstants.WEAPON] == PVP_WEAPONS[i]) {
						client.getActionSender().sendMessage(
								"Your weapon degrades.");
						client.getEquipment().setEquipment(
								PVP_WEAPONSDegraded[i], 1,
								PlayerConstants.WEAPON);
						client.getEquipment().sendWeapon();
					}
				}
	}
}
