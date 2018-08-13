package com.server2.content;

import com.server2.InstanceDistributor;
import com.server2.content.anticheat.WearItemCheating;

/**
 * 
 * @author Rene Roosen & Lukas
 * 
 */
public class TradingConstants {

	/**
	 * All Untradable items
	 */
	public static int[] untradeableItems = { 756, 753, 15017, 18778, 15389,
			18779, 18334, 18780, 18781, 18782, 19709, 10551, 10548, 6529,
			19111, 293, 3840, 3842, 3844, 6570, 23659, 18349, 18351, 18353,
			18355, 18357, 18359, 18363, 18361, 12158, 12159, 12160, 12163,
			12161, 12164, 12165, 12166, 12167, 12168, 19669, 12171, 12170,
			12169, 9950, 9949, 9948, 18508, 18509, 18510, 19709, 19710, 5070,
			5071, 5072, 5073, 5074, 5075, 18335, 18344, 18839, 8844, 8845,
			8846, 8847, 8848, 8849, 8850, 20072, 11137, 9748, 9751, 9754, 9757,
			9760, 9763, 9766, 9769, 9772, 9775, 9778, 9781, 9784, 9787, 9790,
			9793, 9796, 9799, 9802, 9805, 9808, 9811, 9949, 12170, 18510, 9747,
			9750, 9753, 9756, 9759, 9762, 9765, 9768, 9771, 9774, 9777, 9780,
			9783, 9786, 9789, 9792, 9795, 9798, 9801, 9804, 9807, 9810, 9948,
			12169, 18509, 18347, 19111, 9948, 9949, 20771, 7462, 7461, 7460,
			4049, 4050, 7459, 7458, 7456, 7455, 20772, 18333, 18343, 1507, 24,
			15420, 14605, 14603, 14602, 14600, 10507 };

	/**
	 * The people who cannot trade
	 */
	private static String[] cannotTradeUsers = { "Chajkjkjkrlie" };;

	/**
	 * Is the user a non trade user?
	 * 
	 * @return
	 */
	public static boolean isNoTradeUser(String username) {
		for (final String cannotTradeUser : cannotTradeUsers)
			if (username.equalsIgnoreCase(cannotTradeUser))
				return true;

		return false;
	}

	/**
	 * Is the itemID untradable?
	 * 
	 * @param itemId
	 * @return
	 */
	public static boolean isUntradable(int itemId) {
		for (final int untradeableItem : untradeableItems) {
			if (itemId == untradeableItem)
				return true;
			if (itemId >= 9747 && itemId <= 9812)
				return true;
		}

		for (final String dungName : WearItemCheating.DUNGEONEERING_ITEMS) {
			final String dungNameLower = dungName.toLowerCase();
			if (InstanceDistributor.getItemManager().getItemDefinition(itemId) == null)
				continue;
			if (InstanceDistributor.getItemManager().getItemDefinition(itemId)
					.getName().toLowerCase().contains(dungNameLower))
				return true;
		}
		for (final String dungName : WearItemCheating.DUNGEONEERING_ITEMS) {
			final String dungNameLower = dungName.toLowerCase();
			if (InstanceDistributor.getItemManager().getItemDefinition(itemId) != null)
				if (InstanceDistributor.getItemManager()
						.getItemDefinition(itemId).getName()
						.contains(dungNameLower))
					return true;
		}
		if (InstanceDistributor.getItemManager().getItemDefinition(itemId) != null)
			if (InstanceDistributor.getItemManager().getItemDefinition(itemId)
					.getName().contains("defender")
					|| InstanceDistributor.getItemManager()
							.getItemDefinition(itemId).getName()
							.contains("Defender"))
				return true;
		if (itemId >= 15300 && itemId <= 15337)
			return true;
		return false;
	}
}
