package com.server2.model;

/**
 * A type of item.
 * 
 * @author Graham
 */
public class ItemDefinition {

	private final int id;
	private final String name;
	private final double shopValue;
	private final double lowAlch;
	private final double highAlch;
	private final int[] bonuses;
	private final String description;

	public ItemDefinition(int id, String name, String description,
			double shopValue, double lowAlch, double highAlch, int[] bonuses) {
		this.id = id;
		this.name = name;
		this.shopValue = shopValue;
		this.highAlch = lowAlch;
		this.lowAlch = highAlch;
		this.bonuses = bonuses;
		this.description = description;
	}

	public int getBonus(int i) {
		return bonuses[i];
	}

	public int[] getBonuses() {
		return bonuses;
	}

	public String getDescription() {
		return description;
	}

	public double getHighAlch() {
		return highAlch;
	}

	public int getId() {
		return id;
	}

	public double getLowAlch() {
		return lowAlch;
	}

	public String getName() {
		if (getId() == -1)
			return "Unarmed";
		return name;
	}

	public double getShopValue() {
		return shopValue;
	}

}
