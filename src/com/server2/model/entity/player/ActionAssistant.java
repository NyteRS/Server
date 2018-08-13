package com.server2.model.entity.player;

import com.server2.InstanceDistributor;
import com.server2.Settings;
import com.server2.content.TradingConstants;
import com.server2.content.misc.MagicAndPraySwitch;
import com.server2.content.misc.homes.config.Keldagrim;
import com.server2.model.Item;
import com.server2.model.Shop;
import com.server2.model.combat.HitExecutor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.net.GamePacketBuilder;
import com.server2.util.Misc;
import com.server2.world.GroundItemManager;
import com.server2.world.World;

// Referenced classes of package com.server2.model.entity.player:
//            Player, ActionSender, PlayerConstants, Equipment

public class ActionAssistant {

	Player client;

	public ActionAssistant(Player client) {
		this.client = client;
	}

	public void addHP(int amount) {
		if (client.hitpoints + amount > client.calculateMaxHP())
			client.hitpoints = client.calculateMaxHP();
		else
			client.hitpoints += amount;
		client.playerLevel[3] = client.hitpoints;
		client.hitpoints = client.playerLevel[3];
		client.getActionAssistant().refreshSkill(3);
	}

	public void addSkillXP(double amount, int skillId) {
		if (client.expLocked == 1)
			return;
		if (amount + client.playerXP[skillId] < 0.0D
				|| client.playerXP[skillId] > 0xbebc200)
			return;
		final int oldLevel = client.getLevelForXP(client.playerXP[skillId]);
		if (Keldagrim.instance.inKeldagrim(client))
			client.playerXP[skillId] += amount * 1.1000000000000001D;
		else if (client.playerEquipment[12] == 15017)
			client.playerXP[skillId] += amount * 2D;
		else
			client.playerXP[skillId] += amount;
		if (oldLevel < client.getLevelForXP(client.playerXP[skillId])) {
			client.playerLevel[skillId] = client
					.getLevelForXP(client.playerXP[skillId]);
			showNewLevel(skillId);
			if (skillId == 3)
				addHP(client.getLevelForXP(client.playerXP[skillId]) - oldLevel);
		}
		if (client.playerXP[skillId] > 0xbebc200)
			client.playerXP[skillId] = 0xbebc200;
		refreshSkill(skillId);
		client.updateRequired = true;
		client.appearanceUpdateRequired = true;
	}

	public void appendHit(Entity attacker,
			com.server2.model.entity.Entity.CombatType type, int damage) {
		if (damage > client.hitpoints)
			damage = client.hitpoints;
		if (!client.hitUpdateRequired) {
			client.hitDiff = damage;
			subtractHp(client.hitDiff);
			client.type = type;
			client.updateRequired = true;
			client.hitUpdateRequired = true;
			client.getActionSender().sendWindowsRemoval();
			client.getActionAssistant().startAnimation(
					Equipment.getDefendAnimation(client));
		} else if (!client.hitUpdateRequired2) {
			client.hitDiff2 = damage;
			subtractHp(client.hitDiff2);
			client.type2 = type;
			client.updateRequired = true;
			client.hitUpdateRequired2 = true;
			client.getActionSender().sendWindowsRemoval();
		} else
			HitExecutor.addNewHit(attacker, client, type, damage, 1);
	}

	public void createPlayerGfx(int id, int delay, boolean tallGfx) {
		if (id > 0) {
			client.gfxID = id;
			client.gfxDelay = tallGfx ? delay + 0x640000 : delay;
			client.updateRequired = true;
			client.graphicsUpdateRequired = true;
		}
	}

	public void createPlayerGfx2(int id, int delay) {
		client.gfxID = id;
		client.gfxDelay = delay;
		client.updateRequired = true;
		client.graphicsUpdateRequired = true;
	}

	public void decreaseStat(int stat, double decrease) {
		if (client.playerLevel[stat] - decrease > 0.0D)
			client.playerLevel[stat] -= decrease;
		else
			client.playerLevel[stat] = 0;
		refreshSkill(stat);
	}

	public void decreaseStat2(int stat, int decrease) {
		final int actualLevel = client.getLevelForXP(client.playerXP[stat]);
		double decreaseBy = Misc.intToPercentage(decrease) * actualLevel;
		if (client.playerLevel[stat] >= actualLevel + decreaseBy)
			return;
		if (stat == 5 && client.playerLevel[stat] + decreaseBy > actualLevel) {
			decreaseBy = 0.0D;
			client.playerLevel[stat] = actualLevel;
		}
		if (client.playerLevel[stat] - decreaseBy >= 1.0D)
			client.playerLevel[stat] -= decreaseBy;
		else
			client.playerLevel[stat] = 1;
		refreshSkill(stat);
	}

	public void deleteItem(int id, int amount) {
		deleteItem(id, getItemSlot(id), amount);
	}

	public void deleteItem(int id, int slot, int amount) {
		if (slot > -1 && client.playerItems[slot] == id + 1) {
			if (client.playerItemsN[slot] > amount)
				client.playerItemsN[slot] -= amount;
			else {
				client.playerItemsN[slot] = 0;
				client.playerItems[slot] = 0;
			}
			client.getActionSender().sendItemReset(3214);
		}
	}

	public void deleteItem(Item item) {
		deleteItem(item.getId(), item.getCount());
	}

	public void deleteItem1(int id, int slot, int amount) {
		if (slot > -1 && client.playerItems[slot] == id + 1)
			if (client.playerItemsN[slot] > amount)
				client.playerItemsN[slot] -= amount;
			else {
				client.playerItemsN[slot] = 0;
				client.playerItems[slot] = 0;
			}
	}

	public void deleteItem2(int id, int amount) {
		int am = amount;
		for (int i = 0; i < client.playerItems.length; i++) {
			if (am == 0)
				break;
			if (client.playerItems[i] != id + 1)
				continue;
			if (client.playerItemsN[i] > amount) {
				client.playerItemsN[i] -= amount;
				break;
			}
			client.playerItems[i] = 0;
			client.playerItemsN[i] = 0;
			am--;
		}

		client.getActionSender().sendItemReset(3214);
	}

	public void deleteItem3(int id, int amount) {
		deleteItem1(id, getItemSlot(id), amount);
	}

	public void dropItem(int id, int slot) {
		if (!TradingConstants.isUntradable(id)) {
			if (client.playerItems[slot] == id + 1) {
				GroundItemManager.getInstance().createGroundItem(client,
						new Item(id, client.playerItemsN[slot]),
						client.getPosition());
				deleteItem(client.playerItems[slot] - 1, slot,
						client.playerItemsN[slot]);
			}
		} else {
			client.getActionAssistant().deleteItem(id, 1);
			client.getActionSender().sendMessage(
					"The item crumbles to dust as you drop it.");
			if (id == 18778 || id == 18779 || id == 18780 || id == 18791)
				client.numEffigies--;
		}
	}

	public void forceChat(String forcedChat) {
		client.forcedText = forcedChat;
		client.forcedTextUpdateRequired = true;
		client.updateRequired = true;
	}

	public void forceText(String s) {
		client.forcedText = s;
		client.updateRequired = true;
		client.forcedTextUpdateRequired = true;
	}

	public int freeBankSlots() {
		int freeS = 0;
		for (int i = 0; i < 348; i++)
			if (client.bankItems[i] <= 0)
				freeS++;

		return freeS;
	}

	public int freeBankSlots1() {
		int freeS = 0;
		for (int i = 0; i < 348; i++)
			if (client.bankItems[i] <= 0)
				freeS++;

		return freeS;
	}

	public int freeSlots() {
		int freeS = 0;
		for (final int playerItem : client.playerItems)
			if (playerItem <= 0)
				freeS++;

		return freeS;
	}

	public int getItemAmount(int itemID) {
		int tempAmount = 0;
		for (int i = 0; i < client.playerItems.length; i++)
			if (itemID > 0 && client.playerItems[i] - 1 == itemID)
				if (Item.itemStackable[itemID] || Item.itemIsNote[itemID])
					tempAmount += client.playerItemsN[i];
				else
					tempAmount++;

		return tempAmount;
	}

	public int getItemCount(int itemId) {
		int total = 0;
		for (final int playerItem : client.playerItems)
			if (playerItem - 1 == itemId)
				total++;

		return total;
	}

	public int getItemSlot(int itemID) {
		for (int i = 0; i < client.playerItems.length; i++)
			if (client.playerItems[i] - 1 == itemID)
				return i;

		return -1;
	}

	public void gfx100(int id) {
		client.gfxID = id;
		client.gfxDelay = 0x640000;
		client.updateRequired = true;
		client.graphicsUpdateRequired = true;
	}

	public void increaseStat(int stat, int increase) {
		final int actualLevel = client.getLevelForXP(client.playerXP[stat]);
		double increaseBy = Misc.intToPercentage(increase) * actualLevel;
		if (client.playerLevel[stat] >= actualLevel + increaseBy)
			return;
		if (stat == 5 && client.playerLevel[stat] + increaseBy > actualLevel
				&& !MagicAndPraySwitch.isSwitchingPrayer) {
			increaseBy = 0.0D;
			client.playerLevel[stat] = actualLevel;
		}
		client.playerLevel[stat] += increaseBy;
		if (client.playerLevel[stat] >= actualLevel + increaseBy)
			client.playerLevel[stat] = (int) (actualLevel + increaseBy);
		refreshSkill(stat);
	}

	public boolean isItemInBag(int itemID) {
		for (final int playerItem : client.playerItems)
			if (playerItem - 1 == itemID)
				return true;

		return false;
	}

	public void openUpShop(int shopId) {
		final Shop s = InstanceDistributor.getShopManager().getShops()
				.get(Integer.valueOf(shopId));
		if (s == null)
			return;
		client.getActionSender().sendString(s.getName(), 3901);
		client.getActionSender().sendInterfaceInventory(3824, 3822);
		client.getActionSender().sendItemReset(3823);
		client.getActionSender().sendShopReset(s);
		client.getExtraData().put("shop", Integer.valueOf(shopId));
		s.updated();
		if (client.getExtraData().containsKey("shop")
				&& ((Integer) client.getExtraData().get("shop")).intValue() == s
						.getId())
			client.getActionSender().sendShopReset(s);
		client.getActionSender().sendItemReset(3823);
	}

	public boolean playerBankHasItem(int itemID, int amt) {
		itemID++;
		int found = 0;
		for (int i = 0; i < client.bankItems.length; i++)
			if (client.bankItems[i] == itemID) {
				if (client.bankItemsN[i] >= amt)
					return true;
				found++;
			}

		return found >= amt;
	}

	public int playerBankHasItemSlot(int itemID, int amt) {
		itemID++;
		for (int i = 0; i < client.bankItems.length; i++)
			if (client.bankItems[i] == itemID && client.bankItemsN[i] >= amt)
				return i;

		return 0;
	}

	public boolean playerEquipContains(int itemID) {
		for (final int element : client.playerEquipment)
			if (element == itemID)
				return true;

		return false;
	}

	public boolean playerHasItem(int itemID) {
		return playerHasItem(itemID, 1);
	}

	public boolean playerHasItem(int itemID, int amt) {
		itemID++;
		int found = 0;
		for (int i = 0; i < client.playerItems.length; i++)
			if (client.playerItems[i] == itemID) {
				if (client.playerItemsN[i] >= amt)
					return true;
				found++;
			}

		return found >= amt;
	}

	public void refreshSkill(int skillId) {
		switch (skillId) {
		case 0: // '\0'
			client.getActionSender().sendString(
					new StringBuilder().append(client.playerLevel[0])
							.toString(), 7159);
			client.getActionSender().sendString(
					new StringBuilder().append(
							client.getLevelForXP(client.playerXP[0]))
							.toString(), 7160);
			break;

		case 1: // '\001'
			client.getActionSender().sendString(
					new StringBuilder().append(client.playerLevel[1])
							.toString(), 7163);
			client.getActionSender().sendString(
					new StringBuilder().append(
							client.getLevelForXP(client.playerXP[1]))
							.toString(), 7164);
			break;

		case 2: // '\002'
			client.getActionSender().sendString(
					new StringBuilder().append(client.playerLevel[2])
							.toString(), 7161);
			client.getActionSender().sendString(
					new StringBuilder().append(
							client.getLevelForXP(client.playerXP[2]))
							.toString(), 7162);
			break;

		case 3: // '\003'
			client.getActionSender().sendString(
					new StringBuilder().append(client.hitpoints).toString(),
					7175);
			client.getActionSender().sendString(
					new StringBuilder().append(
							client.getLevelForXP(client.playerXP[3]))
							.toString(), 7176);
			break;

		case 4: // '\004'
			client.getActionSender().sendString(
					new StringBuilder().append(client.playerLevel[4])
							.toString(), 7165);
			client.getActionSender().sendString(
					new StringBuilder().append(
							client.getLevelForXP(client.playerXP[4]))
							.toString(), 7166);
			break;

		case 5: // '\005'
			client.getActionSender().sendString(
					new StringBuilder().append(client.playerLevel[5])
							.toString(), 7167);
			client.getActionSender().sendString(
					new StringBuilder().append(
							client.getLevelForXP(client.playerXP[5]))
							.toString(), 7168);
			client.getActionSender().sendString(
					new StringBuilder().append(client.playerLevel[5])
							.append("/")
							.append(client.getLevelForXP(client.playerXP[5]))
							.toString(), 687);
			break;

		case 6: // '\006'
			client.getActionSender().sendString(
					new StringBuilder().append(client.playerLevel[6])
							.toString(), 7169);
			client.getActionSender().sendString(
					new StringBuilder().append(
							client.getLevelForXP(client.playerXP[6]))
							.toString(), 7170);
			break;

		case 7: // '\007'
			client.getActionSender().sendString(
					new StringBuilder().append(client.playerLevel[7])
							.toString(), 7197);
			client.getActionSender().sendString(
					new StringBuilder().append(
							client.getLevelForXP(client.playerXP[7]))
							.toString(), 7198);
			break;

		case 8: // '\b'
			client.getActionSender().sendString(
					new StringBuilder().append(client.playerLevel[8])
							.toString(), 7201);
			client.getActionSender().sendString(
					new StringBuilder().append(
							client.getLevelForXP(client.playerXP[8]))
							.toString(), 7202);
			break;

		case 9: // '\t'
			client.getActionSender().sendString(
					new StringBuilder().append(client.playerLevel[9])
							.toString(), 7185);
			client.getActionSender().sendString(
					new StringBuilder().append(
							client.getLevelForXP(client.playerXP[9]))
							.toString(), 7186);
			break;

		case 10: // '\n'
			client.getActionSender().sendString(
					new StringBuilder().append(client.playerLevel[10])
							.toString(), 7195);
			client.getActionSender().sendString(
					new StringBuilder().append(
							client.getLevelForXP(client.playerXP[10]))
							.toString(), 7196);
			break;

		case 11: // '\013'
			client.getActionSender().sendString(
					new StringBuilder().append(client.playerLevel[11])
							.toString(), 7199);
			client.getActionSender().sendString(
					new StringBuilder().append(
							client.getLevelForXP(client.playerXP[11]))
							.toString(), 7200);
			break;

		case 12: // '\f'
			client.getActionSender().sendString(
					new StringBuilder().append(client.playerLevel[12])
							.toString(), 7183);
			client.getActionSender().sendString(
					new StringBuilder().append(
							client.getLevelForXP(client.playerXP[12]))
							.toString(), 7184);
			break;

		case 13: // '\r'
			client.getActionSender().sendString(
					new StringBuilder().append(client.playerLevel[13])
							.toString(), 7193);
			client.getActionSender().sendString(
					new StringBuilder().append(
							client.getLevelForXP(client.playerXP[13]))
							.toString(), 7194);
			break;

		case 14: // '\016'
			client.getActionSender().sendString(
					new StringBuilder().append(client.playerLevel[14])
							.toString(), 7191);
			client.getActionSender().sendString(
					new StringBuilder().append(
							client.getLevelForXP(client.playerXP[14]))
							.toString(), 7192);
			break;

		case 15: // '\017'
			client.getActionSender().sendString(
					new StringBuilder().append(client.playerLevel[15])
							.toString(), 7179);
			client.getActionSender().sendString(
					new StringBuilder().append(
							client.getLevelForXP(client.playerXP[15]))
							.toString(), 7180);
			break;

		case 16: // '\020'
			client.getActionSender().sendString(
					new StringBuilder().append(client.playerLevel[16])
							.toString(), 7177);
			client.getActionSender().sendString(
					new StringBuilder().append(
							client.getLevelForXP(client.playerXP[16]))
							.toString(), 7178);
			break;

		case 17: // '\021'
			client.getActionSender().sendString(
					new StringBuilder().append(client.playerLevel[17])
							.toString(), 7181);
			client.getActionSender().sendString(
					new StringBuilder().append(
							client.getLevelForXP(client.playerXP[17]))
							.toString(), 7182);
			break;

		case 18: // '\022'
			client.getActionSender().sendString(
					new StringBuilder().append(client.playerLevel[18])
							.toString(), 7187);
			client.getActionSender().sendString(
					new StringBuilder().append(
							client.getLevelForXP(client.playerXP[18]))
							.toString(), 7188);
			break;

		case 19: // '\023'
			client.getActionSender().sendString(
					new StringBuilder().append(client.playerLevel[19])
							.toString(), 7203);
			client.getActionSender().sendString(
					new StringBuilder().append(
							client.getLevelForXP(client.playerXP[19]))
							.toString(), 7204);
			break;

		case 20: // '\024'
			client.getActionSender().sendString(
					new StringBuilder().append(client.playerLevel[20])
							.toString(), 7171);
			client.getActionSender().sendString(
					new StringBuilder().append(
							client.getLevelForXP(client.playerXP[20]))
							.toString(), 7172);
			break;

		case 21: // '\025'
			client.getActionSender().sendString(
					new StringBuilder().append(client.playerLevel[21])
							.toString(), 7205);
			client.getActionSender().sendString(
					new StringBuilder().append(
							client.getLevelForXP(client.playerXP[21]))
							.toString(), 7206);
			break;

		case 22: // '\026'
			client.getActionSender().sendString(
					new StringBuilder().append(client.playerLevel[22])
							.toString(), 7189);
			client.getActionSender().sendString(
					new StringBuilder().append(
							client.getLevelForXP(client.playerXP[22]))
							.toString(), 7190);
			break;

		case 23: // '\027'
			client.getActionSender().sendString(
					new StringBuilder().append(
							client.getDungLevelForXp(client.playerXP[23]))
							.toString(), 7157);
			client.getActionSender().sendString(
					new StringBuilder().append(
							client.getDungLevelForXp(client.playerXP[23]))
							.toString(), 7158);
			break;

		case 24: // '\030'
			client.getActionSender().sendString(
					new StringBuilder().append(client.playerLevel[24])
							.toString(), 7173);
			client.getActionSender().sendString(
					new StringBuilder().append(
							client.getLevelForXP(client.playerXP[24]))
							.toString(), 7174);
			break;
		}
		if (skillId >= 0 && skillId < 7 || skillId == 21) {
			client.combatLevel = client.getCombatLevel();
			client.summoningCombatLevel = client.getCombatLevelNoSummoning();
		}
		final GamePacketBuilder bldr = new GamePacketBuilder(134);
		bldr.put((byte) skillId);
		bldr.putInt1(client.playerXP[skillId]);
		bldr.put((byte) client.playerLevel[skillId]);
		client.write(bldr.toPacket());
	}

	public void replaceItem(int oldID, int newID) {
		deleteItem(oldID, 1);
		client.getActionSender().addItem(newID, 1);
	}

	public void restore(int amount) {
		for (int i = 0; i < 25; i++) {
			if (client.playerLevel[i] > client
					.getLevelForXP(client.playerXP[i]))
				client.playerLevel[i] -= amount;
			refreshSkill(i);
		}

	}

	public void sendAnimation(int anim) {
		if (anim > 0) {
			client.animationRequest = anim;
			client.animationWaitCycles = 0;
			client.updateRequired = true;
		}
	}

	public void setFollowing(int followID, int followType, int followDistance) {
		final GamePacketBuilder bldr = new GamePacketBuilder(174);
		bldr.putShort(followID);
		bldr.put((byte) followType);
		bldr.putShort(followDistance);
		client.write(bldr.toPacket());
	}

	public void setSplitChat(int state) {
		if (state == 0) {
			client.getActionSender().sendFrame36(502, 1);
			client.getActionSender().sendFrame36(287, 1);
			client.SplitChat = true;
		} else {
			client.getActionSender().sendFrame36(502, 0);
			client.getActionSender().sendFrame36(287, 0);
			client.SplitChat = false;
		}
	}

	public void showNewLevel(int skillId) {
		client.getActionSender().sendString(
				new StringBuilder("Total Level : ").append(client.totalLevel)
						.toString(), 7127);
		client.getActionSender().sendMessage(
				new StringBuilder("Congratulations, you just advanced an ")
						.append(PlayerConstants.SKILL_NAMES[skillId]
								.toLowerCase()).append(" level!").toString());
		if (client.getLevelForXP(client.playerXP[skillId]) == 99
				&& skillId != 23)
			client.getActionSender()
					.sendMessage(
							new StringBuilder(
									"@dre@Congratulations! you just achieved the maximum level in ")
									.append(PlayerConstants.SKILL_NAMES[skillId]
											.toLowerCase()).append("!")
									.toString());
		GraphicsProcessor.addNewRequest(client, 199, 100, 2);
		client.getActionSender().sendString(
				new StringBuilder("Total Level: ").append(
						client.calculateTotalLevel()).toString(), 7127);
		switch (client.totalLevel) {
		case 100: // 'd'
		case 200:
		case 300:
		case 400:
		case 500:
		case 600:
		case 700:
		case 800:
		case 900:
		case 1000:
		case 1100:
		case 1200:
		case 1300:
		case 1400:
		case 1500:
		case 1600:
		case 1700:
		case 1800:
		case 1900:
		case 2000:
		case 2100:
		case 2200:
		case 2300:
			client.sendMessage(new StringBuilder(
					"@dre@Congratulations! You have reached the ")
					.append(client.totalLevel)
					.append(" total level milestone!").toString());
			break;

		case 2397:
			client.sendMessage(new StringBuilder(
					"@dre@Congratulations! You've achieved the highest total level possible on ")
					.append(Settings.getString("sv_name")).append("!")
					.toString());
			World.getWorld()
					.sendGlobalMessage(
							new StringBuilder(
									"[@red@SERVER@bla@] Congratulations! @dre@")
									.append(Misc.capitalizeFirstLetter(client
											.getUsername()))
									.append("@bla@ has just achieved the maximum total level on ")
									.append(Settings.getString("sv_name"))
									.append("!").toString());
			break;
		}
	}

	public boolean staffType(int runeType) {
		final int staffTypes[][] = { { 554, 1387, 1393, 1401, 11738, 3054 },
				{ 555, 1383, 1395, 1403, 11738, 6563 },
				{ 556, 1381, 1397, 1405 },
				{ 557, 1385, 1399, 1407, 3054, 6563 } };
		final int weapon = client.playerEquipment[3];
		for (final int[] staffType : staffTypes)
			if (runeType == staffType[0])
				for (int j = 1; j < 4; j++)
					if (weapon == staffType[j])
						return true;

		return false;
	}

	public void startAnimation(int anim) {
		if (anim > 13000)
			return;
		else {
			client.animationRequest = anim;
			client.animationWaitCycles = 0;
			client.updateRequired = true;
			return;
		}
	}

	public void startAnimation(int anim, int delay) {
		if (anim > 13000)
			return;
		else {
			client.animationRequest = anim;
			client.animationWaitCycles = delay;
			client.updateRequired = true;
			return;
		}
	}

	public void subtractHp(int i) {
		client.hitpoints -= i;
		if (client.hitpoints < 0)
			client.hitpoints = 0;
		client.playerLevel[3] = client.hitpoints;
		client.hitpoints = client.playerLevel[3];
		client.getActionAssistant().refreshSkill(3);
	}

	public void turnPlayerTo(int index) {
		client.turnPlayerTo = index;
		client.updateRequired = true;
		client.turnPlayerToUpdateRequired = true;
	}

	public void turnTo(int x, int y) {
		client.viewToX = x;
		client.viewToY = y;
		client.updateRequired = true;
		client.faceUpdateRequired = true;
	}
}