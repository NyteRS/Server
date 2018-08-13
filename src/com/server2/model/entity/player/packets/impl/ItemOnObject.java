package com.server2.model.entity.player.packets.impl;

import com.server2.InstanceDistributor;
import com.server2.content.skills.farming.Farming;
import com.server2.content.skills.hunter.Bait;
import com.server2.content.skills.hunter.Hunter;
import com.server2.content.skills.hunter.Trap;
import com.server2.content.skills.hunter.TrapUtils;
import com.server2.content.skills.prayer.BonesOnAltar;
import com.server2.content.skills.smithing.SmithingMakeInterface;
import com.server2.model.combat.additions.MultiCannon;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;
import com.server2.world.XMLManager;
import com.server2.world.XMLManager.Cooking;
import com.server2.world.objects.ObjectController;
import com.server2.world.objects.ObjectStorage;

public class ItemOnObject implements Packet {

	@Override
	public void handlePacket(Player client, GamePacket packet) {
		final int junk = packet.getShort();
		final int objectId = packet.getLEShort();
		final int objectY = packet.getLEShortA();
		final int junk2 = packet.getShort();
		final int objectX = packet.getLEShortA();
		final int itemId = packet.getShort();

		if (client.getUsername() == "Jordon")
			client.sendMessage("Item: @dre@" + itemId
					+ "@bla@, used on Object: @dre@" + objectId);
		client.setStopRequired();
		if (client.isBusy())
			return;

		if (objectId == 6 && itemId == 2)
			MultiCannon.getInstance().fillCannon(client);
		if (objectId == 12102 || objectId == 12269 || objectId == 8712
				|| objectId == 9085 || objectId == 9086 || objectId == 9087
				|| objectId == 2728 || objectId == 2729 || objectId == 2730
				|| objectId == 2731 || objectId == 2859 || objectId == 3039
				|| objectId == 5275 || objectId == 114 || objectId == 8750)
			for (final Cooking food : XMLManager.ingredients)
				if (food.getRawType() == itemId) {
					client.cooking = itemId;
					ObjectController.run(client,
							ObjectStorage.compress(objectId, objectX, objectY));
					return;
				}
		if (Farming.prepareCrop(client, itemId, objectId, objectX, objectY))
			return;
		if (objectId == 12102 || objectId == 2782 || objectId == 2783
				|| objectId == 4306 || objectId == 6150) {
			client.getActionAssistant().turnTo(client.objectX,
					client.objectY + 1);
			if (client.getActionAssistant().playerHasItem(2347, 1)) {
				if (itemId == 2349)
					new SmithingMakeInterface(client, objectId, "BRONZE",
							client.objectX, client.objectY);
				else if (itemId == 2351)
					new SmithingMakeInterface(client, objectId, "IRON",
							client.objectX, client.objectY);
				else if (itemId == 2359)
					new SmithingMakeInterface(client, objectId, "MITH",
							client.objectX, client.objectY);
				else if (itemId == 2353)
					new SmithingMakeInterface(client, objectId, "STEEL",
							client.objectX, client.objectY);
				else if (itemId == 2361)
					new SmithingMakeInterface(client, objectId, "ADDY",
							client.objectX, client.objectY);
				else if (itemId == 2363)
					new SmithingMakeInterface(client, objectId, "RUNE",
							client.objectX, client.objectY);
				else
					client.getActionSender().sendMessage(
							"You can't use this item on an anvil!");
			} else
				client.getActionSender().sendMessage(
						"You need a hammer to make items!");
		}
		if (objectId == 15621) {
			client.stopMovement();
			InstanceDistributor.getWarriorsGuild()
					.spawnAnimator(client, itemId);
			return;
		}

		if (itemId == 526 || itemId == 528 || itemId == 530 || itemId == 532
				|| itemId == 534 || itemId == 536 || itemId == 18830
				|| itemId == 4834 || itemId == 6729)
			if (objectId == 409) {
				client.boneId = itemId;
				BonesOnAltar.openDialogue(client);
			}

		if (itemId == 9996 || itemId == 1982) {
			final Trap trap = Hunter.getInstance().getPlayerTrap(objectX,
					objectY);
			if (trap != null && trap.getGameObject().getOwner() == client) {
				final Bait bait = TrapUtils.getInstance().getBait(itemId);
				if (bait != null)
					TrapUtils.getInstance().baitTrap(client, trap, bait);
			}
		}

		if (objectId == 2732 || objectId == 3038 || objectId == 3769
				|| objectId == 3775 || objectId == 4265 || objectId == 4266
				|| objectId == 5499 || objectId == 5249 || objectId == 5631
				|| objectId == 5632 || objectId == 5981)
			for (final Cooking food : XMLManager.ingredients)
				if (food.getRawType() == itemId) {
					client.cooking = itemId;
					ObjectController.run(client,
							ObjectStorage.compress(objectId, objectX, objectY));
					return;
				}
		if (itemId >= 434 && itemId <= 454)
			if (objectId == 12100 || objectId == 2781 || objectId == 2785
					|| objectId == 2966 || objectId == 3044 || objectId == 3294
					|| objectId == 3413 || objectId == 4304 || objectId == 4305
					|| objectId == 6189 || objectId == 6190
					|| objectId == 11666) {
				client.getActionAssistant().turnTo(objectX, objectY + 1);
				for (int fi = 0; fi < PlayerConstants.smelt_frame.length; fi++) {
					client.getActionSender().sendFrame246(
							PlayerConstants.smelt_frame[fi], 150,
							PlayerConstants.smelt_bars[fi]);
					client.getActionSender().sendFrame164(2400);
				}
			}
		switch (junk) {
		}
		switch (junk2) {
		}

		switch (itemId) {
		case 1074:
			break;

		case 1592:
		case 1595:
		case 1597:
			if (objectId == 11666 || objectId == 2643)
				ObjectController.run(client,
						ObjectStorage.compress(objectId, objectX, objectY));
			break;

		case 444:// gold ore
			if (objectId == 11666 || objectId == 2643) { // furnaces
				client.oreId = itemId;
				ObjectController.run(client,
						ObjectStorage.compress(objectId, objectX, objectY));
			}
			break;

		case 1935:
			switch (objectId) {
			case 873:
			case 874:
			case 879:
			case 880:
			case 4063:
			case 6151:
			case 8699:
			case 9143:
			case 9684:
			case 10175:
			case 13564:
			case 13563:
			case 12974:
			case 12279:
				ObjectController.run(client,
						ObjectStorage.compress(objectId, objectX, objectY));
				break;
			default:
				break;
			}

		default:
			break;
		}

	}

}
