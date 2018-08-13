package com.server2.model.entity.player;

import java.util.ArrayList;

import com.server2.model.Item;

/**
 * 
 * @author Rene Roosen
 * 
 */
public interface ContainerListener {
	void itemChanged(TradingContainer container, int slot);

	void itemsChanged(TradingContainer container, ArrayList<Item> items);

	void itemsChanged(TradingContainer container, int size);

	void itemsChanged(TradingContainer container, int[] slots);
}
