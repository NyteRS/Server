package com.server2.model.combat.additions;

import com.server2.InstanceDistributor;
import com.server2.content.Achievements;
import com.server2.content.TradingConstants;
import com.server2.content.constants.TotalValuable;
import com.server2.content.minigames.CastleWars;
import com.server2.content.minigames.DuelArena;
import com.server2.content.minigames.FightPits;
import com.server2.content.minigames.pc.PestControl;
import com.server2.content.quests.RecipeForDisaster;
import com.server2.content.skills.prayer.curses.Wrath;
import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.Item;
import com.server2.model.combat.CombatEngine;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Areas;
import com.server2.util.Misc;
import com.server2.world.PlayerManager;

/**
 * A player dies.
 * 
 * @author Rene
 */

public class DeathHandler {

	public static DeathHandler INSTANCE = new DeathHandler();

	public static DeathHandler getInstance() {
		return INSTANCE;
	}

	/**
	 * Handles player death.
	 * 
	 * @param client
	 */
	public void handleDeath(final Player client) {
		if (System.currentTimeMillis() - client.lastDeath < 5000)
			return;

		client.portalEnter = 0;

		if (Areas.isInDuelArena(client.getCoordinates())
				&& PlayerManager.getDuelOpponent(client) != null) {
			client.lastDeath = System.currentTimeMillis();
			PlayerManager.getDuelOpponent(client).lastDeath = System
					.currentTimeMillis();
			AnimationProcessor.addNewRequest(client, 2304, 0);
			final Player killer = client.getKiller();
			if (client.getWildernessLevel() > 0)
				if (killer != null) {
					killer.progress[69]++;
					if (killer.progress[69] >= 500)
						Achievements.getInstance().complete(killer, 69);
					else
						Achievements.getInstance().turnYellow(killer, 69);
				}
			client.getPlayerEventHandler().addEvent(new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					DuelArena.getInstance().onDeath(client);
					client.getActionSender().sendAnimationReset();
					client.hitpoints = client.calculateMaxHP();
					client.playerLevel[3] = client.calculateMaxHP();
					client.getActionAssistant().refreshSkill(
							PlayerConstants.HITPOINTS);
					container.stop();
				}

				@Override
				public void stop() {
					// TODO Auto-generated method stub

				}
			}, 3);
			return;
		}

		if (System.currentTimeMillis() - client.duelDeathPrevention < 3000) {
			client.hitpoints = client.calculateMaxHP();
			client.playerLevel[3] = client.calculateMaxHP();
			client.getActionAssistant().refreshSkill(PlayerConstants.HITPOINTS);
			return;
		}
		if (client.getDeadLock() > 0) {
			client.hitpoints = client.calculateMaxHP();
			client.playerLevel[3] = client.calculateMaxHP();
			client.getActionAssistant().refreshSkill(PlayerConstants.HITPOINTS);
			return;
		}

		client.progress[25]++;
		if (client.progress[25] >= 15)
			Achievements.getInstance().complete(client, 25);
		else
			Achievements.getInstance().turnYellow(client, 25);

		client.lastDeath = System.currentTimeMillis();
		client.lastDungEntry = System.currentTimeMillis();
		if (client.floor2()) {
			client.hitpoints = client.calculateMaxHP();
			client.playerLevel[3] = client.calculateMaxHP();
			client.getActionAssistant().refreshSkill(PlayerConstants.HITPOINTS);
			client.getActionSender().sendMessage(
					"The dungeoneering gods grant you new lifepoints!");
			client.teleportToX = client.getAbsX();
			client.teleportToY = client.getAbsY();
			client.updateRequired = true;
			return;
		}
		client.poisonDelay = -1;
		client.poisonDamage = -1;
		client.setBusy(true);
		client.setDead(true);
		client.killedAhrim = false;
		client.killedDharok = false;
		client.killedTorag = false;
		client.killedKaril = false;
		client.killedVerac = false;
		client.killedGuthan = false;
		client.barrowsKillCount = 0;
		client.setCanWalk(false);
		client.setDeadWaiting(true);
		client.stopMovement();
		AnimationProcessor.addNewRequest(client, 2304, 2);
		Wrath.appendWrath(client);
		CombatEngine.resetAttack(client, true);
		client.getActionSender().sendMessage("Oh dear, you died!");
		client.enteredGwdRoom = false;
		client.teleBlock = false;
		client.teleBlockTimer = -1;
		client.familiarTime = 0;
		if (client.floor1() || Areas.bossRoom1(client.getPosition())
				|| client.floor2() || client.floor3())
			client.deathCounter++;
		if (client.inRFD())
			RecipeForDisaster.getInstance().removeRfdNpcs(client,
					client.getHeightLevel());
		client.getActionSender().sendString(":quicks:off", -1);
		if (client.familiarId > 0)
			InstanceDistributor.getSummoning().dismiss(client);
		client.lastOverload = 3000000;
		client.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				handleRespawn(client);
				container.stop();
			}

			@Override
			public void stop() {
				// TODO Auto-generated method stub

			}
		}, 6);
	}

	/**
	 * Respawns the user, does various tasks, including sending back equipments
	 * etc.
	 * 
	 * @param client
	 */
	public void handleRespawn(Player client) {
		client.setInvulnerability(10);
		client.deathRespawnCounter = 0;
		final Player killer = client.getKiller();
		if (killer != null)
			if (client.inWilderness()) {
				if (client.connectedFrom.contains(killer.connectedFrom))
					killer.getActionSender()
							.sendMessage(
									"You didn't receive pk tokens because you're on the same IP-Adress.");
				else if (client.lastKillerName.equalsIgnoreCase(killer
						.getUsername()))
					killer.getActionSender()
							.sendMessage(
									"You didn't receive pk tokens because you've killed this person twice in a row.");
				else
					PkPoints.getInstance().addPKPoint(killer);
				KillStreaks.getInstance().addKillStreak(killer, client);
				killer.killCount++;
				if (killer.killCount == 20)
					killer.getActionSender().sendMessage(
							"@red@You've reached a killcount of "
									+ killer.killCount
									+ ", you now get 2 pk points per kill.");
				else if (killer.killCount == 50)
					killer.getActionSender().sendMessage(
							"@red@You've reached a killcount of "
									+ killer.killCount
									+ ", you now get 3 pk points per kill.");
				else if (killer.killCount == 75)
					killer.getActionSender().sendMessage(
							"@red@You've reached a killcount of "
									+ killer.killCount
									+ ", you now get 4 pk points per kill.");
				else if (killer.killCount == 150)
					killer.getActionSender().sendMessage(
							"@red@You've reached a killcount of "
									+ killer.killCount
									+ ", you now get 5 pk points per kill.");
				else if (killer.killCount == 300)
					killer.getActionSender().sendMessage(
							"@red@You've reached a killcount of "
									+ killer.killCount
									+ ", you now get 6 pk points per kill.");
				else if (killer.killCount == 500)
					killer.getActionSender().sendMessage(
							"@red@You've reached a killcount of "
									+ killer.killCount
									+ ", you now get 7 pk points per kill.");
				else if (killer.killCount == 800)
					killer.getActionSender().sendMessage(
							"@red@You've reached a killcount of "
									+ killer.killCount
									+ ", you now get 8 pk points per kill.");
				else if (killer.killCount == 1200)
					killer.getActionSender().sendMessage(
							"@red@You've reached a killcount of "
									+ killer.killCount
									+ ", you now get 9 pk points per kill.");
				else if (killer.killCount == 2000)
					killer.getActionSender().sendMessage(
							"@red@You've reached a killcount of "
									+ killer.killCount
									+ ", you now get 10 pk points per kill.");
				else if (killer.killCount == 10000)
					killer.getActionSender().sendMessage(
							"@red@You've reached a killcount of "
									+ killer.killCount
									+ ", you now get 20 pk points per kill.");

				client.lastKillerName = killer.getUsername();
				sendDeathMessage(client);
				client.deathCount++;
				client.killStreak = 0;
				client.logoutDelay = 0;
			}

		if (client.inPcGame())
			PestControl.getInstance().onDeath(client);
		else if (Areas.isInCastleWarsGame(client.getCoordinates()))
			CastleWars.getInstance().onDeath(client);
		else if (client.floor3()) {
			client.teleportToX = 3056;
			client.teleportToY = 4993;
			client.setHeightLevel(client.getIndex() * 4 + 1);
		} else if (FightPits.inFightArea(client)
				|| FightPits.inWaitingArea(client)) {
			client.teleportToX = 2399;
			client.teleportToY = 5175 - Misc.random(2);
		} else if (client.inFightCaves()) {
			client.teleportToX = 2439;
			client.teleportToY = 5168;
			InstanceDistributor.getTzhaarCave().onDeath(client);
			NPC.removeDungeoneeringNPC(client);
		} else if (client.floor1() || Areas.bossRoom1(client.getPosition())) {
			client.teleportToX = 3233;
			client.teleportToY = 9315;
			client.setHeightLevel(client.getIndex() * 4);
		} else if (client.floor2()) {
			client.teleportToX = 1879;
			client.teleportToY = 4620;
			client.setHeightLevel(client.getIndex() * 4);
		} else {

			client.teleportToX = client.getHomeAreaX();
			client.teleportToY = client.getHomeAreaY();
			if (!client.floor1() && !Areas.bossRoom1(client.getPosition())
					&& !client.floor2() && !client.floor3() /*
															 * &&
															 * client.getPrivileges
															 * () != 3
															 */
					&& !TradingConstants.isNoTradeUser(client.getUsername())) {
				client.itemKeptId = TotalValuable.mostValuable(client);
				ItemProtect.dropItems(client, true);
			}
		}
		client.setWildernessLevel(-1);
		client.setFreezeDelay(0);
		client.getEquipment().sendWeapon();
		client.setVeng(false);
		if (!client.floor1() && !client.floor2() && !client.floor3()
				&& !Areas.bossRoom1(client.getPosition()))
			client.setHeightLevel(0);
		client.skullTimer = 0;
		client.skulledOn = -1;
		client.getEquipment().setWeaponEmotes();
		client.hitpoints = client.calculateMaxHP();
		client.playerLevel[3] = client.calculateMaxHP();
		client.getPrayerHandler().resetAllPrayers();
		Specials.deathEvent(client);
		client.overLoaded = false;
		client.getActionAssistant().refreshSkill(3);
		if (client.tempNPCAllocated != null) {
			NPC.removeNPC(client.tempNPCAllocated, 25);
			client.tempNPCAllocated = null;
		}
		if (client.getKiller() != null)
			client.setKiller(null);
		for (int i = 0; i < PlayerConstants.MAX_SKILLS; i++)
			if (i != 3) {

				client.playerLevel[i] = client
						.getLevelForXP(client.playerXP[i]);
				client.getActionAssistant().refreshSkill(i);
			}
		client.setDead(false);
		client.setDeadWaiting(false);
		client.logoutDelay = 20;
		client.setBusy(false);
		client.setCanWalk(true);
		client.deadFinished = true;
		for (int i = 0; i < client.itemKeptId.length; i++)
			if (client.itemKeptId[i] > 0) {
				client.getActionSender().addItem(client.itemKeptId[i], 1);
				client.itemKeptId[i] = 0;
			}
		client.isFullHelm = Item
				.isFullHelm(client.playerEquipment[PlayerConstants.HELM]);
		client.isFullMask = Item
				.isFullMask(client.playerEquipment[PlayerConstants.HELM]);
		client.isFullBody = Item
				.isPlate(client.playerEquipment[PlayerConstants.CHEST]);
		client.setDeadTimer(0);
	}

	/**
	 * Sends a deathMessage
	 * 
	 * @param client
	 */
	public void sendDeathMessage(Player client) {
		final int randomMessage = Misc.random(5);
		final Player killer = client.getKiller();
		if (killer == null)
			return;
		if (randomMessage == 1)
			killer.getActionSender().sendMessage(
					"You are clearly a better warrior than "
							+ client.getUsername() + "!");
		else if (randomMessage == 2)
			killer.getActionSender().sendMessage(
					"You have defeated " + client.getUsername() + ".");
		else if (randomMessage == 3)
			killer.getActionSender().sendMessage(
					"You were clearly a better fighter than "
							+ client.getUsername() + ".");
		else if (randomMessage == 4)
			killer.sendMessage(client.getUsername()
					+ " falls before you might.");
		else if (randomMessage == 5)
			killer.getActionSender().sendMessage(
					client.getUsername() + " was no match for you.");
		else
			killer.getActionSender().sendMessage(
					"With a crushing blow you finish " + client.getUsername());
	}

}