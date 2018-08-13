package com.server2.model.combat.additions;

import com.server2.InstanceDistributor;
import com.server2.content.minigames.FightPits;
import com.server2.model.entity.Entity;
import com.server2.model.entity.player.Language;
import com.server2.model.entity.player.Player;
import com.server2.net.GamePacketBuilder;
import com.server2.util.Areas;
import com.server2.world.PlayerManager;

/**
 * 
 * @author Rene Wilderness levels and attack options.
 * 
 */
public class Wilderness {

	public static boolean checkPlayers(Entity attacker, Entity victim) {

		if (attacker instanceof Player && victim instanceof Player) {

			final int lvldiff = Math.abs(((Player) attacker)
					.getCombatLevelNoSummoning()
					- ((Player) victim).getCombatLevelNoSummoning());

			if (Areas.isInCastleWarsGame(attacker.getCoordinates())
					|| Areas.isInDuelArenaFight(attacker.getCoordinates()))
				return true;

			final Player c = (Player) attacker;
			if (PlayerManager.getDuelOpponent(c) != null
					&& PlayerManager.getDuelOpponent(c) != victim) {
				c.getActionSender().sendMessage("This is not your apponent!");
				return false;
			}

			if (FightPits.inWaitingArea((Player) victim)
					|| ((Player) victim).isViewingOrb())
				return false;

			if (((Player) attacker).getWildernessLevel() == -1
					&& !Areas.isInDuelArena(c.getCoordinates())) {
				((Player) attacker).getActionSender().sendMessage(
						Language.NOT_IN_WILDY);
				return false;
			}
			if (Areas.isInDuelArena(c.getCoordinates())) {
				InstanceDistributor.getDuelArena().handleDuelRequest(c,
						(Player) victim);
				return false;
			}
			if (((Player) victim).getWildernessLevel() == -1) {
				((Player) attacker).getActionSender().sendMessage(
						Language.NOT_IN_WILDY_OTHER);
				return false;
			}
			if (!Areas.isInDuelArenaFight(attacker.getPosition())
					|| !Areas.isInDuelArenaFight(victim.getPosition()))
				if (lvldiff > ((Player) attacker).getWildernessLevel()
						|| lvldiff > ((Player) victim).getWildernessLevel()) {
					((Player) attacker).getActionSender().sendMessage(
							Language.WILDY_DIFFENCE);
					return false;
				}
		}
		return true;
	}

	public static void wildernessEvent(Entity entity) {
		if (entity == null)
			return;

		if (!(entity instanceof Player))
			return;
		final Player client = (Player) entity;
		if (client != null) {
			client.getActionSender().sendMultiCombatIcon(client.multiZone());

			if (Areas.isInCastleWarsLobby(client.getCoordinates())
					|| Areas.isInCastleWarsGame(client.getCoordinates())) {
				if (client.getCastleWarsTeam() != null
						&& Areas.isInCastleWarsGame(client.getCoordinates()))
					client.getActionSender().sendOption("Attack", 1);
				return;
			}
			if (client.inBountyHunterCombat()) {
				client.getActionSender().sendWalkableInterface(25347);
				client.getActionSender().sendOption("Attack", 1);
				return;
			}

			if (client.inPcBoat()) {
				client.getActionSender().sendWalkableInterface(21119);
				return;
			}

			if (client.inPcGame()) {
				client.getActionSender().sendWalkableInterface(21100);
				return;
			}
			if (client.inBarrows()) {
				client.getActionSender().sendFrame99(2);
				client.getActionSender().sendString(
						"Kill Count: " + client.barrowsKillCount, 4536);
				client.getActionSender().sendWalkableInterface(4535);
				return;
			} else
				client.getActionSender().sendFrame99(0);
			if (Areas.isInDuelArena(client.getCoordinates())) {
				String toSend = "Challenge";
				if (Areas.isInDuelArenaFight(client.getPosition()))
					toSend = "Attack";
				client.getActionSender().sendOption(toSend, 1);
				client.getActionSender().sendWalkableInterface(201);
				return;
			}
			if (client.gwdCoords()) {
				client.getActionSender().sendWalkableInterface(16210);
				client.getActionSender().sendString(
						Integer.toString(client.armaKc), 16216);
				client.getActionSender().sendString(
						Integer.toString(client.bandosKc), 16217);
				client.getActionSender().sendString(
						Integer.toString(client.saradominKc), 16218);
				client.getActionSender().sendString(
						Integer.toString(client.zamorakKc), 16219);
				return;
			}
			if (client.inWilderness()) {
				if (client.getAbsX() >= 2615 && client.getAbsX() <= 2760
						&& client.getAbsY() >= 5050 && client.getAbsY() <= 5125)
					client.setWildernessLevel(35);
				else {

					int wildernessaLevelToReturn = (client.getAbsY() - 3520) / 8;
					if (wildernessaLevelToReturn < 8)
						wildernessaLevelToReturn = 8;
					client.setWildernessLevel(wildernessaLevelToReturn);
				}
				client.wildSignWarning = true;

			} else
				client.setWildernessLevel(-1);
			if (client.getWildernessLevel() != -1
					&& !InstanceDistributor.getTzhaarCave().inArea(client)
					&& !FightPits.inFightArea(client)
					&& !FightPits.inWaitingArea(client)) {
				final GamePacketBuilder bldr = new GamePacketBuilder(208);
				bldr.putLEShort(197);
				client.write(bldr.toPacket());
				//
				client.getActionSender().sendString(
						"Level: " + client.getWildernessLevel(), 199);
				client.getActionSender().sendOption("Attack", 1);

			} else if (FightPits.inFightArea(client)) {
				client.setWildernessLevel(126);
				client.getActionSender().sendString("", 199);
				client.getActionSender().sendOption("@red@Attack", 1);
			} else if (FightPits.inWaitingArea(client)) {
				client.setWildernessLevel(126);
				client.getActionSender().sendString("", 199);
				client.getActionSender().sendOption("null", 1);
			} else {
				final GamePacketBuilder bldr = new GamePacketBuilder(208);
				bldr.putLEShort(-1);
				client.getActionSender().sendOption("null", 1);
				client.write(bldr.toPacket());
				client.setWildernessLevel(-1);
			}
		}
	}

}
