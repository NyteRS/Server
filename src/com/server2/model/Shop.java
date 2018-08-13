package com.server2.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.server2.InstanceDistributor;
import com.server2.Settings;
import com.server2.model.entity.player.Player;
import com.server2.world.PlayerManager;

/**
 * Represents a single shop.
 * 
 * @author Graham & Lukas
 */
public class Shop extends ItemContainer {

	public static enum Type {
		GENERAL, SPECIALIST
	}

	private final int id;
	private final String name;
	private final Type type;
	private final int currency;

	private static final int MAX_SHOP_ITEMS = 100;

	/**
	 * Create a shop.
	 * 
	 * @param id
	 * @param name
	 * @param type
	 * @param currency
	 */
	public Shop(int id, String name, Type type, int currency) {
		super(MAX_SHOP_ITEMS);
		this.id = id;
		this.name = name;
		this.type = type;
		this.currency = currency;
	}

	/**
	 * Gets the current shop currency.
	 * 
	 * @return
	 */
	public int getCurrency() {
		return currency;
	}

	/**
	 * Gets this shops id.
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets an item buy value.
	 * 
	 * @param removeID
	 * @return
	 */
	public int getItemBuyValue(int id) {
		final double shopValue = InstanceDistributor.getItemManager()
				.getItemDefinition(id).getShopValue();
		double totalPrice = shopValue * 1.26875;

		for (final Map.Entry<Integer, Item> entry : items.entrySet()) {
			if (!(entry.getValue() instanceof ShopItem))
				continue;

			final ShopItem si = (ShopItem) entry.getValue();
			if (si.getId() == id && id != 20771)
				totalPrice = si.getPrice();
			else if (id == 20771)
				totalPrice = 2496000;

		}

		return (int) Math.ceil(totalPrice);
	}

	/**
	 * Gets an item buy value.
	 * 
	 * @param removeID
	 * @return
	 */
	public int getItemSellValue(int id) {
		final double shopValue = InstanceDistributor.getItemManager()
				.getItemDefinition(id).getShopValue();

		double totalPrice = shopValue * 1.26875;
		if (type == Type.GENERAL)
			totalPrice *= 1.25;
		else
			for (final Map.Entry<Integer, Item> entry : items.entrySet()) {
				if (!(entry.getValue() instanceof ShopItem))
					continue;
				final ShopItem si = (ShopItem) entry.getValue();
				if (si.getId() == id) {
					totalPrice = si.getPrice();
					totalPrice = totalPrice * 0.8;

				}
			}
		return (int) Math.floor(totalPrice);
	}

	/**
	 * Gets this shops name.
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets this shops type.
	 * 
	 * @return
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Is an item sellable?
	 * 
	 * @param id
	 * @return
	 */
	public boolean isItemSellable(int id) {
		if (type == Type.GENERAL)
			return true;
		for (final Map.Entry<Integer, Item> entry : items.entrySet())
			if (entry.getValue() instanceof ShopItem) {
				final ShopItem si = (ShopItem) entry.getValue();
				if (si.getId() == id)
					return true;
			}
		return false;
	}

	/**
	 * Gets the normal number of an item.
	 * 
	 * @param id
	 * @return
	 */
	public int normalNumberOf(int id) {
		int ct = 0;
		for (final Map.Entry<Integer, Item> entry : items.entrySet()) {
			if (!(entry.getValue() instanceof ShopItem))
				continue;
			final ShopItem si = (ShopItem) entry.getValue();
			if (si.getId() == id)
				ct += si.getNormalAmount();
		}
		return ct;
	}

	/**
	 * Updates stock etc.
	 */
	public void process() {
		final List<Integer> remove = new ArrayList<Integer>();
		for (final Map.Entry<Integer, Item> entry : items.entrySet())
			if (entry.getValue() instanceof ShopItem) {
				final ShopItem si = (ShopItem) entry.getValue();
				if (si.updateStock())
					remove.add(entry.getKey());
			}
		for (final int slot : remove)
			removeItem(slot);
		updated();
	}

	/**
	 * We've updated.
	 */
	public void updated() {
		for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
			final Player p = PlayerManager.getSingleton().getPlayers()[i];
			if (p == null)
				continue;
			if (p.isActive && !p.disconnected) {
				final Player c = p;
				if (c != null && c.getExtraData() != null)
					if (c.getExtraData().containsKey("shop"))
						if ((Integer) c.getExtraData().get("shop") != null)
							if ((Integer) c.getExtraData().get("shop") == id)
								c.getActionSender().sendShopReset(this);
			}
		}
	}

}
