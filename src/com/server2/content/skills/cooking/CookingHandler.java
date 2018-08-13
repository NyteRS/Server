package com.server2.content.skills.cooking;

import com.server2.InstanceDistributor;
import com.server2.content.Achievements;
import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Misc;
import com.server2.world.XMLManager;
import com.server2.world.XMLManager.Cooking;
import com.server2.world.objects.ObjectStorage;

/**
 * 
 * @author Rene
 * 
 */
public class CookingHandler {

	public static boolean generateCookSuccess(int level, int levelReq) {
		if (level - 20 > levelReq)
			return true;
		if (level - 15 > levelReq)
			return Misc.random(5) != 0;
		if (level - 10 > levelReq)
			return Misc.random(4) != 0;
		if (level - 5 > levelReq)
			return Misc.random(3) != 0;
		return Misc.random(2) != 0;
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

					if (client.cookingAmount > 0)
						client.cookingAmount--;
					else {
						container.stop();
						return;
					}
					final int[] object = ObjectStorage.getDetails(client);
					client.objectID = object[0];
					client.objectX = object[2];
					client.objectY = object[3];
					client.objectSize = object[1];
					client.setPlayerX = client.getAbsX();
					client.setPlayerY = client.getAbsY();

					if (client.objectSize > 1)
						client.getActionAssistant().turnTo(
								client.objectX + client.objectSize / 2,
								client.objectY + client.objectSize / 2);
					else
						client.getActionAssistant().turnTo(client.objectX,
								client.objectY);

					if (rawInput(client.cooking) != null)
						if (client.getActionAssistant().isItemInBag(
								rawInput(client.cooking).getRawType())) {
							client.getActionAssistant().deleteItem(
									rawInput(client.cooking).getRawType(), 1);
							if (generateCookSuccess(
									client.playerLevel[PlayerConstants.COOKING],
									rawInput(client.cooking).getLevel())) {
								client.getActionSender().addItem(
										rawInput(client.cooking)
												.getCookedType(), 1);
								client.getActionAssistant()
										.addSkillXP(
												rawInput(client.cooking)
														.getXp()
														* PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
												PlayerConstants.COOKING);
								if (rawInput(client.cooking).getCookedType() == 383
										|| rawInput(client.cooking)
												.getCookedType() == 385) {

									client.progress[43]++;
									if (client.progress[43] >= 100)
										Achievements.getInstance().complete(
												client, 43);
									else
										Achievements.getInstance().turnYellow(
												client, 43);
								}
								client.getActionSender()
										.sendMessage(
												"You succesfully managed to cook a "
														+ InstanceDistributor
																.getItemManager()
																.getItemDefinition(
																		rawInput(
																				client.cooking)
																				.getCookedType())
																.getName()
																.toLowerCase()
														+ ".");
							} else
								client.getActionSender()
										.addItem(
												rawInput(client.cooking)
														.getBurntType(), 1);
						} else {
							container.stop();
							return;
						}
					AnimationProcessor.addNewRequest(client,
							client.cookingAnimation, 0);
				}
			}

			@Override
			public void stop() {
				client.getActionSender().sendAnimationReset();
				client.actionSet = false;
				client.currentActivity = null;
				client.cooking = 0;
				client.cookingAmount = 0;
				client.cookingAnimation = 0;
			}

		}, 4);
	}

	public static Cooking rawInput(int id) {
		for (final Cooking c : XMLManager.ingredients)
			if (c.getRawType() == id)
				return c;
		return null;
	}

	public static void start(final Player client) {
		if (client == null)
			return;
		if (client.cooking <= 0 || client.cookingAmount <= 0
				|| client.cookingAnimation <= 0)
			return;
		if (rawInput(client.cooking) != null)
			if (rawInput(client.cooking).getLevel() > client.playerLevel[PlayerConstants.COOKING]) {
				client.getActionSender().sendMessage(
						"You need a cooking level of "
								+ rawInput(client.cooking).getLevel()
								+ " to cook this.");
				return;
			}
		final int[] object = ObjectStorage.getDetails(client);
		client.objectID = object[0];
		client.objectX = object[2];
		client.objectY = object[3];
		client.objectSize = object[1];
		client.setPlayerX = client.getAbsX();
		client.setPlayerY = client.getAbsY();
		if (client.objectSize > 1)
			client.getActionAssistant().turnTo(
					client.objectX + client.objectSize / 2,
					client.objectY + client.objectSize / 2);
		else
			client.getActionAssistant().turnTo(client.objectX, client.objectY);
		AnimationProcessor.addNewRequest(client, client.cookingAnimation, 0);
		loop(client);
	}
}
