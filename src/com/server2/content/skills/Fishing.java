package com.server2.content.skills;

import com.server2.InstanceDistributor;
import com.server2.content.Achievements;
import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.entity.player.Language;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Misc;
import com.server2.world.XMLManager;
import com.server2.world.XMLManager.Catches;

/**
 * 
 * @author Rene
 * @author lukas
 */
public class Fishing {

	public static void calculateFishImport(Player client, int theCount) {
		if (theCount < 0 || theCount >= 3)
			return;

		final int startOn = 0;
		int endOn = startOn + Misc.random(theCount);

		if (fish(client.fishing) == null)
			return;

		while (fish(client.fishing).getCatches()[endOn] == -1)
			if (endOn > 0)
				endOn--;

		final String fish = InstanceDistributor.getItemManager()
				.getItemDefinition(fish(client.fishing).getCatches()[endOn])
				.getName();
		client.getActionSender().sendMessage(
				"You manage to catch a " + fish.toLowerCase() + ".");
		client.getActionSender().addItem(
				fish(client.fishing).getCatches()[endOn], 1);
		if (fish(client.fishing).getCatches()[endOn] == 359) {

			client.progress[13]++;
			if (client.progress[13] >= 50)
				Achievements.getInstance().complete(client, 13);
			else
				Achievements.getInstance().turnYellow(client, 13);

		}
		if (fish(client.fishing).getCatches()[endOn] == 7944) {

			client.progress[44]++;
			if (client.progress[44] >= 500)
				Achievements.getInstance().complete(client, 44);
			else
				Achievements.getInstance().turnYellow(client, 44);

		}
		client.getActionAssistant().startAnimation(
				fish(client.fishing).getAnimation(), 0);
		client.getActionAssistant().deleteItem(
				fish(client.fishing).getTools()[1],
				fish(client.fishing).getToolAmounts()[1]);
		client.getActionAssistant().addSkillXP(
				fishExp(fish(client.fishing).getCatches()[endOn])
						* PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
				PlayerConstants.FISHING);
		if (client.familiarId == 6991) {
			final int extraFishy = Misc.random(10);
			if (extraFishy == 1) {
				client.getActionSender().addItem(
						fish(client.fishing).getCatches()[endOn], 1);
				client.getActionSender()
						.sendMessage(
								"Your ibis catches another "
										+ fish.toLowerCase() + ".");
				client.getActionAssistant().addSkillXP(
						fishExp(fish(client.fishing).getCatches()[endOn]) / 10
								* PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
						PlayerConstants.FISHING);
			}
		} else if (client.familiarId == 6849) {
			final int extraFishy = Misc.random(5);
			if (extraFishy == 1) {
				client.getActionSender().addItem(
						fish(client.fishing).getCatches()[endOn], 1);
				client.getActionSender().sendMessage(
						"Your granite lobster catches another "
								+ fish.toLowerCase() + ".");
				client.getActionAssistant().addSkillXP(
						fishExp(fish(client.fishing).getCatches()[endOn]) / 10
								* PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
						PlayerConstants.FISHING);
			}
		}
		client.updateRequired = true;
	}

	public static boolean destuct(Player client) {
		if (client.fishing < 1 || client.fishingX < 1 || client.fishingY < 1
				|| client.isBusy())
			return true;

		if (client.getActionAssistant().freeSlots() < 1) {
			client.getActionSender().sendMessage(Language.NO_SPACE);
			return true;
		}
		if (fish(client.fishing) == null)
			return true;

		if (fish(client.fishing).getCatchLevels()[0] > client.playerLevel[PlayerConstants.FISHING]) {
			client.getActionSender().sendMessage(
					"You need a fishing level of "
							+ fish(client.fishing).getCatchLevels()[0]
							+ " to fish here.");
			return true;
		}
		final int mainTool = fish(client.fishing).getTools()[0];
		final int nextTool = fish(client.fishing).getTools()[1];

		if (!client.getActionAssistant().isItemInBag(mainTool)) {
			client.getActionSender().sendMessage(
					"You need a "
							+ InstanceDistributor.getItemManager()
									.getItemDefinition(mainTool).getName()
									.toLowerCase() + " to fish here.");
			return true;
		}
		if (!client.getActionAssistant().isItemInBag(nextTool)
				&& nextTool != -1) {
			client.getActionSender().sendMessage(
					"You need a "
							+ InstanceDistributor.getItemManager()
									.getItemDefinition(nextTool).getName()
									.toLowerCase() + " to fish here.");
			return true;
		}
		return false;
	}

	public static Catches fish(int fishingSpot) {
		for (final Catches fish : XMLManager.catches)
			if (fish.getFishingSpot() == fishingSpot)
				return fish;
		return null;
	}

	public static final int fishExp(int fish) {
		switch (fish) {
		case 317: // Raw_shrimps
			return 10;
		case 321: // Raw_anchovies
			return 40;
		case 327: // Raw_sardine
			return 20;
		case 331: // Raw_salmon
			return 70;
		case 335: // Raw_trout
			return 50;
		case 338: // Raw_giant_carp
			return 0;
		case 341: // Raw_cod
			return 45;
		case 345: // Raw_herring
			return 30;
		case 349: // Raw_pike
			return 60;
		case 353: // Raw_mackerel
			return 20;
		case 359: // Raw_tuna
			return 80;
		case 363: // Raw_bass
			return 100;
		case 371: // Raw_swordfish
			return 100;
		case 377: // Raw_lobster
			return 90;
		case 383: // Raw_shark
			return 110;
		case 389: // Raw_manta_ray
			return 150;
		case 395: // Raw_sea_turtle
			return 120;

		case 7944: // raw monkfish
			return 120;
		default:
			return 30;
		}
	}

	public static void loadAction(Player client, int npc, int x, int y) {
		client.fishing = npc;
		client.fishingX = x;
		client.fishingY = y;

		if (destuct(client))
			return;

		client.getActionAssistant().turnTo(client.fishingX, client.fishingY);

		if (fish(npc) == null)
			return;
		if (client.actionSet || client.currentActivity != null)
			return;
		if (System.currentTimeMillis() - client.lastFish < 1000)
			return;
		client.getActionAssistant().startAnimation(fish(npc).getAnimation(), 0);
		client.lastFish = System.currentTimeMillis();

		loop(client);
	}

	public static void loop(final Player client) {
		if (client == null)
			return;
		client.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				client.lastFish = System.currentTimeMillis();

				if (!client.actionSet) {
					client.actionSet = true;
					client.currentActivity = this;
				}
				if (client != null) {
					if (client.isStopRequired()) {
						container.stop();
						return;
					}

					if (fish(client.fishing) == null) {
						container.stop();
						return;
					}
					int count = -1;
					int level = fish(client.fishing).getCatchLevels()[0];
					if (fish(client.fishing).getCatchLevels()[0] == 62)
						level = 45;
					if (fish(client.fishing).getCatchLevels()[0] == 20)
						level = 5;
					if (fish(client.fishing).getCatchLevels()[0] == 30)
						level = 10;
					if (client.playerLevel[PlayerConstants.FISHING] >= fish(
							client.fishing).getCatchLevels()[0]
							&& fish(client.fishing).getCatchLevels()[0] != -1)
						count++;
					if (client.playerLevel[PlayerConstants.FISHING] >= fish(
							client.fishing).getCatchLevels()[1]
							&& fish(client.fishing).getCatchLevels()[1] != -1)
						count++;
					if (client.playerLevel[PlayerConstants.FISHING] >= fish(
							client.fishing).getCatchLevels()[2]
							&& fish(client.fishing).getCatchLevels()[2] != -1)
						count++;
					if (level < Misc.random(client.playerLevel[PlayerConstants.FISHING]) + 5)
						calculateFishImport(client, count);
					else
						client.getActionAssistant().startAnimation(
								fish(client.fishing).getAnimation(), 0);

					if (destuct(client)) {
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
		}, 5);
	}

	public void removeAction(Player client) {
		client.fishing = client.fishingX = client.fishingY = 0;
		client.getActionSender().sendAnimationReset();
		client.updateRequired = true;
	}
}