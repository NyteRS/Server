package com.server2.content.skills.farming;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 19/04/12 Time: 02:42 To change
 * this template use File | Settings | File Templates.
 */
public class Farmers {

	public enum FarmersData {
		DANTAREA(2324, 41, "allotment", new String[] { "Northern patch",
				"Southern patch" }), ELSTAN(2323, 54, "allotment",
				new String[] { "North-west patch", "South-East patch" }), LYRA(
				2326, 123, "allotment", new String[] { "North-west patch",
						"South-East patch" }), KRAGEN(2325, 99, "allotment",
				new String[] { "Northern patch", "Southern patch" }), RHAZIEN(
				2337, 162, "bushes", null), TARIA(2336, 139, "bushes", null), DREVEN(
				2335, 47, "bushes", null), TORREL(2338, 144, "bushes", null), RHONEN(
				2334, 123, "hops", null), SELENA(2332, 134, "hops", null), VASQUEN(
				2333, 153, "hops", null), FRANCIS(2327, 62, "hops", null), BOLONGO(
				2343, 32, "fruitTree", null), ELLENA(2331, 53, "fruitTree",
				null), GILEH(2344, 74, "fruitTree", null), GARTH(2330, 70,
				"fruitTree", null), HESKEL(2340, 85, "tree", null), ALAIN(2339,
				163, "tree", null), TREZNOR(2341, 146, "tree", null), FAYETH(
				2342, 58, "tree", null);

		public static FarmersData forId(int npcId) {
			return npcs.get(npcId);
		}

		private final int npcId;
		private final int shopId;
		private final String fieldProtected;

		private final String[] dialogueOptions;

		private static Map<Integer, FarmersData> npcs = new HashMap<Integer, FarmersData>();

		static {
			for (final FarmersData data : FarmersData.values())
				npcs.put(data.npcId, data);
		}

		FarmersData(int npcId, int shopId, String fieldProtected,
				String[] dialogueOptions) {
			this.npcId = npcId;
			this.shopId = shopId;
			this.fieldProtected = fieldProtected;
			this.dialogueOptions = dialogueOptions;
		}

		public String[] getDialogueOptions() {
			return dialogueOptions;
		}

		public String getFieldProtected() {
			return fieldProtected;
		}

		public int getNpcId() {
			return npcId;
		}

		public int getShopId() {
			return shopId;
		}
	}

	public static final String[][] farmingAdvices = {
			{ "You don't have to buy all your plantpots you know,",
					"you can make them yourself on a pottery wheel. If",
					"you are a good enough craftsman, that is." },
			{ "Don't just throw away your weeds after you've",
					"raked a patch - put them in a compost bin and",
					"make some compost." },
			{ "Tree seeds must be grown in a plantpot of soil",
					"into a tree sapling, and then transferred to a",
					"tree patch to continue growing to adulthood." },
			{ "You can put up to ten potatoes, cabbages, or",
					"onions in vegetable sacks, although you can't",
					"have a mix in the same sack." },
			{ "You can buy all the farming tools from farming",
					"shops which can be found close to the allotments" },
			{ "You can fill plantpots with soil from Farming",
					"patches, if you have a gardening trowel." },
			{ "If you want to make your own sacks and baskets",
					"you'll need to use the loom that's near the",
					"Farming shop in Falador." },
			{ "Bittercap mushrooms can only be grown in special",
					"patches in Morytania, near the Mort Myre swamp." },
			{ "Applying compost to a patch will not only reduce",
					"the chance that your crops will get diseased, but",
					"you will also grow more crops to harvest." },
			{ "Hops are good for brewing ales. I believe there",
					"is a brewery up in Keldagrim somewhere." } };

	public static String getFinalPaymentString(String word) {
		if (word.contains("(5)"))
			return "baskets of " + word.replace("(5)", "");
		if (word.contains("(10)"))
			return "sacks of " + word.replace("(10)", "");
		return word;
	}

}
