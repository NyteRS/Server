package com.server2.content.skills.fletching;

import com.server2.InstanceDistributor;
import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;

/**
 * 
 * @author Rene
 * 
 */
public class Fletching {

	public static int[] stringXp = { 5, 10, 17, 25, 33, 42, 50, 58, 67, 75, 83,
			92 };

	public static boolean chooseItem(Player client, int log) {
		if (client.isBusy())
			return true;

		client.getActionSender().sendWindowsRemoval();
		client.getActionSender().sendFrame164(8880);
		client.getActionSender().sendFrame246(
				8883,
				200,
				getIntArray(PlayerConstants.LOGS, PlayerConstants.LEFT_ITEM,
						log));
		client.getActionSender().sendFrame246(8884, 200, log == 1511 ? 52 : -1);
		client.getActionSender().sendFrame246(
				8885,
				200,
				getIntArray(PlayerConstants.LOGS, PlayerConstants.RIGHT_ITEM,
						log));
		client.getActionSender().sendString(
				getStringArray(PlayerConstants.LOGS,
						PlayerConstants.LEFT_ITEM_NAME, log), 8897);

		client.getActionSender().sendString(log == 1511 ? "Arrow Shaft" : "",
				8893);

		client.getActionSender().sendString(
				getStringArray(PlayerConstants.LOGS,
						PlayerConstants.RIGHT_ITEM_NAME, log), 8889);

		setLogId(client, log);

		return true;
	}

	public static int contains(int[] array, int value) {
		for (final int i : array)
			if (i == value)
				return value;
		return 0;
	}

	public static boolean createArrows(Player client, int item) {

		if (client.isBusy())
			return true;

		final int amount = client.getActionAssistant().getItemAmount(item);

		final int otherAmount = client.getActionAssistant().getItemAmount(53);

		if (amount < 15 || otherAmount < 15)
			return true;
		if (client.getActionAssistant().freeSlots() >= 1) {
			if (client.playerLevel[PlayerConstants.FLETCHING] >= getIntArray(
					PlayerConstants.ARROW_HEADS, PlayerConstants.ARROW_LEVELS,
					item)) {
				client.getActionAssistant().deleteItem(53,
						amount > 15 ? 15 : amount);
				client.getActionAssistant().deleteItem(item,
						amount > 15 ? 15 : amount);
				client.getActionSender().addItem(
						getIntArray(PlayerConstants.ARROW_HEADS,
								PlayerConstants.ARROWSX, item), 15);
				client.getActionAssistant().addSkillXP(
						getIntArray(PlayerConstants.ARROW_HEADS,
								PlayerConstants.ARROW_EXPERIENCE, item)
								* PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
						PlayerConstants.FLETCHING);
				client.getActionSender().sendMessage("You make some arrows.");
			} else
				client.getActionSender().sendMessage(
						"You need a fletching level of"
								+ " "
								+ getIntArray(PlayerConstants.ARROW_HEADS,
										PlayerConstants.ARROW_LEVELS, item)
								+ " to make these arrows.");
		} else
			client.getActionSender().sendMessage(
					"You have no space in your inventory");
		return true;
	}

	public static boolean createDarts(Player client, int item) {

		if (client.isBusy())
			return true;
		final int amount = client.getActionAssistant().getItemAmount(item);
		final int otherAmount = client.getActionAssistant().getItemAmount(314);

		if (amount < 15 || otherAmount < 15)
			return true;
		if (client.getActionAssistant().freeSlots() >= 1) {
			if (client.playerLevel[PlayerConstants.FLETCHING] >= getIntArray(
					PlayerConstants.DART_TIPS, PlayerConstants.DART_LEVELS,
					item)) {
				client.getActionAssistant().deleteItem(314,
						amount > 15 ? 15 : amount);
				client.getActionAssistant().deleteItem(item,
						amount > 15 ? 15 : amount);
				client.getActionSender().addItem(
						getIntArray(PlayerConstants.DART_TIPS,
								PlayerConstants.DARTS, item),
						amount > 15 ? 15 : amount);
				client.getActionAssistant().addSkillXP(
						getIntArray(PlayerConstants.DART_TIPS,
								PlayerConstants.DART_EXPERIENCE, item)
								* PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
						PlayerConstants.FLETCHING);
				client.getActionSender().sendMessage("You make some darts.");
			} else
				client.getActionSender().sendMessage(
						"You need a fletching level of"
								+ " "
								+ getIntArray(PlayerConstants.DART_TIPS,
										PlayerConstants.DART_LEVELS, item)
								+ " to make these darts.");
		} else
			client.getActionSender().sendMessage(
					"You have no space in your inventory");
		return true;
	}

	public static boolean createHeadlessArrows(Player client, int item) {
		if (client.isBusy())
			return true;
		final int amount = client.getActionAssistant().getItemAmount(item);
		final int otherAmount = client.getActionAssistant().getItemAmount(314);

		if (amount < 15 || otherAmount < 15)
			return true;
		if (client.getActionAssistant().freeSlots() >= 1) {
			client.getActionAssistant().deleteItem(314,
					amount > 15 ? 15 : amount);
			client.getActionAssistant().deleteItem(item,
					amount > 15 ? 15 : amount);
			client.getActionSender().addItem(53, amount > 15 ? 15 : amount);
			client.getActionAssistant().addSkillXP(
					100 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
					PlayerConstants.FLETCHING);
			client.getActionSender().sendMessage(
					"You make some headless arrows.");
		} else
			client.getActionSender().sendMessage(
					"You have no space in your inventory");
		return true;
	}

	public static int getIntArray(int[] array1, int[] array2, int bow) {
		int a = 0;
		for (final int object : array1) {
			if (object == bow)
				return array2[a];
			a++;
		}
		return -1;
	}

	public static int getLogId(Player client) {
		if (client.getExtraData().get("logId") == null)
			return -1;
		final int log = (Integer) client.getExtraData().get("logId");
		return log;
	}

	public static String getStringArray(int[] array1, String[] array2, int bow) {
		int a = 0;
		for (final int object : array1) {
			if (object == bow)
				return array2[a];
			a++;
		}
		return "";
	}

	public static boolean isFletchable(Player client, int useItem, int usedItem) {

		if (useItem == 946
				&& usedItem == contains(PlayerConstants.LOGS, usedItem)
				|| useItem == contains(PlayerConstants.LOGS, useItem)
				&& usedItem == 946)
			return chooseItem(
					client,
					useItem == contains(PlayerConstants.LOGS, useItem) ? useItem
							: usedItem);
		else if (useItem == 1777
				&& (usedItem == contains(PlayerConstants.UNSTRUNG_BOWS,
						usedItem) || useItem == contains(
						PlayerConstants.UNSTRUNG_BOWS, useItem)))
			return stringBow(
					client,
					useItem == contains(PlayerConstants.UNSTRUNG_BOWS, useItem) ? useItem
							: usedItem);
		else if (useItem == 52 && usedItem == 314 || useItem == 314
				&& usedItem == 52)
			return createHeadlessArrows(client, 52);
		else if (useItem == 53
				&& usedItem == contains(PlayerConstants.ARROW_HEADS, usedItem)
				|| useItem == contains(PlayerConstants.ARROW_HEADS, useItem)
				&& usedItem == 53)
			return createArrows(
					client,
					useItem == contains(PlayerConstants.ARROW_HEADS, useItem) ? useItem
							: usedItem);
		else if (useItem == 314
				&& usedItem == contains(PlayerConstants.DART_TIPS, usedItem)
				|| useItem == contains(PlayerConstants.DART_TIPS, useItem)
				&& usedItem == 314)
			return createDarts(
					client,
					useItem == contains(PlayerConstants.DART_TIPS, useItem) ? useItem
							: usedItem);
		else
			return false;
	}

	public static void loop(final Player client) {
		client.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!client.actionSet) {
					client.actionSet = true;
					client.currentActivity = this;
				}
				if (client != null) {
					if (client.isStopRequired()) {
						container.stop();
						return;
					}
					if (client.started) {

					} else
						client.started = true;

					if (client.amountLeft == 0 || client.log2 == -1) {
						client.started = false;
						container.stop();
						return;
					}

					if (client.length == "shortbow")
						client.unstrungBow = getIntArray(PlayerConstants.LOGS,
								PlayerConstants.LEFT_ITEM, client.log2);
					else if (client.length == "longbow")
						client.unstrungBow = getIntArray(PlayerConstants.LOGS,
								PlayerConstants.RIGHT_ITEM, client.log2);
					else
						client.unstrungBow = 52;

					if (client.getActionAssistant().freeSlots() >= 0) {
						if (client.getActionAssistant().getItemAmount(
								client.log2) > 0) {
							if (client.playerLevel[PlayerConstants.FLETCHING] >= getIntArray(
									PlayerConstants.UNSTRUNG_BOWS,
									PlayerConstants.FLETCHING_LEVELS,
									client.unstrungBow)) {
								AnimationProcessor.addNewRequest(client, 1248,
										1);
								client.getActionAssistant().deleteItem(
										client.log2, 1);
								client.getActionSender().addItem(
										client.unstrungBow == 52 ? 52
												: client.unstrungBow,
										client.unstrungBow == 52 ? 15 : 1);
								client.getActionAssistant()
										.addSkillXP(
												client.unstrungBow == 52 ? 5 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER
														: getIntArray(
																PlayerConstants.UNSTRUNG_BOWS,
																PlayerConstants.EXPERIENCE,
																client.unstrungBow)
																* PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
												PlayerConstants.FLETCHING);
								client.getActionSender().sendMessage(
										"You fletch the bow.");
								client.amountLeft--;
							} else {
								client.getActionSender()
										.sendMessage(
												"You need a fletching level of "
														+ getIntArray(
																PlayerConstants.UNSTRUNG_BOWS,
																PlayerConstants.FLETCHING_LEVELS,
																client.unstrungBow)
														+ " to fletch that bow.");
								client.started = false;
								container.stop();
								return;
							}
						} else {
							client.getActionSender().sendMessage(
									"You don't have the item to fletch");
							client.started = false;
							container.stop();
							return;
						}
					} else {
						client.getActionSender().sendMessage(
								"You have no space in your inventory");
						client.started = false;
						container.stop();
						return;
					}

				}
			}

			@Override
			public void stop() {
				client.getActionSender().sendAnimationReset();
				client.getActionSender().sendWindowsRemoval();
				client.actionSet = false;
				client.currentActivity = null;
			}

		}, PlayerConstants.FLETCHING_DELAY);
	}

	public static void setLogId(Player client, int id) {
		client.getExtraData().put("logId", id);
	}

	public static void startFletching(Player client, int amount, String length) {
		if (client.isBusy())
			return;
		client.setBusy(true);
		client.length = length;
		client.amountLeft = amount;
		client.log2 = getLogId(client);
		client.unstrungBow = 0;
		// client.FLETCHING_DELAY = 4;
		loop(client);
	}

	public static boolean stringBow(Player client, int bow) {

		if (client.isBusy())
			return true;

		if (client.playerLevel[PlayerConstants.FLETCHING] >= getIntArray(
				PlayerConstants.UNSTRUNG_BOWS,
				PlayerConstants.FLETCHING_LEVELS, bow)) {
			client.getActionAssistant().deleteItem(bow, 1);
			client.getActionAssistant().deleteItem(1777, 1);
			client.getActionAssistant().addSkillXP(
					xp(bow) * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
					PlayerConstants.FLETCHING);
			client.getActionSender().addItem(
					getIntArray(PlayerConstants.UNSTRUNG_BOWS,
							PlayerConstants.STRUNG_BOWS, bow), 1);
			client.getActionSender().sendMessage(
					"You attach the bowstring to the bow.");
		} else
			client.getActionSender().sendMessage(
					"You need a fletching level of"
							+ " "
							+ getIntArray(PlayerConstants.UNSTRUNG_BOWS,
									PlayerConstants.FLETCHING_LEVELS, bow)
							+ " to string that bow.");
		return true;
	}

	public static int whichOne(int itemID) {
		int logId = 0;
		final String name = InstanceDistributor.getItemManager()
				.getItemDefinition(itemID).getName();
		if (name.contains("oak") || name.contains("Oak"))
			logId = 1;
		else if (name.contains("willow") || name.contains("Willow"))
			logId = 2;
		else if (name.contains("maple") || name.contains("Maple"))
			logId = 3;
		else if (name.contains("yew") || name.contains("Yew"))
			logId = 4;
		else if (name.contains("magic") || name.contains("Magic"))
			logId = 5;

		return logId;
	}

	public static int xp(int itemID) {
		int xp = 5;
		final String name = InstanceDistributor.getItemManager()
				.getItemDefinition(itemID).getName();
		int extra = 0;
		if (name.contains("longbow") || name.contains("Longbow"))
			extra = 1;
		xp = stringXp[whichOne(itemID) + extra];
		return xp;
	}
}
