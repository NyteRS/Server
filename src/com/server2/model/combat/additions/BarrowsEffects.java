package com.server2.model.combat.additions;

import com.server2.model.entity.Entity;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Misc;

/**
 * 
 * @author killamess/Lukas
 * 
 */
public class BarrowsEffects {

	/**
	 * Dharoks.
	 */
	public static final int DHAROKS = 0;

	public static int[][] dharokSets = { { 4716, 4720, 4722, 4718 }, // full set
			{ 4880, 4892, 4898, 4886 }, // 100
			{ 4881, 4893, 4899, 4887 }, // 75
			{ 4882, 4894, 4900, 4888 }, // 50
			{ 4883, 4895, 4901, 4889 }, // 25
			{ 4884, 4896, 4902, 4890 }, // 0
	};

	public static void ahrimsEffect(Player client, Entity attacker,
			Entity target, int damage) {
		final Player target1 = (Player) target;
		if (client.playerHasAhrims()) {

			if (Misc.random(5) == 1) {
				target1.playerLevel[2] = target1.playerLevel[2] - 5;
				GraphicsProcessor.addNewRequest(target, 401, 0, 0);
			}
			if (target1.playerLevel[2] < 1)
				target1.playerLevel[2] = 1;
		}
	}

	public static boolean fullDharoks(Player client) {

		int count = 0;

		for (final int[] dharokSet : dharokSets) {
			if (client.playerEquipment[PlayerConstants.HELM] == dharokSet[0])
				count++;
			if (client.playerEquipment[PlayerConstants.CHEST] == dharokSet[1])
				count++;
			if (client.playerEquipment[PlayerConstants.BOTTOMS] == dharokSet[2])
				count++;
			if (client.playerEquipment[PlayerConstants.WEAPON] == dharokSet[3])
				count++;
		}
		return count == 4;
	}

	public static void guthansEffect(Player client, Entity attacker,
			Entity target, int damage) {
		final Player attacker1 = (Player) attacker;
		if (client.playerHasGuthans()) {

			if (Misc.random(20) == 1) {
				attacker1.getActionAssistant().addHP(damage);
				GraphicsProcessor.addNewRequest(target, 398, 0, 0);
			}
			if (attacker1.hitpoints > attacker1.calculateMaxHP()) {
				attacker1.hitpoints = attacker1.calculateMaxHP();
				attacker1.playerLevel[3] = attacker1.hitpoints;
			}
		}
	}

	/**
	 * public static void degradeDharoks(Player client, int damage) {
	 * 
	 * boolean[] sectionFound = new boolean[4];
	 * 
	 * client.setDharokDamage(client.getDharokDamage() + damage);
	 * 
	 * if (client.getDharokDamage() < 1000) { if (!hasNewDharoks(client))
	 * return; } for (int locator = 0; locator < dharokSets.length; locator++) {
	 * 
	 * if (locator == 5) break;
	 * 
	 * if (client.playerEquipment[PlayerConstants.HELM] ==
	 * dharokSets[locator][0] && !sectionFound[0]) {
	 * client.getEquipment().setEquipment(dharokSets[locator + 1][0], 1,
	 * PlayerConstants.HELM); sectionFound[0] = true; } if
	 * (client.playerEquipment[PlayerConstants.CHEST] == dharokSets[locator][1]
	 * && !sectionFound[1]) {
	 * client.getEquipment().setEquipment(dharokSets[locator + 1][1], 1,
	 * PlayerConstants.CHEST); sectionFound[1] = true; } if
	 * (client.playerEquipment[PlayerConstants.BOTTOMS] ==
	 * dharokSets[locator][2] && !sectionFound[2]) {
	 * client.getEquipment().setEquipment(dharokSets[locator + 1][2], 1,
	 * PlayerConstants.BOTTOMS); sectionFound[2] = true; } if
	 * (client.playerEquipment[PlayerConstants.WEAPON] == dharokSets[locator][3]
	 * && !sectionFound[3]) {
	 * client.getEquipment().setEquipment(dharokSets[locator + 1][3], 1,
	 * PlayerConstants.WEAPON); sectionFound[3] = true; } }
	 * client.resetDharokDamage();
	 * client.getActionSender().sendMessage("Your armor has degraded!"); }
	 */

	public static boolean hasNewDharoks(Player client) {
		int count = 0;
		if (client.playerEquipment[PlayerConstants.HELM] == dharokSets[0][0])
			count++;
		if (client.playerEquipment[PlayerConstants.CHEST] == dharokSets[0][1])
			count++;
		if (client.playerEquipment[PlayerConstants.BOTTOMS] == dharokSets[0][2])
			count++;
		if (client.playerEquipment[PlayerConstants.WEAPON] == dharokSets[0][3])
			count++;
		return count == 4;
	}

}
