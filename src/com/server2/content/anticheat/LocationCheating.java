package com.server2.content.anticheat;

import com.server2.content.JailSystem;
import com.server2.content.minigames.DuelArena;
import com.server2.content.misc.mobility.TeleportationHandler;
import com.server2.content.skills.dungeoneering.Dungeoneering;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.Player.DuelStage;
import com.server2.util.Areas;
import com.server2.util.SpecialRights;
import com.server2.world.PlayerManager;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class LocationCheating {

	/**
	 * Instances the LocationCheating class.
	 */
	public static LocationCheating INSTANCE = new LocationCheating();

	/**
	 * Gets the LocationCheating instance.
	 */
	public static LocationCheating getInstance() {
		return INSTANCE;
	}

	/**
	 * Checks the location of specific users.
	 * 
	 * @param client
	 */
	public void checkLocation(Player client) {
		if (SpecialRights.isSpecial(client.getUsername()))
			return;
		if (client.getAbsX() == 2649 && client.getAbsY() == 4569
				&& client.getHeightLevel() == 0)
			client.setHeightLevel(1);
		if (client.glitchedInBank()) {
			client.teleportToX = 3093;
			client.teleportToY = 3494;
			client.getActionSender().sendMessage(
					"You've been teleported out of the banker's place.");
		}
		if (Areas.isInDZone(client))
			if (client.getPrivileges() == 0) {
				client.teleportToX = 3093;
				client.teleportToY = 3493;
				client.getActionSender()
						.sendMessage(
								"You have been kicked out of the donator zone as you are not a donator!");
			}
		if (JailSystem.inJail(client))
			if (!Areas.isInJail(client)) {
				TeleportationHandler.addNewRequest(client, 2775, 2798, 0, 0);
				client.getActionSender()
						.sendMessage(
								"You have been placed back in jail! There is no escape!");
			}
		if (!client.isDunging)
			if (client.floor1() || client.floor2() || client.floor3()
					|| Areas.bossRoom1(client.getPosition()))
				Dungeoneering.getInstance().forceLeaveDungeon(client);
		if (client.isDunging)
			if (!client.floor1()
					&& !client.floor2()
					&& !client.floor3()
					&& !Areas.bossRoom1(client.getPosition())
					&& System.currentTimeMillis() - client.lastDungEntry > 10000) {
				client.getActionSender()
						.sendMessage(
								"You went out of the map while dunging, quiting dungeon...");
				Dungeoneering.getInstance().forceLeaveDungeon(client);
			}
		if (client.floor1() || client.floor2()
				|| Areas.bossRoom1(client.getPosition()) || client.floor3())
			if (!DungeoneeringForceMovementPrevention.correctHeight(client)
					&& System.currentTimeMillis() - client.tempLock > 5000) {
				client.getActionSender().sendMessage(
						"You are not on your own assigned floor, exiting.");
				Dungeoneering.getInstance().forceLeaveDungeon(client);
			}
		if (client.castleWarsing)
			if (!Areas.isInCastleWarsGame(client.getPosition())) {
				/*
				 * client.getActionSender() .sendMessage(
				 * "You were not in the castle wars room anymore, exiting");
				 * CastleWars.getInstance().onDisconnect(client);
				 */
			}

		if (client.isDueling)
			if (!Areas.isInDuelArenaFight(client.getPosition())
					&& System.currentTimeMillis() - client.duelStart > 2500) {
				client.isDueling = false;
				final Player opponent = PlayerManager.getDuelOpponent(client);
				;
				client.restoreStats();
				client.setDuelStage(DuelStage.WAITING);
				client.getActionSender().sendLocationPointerPlayer(0, 0);
				client.teleport(DuelArena.DEATH_RESPAWN_POINT);
				client.setDuelOpponent(null);
				if (opponent != null
						&& Areas.isInDuelArenaFight(opponent.getPosition())) {
					opponent.isDueling = false;
					client.duelDeathPrevention = System.currentTimeMillis();
					opponent.duelDeathPrevention = System.currentTimeMillis();
					DuelArena.getInstance().awardWin(opponent, client);
				}
			}

	}

}
