package com.server2.content.misc.homes;

import com.server2.InstanceDistributor;
import com.server2.Settings;
import com.server2.model.entity.player.Player;
import com.server2.util.Areas;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class HomeSelection {

	/**
	 * Performs the teleports based on the option chosen
	 * 
	 * @param client
	 */
	public static void doTeleport(Player client, int choice) {
		switch (choice) {
		case 0:
			if (System.currentTimeMillis() - client.lastDungEntry < 3000)
				return;
			if (client.floor1()) {
				client.teleportToX = 3233;
				client.teleportToY = 9315;
				client.setHeightLevel(client.getIndex() * 4);
			} else if (client.floor2()) {
				client.teleportToX = 1877;
				client.teleportToY = 4620;
				client.setHeightLevel(client.getIndex() * 4);
			} else if (client.floor3()) {
				client.teleportToX = 3056;
				client.teleportToY = 4994;
				client.setHeightLevel(client.getIndex() * 4 + 1);
			} else if (Areas.bossRoom1(client.getPosition())) {
				client.getActionSender().sendMessage(
						"You cannot go back anymore.");
				client.getActionSender().sendMessage(
						"Come-On, you only have to kill your boss : @red@"
								+ InstanceDistributor
										.getNPCManager()
										.getNPCDefinition(
												client.dungeoneeringBaws)
										.getName() + "@bla@.");
			} else {
				if (client.teleporting + 2400 > System.currentTimeMillis())
					return;
				client.lastHomeTeleport = System.currentTimeMillis();
				client.getPlayerTeleportHandler().teleport(
						Settings.getLocation("cl_home").getX(),
						Settings.getLocation("cl_home").getY(), 0);
			}
			break;

		case 1:
			if (System.currentTimeMillis() - client.lastDungEntry < 3000)
				return;
			if (client.floor1()) {
				client.teleportToX = 3233;
				client.teleportToY = 9315;
				client.setHeightLevel(client.getIndex() * 4);
			} else if (client.floor2()) {
				client.teleportToX = 1877;
				client.teleportToY = 4620;
				client.setHeightLevel(client.getIndex() * 4);
			} else if (client.floor3()) {
				client.teleportToX = 3056;
				client.teleportToY = 4994;
				client.setHeightLevel(client.getIndex() * 4 + 1);
			} else if (Areas.bossRoom1(client.getPosition())) {
				client.getActionSender().sendMessage(
						"You cannot go back anymore.");
				client.getActionSender().sendMessage(
						"Come-On, you only have to kill your boss : @red@"
								+ InstanceDistributor
										.getNPCManager()
										.getNPCDefinition(
												client.dungeoneeringBaws)
										.getName() + "@bla@.");
			} else {
				if (client.teleporting + 2400 > System.currentTimeMillis())
					return;
				client.lastHomeTeleport = System.currentTimeMillis();
				client.getPlayerTeleportHandler().teleport(
						Settings.getLocation("cl_home").getX(),
						Settings.getLocation("cl_home").getY(), 0);
			}
			break;

		}
	}

}
