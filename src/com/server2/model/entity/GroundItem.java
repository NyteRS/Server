package com.server2.model.entity;

import com.server2.content.TradingConstants;
import com.server2.model.Item;
import com.server2.model.entity.player.Player;

/**
 * @author Renual
 */
public final class GroundItem {
	public static final int VISIBLE = 80;
	public static final int REMOVED = 160;
	private Item item;
	private final long encodedName;
	private int ticks;
	private final boolean spawner;
	private final String pked;
	private final Location position;

	public GroundItem(Item item, Location position, long encodedName,
			boolean spawner, String pked) {
		this.item = item;
		this.position = position;
		this.encodedName = encodedName;
		this.spawner = spawner;
		this.pked = pked;
	}

	public long getEncodedName() {
		return encodedName;
	}

	public Item getItem() {
		return item;
	}

	public Location getPosition() {
		return position;
	}

	public void increaseAmount(int amount) {
		item = Item.create(item.getId(), item.getCount() + amount);
	}

	public String isPked() {
		return pked;
	}

	public boolean isRemoved() {
		return ticks >= GroundItem.REMOVED;
	}

	public boolean isVisible() {
		return ticks >= GroundItem.VISIBLE;
	}

	public boolean isVisible(Player player) {
		if (encodedName == -1)
			return true;
		if (encodedName == player.encodedName)
			return true;
		if (spawner)
			return false;
		if (TradingConstants.isUntradable(item.getId()))
			return false;
		if (ticks >= GroundItem.VISIBLE)
			return true;
		return false;
	}

	public boolean makeVisible() {
		return ticks == GroundItem.VISIBLE;
	}

	public void setRemoved() {
		ticks = GroundItem.REMOVED;
	}

	public void tick() {
		ticks++;
	}
}
