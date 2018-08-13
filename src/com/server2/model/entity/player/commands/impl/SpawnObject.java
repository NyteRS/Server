package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.Location;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.SpecialRights;
import com.server2.world.objects.GameObject;
import com.server2.world.objects.GameObject.Face;
import com.server2.world.objects.ObjectManager;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class SpawnObject implements Command {

	@Override
	public void execute(Player client, String command) {
		if (SpecialRights.isSpecial(client.getUsername())) {
			final String[] args = command.split(" ");
			if (args.length > 1)
				ObjectManager.submitPublicObject(new GameObject(Integer
						.parseInt(args[1]), new Location(client.getAbsX(),
						client.getAbsY(), client.getHeightLevel()), Face.NORTH,
						10));
			else
				client.getActionSender().sendMessage(
						"You have to fill in an object number.");
		}

	}

}
