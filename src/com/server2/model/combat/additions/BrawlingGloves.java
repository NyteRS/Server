package com.server2.model.combat.additions;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class BrawlingGloves {

	/**
	 * Gets the correct stat per brawler glove.
	 * 
	 * @param itemID
	 * @return
	 */
	public static int getSkill(int itemID) {
		if (itemID == 13845)
			return PlayerConstants.ATTACK;
		else if (itemID == 13846)
			return PlayerConstants.RANGE;
		else if (itemID == 13847)
			return PlayerConstants.MAGIC;
		else if (itemID == 13848)
			return PlayerConstants.PRAYER;
		else if (itemID == 13849)
			return PlayerConstants.AGILITY;
		else if (itemID == 13850)
			return PlayerConstants.WOODCUTTING;
		else if (itemID == 13851)
			return PlayerConstants.FIREMAKING;
		else if (itemID == 13852)
			return PlayerConstants.MINING;
		else if (itemID == 13853)
			return PlayerConstants.HUNTER;
		else if (itemID == 13854)
			return PlayerConstants.THIEVING;
		else if (itemID == 13855)
			return PlayerConstants.SMITHING;
		else if (itemID == 13856)
			return PlayerConstants.FISHING;
		else if (itemID == 13857)
			return PlayerConstants.COOKING;
		return 30;
	}

	/**
	 * Represents a default null client
	 */
	private final Player client;

	/**
	 * Holds the charges each of the brawlers have
	 */
	public int[] brawlerCharges = new int[13];

	/**
	 * Reconstructs the client
	 */
	public BrawlingGloves(Player client) {
		this.client = client;
	}

	/**
	 * Sets the brawler's charges to the last save
	 */
	public void setCharges(int loop, int toSet) {
		client.getBrawlingGloves().brawlerCharges[loop] = toSet;
	}

}
