package com.server2.sql.database.util;

import com.server2.GameEngine;
import com.server2.engine.task.Task;
import com.server2.model.entity.player.Player;

/**
 * Used to give the player a reward of donation points
 * 
 * @author LT Smith
 * 
 */
public class AddDonationPointTask implements Task {

	/**
	 * The player that we are giving the points to.
	 */
	private Player player;

	/**
	 * The amount of donation points we are gonna give the player.
	 */
	private int amount;

	/**
	 * Creates a new add point task
	 * 
	 * @param thePlayer
	 *            The player who is gonna receive the points
	 * @param points
	 *            The amount of points the player is gonna receive
	 */
	public AddDonationPointTask(Player thePlayer, int points) {
		setPlayer(thePlayer);
		setAmount(points);
	}

	@Override
	public void execute(GameEngine context) {
		if (SQLWebsiteUtils.isEligibleForDonatorRank(getPlayer()
				.getPrivileges()))
			getPlayer().setPrivileges(5);
		getPlayer().sendMessage(
				"You have received " + amount
						+ " points as a reward for donating");
		getPlayer().sendMessage("Thank you man.");
		//getPlayer().setDonatorPoints(4);
		getPlayer().donatorRights = 1;

	}

	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * 
	 * @return The player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * 
	 * @param player
	 *            The player
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

}
