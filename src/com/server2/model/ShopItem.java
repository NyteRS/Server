package com.server2.model;

import com.server2.world.ShopManager;

/**
 * Represents an item being sold in a shop
 * 
 * @author Graham & Lukas
 */
public class ShopItem extends Item {

	/**
	 * The normal amount of items that are in the shop (so restocking/player
	 * stock going down can work).
	 */
	private final int normalAmount;

	/**
	 * The last 'automatic' stock change.
	 */
	private long lastAutomaticStockChange;

	/**
	 * Is this item always stocked?
	 */
	private final boolean isAlwaysStocked;

	private final boolean inf;
	private final int price;

	/**
	 * Creates this shop item. Shop items created this way are automatically
	 * stocked.
	 * 
	 * @param id
	 *            The id.
	 * @param amount
	 *            The initial amount.
	 * @param normalAmount
	 *            The normal amount.
	 */
	public ShopItem(int id, int amount, int normalAmount, int price,
			boolean stocked, boolean inf) {
		super(id, amount);
		isAlwaysStocked = stocked;
		this.price = price;
		this.inf = inf;
		this.normalAmount = normalAmount;
		lastAutomaticStockChange = System.currentTimeMillis();
	}

	/**
	 * Creates this shop item. Shop items created this way are NOT automatically
	 * stocked.
	 * 
	 * @param id
	 *            The id.
	 * @param amount
	 *            The initial amount.
	 * @param normalAmount
	 *            The normal amount.
	 */

	public boolean getIsInf() {
		return inf;
	}

	/**
	 * Gets the last automatic stock chage.
	 * 
	 * @return
	 */
	public long getLastAutomaticStockChange() {
		return lastAutomaticStockChange;
	}

	/**
	 * Gets the normal amount.
	 * 
	 * @return
	 */
	public int getNormalAmount() {
		return normalAmount;
	}

	public int getPrice() {
		return price;
	}

	/**
	 * Is this item always automatically stocked?
	 * 
	 * @return
	 */
	public boolean isAlwaysStocked() {
		return isAlwaysStocked;
	}

	public void setLastAutomaticStockChange(long currentTimeMillis) {
		lastAutomaticStockChange = currentTimeMillis;
	}

	/**
	 * Updates the stock.
	 * 
	 * @return true if we should be removed, false if not.
	 */
	public boolean updateStock() {
		if (!isAlwaysStocked)
			return false;
		if (lastAutomaticStockChange + ShopManager.RESTOCK_DELAY < System
				.currentTimeMillis()) {
			lastAutomaticStockChange = System.currentTimeMillis();
			if (isAlwaysStocked) {
				if (getCount() > normalAmount && normalAmount > 0)
					setAmount(getCount() - 1);
				else if (getCount() < normalAmount)
					setAmount(getCount() + 1);
				return false;
			} else {
				setAmount(getCount() - 1);
				if (getCount() <= 0)
					return true;
				else
					return false;
			}
		}
		return false;
	}

}
