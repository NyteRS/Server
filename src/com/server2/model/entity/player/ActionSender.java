package com.server2.model.entity.player;

import com.server2.Server;
import com.server2.Settings;
import com.server2.model.Item;
import com.server2.model.Shop;
import com.server2.model.entity.GroundItem;
import com.server2.model.entity.Location;
import com.server2.model.entity.Projectile;
import com.server2.net.GamePacketBuilder;
import com.server2.world.PlayerManager;
import com.server2.world.World;
import com.server2.world.map.MapLoader;
import com.server2.world.map.Region;
import com.server2.world.map.tile.TileManager;
import com.server2.world.objects.GameObject;

// Referenced classes of package com.jinrake.model.entity.player:
//            Player, Friends, ActionAssistant

public class ActionSender {

	private Player client;

	private boolean displayingMultiCombatIcon;

	public static int textID[] = { 8145, 8147, 8148, 8149, 8150, 8151, 8152,
			8153, 8154, 8155, 8156, 8157, 8158, 8159, 8160, 8161, 8162, 8163,
			8164, 8165, 8166, 8167, 8168, 8169, 8170, 8171, 8172, 8173, 8174,
			8175, 8176, 8177, 8178, 8179, 8180, 8181, 8182, 8183, 8184, 8185,
			8186, 8187, 8188, 8189, 8190, 8191, 8192, 8193, 8194, 8195, 12174,
			12175, 12176, 12177, 12178, 12179, 12180, 12181, 12182, 12183,
			12184, 12185, 12186, 12187, 12188, 12189, 12190, 12191, 12192,
			12193, 12194, 12195, 12196, 12197, 12198, 12199, 12200, 12201,
			12202, 12203, 12204, 12205, 12206, 12207, 12208, 12209, 12210,
			12211, 12212, 12213, 12214, 12215, 12216, 12217, 12218, 12219,
			12220, 12221, 12222, 12223 };

	public ActionSender(Player client) {
		setClient(client);
	}

	public boolean addItem(int item, int amount) {
		if (item == 7406)
			item = 7158;
		if (item == -1 || item < 0)
			return false;
		if (!Item.itemStackable[item] || amount < 1)
			amount = 1;
		if (hasEnoughSpace(item, amount)) {
			for (int i = 0; i < getClient().playerItems.length; i++)
				if (getClient().playerItems[i] == item + 1
						&& Item.itemStackable[item]
						&& getClient().playerItems[i] > 0) {
					getClient().playerItems[i] = item + 1;
					if (getClient().playerItemsN[i] + amount < 0x7fffffff
							&& getClient().playerItemsN[i] + amount > -1)
						getClient().playerItemsN[i] += amount;
					else
						getClient().playerItemsN[i] = 0x7fffffff;
					final GamePacketBuilder bldr = new GamePacketBuilder(34,
							com.server2.net.GamePacket.Type.VARIABLE_SHORT);
					bldr.putShort(3214);
					bldr.put((byte) i);
					bldr.putShort(getClient().playerItems[i]);
					if (getClient().playerItemsN[i] > 254) {
						bldr.put((byte) -1);
						bldr.putInt(getClient().playerItemsN[i]);
					} else
						bldr.put((byte) getClient().playerItemsN[i]);
					getClient().write(bldr.toPacket());
					i = 30;
					return true;
				}

			for (int i = 0; i < getClient().playerItems.length; i++)
				if (getClient().playerItems[i] <= 0) {
					getClient().playerItems[i] = item + 1;
					if (amount < 0x7fffffff && amount > -1)
						getClient().playerItemsN[i] = amount;
					else
						getClient().playerItemsN[i] = 0x7fffffff;
					final GamePacketBuilder bldr = new GamePacketBuilder(34,
							com.server2.net.GamePacket.Type.VARIABLE_SHORT);
					bldr.putShort(3214);
					bldr.put((byte) i);
					bldr.putShort(getClient().playerItems[i]);
					if (getClient().playerItemsN[i] > 254) {
						bldr.put((byte) -1);
						bldr.putInt(getClient().playerItemsN[i]);
					} else
						bldr.put((byte) getClient().playerItemsN[i]);
					getClient().write(bldr.toPacket());
					i = 30;
					return true;
				}

			return false;
		} else {
			sendMessage("There is not enough space in your inventory.");
			return false;
		}
	}

	public boolean addItem(Item itemAdded) {
		int item = itemAdded.getId();
		int amount = itemAdded.getCount();
		if (item == 7406)
			item = 7158;
		if (item == -1)
			return false;
		if (!Item.itemStackable[item] || amount < 1)
			amount = 1;
		if (hasEnoughSpace(item, amount)) {
			for (int i = 0; i < getClient().playerItems.length; i++)
				if (getClient().playerItems[i] == item + 1
						&& Item.itemStackable[item]
						&& getClient().playerItems[i] > 0) {
					getClient().playerItems[i] = item + 1;
					if (getClient().playerItemsN[i] + amount < 0x7fffffff
							&& getClient().playerItemsN[i] + amount > -1)
						getClient().playerItemsN[i] += amount;
					else
						getClient().playerItemsN[i] = 0x7fffffff;
					final GamePacketBuilder bldr = new GamePacketBuilder(34,
							com.server2.net.GamePacket.Type.VARIABLE_SHORT);
					bldr.putShort(3214);
					bldr.put((byte) i);
					bldr.putShort(getClient().playerItems[i]);
					if (getClient().playerItemsN[i] > 254) {
						bldr.put((byte) -1);
						bldr.putInt(getClient().playerItemsN[i]);
					} else
						bldr.put((byte) getClient().playerItemsN[i]);
					getClient().write(bldr.toPacket());
					i = 30;
					return true;
				}

			for (int i = 0; i < getClient().playerItems.length; i++)
				if (getClient().playerItems[i] <= 0) {
					getClient().playerItems[i] = item + 1;
					if (amount < 0x7fffffff && amount > -1)
						getClient().playerItemsN[i] = amount;
					else
						getClient().playerItemsN[i] = 0x7fffffff;
					final GamePacketBuilder bldr = new GamePacketBuilder(34,
							com.server2.net.GamePacket.Type.VARIABLE_SHORT);
					bldr.putShort(3214);
					bldr.put((byte) i);
					bldr.putShort(getClient().playerItems[i]);
					if (getClient().playerItemsN[i] > 254) {
						bldr.put((byte) -1);
						bldr.putInt(getClient().playerItemsN[i]);
					} else
						bldr.put((byte) getClient().playerItemsN[i]);
					getClient().write(bldr.toPacket());
					i = 30;
					return true;
				}

			return false;
		} else {
			sendMessage("There is not enough space in your inventory.");
			return false;
		}
	}

	public void addItemOrDrop(Item item) {
		addItem(item);
	}

	public void clearClanChatInterface(int x, boolean flag) {
		if (flag) {
			sendString("Talking in: ", 18139);
			sendString("Owner: ", 18140);
			sendString("Join Chat", 18135);
		}
		for (int i = 18144; i < 18144 + x; i++)
			sendString("", i);

	}

	public void createGroundItem(GroundItem groundItem) {
		if (groundItem == null)
			return;
		else {
			GamePacketBuilder packet = new GamePacketBuilder(85);
			packet.putByteC(groundItem.getPosition().getLocalY(
					client.getLastKnownRegion()));
			packet.putByteC(groundItem.getPosition().getLocalX(
					client.getLastKnownRegion()));
			client.write(packet.toPacket());
			packet = new GamePacketBuilder(44);
			packet.putLEShortA(groundItem.getItem().getId());
			packet.putShort(groundItem.getItem().getCount());
			packet.put((byte) 0);
			client.write(packet.toPacket());
			return;
		}
	}

	public void createPlayersObjectAnim(Player c, int X, int Y, int id,
			int type, int orientation) {
		try {
			final GamePacketBuilder bldr = new GamePacketBuilder(85);
			bldr.putByteC(Y - c.mapRegionY * 8);
			bldr.putByteC(X - c.mapRegionX * 8);
			c.write(bldr.toPacket());
			final int x = 0;
			final int y = 0;
			final GamePacketBuilder bldr1 = new GamePacketBuilder(160);
			bldr1.putByteS((byte) (((x & 7) << 4) + (y & 7)));
			bldr1.putByteS((byte) ((type << 2) + (orientation & 3)));
			bldr1.putShortA(id);
			c.write(bldr1.toPacket());
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void dispatchGameObject(GameObject object, boolean expiredObject) {
		final int objType = expiredObject ? object.getReplacementId() : object
				.getObjectId();
		if (getClient().getHeightLevel() != object.getLocation().getZ()
				|| !getClient().withinDistance(object.getLocation().getX(),
						object.getLocation().getY(), 32))
			return;
		if (objType == -1)
			Region.removeClipping(object.getLocation().getX(), object
					.getLocation().getY(), object.getLocation().getZ(),
					0x7fffffff);
		if (objType == -1) {
			final GamePacketBuilder bldr = new GamePacketBuilder(85);
			bldr.putByteC(object.getLocation().getY()
					- getClient().getMapRegionY() * 8);
			bldr.putByteC(object.getLocation().getX()
					- getClient().getMapRegionX() * 8);
			getClient().write(bldr.toPacket());
			final GamePacketBuilder bldr2 = new GamePacketBuilder(101);
			bldr2.putByteC((object.getObjectType() << 2)
					+ (object.getFaceDirection() & 3));
			bldr2.putByteA(0);
			getClient().write(bldr2.toPacket());
		} else {
			sendCoords(object.getLocation().getX(), object.getLocation().getY());
			final GamePacketBuilder bldr = new GamePacketBuilder(151);
			bldr.putByteA(0);
			bldr.putLEShort(objType);
			bldr.putByteS((byte) ((object.getObjectType() << 2) + (object
					.getFaceDirection() & 3)));
			getClient().write(bldr.toPacket());
		}
	}

	public Player getClient() {
		return client;
	}

	public boolean hasEnoughSpace(int item, int amount) {
		if (!Item.itemStackable[item]
				&& getClient().getActionAssistant().freeSlots() < amount)
			return false;
		if (Item.itemStackable[item]) {
			if (!getClient().getActionAssistant().playerHasItem(item, 1)
					&& getClient().getActionAssistant().freeSlots() <= 0)
				return false;
			if (getClient().getActionAssistant().playerHasItem(item, 1))
				return true;
		}
		return true;
	}

	public void itemModelOnInterface(int ifc, int model, int zoom, int itemId) {
		sendFrame164(ifc);
		sendFrame246(model, zoom, itemId);
	}

	public void objectAnim(int X, int Y, int animationID, int tileObjectType,
			int orientation) {
		for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
			final Player p = PlayerManager.getSingleton().getPlayers()[i];
			if (p != null) {
				final Player players = p;
				final int location[] = { X, Y };
				if (TileManager.calculateDistance(location, p) <= 25)
					players.getActionSender().createPlayersObjectAnim(players,
							X, Y, animationID, tileObjectType, orientation);
			}
		}

	}

	public void removeGroundItem(GroundItem groundItem) {
		GamePacketBuilder packet = new GamePacketBuilder(85);
		packet.putByteC(groundItem.getPosition().getLocalY(
				client.getLastKnownRegion()));
		packet.putByteC(groundItem.getPosition().getLocalX(
				client.getLastKnownRegion()));
		client.write(packet.toPacket());
		packet = new GamePacketBuilder(156);
		packet.putByteS((byte) 0);
		packet.putShort(groundItem.getItem().getId());
		client.write(packet.toPacket());
	}

	public void replaceGroundItem(GroundItem groundItem, int oldAmount,
			int newAmount) {
		GamePacketBuilder packet = new GamePacketBuilder(85);
		packet.putByteC(groundItem.getPosition().getLocalY(
				client.getLastKnownRegion()));
		packet.putByteC(groundItem.getPosition().getLocalX(
				client.getLastKnownRegion()));
		client.write(packet.toPacket());
		packet = new GamePacketBuilder(84);
		packet.put((byte) 0);
		packet.putShort(groundItem.getItem().getId());
		packet.putShort(oldAmount);
		packet.putShort(newAmount);
		client.write(packet.toPacket());
	}

	public void selectOption(String question, String s1, String s2) {
		sendFrame171(1, 2461);
		sendFrame171(0, 2462);
		sendString(question, 2493);
		sendString(s1, 2461);
		sendString(s2, 2462);
		sendFrame164(2459);
	}

	public void selectOption(String question, String s1, String s2, String s3) {
		sendFrame171(1, 2461);
		sendFrame171(0, 2462);
		sendString(question, 2493);
		sendString(s1, 2471);
		sendString(s2, 2472);
		sendString(s3, 2473);
		sendFrame164(2469);
	}

	public void selectOption(String question, String s1, String s2, String s3,
			String s4, String s5) {
		sendFrame171(1, 2465);
		sendFrame171(0, 2468);
		sendString(question, 2493);
		sendString(s1, 2494);
		sendString(s2, 2495);
		sendString(s3, 2496);
		sendString(s4, 2497);
		sendString(s5, 2498);
		sendFrame164(2492);
	}

	public void sendAnimationReset() {
		final GamePacketBuilder bldr = new GamePacketBuilder(1);
		getClient().write(bldr.toPacket());
	}

	public void sendBankInterface() {
		boolean hasPin = false;
		for (final int element : getClient().bankPin) {
			if (element == 0)
				continue;
			hasPin = true;
			break;
		}

		if (hasPin && !getClient().enteredBankPinSuccesfully)
			getClient().getBankPin().openInterface();
		else {
			getClient().canBank = true;
			final GamePacketBuilder bldr = new GamePacketBuilder(248);
			bldr.putShortA(5292);
			bldr.putShort(5063);
			getClient().write(bldr.toPacket());
			sendReplacementOfTempItem();
		}
	}

	public boolean sendBankReset() {
		final GamePacketBuilder bldr = new GamePacketBuilder(53,
				com.server2.net.GamePacket.Type.VARIABLE_SHORT);
		bldr.putShort(5382);
		bldr.putShort(getClient().getPlayerBankSize());
		for (int i = 0; i < getClient().getPlayerBankSize(); i++) {
			if (getClient().bankItemsN[i] > 254) {
				bldr.put((byte) -1);
				bldr.putInt2(getClient().bankItemsN[i]);
			} else
				bldr.put((byte) getClient().bankItemsN[i]);
			if (getClient().bankItemsN[i] < 1)
				getClient().bankItems[i] = 0;
			if (getClient().bankItems[i] > 23000
					|| getClient().bankItems[i] < 0)
				getClient().bankItems[i] = 23000;
			bldr.putLEShortA(getClient().bankItems[i]);
		}

		getClient().write(bldr.toPacket());
		return true;
	}

	public void sendChatOptions(int publicChat, int privateChat, int tradeBlock) {
		final GamePacketBuilder bldr = new GamePacketBuilder(206);
		bldr.put((byte) publicChat);
		bldr.put((byte) privateChat);
		bldr.put((byte) tradeBlock);
		getClient().write(bldr.toPacket());
	}

	public void sendComponentItems(int interfaceId, int items[], int amounts[],
			int slots[]) {
		final GamePacketBuilder bldr = new GamePacketBuilder(34);
		bldr.putShort(interfaceId);
		int ai[];
		final int j = (ai = slots).length;
		for (int i = 0; i < j; i++) {
			final int slot = ai[i];
			final int item = items[slot];
			bldr.putSmart(slot);
			bldr.putShort(item + 1);
			final int count = amounts[slot];
			if (count > 254) {
				bldr.put((byte) -1);
				bldr.putInt(count);
			} else
				bldr.put((byte) count);
		}

		client.write(bldr.toPacket());
	}

	public void sendComponentItems(int interfaceId, int items[], int amounts[],
			int size) {
		final GamePacketBuilder packet = new GamePacketBuilder(53);
		packet.putShort(interfaceId);
		packet.putShort(size);
		for (int i = 0; i < size; i++) {
			if (amounts[i] <= 0) {
				items[i] = -1;
				amounts[i] = 0;
			}
			if (items[i] == -1) {
				packet.put((byte) 0);
				packet.putLEShortA(0);
			} else {
				final int count = amounts[i];
				if (count > 254) {
					packet.put((byte) -1);
					packet.putInt2(count);
				} else
					packet.put((byte) count);
				packet.putLEShortA(items[i] + 1);
			}
		}

		client.write(packet.toPacket());
	}

	public void sendConfig(int id, int value) {
		if (value < 128) {
			final GamePacketBuilder bldr = new GamePacketBuilder(36);
			bldr.putLEShort(id);
			bldr.put((byte) value);
			getClient().write(bldr.toPacket());
		} else {
			final GamePacketBuilder bldr = new GamePacketBuilder(87);
			bldr.putLEShort(id);
			bldr.putInt1(value);
			getClient().write(bldr.toPacket());
		}
	}

	public void sendCookOption(int i) {
		sendFrame164(1743);
		sendFrame246(13716, 250, i);
		sendString("How many would you like to cook?", 13721);
		sendString("", 13717);
		sendString("", 13718);
		sendString("", 13719);
		sendString("", 13720);
	}

	public ActionSender sendCoordinates(Location position) {
		final GamePacketBuilder bldr = new GamePacketBuilder(85);
		final int y = position.getY() - 8 * getClient().mapRegionY;
		final int x = position.getX() - 8 * getClient().mapRegionX;
		bldr.putByteC((byte) y);
		bldr.putByteC((byte) x);
		getClient().write(bldr.toPacket());
		return this;
	}

	public ActionSender sendCoordinates2(Location position) {
		final GamePacketBuilder bldr = new GamePacketBuilder(85);
		final int y = position.getY()
				- client.getLastKnownRegion().getRegionY() * 8 - 2;
		final int x = position.getX()
				- client.getLastKnownRegion().getRegionX() * 8 - 3;
		bldr.putByteC((byte) y);
		bldr.putByteC((byte) x);
		client.write(bldr.toPacket());
		return this;
	}

	public void sendCoords(int x, int y) {
		final GamePacketBuilder bldr = new GamePacketBuilder(85);
		bldr.putByteC(y - getClient().mapRegionY * 8);
		bldr.putByteC(x - getClient().mapRegionX * 8);
		getClient().write(bldr.toPacket());
	}

	public ActionSender sendCutscene(final int x, final int y,
			final int height, final int speed, final int angle) {
		final GamePacketBuilder bldr = new GamePacketBuilder(177);
		final int xCoord = x / 64;
		final int yCoord = y / 64;
		bldr.put((byte) xCoord);
		bldr.put((byte) yCoord);
		bldr.putLEShort(height);
		bldr.put((byte) speed);
		bldr.put((byte) angle);
		client.write(bldr.toPacket());
		return this;
	}

	public ActionSender sendCutsceneReset() {
		final GamePacketBuilder bldr = new GamePacketBuilder(107);
		client.write(bldr.toPacket());
		return this;
	}

	public void sendDetails() {
		client.write(new GamePacketBuilder(249).putByteA(1)
				.putLEShortA(client.getIndex()).toPacket());
		client.write(new GamePacketBuilder(107).toPacket());
		client.write(new GamePacketBuilder(68).toPacket());
	}

	public void sendFlagRemoval() {
		final GamePacketBuilder bldr = new GamePacketBuilder(78);
		getClient().write(bldr.toPacket());
	}

	public void sendFollowing(int followID, boolean npc, int distance) {
		final GamePacketBuilder bldr = new GamePacketBuilder(174);
		bldr.putShort(followID);
		bldr.put((byte) (npc ? 0 : 1));
		bldr.putShort(distance);
		getClient().write(bldr.toPacket());
	}

	public void sendFrame106(int a) {
		final GamePacketBuilder bldr = new GamePacketBuilder(106);
		bldr.putByteC(a);
		getClient().write(bldr.toPacket());
	}

	public void sendFrame126(String s, int id) {
		client.getActionSender().sendString(s, id);
	}

	public void sendFrame164(int frame) {
		final GamePacketBuilder bldr = new GamePacketBuilder(164);
		bldr.putLEShort(frame);
		getClient().write(bldr.toPacket());
	}

	public void sendFrame171(int id, int visible) {
		final GamePacketBuilder bldr = new GamePacketBuilder(171);
		bldr.put((byte) visible);
		bldr.putShort(id);
		getClient().write(bldr.toPacket());
	}

	public void sendFrame177(int x, int y, int height, int speed, int angle) {
		final GamePacketBuilder bldr = new GamePacketBuilder(177);
		bldr.put((byte) (x / 64));
		bldr.put((byte) (y / 64));
		bldr.putShort(height);
		bldr.put((byte) speed);
		bldr.put((byte) angle);
		getClient().write(bldr.toPacket());
	}

	public void sendFrame185(int i) {
		final GamePacketBuilder bldr = new GamePacketBuilder(185);
		bldr.putLEShortA(i);
		getClient().write(bldr.toPacket());
	}

	public void sendFrame200(int i, int j) {
		final GamePacketBuilder bldr = new GamePacketBuilder(200);
		bldr.putShort(i);
		bldr.putShort(j);
		getClient().write(bldr.toPacket());
	}

	public ActionSender sendFrame230(int interfaceId, int rotation1,
			int rotation2, int zoom) {
		final GamePacketBuilder bldr = new GamePacketBuilder(230);
		bldr.putShortA(zoom);
		bldr.putShort(interfaceId);
		bldr.putShort(rotation1);
		bldr.putLEShortA(rotation2);
		getClient().write(bldr.toPacket());
		return this;
	}

	public void sendFrame246(int mainFrame, int subFrame, int subFrame2) {
		final GamePacketBuilder bldr = new GamePacketBuilder(246);
		bldr.putLEShort(mainFrame);
		bldr.putShort(subFrame);
		bldr.putShort(subFrame2);
		getClient().write(bldr.toPacket());
	}

	public void sendFrame34(int id, int slot, int column, int amount) {
		final GamePacketBuilder bldr = new GamePacketBuilder(34,
				com.server2.net.GamePacket.Type.VARIABLE_SHORT);
		bldr.putShort(column);
		bldr.put((byte) 4);
		bldr.putInt(slot);
		bldr.putShort(id + 1);
		bldr.put((byte) amount);
		getClient().write(bldr.toPacket());
	}

	public void sendFrame35(int i1, int i2, int i3, int i4) {
		final GamePacketBuilder bldr = new GamePacketBuilder(35);
		bldr.put((byte) i1);
		bldr.put((byte) i2);
		bldr.put((byte) i3);
		bldr.put((byte) i4);
		getClient().updateRequired = true;
		getClient().appearanceUpdateRequired = true;
		getClient().write(bldr.toPacket());
	}

	public void sendFrame36(int id, int state) {
		final GamePacketBuilder bldr = new GamePacketBuilder(36);
		bldr.putLEShort(id);
		bldr.put((byte) state);
		getClient().write(bldr.toPacket());
	}

	public void sendFrame70(int i, int o, int id) {
		final GamePacketBuilder bldr = new GamePacketBuilder(70);
		bldr.putShort(i);
		bldr.putLEShort(o);
		bldr.putLEShort(id);
		client.write(bldr.toPacket());
	}

	public void sendFrame71(int a, int b) {
		final GamePacketBuilder bldr = new GamePacketBuilder(71);
		bldr.putShort(a);
		bldr.putByteA(b);
		getClient().write(bldr.toPacket());
	}

	public void sendFrame75(int npc, int i) {
		final GamePacketBuilder bldr = new GamePacketBuilder(75);
		bldr.putLEShortA(npc);
		bldr.putLEShortA(i);
		getClient().write(bldr.toPacket());
	}

	public void sendFrame87(int id, int state) {
		final GamePacketBuilder bldr = new GamePacketBuilder(87);
		bldr.putLEShort(id);
		bldr.putInt1(state);
		getClient().write(bldr.toPacket());
	}

	public void sendFrame99(int a) {
		final GamePacketBuilder bldr = new GamePacketBuilder(99);
		bldr.put((byte) a);
		getClient().write(bldr.toPacket());
	}

	public void sendFriend(long l, int world) {
		final GamePacketBuilder bldr = new GamePacketBuilder(50);
		bldr.putLong(l);
		bldr.put((byte) world);
		client.write(bldr.toPacket());
	}

	public void sendHintIcon(int type, int id) {
		final GamePacketBuilder bldr = new GamePacketBuilder(254);
		bldr.put((byte) type); // 10 player 1 npc
		bldr.putShort(id);
		bldr.putTriByte(0);
		getClient().write(bldr.toPacket());
	}

	public void sendInterface(int interfaceID) {
		final GamePacketBuilder bldr = new GamePacketBuilder(97);
		bldr.putShort(interfaceID);
		getClient().write(bldr.toPacket());
	}

	public void sendInterfaceInventory(int mainFrame, int subFrame) {
		final GamePacketBuilder bldr = new GamePacketBuilder(248);
		bldr.putShortA(mainFrame);
		bldr.putShort(subFrame);
		getClient().write(bldr.toPacket());
	}

	public boolean sendInventoryItem(int item, int amount, int slot) {
		if (item < 1 || item > 0x7fffffff)
			return false;
		if (item == 7406)
			item = 7158;
		if (!Item.itemStackable[item]
				&& getClient().getActionAssistant().freeSlots() < 1) {
			sendMessage("There is not enough space in your inventory.");
			return false;
		}
		if (Item.itemStackable[item]
				&& (getClient().getActionAssistant().freeSlots() > 1 || getClient()
						.getActionAssistant().playerHasItem(item, 1)))
			for (int i = 0; i < getClient().playerItems.length; i++)
				if (getClient().playerItems[i] == item + 1) {
					if (getClient().playerItemsN[i] + amount < 0x7fffffff
							&& getClient().playerItemsN[i] + amount > -1)
						getClient().playerItemsN[i] += amount;
					else {
						sendMessage("I don't think i'll be able to carry all that!");
						return false;
					}
					final GamePacketBuilder bldr = new GamePacketBuilder(34,
							com.server2.net.GamePacket.Type.VARIABLE_SHORT);
					bldr.putShort(3214);
					bldr.put((byte) i);
					bldr.putShort(getClient().playerItems[i]);
					if (getClient().playerItemsN[i] > 254) {
						bldr.put((byte) -1);
						bldr.putInt(getClient().playerItemsN[i]);
					} else
						bldr.put((byte) getClient().playerItemsN[i]);
					getClient().write(bldr.toPacket());
					return true;
				}
		boolean canAddToSlot = true;
		for (int i = 0; i < getClient().playerItems.length; i++)
			if (i == slot && getClient().playerItems[i] > 0)
				canAddToSlot = false;

		if (canAddToSlot) {
			for (int i = 0; i < getClient().playerItems.length; i++)
				if (i == slot) {
					if (getClient().playerItemsN[i] + amount < 0x7fffffff
							&& getClient().playerItemsN[i] + amount > -1) {
						getClient().playerItems[i] = item + 1;
						getClient().playerItemsN[i] += amount;
					} else {
						sendMessage("I don't think i'll be able to carry all that!");
						return false;
					}
					final GamePacketBuilder bldr = new GamePacketBuilder(34,
							com.server2.net.GamePacket.Type.VARIABLE_SHORT);
					bldr.putShort(3214);
					bldr.put((byte) i);
					bldr.putShort(getClient().playerItems[i]);
					if (getClient().playerItemsN[i] > 254) {
						bldr.put((byte) -1);
						bldr.putInt(getClient().playerItemsN[i]);
					} else
						bldr.put((byte) getClient().playerItemsN[i]);
					getClient().write(bldr.toPacket());
					return true;
				}

		} else
			for (int i = 0; i < getClient().playerItems.length; i++)
				if (getClient().playerItems[i] < 1) {
					if (getClient().playerItemsN[i] + amount < 0x7fffffff
							&& getClient().playerItemsN[i] + amount > -1) {
						getClient().playerItems[i] = item + 1;
						getClient().playerItemsN[i] += amount;
					} else {
						sendMessage("I don't think i'll be able to carry all that!");
						return false;
					}
					final GamePacketBuilder bldr = new GamePacketBuilder(34,
							com.server2.net.GamePacket.Type.VARIABLE_SHORT);
					bldr.putShort(3214);
					bldr.put((byte) i);
					bldr.putShort(getClient().playerItems[i]);
					if (getClient().playerItemsN[i] > 254) {
						bldr.put((byte) -1);
						bldr.putInt(getClient().playerItemsN[i]);
					} else
						bldr.put((byte) getClient().playerItemsN[i]);
					getClient().write(bldr.toPacket());
					return true;
				}
		sendMessage("There is not enough space in your inventory.");
		return false;
	}

	public boolean sendInventoryItem1(int item, int amount) {
		if (item == -1)
			return false;
		if (item == 7406)
			item = 7158;
		if (!Item.itemStackable[item] || amount < 1)
			amount = 1;
		if (getClient().getActionAssistant().freeSlots() >= amount
				&& !Item.itemStackable[item]
				|| getClient().getActionAssistant().freeSlots() > 0) {
			for (int i = 0; i < getClient().playerItems.length; i++)
				if (getClient().playerItems[i] == item + 1
						&& Item.itemStackable[item]
						&& getClient().playerItems[i] > 0) {
					getClient().playerItems[i] = item + 1;
					if (getClient().playerItemsN[i] + amount < 0x7fffffff
							&& getClient().playerItemsN[i] + amount > -1)
						getClient().playerItemsN[i] += amount;
					else
						getClient().playerItemsN[i] = 0x7fffffff;
					i = 30;
					return true;
				}

			for (int i = 0; i < getClient().playerItems.length; i++)
				if (getClient().playerItems[i] <= 0) {
					getClient().playerItems[i] = item + 1;
					if (amount < 0x7fffffff && amount > -1)
						getClient().playerItemsN[i] = amount;
					else
						getClient().playerItemsN[i] = 0x7fffffff;
					i = 30;
					return true;
				}

			return false;
		} else {
			sendMessage("There is not enough space in your inventory.");
			return false;
		}
	}

	public boolean sendInventoryItem2(int item, int amount, int slot) {
		if (item == -1)
			return false;
		if (!Item.itemStackable[item] || amount < 1)
			amount = 1;
		if (getClient().getActionAssistant().freeSlots() >= amount
				&& !Item.itemStackable[item]
				|| getClient().getActionAssistant().freeSlots() > 0) {
			for (int i = 0; i < getClient().playerItems.length; i++)
				if (getClient().playerItems[i] == item + 1
						&& Item.itemStackable[item]
						&& getClient().playerItems[i] > 0) {
					getClient().playerItems[i] = item + 1;
					if (getClient().playerItemsN[i] + amount < 0x7fffffff
							&& getClient().playerItemsN[i] + amount > -1)
						getClient().playerItemsN[i] += amount;
					else
						getClient().playerItemsN[i] = 0x7fffffff;
					final GamePacketBuilder bldr = new GamePacketBuilder(34,
							com.server2.net.GamePacket.Type.VARIABLE_SHORT);
					bldr.putShort(3214);
					bldr.put((byte) i);
					bldr.putShort(getClient().playerItems[i]);
					if (getClient().playerItemsN[i] > 254) {
						bldr.put((byte) -1);
						bldr.putInt(getClient().playerItemsN[i]);
					} else
						bldr.put((byte) getClient().playerItemsN[i]);
					getClient().write(bldr.toPacket());
					i = 30;
					return true;
				}

			boolean canAddToSlot = true;
			for (int i = 0; i < getClient().playerItems.length; i++)
				if (i == slot && getClient().playerItems[i] > 0)
					canAddToSlot = false;

			if (canAddToSlot) {
				for (int i = 0; i < getClient().playerItems.length; i++)
					if (i == slot) {
						if (getClient().playerItemsN[i] + amount < 0x7fffffff
								&& getClient().playerItemsN[i] + amount > -1) {
							getClient().playerItems[i] = item + 1;
							getClient().playerItemsN[i] += amount;
						} else {
							sendMessage("I don't think i'll be able to carry all that!");
							return false;
						}
						final GamePacketBuilder bldr = new GamePacketBuilder(
								34,
								com.server2.net.GamePacket.Type.VARIABLE_SHORT);
						bldr.putShort(3214);
						bldr.put((byte) i);
						bldr.putShort(getClient().playerItems[i]);
						if (getClient().playerItemsN[i] > 254) {
							bldr.put((byte) -1);
							bldr.putInt(getClient().playerItemsN[i]);
						} else {
							bldr.put((byte) getClient().playerItemsN[i]);
							getClient().write(bldr.toPacket());
							return true;
						}
					}

				for (int i = 0; i < getClient().playerItems.length; i++)
					if (getClient().playerItems[i] <= 0) {
						getClient().playerItems[i] = item + 1;
						if (amount < 0x7fffffff && amount > -1)
							getClient().playerItemsN[i] = amount;
						else
							getClient().playerItemsN[i] = 0x7fffffff;
						final GamePacketBuilder bldr = new GamePacketBuilder(
								34,
								com.server2.net.GamePacket.Type.VARIABLE_SHORT);
						bldr.putShort(3214);
						bldr.put((byte) i);
						bldr.putShort(getClient().playerItems[i]);
						if (getClient().playerItemsN[i] > 254) {
							bldr.put((byte) -1);
							bldr.putInt(getClient().playerItemsN[i]);
						} else
							bldr.put((byte) getClient().playerItemsN[i]);
						getClient().write(bldr.toPacket());
						i = 30;
						return true;
					}

			}
			return false;
		} else {
			sendMessage("There is not enough space in your inventory.");
			return false;
		}
	}

	public void sendItemReset() {
		final GamePacketBuilder bldr = new GamePacketBuilder(53,
				com.server2.net.GamePacket.Type.VARIABLE_SHORT);
		bldr.putShort(3214);
		bldr.putShort(getClient().playerItems.length);
		for (int i = 0; i < getClient().playerItems.length; i++) {
			if (getClient().playerItemsN[i] > 254) {
				bldr.put((byte) -1);
				bldr.putInt2(getClient().playerItemsN[i]);
			} else
				bldr.put((byte) getClient().playerItemsN[i]);
			if (getClient().playerItems[i] > 23000
					|| getClient().playerItems[i] < 0)
				getClient().playerItems[i] = 23000;
			bldr.putLEShortA(getClient().playerItems[i]);
		}

		getClient().write(bldr.toPacket());
	}

	public void sendItemReset(int frame) {
		final GamePacketBuilder bldr = new GamePacketBuilder(53,
				com.server2.net.GamePacket.Type.VARIABLE_SHORT);
		bldr.putShort(frame);
		bldr.putShort(getClient().playerItems.length);
		for (int i = 0; i < getClient().playerItems.length; i++) {
			if (getClient().playerItemsN[i] > 254) {
				bldr.put((byte) -1);
				bldr.putInt2(getClient().playerItemsN[i]);
			} else
				bldr.put((byte) getClient().playerItemsN[i]);
			bldr.putLEShortA(getClient().playerItems[i]);
		}

		getClient().write(bldr.toPacket());
	}

	public void sendLocationPointer(int x, int y, int height, int pos) {
		final GamePacketBuilder bldr = new GamePacketBuilder(254);
		bldr.put((byte) pos);
		bldr.putShort(x);
		bldr.putShort(y);
		bldr.put((byte) height);
		getClient().write(bldr.toPacket());
	}

	public void sendLocationPointerPlayer(int type, int id) {
		if (getClient() != null) {
			final GamePacketBuilder bldr = new GamePacketBuilder(254);
			bldr.put((byte) type);
			bldr.putShort(id);
			bldr.putTriByte(0);
			getClient().write(bldr.toPacket());
		}
	}

	public void sendLogout() {
		if (getClient().logoutDelay > 0) {
			sendMessage("You can't logout until 15 seconds after the end of combat.");
			return;
		} else {
			final GamePacketBuilder bldr = new GamePacketBuilder(109);
			getClient().write(bldr.toPacket());
			World.getWorld().unregister(getClient());
			return;
		}
	}

	public void sendMapRegion() {
		client.setLastKnownRegion(client.getPosition());
		client.mapRegionX = client.getPosition().getRegionX();
		client.mapRegionY = client.getPosition().getRegionY();
		client.write(new GamePacketBuilder(73).putShortA(client.mapRegionX + 6)
				.putShort(client.mapRegionY + 6).toPacket());
	}

	public void sendMessage(String s) {
		final GamePacketBuilder bldr = new GamePacketBuilder(253,
				com.server2.net.GamePacket.Type.VARIABLE);
		bldr.putRS2String(s);
		getClient().write(bldr.toPacket());
	}

	public void sendMessage(String s[]) {
		for (final String element : s) {
			final GamePacketBuilder bldr = new GamePacketBuilder(253,
					com.server2.net.GamePacket.Type.VARIABLE);
			bldr.putRS2String(element);
			getClient().write(bldr.toPacket());
		}

	}

	public void sendMessage(String s, String s2) {
		final GamePacketBuilder bldr = new GamePacketBuilder(253,
				com.server2.net.GamePacket.Type.VARIABLE);
		bldr.putRS2String(s);
		getClient().write(bldr.toPacket());
		final GamePacketBuilder bldr2 = new GamePacketBuilder(253,
				com.server2.net.GamePacket.Type.VARIABLE);
		bldr2.putRS2String(s2);
		getClient().write(bldr2.toPacket());
	}

	public void sendMultiCombatIcon(boolean flag) {
		if (displayingMultiCombatIcon != flag) {
			client.write(new GamePacketBuilder(61).put((byte) (flag ? 1 : 0))
					.toPacket());
			displayingMultiCombatIcon = flag;
		}
	}

	public void sendObject(int objectID, int objectX, int objectY,
			int objectHeight, int objectFace, int objectType) {
		if (getClient().getHeightLevel() != objectHeight
				|| !getClient().withinDistance(objectX, objectY, 32))
			return;
		if (objectID == -1) {
			final GamePacketBuilder bldr = new GamePacketBuilder(85);
			bldr.putByteC(objectY - getClient().getMapRegionY() * 8);
			bldr.putByteC(objectX - getClient().getMapRegionX() * 8);
			getClient().write(bldr.toPacket());
			final GamePacketBuilder bldr2 = new GamePacketBuilder(101);
			bldr2.putByteC((objectType << 2) + (objectFace & 3));
			bldr2.putByteA(0);
			getClient().write(bldr2.toPacket());
		} else {
			MapLoader.registerObject(objectX, objectY, objectHeight, objectID);
			sendCoords(objectX, objectY);
			final GamePacketBuilder bldr = new GamePacketBuilder(151);
			bldr.putByteA(0);
			bldr.putLEShort(objectID);
			bldr.putByteS((byte) ((objectType << 2) + (objectFace & 3)));
			getClient().write(bldr.toPacket());
		}
	}

	public void sendObjectAnim(int x, int y, int id, int type, int orientation) {
		sendCoords(x, y);
		final GamePacketBuilder bldr = new GamePacketBuilder(160);
		bldr.putByteS((byte) (((x & 7) << 4) + (y & 7)));
		bldr.putByteS((byte) ((type << 2) + (orientation & 3)));
		bldr.putShortA(id);
		getClient().write(bldr.toPacket());
	}

	public void sendOption(String s, int pos) {
		final GamePacketBuilder bldr = new GamePacketBuilder(104,
				com.server2.net.GamePacket.Type.VARIABLE);
		bldr.putByteC(pos);
		bldr.putByteA(0);
		bldr.putRS2String(s);
		getClient().write(bldr.toPacket());
	}

	public void sendOption5(String s, String s1, String s2, String s3, String s4) {
		sendString("Select an Option", 2493);
		sendString(s, 2494);
		sendString(s1, 2495);
		sendString(s2, 2496);
		sendString(s3, 2497);
		sendString(s4, 2498);
		sendFrame164(2492);
	}

	public void sendPacket107() {
	}

	public void sendPM(long from, int playerRights, byte message[]) {
		final GamePacketBuilder bldr = new GamePacketBuilder(196,
				com.server2.net.GamePacket.Type.VARIABLE);
		bldr.putLong(from);
		bldr.putInt(Friends.getMessageIndex());
		bldr.put((byte) playerRights);
		bldr.put(message);
		client.write(bldr.toPacket());
	}

	public void sendPMStatus(int status) {
		final GamePacketBuilder bldr = new GamePacketBuilder(221);
		bldr.put((byte) status);
		client.write(bldr.toPacket());
	}

	public void sendProjectile(int absY, int absX, int offsetY, int offsetX,
			int proID, int startHeight, int endHeight, int speed, int lockon) {
		GamePacketBuilder bldr = new GamePacketBuilder(85);
		bldr.putByteC(absY - getClient().mapRegionY * 8 - 2);
		bldr.putByteC(absX - getClient().mapRegionX * 8 - 3);
		getClient().write(bldr.toPacket());
		bldr = new GamePacketBuilder(117);
		bldr.put((byte) 50);
		bldr.put((byte) offsetY);
		bldr.put((byte) offsetX);
		bldr.putShort(lockon);
		bldr.putShort(proID);
		bldr.put((byte) startHeight);
		bldr.put((byte) endHeight);
		bldr.putShort(51);
		bldr.putShort(speed);
		bldr.put((byte) 16);
		bldr.put((byte) 64);
		getClient().write(bldr.toPacket());
	}

	public void sendProjectile(int absY, int absX, int offsetY, int offsetX,
			int proID, int startHeight, int endHeight, int speed, int angle,
			int lockon) {
		GamePacketBuilder bldr = new GamePacketBuilder(85);
		bldr.putByteC(absY - getClient().mapRegionY * 8 - 2);
		bldr.putByteC(absX - getClient().mapRegionX * 8 - 3);
		getClient().write(bldr.toPacket());
		bldr = new GamePacketBuilder(117);
		bldr.put((byte) 50);
		bldr.put((byte) offsetY);
		bldr.put((byte) offsetX);
		bldr.putShort(lockon);
		bldr.putShort(proID);
		bldr.put((byte) startHeight);
		bldr.put((byte) endHeight);
		bldr.putShort(51);
		bldr.putShort(speed);
		bldr.put((byte) angle);
		bldr.put((byte) 64);
		getClient().write(bldr.toPacket());
	}

	public ActionSender sendProjectile(Projectile projectile) {
		sendCoordinates2(projectile.getTarget().getPosition());
		final GamePacketBuilder bldr = new GamePacketBuilder(117);
		bldr.put((byte) 50);
		bldr.put((byte) projectile.getYOffset());
		bldr.put((byte) projectile.getXOffset());
		bldr.putShort(projectile.getCaster().getLockonIndex());
		bldr.putShort(projectile.getId());
		bldr.put((byte) projectile.getStartHeight());
		bldr.put((byte) projectile.getEndHeight());
		bldr.putShort(projectile.getStartDelay());
		bldr.putShort(projectile.getSpeed());
		bldr.put((byte) projectile.getCurve());
		bldr.put((byte) 64);
		client.write(bldr.toPacket());
		return this;
	}

	public void sendReplacementOfTempItem() {
		int itemCount = 0;
		for (int i = 0; i < getClient().playerItems.length; i++)
			if (getClient().playerItems[i] > -1)
				itemCount = i;

		final GamePacketBuilder bldr = new GamePacketBuilder(53,
				com.server2.net.GamePacket.Type.VARIABLE_SHORT);
		bldr.putShort(5064);
		bldr.putShort(itemCount + 1);
		for (int i = 0; i < itemCount + 1; i++) {
			if (getClient().playerItemsN[i] > 254) {
				bldr.put((byte) -1);
				bldr.putInt2(getClient().playerItemsN[i]);
			} else
				bldr.put((byte) getClient().playerItemsN[i]);
			if (getClient().playerItems[i] > 23000
					|| getClient().playerItems[i] < 0)
				getClient().playerItems[i] = 23000;
			bldr.putLEShortA(getClient().playerItems[i]);
		}

		getClient().write(bldr.toPacket());
	}

	public void sendShopReset(Shop shop) {
		final GamePacketBuilder bldr = new GamePacketBuilder(53,
				com.server2.net.GamePacket.Type.VARIABLE_SHORT);
		bldr.putShort(3900);
		int count = 0;
		for (int i = 0; i < shop.getContainerSize(); i++) {
			final Item si = shop.getItemBySlot(i);
			if (si != null)
				count++;
		}

		bldr.putShort(count);
		for (int i = 0; i < shop.getContainerSize(); i++) {
			final Item si = shop.getItemBySlot(i);
			if (si != null) {
				if (si.getCount() > 254) {
					bldr.put((byte) -1);
					bldr.putInt2(si.getCount());
				} else
					bldr.put((byte) si.getCount());
				bldr.putLEShortA(si.getId() + 1);
			}
		}

		getClient().write(bldr.toPacket());
	}

	public void sendSidebar(int menuId, int form) {
		final GamePacketBuilder bldr = new GamePacketBuilder(71);
		bldr.putShort(form);
		bldr.putByteA(menuId);
		getClient().write(bldr.toPacket());
	}

	public void sendSong(int songID) {
		final GamePacketBuilder bldr = new GamePacketBuilder(74);
		bldr.putLEShort(songID);
		getClient().write(bldr.toPacket());
	}

	public void sendString(String s, int id) {
		final GamePacketBuilder bldr = new GamePacketBuilder(126,
				com.server2.net.GamePacket.Type.VARIABLE_SHORT);
		bldr.putRS2String(s);
		bldr.putShortA(id);
		getClient().write(bldr.toPacket());
	}

	public ActionSender sendSystemUpdate(int seconds) {
		final GamePacketBuilder bldr = new GamePacketBuilder(114);
		bldr.putLEShort(seconds * 50 / 30);
		getClient().write(bldr.toPacket());
		return this;
	}

	public void sendUpdateItems(int interfaceId, Item items[]) {
		final GamePacketBuilder bldr = new GamePacketBuilder(53,
				com.server2.net.GamePacket.Type.VARIABLE_SHORT);
		bldr.putShort(interfaceId);
		bldr.putShort(items.length);
		Item aitem[];
		final int j = (aitem = items).length;
		for (int i = 0; i < j; i++) {
			final Item item = aitem[i];
			if (item != null) {
				final int count = item.getCount();
				if (count > 254) {
					bldr.put((byte) -1);
					bldr.putInt2(count);
				} else
					bldr.put((byte) count);
				bldr.putLEShortA(item.getId() + 1);
			} else {
				bldr.put((byte) 0);
				bldr.putLEShortA(0);
			}
		}

		getClient().write(bldr.toPacket());
	}

	public void sendWalkableInterface(int i) {
		final GamePacketBuilder bldr = new GamePacketBuilder(208);
		bldr.putLEShort(i);
		getClient().write(bldr.toPacket());
	}

	public void sendWindowsRemoval() {
		final GamePacketBuilder bldr = new GamePacketBuilder(219);
		getClient().write(bldr.toPacket());
	}

	public void setClient(Player client) {
		this.client = client;
	}

	public void showItems(int writeFrame, int items[], int itemAmounts[]) {
		final GamePacketBuilder packet = new GamePacketBuilder(53);
		packet.putShort(writeFrame);
		packet.putShort(items.length);
		for (int i = 0; i < items.length; i++) {
			if (itemAmounts[i] < 0) {
				items[i] = -1;
				itemAmounts[i] = 0;
			}
			if (itemAmounts[i] > 254) {
				packet.put((byte) -1);
				packet.putInt2(itemAmounts[i]);
			} else
				packet.put((byte) itemAmounts[i]);
			packet.putLEShortA(items[i] + 1);
		}

		getClient().write(packet.toPacket());
	}

	public void sound(int songid, int vol, int delay) {
		final GamePacketBuilder bldr = new GamePacketBuilder(174);
		bldr.putShort(songid);
		bldr.put((byte) vol);
		bldr.putShort(delay);
		getClient().write(bldr.toPacket());
	}

	public ActionSender toggleSnow() {
		final GamePacketBuilder bldr = new GamePacketBuilder(127);
		final byte snow = (byte) (Server.snowMode ? 1 : 0);
		bldr.put(snow);
		client.write(bldr.toPacket());
		return this;
	}

	public void writePlayers() {
	}
}