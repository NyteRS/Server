package com.server2.world;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.server2.model.Shop;

/**
 * Manages shops
 * 
 * @author Graham/Killamess/Steven
 */
public class ShopManager {

	public Map<Integer, Shop> shops;

	public static final int RESTOCK_DELAY = 30000;

	public ShopManager() {
		shops = new ConcurrentHashMap<Integer, Shop>();

	}

	/**
	 * Gets the shops.
	 * 
	 * @return
	 */
	public Map<Integer, Shop> getShops() {
		return shops;
	}

	/**
	 * Processes shops.
	 */
	public void tick() {
		for (final ConcurrentHashMap.Entry<Integer, Shop> entry : shops
				.entrySet())
			entry.getValue().process();
	}

}
