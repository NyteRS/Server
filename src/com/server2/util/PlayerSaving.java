package com.server2.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import com.server2.Constants;
import com.server2.Settings;
import com.server2.content.skills.farming.FarmingSave;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene Roosen & Lukas
 * 
 */
public class PlayerSaving {
	private static void saveExtraData(final Player client) {
		saveExtraStrings(client);
		try {
			final File file = new File(
					Constants.CHARACTER_EXTRA_DATA_1_DIRECTORY
							+ client.getUsername() + ".dat");

			if (!file.exists())
				file.createNewFile();

			final FileOutputStream outFile = new FileOutputStream(file);
			final DataOutputStream write = new DataOutputStream(outFile);
			write.writeInt(client.numEffigies);
			write.writeInt(client.effigyType);
			write.writeInt(client.getHalloweenEvent().questStage);
			write.writeInt(client.getHalloweenEvent().soulsCollected);
			write.writeInt(client.musicEnabled);
			write.writeInt(client.getGertrudesQuest().stage);
			write.writeInt(client.duoPoints);
			write.writeInt(client.dwarfStage);
			write.writeInt(client.splitPrivateChat);
			write.writeInt(client.wealthStage);
			write.writeInt(client.xmasStage);
			write.writeInt(client.ratStage);
			write.writeInt(client.puppetStage);
			write.writeInt(client.gnomeStage);
			write.writeInt(client.presents);
			write.writeInt(client.totalKilled);
			write.writeInt(client.bossesKilled);
			write.writeInt(client.tasksCompleted);
			write.writeInt(client.daggsKilled);
			write.writeInt(client.demonsKilled);
			write.writeInt(client.nexKilled);
			write.writeInt(client.nomadKilled);
			write.writeInt(client.corpKilled);
			write.writeInt(client.chaosKilled);
			write.writeInt(client.barrelKilled);
			write.writeInt(client.avatarKilled);
			write.writeInt(client.glacorKilled);
			write.writeInt(client.frostsKilled);
			write.writeInt(client.godwarKilled);
			write.writeInt(client.jadKilled);
			write.writeInt(client.mithKilled);
			write.writeLong(client.getVault().stored);
			write.flush();
			write.close();

		} catch (final Exception e) {
			e.printStackTrace();
			// write.close();
		}

	}

	private static void saveExtraStrings(final Player client) {
		try {
			final File file = new File(
					Constants.CHARACTER_EXTRA_DATA_2_DIRECTORY
							+ client.getUsername() + ".dat");

			if (!file.exists())
				file.createNewFile();

			final FileOutputStream outFile = new FileOutputStream(file);
			final DataOutputStream write = new DataOutputStream(outFile);
			write.writeUTF(client.ipString);

			write.writeBoolean(client.setPass);

			write.writeLong(client.muteTimer);

			write.flush();
			write.close();

		} catch (final Exception e) {
			e.printStackTrace();
			// write.close();
		}

	}

	private static void saveLoginCredentials(Player client) {
		try {
			final File file = new File(Constants.CREDENTIAL_DIRECTORY
					+ client.getUsername() + ".dat");

			if (!file.exists())
				file.createNewFile();

			final FileOutputStream outFile = new FileOutputStream(file);
			final DataOutputStream write = new DataOutputStream(outFile);
			write.writeUTF(client.getUsername());
			write.writeUTF(client.getPassword());
			write.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	public static boolean savePlayer(Player player) {
		if (!player.fullyInitialized
				|| player.lastSave + Settings.getLong("sv_cyclerate") > System
						.currentTimeMillis())
			return false;
		if (player.tradeStage != 0)
			return false;

		try {
			final File file = new File(Constants.CHARACTER_DATA_DIRECTORY
					+ player.getUsername() + ".dat");

			if (!file.exists())
				file.createNewFile();

			final FileOutputStream outFile = new FileOutputStream(file);
			final DataOutputStream write = new DataOutputStream(outFile);
			write.writeInt(player.getAbsX());
			write.writeInt(player.getAbsY());
			write.writeInt(player.getHeightLevel());
			write.writeInt(player.getPrivileges());
			write.writeInt(player.getKillStreak());
			write.writeInt(player.hitpoints);
			write.writeInt(player.slayerPoints);
			write.writeInt(player.specialAmount);
			write.writeInt(player.poisonDelay);
			write.writeInt(player.augury);
			write.writeInt(player.renewal);
			write.writeInt(player.rigour);
			write.writeInt(player.poisonDamage);
			write.writeInt(player.skullTimer);
			write.writeInt(player.pkPoints);
			write.writeInt(player.energy);
			write.writeInt(player.spellBook);
			write.writeBoolean(player.appearanceSet);
			write.writeBoolean(player.SplitChat);
			write.writeBoolean(player.isRunning);
			write.writeBoolean(player.autoRetaliate);
			write.writeInt(player.combatMode);
			write.writeInt(player.lastMeleeMode);
			write.writeInt(player.lastRangeMode);
			write.writeInt(player.starter);
			write.writeInt(player.slayerTask);
			write.writeInt(player.slayerTaskAmount);
			write.writeInt(player.slayerMaster);
			write.writeInt(player.loyaltyRank);
			write.writeInt(player.prayerBook);
			write.writeInt(player.familiarId);
			write.writeInt(player.musicLevel);
			write.writeInt(player.last1);
			write.writeInt(player.last2);
			write.writeInt(player.teleBlock ? 1 : 0);
			write.writeLong(player.teleBlockTimer);
			write.writeLong(player.gameTime);
			write.writeInt(player.succesfullCompletedTasks);
			write.writeInt(player.pouchUsed);
			write.writeInt(player.foodIdStored);
			write.writeInt(player.foodAmountStored);
			write.writeInt(player.rfdProgress);
			write.writeDouble(player.familiarTime);
			write.writeInt(player.votePoints);
			write.writeDouble(player.killCount);
			write.writeDouble(player.deathCount);
			write.writeInt(player.bound1);
			write.writeInt(player.bound2);
			write.writeInt(player.bound3);
			write.writeInt(player.bound4);
			write.writeInt(player.hftdStage);
			write.writeInt(player.dungTokens);
			write.writeInt(player.expLocked);
			write.writeInt(player.hasBeenReset);
			write.writeInt(player.donatorRights);
			write.writeInt(player.gamblingRights);
			write.writeInt(player.privateChatMode);
			write.writeInt(player.earningPotential);
			write.writeUTF(player.lastKillerName);
			write.writeInt(player.getPestControlCommedations());

			for (final int element : player.playerLevel)
				write.writeInt(element);
			for (final int element : player.playerXP)
				write.writeInt(element);
			for (final int playerItem : player.playerItems)
				write.writeInt(playerItem);
			for (final int element : player.playerItemsN)
				write.writeInt(element);
			for (final int element : player.playerEquipment)
				write.writeInt(element);
			for (final int element : player.playerEquipmentN)
				write.writeInt(element);
			for (final int element : player.playerLook)
				write.writeInt(element);
			for (int i = 0; i < player.getFriends().length; i++)
				write.writeLong(player.getFriends()[i]);
			for (int i = 0; i < player.getIgnores().length; i++)
				write.writeLong(player.getIgnores()[i]);
			for (final int bankItem : player.bankItems)
				write.writeInt(bankItem);
			for (final int element : player.bankItemsN)
				write.writeInt(element);
			write.flush();
			write.close();
			player.lastSave = System.currentTimeMillis();
			FarmingSave.saveFarmingData(player);
			saveExtraData(player);
			saveExtraStrings(player);
			saveLoginCredentials(player);

			return true;
		} catch (final Exception e) {
			e.printStackTrace();
			// write.close();
			return false;
		}

	}

}
