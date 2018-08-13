package com.server2.model.entity.player.packets.impl;

import com.server2.InstanceDistributor;
import com.server2.content.Achievements;
import com.server2.content.misc.ItemCreation;
import com.server2.content.skills.crafting.GemCrafting;
import com.server2.content.skills.crafting.HideCrafting;
import com.server2.content.skills.crafting.Pottery;
import com.server2.content.skills.crafting.SpecialItemCrafting;
import com.server2.content.skills.firemaking.Firemaking;
import com.server2.content.skills.fletching.Fletching;
import com.server2.content.skills.herblore.PotionMixing;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;

/**
 * Item on Item
 * 
 * @author ReLu Computing Solutions
 */

public class ItemOnItem implements Packet {

	public static final int BALL_OF_WALL = 1759;
	public static final int CLAY = 434;
	public static final int JUG_OF_WATER = 1937;
	public static final int CHISEL = 1755;
	public static final int NEEDLE = 1733, THREAD = 1734;
	public static final int TINDERBOX = 590;

	@Override
	public void handlePacket(Player client, GamePacket packet) {
		try {
			if (client == null || packet == null)
				return;
			final int usedWithSlot = packet.getShort();
			final int itemUsedSlot = packet.getShortA();
			if (usedWithSlot < 0 || usedWithSlot > 28 || itemUsedSlot < 0
					|| itemUsedSlot > 28)
				return;
			final int useWith = client.playerItems[usedWithSlot] - 1;
			final int itemUsed = client.playerItems[itemUsedSlot] - 1;
			if (Fletching.isFletchable(client, itemUsed, useWith)) {
				// client.sendA
			}
			if (itemUsed == 1573 && useWith == 327 || itemUsed == 327
					&& useWith == 1573) {
				client.getActionAssistant().deleteItem(1573, 1);
				client.getActionAssistant().deleteItem(327, 1);
				client.getActionSender().addItem(1552, 1);
			}
			if (itemUsed == SpecialItemCrafting.chisel || itemUsed == 6157
					|| itemUsed == 6159 || itemUsed == 6161)
				SpecialItemCrafting.startProcess(client, itemUsedSlot, useWith);

			if (itemUsed == 227 && useWith == 753 || useWith == 227
					&& itemUsed == 753) {
				client.getHalloweenEvent().makeCadava();
				return;
			}

			if (itemUsed == 12435)
				if (client.getActionAssistant().playerHasItem(itemUsed)
						&& client.getActionAssistant().playerHasItem(useWith)) {
					if (System.currentTimeMillis()
							- client.lastSummoningSpecial < 3000) {
						client.getActionSender().sendMessage(
								"You have to wait some more to do this again.");
						return;
					}
					if (client.familiarId != 6873) {
						client.getActionSender().sendMessage(
								"You don't even have a pack-yak summoned!");
						return;
					}
					if (!client.multiZone() && client.getWildernessLevel() > 0
							&& client.getWildernessLevel() < 25) {
						client.getActionSender()
								.sendMessage(
										"@red@You cannot use your familiar below lvl 25 in a single combat zone.");
						return;
					}
					client.lastSummoningSpecial = System.currentTimeMillis();
					AnimationProcessor.addNewRequest(client, 7660, 0);
					client.freeBank = true;
					Player.getContainerAssistant().bankItem(client, useWith,
							usedWithSlot, 1);
					client.getActionAssistant().deleteItem(useWith,
							usedWithSlot, 1);
					client.getActionAssistant().deleteItem(itemUsed, 1);
					client.progress[87]++;
					if (client.progress[87] >= 100)
						Achievements.getInstance().complete(client, 87);
					else
						Achievements.getInstance().turnYellow(client, 87);
					client.getActionSender().sendMessage(
							"Your pack-yak transports your item to your bank!");

					GraphicsProcessor.addNewRequest(client, 1303, 0, 3);

					AnimationProcessor.addNewRequest(client, 7660, 0);
					client.freeBank = false;
				}
			if (itemUsed == NEEDLE || useWith == NEEDLE)
				if (useWith == 1745 && itemUsed == NEEDLE || itemUsed == 1745
						&& useWith == NEEDLE) {
					client.crafting = 1745;
					HideCrafting.createDragonHideInterface(client, 0);
					return;
				} else if (useWith == 2505 && itemUsed == NEEDLE
						|| itemUsed == 2505 && useWith == NEEDLE) {
					client.crafting = 2505;
					HideCrafting.createDragonHideInterface(client, 1);
					return;
				} else if (useWith == 2507 && itemUsed == NEEDLE
						|| itemUsed == 2507 && useWith == NEEDLE) {
					client.crafting = 2507;
					HideCrafting.createDragonHideInterface(client, 2);
					return;
				} else if (useWith == 2509 && itemUsed == NEEDLE
						|| itemUsed == 2509 && useWith == NEEDLE) {
					client.crafting = 2509;
					HideCrafting.createDragonHideInterface(client, 3);
					return;
				} else if (useWith == 1741 && itemUsed == NEEDLE
						|| itemUsed == 1741 && useWith == NEEDLE) {
					client.crafting = 1741;
					HideCrafting.createDragonHideInterface(client, 4);
					return;
				} else if (useWith == 1743 && itemUsed == NEEDLE
						|| itemUsed == 1743 && useWith == NEEDLE) {
					client.crafting = 1743;
					HideCrafting.createDragonHideInterface(client, 5);
					return;
				}

			if (itemUsed == 233 || useWith == 233)
				if (useWith == 9735 && itemUsed == 233 || itemUsed == 9735
						&& useWith == 233)
					PotionMixing.itemGrinding(client, itemUsed, useWith);
				else if (useWith == 5075 && itemUsed == 233 || itemUsed == 5075
						&& useWith == 233)
					PotionMixing.itemGrinding(client, itemUsed, useWith);
				else if (useWith == 237 && itemUsed == 233 || itemUsed == 237
						&& useWith == 233)
					PotionMixing.itemGrinding(client, itemUsed, useWith);
				else if (useWith == 4698 && itemUsed == 233 || itemUsed == 4698
						&& useWith == 233)
					PotionMixing.itemGrinding(client, itemUsed, useWith);
				else if (useWith == 243 && itemUsed == 233 || itemUsed == 243
						&& useWith == 233)
					PotionMixing.itemGrinding(client, itemUsed, useWith);
			if (itemUsed == PotionMixing.VIAL || useWith == PotionMixing.VIAL)
				if (itemUsed == PotionMixing.VIAL)
					PotionMixing.makeUnfinished(client, PotionMixing.VIAL,
							useWith);
				else
					PotionMixing.makeUnfinished(client, PotionMixing.VIAL,
							itemUsed);
			if (PotionMixing.isUnFinishedPotion(client, itemUsed)
					|| PotionMixing.isUnFinishedPotion(client, useWith))
				if (PotionMixing.isExtraMixture(client, itemUsed))
					PotionMixing.makeFinished(client, useWith);
				else if (PotionMixing.isExtraMixture(client, useWith))
					PotionMixing.makeFinished(client, itemUsed);

			if (TINDERBOX == useWith || TINDERBOX == itemUsed)
				Firemaking.startFire(client, itemUsed, useWith);

			if (client.getSeedling().placeSeedInPot(itemUsed, useWith,
					itemUsedSlot, usedWithSlot))
				return;
			if (client.getSeedling().waterSeedling(itemUsed, useWith,
					itemUsedSlot, usedWithSlot))
				return;

			/*
			 * Herblore Potion Mixing
			 */
			String nameWith = "";
			if (InstanceDistributor.getItemManager().getItemDefinition(useWith) != null)
				nameWith = InstanceDistributor.getItemManager()
						.getItemDefinition(useWith).getName();
			String nameUsed = "";
			if (InstanceDistributor.getItemManager()
					.getItemDefinition(itemUsed) != null)
				nameUsed = InstanceDistributor.getItemManager()
						.getItemDefinition(itemUsed).getName();
			if (nameWith.contains("(") && nameUsed.contains("("))
				Player.getDecanting().potionCombination(client, itemUsed,
						useWith);
			if (CHISEL == useWith || CHISEL == itemUsed)
				for (final int[] element : GemCrafting.craftGem)
					if (CHISEL != useWith) {
						if (useWith == element[0]) {
							GemCrafting.craftGem(client, useWith);
							return;
						}
					} else if (CHISEL != itemUsed)
						if (itemUsed == element[0]) {
							GemCrafting.craftGem(client, itemUsed);
							return;
						}
			ItemCreation.getInstance().createGodswords(client, itemUsed,
					useWith);
			ItemCreation.getInstance().createDragonPlatebody(client, itemUsed,
					useWith);
			ItemCreation.getInstance().createBlade(client, itemUsed, useWith);
			ItemCreation.getInstance().createBlessedSpiritShield(client,
					itemUsed, useWith);
			ItemCreation.getInstance().createDragonfireShield(client, itemUsed,
					useWith);
			ItemCreation.getInstance().createOramentItems(client, itemUsed,
					useWith);
			client.setStopRequired();
			if (CLAY == useWith && JUG_OF_WATER == itemUsed || CLAY == itemUsed
					&& JUG_OF_WATER == useWith) {
				Pottery.makeSoftClay(client);
				return;
			}
			if (BALL_OF_WALL == useWith || BALL_OF_WALL == itemUsed)
				for (int i = 0; i < GemCrafting.mendItems.length; i++)
					if (GemCrafting.mendItems[i][0] == useWith
							|| GemCrafting.mendItems[i][0] == itemUsed) {
						GemCrafting.string(client, i);
						break;
					}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

}