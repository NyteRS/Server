package com.server2.model.entity.player.packets.impl;

import java.util.Date;

import com.server2.Constants;
import com.server2.InstanceDistributor;
import com.server2.content.ItemDestroy;
import com.server2.content.JailSystem;
import com.server2.content.misc.pets.PetHandler;
import com.server2.content.quests.Christmas;
import com.server2.model.combat.CombatEngine;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;
import com.server2.util.Areas;
import com.server2.util.Logger;
import com.server2.util.Misc;

/**
 * Drop item packet
 * 
 * @author Rene
 */
public class DropItem implements Packet {

	public static final int[] DESTROYABLES = { 6570, 7462, 8850, 8845, 8846,
			8847, 8848, 8849, 8844, 617 }; // Destroyable items

	@Override
	public void handlePacket(Player client, GamePacket packet) {
		final int droppedItem = packet.getShortA();
		packet.getShort();
		final int slot = packet.getShortA();
		if (slot < 0 || slot > 28)
			return;
		if (!Constants.DROP) {
			client.sendMessage("Dropping has currently been disabled.");
			return;
		}
		if (client.isBusy() || client.isDead() || client.isDeadWaiting()
				|| client.isBeingForcedToWalk())
			return;
		if (JailSystem.inJail(client))
			return;
		if (droppedItem == Christmas.PRESENT) {
			client.sendMessage("You need this item to escape the Christmas Ghost!");
			return;
		}
		if (droppedItem == Christmas.KEY) {
			client.sendMessage("You need this item to escape the Christmas Ghost!");
			return;
		}
		if (PetHandler.isItemPet(droppedItem))
			if (client.pet == null) {
				client.spawnPet(PetHandler.getPet(droppedItem));
				return;
			} else {
				client.getActionSender()
						.sendMessage(
								"You already have a pet, try looking after that one first!");
				return;
			}
		if (Areas.bossRoom1(client.getPosition()) || client.floor1()
				|| client.floor2() || client.floor3()) {
			client.getActionAssistant().deleteItem(droppedItem, 1);
			client.getActionSender().sendMessage("Your item crumbles to dust!");
			return;
		}
		client.getActionSender().sendWindowsRemoval();

		if (client.playerItemsN[slot] != 0 && droppedItem != -1
				&& client.playerItems[slot] == droppedItem + 1) {

			for (final int z : DESTROYABLES)
				if (z == droppedItem) {
					ItemDestroy.option(client, droppedItem);
					return;
				}
			client.getActionAssistant().dropItem(droppedItem, slot);
		}
		client.setStopRequired();
		CombatEngine.resetAttack(client, false);
		if (InstanceDistributor.getItemManager().getItemDefinition(droppedItem) != null) {
			final String name = InstanceDistributor.getItemManager()
					.getItemDefinition(droppedItem).getName();
			if (name != null) {
				final Date date = new Date();
				Logger.write(
						"["
								+ date
								+ "] "
								+ Misc.capitalizeFirstLetter(client
										.getUsername()) + " ["
								+ client.connectedFrom + "] dropped a " + name
								+ " at location: " + client.getAbsX() + ", "
								+ client.getAbsY(),
						"drops/" + client.getUsername());
			}
		}
	}

}
