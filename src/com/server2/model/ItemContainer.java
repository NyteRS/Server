package com.server2.model;

import java.util.HashMap;
import java.util.Map;

/**
 * An item container
 * 
 * @author Graham
 */
public class ItemContainer {

	/**
	 * A map of slot ID to item data.
	 */
	protected Map<Integer, Item> items;

	/**
	 * The number of slots in the container.
	 */
	private final int containerSize;

	/**
	 * Initialises the container, reserving memory for a set size.
	 * 
	 * @param size
	 *            The size of the container.
	 */
	public ItemContainer(int size) {
		items = new HashMap<Integer, Item>(size);
		containerSize = size;
	}

	/**
	 * Adds an item to the container.
	 * 
	 * @param item
	 *            The item to add.
	 */
	public boolean addItem(Item item) {
		final int slot = getFreeSlot();
		if (slot != -1) {
			items.put(slot, item);
			return true;
		}
		return false;
	}

	public void clear() {
		items.clear();
	}

	/**
	 * Gets the size of this container.
	 * 
	 * @return The size of this container.
	 */
	public int getContainerSize() {
		return containerSize;
	}

	/**
	 * Gets the size of this container.
	 * 
	 * @return The size of this container.
	 */
	public int getContainerSize2() {
		return items.size();
	}

	public int getFreeSlot() {
		for (int i = 0; i < containerSize; i++)
			if (!items.containsKey(i))
				return i;
		return -1;
	}

	/**
	 * Get item by slot.
	 * 
	 * @param slot
	 * @return
	 */
	public Item getItemBySlot(int slot) {
		return items.get(slot);
	}

	/**
	 * Gets the next slot for the specified item.
	 * 
	 * @return The item slot, or -1 if it does not exist.
	 */
	public int getItemSlot(int id) {
		for (final Map.Entry<Integer, Item> entry : items.entrySet())
			if (entry.getValue().getId() == id)
				return entry.getKey();
		return -1;
	}

	public Map<Integer, Item> getMap() {
		return items;
	}

	/**
	 * Gets the nubmer of an item.
	 * 
	 * @param id
	 * @return
	 */
	public int numberOf(int id) {
		int ct = 0;
		for (final Map.Entry<Integer, Item> entry : items.entrySet())
			if (entry.getValue().getId() == id)
				ct += entry.getValue().getCount();
		return ct;
	}

	/**
	 * Removes the item in the specified slot.
	 * 
	 * @param slot
	 *            The slot.
	 */
	public void removeItem(int slot) {
		items.remove(slot);
	}

	/**
	 * @return the item container as an array.
	 */
	public Item[] toArray() {
		final Item[] array = new Item[14];
		for (int i = 0; i < array.length; i++)
			array[i] = items.get(i);
		return array;
	}

}
