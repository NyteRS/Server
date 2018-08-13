package com.server2.model.entity.player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.server2.model.Item;

/**
 * 
 * @author Rene Roosen
 * 
 */
public final class TradingContainer {

	public static enum Type {
		STANDARD, ALWAYS_STACK, CONTIGUOUS,
	}

	protected final int capacity;
	protected final int[] items;

	protected final int[] amounts;
	protected final Type type;
	private final List<ContainerListener> listeners = new LinkedList<ContainerListener>();

	protected boolean firingEvents = true;

	public TradingContainer(Type type, int[] items, int[] amounts) {
		this.type = type;
		capacity = items.length;
		this.items = items;
		this.amounts = amounts;
		for (int i = 0; i < capacity; i++)
			items[i] = -1;
	}

	public boolean add(int item) {
		if (Item.itemStackable[item] || type == Type.ALWAYS_STACK) {
			for (int i = 0; i < capacity; i++)
				if (items[i] == item) {
					final int totalCount = 1 + amounts[i];
					if (totalCount < 1)
						return false;
					this.set(i, Item.create(items[i], amounts[i] + 1));
					return true;
				}
			final int slot = freeSlot();
			if (slot == -1)
				return false;
			this.set(slot, item);
			return true;
		} else {
			final int freeSlot = freeSlot();
			if (freeSlot == -1)
				return false;
			this.set(freeSlot, item);
			return true;
		}
	}

	public boolean add(int item, int amount) {
		if (amount <= 0)
			return false;
		if (Item.itemStackable[item] || type == Type.ALWAYS_STACK) {
			for (int i = 0; i < capacity; i++)
				if (items[i] == item) {
					final int totalCount = amount + amounts[i];
					if (totalCount < 1)
						return false;
					this.set(i, Item.create(items[i], amounts[i] + amount));
					return true;
				}
			final int slot = freeSlot();
			if (slot == -1)
				return false;
			this.set(slot, item, amount);
			return true;
		} else {
			final int freeSlots = freeSlots();
			if (freeSlots == 0)
				return false;
			if (amount == 1) {
				this.set(freeSlot(), item);
				return true;
			} else {
				if (amount > freeSlots)
					amount = freeSlots;
				final boolean b = firingEvents;
				firingEvents = false;
				try {
					final int[] slots = new int[amount];
					for (int i = 0; i < amount; i++) {
						final int slot = freeSlot();
						this.set(freeSlot(), Item.create(item));
						slots[i] = slot;
					}
					if (b)
						this.fireItemsChanged(slots);
					return true;
				} finally {
					firingEvents = b;
				}
			}
		}
	}

	public boolean add(int preferredSlot, Item item) {
		if (Item.itemStackable[item.getId()] || type == Type.ALWAYS_STACK) {
			for (int i = 0; i < capacity; i++)
				if (items[i] == item.getId()) {
					final int totalCount = item.getCount() + amounts[i];
					if (totalCount < 1)
						return false;
					this.set(i,
							Item.create(items[i], amounts[i] + item.getCount()));
					return true;
				}
			this.set(preferredSlot, item);
			return true;
		} else {
			int freeSlots = freeSlots();
			if (freeSlots == 0)
				return false;
			if (freeSlots > item.getCount())
				freeSlots = item.getCount();
			final boolean b = firingEvents;
			firingEvents = false;
			try {
				final int[] slots = new int[freeSlots];
				for (int i = 0; i < freeSlots; i++) {
					int slot = freeSlot();
					if (i == 0 && preferredSlot != -1) {
						final int inSlot = items[preferredSlot];
						if (inSlot == 0)
							slot = preferredSlot;
					}
					this.set(slot, Item.create(item.getId()));
					slots[i] = slot;
				}
				if (b)
					this.fireItemsChanged(slots);
				return true;
			} finally {
				firingEvents = b;
			}
		}
	}

	public boolean add(Item item) {
		if (Item.itemStackable[item.getId()] || type == Type.ALWAYS_STACK) {
			for (int i = 0; i < capacity; i++)
				if (items[i] == item.getId()) {
					final int totalCount = item.getCount() + amounts[i];
					if (totalCount < 1)
						return false;
					this.set(i,
							Item.create(items[i], amounts[i] + item.getCount()));
					return true;
				}
			final int slot = freeSlot();
			if (slot == -1)
				return false;
			this.set(slot, item);
			return true;
		} else {
			int freeSlots = freeSlots();
			if (freeSlots == 0)
				return false;
			if (item.getCount() == 1) {
				this.set(freeSlot(), item);
				return true;
			} else {
				if (freeSlots > item.getCount())
					freeSlots = item.getCount();
				final boolean b = firingEvents;
				firingEvents = false;
				try {
					final int[] slots = new int[freeSlots];
					for (int i = 0; i < freeSlots; i++) {
						final int slot = freeSlot();
						this.set(freeSlot(), Item.create(item.getId()));
						slots[i] = slot;
					}
					if (b)
						this.fireItemsChanged(slots);
					return true;
				} finally {
					firingEvents = b;
				}
			}
		}
	}

	public boolean add(Item item, int amount) {
		if (Item.itemStackable[item.getId()] || type == Type.ALWAYS_STACK) {
			for (int i = 0; i < capacity; i++)
				if (items[i] == item.getId()) {
					final int totalCount = amount + amounts[i];
					if (totalCount < 1)
						return false;
					this.set(i, Item.create(items[i], amounts[i] + amount));
					return true;
				}
			final int slot = freeSlot();
			if (slot == -1)
				return false;
			this.set(slot, Item.create(item.getId(), amount));
			return true;
		} else {
			int freeSlots = freeSlots();
			if (freeSlots == 0)
				return false;
			if (freeSlots > amount)
				freeSlots = amount;
			final boolean b = firingEvents;
			firingEvents = false;
			try {
				final int[] slots = new int[freeSlots];
				for (int i = 0; i < freeSlots; i++) {
					final int slot = freeSlot();
					this.set(freeSlot(), Item.create(item.getId()));
					slots[i] = slot;
				}
				if (b)
					this.fireItemsChanged(slots);
				return true;
			} finally {
				firingEvents = b;
			}
		}
	}

	public void addListener(ContainerListener listener) {
		listeners.add(listener);
		if (type == Type.CONTIGUOUS) {
			// listener.itemsChanged(this, this.size());
		} else {
			// listener.itemsChanged(this, capacity);
		}
	}

	public int[] amounts() {
		return amounts;
	}

	public int capacity() {
		return capacity;
	}

	public void clear() {
		for (int i = 0; i < capacity; i++)
			reset(i);
		if (firingEvents)
			this.fireItemsChanged();
	}

	public void clear(boolean display) {
		for (int i = 0; i < capacity; i++)
			reset(i);
		if (display)
			if (firingEvents)
				this.fireItemsChanged();
	}

	public boolean contains(int id) {
		return getSlotById(id) != -1;
	}

	public boolean contains(int id, int amount) {
		return this.getCount(id) >= amount;
	}

	public boolean contains(Item item) {
		return this.getCount(item.getId()) >= item.getCount();
	}

	public boolean containsAll(Item[] items) {
		for (final Item item : items) {
			if (getSlotById(item.getId()) != -1)
				continue;
			return false;
		}
		return true;
	}

	public ArrayList<Item> displaySearch(Player player, String toSearch) {
		player.containerSearch = toSearch;
		final ArrayList<Item> list = new ArrayList<Item>();
		final ArrayList<Item> item = toArray();
		toSearch = toSearch.toLowerCase();
		for (final Item items : item)
			if (items != null && items.getId() != -1) {
				final String itemName = items.getDefinition().getName()
						.toLowerCase();
				if (itemName.contains(toSearch))
					list.add(items);
			}
		for (final ContainerListener listener : listeners)
			listener.itemsChanged(this, list);
		player.sendMessage("Found " + list.size() + " results for " + toSearch
				+ '.');
		return list;
	}

	public void fireItemChanged(int slot) {
		for (final ContainerListener listener : listeners)
			listener.itemChanged(this, slot);
	}

	public void fireItemsChanged() {
		if (type == Type.CONTIGUOUS) {
			final int size = size();
			for (final ContainerListener listener : listeners)
				listener.itemsChanged(this, size);
		} else
			this.fireItemsChanged(usedSlots());
	}

	public void fireItemsChanged(int[] slots) {
		for (final ContainerListener listener : listeners)
			listener.itemsChanged(this, slots);
	}

	public int freeSlot() {
		for (int i = 0; i < capacity; i++) {
			if (items[i] != -1)
				continue;
			return i;
		}
		return -1;
	}

	public int freeSlots() {
		return capacity - size();
	}

	public int getAmount(int index) {
		if (index == -1)
			return -1;
		return amounts[index];
	}

	public int getCount(int id) {
		if (type == Type.ALWAYS_STACK || Item.itemStackable[id]) {
			for (int i = 0; i < capacity; i++) {
				if (items[i] != id)
					continue;
				return amounts[i];
			}
			return 0;
		} else {
			int total = 0;
			for (int i = 0; i < capacity; i++) {
				if (items[i] != id)
					continue;
				total += 1;
			}
			return total;
		}
	}

	public int getCount(Item item) {
		return this.getCount(item.getId());
	}

	public int getItem(int index) {
		if (index == -1)
			return -1;
		return items[index];
	}

	public int getItemSlot(int id) {
		for (int i = 0; i < items.length; i++)
			if (items[i] == id)
				return i;
		return -1;
	}

	public int getSlotById(int id) {
		for (int i = 0; i < capacity; i++) {
			if (items[i] != id)
				continue;
			return i;
		}
		return -1;
	}

	public boolean hasRoomFor(Item item) {
		if (Item.itemStackable[item.getId()] || type == Type.ALWAYS_STACK) {
			for (int i = 0; i < capacity; i++) {
				if (items[i] != item.getId())
					continue;
				return item.getCount() + amounts[i] >= 1;
			}
			return freeSlot() != -1;
		}
		return freeSlots() >= item.getCount();
	}

	public boolean isEmpty() {
		boolean isEmpty = true;
		for (int i = 0; i < capacity; i++)
			if (items[i] >= 0)
				isEmpty = false;
		return isEmpty;
	}

	public boolean isFiringEvents() {
		return firingEvents;
	}

	public boolean isSlotFree(int slot) {
		return items[slot] == -1;
	}

	public boolean isSlotUsed(int slot) {
		return items[slot] != -1;
	}

	public int[] items() {
		return items;
	}

	public void order() {
		final int[] tempItem = items.clone();
		final int[] tempAmount = amounts.clone();
		int slot = 0;
		this.clear(false);
		for (int i = 0; i < tempItem.length; i++)
			if (tempItem[i] != -1) {
				items[slot] = tempItem[i];
				amounts[slot] = tempAmount[i];
				slot++;
			}
	}

	public void refresh() {
		this.fireItemsChanged(usedSlots());
		firingEvents = true;
	}

	public void remove(int id) {
		this.remove(-1, id);
	}

	public void remove(int preferredSlot, int id) {
		if (id == -1)
			return;
		if (Item.itemStackable[id] || type == Type.ALWAYS_STACK) {
			final int slot = getSlotById(id);
			if (slot == -1)
				return;
			final int stackAmount = amounts[slot];
			if (stackAmount > 1)
				this.set(slot, Item.create(items[slot], stackAmount - 1));
			else
				this.set(slot, null);
		} else if (preferredSlot == -1)
			this.set(getSlotById(id), null);
		else {
			int slot = preferredSlot;
			if (items[preferredSlot] != id)
				slot = getSlotById(id);
			this.set(slot, null);
		}
	}

	public int remove(int preferredSlot, int id, int amount) {
		int removed = 0;
		if (id == -1)
			return removed;
		if (Item.itemStackable[id] || type == Type.ALWAYS_STACK) {
			final int slot = getSlotById(id);
			if (slot == -1)
				return removed;
			final int stackAmount = amounts[slot];
			if (stackAmount > amount) {
				removed = amount;
				this.set(slot, Item.create(items[slot], stackAmount - amount));
			} else {
				removed = stackAmount;
				this.set(slot, null);
			}
		} else if (amount == 1) {
			this.set(preferredSlot == -1 ? getSlotById(id)
					: items[preferredSlot] != id ? getSlotById(id)
							: preferredSlot, null);
			return 1;
		} else {
			final boolean b = firingEvents;
			firingEvents = false;
			try {
				int count = this.getCount(id);
				if (count > amount)
					count = amount;
				final int[] slots = new int[count];
				for (int i = 0; i < count; i++) {
					int slot = getSlotById(id);
					if (i == 0 && preferredSlot != -1)
						if (items[preferredSlot] == id)
							slot = preferredSlot;
					this.set(slot, null);
					slots[i] = slot;
				}
				if (b)
					this.fireItemsChanged(slots);
				removed += count;
			} finally {
				firingEvents = b;
			}
		}
		return removed;
	}

	/*
	 * public boolean removeAll(Item[] items) { boolean removedAll = true; for
	 * (Item item : items) { if (remove(item) != item.getAmount()) { removedAll
	 * = false; } } return removedAll; }
	 */

	public int remove(int preferredSlot, Item item) {
		int removed = 0;
		if (Item.itemStackable[item.getId()] || type == Type.ALWAYS_STACK) {
			final int slot = getSlotById(item.getId());
			final int stackAmount = amounts[slot];
			if (stackAmount > item.getCount()) {
				removed = item.getCount();
				this.set(slot,
						Item.create(items[slot], stackAmount - item.getCount()));
			} else {
				removed = stackAmount;
				this.set(slot, null);
			}
		} else if (item.getCount() == 1) {
			this.set(
					preferredSlot == -1 ? getSlotById(item.getId())
							: items[preferredSlot] != item.getId() ? getSlotById(item
									.getId()) : preferredSlot, null);
			return 1;
		} else {
			final boolean b = firingEvents;
			firingEvents = false;
			try {
				int count = this.getCount(item.getId());
				if (count > item.getCount())
					count = item.getCount();
				final int[] slots = new int[count];
				for (int i = 0; i < count; i++) {
					int slot = getSlotById(item.getId());
					if (i == 0 && preferredSlot != -1)
						if (items[preferredSlot] == item.getId())
							slot = preferredSlot;
					this.set(slot, null);
					slots[i] = slot;
				}
				if (b)
					this.fireItemsChanged(slots);
				removed += count;
			} finally {
				firingEvents = b;
			}
		}
		return removed;
	}

	public int remove(Item item) {
		return this.remove(item, item.getCount());
	}

	public int remove(Item item, int amount) {
		return this.remove(-1, item.getId(), amount);
	}

	public void removeAllListeners() {
		listeners.clear();
	}

	public void removeListener(ContainerListener listener) {
		listeners.remove(listener);
	}

	private void reset(int slot) {
		if (slot == -1)
			return;
		items[slot] = -1;
		amounts[slot] = 0;
	}

	public void set(int index, int item) {
		if (item == 0)
			reset(index);
		else {
			items[index] = item;
			amounts[index] = 1;
		}
		if (firingEvents)
			fireItemChanged(index);
	}

	public void set(int index, int item, int amount) {
		if (item == 0)
			reset(index);
		else {
			items[index] = item;
			amounts[index] = amount;
		}
		if (firingEvents)
			fireItemChanged(index);
	}

	public void set(int index, Item item) {
		if (item == null)
			reset(index);
		else {
			items[index] = item.getId();
			amounts[index] = item.getCount();
		}
		if (firingEvents)
			fireItemChanged(index);
	}

	public void setFiringEvents(boolean firingEvents) {
		this.firingEvents = firingEvents;
	}

	public void shift() {
		final int[] oldItems = items.clone();
		final int[] oldAmounts = amounts.clone();
		int newIndex = 0;
		for (int i = 0; i < capacity; i++) {
			if (items[i] == -1)
				continue;
			items[newIndex] = oldItems[i];
			amounts[newIndex++] = oldAmounts[i];
		}
		for (; newIndex < capacity; newIndex++) {
			items[newIndex] = -1;
			amounts[newIndex] = 0;
		}
	}

	public int size() {
		int size = 0;
		for (final int item : items) {
			if (item == -1)
				continue;
			size++;
		}
		return size;
	}

	public ArrayList<Item> toArray() {
		final ArrayList<Item> list = new ArrayList<Item>();
		for (int i = 0; i < items.length; i++)
			list.add(Item.create(items[i], amounts[i]));
		return list;
	}

	public Item[] toArray2() {
		final Item[] items2 = new Item[items.length];
		for (int i = 0; i < items.length; i++)
			items2[i] = new Item(items[i], amounts[i]);
		return items2;
	}

	private int[] usedSlots() {
		final int[] slots = new int[size()];
		int offset = 0;
		for (int i = 0; i < capacity; i++) {
			if (items[i] == -1)
				continue;
			slots[offset++] = i;
		}
		return slots;
	}
}
