package com.server2.model.combat.additions;

import com.server2.InstanceDistributor;
import com.server2.content.TradingConstants;
import com.server2.content.misc.GeneralStore;
import com.server2.model.Item;
import com.server2.model.entity.player.Player;
import com.server2.net.GamePacketBuilder;
import com.server2.world.GroundItemManager;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class ItemProtect {

	public static final int[] protectedList = { 6570, 10498, 10499, 9747, 9748,
			9753, 9754, 9750, 9751, 9768, 9769, 9756, 9757, 9759, 9760, 9762,
			9763, 9801, 9802, 9807, 9808, 9783, 9784, 9798, 9799, 9804, 9805,
			9780, 9781, 9795, 9796, 19111, 9792, 9793, 9774, 9775, 9771, 9772,
			9777, 9778, 9786, 9787, 9810, 9811, 9765, 9766, 9789, 9790, 9948,
			9949, 9813, 9813, 10551, 10548, };

	/**
	 * Untradables to let re-appear
	 */
	public static final int[] specialItems = { 19669, 20771, 20769, 10551,
			18363, 18359, 18361, 18347, 18335, 18349, 18351, 18353, 18355,
			18357, 19111, 20072, 7455, 7456, 7457, 7458, 7459, 7460, 7461,
			7462, 6570, 8850, 8849, 8848, 8847, 8846, 8845, 8844 };

	public static int[] dropItems(Player client, boolean dropItems) {
		if (!client.inWilderness()) {
			for (int i = 0; i < client.playerItems.length; i++)
				if (client.playerItems[i] - 1 != client.itemKeptId[0]
						&& client.playerItems[i] - 1 != client.itemKeptId[1]
						&& client.playerItems[i] - 1 != client.itemKeptId[2]
						&& client.playerItems[i] - 1 != client.itemKeptId[3]
						&& client.playerItems[i] - 1 > 0)
					if (InstanceDistributor.getItemManager().getItemDefinition(
							client.playerItems[i] - 1) != null)
						GroundItemManager.getInstance().createGroundItem(
								client,
								new Item(client.playerItems[i] - 1,
										client.playerItemsN[i]),
								client.getPosition());
			for (int i = 0; i < client.playerEquipment.length; i++)
				if (client.playerEquipment[i] != client.itemKeptId[0]
						&& client.playerEquipment[i] != client.itemKeptId[1]
						&& client.playerEquipment[i] != client.itemKeptId[2]
						&& client.playerEquipment[i] != client.itemKeptId[3]
						&& client.playerEquipment[i] > 0)
					if (InstanceDistributor.getItemManager().getItemDefinition(
							client.playerEquipment[i]) != null)
						GroundItemManager.getInstance().createGroundItem(
								client,
								new Item(client.playerEquipment[i],
										client.playerEquipmentN[i]),
								client.getPosition());
			removeAllItems(client);
			return new int[0];
		}

		int coinsToDrop = 0;
		for (int i = 0; i < client.playerItems.length; i++) {
			if (client.playerItems[i] - 1 != client.itemKeptId[0]
					&& client.playerItems[i] - 1 != client.itemKeptId[1]
					&& client.playerItems[i] - 1 != client.itemKeptId[2]
					&& client.playerItems[i] - 1 != client.itemKeptId[3]
					&& !onProtectedList(client.playerItems[i] - 1)
					&& client.playerItems[i] - 1 > 0
					&& !isSpecial(client.playerItems[i] - 1)) {
				if (TradingConstants.isUntradable(client.playerItems[i] - 1)
						&& !isSpecial(client.playerItems[i] - 1)) {
					coinsToDrop += GeneralStore.getInstance().itemPrice(
							client.playerItems[i] - 1);
					continue;
				}
				if (InstanceDistributor.getItemManager().getItemDefinition(
						client.playerItems[i] - 1) != null) {
					if (client.playerItems[i] - 1 == 995) {
						coinsToDrop += client.playerItemsN[i] - 1;
						continue;
					}
					final Player killer = client.getKiller();
					GroundItemManager.getInstance().createGroundItem(
							killer == null ? client : killer,
							new Item(client.playerItems[i] - 1,
									client.playerItemsN[i]),
							client.getPosition());
				}
			}
			if (isSpecial(client.playerItems[i] - 1)
					&& client.playerItems[i] - 1 != client.itemKeptId[0]
					&& client.playerItems[i] - 1 != client.itemKeptId[1]
					&& client.playerItems[i] - 1 != client.itemKeptId[2]
					&& client.playerItems[i] - 1 != client.itemKeptId[3])
				GroundItemManager.getInstance().createGroundItem(
						client,
						new Item(client.playerItems[i] - 1,
								client.playerItemsN[i]), client.getPosition());
		}
		for (int i = 0; i < client.playerEquipment.length; i++) {
			if (client.playerEquipment[i] != client.itemKeptId[0]
					&& client.playerEquipment[i] != client.itemKeptId[1]
					&& client.playerEquipment[i] != client.itemKeptId[2]
					&& client.playerEquipment[i] != client.itemKeptId[3]
					&& !onProtectedList(client.playerEquipment[i])
					&& client.playerEquipment[i] > 0
					&& !isSpecial(client.playerEquipment[i])) {
				if (TradingConstants.isUntradable(client.playerEquipment[i])
						&& !isSpecial(client.playerEquipment[i])) {
					coinsToDrop += GeneralStore.getInstance().itemPrice(
							client.playerEquipment[i]);
					continue;
				}
				if (client.playerEquipment[i] == 995) {
					coinsToDrop += client.playerEquipmentN[i];
					continue;
				}
				if (InstanceDistributor.getItemManager().getItemDefinition(
						client.playerEquipment[i]) != null) {
					final Player killer = client.getKiller();
					GroundItemManager.getInstance().createGroundItem(
							killer == null ? client : killer,
							new Item(client.playerEquipment[i],
									client.playerEquipmentN[i]),
							client.getPosition());
				}
			}
			if (isSpecial(client.playerEquipment[i])
					&& client.playerEquipment[i] != client.itemKeptId[0]
					&& client.playerEquipment[i] != client.itemKeptId[1]
					&& client.playerEquipment[i] != client.itemKeptId[2]
					&& client.playerEquipment[i] != client.itemKeptId[3])
				GroundItemManager.getInstance().createGroundItem(
						client,
						new Item(client.playerEquipment[i],
								client.playerEquipmentN[i]),
						client.getPosition());
		}
		if (coinsToDrop > 0 && client.getKiller() != null) {
			final Player killer = client.getKiller();
			if (killer != null)
				GroundItemManager.getInstance().createGroundItem(killer,
						new Item(995, coinsToDrop), client.getPosition());
			else
				GroundItemManager.getInstance().createGroundItem(client,
						new Item(995, coinsToDrop), client.getPosition());
		}
		removeAllItems(client);
		return new int[0];
	}

	/**
	 * Is this a special item?
	 */
	public static boolean isSpecial(int itemID) {
		for (final int daShit : specialItems)
			if (daShit == itemID)
				return true;
		return false;
	}

	public static boolean onProtectedList(int itemId) {
		for (final int list : protectedList)
			if (list == itemId)
				return true;
		return false;
	}

	public static void removeAllItems(Player client) {
		for (int i = 0; i < 28; i++) {
			client.playerItemsN[i] = 0;
			client.playerItems[i] = 0;
		}
		client.getActionSender().sendItemReset(3214);

		for (int i = 0; i < 14; i++) {
			client.playerEquipment[i] = -1;
			client.playerEquipmentN[i] = 0;
			final GamePacketBuilder bldr = new GamePacketBuilder(34);
			bldr.putShort(6);
			bldr.putShort(1688);
			bldr.put((byte) i);
			bldr.putShort(0);
			bldr.put((byte) 0);
			client.write(bldr.toPacket());
		}
		client.updateRequired = true;
		client.appearanceUpdateRequired = true;
		client.getBonuses().calculateBonus();
		client.getEquipment().sendWeapon();
	}
}
