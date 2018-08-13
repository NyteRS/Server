package com.server2.content.skills.crafting;

import com.server2.InstanceDistributor;
import com.server2.content.actions.CraftHide;
import com.server2.model.entity.Entity;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author killamess & Lukas
 * 
 */

public class HideCrafting {

	public static final int[][] dragonhide = {

	{ 1065, 1099, 1135 }, // green
			{ 2487, 2493, 2499 }, // blue
			{ 2489, 2495, 2501 }, // red
			{ 2491, 2497, 2503 }, // black
			{ 1059, 1061, 1167 }, { -1, 1131, -1 }, };

	public static void craftLeather(Player client, int item, int amount) {
		// System.out.println("item"+item);
		client.craftingItem = item;
		// System.out.println(client.craftingItem);
		client.craftingAmount = amount;
		CraftHide.loop(client);
	}

	public static void createDragonHideInterface(Entity entity, int set) {

		if (set > 5 || set < 0)
			return;

		if (!(entity instanceof Player))
			return;

		final Player client = (Player) entity;

		final String[] hideNames = new String[3];

		hideNames[0] = InstanceDistributor.getItemManager()
				.getItemDefinition(dragonhide[set][0]).getName();
		hideNames[1] = InstanceDistributor.getItemManager()
				.getItemDefinition(dragonhide[set][1]).getName();
		hideNames[2] = InstanceDistributor.getItemManager()
				.getItemDefinition(dragonhide[set][2]).getName();
		if (set == 5) {
			hideNames[0] = " ";
			hideNames[1] = "Hardleather body";
			hideNames[2] = " ";
		}

		client.getActionSender().sendFrame164(8880); // 8880
		client.getActionSender().sendString(hideNames[0], 8897); // 8897

		client.getActionSender().sendString(hideNames[1], 8893); // 8893

		client.getActionSender().sendString(hideNames[2], 8889); // 8889

		client.getActionSender().sendFrame246(8883, 250, dragonhide[set][0]);
		client.getActionSender().sendFrame246(8884, 200, dragonhide[set][1]);
		client.getActionSender().sendFrame246(8885, 200, dragonhide[set][2]);
	}
}
