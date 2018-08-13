package com.server2.content.skills.harvesting.tree;

import com.server2.content.skills.harvesting.Harvest.Resource;

/**
 * 
 * @author Faris
 */
public interface Tree extends Resource {
	/**
	 * Gets and returns
	 * 
	 * @returns tree cutting chance per cycle
	 */
	public abstract int CUT_CHANCE();

	/**
	 * Gets and returns
	 * 
	 * @return Logs ID
	 */
	public abstract int LOG_ID();

	/**
	 * Gets and returns
	 * 
	 * @returns tree cutting requirement
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
	 * @returns tree stump id
	 */
	public abstract int STUMP_ID();

	/**
	 * Gets and returns
	 * 
	 * @return Trees ID
	 */
	public abstract int TREE_ID();

	/**
	 * Gets and returns
	 * 
	 * @returns tree cutting XP
	 */
	public abstract int XP_PER_LOG();
}
