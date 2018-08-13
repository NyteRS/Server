package com.server2.content.skills.harvesting;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.Utility;

/**
 * 
 * @author Faris
 */
public class Pickaxe extends Utility {

	/**
	 * Stores players current pickaxe index to prevent further need for looping
	 */
	public int PICKAXE_INDEX = 0;

	/**
	 * Requirement, Bonus, Animation
	 */
	private static final int[][] PICKAXE_DATA = { { 1, 1, 6753 }, // Bronze
			{ 1, 2, 6754 }, // Iron
			{ 6, 3, 6755 }, // Steel
			{ 21, 5, 6757 }, // Mithril
			{ 31, 6, 6756 }, // Addy
			{ 41, 7, 6752 }, // Rune
			{ 61, 8, 12199 }, // Dragon
			{ 41, 9, 10222 } // Adze
	};

	/**
	 * Stores loop information to find if player has correct pick
	 */
	private static final int[] PICKAXE_IDS = { 1265, // Bronze
			1267, // Iron
			1269, // Steel
			1273, // Mithril
			1271, // Addy
			1275, // Rune
			15259, // Dragon
			13661 // Adze
	};

	public Pickaxe(Player client) {
		super(client);
	}

	/**
	 * Send in the required data and receive a piece of pick information
	 * 
	 * @param dataType
	 *            the index
	 * @param player
	 *            client using
	 * @return
	 */
	public int getPickaxeData(int dataType, Player player) {
		return PICKAXE_DATA[getPickaxeIndex(player)][dataType];
	}

	/**
	 * Loops through the pick array checking players pick against all
	 * 
	 * @param Player
	 * @return the index required to retrieve data
	 */
	private int getPickaxeIndex(Player player) {
		for (int i = 0; i < PICKAXE_IDS.length; i++)
			if (player.getActionAssistant().playerHasItem(PICKAXE_IDS[i])
					|| player.getActionAssistant().playerEquipContains(
							PICKAXE_IDS[i])) {
				setPickaxeIndex(i);
				return i;
			}
		return 0;
	}

	/**
	 * Tell us if the player has a mining pick to use
	 * 
	 * @param Player
	 *            is the client using
	 * @return
	 */
	public boolean hasPickaxe(Player player) {
		for (int i = 0; i < PICKAXE_IDS.length; i++)
			if (player.getActionAssistant().playerHasItem(PICKAXE_IDS[i])
					|| player.getActionAssistant().playerEquipContains(
							PICKAXE_IDS[i])) {
				setPickaxeIndex(i);
				return true;
			}
		return false;
	}

	/**
	 * Sets the current pick being used to the Index to prevent the need to loop
	 * 
	 * @param axeID
	 */
	private void setPickaxeIndex(int axeID) {
		PICKAXE_INDEX = axeID;
	}
}
