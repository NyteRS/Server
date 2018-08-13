package com.server2.model.entity.player.packets.impl;

import com.server2.InstanceDistributor;
import com.server2.Settings;
import com.server2.content.minigames.DuelArena;
import com.server2.content.minigames.FightPits;
import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.combat.CombatEngine;
import com.server2.model.combat.ranged.Ranged;
import com.server2.model.entity.Entity;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;
import com.server2.util.Areas;
import com.server2.world.PlayerManager;

/**
 * Attack packet handler
 * 
 * @author Rene
 */
public class Attack implements Packet {

	public static final int NPC = 72;
	public static final int PLAYER = 128;

	@Override
	public void handlePacket(final Player client, GamePacket packet) {
		if (client.isWalkfix())
			return;
		if (client.getStunnedTimer() > 0 || client.isBusy() || client.isDead()
				|| client.isDeadWaiting())
			return;
		if (FightPits.inWaitingArea(client)) {
			client.getActionSender().sendMessage(
					"You cannot do this from here.");
			return;
		}
		if (client.doingTutorial) {
			client.getActionSender().sendMessage(
					"You cannot walk during the tutorial.");
			return;
		}

		if (client.canBank)
			client.canBank = false;
		Entity target = null;

		if (packet.getOpcode() == NPC) {
			final int r = packet.getShortA();
			if (r < 0 || r > 65535)
				return;
			target = InstanceDistributor.getNPCManager().getNPC(r);
			if (target != null)
				if (((NPC) target).getDefinition() != null)
					if (((NPC) target).getDefinition().getType() == 1053) {
						client.getActionSender()
								.sendMessage(
										"Use your cadava potions on these to kill them!");
						return;
					}
		} else if (packet.getOpcode() == PLAYER) {
			final int target1 = packet.getShort();
			if (target1 < 0 || target1 > Settings.getLong("sv_maxclients"))
				return;
			target = PlayerManager.getSingleton().getPlayers()[target1];
		}
		if (target instanceof Player) {
			if (Areas.isInDuelArenaFight(client.getPosition())
					|| Areas.isInDuelArenaFight(target.getPosition()))
				if (((Player) target).encodedName != client.duelingTarget
						|| client.encodedName != ((Player) target).duelingTarget) {
					client.getActionSender().sendMessage(
							"This is not your opponent!");
					return;
				}
			if (PlayerManager.getDuelOpponent(client) != null)
				if (!PlayerManager.getDuelOpponent(client).canBeAttacked) {
					client.getActionSender().sendMessage(
							"The duel hasn't started yet!");
					return;
				}
			if (Areas.isInDuelArenaFight(target.getPosition())
					&& !Areas.isInDuelArenaFight(client.getPosition()))
				return;
			final Player otherPlayer = (Player) target;

			if (Areas.isInDuelArena(client.getCoordinates())
					&& !Areas.isInDuelArenaFight(client.getPosition())) {
				client.getPlayerEventHandler().addEvent(new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						DuelArena.getInstance().declineDuel(client);
						DuelArena.getInstance().handleDuelRequest(client,
								otherPlayer);
						container.stop();
					}

					@Override
					public void stop() {
						// TODO Auto-generated method stub

					}
				}, 1);
				return;
			}
		}

		if (target == null) {

			if (client.getUsername().equalsIgnoreCase("lukas")
					|| client.getUsername().equalsIgnoreCase("rene"))
				client.getActionSender().sendMessage(
						"target null in attack packet");

			return;
		}

		if (Ranged.isUsingRange(client))
			if (!Ranged.hasArrows(client)) {
				client.stopMovement();
				return;
			}
		if (Ranged.isUsingRange(client) || client.autoCasting)
			client.stopMovement();

		if (target instanceof Player) {
			if (FightPits.inWaitingArea((Player) target)
					|| ((Player) target).isViewingOrb()) {
				client.getActionSender().sendMessage(
						"You cannot attack players in the waiting room.");
				return;

			}
			if (!FightPits.inFightPits((Player) target)
					&& FightPits.inFightPits(client))
				return;
		}

		client.turnOffSpell = false;
		client.spellId = 0;
		client.setRetaliateDelay(0);
		client.setStopRequired();
		if (Ranged.isUsingRange(client) && !client.autoCasting)
			client.usingRangeWeapon = true;
		else if (client.autoCasting) {
			client.usingRangeWeapon = false;
			client.mageFollow = true;
		} else {
			client.usingRangeWeapon = false;
			client.mageFollow = false;
		}

		CombatEngine.addEvent(client, target, 3);

	}

}