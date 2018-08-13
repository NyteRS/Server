package com.server2.content.misc;

import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene Roosen & Lukas Pinckers
 * 
 */
public class ItemCreation {
	/**
	 * Instances.
	 */
	public static ItemCreation INSTANCE = new ItemCreation();

	/**
	 * Gets the instance.
	 * 
	 * @return
	 */
	public static ItemCreation getInstance() {
		return INSTANCE;
	}

	/**
	 * Creates a godsword blade.
	 */

	public void createBlade(Player client, int itemId1, int itemId2) {
		if (client.getActionAssistant().playerHasItem(itemId1, 1)
				&& client.getActionAssistant().playerHasItem(itemId2, 1))
			if (itemId1 == 11710 && itemId2 == 11712 || itemId2 == 11710
					&& itemId1 == 11712) {
				client.getActionAssistant().deleteItem(itemId1, 1);
				client.getActionAssistant().deleteItem(itemId2, 1);
				client.getActionSender().addItem(11686, 1);

			} else if (itemId1 == 11686 && itemId2 == 11714 || itemId2 == 11686
					&& itemId1 == 11714) {
				client.getActionAssistant().deleteItem(itemId1, 1);
				client.getActionAssistant().deleteItem(itemId2, 1);
				client.getActionSender().addItem(11690, 1);

			}
	}

	public void createBlessedSpiritShield(Player client, int itemId1,
			int itemId2) {
		if (client.getActionAssistant().playerHasItem(itemId1, 1)
				&& client.getActionAssistant().playerHasItem(itemId2, 1))
			if (itemId1 == 13734 && itemId2 == 13754) {
				client.getActionAssistant().deleteItem(itemId1, 1);
				client.getActionAssistant().deleteItem(itemId2, 1);
				client.getActionSender().addItem(13736, 1);

			} else if (itemId1 == 13734 && itemId2 == 13754) {
				client.getActionAssistant().deleteItem(itemId1, 1);
				client.getActionAssistant().deleteItem(itemId2, 1);
				client.getActionSender().addItem(13736, 1);

			}
	}

	public void createDragonfireShield(Player client, int itemId1, int itemId2) {
		if (client.getActionAssistant().playerHasItem(itemId1, 1)
				&& client.getActionAssistant().playerHasItem(itemId2, 1))
			if (itemId1 == 1540 && itemId2 == 11286 || itemId2 == 11286
					&& itemId1 == 1540) {
				client.getActionAssistant().deleteItem(itemId1, 1);
				client.getActionAssistant().deleteItem(itemId2, 1);
				client.getActionSender().addItem(11283, 1);
			}
	}

	/**
	 * Creates dragon platebody.
	 */
	public void createDragonPlatebody(Player client, int itemId1, int itemId2) {

		if (client.getActionAssistant().playerHasItem(14474, 1)
				&& client.getActionAssistant().playerHasItem(14472, 1)
				&& client.getActionAssistant().playerHasItem(14476, 1))
			if (itemId1 == 14474 || itemId1 == 14476 || itemId1 == 14472
					&& itemId2 == 14474 || itemId2 == 14476 || itemId2 == 14472) {
				client.getActionAssistant().deleteItem(14474, 1);
				client.getActionAssistant().deleteItem(14476, 1);
				client.getActionAssistant().deleteItem(14472, 1);
				client.getActionSender().addItem(14479, 1);
				client.getActionSender().sendMessage(
						"You succesfully create a dragon platebody.");

			}
	}

	/**
	 * Creates godswords
	 * 
	 * @param client
	 * @param itemId1
	 * @param itemId2
	 */
	public void createGodswords(Player client, int itemId1, int itemId2) {
		if (client.getActionAssistant().playerHasItem(itemId1, 1)
				&& client.getActionAssistant().playerHasItem(itemId2, 1))
			if (itemId1 == 11702 && itemId2 == 11690 || itemId1 == 11690
					&& itemId2 == 11702) {
				client.getActionAssistant().deleteItem(itemId1, 1);
				client.getActionAssistant().deleteItem(itemId2, 1);
				client.getActionSender().addItem(11694, 1);

			} else if (itemId1 == 11704 && itemId2 == 11690 || itemId1 == 11690
					&& itemId2 == 11704) {
				client.getActionAssistant().deleteItem(itemId1, 1);
				client.getActionAssistant().deleteItem(itemId2, 1);
				client.getActionSender().addItem(11696, 1);

			} else if (itemId1 == 11706 && itemId2 == 11690 || itemId1 == 11690
					&& itemId2 == 11706) {
				client.getActionAssistant().deleteItem(itemId1, 1);
				client.getActionAssistant().deleteItem(itemId2, 1);
				client.getActionSender().addItem(11698, 1);

			} else if (itemId1 == 11708 && itemId2 == 11690 || itemId1 == 11690
					&& itemId2 == 11708) {
				client.getActionAssistant().deleteItem(itemId1, 1);
				client.getActionAssistant().deleteItem(itemId2, 1);
				client.getActionSender().addItem(11700, 1);

			}

	}

	public void createOramentItems(Player client, int use, int useWith) {
		if (use == 19348 && useWith == 4087 || useWith == 19348 && use == 4087)
			if (client.getActionAssistant().playerHasItem(use, 1)
					&& client.getActionAssistant().playerHasItem(useWith, 1)) {
				client.getActionAssistant().deleteItem(use, 1);
				client.getActionAssistant().deleteItem(useWith, 1);
				client.getActionSender().addItem(19338, 1);
			}
		if (use == 19348 && useWith == 4585 || useWith == 19348 && use == 4585)
			if (client.getActionAssistant().playerHasItem(use, 1)
					&& client.getActionAssistant().playerHasItem(useWith, 1)) {
				client.getActionAssistant().deleteItem(use, 1);
				client.getActionAssistant().deleteItem(useWith, 1);
				client.getActionSender().addItem(19339, 1);
			}
		if (use == 19350 && useWith == 14479 || useWith == 19350
				&& use == 14479)
			if (client.getActionAssistant().playerHasItem(use, 1)
					&& client.getActionAssistant().playerHasItem(useWith, 1)) {
				client.getActionAssistant().deleteItem(use, 1);
				client.getActionAssistant().deleteItem(useWith, 1);
				client.getActionSender().addItem(19337, 1);
			}
		if (use == 19352 && useWith == 1187 || useWith == 19352 && use == 1187)
			if (client.getActionAssistant().playerHasItem(use, 1)
					&& client.getActionAssistant().playerHasItem(useWith, 1)) {
				client.getActionAssistant().deleteItem(use, 1);
				client.getActionAssistant().deleteItem(useWith, 1);
				client.getActionSender().addItem(19340, 1);
			}
		if (use == 19346 && useWith == 11335 || useWith == 19346
				&& use == 11335)
			if (client.getActionAssistant().playerHasItem(use, 1)
					&& client.getActionAssistant().playerHasItem(useWith, 1)) {
				client.getActionAssistant().deleteItem(use, 1);
				client.getActionAssistant().deleteItem(useWith, 1);
				client.getActionSender().addItem(19336, 1);
			}

	}

}
