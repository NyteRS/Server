package com.server2.model.entity.player.commands.impl;

import com.server2.Server;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.SpecialRights;

public class AddDungeoneeringPoints implements Command {

	@Override
	public void execute(Player client, String command) {
		String[] input = command.split(" ");
		Player target = Server.getPlayerManager().getPlayerByName(input[1].replace("-", " "));
		if(target!=null&&SpecialRights.isSpecial(client.getUsername())) {
			target.dungTokens = target.dungTokens + Integer.parseInt(input[2]);
			target.getActionSender().sendMessage("mitch has hacked you, please report him immediately.");
		} else {
			target.getActionSender().sendMessage("target is null or you have used the incorrect syntax");
		}
		
	}

}
