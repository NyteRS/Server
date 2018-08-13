package com.server2.content.skills.harvesting;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.Utility;

/**
 * 
 * @author Faris
 */
public class Hatchet extends Utility {

	/**
	 * Stores players current axe index to prevent further need for looping
	 */
	public int HATCHET_INDEX = 0;

	/**
	 * Requirement, Bonus, Animation
	 */
	private static final int[][] HATCHET_DATA = { { 1, 1, 879 }, // Bronze
			{ 1, 2, 877 }, // Iron
			{ 6, 3, 875 }, // Steel
			{ 6, 4, 873 }, // Black
			{ 21, 5, 871 }, // Mithril
			{ 31, 6, 869 }, // Addy
			{ 41, 7, 867 }, // Rune
			{ 61, 8, 2846 }, // Dragon
			{ 41, 9, 10251 } // Adze
	};

	/**
	 * Stores loop information to find if player has correct axe
	 */
	private static final int[] HATCHET_IDS = { 1351, // Bronze
			1349, // Iron
			1353, // Steel
			1361, // Black
			1355, // Mithril
			1357, // Addy
			1359, // Rune
			6739, // Dragon
			13661 // Adze
	};

	public Hatchet(Player client) {
		super(client);
	}

	/**
	 * Send in the required data and receive a piece of axe information
	 * 
	 * @param dataType
	 *            the index
	 * @param player
	 *            client using
	 * @return
	 */
	public int getHatchetData(int dataType, Player player) {
		return HATCHET_DATA[getHatchetIndex(player)][dataType];
	}

	/**
	 * Loops through the axe array checking players axe against all
	 * 
	 * @param Player
	 * @return the index required to retrieve data
	 */
	private int getHatchetIndex(Player player) {
		for (int i = 0; i < HATCHET_IDS.length; i++)
			if (player.getActionAssistant().playerHasItem(HATCHET_IDS[i])
					|| player.getActionAssistant().playerEquipContains(
							HATCHET_IDS[i])) {
				setHatchetIndex(i);
				return i;
			}
		return 0;
	}

	/**
	 * Tell us if the player has a woodcutting axe to use
	 * 
	 * @param Player
	 *            is the client using
	 * @return
	 */
	public boolean hasHatchet(Player player) {
		for (int i = 0; i < HATCHET_IDS.length; i++)
			if (player.getActionAssistant().playerHasItem(HATCHET_IDS[i])
					|| player.getActionAssistant().playerEquipContains(
							HATCHET_IDS[i])) {
				setHatchetIndex(i);
				return true;
			}
		return false;
	}

	/**
	 * Sets the current axe being used to the Index to prevent the need to loop
	 * 
	 * @param axeID
	 */
	private void setHatchetIndex(int axeID) {
		HATCHET_INDEX = axeID;
	}
}
