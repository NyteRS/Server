package com.server2.model.entity.player;

import com.server2.content.skills.harvesting.Harvest.Resource;
import com.server2.content.skills.harvesting.Hatchet;
import com.server2.content.skills.harvesting.Pickaxe;
import com.server2.content.skills.harvesting.tree.Tree;

/**
 * 
 * @author Faris
 */
public class Utility {

	/**
	 * Returns the utility the player has equipped
	 * 
	 * @returns null if nothing is carried
	 */
	public static Utility getCarriedUtility(Player client, Resource resource) {
		final Pickaxe pickaxe = new Pickaxe(client);
		final Hatchet hatchet = new Hatchet(client);
		if (pickaxe.hasPickaxe(client) && !hatchet.hasHatchet(client))
			return pickaxe;
		if (hatchet.hasHatchet(client) && !pickaxe.hasPickaxe(client))
			return hatchet;
		if (hatchet.hasHatchet(client) && pickaxe.hasPickaxe(client)) {
			final Utility utilRequired = resource instanceof Tree ? hatchet
					: pickaxe;
			return utilRequired;
		}
		return null;
	}

	Player client;

	private boolean usingUtility;

	public Utility(Player client) {
		this.client = client;
	}

	/**
	 * @return the usingUtility
	 */
	public boolean isUsingUtility() {
		return usingUtility;
	}

	/**
	 * @param usingUtility
	 *            the usingUtility to set
	 */
	public void setUsingUtility(boolean usingUtility) {
		this.usingUtility = usingUtility;
	}

}
