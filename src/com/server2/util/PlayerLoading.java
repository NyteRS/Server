package com.server2.util;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import com.server2.Constants;
import com.server2.content.skills.farming.FarmingLoad;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene Roosen & Lukas
 * 
 */
public class PlayerLoading {

	private static void loadExtraData(final Player client) {

		File file = new File(Constants.CHARACTER_EXTRA_DATA_1_DIRECTORY
				+ client.getUsername() + ".dat");
		if (!file.exists())
			return;
		try {
			final FileInputStream inFile = new FileInputStream(file);
			final DataInputStream load = new DataInputStream(inFile);
			client.numEffigies = load.readInt();
			client.effigyType = load.readInt();
			client.getHalloweenEvent().questStage = load.readInt();
			client.getHalloweenEvent().soulsCollected = load.readInt();
			client.musicEnabled = load.readInt();
			client.getGertrudesQuest().stage = load.readInt();
			client.duoPoints = load.readInt();
			client.dwarfStage = load.readInt();
			client.splitPrivateChat = load.readInt();
			client.wealthStage = load.readInt();
			client.xmasStage = load.readInt();
			client.ratStage = load.readInt();
			client.puppetStage = load.readInt();
			client.gnomeStage = load.readInt();
			client.presents = load.readInt();
			client.totalKilled = load.readInt();
			client.bossesKilled = load.readInt();
			client.tasksCompleted = load.readInt();
			client.daggsKilled = load.readInt();
			client.demonsKilled = load.readInt();
			client.nexKilled = load.readInt();
			client.nomadKilled = load.readInt();
			client.corpKilled = load.readInt();
			client.chaosKilled = load.readInt();
			client.barrelKilled = load.readInt();
			client.avatarKilled = load.readInt();
			client.glacorKilled = load.readInt();
			client.frostsKilled = load.readInt();
			client.godwarKilled = load.readInt();
			client.jadKilled = load.readInt();
			client.mithKilled = load.readInt();
			client.getVault().stored = load.readLong();
			load.close();

		} catch (final Exception e) {
			// e.printStackTrace();

		}

		file = new File(Constants.CHARACTER_EXTRA_DATA_2_DIRECTORY
				+ client.getUsername() + ".dat");
		if (!file.exists())
			return;

		try {
			final FileInputStream inFile = new FileInputStream(file);
			final DataInputStream load = new DataInputStream(inFile);
			client.ipString = load.readUTF();
			for (int i = 0; i < 14; i++)
				load.readUTF();
			for (int i = 0; i < 15; i++)
				load.readDouble();
			client.setPass = load.readBoolean();

			for (int i = 0; i < 14; i++)
				load.readBoolean();
			client.muteTimer = load.readLong();

			load.close();

		} catch (final Exception e) {
			// e.printStackTrace();

		}
	}

	/**
	 * Loads a player
	 * 
	 * @param player
	 * @return
	 */
	public static boolean loadPlayer(final Player player) {
		final File file = new File(Constants.CHARACTER_DATA_DIRECTORY
				+ player.getUsername() + ".dat");
		if (!file.exists()) {
			player.fullyInitialized = true;
			player.setPass = true;
			player.startLogin();
			player.ipString = player.connectedFrom + ", ";
			return false;// run that// u
		}
		try { // why u closing it? i
			final FileInputStream inFile = new FileInputStream(file);
			final DataInputStream load = new DataInputStream(inFile);
			player.teleportToX = load.readInt();
			player.teleportToY = load.readInt();
			player.heightLevel2Baws = load.readInt();
			player.setPrivileges(load.readInt());
			player.setKillStreak(load.readInt());
			player.hitpoints = load.readInt();
			player.slayerPoints = load.readInt();
			player.setSpecialAmount(load.readInt());
			player.poisonDelay = load.readInt();
			player.augury = load.readInt();
			player.renewal = load.readInt();
			player.rigour = load.readInt();
			player.poisonDamage = load.readInt();
			player.skullTimer = load.readInt();
			player.pkPoints = load.readInt();
			player.energy = load.readInt();
			player.spellBook = load.readInt();
			player.appearanceSet = load.readBoolean();
			player.SplitChat = load.readBoolean();
			player.isRunning = load.readBoolean();
			player.autoRetaliate = load.readBoolean();
			player.combatMode = load.readInt();
			player.lastMeleeMode = load.readInt();
			player.lastRangeMode = load.readInt();
			player.starter = load.readInt();
			player.slayerTask = load.readInt();
			player.slayerTaskAmount = load.readInt();
			player.slayerMaster = load.readInt();
			player.loyaltyRank = load.readInt();
			player.prayerBook = load.readInt();
			player.familiarId = load.readInt();
			player.musicLevel = load.readInt();
			player.last1 = load.readInt();
			player.last2 = load.readInt();
			player.teleBlock = load.readInt() == 1 ? true : false;
			player.teleBlockTimer = load.readLong();
			player.gameTime = load.readLong();
			player.succesfullCompletedTasks = load.readInt();
			player.pouchUsed = load.readInt();
			player.foodIdStored = load.readInt();
			player.foodAmountStored = load.readInt();
			player.rfdProgress = load.readInt();
			player.familiarTime = load.readDouble();
			player.votePoints = load.readInt();
			player.killCount = load.readDouble();
			player.deathCount = load.readDouble();
			player.bound1 = load.readInt();
			player.bound2 = load.readInt();
			player.bound3 = load.readInt();
			player.bound4 = load.readInt();
			player.hftdStage = load.readInt();
			player.dungTokens = load.readInt();
			player.expLocked = load.readInt();
			player.hasBeenReset = load.readInt();
			player.donatorRights = load.readInt();
			player.gamblingRights = load.readInt();
			player.privateChatMode = load.readInt();
			player.earningPotential = load.readInt();
			player.lastKillerName = load.readUTF();
			player.setPestControlCommedations(load.readInt());
			for (int i = 0; i < player.playerLevel.length; i++)
				player.playerLevel[i] = load.readInt();
			for (int i = 0; i < player.playerXP.length; i++)
				player.playerXP[i] = load.readInt();
			for (int i = 0; i < player.playerItems.length; i++)
				player.playerItems[i] = load.readInt();
			for (int i = 0; i < player.playerItemsN.length; i++)
				player.playerItemsN[i] = load.readInt();
			for (int i = 0; i < player.playerEquipment.length; i++)
				player.playerEquipment[i] = load.readInt();
			for (int i = 0; i < player.playerEquipmentN.length; i++)
				player.playerEquipmentN[i] = load.readInt();
			for (int i = 0; i < player.playerLook.length; i++)
				player.playerLook[i] = load.readInt();
			for (int i = 0; i < player.getFriends().length; i++)
				player.friends[i] = load.readLong();
			for (int i = 0; i < player.getIgnores().length; i++)
				player.ignores[i] = load.readLong();
			for (int i = 0; i < player.bankItems.length; i++)
				player.bankItems[i] = load.readInt();
			for (int i = 0; i < player.bankItemsN.length; i++)
				player.bankItemsN[i] = load.readInt();
			load.close();
			player.getSecurityDetails().loadDetails();
			player.fullyInitialized = true;
			FarmingLoad.loadFarmingData(player);
			loadExtraData(player);
			player.teleBlock = false;
			player.teleBlockTimer = 0;
			player.startLogin();
			// Load finished, sign player in

			return true;
		} catch (final Exception e) {
			System.out.println("There was an exception while loading "
					+ player.getUsername() + " please investigate.");
			Logger.write(
					player.getUsername() + " is corrupted, please delete.",
					"CorruptedAccounts");
			player.startLogin();
			return true;
		}
	}

	/**
	 * Loads the login details for a player
	 * 
	 * @param player
	 */
	public static void loadPlayerCredentials(Player player) {
		try {
			final File file = new File(Constants.CREDENTIAL_DIRECTORY
					+ player.getUsername() + ".dat");
			final FileInputStream inFile = new FileInputStream(file);
			final DataInputStream load = new DataInputStream(inFile);
			player.setUsername(load.readUTF());
			player.setPassword(load.readUTF());
			load.close();
		} catch (final Exception e) {
			// e.printStackTrace();
		}
	}

	public static void log(String ip, String name) {

		final Date date = new Date();
		final String data = "[" + date + "]  ip: " + ip;

		final File file = new File("./data/logs/ips/" + name + ".txt");
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new FileWriter(file, true));
			bw.write(data);
			bw.newLine();
			bw.newLine();
			bw.flush();
		} catch (final IOException ioe) {

		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (final IOException ioe2) {

				}
		}

	}

}
