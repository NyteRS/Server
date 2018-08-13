package com.server2.model.entity.player.packets.impl;

import com.server2.content.minigames.DuelArena;
import com.server2.model.combat.CombatEngine;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Location;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;
import com.server2.util.Areas;
import com.server2.world.PlayerManager;
import com.server2.world.map.tile.FollowEngine;

/**
 * Walking
 * 
 * @author Rene
 */
public class Walk implements Packet {

	public static final int minimap = 248, map = 164, playerClick = 98;

	@Override
	public void handlePacket(Player client, GamePacket packet) {
		if (client == null)
			return;
		if (client.isWalkfix())
			return;
		client.setWalkfix(true); // This is necessary; Players can teleport with
									// this exploit and server2 is the ONLY
									// private server that's vulnerable like
									// runescape.
		// Luckily I'm the only one (in the world probably) who knows the
		// exploit

		if (client.isDead() || client.isDeadWaiting()) {
			client.setWalkfix(false);
			return;
		}
		if (System.currentTimeMillis() - client.duelStart < 1000) {
			client.setWalkfix(false);
			return;
		}
		if (client.canBank)
			client.canBank = false;
		if (client.nDialogue > 0)
			client.nDialogue = -1;
		if (client.hftdUpdateRequired)
			client.hftdUpdateRequired = false;
		if (client.openedFarmingStore) {
			client.openedFarmingStore = false;
			client.getActionSender().sendSidebar(3, 3213);
		}
		if (client.dialogueAction > 0)
			client.dialogueAction = 0;
		if (PlayerManager.getDuelOpponent(client) != null
				&& client.getDuelRules()[1]) {
			client.getActionSender().sendMessage(
					"You're not allowed to move in this duel.");
			client.setWalkfix(false);
			return;
		}
		if (client.isBeingForcedToWalk()) {
			client.setWalkfix(false);
			return;
		}
		Entity ent = null;

		ent = client;

		if (ent != null)
			if (ent.isBusy()) {
				if (client.floor1() || client.floor2() || client.floor3()
						|| Areas.bossRoom1(client.getPosition()))
					client.getActionSender().sendMessage(
							"Your floor will start shortly!");
				client.setWalkfix(false);
				return;
			}
		int packetSize = packet.getLength();
		if (client.doingTutorial) {
			client.getActionSender().sendMessage(
					"You cannot walk during the tutorial.");
			client.setWalkfix(false);
			return;
		}
		if (ent.getFreezeDelay() > 0) {
			client.getActionSender().sendMessage(
					"A magical force holds you from moving!");
			client.setWalkfix(false);
			return;
		}
		if (packet.getOpcode() == minimap)
			packetSize -= 14;
		if (packet.getOpcode() == map || packet.getOpcode() == minimap) {
			for (int pickupReset = 0; pickupReset < client.pickup.length; pickupReset++)
				client.pickup[pickupReset] = -1;
			if (client.getUtility() != null)
				client.getUtility().setUsingUtility(false);
			if (client.isSkilling())
				client.setSkilling(false);
			FollowEngine.resetFollowing(client);
			client.resetFaceDirection();
			CombatEngine.resetAttack(client, false);
		}
		DuelArena.getInstance().resetOnWalk(client);
		client.setStopRequired();
		client.cancelTasks();
		final int steps = (packetSize - 5) / 2;
		if (steps < 0) {
			client.setWalkfix(false);
			return;
		}
		final int firstStepX = packet.getLEShortA();
		final int[][] path = new int[steps][2];
		for (int i = 0; i < steps; i++) {
			path[i][0] = packet.get();
			path[i][1] = packet.get();
		}
		final int firstStepY = packet.getLEShort();
		final boolean runSteps = packet.getByteC() == 1;
		client.getWalkingQueue().setRunningQueue(runSteps);
		final Location[] positions = new Location[steps + 1];
		positions[0] = new Location(firstStepX, firstStepY, client
				.getPosition().getZ());
		for (int i = 0; i < steps; i++)
			positions[i + 1] = new Location(path[i][0] + firstStepX, path[i][1]
					+ firstStepY, client.getPosition().getZ());
		if (client.getWalkingQueue().addFirstStep(positions[0]))
			for (int i = 1; i < positions.length; i++)
				client.getWalkingQueue().addStep(positions[i], true);
		client.setWalkfix(false); // Now it's safe for them to trigger another
									// packet.
	}
}