package com.server2.model.entity.player;

import java.util.Map;

import com.server2.InstanceDistributor;
import com.server2.content.Achievements;
import com.server2.content.anticheat.BankCheating;
import com.server2.content.misc.pets.PetHandler;
import com.server2.model.Item;
import com.server2.model.ItemDefinition;
import com.server2.model.Shop;
import com.server2.model.ShopItem;

/**
 * A bunch of methods to assist with containers.
 * 
 * @author Graham
 */
public class ContainerAssistant {

	public static ContainerAssistant INSTANCE = new ContainerAssistant();

	public static int[] capes = { 9747, 9750, 9753, 9756, 9759, 9762, 9765,
			9768, 9771, 9774, 9777, 9780, 9783, 9786, 9789, 9792, 9795, 9798,
			9801, 9804, 9807, 9810, 9948, 12169, 18508, 20771, 9948 };

	public static ContainerAssistant getInstance() {
		return INSTANCE;
	}

	public void bankAll(Player client) {

		for (int i = 0; i < client.playerItems.length; i++) {
			ContainerAssistant.getInstance().bankAllItem(client,
					client.playerItems[i], i, client.playerItemsN[i]);

			client.playerItems[i] = 0;
			client.playerItemsN[i] = 0;

		}

		client.getActionSender().sendItemReset();
		client.getActionSender().sendItemReset(7423);
		client.getActionSender().sendItemReset(5064);
		client.getActionSender().sendBankReset();

	};

	public void bankAllEquipment(Player client) {
		if (System.currentTimeMillis() - client.lastEquipmentCheck > 1000) {
			if (!BankCheating.getInstance().inBank(client)) {
				client.getActionSender().sendMessage("You are not in a bank.");
				return;
			}
			for (int i = 0; i < client.playerEquipment.length; i++) {
				if (client.playerEquipment[i] <= 0
						|| client.playerEquipmentN[i] <= 0)
					continue;
				boolean skip = false;
				if (client.getActionAssistant().playerBankHasItem(
						client.playerEquipment[i], 1)) {
					client.bankItemsN[client
							.getActionAssistant()
							.playerBankHasItemSlot(client.playerEquipment[i], 1)] += client.playerEquipmentN[i];
					skip = true;
				}
				if (!skip) {
					final int slot = client.getNextAvailableBankSlot();
					client.bankItems[slot] = client.playerEquipment[i] + 1;
					client.bankItemsN[slot] = client.playerEquipmentN[i];
				}
				client.getEquipment().deleteEquipment(i);
			}
			client.getActionSender().sendReplacementOfTempItem();
			client.getActionSender().sendBankReset();
			client.getEquipment().sendWeapon();
			client.getActionSender().sendAnimationReset();
			client.lastEquipmentCheck = System.currentTimeMillis();
		}
	}

	public boolean bankAllItem(Player client, int itemID, int fromSlot,
			int amount) {
		if (!BankCheating.getInstance().inBank(client)) {
			client.getActionSender().sendMessage("You are not in a bank.");
			return false;
		}
		if (fromSlot < 0 || fromSlot - 1 < 0)
			return false;
		if (client.playerItemsN[fromSlot] <= 0)
			return false;

		if (!Item.itemIsNote[client.playerItems[fromSlot] - 1]) {
			if (client.playerItems[fromSlot] <= 0)
				return false;
			if (Item.itemStackable[client.playerItems[fromSlot] - 1]
					|| client.playerItemsN[fromSlot] > 1) {
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < client.getPlayerBankSize(); i++)
					if (client.bankItems[i] == client.playerItems[fromSlot]) {
						if (client.playerItemsN[fromSlot] < amount)
							amount = client.playerItemsN[fromSlot];
						alreadyInBank = true;
						toBankSlot = i;
						i = client.getPlayerBankSize() + 1;
					}

				if (!alreadyInBank
						&& client.getActionAssistant().freeBankSlots() > 0) {
					for (int i = 0; i < client.getPlayerBankSize(); i++)
						if (client.bankItems[i] <= 0) {
							toBankSlot = i;
							i = client.getPlayerBankSize() + 1;
						}
					client.bankItems[toBankSlot] = client.playerItems[fromSlot];
					if (client.playerItemsN[fromSlot] < amount)
						amount = client.playerItemsN[fromSlot];
					if (client.bankItemsN[toBankSlot] + amount <= PlayerConstants.MAX_ITEM_AMOUNT
							&& client.bankItemsN[toBankSlot] + amount > -1)
						client.bankItemsN[toBankSlot] += amount;
					else {
						client.getActionSender().sendMessage("Bank full!");
						return false;
					}
					/*
					 * client.getActionAssistant().deleteItem(
					 * (client.playerItems[fromSlot] - 1), fromSlot, amount);
					 */
					// client.getActionSender().sendReplacementOfTempItem();
					// client.getActionSender().sendBankReset();
					return true;
				} else if (alreadyInBank) {
					if (client.bankItemsN[toBankSlot] + amount <= PlayerConstants.MAX_ITEM_AMOUNT
							&& client.bankItemsN[toBankSlot] + amount > -1)
						client.bankItemsN[toBankSlot] += amount;
					else {
						client.getActionSender().sendMessage("Bank full!");
						return false;
					}
					/*
					 * client.getActionAssistant().deleteItem(
					 * (client.playerItems[fromSlot] - 1), fromSlot, amount);
					 */
					// client.getActionSender().sendReplacementOfTempItem();
					// client.getActionSender().sendBankReset();
					return true;
				} else {
					client.getActionSender().sendMessage("Bank full!");
					return false;
				}
			} else {
				itemID = client.playerItems[fromSlot];
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < client.getPlayerBankSize(); i++)
					if (client.bankItems[i] == client.playerItems[fromSlot]) {
						alreadyInBank = true;
						toBankSlot = i;
						i = client.getPlayerBankSize() + 1;
					}
				if (!alreadyInBank
						&& client.getActionAssistant().freeBankSlots() > 0) {
					for (int i = 0; i < client.getPlayerBankSize(); i++)
						if (client.bankItems[i] <= 0) {
							toBankSlot = i;
							i = client.getPlayerBankSize() + 1;
						}
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < client.playerItems.length; i++)
							if (client.playerItems[i] == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						if (itemExists) {
							client.bankItems[toBankSlot] = client.playerItems[firstPossibleSlot];
							client.bankItemsN[toBankSlot] += 1;
							/*
							 * client .getActionAssistant() .deleteItem(
							 * (client.playerItems[firstPossibleSlot] - 1),
							 * firstPossibleSlot, 1);
							 */
							amount--;
						} else
							amount = 0;
					}
					// client.getActionSender().sendReplacementOfTempItem();
					// client.getActionSender().sendBankReset();
					return true;
				} else if (alreadyInBank) {
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < client.playerItems.length; i++)
							if (client.playerItems[i] == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						if (itemExists) {
							client.bankItemsN[toBankSlot] += 1;
							/*
							 * client .getActionAssistant() .deleteItem(
							 * (client.playerItems[firstPossibleSlot] - 1),
							 * firstPossibleSlot, 1);
							 */
							amount--;
						} else
							amount = 0;
					}
					// client.getActionSender().sendReplacementOfTempItem();
					// client.getActionSender().sendBankReset();
					return true;
				} else {
					client.getActionSender().sendMessage("Bank full!");
					return false;
				}
			}
		} else if (Item.itemIsNote[client.playerItems[fromSlot] - 1]
				&& !Item.itemIsNote[client.playerItems[fromSlot] - 2]) {
			if (client.playerItems[fromSlot] <= 0)
				return false;
			if (Item.itemStackable[client.playerItems[fromSlot] - 1]
					|| client.playerItemsN[fromSlot] > 1) {
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < client.getPlayerBankSize(); i++)
					if (client.bankItems[i] == client.playerItems[fromSlot] - 1) {
						if (client.playerItemsN[fromSlot] < amount)
							amount = client.playerItemsN[fromSlot];
						alreadyInBank = true;
						toBankSlot = i;
						i = client.getPlayerBankSize() + 1;
					}

				if (!alreadyInBank
						&& client.getActionAssistant().freeBankSlots() > 0) {
					for (int i = 0; i < client.getPlayerBankSize(); i++)
						if (client.bankItems[i] <= 0) {
							toBankSlot = i;
							i = client.getPlayerBankSize() + 1;
						}
					client.bankItems[toBankSlot] = client.playerItems[fromSlot] - 1;
					if (client.playerItemsN[fromSlot] < amount)
						amount = client.playerItemsN[fromSlot];
					if (client.bankItemsN[toBankSlot] + amount <= PlayerConstants.MAX_ITEM_AMOUNT
							&& client.bankItemsN[toBankSlot] + amount > -1)
						client.bankItemsN[toBankSlot] += amount;
					else
						return false;
					/*
					 * client.getActionAssistant().deleteItem(
					 * (client.playerItems[fromSlot] - 1), fromSlot, amount);
					 */
					// client.getActionSender().sendReplacementOfTempItem();
					// client.getActionSender().sendBankReset();
					return true;
				} else if (alreadyInBank) {
					if (client.bankItemsN[toBankSlot] + amount <= PlayerConstants.MAX_ITEM_AMOUNT
							&& client.bankItemsN[toBankSlot] + amount > -1)
						client.bankItemsN[toBankSlot] += amount;
					else
						return false;
					/*
					 * client.getActionAssistant().deleteItem(
					 * (client.playerItems[fromSlot] - 1), fromSlot, amount);
					 */
					client.getActionSender().sendReplacementOfTempItem();
					client.getActionSender().sendBankReset();
					return true;
				} else {
					client.getActionSender().sendMessage("Bank full!");
					return false;
				}
			} else {
				itemID = client.playerItems[fromSlot];
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < client.getPlayerBankSize(); i++)
					if (client.bankItems[i] == client.playerItems[fromSlot] - 1) {
						alreadyInBank = true;
						toBankSlot = i;
						i = client.getPlayerBankSize() + 1;
					}
				if (!alreadyInBank
						&& client.getActionAssistant().freeBankSlots() > 0) {
					for (int i = 0; i < client.getPlayerBankSize(); i++)
						if (client.bankItems[i] <= 0) {
							toBankSlot = i;
							i = client.getPlayerBankSize() + 1;
						}
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < client.playerItems.length; i++)
							if (client.playerItems[i] == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						if (itemExists) {
							client.bankItems[toBankSlot] = client.playerItems[firstPossibleSlot] - 1;
							client.bankItemsN[toBankSlot] += 1;
							/*
							 * client .getActionAssistant() .deleteItem(
							 * (client.playerItems[firstPossibleSlot] - 1),
							 * firstPossibleSlot, 1);
							 */
							amount--;
						} else
							amount = 0;
					}
					// client.getActionSender().sendReplacementOfTempItem();
					// client.getActionSender().sendBankReset();
					return true;
				} else if (alreadyInBank) {
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < client.playerItems.length; i++)
							if (client.playerItems[i] == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						if (itemExists) {
							client.bankItemsN[toBankSlot] += 1;
							/*
							 * client .getActionAssistant() .deleteItem(
							 * (client.playerItems[firstPossibleSlot] - 1),
							 * firstPossibleSlot, 1);
							 */
							amount--;
						} else
							amount = 0;
					}
					// client.getActionSender().sendReplacementOfTempItem();
					// client.getActionSender().sendBankReset();
					return true;
				} else {
					client.getActionSender().sendMessage("Bank full!");
					return false;
				}
			}
		} else {
			client.getActionSender().sendMessage(
					"Item not supported " + (client.playerItems[fromSlot] - 1));
			return false;
		}
	}

	public boolean bankItem(Player client, int itemID, int fromSlot, int amount) {
		try {
			if (!client.freeBank)
				if (!BankCheating.getInstance().inBank(client)
						|| !client.canBank && client.getPrivileges() != 3
						&& PetHandler.isItemPet(itemID)) {
					client.getActionSender().sendMessage(
							"You are not in a bank.");
					return false;
				}
			client.freeBank = false;
			if (itemID < 0 || itemID > Item.itemStackable.length)
				return false;

			if (client.playerItemsN[fromSlot] <= 0)
				return false;
			if (itemID == 7407) {
				itemID = 7159;
				client.playerItems[fromSlot] = 7159;
			}
			if (!Item.itemIsNote[client.playerItems[fromSlot] - 1]) {
				if (client.playerItems[fromSlot] <= 0)
					return false;
				if (Item.itemStackable[client.playerItems[fromSlot] - 1]
						|| client.playerItemsN[fromSlot] > 1) {
					int toBankSlot = 0;
					boolean alreadyInBank = false;
					for (int i = 0; i < client.getPlayerBankSize(); i++)
						if (client.bankItems[i] == client.playerItems[fromSlot]) {
							if (client.playerItemsN[fromSlot] < amount)
								amount = client.playerItemsN[fromSlot];
							alreadyInBank = true;
							toBankSlot = i;
							i = client.getPlayerBankSize() + 1;
						}
					if (!alreadyInBank)
						if (client.getActionAssistant().freeBankSlots1() < 1) {
							client.getActionSender().sendMessage(
									"Bank full1! (350 spots)");
							return false;
						}
					if (!alreadyInBank
							&& client.getActionAssistant().freeBankSlots() > 0) {

						for (int i = 0; i < client.getPlayerBankSize(); i++)
							if (client.bankItems[i] <= 0) {
								toBankSlot = i;
								i = client.getPlayerBankSize() + 1;
							}
						client.bankItems[toBankSlot] = client.playerItems[fromSlot];
						if (client.playerItemsN[fromSlot] < amount)
							amount = client.playerItemsN[fromSlot];
						if (client.bankItemsN[toBankSlot] + amount <= PlayerConstants.MAX_ITEM_AMOUNT
								&& client.bankItemsN[toBankSlot] + amount > -1)
							client.bankItemsN[toBankSlot] += amount;
						else {
							client.getActionSender().sendMessage(
									"Bank full! (more than 2147m of a item)");
							return false;
						}
						client.getActionAssistant().deleteItem(
								client.playerItems[fromSlot] - 1, fromSlot,
								amount);
						client.getActionSender().sendReplacementOfTempItem();
						client.getActionSender().sendBankReset();
						return true;
					} else if (alreadyInBank) {
						if (client.bankItemsN[toBankSlot] + amount <= PlayerConstants.MAX_ITEM_AMOUNT
								&& client.bankItemsN[toBankSlot] + amount > -1)
							client.bankItemsN[toBankSlot] += amount;
						else {
							client.getActionSender().sendMessage(
									"Bank full! (more than 2147m of a item)");
							return false;
						}
						client.getActionAssistant().deleteItem(
								client.playerItems[fromSlot] - 1, fromSlot,
								amount);
						client.getActionSender().sendReplacementOfTempItem();
						client.getActionSender().sendBankReset();
						return true;
					} else {
						client.getActionSender().sendMessage("Bank full!");
						return false;
					}
				} else {
					itemID = client.playerItems[fromSlot];
					int toBankSlot = 0;
					boolean alreadyInBank = false;
					for (int i = 0; i < client.getPlayerBankSize(); i++)
						if (client.bankItems[i] == client.playerItems[fromSlot]) {
							alreadyInBank = true;
							toBankSlot = i;
							i = client.getPlayerBankSize() + 1;
						}
					if (!alreadyInBank
							&& client.getActionAssistant().freeBankSlots() > 0) {
						for (int i = 0; i < client.getPlayerBankSize(); i++)
							if (client.bankItems[i] <= 0) {
								toBankSlot = i;
								i = client.getPlayerBankSize() + 1;
							}
						int firstPossibleSlot = 0;
						boolean itemExists = false;
						while (amount > 0) {
							itemExists = false;
							for (int i = firstPossibleSlot; i < client.playerItems.length; i++)
								if (client.playerItems[i] == itemID) {
									firstPossibleSlot = i;
									itemExists = true;
									i = 30;
								}
							if (itemExists) {
								client.bankItems[toBankSlot] = client.playerItems[firstPossibleSlot];
								client.bankItemsN[toBankSlot] += 1;
								client.getActionAssistant()
										.deleteItem(
												client.playerItems[firstPossibleSlot] - 1,
												firstPossibleSlot, 1);
								amount--;
							} else
								amount = 0;
						}
						client.getActionSender().sendReplacementOfTempItem();
						client.getActionSender().sendBankReset();
						return true;
					} else if (alreadyInBank) {
						int firstPossibleSlot = 0;
						boolean itemExists = false;
						while (amount > 0) {
							itemExists = false;
							for (int i = firstPossibleSlot; i < client.playerItems.length; i++)
								if (client.playerItems[i] == itemID) {
									firstPossibleSlot = i;
									itemExists = true;
									i = 30;
								}
							if (itemExists) {
								client.bankItemsN[toBankSlot] += 1;
								client.getActionAssistant()
										.deleteItem(
												client.playerItems[firstPossibleSlot] - 1,
												firstPossibleSlot, 1);
								amount--;
							} else
								amount = 0;
						}
						client.getActionSender().sendReplacementOfTempItem();
						client.getActionSender().sendBankReset();
						return true;
					} else {
						client.getActionSender().sendMessage("Bank full!");
						return false;
					}
				}
			} else if (Item.itemIsNote[client.playerItems[fromSlot] - 1]
					&& !Item.itemIsNote[client.playerItems[fromSlot] - 2]) {
				if (client.playerItems[fromSlot] <= 0)
					return false;
				if (Item.itemStackable[client.playerItems[fromSlot] - 1]
						|| client.playerItemsN[fromSlot] > 1) {
					int toBankSlot = 0;
					boolean alreadyInBank = false;
					for (int i = 0; i < client.getPlayerBankSize(); i++)
						if (client.bankItems[i] == client.playerItems[fromSlot] - 1) {
							if (client.playerItemsN[fromSlot] < amount)
								amount = client.playerItemsN[fromSlot];
							alreadyInBank = true;
							toBankSlot = i;
							i = client.getPlayerBankSize() + 1;
						}

					if (!alreadyInBank
							&& client.getActionAssistant().freeBankSlots() > 0) {
						for (int i = 0; i < client.getPlayerBankSize(); i++)
							if (client.bankItems[i] <= 0) {
								toBankSlot = i;
								i = client.getPlayerBankSize() + 1;
							}
						client.bankItems[toBankSlot] = client.playerItems[fromSlot] - 1;
						if (client.playerItemsN[fromSlot] < amount)
							amount = client.playerItemsN[fromSlot];
						if (client.bankItemsN[toBankSlot] + amount <= PlayerConstants.MAX_ITEM_AMOUNT
								&& client.bankItemsN[toBankSlot] + amount > -1)
							client.bankItemsN[toBankSlot] += amount;
						else
							return false;
						client.getActionAssistant().deleteItem(
								client.playerItems[fromSlot] - 1, fromSlot,
								amount);
						client.getActionSender().sendReplacementOfTempItem();
						client.getActionSender().sendBankReset();
						return true;
					} else if (alreadyInBank) {
						if (client.bankItemsN[toBankSlot] + amount <= PlayerConstants.MAX_ITEM_AMOUNT
								&& client.bankItemsN[toBankSlot] + amount > -1)
							client.bankItemsN[toBankSlot] += amount;
						else
							return false;
						client.getActionAssistant().deleteItem(
								client.playerItems[fromSlot] - 1, fromSlot,
								amount);
						client.getActionSender().sendReplacementOfTempItem();
						client.getActionSender().sendBankReset();
						return true;
					} else {
						client.getActionSender().sendMessage("Bank full!");
						return false;
					}
				} else {
					itemID = client.playerItems[fromSlot];
					int toBankSlot = 0;
					boolean alreadyInBank = false;
					for (int i = 0; i < client.getPlayerBankSize(); i++)
						if (client.bankItems[i] == client.playerItems[fromSlot] - 1) {
							alreadyInBank = true;
							toBankSlot = i;
							i = client.getPlayerBankSize() + 1;
						}
					if (!alreadyInBank
							&& client.getActionAssistant().freeBankSlots() > 0) {
						for (int i = 0; i < client.getPlayerBankSize(); i++)
							if (client.bankItems[i] <= 0) {
								toBankSlot = i;
								i = client.getPlayerBankSize() + 1;
							}
						int firstPossibleSlot = 0;
						boolean itemExists = false;
						while (amount > 0) {
							itemExists = false;
							for (int i = firstPossibleSlot; i < client.playerItems.length; i++)
								if (client.playerItems[i] == itemID) {
									firstPossibleSlot = i;
									itemExists = true;
									i = 30;
								}
							if (itemExists) {
								client.bankItems[toBankSlot] = client.playerItems[firstPossibleSlot] - 1;
								client.bankItemsN[toBankSlot] += 1;
								client.getActionAssistant()
										.deleteItem(
												client.playerItems[firstPossibleSlot] - 1,
												firstPossibleSlot, 1);
								amount--;
							} else
								amount = 0;
						}
						client.getActionSender().sendReplacementOfTempItem();
						client.getActionSender().sendBankReset();
						return true;
					} else if (alreadyInBank) {
						int firstPossibleSlot = 0;
						boolean itemExists = false;
						while (amount > 0) {
							itemExists = false;
							for (int i = firstPossibleSlot; i < client.playerItems.length; i++)
								if (client.playerItems[i] == itemID) {
									firstPossibleSlot = i;
									itemExists = true;
									i = 30;
								}
							if (itemExists) {
								client.bankItemsN[toBankSlot] += 1;
								client.getActionAssistant()
										.deleteItem(
												client.playerItems[firstPossibleSlot] - 1,
												firstPossibleSlot, 1);
								amount--;
							} else
								amount = 0;
						}
						client.getActionSender().sendReplacementOfTempItem();
						client.getActionSender().sendBankReset();
						return true;
					} else {
						client.getActionSender().sendMessage("Bank full!");
						return false;
					}
				}
			} else {
				client.getActionSender().sendMessage(
						"Item not supported "
								+ (client.playerItems[fromSlot] - 1));
				return false;
			}
		} catch (final IndexOutOfBoundsException ex) {
			return false;
		}
	}

	public void buyItem(Player client, int removeID, int removeSlot, int amount) {
		if (client.getUsername().equalsIgnoreCase("Jordan")) {
			client.getActionSender().sendMessage(
					"You cannot do this, motherbeetch.");
			return;
		}
		String coinName = "n";
		int coinSlot = 0;

		if (client.getExtraData().containsKey("shop")) {
			final int id = (Integer) client.getExtraData().get("shop");
			final Shop s = InstanceDistributor.getShopManager().getShops()
					.get(id);
			if (s == null)
				return;
			if (s.getCurrency() != 123 && s.getCurrency() != 125
					&& s.getCurrency() != 127 && s.getCurrency() != 128
					&& s.getCurrency() != 10 && s.getCurrency() != 200) {
				coinSlot = client.getActionAssistant().getItemSlot(
						s.getCurrency());

				coinName = InstanceDistributor.getItemManager()
						.getItemDefinition(s.getCurrency()).getName();
			}
			if (Item.itemStackable[removeID]) {
				if (client.getActionAssistant().freeSlots() < 1) {
					client.getActionSender().sendMessage(
							"You don't have enough inventory space");
					return;
				}
			} else if (client.getActionAssistant().freeSlots() < amount) {

			}
			if (s.getCurrency() == 123)
				coinName = "Slayer points";
			if (s.getCurrency() == 125)
				coinName = "Voting points";
			if (s.getCurrency() == 127)
				coinName = "Pest Control points";
			if (s.getCurrency() == 128)
				coinName = "Tokens";
			if (s.getCurrency() == 200)
				coinName = "Pk-Tokens";
			if (s.getCurrency() == 10)
				coinName = "Duo Slayer Points";

			if (coinSlot == -1) {
				client.getActionSender().sendMessage(
						"You don't have enough " + coinName.toLowerCase());
				return;
			}

			if (s.getItemBySlot(removeSlot) == null)
				return;

			removeID = s.getItemBySlot(removeSlot).getId();
			final ShopItem item1 = (ShopItem) s.getItemBySlot(removeSlot);
			if (amount > 0 && s.getItemBySlot(removeSlot).getId() == removeID) {
				if (amount > s.getItemBySlot(removeSlot).getCount()
						&& !item1.getIsInf())
					amount = s.getItemBySlot(removeSlot).getCount();
				if (removeID != s.getItemBySlot(removeSlot).getId()) {
					client.getActionSender().sendMessage("No.");
					return;
				}
				for (int i = 0; i < amount; i++) {

					final int totalPrice = s.getItemBuyValue(removeID);
					int theAmountPlayerHas = 0;
					if (s.getCurrency() != 123 && s.getCurrency() != 125
							&& s.getCurrency() != 127 && s.getCurrency() != 128
							&& s.getCurrency() != 10 && s.getCurrency() != 200)
						theAmountPlayerHas = client.playerItemsN[coinSlot];
					else if (s.getCurrency() == 123)
						theAmountPlayerHas = client.slayerPoints;
					else if (s.getCurrency() == 125)
						theAmountPlayerHas = client.votePoints;
					else if (s.getCurrency() == 127)
						theAmountPlayerHas = client
								.getPestControlCommedations();
					else if (s.getCurrency() == 128)
						theAmountPlayerHas = client.dungTokens;
					else if (s.getCurrency() == 10)
						theAmountPlayerHas = client.duoPoints;
					else if (s.getCurrency() == 200)
						theAmountPlayerHas = client.pkPoints;

					if (removeID == 20771)
						if (client.completionist() == false) {
							client.getActionSender()
									.sendMessage(
											"You need all skills 99 and 120 dungeoneering to buy this cape.");
							client.getActionSender()
									.sendMessage(
											"You also need to have completed all of the quests.");
							return;
						}
					if (level(client, removeID) < 99 && s.getId() == 29
							&& removeID != 19709 && removeID != 20771) {
						client.getActionSender()
								.sendMessage(
										"You need atleast 99 of that skill to buy this cape");
						return;
					} else if (removeID == 19709
							&& client.getDungLevelForXp(client.playerXP[23]) < 120) {
						client.getActionSender()
								.sendMessage(
										"You need atleast 120 dungeoneering to buy this cape");
						return;
					} else if (theAmountPlayerHas >= totalPrice) {
						if (s.getId() != 29) {
							if (client.getActionAssistant().freeSlots() > 0) {
								if (s.getCurrency() != 123
										&& s.getCurrency() != 125
										&& s.getCurrency() != 127
										&& s.getCurrency() != 128
										&& s.getCurrency() != 10
										&& s.getCurrency() != 200) {
									client.getActionAssistant().deleteItem(
											s.getCurrency(), totalPrice);
									if (s.getCurrency() == 6529) {
										Achievements.getInstance().complete(
												client, 42);
										if (removeID == 6585
												|| removeID == 6586
												|| removeID == 6584)
											Achievements.getInstance()
													.complete(client, 93);
									}

								} else if (s.getCurrency() == 123) {
									theAmountPlayerHas = client.slayerPoints;
									client.slayerPoints = client.slayerPoints
											- totalPrice;
									client.getActionSender().sendString(
											"@whi@Slayer points : "
													+ client.slayerPoints,
											16038);
								} else if (s.getCurrency() == 125) {
									theAmountPlayerHas = client.votePoints;
									client.votePoints = client.votePoints
											- totalPrice;
									client.getActionSender().sendString(
											"@whi@Voting Points : "
													+ client.votePoints, 16037);
								} else if (s.getCurrency() == 127) {
									theAmountPlayerHas = client
											.getPestControlCommedations();
									client.setPestControlCommedations(client
											.getPestControlCommedations()
											- totalPrice);

								} else if (s.getCurrency() == 128) {
									theAmountPlayerHas = client.dungTokens;
									client.dungTokens = client.dungTokens
											- totalPrice;
									client.getActionSender().sendString(
											"@whi@Tokens : "
													+ client.dungTokens, 16032);
								} else if (s.getCurrency() == 10) {
									theAmountPlayerHas = client.duoPoints;
									client.duoPoints = client.duoPoints
											- totalPrice;
								} else if (s.getCurrency() == 200) {
									theAmountPlayerHas = client.pkPoints;
									client.pkPoints = client.pkPoints
											- totalPrice;
								}
								client.getActionSender().addItem(removeID, 1);
								final ShopItem item = (ShopItem) s
										.getItemBySlot(removeSlot);
								if (!item.getIsInf()) {
									item.setAmount(item.getCount() - 1);
									item.setLastAutomaticStockChange(System
											.currentTimeMillis());
									if (item.getCount() == 0
											&& !item.isAlwaysStocked()) {
										s.removeItem(removeSlot);
										break;
									}
								}
							} else {
								client.getActionSender().sendMessage(
										"Not enough space in your inventory.");
								break;
							}
						} else if (client.getActionAssistant().freeSlots() > 1) {

							client.getActionSender().addItem(removeID, 1);
							if (removeID != 19709 && removeID != 20771)
								client.getActionSender().addItem(removeID + 2,
										1);
							else if (removeID == 20771)
								client.getActionSender().addItem(removeID + 1,
										1);
							if (removeID == 20771)
								client.getActionAssistant().deleteItem(995,
										2496000);
							else
								client.getActionAssistant().deleteItem(995,
										99000);
							final ShopItem item = (ShopItem) s
									.getItemBySlot(removeSlot);
							if (!item.getIsInf()) {
								item.setAmount(item.getCount() - 1);
								item.setLastAutomaticStockChange(System
										.currentTimeMillis());
								if (item.getCount() == 0
										&& !item.isAlwaysStocked()) {
									s.removeItem(removeSlot);
									break;
								}
							}
						} else {
							client.getActionSender().sendMessage(
									"Not enough space in your inventory.");
							break;
						}

					} else {
						client.getActionSender().sendMessage(
								"You don't have enough "
										+ coinName.toLowerCase());
						break;
					}

				}
			}

		}
		client.getActionSender().sendItemReset(3823);
	}

	public void fromBank(Player client, int itemID, int fromSlot, int amount) {
		if (!BankCheating.getInstance().inBank(client) || !client.canBank) {
			client.getActionSender().sendMessage("You are not in a bank.");
			return;
		}
		if (amount > 0)
			if (client.bankItems[fromSlot] > 0)
				if (!client.takeAsNote) {
					if (Item.itemStackable[client.bankItems[fromSlot] - 1]) {
						if (client.bankItemsN[fromSlot] > amount) {
							if (client.getActionSender().addItem(
									client.bankItems[fromSlot] - 1, amount)) {
								client.bankItemsN[fromSlot] -= amount;
								client.getActionSender().sendBankReset();
								client.getActionSender().sendItemReset(5064);
							}
						} else if (client.getActionSender().addItem(
								client.bankItems[fromSlot] - 1,
								client.bankItemsN[fromSlot])) {
							client.bankItems[fromSlot] = 0;
							client.bankItemsN[fromSlot] = 0;
							client.getActionSender().sendBankReset();
							client.getActionSender().sendItemReset(5064);
						}
					} else {
						while (amount > 0)
							if (client.bankItemsN[fromSlot] > 0) {
								if (client.getActionSender().addItem(
										client.bankItems[fromSlot] - 1, 1)) {
									client.bankItemsN[fromSlot] += -1;
									amount--;
								} else
									amount = 0;
							} else
								amount = 0;
						client.getActionSender().sendBankReset();
						client.getActionSender().sendItemReset(5064);
					}
				} else if (client.takeAsNote
						&& Item.itemIsNote[client.bankItems[fromSlot]]) {
					if (client.bankItemsN[fromSlot] > amount) {
						if (client.getActionSender().addItem(
								client.bankItems[fromSlot], amount)) {
							client.bankItemsN[fromSlot] -= amount;
							client.getActionSender().sendBankReset();
							client.getActionSender().sendItemReset(5064);
						}
					} else if (client.getActionSender().addItem(
							client.bankItems[fromSlot],
							client.bankItemsN[fromSlot])) {
						client.bankItems[fromSlot] = 0;
						client.bankItemsN[fromSlot] = 0;
						client.getActionSender().sendBankReset();
						client.getActionSender().sendItemReset(5064);
					}
				} else {
					client.getActionSender().sendMessage(
							"Item can't be drawn as note.");
					if (Item.itemStackable[client.bankItems[fromSlot] - 1]) {
						if (client.bankItemsN[fromSlot] > amount) {
							if (client.getActionSender().addItem(
									client.bankItems[fromSlot] - 1, amount)) {
								client.bankItemsN[fromSlot] -= amount;
								client.getActionSender().sendBankReset();
								client.getActionSender().sendItemReset(5064);
							}
						} else if (client.getActionSender().addItem(
								client.bankItems[fromSlot] - 1,
								client.bankItemsN[fromSlot])) {
							client.bankItems[fromSlot] = 0;
							client.bankItemsN[fromSlot] = 0;
							client.getActionSender().sendBankReset();
							client.getActionSender().sendItemReset(5064);
						}
					} else {
						while (amount > 0)
							if (client.bankItemsN[fromSlot] > 0) {
								if (client.getActionSender().addItem(
										client.bankItems[fromSlot] - 1, 1)) {
									client.bankItemsN[fromSlot] += -1;
									amount--;
								} else
									amount = 0;
							} else
								amount = 0;
						client.getActionSender().sendBankReset();
						client.getActionSender().sendItemReset(5064);
					}
				}
		if (client.bankItems[fromSlot] == 0)
			rearrangeBank(client, fromSlot);
	}

	public int level(Player client, int id) {
		int lvl = 0;
		int theOne = 0;
		for (int i = 0; i < capes.length; i++)
			if (id == capes[i])
				theOne = i;
		if (theOne == 0)
			lvl = client.playerLevel[0];
		else if (theOne == 1)
			lvl = client.playerLevel[2];
		else if (theOne == 2)
			lvl = client.playerLevel[1];
		else if (theOne == 3)
			lvl = client.playerLevel[4];
		else if (theOne == 4)
			lvl = client.playerLevel[5];
		else if (theOne == 5)
			lvl = client.playerLevel[6];
		else if (theOne == 6)
			lvl = client.playerLevel[20];
		else if (theOne == 7)
			lvl = client.playerLevel[3];
		else if (theOne == 8)
			lvl = client.playerLevel[16];
		else if (theOne == 9)
			lvl = client.playerLevel[15];
		else if (theOne == 10)
			lvl = client.playerLevel[17];
		else if (theOne == 11)
			lvl = client.playerLevel[12];
		else if (theOne == 12)
			lvl = client.playerLevel[9];
		else if (theOne == 13)
			lvl = client.playerLevel[18];
		else if (theOne == 14)
			lvl = client.playerLevel[22];
		else if (theOne == 15)
			lvl = client.playerLevel[14];
		else if (theOne == 16)
			lvl = client.playerLevel[13];
		else if (theOne == 17)
			lvl = client.playerLevel[10];
		else if (theOne == 18)
			lvl = client.playerLevel[7];
		else if (theOne == 19)
			lvl = client.playerLevel[11];
		else if (theOne == 20)
			lvl = client.playerLevel[8];
		else if (theOne == 21)
			lvl = client.playerLevel[19];
		else if (theOne == 22)
			lvl = client.playerLevel[24];
		else if (theOne == 23)
			lvl = client.playerLevel[21];
		else if (theOne == 24)
			lvl = client.getDungLevelForXp(client.playerXP[23]);
		else if (id == 9948)
			lvl = client.playerLevel[22];
		return lvl;

	}

	public void moveItems(Player client, int from, int to, int moveWindow,
			boolean insertMode) {
		if (moveWindow == 3214) {
			int tempI;
			int tempN;
			tempI = client.playerItems[from];
			tempN = client.playerItemsN[from];

			client.playerItems[from] = client.playerItems[to];
			client.playerItemsN[from] = client.playerItemsN[to];
			client.playerItems[to] = tempI;
			client.playerItemsN[to] = tempN;
			client.getActionSender().sendItemReset(3214);
		}
		if (moveWindow == 5382 && from >= 0 && to >= 0
				&& from < client.getPlayerBankSize()
				&& to < client.getPlayerBankSize())
			if (insertMode) {
				int tempFrom = from;
				for (final int tempTo = to; tempFrom != tempTo;)
					if (tempFrom > tempTo) {
						swapBankItem(client, tempFrom, tempFrom - 1);
						tempFrom--;
					} else if (tempFrom < tempTo) {
						swapBankItem(client, tempFrom, tempFrom + 1);
						tempFrom++;
					}
			} else
				swapBankItem(client, from, to);
		if (moveWindow == 5382)
			client.getActionSender().sendBankReset();
		if (moveWindow == 5064) {
			int tempI;
			int tempN;
			tempI = client.playerItems[from];
			tempN = client.playerItemsN[from];

			client.playerItems[from] = client.playerItems[to];
			client.playerItemsN[from] = client.playerItemsN[to];
			client.playerItems[to] = tempI;
			client.playerItemsN[to] = tempN;
			client.getActionSender().sendItemReset(3214);
		}
	}

	/**
	 * Re arranges the bank if needed.
	 * 
	 * @param client
	 * @param fromSlot
	 */
	public void rearrangeBank(Player client, int fromSlot) {
		int bankLength = 0;
		for (final int bankItem : client.bankItems)
			if (bankItem > 0)
				bankLength++;
		for (int i = fromSlot; i < bankLength; i++) {
			client.bankItems[i] = client.bankItems[i + 1];
			client.bankItemsN[i] = client.bankItemsN[i + 1];
			client.bankItems[i + 1] = 0;
			client.bankItemsN[i + 1] = 0;
		}

		client.getActionSender().sendBankReset();
		client.getActionSender().sendItemReset(5064);
	}

	public void sellItem(Player client, int removeID, int removeSlot, int amount) {
		if (client.getUsername().equalsIgnoreCase("Jordan")) {
			client.getActionSender().sendMessage(
					"You cannot do this, motherbeetch.");
			return;
		}
		if (removeID == 5304)
			client.getActionSender()
					.sendMessage("You can't sell torstol seeds");

		if (client.playerItems[removeSlot] == 0)
			return;
		removeID = client.playerItems[removeSlot] - 1;
		int addToShopID = removeID;
		if (Item.itemIsNote[removeID]) {
			final int unnoted = InstanceDistributor.getItemManager()
					.getUnnotedItem(removeID);
			if (unnoted != -1)
				addToShopID = unnoted;
		}
		if (client.getExtraData().containsKey("shop")) {
			final int id = (Integer) client.getExtraData().get("shop");
			final Shop s = InstanceDistributor.getShopManager().getShops()
					.get(id);
			if (s == null)
				return;
			if (s.getType() == Shop.Type.GENERAL) {
				InstanceDistributor.getGeneralStore().handleSell(client,
						removeID, removeSlot, amount, s);
				return;
			}
			final ItemDefinition def = InstanceDistributor.getItemManager()
					.getItemDefinition(addToShopID);
			if (def == null)
				return; // could not find item definition.
			if (s.getType() == Shop.Type.SPECIALIST) {
				boolean ok = false;
				for (final Map.Entry<Integer, Item> entry : s.getMap()
						.entrySet())
					if (entry.getValue().getId() == addToShopID)
						ok = true;
				if (s.getCurrency() == 123 || s.getCurrency() == 6529
						|| s.getCurrency() == 125 || s.getCurrency() == 127
						|| s.getCurrency() == 128 || s.getCurrency() == 10
						|| s.getCurrency() == 200) {
					client.getActionSender().sendMessage(
							"You cannot sell items to this store");
					return;
				}
				if (!ok) {
					client.getActionSender().sendMessage(
							"You cannot sell " + def.getName().toLowerCase()
									+ " in this store.");
					return;
				}

				if (!(s.getFreeSlot() > 0) && s.getItemSlot(addToShopID) == -1) {
					// TODO real message
					client.getActionSender().sendMessage("The shop is full!");
					return;
				}
				if (s.getItemSellValue(addToShopID) > 250000) {
					client.getActionSender().sendMessage(
							"You cannot sell this item to the shop.");
					return;
				}
				final int has = client.getActionAssistant().getItemAmount(
						removeID);
				if (amount > has)
					amount = has;
				for (int i = 0; i < amount; i++) {
					final int totalPrice = s.getItemSellValue(addToShopID);

					if (client.getActionAssistant().freeSlots() > 0
							|| client.getActionAssistant().isItemInBag(
									s.getCurrency())) {
						client.getActionAssistant().deleteItem(
								removeID,
								client.getActionAssistant().getItemSlot(
										removeID), 1);

						client.getActionSender().addItem(s.getCurrency(),
								totalPrice);
						final int slot = s.getItemSlot(addToShopID);
						if (!((ShopItem) s.getItemBySlot(slot)).getIsInf())
							if (slot != -1) {
								s.getItemBySlot(slot).setAmount(
										s.getItemBySlot(slot).getCount() + 1);

								((ShopItem) s.getItemBySlot(slot))
										.setLastAutomaticStockChange(System
												.currentTimeMillis());
							}
					} else {
						client.getActionSender().sendMessage(
								"Not enough free space in your inventory.");
						break;
					}
				}
				client.getActionSender().sendItemReset(3823);
				s.updated();
			}

		}

	}

	public void swapBankItem(Player client, int from, int to) {
		final int tempI = client.bankItems[from];
		final int tempN = client.bankItemsN[from];
		client.bankItems[from] = client.bankItems[to];
		client.bankItemsN[from] = client.bankItemsN[to];
		client.bankItems[to] = tempI;
		client.bankItemsN[to] = tempN;
	}

}