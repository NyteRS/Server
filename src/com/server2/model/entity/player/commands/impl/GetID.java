package com.server2.model.entity.player.commands.impl;

import com.server2.InstanceDistributor;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;

public class GetID implements Command {

	@Override
	public void execute(final Player client, String command) {
		final String[] items = command.split(" ");
		if (client.getPrivileges() > 1
				&& InstanceDistributor.getItemManager().getItemDefinition(
						items[1].replace("-", " ")) != null) {
			final int itemId = InstanceDistributor.getItemManager()
					.getItemDefinition(items[1].replace("-", " ")).getId();
			client.getActionSender()
					.sendMessage(
							"ID of " + items[1].replace("-", " ") + " = "
									+ itemId + "");
		} else if (client.getPrivileges() < 2)
			client.getActionSender().sendMessage(
					"You need to be an administrator to use this command");
		else {
			client.getActionSender().sendMessage("Syntax is ::getid name");
			client.getActionSender().sendMessage(
					"If name contains a space, use a '-' to represent a space");
			client.getActionSender().sendMessage("E.G. ::getid Bronze-dagger");
		}

	}

}
