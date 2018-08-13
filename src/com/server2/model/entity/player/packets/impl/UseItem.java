package com.server2.model.entity.player.packets.impl;

/**
 * @author Rene
 */

import com.server2.InstanceDistributor;
import com.server2.content.UsingItemHandler;
import com.server2.content.actions.MithrilSeed;
import com.server2.content.misc.Dicing;
import com.server2.content.misc.Effigies;
import com.server2.content.misc.ExperienceLamps;
import com.server2.content.misc.InstructionInterface;
import com.server2.content.misc.RandomRewards;
import com.server2.content.misc.ShardTrading;
import com.server2.content.skills.crafting.EssencePouches;
import com.server2.content.skills.herblore.Cleaning;
import com.server2.model.combat.additions.MultiCannon;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;
import com.server2.util.Areas;
import com.server2.util.Misc;

public class UseItem implements Packet {

	private static int[] casketRewards = { 1000, 1000, 1000, 1000, 1000, 1000,
			1000, 1000, 2500, 1000, 1000, 1000, 1000, 1000, 1000, 5000, 2500,
			1000, 1000, 1000, 1000, 7500, 5000, 2500, 1000, 1000, 10000, 7500,
			5000, 2500, 25000, 10000, 7500, 50000, 25000, 100000 };

	@Override
	public void handlePacket(Player client, GamePacket packet) {

		@SuppressWarnings("unused")
		final int interfaaceID = packet.getLEShortA();
		final int itemSlot = packet.getShortA();
		final int itemID = packet.getLEShort();
		client.setStopRequired();
		if (!client.getActionAssistant().playerHasItem(itemID, 1))
			return;
		if (client.isBeingForcedToWalk())
			// Busy walking
			return;
		if (client.isDead() || client.isDeadWaiting() || client.hitpoints == 0)
			return;
		if (itemID == 756) {
			client.getActionSender()
					.sendMessage(
							"You're supposed to use these to kill the Halloween monsters with.");
			return;
		}
		if (itemID == 18839 || itemID == 18343 || itemID == 18344) {
			ExperienceLamps.unLockPrayer(client, itemID);
			return;
		}
		if (itemID == 299) {
			new MithrilSeed(client).plantFlower();
			return;
		}
		if (itemID == 15262) {
			ShardTrading.openShardPack(client);
			return;
		}
		if (itemID == 4049) {
			if (!Areas.isInCastleWarsGame(client.getPosition())) {
				client.getActionSender().sendMessage(
						"You cannot use these out of Castle Wars.");
				client.getActionAssistant().deleteItem(4049, 28);
				return;
			}
			if (client.hitpoints == client
					.getLevelForXP(client.playerXP[PlayerConstants.HITPOINTS])) {
				client.getActionSender().sendMessage("You're already full HP.");
				return;
			}
			final int toAdd = client
					.getLevelForXP(client.playerXP[PlayerConstants.HITPOINTS]);
			client.getActionAssistant().addHP(toAdd / 10);
			client.getActionAssistant().deleteItem(itemID, 1);
			client.getActionSender().sendMessage("The bandage heals you.");
		}
		if (itemID == 15389) {
			ExperienceLamps.handleDungLamp(client);
			return;

		}
		if (itemID == 4155) {
			client.getActionSender().selectOption("Select",
					"Normal Slayer Task", "Duo Slayer Task");
			client.dialogueAction = 13999;
			return;
		}

		if (itemID == 18782) {
			client.getActionSender().selectOption("Select a category",
					"Combat", "Gathering", "Support", "Artisan", "Nevermind");
			client.dialogueAction = 2896579;
			return;
		}
		if (itemID == 18778 || itemID == 18779 || itemID == 18781
				|| itemID == 18780) {

			client.getActionSender().selectOption(
					"Select",
					PlayerConstants.SKILL_NAMES[Effigies
							.getSkillNeeded(client.effigyType)[0]],
					PlayerConstants.SKILL_NAMES[Effigies
							.getSkillNeeded(client.effigyType)[1]]);
			client.dialogueAction = 4945859;
			client.itemUsed = itemID;
			return;

		}
		if (itemID == 1856)
			InstanceDistributor.getDB().handleDungeoneeringBook(client,
					client.getFloor());
		if (itemID == 6199)
			RandomRewards.giveBoxResult(client);
		if (itemID == 18768)
			RandomRewards.skillerBox(client);
		if (itemID == 20935) {
			RandomRewards.voteBox(client);
			return;
		}
		if (itemID == 6183)
			RandomRewards.godBox(client);
		if (itemID == 3062)
			RandomRewards.coinBox(client);
		if (itemID == 10025)
			RandomRewards.potLuckBox(client);
		if (itemID == 952)
			InstanceDistributor.getBarrows().handleSpade(client);
		if (itemID == 6)
			MultiCannon.getInstance().lay(client);

		if (itemID == 6812)
			client.getActionSender()
					.sendMessage(
							"You shouldn't bury these bones because you can make super prayer potions with them.");
		if (itemID == 11137)
			ExperienceLamps.handleSlayerLamp(client);
		if (itemID == 15389)
			ExperienceLamps.handleDungLamp(client);
		if (client.playerItems[itemSlot] == itemID + 1) {

			if (itemID == 405
					&& client.getActionAssistant().isItemInBag(itemID)) {
				client.getActionAssistant().deleteItem(itemID, itemSlot, 1);
				client.getActionSender().addItem(995,
						casketRewards[Misc.random(casketRewards.length - 1)]);
			}
			if (itemID > 15085 && itemID < 15102)
				Dicing.getInstance().useDice(client, itemID, false, 0);
			if (itemID == 15084) {
				client.diceID = itemID;
				client.getDM().sendDialogue(106, 0);
			}

			UsingItemHandler.useItem(client, itemID, itemSlot);
			Cleaning.cleanHerb(client, itemID, itemSlot);

			switch (itemID) {
			case 8013:
				client.getPlayerTeleportHandler().teleTab(itemID, "HOME");
				break;
			case 8007:
				client.getPlayerTeleportHandler().teleTab(itemID, "VARROCK");
				break;
			case 8008:
				client.getPlayerTeleportHandler().teleTab(itemID, "LUMBRIDGE");
				break;
			case 8009:
				client.getPlayerTeleportHandler().teleTab(itemID, "FALADOR");
				break;
			case 8010:
				client.getPlayerTeleportHandler().teleTab(itemID, "CAMELOT");
				break;
			case 8011:
				client.getPlayerTeleportHandler().teleTab(itemID, "ARDOUGNE");
				break;
			case 15707:
				InstanceDistributor.getDung().teleToMaster(client);
				break;
			case 5:
				InstructionInterface.sendWelcomeInterface(client);
				break;

			case 5509:
				EssencePouches.fillPouch(client, itemID, 3);
				break;
			case 5511:
				EssencePouches.fillPouch(client, itemID, 6);
				break;
			case 5512:
				EssencePouches.fillPouch(client, itemID, 9);
				break;
			case 5515:
				EssencePouches.fillPouch(client, itemID, 12);
				break;

			case 5070:
			case 5071:
			case 5072:
			case 5073:
			case 5074:
				RandomRewards.seedReward(client, itemID);
				break;

			}
		}
	}

}
