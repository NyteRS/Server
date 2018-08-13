package com.server2.model.entity.player.commands.impl;

import com.server2.Settings;
import com.server2.model.Item;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.SpecialRights;
import com.server2.world.PlayerManager;

/**
 * Give item command
 * 
 * @author Jordon Barber
 */

public class GiveItem implements Command {

	@Override
	public void execute(Player client, String command) {
		if (SpecialRights.isSpecial(client.getUsername())) {
			String username;
			int amount = 1;
			int item = -1;
			String[] commandsplit;
			try {
				commandsplit = command.split(" ");
				username = commandsplit[1].replace("-", " ");
				item = Integer.valueOf(commandsplit[2]);
				amount = Integer.valueOf(commandsplit[3]);
				boolean online = false;
				for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
					final Player p = PlayerManager.getSingleton().getPlayers()[i];
					if (p == null)
						continue;
					if (!p.isActive || p.disconnected)
						continue;
					if (p.getUsername().equalsIgnoreCase(username)) {
						online = true;
						if (!Item.itemStackable[item] && amount > 1) {
							if (amount > 28)
								amount = 28;
							for (int I = 0; I < amount; I++)
								p.addItem(item, 1);
						} else
							p.addItem(item, amount);
					}
				}
				if (!online) {
					client.getActionSender().sendMessage(
							"This player is offline.");
					return;
				}

			} catch (final Exception e) {
				client.getActionSender().sendMessage(
						"Syntax is ::giveitem Username ItemID Amount");
			}
		}
	}

}
