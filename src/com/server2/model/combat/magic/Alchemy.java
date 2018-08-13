package com.server2.model.combat.magic;

import com.server2.InstanceDistributor;
import com.server2.content.Achievements;
import com.server2.content.TradingConstants;
import com.server2.content.skills.summoning.Summoning;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.player.Language;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;

/**
 * 
 * @author killamess & Lukas
 * 
 */
public class Alchemy {

	public static int alch(int itemId, int spellId) {
		return (int) (spellId == 1162 ? InstanceDistributor.getGeneralStore()
				.itemPrice(itemId) / 3 : InstanceDistributor.getGeneralStore()
				.itemPrice(itemId) * 0.75);
	}

	public static void alch(Player client, int itemId, int spellId, int junk) {
		if (client == null || !client.getActionAssistant().isItemInBag(itemId)
				|| client.isBusy() || client.isDead())
			return;
		final int reqMagicLevel = spellId == 1162 ? 21 : 55;

		if (client.playerLevel[PlayerConstants.MAGIC] < reqMagicLevel) {
			client.getActionSender().sendMessage(Language.MAGE_TOO_LOW);
			return;
		}
		if (!client.getActionAssistant().isItemInBag(561)
				|| !client.getActionAssistant().playerHasItem(554, 5)
				&& client.playerEquipment[PlayerConstants.WEAPON] != 1387) {
			client.getActionSender().sendMessage(Language.NO_RUNES);
			return;
		}
		if (itemId == 995) {
			client.getActionSender().sendMessage(
					"You cannot turn gold into gold!");
			return;
		}
		if (TradingConstants.isUntradable(itemId)) {
			client.getActionSender().sendMessage(
					"You cannot alch untradeable items.");
			return;
		}
		boolean hasCoins = false;
		if (client.getActionAssistant().playerHasItem(995, 1))
			hasCoins = true;
		if (!hasCoins)
			if (client.getActionAssistant().freeSlots() < 1) {
				client.getActionSender().sendMessage(Language.NO_SPACE);
				return;
			}
		if (itemId == 561)
			if (!client.getActionAssistant().playerHasItem(561, 2))
				return;
		if (itemId == 554)
			if (!client.getActionAssistant().playerHasItem(554, 6)
					&& client.playerEquipment[PlayerConstants.WEAPON] == 1387)
				return;
		client.setBusyTimer(spellId == 1162 ? 3 : 5);
		int valueToAdd = alch(itemId, spellId);
		if (valueToAdd == 0) {
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
			}
			for (final double[] element : Summoning.summoningInfo)
				if (element[pouchOrScroll] == itemId - 1)
					swapAmount = (int) element[10];
			if (!noted && pouchOrScroll != 3) {
				client.getActionSender().sendMessage(
						"You can only alch noted pouches.");
				return;
			} else if (noted)
				valueToAdd = swapAmount * 30;
		}
		if (valueToAdd == 0) {
			client.getActionSender()
					.sendMessage(
							"This item has a unknown price, please report to Rene or Lukas.");
			return;
		}
		client.getActionSender().addItem(995, valueToAdd);
		client.progress[80] = client.progress[80] + valueToAdd;
		if (client.progress[80] >= 1000000000)
			Achievements.getInstance().complete(client, 80);
		else
			Achievements.getInstance().turnYellow(client, 80);
		AnimationProcessor
				.addNewRequest(client, spellId == 1162 ? 712 : 713, 1);
		GraphicsProcessor.addNewRequest(client, spellId == 1162 ? 112 : 113, 0,
				1);
		client.getActionSender().sendSidebar(6, 1151);
		client.getActionSender().sendFrame106(6);
		client.getActionAssistant().addSkillXP(
				spellId == 1162 ? 30 : 65 * client.multiplier,
				PlayerConstants.MAGIC);
		client.getActionAssistant().deleteItem(itemId, 1);
		client.getActionAssistant().deleteItem(561, 1);
		if (client.playerEquipment[PlayerConstants.WEAPON] != 1387)
			client.getActionAssistant().deleteItem(554, 5);
	}
}
