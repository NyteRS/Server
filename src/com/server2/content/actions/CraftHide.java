package com.server2.content.actions;

import com.server2.InstanceDistributor;
import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Misc;

/**
 * 
 * @author R & Lukas
 * 
 */
public class CraftHide {

	// item, hide type, hides needed, level, experience
	public static final int[][] craft = {
	/* green hide */
	{ 1065, 1745, 1, 57, 62 },// vambraces
			{ 1099, 1745, 2, 60, 124 },// chaps
			{ 1135, 1745, 3, 63, 186 },// body

			/* blue hide */
			{ 2487, 2505, 1, 66, 70 },// vambraces
			{ 2493, 2505, 2, 68, 140 },// chaps
			{ 2499, 2505, 3, 71, 210 },// body

			/* red hide */
			{ 2489, 2507, 1, 73, 78 },// vambraces
			{ 2495, 2507, 2, 75, 156 },// chaps
			{ 2501, 2507, 3, 77, 234 },// body

			/* black hide */
			{ 2491, 2509, 1, 79, 86 },// vambraces
			{ 2497, 2509, 2, 82, 172 },// chaps
			{ 2503, 2509, 3, 84, 258 },// body

			{ 1059, 1741, 1, 1, 14 }, // glovev
			{ 1061, 1741, 1, 7, 17 },// boots
			{ 1167, 1741, 1, 9, 19 }, // cowl
			{ 0, 0, 0, 0, 0 }, { 1131, 1743, 1, 28, 35 }, // hard leather
			{ 0, 0, 0, 0, 0 } };

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
					if (client.crafting < 1 || client.craftingItem < 1
							|| client.craftingAmount < 1) {
						container.stop();
						return;
					}

					client.craftingAmount--;
					// System.out.println("itemthis "+client.craftingItem);

					for (int i = 0; i < craft.length; i++)
						if (craft[i][0] == client.craftingItem) {
							if (client.playerLevel[PlayerConstants.CRAFTING] < craft[i][3]) {
								client.getActionSender().sendMessage(
										"You need a crafting level of "
												+ craft[i][3]
												+ " to craft "
												+ InstanceDistributor
														.getItemManager()
														.getItemDefinition(
																craft[i][0])
														.getName()
														.toLowerCase() + ".");
								container.stop();
								return;
							}
							if (client.getActionAssistant().playerHasItem(
									craft[i][1], craft[i][2])) {
								if (client.getActionAssistant().playerHasItem(
										1733, 1)) {
									if (client.getActionAssistant()
											.playerHasItem(1734, 1)) {
										for (int delete = 0; delete < craft[i][2]; delete++)
											client.getActionAssistant()
													.deleteItem(craft[i][1], 1);
										client.getActionSender().addItem(
												client.craftingItem, 1);
										client.getActionAssistant()
												.addSkillXP(
														craft[i][4]
																* PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
														PlayerConstants.CRAFTING);

										if (!InstanceDistributor
												.getItemManager()
												.getItemDefinition(craft[i][0])
												.getName().contains("chaps")
												&& !InstanceDistributor
														.getItemManager()
														.getItemDefinition(
																craft[i][0])
														.getName()
														.contains("body"))
											client.craftingThreadCount = client.craftingThreadCount + 1;
										else if (InstanceDistributor
												.getItemManager()
												.getItemDefinition(craft[i][0])
												.getName().contains("chaps"))
											client.craftingThreadCount = client.craftingThreadCount + 2;
										else if (InstanceDistributor
												.getItemManager()
												.getItemDefinition(craft[i][0])
												.getName().contains("body"))
											client.craftingThreadCount = client.craftingThreadCount + 3;
										if (client.craftingThreadCount >= 3) {
											client.craftingThreadCount = client.craftingThreadCount - 3;
											if (Misc.random(5) == 1)
												client.getActionAssistant()
														.deleteItem(1734, 1);
											if (Misc.random(20) == 1)
												client.getActionAssistant()
														.deleteItem(1733, 1);

										}
									} else {
										client.getActionSender()
												.sendWindowsRemoval();
										client.getActionSender()
												.sendMessage(
														"You need more thread to craft this item.");
										container.stop();
										return;
									}
								} else {
									client.getActionSender()
											.sendMessage(
													"You need a needle to craft this item.");
									container.stop();
									return;
								}
							} else {
								client.getActionSender().sendMessage(
										"You do not have enough "
												+ InstanceDistributor
														.getItemManager()
														.getItemDefinition(
																craft[i][1])
														.getName()
														.toLowerCase()
												+ " to craft this item.");
								container.stop();
								return;
							}
						}
					AnimationProcessor.addNewRequest(client, 885, 0);

				}
			}

			@Override
			public void stop() {
				client.getActionSender().sendAnimationReset();
				client.actionSet = false;
				client.currentActivity = null;
				client.crafting = 0;
				client.craftingItem = 0;
				client.craftingAmount = 0;
			}

		}, 2);
	}

	public void start(final Player client) {
		loop(client);
	}

}