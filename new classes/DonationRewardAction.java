package com.server2.sql.database.util;

import com.server2.engine.task.Task;

/**
 * Represents the actions that will be ran on the player once they have donated.
 * 
 * @author LT Smith
 * 
 */
public class DonationRewardAction {

	/**
	 * The id of the reward set that the player chose.
	 */
	private int productId;

	/**
	 * The price of the reward set.
	 */
	private int productPrice;

	/**
	 * Contains the actions that will be executed on the player. For example, an
	 * add item method could be called in the method body. That could be used to
	 * give the player their rewards or alternatively, send them a message to
	 * thank them.
	 */
	private Task action;

	/**
	 * To construct a new donation reward object
	 * 
	 * @param id
	 *            the id of the reward
	 * @param event
	 *            what to do when the donation action is called
	 */
	public DonationRewardAction(int id, int price, final Task event) {
		setProductId(id);
		setProductPrice(price);
		setAction(event);
	}

	/**
	 * @return The action set
	 */
	public Task getAction() {
		return action;
	}

	/**
	 * @return the product id
	 */
	public int getProductId() {
		return productId;
	}

	/**
	 * @return The price of the product.
	 */
	public int getProductPrice() {
		return productPrice;
	}

	/**
	 * @param action
	 *            The action set
	 */
	public void setAction(Task action) {
		this.action = action;
	}

	/**
	 * @param productId
	 *            The id of the donation reward
	 */
	public void setProductId(int productId) {
		this.productId = productId;
	}

	/**
	 * @param productPrice
	 *            The price
	 */
	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}

}
