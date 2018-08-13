package com.server2.content.misc;

import java.util.Map;

import com.server2.InstanceDistributor;
import com.server2.content.TradingConstants;
import com.server2.model.Item;
import com.server2.model.ItemDefinition;
import com.server2.model.Shop;
import com.server2.model.entity.player.Player;

/**
 * Handles the general store
 * 
 * @author Lukas P
 * 
 */
public class GeneralStore {

	public static final int[] prices = { 20771, 20772, 20773, 20770, 4170,
			500000, 13736, 800000, 10548, 3000000, 10551, 5000000, 1511, 25,
			1513, 1500, 1515, 500, 1517, 65, 1519, 30, 1521, 35, 1777, 150,
			1779, 75, 48, 20, 56, 40, 58, 35, 62, 90, 66, 550, 70, 1450, 839,
			190, 845, 200, 847, 210, 851, 265, 855, 700, 859, 1590, 50, 18, 54,
			35, 60, 30, 64, 80, 68, 500, 72, 1250, 841, 150, 843, 170, 849,
			175, 853, 220, 857, 550, 861, 1600, 436, 150, 438, 150, 440, 350,
			442, 250, 444, 500, 447, 1200, 449, 3200, 451, 20000, 453, 430,
			2349, 400, 2351, 450, 2353, 1500, 2359, 4700, 2361, 7000, 2363,
			26000, 1203, 200, 1205, 100, 1207, 400, 1209, 700, 1211, 2000,
			1213, 25000, 1115, 2500, 1117, 2000, 1119, 6000, 1121, 23500, 1123,
			28000, 1127, 80000, 317, 15, 321, 25, 327, 50, 331, 150, 335, 100,
			341, 170, 345, 60, 349, 120, 353, 250, 359, 200, 363, 250, 371,
			500, 377, 420, 383, 1500, 7944, 700, 7946, 700, 315, 15, 319, 25,
			325, 50, 329, 150, 332, 100, 339, 170, 343, 60, 347, 120, 351, 250,
			357, 200, 361, 250, 369, 500, 375, 420, 381, 1500, 1614, 15000,
			1619, 8000, 1621, 4000, 1623, 1500, 1607, 1000, 1605, 1500, 1603,
			1500, 1601, 11000, 1065, 1000, 1099, 1900, 1135, 3000, 2487, 1200,
			2489, 2000, 2491, 2500, 2493, 2400, 2495, 4000, 2497, 5000, 2499,
			3600, 2501, 6000, 2503, 7500, 1059, 50, 1061, 100, 1167, 120, 1131,
			250, 554, 3, 555, 3, 556, 3, 557, 3, 558, 4, 559, 5, 560, 200, 561,
			100, 562, 80, 563, 150, 564, 90, 565, 250, 566, 300, 892, 290,
			1079, 20000, 1093, 20000, 1113, 25000, 1127, 40000, 1147, 10000,
			1163, 20000, 1185, 15000, 1201, 30000, 1247, 5000, 1289, 7000,
			1303, 25000, 1319, 25000, 1333, 12000, 1347, 8000, 1373, 20000,
			1432, 7500, 10818, 300, 10816, 100, 10814, 75, 526, 100, 532, 750,
			536, 5000, 10088, 145, 10090, 155, 10091, 165, 10089, 175, 10087,
			185, 13899, 2000000, 13902, 2000000, 13887, 2000000, 13893,
			2000000, 4151, 6000000, 19784, 20000000, 14484, 50000000, 11694,
			120000000, 11696, 25000000, 11698, 25000000, 11700, 25000000, 7462,
			100000, 805, 300, 811, 200, 817, 210, 830, 300, 836, 350, 868,
			1000, 876, 1200, 892, 200, 893, 250, 1079, 20000, 1093, 20000,
			1113, 25000, 1127, 50000, 1147, 12000, 1163, 25000, 1185, 20000,
			1201, 30000, 1213, 1000, 1229, 1200, 1247, 5000, 1261, 5500, 1275,
			15000, 1289, 10000, 1303, 18000, 1319, 30000, 1333, 15000, 1347,
			10000, 1359, 7000, 1373, 20000, 1432, 7000, 3101, 50000, 3202,
			40000, 4131, 15000, 810, 100, 829, 150, 867, 350, 890, 200, 1073,
			3000, 1091, 3000, 1111, 5000, 1123, 8000, 1145, 2000, 1161, 3100,
			1183, 5000, 1199, 7500, 1211, 500, 1245, 700, 1271, 2000, 1287,
			1500, 1301, 2500, 1317, 5000, 1331, 2500, 1357, 1200, 1371, 3500,
			1430, 1000, 3100, 1200, 3200, 5000, 4129, 7000, 6895, 1000, 803,
			100, 809, 80, 815, 100, 828, 150, 866, 150, 888, 85, 1071, 2000,
			1085, 2000, 1109, 2500, 1121, 5000, 1143, 1000, 1159, 2000, 1181,
			1500, 1197, 2200, 1209, 400, 1243, 800, 1273, 1000, 1285, 800,
			1299, 1200, 1315, 1800, 1329, 1200, 1343, 1000, 1345, 800, 1369,
			1300, 1429, 800, 4127, 3000, 9431, 12000, 869, 100, 1077, 1000,
			1089, 1000, 1107, 2000, 1125, 4000, 1151, 2000, 1165, 2500, 1179,
			2000, 1195, 3000, 1217, 1000, 1283, 1200, 1297, 1500, 1313, 2500,
			1327, 1500, 808, 75, 817, 80, 827, 100, 886, 20, 1096, 1000, 1083,
			1000, 1105, 2000, 1141, 500, 1157, 800, 1177, 1000, 1193, 1200,
			1207, 300, 1281, 500, 1295, 800, 1311, 1200, 1325, 1000, 1339, 800,
			1353, 500, 1365, 1000, 1424, 800, 863, 60, 1067, 500, 1081, 500,
			1101, 800, 1115, 1000, 1137, 300, 1153, 400, 1175, 350, 1191, 450,
			1203, 150, 1279, 200, 1293, 250, 1309, 500, 1323, 200, 1335, 100,
			1363, 200, 1420, 100, 864, 30, 882, 10, 1075, 200, 1087, 200, 1103,
			200, 1117, 80, 1139, 100, 115, 120, 1173, 150, 1189, 180, 1205, 50,
			1277, 70, 1291, 80, 1307, 150, 1321, 70, 1337, 80, 1375, 90, 1422,
			60, 18349, 75000000, 18351, 75000000, 18355, 75000000, 18353,
			75000000, 18357, 75000000, 18359, 50000000, 18361, 50000000, 18363,
			50000000, 18335, 750000, 14484, 30000000, 11694, 50000000, 11720,
			25000000, 11718, 15000000, 11722, 22000000, 11696, 15000000, 11724,
			30000000, 11726, 30000000, 11698, 25000000, 11700, 18000000, 11730,
			10000000, 4151, 3000000, 21369, 10000000, 11235, 5000000, 15486,
			5000000, 13738, 75000000, 13740, 650000000, 13742, 400000000,
			13744, 40000000, 13858, 500000, 13861, 700000, 13864, 600000,
			13869, 500000, 13876, 600000, 13887, 700000, 13890, 500000, 13893,
			50000000, 13844, 70000000, 13899, 75000000, 13899, 75000000, 13905,
			80000000, 20135, 200000000, 20139, 400000000, 20143, 300000000,
			20147, 150000000, 20151, 300000000, 20155, 250000000, 20159,
			100000000, 20163, 200000000, 20167, 150000000, 20771, 500000 };

	private static GeneralStore instance = new GeneralStore();

	public static GeneralStore getInstance() {
		return instance;
	}

	public void handleSell(Player client, int removeID, int removeSlot,
			int amount, Shop shop) {

		if (client.playerItems[removeSlot] == 0)
			return;
		removeID = client.playerItems[removeSlot] - 1;
		int addToShopID = removeID;
		/**
		 * Converts noted items in unnoted
		 */
		if (Item.itemIsNote[removeID])
			addToShopID = removeID - 1;
		final ItemDefinition def = InstanceDistributor.getItemManager()
				.getItemDefinition(addToShopID);
		/**
		 * checks if the item is known
		 */
		if (def == null)
			return;
		if (addToShopID == 995) {
			client.getActionSender().sendMessage(
					"You cannot sell gold for gold.");
			return;
		}
		if (TradingConstants.isUntradable(addToShopID)) {
			client.getActionSender().sendMessage(
					"You cannot sell untradeable items.");
			return;
		}
		boolean isInShop = false;
		for (final Map.Entry<Integer, Item> entry : shop.getMap().entrySet())
			if (entry.getValue().getId() == addToShopID)
				isInShop = true;
		/**
		 * Checks if the shop is not full
		 */
		boolean full = false;
		if (!(shop.getFreeSlot() > 0) && shop.getItemSlot(addToShopID) == -1)
			full = true;
		/**
		 * Checks if the player really has the item amount
		 */
		final int has = client.getActionAssistant().getItemAmount(removeID);
		if (amount > has) {
			client.getActionSender().sendMessage(
					"You don't have this many to sell.");
			return;
		}
		final int price = itemPrice(addToShopID);
		if (price == 0) {
			client.getActionSender()
					.sendMessage(
							"This item has a unknown price, please report to a developer.");
			return;
		}
		client.getActionAssistant().deleteItem(removeID,
				client.getActionAssistant().getItemSlot(removeID), amount);
		/**
		 * Checks if the price of the item is known
		 */

		final int sellPrice = (int) (price * amount * 0.5);
		client.getActionSender().addItem(995, sellPrice);
		if (!full) {
			if (!isInShop) {
				// shop.addItem(new ShopItem(addToShopID, amount, amount,
				// buyPrice, false, false));
			} else {
				// int slot = shop.getItemSlot(addToShopID);
				// shop.getItemBySlot(slot).setAmount(
				// shop.getItemBySlot(slot).getCount() + amount);
			}
			shop.updated();
		}
		client.getActionSender().sendItemReset(3823);

	}

	public int itemPrice(int itemId) {
		if (itemId <= 0)
			return 0;
		if (Item.itemIsNote[itemId])
			itemId = itemId - 1;
		int price = 0;
		price = lookUpPrice(itemId);

		if (price <= 1)
			if (InstanceDistributor.getItemManager().getItemDefinition(itemId) != null)
				price = (int) InstanceDistributor.getItemManager()
						.getItemDefinition(itemId).getShopValue();
		if (price <= 1)
			if (InstanceDistributor.getItemManager().getItemDefinition(itemId) != null)
				if (InstanceDistributor.getItemManager()
						.getItemDefinition(itemId).getName().contains("Guthan")
						|| InstanceDistributor.getItemManager()
								.getItemDefinition(itemId).getName()
								.contains("Ahrim")
						|| InstanceDistributor.getItemManager()
								.getItemDefinition(itemId).getName()
								.contains("Dharok")
						|| InstanceDistributor.getItemManager()
								.getItemDefinition(itemId).getName()
								.contains("Verac")
						|| InstanceDistributor.getItemManager()
								.getItemDefinition(itemId).getName()
								.contains("Torag")
						|| InstanceDistributor.getItemManager()
								.getItemDefinition(itemId).getName()
								.contains("Karil"))
					return 1000000;
		if (itemId == 13736)
			price = 800000;
		return price;
	}

	private int lookUpPrice(int itemId) {
		int price = 0;

		for (int i = 0; i < prices.length; i = i + 2)
			if (prices[i] == itemId) {
				price = prices[i + 1];

				break;
			}
		return price;
	}
}
