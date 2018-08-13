package com.server2.content.skills;

import com.server2.InstanceDistributor;
import com.server2.content.skills.harvesting.Harvest;
import com.server2.content.skills.harvesting.Harvest.Resource;
import com.server2.content.skills.harvesting.rock.impl.Adamantite;
import com.server2.content.skills.harvesting.rock.impl.Adamantite2;
import com.server2.content.skills.harvesting.rock.impl.Coal;
import com.server2.content.skills.harvesting.rock.impl.Coal2;
import com.server2.content.skills.harvesting.rock.impl.Copper;
import com.server2.content.skills.harvesting.rock.impl.Copper2;
import com.server2.content.skills.harvesting.rock.impl.Essence;
import com.server2.content.skills.harvesting.rock.impl.Gold;
import com.server2.content.skills.harvesting.rock.impl.Gold2;
import com.server2.content.skills.harvesting.rock.impl.Iron;
import com.server2.content.skills.harvesting.rock.impl.Iron2;
import com.server2.content.skills.harvesting.rock.impl.Mithril;
import com.server2.content.skills.harvesting.rock.impl.Mithril2;
import com.server2.content.skills.harvesting.rock.impl.Runite;
import com.server2.content.skills.harvesting.rock.impl.Runite2;
import com.server2.content.skills.harvesting.rock.impl.Silver;
import com.server2.content.skills.harvesting.rock.impl.Silver2;
import com.server2.content.skills.harvesting.rock.impl.Tin;
import com.server2.content.skills.harvesting.rock.impl.Tin2;
import com.server2.content.skills.harvesting.tree.impl.DeadTree;
import com.server2.content.skills.harvesting.tree.impl.Evergreen;
import com.server2.content.skills.harvesting.tree.impl.LeaningWillow;
import com.server2.content.skills.harvesting.tree.impl.LeaningWillow2;
import com.server2.content.skills.harvesting.tree.impl.LeaningWillow3;
import com.server2.content.skills.harvesting.tree.impl.Magic;
import com.server2.content.skills.harvesting.tree.impl.Maple;
import com.server2.content.skills.harvesting.tree.impl.NormalTree;
import com.server2.content.skills.harvesting.tree.impl.Oak;
import com.server2.content.skills.harvesting.tree.impl.SmallTree;
import com.server2.content.skills.harvesting.tree.impl.Willow;
import com.server2.content.skills.harvesting.tree.impl.Yew;
import com.server2.model.Item;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;

/**
 * 
 * @author Faris
 */
public class Skill {
	/**
	 * The constant skill ID's for each different skill
	 */
	public static final int ATTACK = 0, DEFENCE = 1, STRENGTH = 2,
			HITPOINTS = 3, RANGED = 4, PRAYER = 5, MAGIC = 6, COOKING = 7,
			WOODCUTTING = 8, FLETCHING = 9, FISHING = 10, FIREMAKING = 11,
			CRAFTING = 12, SMITHING = 13, MINING = 14, HERBLORE = 15,
			AGILITY = 16, THIEVING = 17, SLAYER = 18, FARMING = 19,
			RUNECRAFTING = 20, SUMMONING = 21, HUNTER = 22, CONSTRUCTION = 23,
			DUNGEONEERING = 24;

	public static final boolean CAN_HARVEST = true, CANNOT_HARVEST = false;

	/**
	 * Goes through all known resource implementations and returns the defined
	 * object associated with the implementation
	 * 
	 * @param objectId
	 * @return
	 */
	public static Resource getResourceForId(int objectId) {
		switch (objectId) {
		case 1286:
			return new DeadTree();
		case 1316:
			return new Evergreen();
		case 5553:
			return new LeaningWillow3();
		case 5552:
			return new LeaningWillow();
		case 5551:
			return new LeaningWillow2();
		case 1306:
			return new Magic();
		case 1307:
			return new Maple();
		case 1276:
			return new NormalTree();
		case 1281:
			return new Oak();
		case 1278:
			return new SmallTree();
		case 1308:
			return new Willow();
		case 1309:
			return new Yew();
		case 2491:
			return new Essence();
		case 2099:
			return new Gold2();
		case 2098:
			return new Gold();
		case 2101:
			return new Silver2();
		case 2100:
			return new Silver();
		case 2106:
			return new Runite2();
		case 2107:
			return new Runite();
		case 2104:
			return new Adamantite2();
		case 2105:
			return new Adamantite();
		case 2091:
			return new Copper();
		case 2090:
			return new Copper2();
		case 2095:
			return new Tin();
		case 2094:
			return new Tin2();
		case 2093:
			return new Iron();
		case 2092:
			return new Iron2();
		case 2103:
			return new Mithril2();
		case 2102:
			return new Mithril();
		case 2097:
			return new Coal();
		case 2096:
			return new Coal2();
		}
		return null;
	}

	/**
	 * Begins the harvesting action for harvesting skills
	 * 
	 * @param harvest
	 *            is the type of action to be undertaken
	 * @param client
	 *            is the player undertaking the action
	 * @param resoruce
	 *            is what is being harvested
	 */
	public static void harvest(Harvest harvest, Player client, Resource resoruce) {
		harvest.init(client, resoruce);
	}

	/**
	 * Awards the client with the given amount of experience set
	 * 
	 * @param client
	 * @param exp
	 * @param skillId
	 */
	public void awardExp(Player client, int exp, int skillId) {
		client.getActionAssistant().addSkillXP(
				exp * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER, skillId);
	}

	/**
	 * Awards the client with the item given, after checking for inventory slots
	 * sends a post receive message to alert the user
	 * 
	 * @param client
	 * @param itemId
	 * @param amount
	 */
	public void awardItem(Player client, int itemId, int amount, boolean message) {
		if (!hasInventorySpace(client))
			return;
		client.getActionSender().addItem(itemId, amount);
		if (message)
			client.getActionSender().sendMessage(
					"You get some "
							+ InstanceDistributor.getItemManager()
									.getItemDefinition(itemId).getName() + ".");
	}

	/**
	 * Deletes an item from a client
	 * 
	 * @param client
	 */
	public void deleteItem(Player client, Item item, boolean message) {
		client.getActionAssistant().deleteItem(item.getId(), item.getCount());
		if (message)
			if (InstanceDistributor.getItemManager().getItemDefinition(
					item.getId()) != null)
				client.getActionSender().sendMessage(
						"You consume the "
								+ InstanceDistributor.getItemManager()
										.getItemDefinition(item.getId())
										.getName());
	}

	/**
	 * Returns the clients skill level for the parsed skill id
	 * 
	 * @param client
	 * @param skillId
	 * @return
	 */
	public int getSkillLevel(Player client, int skillId) {
		return client.playerLevel[skillId];
	}

	/**
	 * Checks if the client has enough inventory space to receive one item
	 * 
	 * @param client
	 * @return
	 */
	public boolean hasInventorySpace(Player client) {
		if (client.getActionAssistant().freeSlots() < 1) {
			client.getDM().sendPlayerChat1(
					"There is not enough space in my inventory.");
			if (client.nDialogue > 0)
				client.nDialogue = -1;
			return false;
		}
		return true;
	}

	/**
	 * Checks if the players equipment or inventory contains the given item, if
	 * not displays a dialogue alerting the user
	 * 
	 * @param client
	 * @param itemId
	 * @return
	 */
	public boolean playerHasItem(Player client, int itemId) {
		if (!client.getActionAssistant().isItemInBag(itemId)
				&& !client.getActionAssistant().playerEquipContains(itemId)) {
			client.getDM().sendPlayerChat1(
					"I do not have the required equipment for this action.");
			if (client.nDialogue > 0)
				client.nDialogue = -1;
			return false;
		}
		return true;
	}

	/**
	 * Checks if the client's level in the given skill exceeds the given level
	 * requirement and displays a dialogue if not
	 * 
	 * @param client
	 * @param skillId
	 * @param levelRequirement
	 * @return
	 */
	public boolean playerHasRequiredLevel(Player client, int skillId,
			int levelRequirement) {
		if (getSkillLevel(client, skillId) < levelRequirement) {
			client.getDM().sendPlayerChat1(
					"I do not have the required level for this action.");
			if (client.nDialogue > 0)
				client.nDialogue = -1;
			return false;
		}
		return true;
	}

	/**
	 * Sends a message to the client
	 * 
	 * @param client
	 * @param message
	 */
	public void sendMessage(Player client, String message) {
		client.getActionSender().sendMessage(message);
	}

}
