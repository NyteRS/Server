package com.server2.content.misc;

import com.server2.content.skills.summoning.Summoning;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Lukas
 * 
 */
public class ShardTrading {

	public static void openShardPack(Player client) {
		if (client.getActionAssistant().playerHasItem(15262)) {
			client.getActionAssistant().deleteItem(15262, 1);
			client.getActionSender().addItem(12183, 100000);
		}
	}

	public void buyShards(Player client, int amount) {
		client.getActionSender().sendWindowsRemoval();
		final int shard = 12183;
		final int price = 25;
		final int totalPrice = amount * price;
		if (amount < 1)
			client.getActionSender().sendMessage(
					"You must buy atleast 1 spirit shard.");
		if (client.getActionAssistant().playerHasItem(995, totalPrice)) {
			client.getActionAssistant().deleteItem(995, totalPrice);
			client.getActionSender().addItem(shard, amount);
		} else {
			client.getActionSender().sendMessage(
					"You don't have enough coins..");
			return;
		}

	}

	public void sellShards(Player client, int amount) {
		client.getActionSender().sendWindowsRemoval();
		final int shard = 12183;
		final int price = 25;
		int totalPrice = 0;

		if (amount < 1) {
			client.getActionSender().sendMessage(
					"You must sell atleast 1 spirit shard.");
			return;
		}
		if (amount == 123)
			amount = client.getActionAssistant().getItemAmount(shard);
		final int hasAmount = client.getActionAssistant().getItemAmount(shard);
		if (hasAmount >= amount) {
			totalPrice = price * amount;
			if (amount > 80000000) {
				amount = 80000000;
				totalPrice = price * amount;
			}
			if (totalPrice + client.getActionAssistant().getItemAmount(995) >= Integer.MAX_VALUE) {
				totalPrice = Integer.MAX_VALUE
						- client.getActionAssistant().getItemAmount(995) - 1;
				amount = totalPrice / 25;
			}

			client.getActionAssistant().deleteItem(shard, amount);
			client.getActionSender().addItem(995, totalPrice);

		} else {
			client.getActionSender().sendMessage(
					"You don't have so many spirit shards.");
			return;
		}
	}

	public void swapToShards(Player client, int itemId) {
		int pouchOrScroll = 3;
		int swapAmount = 0;

		boolean noted = true;
		for (final double[] element : Summoning.summoningInfo) {
			if (itemId == element[0]) {
				pouchOrScroll = 0;
				noted = false;

			}
			if (itemId == element[0] + 1) {
				pouchOrScroll = 0;
				noted = true;

			}
			if (itemId == element[1])
				pouchOrScroll = 1;
			// System.out.println("here");
		}
		if (pouchOrScroll == 3) {
			client.getActionSender().sendMessage(
					"You can't trade this item for shards");
			return;
		}
		for (final double[] element : Summoning.summoningInfo)
			if (element[pouchOrScroll] == itemId - 1)
				try {
					swapAmount = (int) element[10];
					if (pouchOrScroll == 1)
						swapAmount = swapAmount / 10;
				} catch (final IndexOutOfBoundsException ex) {

				}
		if (noted) {

			// ArrayOutOfBounds -1 Exception //TODO
			client.getActionAssistant().deleteItem(
					itemId,
					client.playerItemsN[client.getActionAssistant()
							.getItemSlot(itemId)]);
			if (swapAmount > 0) {
				client.getActionSender().addItem(12183, swapAmount);
				client.getActionSender().sendMessage(
						"You swap your scrolls and pouches for " + swapAmount
								+ " spirit shards.");
			} else {
				client.getActionSender()
						.sendMessage(
								"Pikkupstix won't give me any spirit shards in return.");
				return;
			}
		}
		if (!noted) {
			client.getActionSender().sendMessage(
					"Pikkupstix only accepts scrolls and noted pouches.");
			return;
		}

	}
}
