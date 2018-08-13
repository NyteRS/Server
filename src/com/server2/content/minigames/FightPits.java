package com.server2.content.minigames;

import java.util.concurrent.CopyOnWriteArrayList;

import com.server2.Constants;
import com.server2.Settings;
import com.server2.content.Achievements;
import com.server2.model.combat.CombatEngine;
import com.server2.model.combat.additions.Specials;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;
import com.server2.world.PlayerManager;
import com.server2.world.map.tile.TileManager;

/**
 * 
 * @author Rene FightPits
 */

public class FightPits {

	private static String previousWinner = "No one";

	private static int LIMIT = 120;

	public static int nextRoundDelay = LIMIT;
	public static boolean matchActive = false;

	private static CopyOnWriteArrayList<Player> inWaitingRoom = new CopyOnWriteArrayList<Player>();
	private static CopyOnWriteArrayList<Player> inFightPits = new CopyOnWriteArrayList<Player>();

	public static void addToFightPits(Player client) {
		if (!inFightPits.contains(client))
			inFightPits.add(client);
	}

	public static void addToWaitingRoom(Player client) {
		if (!inWaitingRoom.contains(client))
			inWaitingRoom.add(client);
	}

	public static void fightPitsOrb(String setting, int frame, Player client) {
		client.dialogueAction = 81296;
		client.getActionSender().sendString("@yel@Centre", 15239);

		client.getActionSender().sendString("@yel@North-West", 15240);

		client.getActionSender().sendString("@yel@North-East", 15241);

		client.getActionSender().sendString("@yel@South-East", 15242);

		client.getActionSender().sendString("@yel@South-West", 15243);

		client.getActionSender().sendString("@whi@" + setting, frame);

	}

	public static void hidePlayer(Player client) {
		if (isInViewingOrb(client))
			if (client.getHeightLevel() != 0)
				client.setHeightLevel(0);
			else if (client.getHeightLevel() == 0) {
				client.npcID = 1666;
				client.isNPC = true;
				client.setViewingOrb(true);
				client.updateRequired = true;
				client.appearanceUpdateRequired = true;
			}
	}

	public static boolean inFightArea(Player client) {
		final int[] myLocation = TileManager.currentLocation(client);

		return myLocation[0] >= 2375 && myLocation[0] <= 2418
				&& myLocation[1] >= 5129 && myLocation[1] <= 5167;
	}

	public static boolean inFightPits(Player client) {
		return inFightPits.contains(client);
	}

	public static boolean inWaitingArea(Player client) {
		final int[] myLocation = TileManager.currentLocation(client);

		return myLocation[0] >= 2395 && myLocation[0] <= 2403
				&& myLocation[1] >= 5169 && myLocation[1] <= 5175;
	}

	public static boolean inWaitingRoom(Player client) {
		return inWaitingRoom.contains(client);
	}

	public static boolean isInViewingOrb(Player client) {
		final int absX = client.getAbsX();
		final int absY = client.getAbsY();
		if (absX == 2398 && absY == 5150)
			return true;// center
		if (absX == 2384 && absY == 5157)
			return true;// north - west
		if (absX == 2409 && absY == 5158)
			return true;// north - east
		if (absX == 2411 && absY == 5137)
			return true;// south - east
		if (absX == 2388 && absY == 5138)
			return true;// south - west
		return false;
	}

	public static void removeFromFightPits(Player client) {
		if (inFightPits.contains(client))
			inFightPits.remove(client);
	}

	public static void removeFromWaitingRoom(Player client) {
		if (inWaitingRoom.contains(client))
			inWaitingRoom.remove(client);
	}

	public static void resetRoundDelay() {
		nextRoundDelay = LIMIT;
	}

	public static void tick() {

		CopyOnWriteArrayList<Player> inWaitingRoomCopy = new CopyOnWriteArrayList<Player>();
		inWaitingRoomCopy = inWaitingRoom;

		CopyOnWriteArrayList<Player> inFightPitCopy = new CopyOnWriteArrayList<Player>();
		inFightPitCopy = inFightPits;

		if (!Constants.MINIGAME)
			return;

		for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
			final Player player = PlayerManager.getSingleton().getPlayers()[i];
			if (player != null) {
				final Player client = player;
				if (client != null) {
					if (inFightArea(client))
						if (!inFightPits(client) && !client.isViewingOrb())
							addToFightPits(client);

					if (inWaitingArea(client))
						if (!inWaitingRoom(client))
							addToWaitingRoom(client);
					if (client.isViewingOrb())
						addToWaitingRoom(client);
				}
			}
		}

		for (final Player client : inWaitingRoomCopy)
			if (!inWaitingArea(client) && !client.isViewingOrb())
				inWaitingRoom.remove(client);

		for (final Player client : inFightPitCopy) {
			if (!matchActive)
				client.getPlayerTeleportHandler().forceTeleport(2399, 5169, 0);

			if (!inFightArea(client) && !client.isViewingOrb())
				removeFromFightPits(client);
		}

		if (inWaitingRoomCopy.size() == 0 && inFightPitCopy.size() == 0)
			return;

		for (final Player client : inWaitingRoomCopy) {
			if (previousWinner == "No one")
				client.getActionSender().sendString("Current Champion: No one",
						2805);
			else
				client.getActionSender().sendString(
						"Current Champion: JalYt-Ket-" + previousWinner, 2805);

			client.getActionSender().sendString(
					"@whi@Players Waiting: " + inWaitingRoomCopy.size(), 2806);

			client.getActionSender().sendFrame36(560, 1);
			client.getActionSender().sendWalkableInterface(2804);
		}

		for (final Player client : inFightPitCopy) {
			if (previousWinner == "No one")
				client.getActionSender().sendString("Current Champion: No one",
						2805);
			else
				client.getActionSender().sendString(
						"Current Champion: JalYt-Ket-" + previousWinner, 2805);

			client.getActionSender().sendString(
					"Foes: " + (inFightPitCopy.size() - 1), 2806);

			client.getActionSender().sendFrame36(560, 1);
			client.getActionSender().sendWalkableInterface(2804);
		}
		if (inFightPitCopy.size() == 1 && matchActive)
			for (final Player client : inFightPitCopy)
				if (matchActive) {
					matchActive = false;
					client.setBusyTimer(9);
					CombatEngine.resetAttack(client, true);
					previousWinner = client.getUsername();
					client.progress[70]++;
					if (client.progress[70] >= 5)
						Achievements.getInstance().complete(client, 70);
					else
						Achievements.getInstance().turnYellow(client, 70);
					AnimationProcessor.addNewRequest(client, 862, 4);
					client.getPlayerTeleportHandler().forceDelayTeleport(2399,
							5169, client.getHeightLevel(), 9);
					removeFromFightPits(client);
					break;
				}
		if (nextRoundDelay == 10)
			for (final Player client : inWaitingRoomCopy) {
				client.getActionSender()
						.sendMessage(
								"The match will begin in: 5 seconds, Prepare for battle!.");
				if (client.isViewingOrb()) {
					fightPitsOrb("Centre", 15239, client);
					client.setViewingOrb(false);
					client.getActionSender().sendWindowsRemoval();
					client.getActionSender().sendFrame99(0);
					client.getActionSender().sendSidebar(10, 2449);
					client.teleportToX = 2399;
					client.teleportToY = 5171;
					client.setHeightLevel(0);
					client.isNPC = false;
					client.updateRequired = true;
					client.appearanceUpdateRequired = true;
				}
			}
		if (nextRoundDelay == 0) {
			if (inWaitingRoomCopy.size() < 2) {

				for (final Player client : inWaitingRoomCopy) {

					if (client == null || client.isDead()
							|| client.disconnected)
						continue;

					client.getActionSender()
							.sendMessage(
									"You need atleast 2 players to start the match, The Match will begin in: 60 seconds.");
				}
				nextRoundDelay = LIMIT;
				return;
			}
			if (matchActive) {
				for (final Player client : inWaitingRoomCopy) {

					if (client == null || client.isDead()
							|| client.disconnected)
						continue;

					client.getActionSender()
							.sendMessage(
									"The current game is still running, The Match will begin in: 60 seconds.");
				}
				nextRoundDelay = LIMIT;
				return;
			}
			for (final Player client : inWaitingRoomCopy) {

				if (client == null || client.isDead() || client.disconnected)
					continue;

				if (inWaitingArea(client) && !client.isViewingOrb())
					client.getPlayerTeleportHandler().forceTeleport(
							2385 + Misc.random(20), 5151 + Misc.random(9), 0);

				client.setFreezeDelay(0);
				client.skullTimer = 0;
				Specials.deathEvent(client);

			}
			matchActive = true;

		}
		if (nextRoundDelay > 0)
			nextRoundDelay -= 1;
		else
			resetRoundDelay();

		inFightPitCopy.clear();
		inWaitingRoomCopy.clear();
		inFightPits.clear();
		inWaitingRoom.clear();

	}
}
