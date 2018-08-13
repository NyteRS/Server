package com.server2.model.entity.player.commands.impl;

import com.server2.InstanceDistributor;
import com.server2.model.Item;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;

/**
 * Pickup command
 * 
 * @author Graham
 */
public class Pickup implements Command {

	@Override
	public void execute(Player client, String command) {
		if (client.getPrivileges() == 2) {
			final String[] parts = command.split(" ");
			try {
				final int item = Integer.valueOf(parts[1]);
				int amount = Integer.valueOf(parts[2]);

				if (!Item.itemStackable[item] && amount > 1) {
					if (amount > 28)
						amount = 28;
					for (int i = 0; i < amount; i++)
						client.getActionSender().addItem(item, 1);
				} else
					client.getActionSender().addItem(item, amount);

				final String itemName = InstanceDistributor.getItemManager()
						.getItemDefinition(item).getName();
				client.sendMessage("[@red@Spawn@bla@] You spawn @dre@" + amount
						+ "@bla@, @dre@" + itemName);
			} catch (final Exception e) {
				client.getActionSender().sendMessage(
						"Syntax is ::item id amount.");
			}
		}
	}

}
