package com.server2.content.skills.farming;

import java.util.HashMap;
import java.util.Map;

import com.server2.InstanceDistributor;
import com.server2.model.Item;
import com.server2.model.entity.player.Player;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 23/02/12 Time: 12:12 To change
 * this template use File | Settings | File Templates.
 */
public class ToolLeprechaun {

	public enum ToolStoreData {

		RAKE(0, 5341, 1, 1, 15596, 15597, "Rake"), SEED_DIBBER(1, 5343, 2, 1,
				15598, 15599, "Dibber"), SPADE(2, 952, 4, 1, 15600, 15601,
				"Spade"), SECATEURS(3, 5329, 8, 1, 15602, 15603, "Secateurs"), MAGIC_SECATEURS(
				4, 7409, 8, 1, 15602, 15603, "Secateurs"), WATERING_CAN_0(5,
				5331, 16, 1, 15604, 15605, "Watering Can"), WATERING_CAN_1(6,
				5333, 32, 1, 15604, 15605, "Watering Can"), WATERING_CAN_2(7,
				5334, 48, 1, 15604, 15605, "Watering Can"), WATERING_CAN_3(8,
				5335, 64, 1, 15604, 15605, "Watering Can"), WATERING_CAN_4(9,
				5336, 80, 1, 15604, 15605, "Watering Can"), WATERING_CAN_5(10,
				5337, 96, 1, 15604, 15605, "Watering Can"), WATERING_CAN_6(11,
				5338, 112, 1, 15604, 15605, "Watering Can"), WATERING_CAN_7(12,
				5339, 128, 1, 15604, 15605, "Watering Can"), WATERING_CAN_8(13,
				5340, 144, 1, 15604, 15605, "Watering Can"), GARDENING_TROWEL(
				14, 5325, 256, 1, 15606, 15607, "Trowel"), EMPTY_BUCKETS(15,
				1925, 512, 31, 15608, 15609, "Buckets"), COMPOST(16, 6032,
				16384, 255, 15610, 15611, "Compost"), SUPER_COMPOST(17, 6034,
				4194304, 255, 15612, 15613, "Super Compost");

		private int toolIndex;
		private int toolId;
		private int toolConfig;
		private int toolMaxQuantity;
		private int toolFrameId;
		private int toolCountFrameId;
		private String toolName;

		private static Map<Integer, ToolStoreData> tools = new HashMap<Integer, ToolStoreData>();
		private static Map<Integer, ToolStoreData> indexes = new HashMap<Integer, ToolStoreData>();

		static {
			for (final ToolStoreData data : ToolStoreData.values()) {
				tools.put(data.toolId, data);
				indexes.put(data.toolIndex, data);
			}
		}

		public static ToolStoreData forId(int toolId) {
			return tools.get(toolId);
		}

		public static ToolStoreData forIndex(int index) {
			return indexes.get(index);
		}

		ToolStoreData(int toolIndex, int toolId, int toolConfig,
				int toolMaxQuantity, int toolFrameId, int toolCountFrameId,
				String toolName) {
			this.toolIndex = toolIndex;
			this.toolId = toolId;
			this.toolConfig = toolConfig;
			this.toolMaxQuantity = toolMaxQuantity;
			this.toolFrameId = toolFrameId;
			this.toolCountFrameId = toolCountFrameId;
			this.toolName = toolName;
		}

		public int getToolConfig() {
			return toolConfig;
		}

		public int getToolCountFrameId() {
			return toolCountFrameId;
		}

		public int getToolFrameId() {
			return toolFrameId;
		}

		public int getToolId() {
			return toolId;
		}

		public int getToolIndex() {
			return toolIndex;
		}

		public int getToolMaxQuantity() {
			return toolMaxQuantity;
		}

		public String getToolName() {
			return toolName;
		}
	}

	private final Player player;

	public int[] tools = { 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1 };// new
																					// int[18];

	public Item[] storeItems = { new Item(5341), new Item(5343), new Item(952),
			new Item(5329), new Item(5331), new Item(5325) };

	public Item[] storeItems2 = { new Item(1925), new Item(6032),
			new Item(6034) };

	/* setting up the store item array and the player item array */

	public Item[] storeItemsPlayer = { new Item(5341), new Item(5343),
			new Item(952), new Item(5329), new Item(5331), new Item(5325) };
	public Item[] storeItems2Player = { new Item(1925), new Item(6032),
			new Item(6034) };
	public static final int[] NOTABLE_ITEMS = { 225, 239, 247, 249, 251, 253,
			255, 257, 259, 261, 263, 265, 267, 269, 3000, 2481, 592, 1965,
			1967, 6004, 5980, 5976, 1955, 1963, 2108, 5972, 2114, 754, 2126,
			248, 1951, 240, 2367, 1942, 1957, 1965, 1982, 5986, 5504, 5982,
			6006, 5994, 5996, 5931, 5998, 6000, 6002, 6016, 6055 };
	public static final int LEPRECHAUN_INTERFACE = 15614;

	/* setting up the main constant field */

	public static final int LEPRECHAUN_INTERFACE_CONTAINER = 15682;

	public static final int LEPRECHAUN_INTERFACE_CONTAINER2 = 15683;
	public static final int PLAYER_INTERFACE = 15593;
	public static final int PLAYER_INTERFACE_CONTAINER = 15594;
	public static final int PLAYER_INTERFACE_CONTAINER2 = 15595;
	public static final int TOOL_CONFIGS = 615;
	public static final int TOOL_LEPRECHAUN = 3021;

	public ToolLeprechaun(Player player) {
		this.player = player;
	}

	public void checkWateringCanQuantity() {
		int counter = 0;
		int counter2 = 0;
		for (int i = 5; i <= 13; i++) {
			final ToolStoreData toolStoreData = ToolStoreData.forIndex(i);
			if (player.getActionAssistant().playerHasItem(
					toolStoreData.getToolId(), 1))
				counter2++;
			if (tools[i] == 1)
				counter++;
		}
		if (counter == 0)
			storeItems[4] = new Item(5331);
		if (counter2 == 0)
			storeItemsPlayer[4] = new Item(5331);

	}

	/* this enum store every tool data for the interfaces loading */

	public int[] getTools() {
		return tools;
	}

	/* loading the interfaces */

	public void handleAdditionalTools() {
		int item;
		int i = 5340;
		while (!player.getActionAssistant().playerHasItem(i, 1) && i >= 5330)
			i--;
		item = i;
		if (item == 5330)
			return;
		storeItemsPlayer[4] = new Item(item);

		if (player.getActionAssistant().playerHasItem(7409, 1))
			storeItemsPlayer[3] = new Item(7409);
		else
			storeItemsPlayer[3] = new Item(5329);

	}

	/* handling watering can things */

	public boolean hasWateringCanInStore() {
		int counter = 0;
		for (int i = 5; i <= 13; i++)
			if (tools[i] == 1)
				counter++;
		if (counter == 0)
			return false;
		return true;

	}

	public void loadInterfaces() {
		player.getActionSender().sendInterface(LEPRECHAUN_INTERFACE);
		player.getActionSender().sendUpdateItems(
				LEPRECHAUN_INTERFACE_CONTAINER, storeItems);
		player.getActionSender().sendUpdateItems(
				LEPRECHAUN_INTERFACE_CONTAINER2, storeItems2);
		player.getActionSender().sendSidebar(3, PLAYER_INTERFACE);
		player.getActionSender().sendUpdateItems(PLAYER_INTERFACE_CONTAINER,
				storeItemsPlayer);
		player.getActionSender().sendUpdateItems(PLAYER_INTERFACE_CONTAINER2,
				storeItems2Player);
		player.openedFarmingStore = true;
		updateStore();
	}

	public boolean noteItem(int itemId) {
		if (Item.itemIsNote[itemId]) {
			player.getActionSender().sendMessage("That is a banknote!");
			return true;
		}
		for (final int item : NOTABLE_ITEMS)
			if (itemId == item) {
				final int count = player.getActionAssistant().getItemCount(
						itemId);
				for (int i = 0; i < count; i++)
					player.getActionAssistant().deleteItem(itemId, 1);
				player.getActionSender().addItem(itemId + 1, count);
				player.getActionSender().sendMessage(
						"The tool leprechaun notes those items for you.");
				return true;
			}
		player.getActionSender().sendMessage("You cannot exchange this.");
		return true;
	}

	/* updating the store state and player state */

	public void setTools(int i, int tools) {
		this.tools[i] = tools;
	}

	public void storeItems(int itemId, int amount) {
		final ToolStoreData toolStoreData = ToolStoreData.forId(itemId);
		if (toolStoreData == null)
			return;

		final int storeAmount = tools[toolStoreData.getToolIndex()];
		int finalAmount = amount;
		if (!player.getActionAssistant().playerHasItem(itemId, 1)) {
			player.getActionSender().sendMessage(
					"You don't have a "
							+ InstanceDistributor.getItemManager()
									.getItemDefinition(itemId).getName()
									.toLowerCase() + ".");
			return;
		}
		if (toolStoreData.getToolMaxQuantity() == storeAmount
				|| (itemId == 7409 || itemId == 5329)
				&& (tools[3] == 1 || tools[4] == 1) || hasWateringCanInStore()
				&& toolStoreData.getToolId() != 5332 && itemId >= 5340
				&& itemId <= 5331) {
			player.getActionSender().sendMessage(
					"You can't store any more of those.");
			return;
		}
		if (player.getActionAssistant().getItemCount(itemId) <= 0) {
			player.getActionSender().sendMessage(
					"You aren't carrying any of those.");
			return;
		}
		if (player.getActionAssistant().getItemCount(itemId) < amount)
			finalAmount = player.getActionAssistant().getItemCount(itemId);

		player.getActionAssistant().deleteItem(itemId, finalAmount);
		tools[toolStoreData.getToolIndex()] += finalAmount;
		updateStore();

	}

	/* store any item with id and amount provided */

	public void updatePlayerInterface(ToolStoreData toolStoreData, int count,
			int index) {
		player.tempBoolean = false;
		if (count > 0) {
			if (index >= 5 && index <= 13)
				player.tempBoolean = true;
			player.getActionSender().sendString(
					"@gre@" + toolStoreData.getToolName(),
					toolStoreData.getToolFrameId());
			player.getActionSender().sendString("@gre@" + count,
					toolStoreData.getToolCountFrameId());
		} else {
			// watering can doses
			if (index >= 5 && index <= 13 && player.tempBoolean)
				return;
			// secateurs
			if ((index == 3 || index == 4)
					&& (player.getActionAssistant().playerHasItem(7409, 1) || player
							.getActionAssistant().playerHasItem(5329, 1)))
				return;

			player.getActionSender().sendString(
					"" + toolStoreData.getToolName(),
					toolStoreData.getToolFrameId());
			player.getActionSender().sendString("" + count,
					toolStoreData.getToolCountFrameId());

		}
	}

	/* withdraw any item with item id and amount provided */

	public void updateStore() {
		int configValue = 0;
		for (int i = 0; i < tools.length; i++) {
			final ToolStoreData toolStoreData = ToolStoreData.forIndex(i);
			if (toolStoreData == null)
				return;
			configValue += toolStoreData.getToolConfig() * tools[i];
			updatePlayerInterface(toolStoreData, player.getActionAssistant()
					.getItemCount(toolStoreData.getToolId()), i);
			if (toolStoreData.getToolId() != 5332
					&& toolStoreData.getToolId() >= 5331
					&& toolStoreData.getToolId() <= 5340 && tools[i] == 1)
				storeItems[4] = new Item(toolStoreData.getToolId());
			if (toolStoreData.getToolId() == 7409 && tools[i] == 1)
				storeItems[3] = new Item(7409);

		}
		handleAdditionalTools();
		checkWateringCanQuantity();
		player.getActionSender().sendConfig(TOOL_CONFIGS, configValue);
		player.getActionSender().sendUpdateItems(
				LEPRECHAUN_INTERFACE_CONTAINER, storeItems);
		player.getActionSender().sendUpdateItems(PLAYER_INTERFACE_CONTAINER,
				storeItemsPlayer);

	}

	/* note any item with the item id provided */

	public void withdrawItems(int itemId, int amount) {
		final ToolStoreData toolStoreData = ToolStoreData.forId(itemId);
		if (toolStoreData == null)
			return;

		if (player.getActionAssistant().freeSlots() <= 0) {
			player.getActionSender().sendMessage(
					"Not enough space in your inventory.");
			return;
		}
		if (tools[toolStoreData.getToolIndex()] <= 0) {
			player.getActionSender().sendMessage(
					"You haven't got any of those stored in here.");
			return;
		}
		int finalAmount;
		if (amount > tools[toolStoreData.getToolIndex()])
			finalAmount = tools[toolStoreData.getToolIndex()];
		else
			finalAmount = amount;
		if (finalAmount > player.getActionAssistant().freeSlots())
			finalAmount = player.getActionAssistant().freeSlots();

		tools[toolStoreData.getToolIndex()] -= finalAmount;
		player.getActionSender().addItem(itemId, finalAmount);
		updateStore();
	}

}
