package com.server2.content.skills.crafting;

import com.server2.content.Achievements;
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
public class BowStringing {

	private static int[] spinValues = { 1779, 1777, 15, 896 };
	private static String[] stringData = { "flax", "bowstring" };

	public static void startStringing(final Player client, final int amount) {
		if (client.isBusy() || client == null)
			return;
		client.amountLeft = amount;

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
					if (client.amountLeft == 0) {
						container.stop();
						return;
					}
					if (client.getActionAssistant()
							.getItemAmount(spinValues[0]) > 0) {
						if (client.playerLevel[PlayerConstants.CRAFTING] >= spinValues[2]) {
							AnimationProcessor.addNewRequest(client,
									spinValues[3], 1);
							client.getActionAssistant().deleteItem(
									spinValues[0], 1);
							client.getActionAssistant()
									.addSkillXP(
											spinValues[2]
													* PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
											PlayerConstants.CRAFTING);
							client.getActionSender().addItem(spinValues[1], 1);
							if (spinValues[1] == 1777) {
								client.progress[10]++;
								if (client.progress[10] >= 30)
									Achievements.getInstance().complete(client,
											10);
								else
									Achievements.getInstance().turnYellow(
											client, 10);
							}
							client.getActionSender().sendMessage(
									"You spin the " + stringData[0]
											+ " into a " + stringData[1] + ".");

						} else {
							client.getActionSender().sendMessage(
									"You need a crafting level of "
											+ spinValues[2] + " to craft a "
											+ stringData[1] + ".");
							container.stop();
							return;
						}
					} else {
						client.getActionSender().sendMessage(
								"You do not have anymore " + stringData[0]
										+ " to string.");
						container.stop();
						return;
					}

				}
			}

			@Override
			public void stop() {
				client.getActionSender().sendAnimationReset();
				client.actionSet = false;
				client.currentActivity = null;
			}

		}, 2);

	}

}
