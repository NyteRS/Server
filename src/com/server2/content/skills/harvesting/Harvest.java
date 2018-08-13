package com.server2.content.skills.harvesting;

import com.server2.model.entity.player.Player;

/**
 * 
 * @author Faris
 */
public interface Harvest {

	public interface Resource {
	}

	/**
	 * Handles the distributing of random events
	 */
	public abstract void attemptRandomEvent(Player client);

	/**
	 * Main loop of harvesting action
	 * 
	 * @param client
	 */
	public abstract void execute(Player client);

	/**
	 * The post method after a successful harvest is completed
	 * 
	 * @param client
	 */
	public abstract void finalise(Player client);

	/**
	 * Attempts to handle any bonuses received by any familiars the client has
	 * currently summoning in aid of the harvesting action
	 * 
	 * @param client
	 * @return
	 */
	public abstract boolean handlePetAddition(Player client);

	/**
	 * Handles distributing the material to the player
	 * 
	 * @param client
	 */
	public abstract void harvestMaterial(Player client);

	/**
	 * The pre checks before harvesting continues
	 * 
	 * @param client
	 * @return
	 */
	public abstract boolean init(Player client, Resource resource);

	/**
	 * Handles the movement checking and animation loop
	 * 
	 * @param client
	 */
	public abstract boolean loopMobility(Player client);

	/**
	 * Resets the action ready for ending the event
	 * 
	 * @param client
	 */
	public abstract void reset(Player client);

	/**
	 * The algorithm linked to determine the success rate of player during the
	 * harvesting action
	 * 
	 * @param client
	 * @return
	 */
	public abstract int successAlgorithm(Player client);

}
