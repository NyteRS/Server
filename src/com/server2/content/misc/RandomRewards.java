package com.server2.content.misc;

import com.server2.InstanceDistributor;
import com.server2.content.Achievements;
import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.entity.player.Language;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;

/**
 * 
 * @author Lukas P
 * 
 */
public class RandomRewards {

	public static final int[] junkRewards = { 1432, 20, 9185, 20, 18830, 18,
			1187, 17, 5316, 12, 5304, 15, 1601, 20, 11732, 8, 4675, 15, 1127,
			18, 1712, 9, 12093, 5, 12790, 4 };// 181 0.10$ 16
	public static final int[] lowRewards = { 4716, 5, 4720, 5, 4710, 5, 4753,
			5, 4716, 5, 4718, 5, 4722, 5, 4759, 5, 4757, 5, 4738, 5, 4747, 5,
			4151, 3, 3140, 2, 11235, 4, 4214, 6, 11728, 6, 6916, 3, 6918, 4,
			6920, 3, 6922, 3, 6924, 4, 6914, 2, 6889, 2 };// 51
															// 1$
															// 14
	public static final int[] medRewards = { 13890, 12, 19784, 15, 11335, 6,
			14479, 8, 6585, 20, 13887, 10, 11700, 25, 11698, 20, 21787, 22,
			11726, 20, 11720, 20, 13744, 15, 13899, 3, 15259, 10 };// 196 6$ 9
	public static final int[] highRewards = { 6570, 15, 1050, 1, 1053, 1, 1055,
			1, 1057, 1, 1961, 1, 13742, 3 };// 36 28$ 3
	public static final int[] extremeRewards = { 1042, 1, 1040, 6, 1044, 6,
			1048, 5, 1038, 6, 1046, 7, 23639, 15, 20135, 10, 20139, 10, 20143,
			10, 20159, 10, 20163, 10, 20167, 10, 20147, 10, 20151, 10, 20155,
			10 };// 139 75$ 1
	public static final int[] skillBoxRewards = { 7390, 15, 7392, 25, 7394, 15,
			7396, 25, 7386, 15, 7388, 25, 10400, 30, 10402, 30, 10404, 30,
			10406, 30, 10408, 30, 10410, 30, 10412, 30, 10414, 30, 10416, 30,
			10418, 30, 10420, 30, 10420, 30, 10422, 30, 10424, 30, 10426, 30,
			10428, 30, 10430, 30, 10432, 30, 10434, 30, 10436, 30, 10438, 30,
			10748, 20, 10727, 2, 10728, 2, 10726, 2, 2651, 35, 2651, 35, 2997,
			5, 30, 30, 30, 30, 30, 7124, 30, 7126, 30, 7128, 30, 7130, 30,
			7132, 30, 7134, 30, 7136, 30, 7138, 30, 6654, 10, 6655, 10, 6656,
			10, 1037, 1, 1419, 1, 14602, 3, 14600, 3, 14603, 3, 14605, 3,
			11015, 4, 11017, 4, 11018, 4, 11016, 4, 4566, 6, 2579, 3, 2577, 2,
			3842, 20, 3843, 20, 3844, 20 }; // 1304
											// 60
	public static final int[] godLow3 = { 10376, 2416, 2669, 2671, 2673, 2675,
			3480, 10378, 10380, 10382, 10442, 10448, 10454, 10462, 10466, 10472 };
	public static final int[] godLow2 = { 2653, 2655, 2417, 2657, 2659, 3478,
			10368, 10370, 10372, 10374, 10444, 10450, 10456, 10460, 10468,
			10474 };
	public static final int[] godLow1 = { 2415, 2661, 2663, 2665, 2667, 3479,
			10384, 10388, 10386, 10390, 10440, 10446, 10452, 10458, 10464,
			10470 };
	public static final int[] godHigh = { 11696, 11724, 11726, 11728, 11698,
			11730, 11700, 11716, 11694, 11720, 11718, 11722 };
	public static final int[] junk = { 1432, 9185, 18830, 1187, 5316, 5304,
			1601, 11732, 4675, 1127, 1712, 12093, 12790, 13360 };
	public static final int[] pets = { 1563, 1561, 1564, 1566, 1562, 12476,
			12472, 12523, 12519, 12474, 12515, 12517, 12742, 12683, 12482,
			12470, 12521, 12513 };

	private static final int PET_REWARD = 1, ITEM_REWARD = 2;

	public static void coinBox(final Player client) {
		if (client.getActionAssistant().playerHasItem(3062, 1)) {
			client.getActionAssistant().deleteItem(3062, 1);
			client.getActionSender().sendMessage(
					Language.gamblingBoxMessages[1]);
			client.getPlayerEventHandler().addEvent(new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					final int reward = 995;
					int amount = 2500000;
					amount = amount + Misc.random(7500000);
					if (Misc.random(15) == 1)
						amount = amount + Misc.random(100000000);
					if (Misc.random(70) == 1)
						amount = amount + Misc.random(500000000);
					client.getActionSender().addItem(reward, amount);
					if (InstanceDistributor.getItemManager().getItemDefinition(
							reward) != null)
						client.getActionSender().sendMessage(
								"and find  ..... @red@"
										+ Misc.formatAmount(amount)
										+ " coins @bla@!");
					container.stop();
				}

				@Override
				public void stop() {
					// TODO Auto-generated method stub

				}
			}, 1);
		} else
			client.getActionSender().sendMessage(
					Language.gamblingBoxMessages[0]);
	}

	public static void giveBoxResult(final Player client) {
		if (client.getActionAssistant().playerHasItem(6199, 1)) {
			client.getActionAssistant().deleteItem(6199, 1);
			client.getActionSender().sendMessage(
					Language.gamblingBoxMessages[1]);
			client.getPlayerEventHandler().addEvent(new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					final int chance = Misc.random(8);
					if (chance != 1) {
						final int reward = reward();
						client.getActionSender().addItem(reward, 1);
						if (InstanceDistributor.getItemManager()
								.getItemDefinition(reward) != null)
							client.getActionSender().sendMessage(
									"and find a ..... @red@"
											+ InstanceDistributor
													.getItemManager()
													.getItemDefinition(reward)
													.getName() + "@bla@!");
					}
					if (chance == 1) {
						client.getActionSender()
								.addItem(12158, Misc.random(50));
						client.getActionSender()
								.addItem(12159, Misc.random(30));
						client.getActionSender()
								.addItem(12160, Misc.random(20));
						client.getActionSender()
								.addItem(12163, Misc.random(10));
						client.getActionSender().sendMessage(
								"and find a ..... @red@charm pack@bla@!");
					}
					container.stop();
				}

				@Override
				public void stop() {
					// TODO Auto-generated method stub

				}
			}, 1);
		} else
			client.getActionSender().sendMessage(
					Language.gamblingBoxMessages[0]);
	}

	public static void godBox(final Player client) {
		if (client.getActionAssistant().playerHasItem(6183, 1)) {
			client.getActionAssistant().deleteItem(6183, 1);
			client.getActionSender().sendMessage(
					Language.gamblingBoxMessages[1]);
			client.getPlayerEventHandler().addEvent(new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					final int reward = godReward();
					client.getActionSender().addItem(reward, 1);
					if (InstanceDistributor.getItemManager().getItemDefinition(
							reward) != null)
						client.getActionSender().sendMessage(
								"and find a ..... @red@"
										+ InstanceDistributor.getItemManager()
												.getItemDefinition(reward)
												.getName() + "@bla@!");
					container.stop();
				}

				@Override
				public void stop() {
					// TODO Auto-generated method stub

				}
			}, 1);
		} else
			client.getActionSender().sendMessage(
					Language.gamblingBoxMessages[0]);
	}

	public static int godReward() {
		final int num = Misc.random(100);
		if (num < 35)
			return godLow3[Misc.random(godLow3.length - 1)];
		else if (num >= 35 && num < 65)
			return godLow2[Misc.random(godLow2.length - 1)];
		else if (num >= 65 && num < 90)
			return godLow1[Misc.random(godLow1.length - 1)];
		else if (num >= 90)
			return godHigh[Misc.random(godHigh.length - 1)];
		else
			return 2653;
	}

	public static void potLuckBox(final Player client) {
		if (client.getActionAssistant().playerHasItem(10025, 1)) {
			client.getActionAssistant().deleteItem(10025, 1);
			client.getActionSender().sendMessage(
					Language.gamblingBoxMessages[1]);
			client.getPlayerEventHandler().addEvent(new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					int reward = junk[Misc.random(junk.length - 1)];
					String msg = "awwh, bad luck";
					if (Misc.random(100) == 1) {
						msg = "woot!";
						final int num = Misc.random(10);
						if (num == 10)
							reward = 1042;
						else if (num == 9 || num == 8)
							reward = 1048;
						else if (num == 7 || num == 6)
							reward = 1038;
						else if (num == 5 || num == 4 || num == 3) {
							if (Misc.random(2) == 1)
								reward = 1044;
							else
								reward = 1040;
						} else
							reward = 1046;
					}
					client.getActionSender().addItem(reward, 1);
					if (InstanceDistributor.getItemManager().getItemDefinition(
							reward) != null)
						client.getActionSender().sendMessage(
								"and.... "
										+ msg
										+ ", you find a  @red@"
										+ InstanceDistributor.getItemManager()
												.getItemDefinition(reward)
												.getName() + "@bla@!");
					container.stop();
				}

				@Override
				public void stop() {
					// TODO Auto-generated method stub

				}
			}, 1);
		} else
			client.getActionSender().sendMessage(
					Language.gamblingBoxMessages[0]);
	}

	public static int reward() {
		int reward = 0;
		int chance = 0;
		int kans = 0;
		int count = 0;
		chance = Misc.random(100);
		/*
		 * if (chance > 0 && chance <= 16) { kans = Misc.random(181); for (int i
		 * = 0; i < junkRewards.length; i = i + 2) { if (junkRewards[i] > 50) {
		 * if (kans > count && kans <= count + junkRewards[i + 1]) { reward =
		 * junkRewards[i]; } count = count + junkRewards[i + 1]; } } }
		 */if (chance <= 45) {
			kans = Misc.random(72 + 25);
			for (int i = 0; i < lowRewards.length; i = i + 2)
				if (lowRewards[i] > 60) {
					if (kans > count && kans <= count + lowRewards[i + 1])
						reward = lowRewards[i];
					count = count + lowRewards[i + 1];
				}
		} else if (chance > 60 && chance <= 85) {
			kans = Misc.random(206);
			for (int i = 0; i < medRewards.length; i = i + 2)
				if (medRewards[i] > 50) {
					if (kans > count && chance <= kans + medRewards[i + 1])
						reward = medRewards[i];
					count = count + medRewards[i + 1];
				}
		} else if (chance > 85 && chance <= 99) {
			kans = Misc.random(24);
			for (int i = 0; i < highRewards.length; i = i + 2)
				if (highRewards[i] > 50) {
					if (kans > count && kans <= count + highRewards[i + 1])
						reward = highRewards[i];
					count = count + highRewards[i + 1];
				}
		}
		if (chance == 100) {
			kans = Misc.random(139);
			for (int i = 0; i < extremeRewards.length; i = i + 2)
				if (extremeRewards[i] > 50) {
					if (kans > count && kans <= count + extremeRewards[i + 1])
						reward = extremeRewards[i];
					count = count + extremeRewards[i + 1];
				}
		}
		if (reward == 0)
			reward = reward();
		return reward;
	}

	public static int rewardVoteItems() {
		int reward = 0;
		int chance = 0;
		int kans = 0;
		int count = 0;
		chance = Misc.random(100);
		if (chance > 0 && chance <= 85) {
			kans = Misc.random(181);
			for (int i = 0; i < junkRewards.length; i = i + 2)
				if (junkRewards[i] > 50) {
					if (kans > count && kans <= count + junkRewards[i + 1])
						reward = junkRewards[i];
					count = count + junkRewards[i + 1];
				}
		} else if (chance > 85 && chance <= 98) {
			kans = Misc.random(72 + 25);
			for (int i = 0; i < lowRewards.length; i = i + 2)
				if (lowRewards[i] > 50) {
					if (kans > count && kans <= count + lowRewards[i + 1])
						reward = lowRewards[i];
					count = count + lowRewards[i + 1];
				}
		} else if (chance > 98 && chance <= 100) {
			kans = Misc.random(206);
			for (int i = 0; i < medRewards.length; i = i + 2)
				if (medRewards[i] > 50) {
					if (kans > count && chance <= kans + medRewards[i + 1])
						reward = medRewards[i];
					count = count + medRewards[i + 1];
				}
		}
		if (reward == 0)
			reward = rewardVoteItems();
		return reward;
	}

	public static void seedReward(Player client, int itemID) {
		client.progress[71]++;
		if (client.progress[71] >= 50)
			Achievements.getInstance().complete(client, 71);
		else
			Achievements.getInstance().turnYellow(client, 71);
		if (itemID == 5073) {
			final int[] seedreward1 = { 5314, 5283, 5284, 5313, 5285, 5286 };
			final int[] seedreward2 = { 5314, 5288, 5287, 5315, 5289 };
			final int chance = Misc.random(30);
			if (chance <= 18) {
				final int reward = seedreward1[Misc.random(5)];
				client.getActionAssistant().deleteItem(itemID,
						client.getActionAssistant().getItemSlot(itemID), 1);
				client.getActionSender().addItem(reward, 1);
				client.getActionSender().addItem(5075, 1);
				final String seedName = InstanceDistributor.getItemManager()
						.getItemDefinition(reward).getName();
				client.getActionSender().sendMessage(
						"You search the bird nest and find a "
								+ seedName.toLowerCase() + ".");

			} else if (chance >= 19 || chance <= 29) {
				final int reward = seedreward2[Misc.random(4)];
				client.getActionAssistant().deleteItem(itemID,
						client.getActionAssistant().getItemSlot(itemID), 1);
				client.getActionSender().addItem(reward, 1);
				client.getActionSender().addItem(5075, 1);
				final String seedName = InstanceDistributor.getItemManager()
						.getItemDefinition(reward).getName();
				client.getActionSender().sendMessage(
						"You search the bird nest and find a "
								+ seedName.toLowerCase() + ".");
			} else if (chance == 30) {
				client.getActionAssistant().deleteItem(itemID,
						client.getActionAssistant().getItemSlot(itemID), 1);
				client.getActionSender().addItem(5316, 1);
				client.getActionSender().addItem(5075, 1);
				client.getActionSender().sendMessage(
						"You search the bird nest and find a magic seed.");
			}
		} else if (itemID == 5074) {
			final int[] ringReward = { 1635, 1637 };
			final int randomizer = Misc.random(16);
			if (randomizer <= 10) {
				final int reward = ringReward[Misc.random(1)];
				client.getActionAssistant().deleteItem(itemID,
						client.getActionAssistant().getItemSlot(itemID), 1);
				client.getActionSender().addItem(reward, 1);
				client.getActionSender().addItem(5075, 1);
				final String ringName = InstanceDistributor.getItemManager()
						.getItemDefinition(reward).getName();
				client.getActionSender().sendMessage(
						"You search the bird nest and find a "
								+ ringName.toLowerCase() + ".");
			} else if (randomizer == 11 || randomizer == 12 || randomizer == 13) {
				final int reward = 1639;
				client.getActionAssistant().deleteItem(itemID,
						client.getActionAssistant().getItemSlot(itemID), 1);
				client.getActionSender().addItem(reward, 1);
				client.getActionSender().addItem(5075, 1);
				final String ringName = InstanceDistributor.getItemManager()
						.getItemDefinition(reward).getName();
				client.getActionSender().sendMessage(
						"You search the bird nest and find a "
								+ ringName.toLowerCase() + ".");
			} else if (randomizer == 14 || randomizer == 15) {
				final int reward = 1641;
				client.getActionAssistant().deleteItem(itemID,
						client.getActionAssistant().getItemSlot(itemID), 1);
				client.getActionSender().addItem(reward, 1);
				client.getActionSender().addItem(5075, 1);
				final String ringName = InstanceDistributor.getItemManager()
						.getItemDefinition(reward).getName();
				client.getActionSender().sendMessage(
						"You search the bird nest and find a "
								+ ringName.toLowerCase() + ".");
			} else if (randomizer == 16) {
				final int reward = 1643;
				client.getActionAssistant().deleteItem(itemID,
						client.getActionAssistant().getItemSlot(itemID), 1);
				client.getActionSender().addItem(reward, 1);
				client.getActionSender().addItem(5075, 1);
				final String ringName = InstanceDistributor.getItemManager()
						.getItemDefinition(reward).getName();
				client.getActionSender().sendMessage(
						"You search the bird nest and find a "
								+ ringName.toLowerCase() + ".");
			}
		} else if (itemID == 5070) {
			final int reward = 12115;
			client.getActionAssistant().deleteItem(itemID,
					client.getActionAssistant().getItemSlot(itemID), 1);
			client.getActionSender().addItem(reward, 1);
			client.getActionSender().addItem(5075, 1);
			final String ringName = InstanceDistributor.getItemManager()
					.getItemDefinition(reward).getName();
			client.getActionSender().sendMessage(
					"You search the bird nest and find a "
							+ ringName.toLowerCase() + ".");
		} else if (itemID == 5071) {
			final int reward = 12111;
			client.getActionAssistant().deleteItem(itemID,
					client.getActionAssistant().getItemSlot(itemID), 1);
			client.getActionSender().addItem(reward, 1);
			client.getActionSender().addItem(5075, 1);
			final String ringName = InstanceDistributor.getItemManager()
					.getItemDefinition(reward).getName();
			client.getActionSender().sendMessage(
					"You search the bird nest and find a "
							+ ringName.toLowerCase() + ".");
		} else if (itemID == 5072) {
			final int reward = 12113;
			client.getActionAssistant().deleteItem(itemID,
					client.getActionAssistant().getItemSlot(itemID), 1);
			client.getActionSender().addItem(reward, 1);
			client.getActionSender().addItem(5075, 1);
			final String ringName = InstanceDistributor.getItemManager()
					.getItemDefinition(reward).getName();
			client.getActionSender().sendMessage(
					"You search the bird nest and find a "
							+ ringName.toLowerCase() + ".");
		}

	}

	public static void skillerBox(final Player client) {
		if (client.getActionAssistant().playerHasItem(18768, 1)) {
			client.getActionAssistant().deleteItem(18768, 1);
			client.getActionSender().sendMessage(
					Language.gamblingBoxMessages[1]);
			client.getPlayerEventHandler().addEvent(new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {

					final int reward = skillReward();
					client.getActionSender().addItem(reward, 1);
					if (InstanceDistributor.getItemManager().getItemDefinition(
							reward) != null)
						client.getActionSender().sendMessage(
								"and find a ..... @red@"
										+ InstanceDistributor.getItemManager()
												.getItemDefinition(reward)
												.getName() + "@bla@!");
					container.stop();
				}

				@Override
				public void stop() {
					// TODO Auto-generated method stub

				}
			}, 1);
		} else
			client.getActionSender().sendMessage(
					Language.gamblingBoxMessages[0]);
	}

	public static int skillReward() {
		int reward = 0;
		int count = 0;
		final int chance = Misc.random(1372);
		for (int i = 0; i < skillBoxRewards.length; i = i + 2)
			if (skillBoxRewards[i] > 50) {
				if (chance > count && chance <= count + skillBoxRewards[i + 1])
					reward = skillBoxRewards[i];
				count = count + skillBoxRewards[i + 1];
			}
		return reward;
	}

	public static void voteBox(Player client) {
		if (!client.getActionAssistant().playerHasItem(20935, 1))
			return;
		client.getActionAssistant().deleteItem(20935, 1);
		client.votePoints++;
		client.getActionSender().sendMessage(
				"You recieved a voting point, you now have @red@"
						+ client.votePoints + "@bla@voting points.");
		final int voteType = Misc.random(3);
		switch (voteType) {
		case PET_REWARD:
			client.getActionSender().addItem(
					pets[Misc.random(pets.length - 1)], 1);
			client.getActionSender().sendMessage("You recieve a pet reward!");
			break;
		case ITEM_REWARD:
			client.getActionSender().addItem(rewardVoteItems(), 1);
			client.getActionSender().sendMessage("You recieve an item reward!");
			break;
		default:
			client.getActionSender().addItem(995, Misc.random(250000));
			client.getActionSender().sendMessage("You recieve a cash reward.");
			break;
		}
	}
}
