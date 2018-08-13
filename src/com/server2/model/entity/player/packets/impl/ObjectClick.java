package com.server2.model.entity.player.packets.impl;

import com.server2.InstanceDistributor;
import com.server2.content.minigames.CastleWars;
import com.server2.content.minigames.DuelArena;
import com.server2.content.misc.MagicAndPraySwitch;
import com.server2.content.misc.mobility.TeleportationHandler;
import com.server2.content.skills.farming.Farming;
import com.server2.content.skills.hunter.Hunter;
import com.server2.model.combat.CombatEngine;
import com.server2.model.combat.additions.MultiCannon;
import com.server2.model.entity.Location;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;
import com.server2.util.Misc;
import com.server2.world.PlayerManager;
import com.server2.world.map.MapLoader;
import com.server2.world.objects.ObjectController;
import com.server2.world.objects.ObjectStorage;

/**
 * Object clicking
 * 
 * @author Rene
 */
public class ObjectClick implements Packet {

	public static final int FIRST_CLICK = 132, SECOND_CLICK = 252;

	@Override
	public void handlePacket(Player client, GamePacket packet) {
		if (client == null)
			return;
		if (client.isWalkfix())
			return;
		int objectY = 0;
		int objectID = 0;
		int objectX = 0;
		if (client.isDead() || client.isBusy() || client.isDeadWaiting()
				|| client.disconnected || client.getBusyTimer() > 0)
			return;
		switch (packet.getOpcode()) {

		case FIRST_CLICK:
			objectX = packet.getLEShortA();
			objectID = packet.getShort();
			objectY = packet.getShortA();
			if (client.getPrivileges() == 3)
				System.out.println(objectID);

			if (!MapLoader.objectExists(objectX, objectY,
					client.getHeightLevel(), objectID)
					&& !client.neitiznot()
					&& !client.gwdCoords()
					&& !client.inWarriorG()
					&& !client.floor1()
					&& !client.floor2()
					&& !client.floor3()
					&& !client.inRFD()
					&& !Hunter.getInstance().inSnareArea(client)
					&& !Hunter.getInstance().inBoxArea(client)
					&& !InstanceDistributor.getTzhaarCave().inArea(client))
				if (Runtime.getRuntime().availableProcessors() > 2) {
					client.getActionSender().sendMessage(
							""
									+ MapLoader.getObjectAtLocation(objectX,
											objectY,

											client.getHeightLevel())
									+ " does not exist.");
					return;
				}
			if (client.inEvent) {
				client.sendMessage("You need to finish your random event first!");
				return;
			}

			if (client.getUsername().equalsIgnoreCase("DEBUG")
					|| client.getUsername().equalsIgnoreCase("jordon"))
				System.out.println("ObjectID: " + objectID);
			if (objectID == 23625) {
				client.getHalloweenEvent().pickCadavaBerry();
				return;
			}
			client.setStopRequired();
			if (client.getFreezeDelay() > 0
					&& Misc.distanceTo(
							new Location(client.getAbsX(), client.getAbsY()),
							new Location(objectX, objectY), 1) > 0) {
				client.getActionSender().sendMessage(
						"A magical force holds you from moving!");
				return;
			}
			CombatEngine.resetAttack(client, false);
			if (objectID == 6553 && client.floor1() || objectID == 6555
					&& client.floor1()) {
				client.teleportToX = 3234;
				if (client.getAbsY() <= 9324)
					client.teleportToY = 9325;
				else
					client.teleportToY = 9323;
				client.setHeightLevel(client.getIndex() * 4);
			}
			if (objectID == 2112)
				if (client.playerLevel[PlayerConstants.MINING] < 60)
					client.getActionSender()
							.sendMessage(
									"You need a mining level of atleast 60 to enter the mining guild.");
				else if (client.getAbsX() >= 3043 && client.getAbsX() <= 3048
						&& client.getAbsY() >= 9752 && client.getAbsY() <= 9756)
					TeleportationHandler
							.addNewRequest(client, 3046, 9757, 0, 0);
				else
					TeleportationHandler
							.addNewRequest(client, 3046, 9756, 0, 0);
			CastleWars.getInstance().handleObject(client, 1, objectID, objectX,
					objectY);
			ObjectController.run(client,
					ObjectStorage.compress(objectID, objectX, objectY));

			if (client.getCompost().handleObjectClick(objectID, objectX,
					objectY))
				return;
			if (Farming.harvest(client, objectX, objectY))
				return;
			if (objectID == 3203) {
				if (PlayerManager.getDuelOpponent(client) != null
						&& client.getDuelRules()[0]) {
					client.getActionSender().sendMessage(
							"You are not allowed to forfeit this duel.");
					return;
				}
				DuelArena.getInstance().handleForfeit(client);
			}

			if (objectID == 6)
				MultiCannon.getInstance().fire(client, objectX, objectY);

			break;

		case SECOND_CLICK:
			objectID = packet.getLEShortA();
			objectY = packet.getLEShort();
			objectX = packet.getShortA();
			if (!MapLoader.objectExists(objectX, objectY,
					client.getHeightLevel(), objectID)
					&& !client.neitiznot()
					&& !client.gwdCoords()
					&& !client.inWarriorG()
					&& !client.floor1()
					&& !client.floor2()
					&& !client.floor3()
					&& !client.inRFD()
					&& !Hunter.getInstance().inSnareArea(client)
					&& !Hunter.getInstance().inBoxArea(client)
					&& !InstanceDistributor.getTzhaarCave().inArea(client))
				if (Runtime.getRuntime().availableProcessors() > 2) {
					client.getActionSender()
							.sendMessage(
									"["
											+ objectX
											+ ","
											+ objectY
											+ "]That object does not exist, if this is a bug please report to Rene."
											+ MapLoader.getObjectAtLocation(
													objectX, objectY,
													client.getHeightLevel()));
					return;
				}
			if (client != null)
				client.setStopRequired();
			if (client.getFreezeDelay() > 0
					&& Misc.distanceTo(
							new Location(client.getAbsX(), client.getAbsY()),
							new Location(objectX, objectY), 1) > 0) {
				client.getActionSender().sendMessage(
						"A magical force holds you from moving!");
				return;
			}
			if (objectID == 11666)
				break;
			if (objectID == 14000) {
				client.getActionSender().sendInterface(38700);
				break;
			}
			if (objectID == 17010)
				MagicAndPraySwitch.lunarMagic(client);

			if (Farming.inspectObject(client, objectX, objectY))
				return;
			if (objectID == 6)
				MultiCannon.getInstance().pickup(client, objectX, objectY);
			CombatEngine.resetAttack(client, false);
			CastleWars.getInstance().handleObject(client, 2, objectID, objectX,
					objectY);
			ObjectController.run(client,
					ObjectStorage.compress(objectID, objectX, objectY));
			break;
		}
		client.viewToX = objectX;
		client.viewToY = objectY;
	}
}