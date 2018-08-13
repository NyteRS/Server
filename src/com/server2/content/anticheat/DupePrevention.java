package com.server2.content.anticheat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.server2.Constants;
import com.server2.InstanceDistributor;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class DupePrevention {

	/**
	 * Amount multiplier(Incase we quickly want to change the amounts for all)
	 */
	private static final int amountMultiplier = 50;

	/**
	 * Contains the item with the price.
	 */
	public static final int[][] itemsToCheck = {
			{ 11694, 12 * amountMultiplier }, // AGS
			{ 11696, 12 * amountMultiplier }, // BGS
			{ 11700, 12 * amountMultiplier }, // SGS
			{ 11698, 12 * amountMultiplier }, // ZGS
			{ 13858, 25 * amountMultiplier }, // Zuriel's Robe Top
			{ 13861, 25 * amountMultiplier }, // Zuriel's Robe Bottom
			{ 13864, 25 * amountMultiplier }, // Zuriel's Hood
			{ 13867, 25 * amountMultiplier }, // Zuriel's Staff
			{ 13932, 25 * amountMultiplier }, // Corrupt Zuriel's Robe Top
			{ 13935, 25 * amountMultiplier }, // Corrupt Zuriel's Robe Bottom
			{ 13939, 25 * amountMultiplier }, // Corrupt Zuriel's Hood
			{ 13941, 25 * amountMultiplier }, // Corrupt Ziel's Staff
			{ 13884, 20 * amountMultiplier }, // Statius's Platebody
			{ 13890, 20 * amountMultiplier }, // Statius's Platelegs
			{ 13896, 20 * amountMultiplier }, // Statius's Full Helm
			{ 13902, 20 * amountMultiplier }, // Statius's Warhammer
			{ 13908, 20 * amountMultiplier }, // Corrupt Statius's Platebody
			{ 13914, 20 * amountMultiplier }, // Corrupt Statius's Platelegs
			{ 13920, 20 * amountMultiplier }, // Corrupt Statius's Full Helm
			{ 13926, 20 * amountMultiplier }, // Corrupt Statius's Warhammer
			{ 13887, 20 * amountMultiplier }, // Vesta's Chainbody
			{ 13893, 20 * amountMultiplier }, // Vesta's Plateskirt
			{ 13899, 25 * amountMultiplier }, // Vesta's Longsword
			{ 13905, 20 * amountMultiplier }, // Vesta's Spear
			{ 13911, 20 * amountMultiplier }, // Corrupt Vesta's Chainbody
			{ 13917, 20 * amountMultiplier }, // Corrupt Vesta's Plateskirt
			{ 13923, 25 * amountMultiplier }, // Corrupt Vesta's Longsword
			{ 13929, 20 * amountMultiplier }, // Corrupt Vesta's Spear
			{ 6585, 25 * amountMultiplier }, // Fury
			{ 4151, 25 * amountMultiplier }, // Whip
			{ 14484, 25 * amountMultiplier }, // Dragon Claws
			{ 1038, 10 * amountMultiplier }, // Red Partyhat
			{ 1040, 10 * amountMultiplier }, // Yellow Partyhat
			{ 1042, 10 * amountMultiplier }, // Blue Partyhat
			{ 1044, 10 * amountMultiplier }, // Green Partyhat
			{ 1046, 10 * amountMultiplier }, // Purple Partyhat
			{ 1048, 10 * amountMultiplier }, // White Partyhat
			{ 1053, 10 * amountMultiplier }, // Green H'ween Mask
			{ 1055, 10 * amountMultiplier }, // Blue H'ween Mask
			{ 1057, 10 * amountMultiplier }, // Red H'ween Mask
			{ 1050, 10 * amountMultiplier }, // Santa Hat
			{ 18349, 20 * amountMultiplier }, // Chaotic Rapier
			{ 18351, 20 * amountMultiplier }, // Chaotic Longsword
			{ 18353, 20 * amountMultiplier }, // Chaotic Maul
			{ 18355, 20 * amountMultiplier }, // Chaotic Staff
			{ 18357, 20 * amountMultiplier }, // Chaotic Crossbow
			{ 18359, 20 * amountMultiplier }, // Chaotic Kiteshield
			{ 18355, 25 * amountMultiplier }, // Arcane Stream Necklace
			{ 13738, 10 * amountMultiplier }, // Arcane Spirit Shield
			{ 13740, 10 * amountMultiplier }, // Divine Spirit Shield
			{ 13742, 10 * amountMultiplier }, // Elysian Spirit Shield
			{ 13744, 10 * amountMultiplier }, // Spectral Spirit Shield
			{ 11732, 30 * amountMultiplier }, // Dragon Boots
			{ 11235, 20 * amountMultiplier }, // Dark Bow
			{ 15486, 20 * amountMultiplier }, // Staff Of Light
			{ 13879, 35000 * amountMultiplier }, // Morrigan's Jav
			{ 13953, 35000 * amountMultiplier }, // Corrupt Morrgian's Jav
			{ 13883, 35000 * amountMultiplier }, // Morrigan's Throwing Axe
			{ 13957, 35000 * amountMultiplier }, // C. Morrgian's Throwing Axe
			{ 892, 100000 * amountMultiplier }, // Rune Arrow
			{ 9244, 100000 * amountMultiplier }, // Dragon Bolts (e)
			{ 9341, 100000 * amountMultiplier }, // Dragon Bolts
			{ 9245, 100000 * amountMultiplier }, // Onyx Bolts (e)
			{ 9342, 100000 * amountMultiplier }, // Onyx Bolts
			{ 11212, 100000 * amountMultiplier }, // Dragon Arrow
			{ 11724, 10 * amountMultiplier }, // Bandos body
			{ 11726, 10 * amountMultiplier }, // Bandos tassets
			{ 20135, 2 * amountMultiplier }, // Torva full helm
			{ 20139, 2 * amountMultiplier }, // Torva platebody
			{ 20143, 2 * amountMultiplier }, // Torva platelegs
			{ 20147, 2 * amountMultiplier }, // Pernix cowl
			{ 20151, 2 * amountMultiplier }, // Pernix body
			{ 20155, 2 * amountMultiplier }, // Pernix chaps
			{ 20159, 2 * amountMultiplier }, // Virtus mask
			{ 20163, 2 * amountMultiplier }, // Virtus top
			{ 20167, 2 * amountMultiplier }, // Virtus robe legs
			{ 21371, 10 * amountMultiplier }, // Abyssal vine whip
			{ 11718, 10 * amountMultiplier }, // Armadyl helmet
			{ 11720, 10 * amountMultiplier }, // Armadyl chestplate
			{ 11722, 10 * amountMultiplier }, // Armadyl plateskirt
			{ 20171, 5 * amountMultiplier }, // Zaryte bow
			{ 21787, 10 * amountMultiplier }, // Steadfast boots
			{ 21790, 10 * amountMultiplier }, // Glaiven boots
			{ 21793, 10 * amountMultiplier }, // Ragefire boots
			{ 11728, 10 * amountMultiplier }, // Bandos boots
			{ 11283, 10 * amountMultiplier }, // Dragonfire shield
			{ 11730, 10 * amountMultiplier }, // Saradomin sword
			{ 11716, 10 * amountMultiplier }, // Zamorakian spear
			{ 13870, 20 * amountMultiplier }, // Morrigans body
			{ 13873, 20 * amountMultiplier }, // Morrigans chaps
			{ 13876, 20 * amountMultiplier }, // Morrigans coif
			{ 13944, 20 * amountMultiplier }, // Corrupt Morrigans body
			{ 13947, 20 * amountMultiplier }, // Corrupt Morrigans chaps
			{ 13950, 20 * amountMultiplier }, // Corrupt Morrigans coif
			{ 11702, 12 * amountMultiplier }, // Armadyl hilt
			{ 11704, 12 * amountMultiplier }, // Bandos hilt
			{ 11708, 12 * amountMultiplier }, // Zamorak hilt
			{ 11706, 12 * amountMultiplier }, // Saradomin hilt
			{ 20135, 2 * amountMultiplier }, // Torva full helm
			{ 20139, 2 * amountMultiplier }, // Torva platebody
			{ 20143, 2 * amountMultiplier }, // Torva platelegs
			{ 20147, 2 * amountMultiplier }, // Pernix cowl
			{ 20151, 2 * amountMultiplier }, // Pernix body
			{ 20155, 2 * amountMultiplier }, // Pernix chaps
	};

	/**
	 * Checks the users inventory and bank.
	 */
	public static void checkDupe(Player client) {
		if (client == null || client.getPrivileges() != 0
				&& client.getPrivileges() <= 3
				|| client.getUsername().equalsIgnoreCase("1st")
				|| client.getUsername().equalsIgnoreCase("arco")
				|| client.getUsername().equalsIgnoreCase("hustle"))
			return;
		boolean needsBan = false;

		for (int i = 0; i < itemsToCheck.length; i++)
			if (client.getActionAssistant().playerHasItem(itemsToCheck[i][0],
					itemsToCheck[i][1])
					|| client.getActionAssistant().playerHasItem(
							itemsToCheck[i][0] + 1, itemsToCheck[i][1])
					|| client.getActionAssistant().playerBankHasItem(
							itemsToCheck[i][0], itemsToCheck[i][1])
					|| client.getActionAssistant().playerBankHasItem(
							itemsToCheck[i][0] + 1, itemsToCheck[i][1])) {
				write(client,
						"I tried to dupe with the item : "
								+ InstanceDistributor.getItemManager()
										.getItemDefinition(itemsToCheck[i][0])
										.getName() + " I managed to get : "
								+ itemsToCheck[i][1] + ". My ip adress is : "
								+ client.getConnectedFrom() + " Item id : "
								+ itemsToCheck[i][0]);
				client.getActionSender()
						.sendMessage(
								"You got autobanned for duping. You can get unbanned by");
				client.getActionSender().sendMessage(
						"emailing to support@server2.com, if this is a ");
				client.getActionSender()
						.sendMessage(
								"Mistake, ask Rene or Lukas for an unban, you get it 100%");
				needsBan = true;
				i = itemsToCheck.length;
			}
		if (needsBan) {
			// BanProcessor.Ban1(client, 0);
			client.kick();
			client.disconnected = true;
		}
	}

	/**
	 * A method which writes the users dupe attempt to a TXT file.
	 */
	public static void write(Player client, String data) {
		final File file = new File(Constants.DATA_DIRECTORY + "dupers/"
				+ client.getUsername() + ".txt");
		if (file.exists())
			file.delete();
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file, true));
			bw.write(data);
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
