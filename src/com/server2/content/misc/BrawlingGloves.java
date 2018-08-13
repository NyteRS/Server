package com.server2.content.misc;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;

/**
 * 
 * @author Hoodlum
 * 
 */
public class BrawlingGloves {
	/**
	 * The brawling gloves in order of skill
	 */
	private static final int[] GLOVES = { 13845, // Attack
			13845, // Defence
			13845, // Strength
			13845, // Hitpoints
			13846, // Range
			13848, // Prayer
			13847, // Magic
			13857, // Cooking
			13850, // Woodcutting
			0, // Fletching
			13856, // Fishing
			13851, // Firemaking
			0, // Crafting
			13855, // Smithing
			13852, // Mining
			0, // Herblore
			13859, // Agility
			13854, // Thieving
	};

	/**
	 * Getting the experience multiplier
	 * 
	 * @param player
	 *            the player
	 * @param skillId
	 *            the brawling gloves skill id
	 * @return the experience multiplier
	 */
	public static double getExperience(Player player, int skillId) {
		/**
		 * Invalid conditions
		 */
		if (GLOVES[skillId] < 1 || skillId > GLOVES.length)
			return 1.0;
		/**
		 * The multiplying
		 */
		if (player.playerEquipment[PlayerConstants.GLOVES] == GLOVES[skillId])
			return player.inWilderness() ? 1.5 : 4.0;
		return 1.0;
	}

}
