package com.server2.content.skills.harvesting.rock;

import com.server2.content.skills.harvesting.Harvest.Resource;

/**
 * 
 * @author Faris
 */
public interface Rock extends Resource {
	/**
	 * Gets and returns
	 * 
	 * @returns ore mining chance per cycle
	 */
	public abstract int MINE_CHANCE();

	/**
	 * Gets and returns
	 * 
	 * @return ore ID
	 */
	public abstract int ORE_ID();

	/**
	 * Gets and returns
	 * 
	 * @returns ore mining requirement
	 */
	public abstract int REQUIREMENT();

	/**
	 * Gets and returns
	 * 
	 * @returns placement time
	 */
	public abstract int RESPAWN_LENGTH();

	/**
	 * Gets and returns
	 * 
	 * @return Rock ID
	 */
	public abstract int ROCK_ID();

	/**
	 * Gets and returns
	 * 
	 * @return name of Rock
	 */
	public abstract String ROCK_NAME();

	/**
	 * Gets and returns
	 * 
	 * @returns ore vein id
	 */
	public abstract int VEIN_ID();

	/**
	 * Gets and returns
	 * 
	 * @returns ore mining XP
	 */
	public abstract int XP_PER_ORE();
}
