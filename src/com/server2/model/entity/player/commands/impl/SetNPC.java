package com.server2.model.entity.player.commands.impl;

import com.server2.InstanceDistributor;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.SpecialRights;

public class SetNPC implements Command {

	@Override
	public void execute(Player client, String command) {
		if (SpecialRights.isSpecial(client.getUsername()))
			if (command.length() > 4) {
				final int npcId = Integer.valueOf(command.substring(7));
				if (client.isNPC && npcId == -1) {
					client.isNPC = false;
					client.getActionSender().sendMessage(
							"You have changed back to human.");
					client.getActionSender().sendAnimationReset();
					return;
				} else {
					client.npcID = npcId;
					client.isNPC = true;
					if (InstanceDistributor.getNPCManager().getNPCDefinition(
							npcId) != null) {
						client.getActionSender().sendMessage(
								"You changed into an "
										+ InstanceDistributor.getNPCManager()
												.getNPCDefinition(npcId)
												.getName().toLowerCase() + ".");
						client.getActionSender().sendMessage(
								"To change back to human type ::setnpc -1.");
					}
				}
			} else
				client.getActionSender().sendMessage(
						"Wrong syntax use ::setnpc #id.");
	}
}