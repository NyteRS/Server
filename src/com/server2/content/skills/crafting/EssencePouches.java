package com.server2.content.skills.crafting;

import com.server2.content.Achievements;
import com.server2.model.entity.player.Language;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Lukas & Rene
 * 
 */
public class EssencePouches {

	public static final int pureEssence = 7936;

	public static int[] storageCapacity = { 3, 6, 9, 12 };

	public static void addToPouch(Player client, int itemID, int amount) {
		if (client.getActionAssistant().isItemInBag(pureEssence)) {

			int succesfull = 0;
			switch (itemID) {

			case 5509:
				if (client.smallPouch >= storageCapacity[0]) {
					client.getActionSender().sendMessage(Language.pouchFull);
					return;
				}

				for (int i = 0; i < storageCapacity[0]; i++)
					if (client.smallPouch < 3
							&& client.getActionAssistant().isItemInBag(
									pureEssence)) {
						client.smallPouch = client.smallPouch + 1;
						client.getActionAssistant().deleteItem(pureEssence, 1);
						succesfull = succesfull + 1;

					}
				if (succesfull == 1)
					client.getActionSender().sendMessage(
							"You added " + succesfull
									+ " essence to your pouch.");
				else
					client.getActionSender().sendMessage(
							"You added " + succesfull
									+ " essences to your pouch.");
				break;
			case 5511:
				if (client.mediumPouch >= storageCapacity[1]) {
					client.getActionSender().sendMessage(Language.pouchFull);
					return;
				}

				for (int i = 0; i < storageCapacity[1]; i++) {
					if (client.mediumPouch < 6
							&& client.getActionAssistant().isItemInBag(
									pureEssence)) {
						client.mediumPouch = client.mediumPouch + 1;
						client.getActionAssistant().deleteItem(pureEssence, 1);
						succesfull = succesfull + 1;

					}
					Achievements.getInstance().complete(client, 41);

				}
				if (succesfull == 1)
					client.getActionSender().sendMessage(
							"You added " + succesfull
									+ " essence to your pouch.");
				else
					client.getActionSender().sendMessage(
							"You added " + succesfull
									+ " essences to your pouch.");
				break;
			case 5512:
				if (client.largePouch >= storageCapacity[2]) {
					client.getActionSender().sendMessage(Language.pouchFull);
					return;
				}

				for (int i = 0; i < storageCapacity[2]; i++)
					if (client.largePouch < 9
							&& client.getActionAssistant().isItemInBag(
									pureEssence)) {
						client.largePouch = client.largePouch + 1;
						client.getActionAssistant().deleteItem(pureEssence, 1);
						succesfull = succesfull + 1;

					}
				if (succesfull == 1)
					client.getActionSender().sendMessage(
							"You added " + succesfull
									+ " essence to your pouch.");
				else
					client.getActionSender().sendMessage(
							"You added " + succesfull
									+ " essences to your pouch.");
				break;
			case 5515:
				if (client.giantPouch >= storageCapacity[3]) {
					client.getActionSender().sendMessage(Language.pouchFull);
					return;
				}

				for (int i = 0; i < storageCapacity[3]; i++)
					if (client.giantPouch < 12
							&& client.getActionAssistant().isItemInBag(
									pureEssence)) {
						client.giantPouch = client.giantPouch + 1;
						client.getActionAssistant().deleteItem(pureEssence, 1);
						succesfull = succesfull + 1;

					}
				if (succesfull == 1)
					client.getActionSender().sendMessage(
							"You added " + succesfull
									+ " essence to your pouch.");
				else
					client.getActionSender().sendMessage(
							"You added " + succesfull
									+ " essences to your pouch.");
				break;
			}
		} else
			client.getActionSender()
					.sendMessage("You do not have any essence.");
	}

	public static void checkEssence(Player client, int itemId) {
		if (itemId == 5509)
			client.getActionSender().sendMessage(
					"Your pouch currently stores " + client.smallPouch
							+ "pure essences.");
		else if (itemId == 5511)
			client.getActionSender().sendMessage(
					"Your pouch currently stores " + client.mediumPouch
							+ "pure essences.");
		else if (itemId == 5512)
			client.getActionSender().sendMessage(
					"Your pouch currently stores " + client.largePouch
							+ "pure essences.");
		else if (itemId == 5515)
			client.getActionSender().sendMessage(
					"Your pouch currently stores " + client.giantPouch
							+ "pure essences.");

	}

	public static void empty(Player client, int itemId) {
		if (client.smallPouch == 0 || client.mediumPouch == 0
				|| client.largePouch == 0 || client.giantPouch == 0)
			client.getActionSender().sendMessage(
					"Your pouch does not have anymore essence.");
		if (itemId == 5509) {
			final int soManyTimes = client.smallPouch;
			for (int i = 0; i < soManyTimes; i++) {
				final int dehSlots = client.getActionAssistant().freeSlots();
				if (dehSlots > 0) {
					client.getActionSender().addItem(pureEssence, 1);
					client.smallPouch--;
				}

			}
		} else if (itemId == 5511) {
			final int soManyTimes = client.mediumPouch;
			for (int i = 0; i < soManyTimes; i++) {
				final int dehSlots = client.getActionAssistant().freeSlots();
				if (dehSlots > 0) {
					client.getActionSender().addItem(pureEssence, 1);
					client.mediumPouch--;
				}
			}
		} else if (itemId == 5512) {

			final int soManyTimes = client.largePouch;
			for (int i = 0; i < soManyTimes; i++) {
				final int dehSlots = client.getActionAssistant().freeSlots();
				if (dehSlots > 0) {
					client.getActionSender().addItem(pureEssence, 1);
					client.largePouch--;
				}
			}
		} else if (itemId == 5515) {
			final int soManyTimes = client.giantPouch;
			for (int i = 0; i < soManyTimes; i++) {
				final int dehSlots = client.getActionAssistant().freeSlots();
				if (dehSlots > 0) {
					client.getActionSender().addItem(pureEssence, 1);
					client.giantPouch--;
				}
			}
		}
	}

	public static void fillPouch(Player client, int itemID, int amount) {
		addToPouch(client, itemID, amount);
	}
}
