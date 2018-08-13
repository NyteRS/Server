package com.server2.content.skills.herblore;

import com.server2.InstanceDistributor;
import com.server2.content.Achievements;
import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.combat.additions.Specials;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Misc;

/**
 * 
 * @author Rene Roosen & Lukas
 * 
 */
public class SpecialPotions {

	public static void antiPoison(Player client, boolean isSuper) {

		long time = System.currentTimeMillis();
		final long minute = 60000;
		double extraTimeMultiplier = 1.5;
		if (isSuper)
			extraTimeMultiplier = 6;
		time = (long) (time + minute * extraTimeMultiplier);
		client.poisonDamage = 0;
		client.poisonProtectTime = time;
		client.poisonProtected = true;
		client.getActionSender().sendMessage(
				"You drink some of the super antipoison.");

	}

	/**
	 * Handles prayer renewal potion
	 * 
	 * @param client
	 */
	public static void prayerRenewal(final Player client) {
		if (client.isUsingRenewal) {
			client.getActionSender().sendMessage(
					"You are already using a prayer renewal potion.");
			return;
		}
		client.isUsingRenewal = true;
		client.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (client.playerLevel[PlayerConstants.PRAYER] < client.getLevelForXP(client.playerXP[PlayerConstants.PRAYER] - 2))
					client.playerLevel[PlayerConstants.PRAYER] += 2;
				client.getActionAssistant()
						.refreshSkill(PlayerConstants.PRAYER);
				// client.getActionSender().sendMessage("Your prayer renewal refills your prayer!");
				client.prayerRenewalCount++;
				if (client.prayerRenewalCount == 45)
					client.getActionSender().sendMessage(
							"@blu@Your prayer renewal is about to run out.");
				if (client.prayerRenewalCount >= 50) {
					client.prayerRenewalCount = 0;
					client.getActionSender().sendMessage(
							"@blu@Your prayer renewal has run out.");
					client.isUsingRenewal = false;
					container.stop();
				}
			}

			@Override
			public void stop() {
				// TODO Auto-generated method stub

			}
		}, 10);
	}

	public static void recoverSpecial(Player client, int itemId, int newItem,
			int itemSlot) {
		if (client.inWilderness()) {
			client.getActionSender().sendMessage(
					"You cannot use this potion in the wilderness!");
			return;
		}
		if (client.specialAmount == 100) {
			client.getActionSender()
					.sendMessage("You're already full special.");
			return;
		}
		if (System.currentTimeMillis() - client.lastSpecialRestore > 30000) {
			client.getActionAssistant().startAnimation(829, 0);
			client.getActionAssistant().deleteItem(itemId, itemSlot, 1);
			client.getActionSender().sendInventoryItem(newItem, 1, itemSlot);
			client.setSpecialAmount(client.specialAmount + 25);
			if (client.specialAmount > 100)
				client.setSpecialAmount(100);
			Specials.updateSpecialBar(client);
			client.lastSpecialRestore = System.currentTimeMillis();
			client.getActionSender().sendMessage(
					"You drink some of the "
							+ InstanceDistributor.getItemManager()
									.getItemDefinition(itemId).getName()
									.toLowerCase() + ".");
		} else
			client.getActionSender()
					.sendMessage(
							"You can only drink a special restore potion every 30 seconds.");

	}

	public static void restorePotStat(Player client, int stat, int increase) {
		boolean changed = false;
		final int actualLevel = client.getLevelForXP(client.playerXP[stat]);
		final double increaseBy = Misc.intToPercentage(increase) * actualLevel;

		if (client.playerLevel[stat] >= actualLevel)
			return;
		if (client.playerLevel[stat] + increaseBy > actualLevel) {
			changed = true;
			client.playerLevel[stat] = actualLevel;
		} else {
			client.playerLevel[stat] += increaseBy;
			changed = true;
		}
		if (changed)
			client.getActionAssistant().refreshSkill(stat);
	}

	public static void saradominBrew(Player client) {
		client.lastBrew = System.currentTimeMillis();
		client.hitpoints += 0.15 * client.calculateMaxHP() + 2;
		client.playerLevel[PlayerConstants.HITPOINTS] = client.hitpoints;
		if (client.playerLevel[3] > 1.15 * client.calculateMaxHP() + 2) {
			client.hitpoints = (int) (1.15 * client.calculateMaxHP() + 2);
			client.playerLevel[3] = client.hitpoints;

		}
		if (!client.overLoaded) {
			client.getActionAssistant().increaseStat(PlayerConstants.DEFENCE,
					25);
			client.getActionAssistant()
					.decreaseStat(PlayerConstants.ATTACK, 10);
			client.getActionAssistant().decreaseStat(PlayerConstants.STRENGTH,
					10);
			client.getActionAssistant().decreaseStat(PlayerConstants.RANGE, 10);
			client.getActionAssistant().decreaseStat(PlayerConstants.MAGIC, 10);
		}
		client.playerLevel[PlayerConstants.HITPOINTS] = client.hitpoints;
		client.hitpoints = client.playerLevel[PlayerConstants.HITPOINTS];
		client.getActionAssistant().refreshSkill(3);

	}

	public static void superRestore(Player client, int itemId, int newItem,
			int itemSlot) {
		client.getActionAssistant().startAnimation(829, 0);
		client.getActionAssistant().deleteItem(itemId, itemSlot, 1);
		if (client.isDunging)
			Achievements.getInstance().complete(client, 21);
		client.getActionSender().sendInventoryItem(newItem, 1, itemSlot);
		for (int i = 0; i < 7; i++)
			if (i != 3)
				restorePotStat(client, i, 33);

	}

}
