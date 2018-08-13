package com.server2.content;

import java.util.ArrayList;
import java.util.Date;

import com.server2.InstanceDistributor;
import com.server2.model.Item;
import com.server2.model.ItemDefinition;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.Player.DuelStage;
import com.server2.model.entity.player.TradingContainer;
import com.server2.net.GamePacket.Type;
import com.server2.net.GamePacketBuilder;
import com.server2.util.Areas;
import com.server2.util.Logger;
import com.server2.util.Misc;
import com.server2.util.PlayerSaving;
import com.server2.world.PlayerManager;
import com.server2.world.World;

/**
 * 
 * @author server2 Dev Team
 * 
 */
public final class Trading {

	/**
	 * 
	 * @author Lukas
	 * 
	 */
	static class TradeLogging {

		private static int[] getIA(int[] arg0, int[] arg1) {
			final int[] i = new int[2];

			i[0] = arg0[0];
			if (Item.itemStackable[i[0]])
				i[1] = arg1[0];
			else
				i[1] = arg0.length;

			return i;
		}

		public static void logTrade(Player c1, Player c2, int[] r, int[] rn,
				int[] u, int[] un) {
			/*
			 * if(c1.lastTradeId==c2.getID() || c2.lastTradeId==c1.getID()){
			 * return; }
			 */
			boolean b1 = ok1(r);
			boolean b2 = ok2(u);
			if (oc(r))
				b1 = false;
			if (oc(u))
				b2 = false;
			if (!b1 && !b2)
				return;
			int[] o = new int[28];
			int[] on = new int[28];
			;

			int itemID = 0;
			int amount = 0;
			if (b1) {
				final int[] z = getIA(r, rn);
				o = u;
				on = un;
				itemID = z[0];
				amount = z[1];
			} else if (b2) {
				final int[] z = getIA(u, un);
				o = r;
				on = rn;
				itemID = z[0];
				amount = z[1];
			}

			// c1.lastTradeId=c2.getID();
			// c2.lastTradeId=c1.getID();
			final boolean onlyCoins = oc(o);
			final String offer = offerToString(o, on);
			int y = 0;
			if (onlyCoins)
				y = 1;
			save(itemID, amount, offer, y);

		}

		private static boolean oc(int[] i) {
			for (int r = 0; r < i.length; r++)
				if (r != 995 && r != 0)
					return true;
			return true;
		}

		private static String offerToString(int[] arg0, int[] arg1) {
			String str = "";
			for (int i = 0; i < arg0.length; i++) {
				str = str + Integer.toString(arg0[i]);
				str = str + ", ";
				str = str + Integer.toString(arg1[i]);
			}
			str = str.substring(0, str.length() - 2);
			return str;
		}

		private static boolean ok1(int[] arg) {
			for (int i = 0; i < arg.length; i++)
				for (int r = 0; r < arg.length; r++)
					if (arg[i] != arg[r] && i != r)
						return false;
			return true;

		}

		private static boolean ok2(int[] arg) {
			for (int i = 0; i < arg.length; i++)
				for (int r = 0; r < arg.length; r++)
					if (arg[i] != arg[r] && i != r)
						return false;

			return true;
		}

		private static void save(int id, int amount, String str, int onlyCoins) {
			if (id == 995)
				return;
			try {

				World.getGameDatabase().executeQuery(
						"insert into trades (time, id, amount, offer, coinsonly) values ('"
								+ System.currentTimeMillis() + "', '" + id
								+ "', '" + amount + "', '" + str + "', '"
								+ onlyCoins + "')");

			} catch (final Exception e) {

			}
		}
	}

	public static boolean canTrade(Player player, Player other) {
		if (player.inWilderness())
			return false;
		if (Areas.inMiniGame(player))
			return false;
		if (!player.getDuelStage().equals(DuelStage.WAITING)) {
			player.getActionSender().sendMessage(
					"You cannot do this right now.");
			return false;
		}
		return true;
	}

	public static String formatInt(int num) {
		return String.valueOf(num);
	}

	private final Player player;

	public Trading(Player player) {
		this.player = player;
	}

	public void declineTrade() {
		this.declineTrade(true);
	}

	void declineTrade(boolean informPartner) {
		final Player o = PlayerManager.getSingleton().getPlayerByNameLong(
				player.tradingWith);
		if (o == null)
			return;
		if (informPartner) {
			o.getTrading().declineTrade(false);
			o.sendMessage("Your partner has declined the trade.");
		}
		final TradingContainer tradeContainer = player.getTradeContainer();
		for (int i = 0; i < 28; i++) {
			final int amount = tradeContainer.getAmount(i);
			if (amount > 0)
				player.getActionSender().addItem(tradeContainer.getItem(i),
						amount);
		}
		tradeContainer.clear();
		player.getActionSender().sendWindowsRemoval();
		player.tradingWith = -1;
		player.canOffer = true;
		player.tradeConfirmed = false;
		player.tradeConfirmed2 = false;
		player.acceptedTrade = false;
		player.getActionSender().sendFrame126(
				"Are you sure you want to make this trade?", 3535);
		PlayerSaving.savePlayer(player);
	}

	public void fromTrade(int itemID, int amount) {
		final Player o = PlayerManager.getSingleton().getPlayerByNameLong(
				player.tradingWith);
		if (o == null)
			return;
		if (player.tradingWith == -1 || !player.canOffer) {
			this.declineTrade();
			return;
		}
		if (player.disconnected || o.disconnected) {
			this.declineTrade();
			return;
		}

		if (!player.getTradeContainer().contains(itemID))
			return;
		if (player.getTradeContainer().getCount(Item.create(itemID)) < amount)
			amount = player.getTradeContainer().getCount(Item.create(itemID));
		if (!player.getTradeContainer().contains(Item.create(itemID, amount)))
			return;
		if (player.getActionAssistant().freeSlots() < amount) {
			if (!Item.itemStackable[itemID])
				amount = player.getActionAssistant().freeSlots();
			if (amount == 0) {
				player.sendMessage("Your inventory is full!");
				return;
			}
		}
		player.tradeConfirmed = false;
		o.tradeConfirmed = false;
		final int removed = player.getTradeContainer().remove(
				Item.create(itemID, amount), amount);
		if (removed == 0)
			return;
		if (Item.itemStackable[itemID])
			player.getActionSender().addItem(itemID, amount);
		else
			for (int i = 0; i < amount; i++)
				player.getActionSender().addItem(itemID, 1);
		o.getActionSender().sendFrame126(
				"Trading with: "
						+ Misc.capitalizeFirstLetter(player.getUsername())
						+ " who has @gre@"
						+ player.getActionAssistant().freeSlots()
						+ " free slots", 3417);
		player.tradeConfirmed = false;
		o.tradeConfirmed = false;
		o.getActionSender().sendUpdateItems(3416,
				player.getTradeContainer().toArray2());
		player.getActionSender().sendUpdateItems(3415,
				player.getTradeContainer().toArray2());
		player.getActionSender().sendFrame126("", 3431);
		player.getActionSender().sendItemReset(3322);
		o.getActionSender().sendFrame126("", 3431);
		player.lastModified = System.currentTimeMillis() + 4000;
		o.lastModified = System.currentTimeMillis() + 4000;
	}

	public void giveItems() {
		final Player o = PlayerManager.getSingleton().getPlayerByNameLong(
				player.tradingWith);
		if (o == null)
			return;
		if (player == null || o == null)
			return;
		final TradingContainer tradeContainer = o.getTradeContainer();
		final TradingContainer tradeContainer_ = player.getTradeContainer();
		for (int i = 0; i < 28; i++) {
			final int amount = tradeContainer.getAmount(i);
			if (amount > 0)
				player.getActionSender().addItem(tradeContainer.getItem(i),
						amount);
			if (tradeContainer != null && tradeContainer_ != null) {
				if (amount > 0)
					if (InstanceDistributor.getItemManager().getItemDefinition(
							tradeContainer.getItem(i)) != null) {
						final String name = InstanceDistributor
								.getItemManager()
								.getItemDefinition(tradeContainer.getItem(i))
								.getName();
						if (name != null) {
							final Date date = new Date();
							Logger.write(
									"["
											+ date
											+ "] "
											+ Misc.capitalizeFirstLetter(o
													.getUsername())
											+ " ["
											+ o.connectedFrom
											+ "] traded "
											+ amount
											+ " "
											+ name
											+ " to the user "
											+ Misc.capitalizeFirstLetter(player
													.getUsername()) + " ["
											+ player.connectedFrom + "]",
									"trades/" + o.getUsername());
							Logger.write(
									"["
											+ date
											+ "] "
											+ Misc.capitalizeFirstLetter(player
													.getUsername())
											+ " ["
											+ player.connectedFrom
											+ "] recieved "
											+ amount
											+ " "
											+ name
											+ " from the user "
											+ Misc.capitalizeFirstLetter(o
													.getUsername()) + " ["
											+ o.connectedFrom + "]", "trades/"
											+ player.getUsername());
						}
					}
			} else
				System.out.println("Cant log trade of " + player.getUsername()
						+ " & " + o.getUsername() + "!");
		}
		tradeContainer.clear();
		player.getActionSender().sendFrame126(
				"Are you sure you want to make this trade?", 3535);
		player.getActionSender().sendWindowsRemoval();
		player.tradingWith = -1;
		player.canOffer = true;
		player.tradeConfirmed = false;
		player.tradeConfirmed2 = false;
		player.acceptedTrade = false;
		player.getActionSender().sendFrame126(
				"Are you sure you want to make this trade?", 3535);
	}

	public String listConfirmScreen(ArrayList<Item> list) {
		player.canOffer = false;
		String sendTrade = "Absolutely nothing!";
		String sendAmount = "";
		int count = 0;
		for (final Item item : list) {
			if (item == null)
				continue;
			if (item.getId() > 0) {
				if (item.getCount() >= 1000 && item.getCount() < 1000000)
					sendAmount = "@cya@" + item.getCount() / 1000 + "K @whi@("
							+ Trading.formatInt(item.getCount()) + ')';
				else if (item.getCount() >= 1000000)
					sendAmount = "@gre@" + item.getCount() / 1000000
							+ " million @whi@("
							+ Trading.formatInt(item.getCount()) + ')';
				else
					sendAmount = Trading.formatInt(item.getCount());
				if (count == 0) {
					sendTrade = "";
					count = 2;
				}
				if (count == 1) {
					String name = "";
					if (item.getDefinition() != null) {
						if (item.getDefinition().getName() != null)
							name = item.getDefinition().getName();
						if (name == "Unarmed")
							name = "";
						sendTrade = sendTrade + "\\n" + name;
					}
				} else if (count == 2) {
					final ItemDefinition itemDef = item.getDefinition();
					String name = "";
					if (itemDef != null)
						name = itemDef.getName();
					sendTrade = sendTrade + ' ' + name != null ? name : "";
					count = 0;
				}
				if (Item.itemStackable[item.getId()])
					sendTrade = sendTrade + " x " + sendAmount;
				sendTrade = sendTrade + "     ";
				count++;
			}
		}
		return sendTrade;
	}

	public void openTrade() {
		final Player o = PlayerManager.getSingleton().getPlayerByNameLong(
				player.requestedWith);
		if (o == null)
			return;

		player.tradingWith = o.encodedName;
		player.requestedWith = -1;
		player.canOffer = true;
		final Item[] items = new Item[28];
		for (int i = 0; i < items.length; i++)
			items[i] = new Item(player.tradeItems[i],
					player.tradeItemAmounts[i]);
		// This player
		player.getActionSender().sendInterfaceInventory(3323, 3321);
		player.getActionSender().sendString(
				"Trading with: " + Misc.capitalizeFirstLetter(o.getUsername())
						+ " who has @gre@" + o.getActionAssistant().freeSlots()
						+ " free slots", 3417);
		player.getActionSender().sendItemReset(3322);
		player.getActionSender().sendString("", 3431);
		player.getActionSender().sendUpdateItems(3415,
				player.getTradeContainer().toArray2());
		player.getActionSender().sendUpdateItems(3416,
				o.getTradeContainer().toArray2());
		// Partner
		o.tradingWith = player.encodedName;
		o.canOffer = true;
		o.requestedWith = -1;
		o.getActionSender().sendInterfaceInventory(3323, 3321);
		o.getActionSender().sendItemReset(3322);
		o.getActionSender().sendString(
				"Trading with: "
						+ Misc.capitalizeFirstLetter(player.getUsername())
						+ " who has @gre@"
						+ player.getActionAssistant().freeSlots()
						+ " free slots", 3417);
		o.getActionSender().sendString("", 3431);
		o.getActionSender().sendUpdateItems(3415,
				o.getTradeContainer().toArray2());
		o.getActionSender().sendUpdateItems(3416,
				player.getTradeContainer().toArray2());
	}

	public void requestTrade(Player o) {
		final Player client = player;
		if (o == null) {
			client.getActionSender().sendMessage(
					"That player is currently not available.");
			return;
		}
		if (Areas.inMiniGame(player) || Areas.inMiniGame(o)) {
			player.getActionSender().sendMessage(
					"You cannot do this in a minigame.");
			return;
		}
		if (player.isBusy() || o.isBusy()) {
			if (o.isBusy())
				client.getActionSender().sendMessage(
						"That player is currently busy");
			else
				client.getActionSender().sendMessage("You're currently busy.");
			return;
		}
		if (player.tradingWith != -1) {
			player.sendMessage("You are already in a trade.");
			return;
		}
		if (o.tradingWith != -1) {
			player.sendMessage("This player is already in a trade.");
			return;
		}
		if (!player.withinDistance(o)) {
			player.sendMessage("You need to get closer to the person you are trading.");
			return;
		}
		if (!player.getDuelStage().equals(DuelStage.WAITING)) {
			client.getActionSender().sendMessage(
					"You cannot do this right now.");
			return;
		}
		player.requestedWith = o.encodedName;
		if (o.requestedWith != player.encodedName) {
			player.sendMessage("Sending trade request...");
			o.sendMessage(Misc.capitalizeFirstLetter(player.getUsername())
					+ ":tradereq:");
			return;
		}
		if (!Trading.canTrade(player, o)) {
			player.sendMessage("You cannot trade here.");
			return;
		}
		if (!Trading.canTrade(o, player)) {
			player.sendMessage("The other person can't trade in this situation..");
			return;
		}
		openTrade();

	}

	public void resetOtherItems(int frame) {
		final Player other = PlayerManager.getSingleton().getPlayerByNameLong(
				player.tradingWith);
		if (other == null) {
			player.getActionSender().sendMessage("Your partner disconnected.");
			return;
		}
		final int[] offer = other.tradeItems;
		final int[] offerN = other.tradeItemAmounts;
		final GamePacketBuilder bldr = new GamePacketBuilder(53,
				Type.VARIABLE_SHORT);
		bldr.putShort(frame);
		bldr.putShort(offer.length);
		for (int i = 0; i < offer.length; i++) {
			if (offerN[i] > 254) {
				bldr.put((byte) 255);
				bldr.putInt2(offerN[i]);
			} else
				bldr.put((byte) offerN[i]);
			bldr.putLEShortA(offer[i]);
		}
		player.write(bldr.toPacket());
	}

	public void tradeItem(int itemID, int fromSlot, int amount) {
		final Player o = PlayerManager.getSingleton().getPlayerByNameLong(
				player.tradingWith);
		if (o == null)
			return;
		if (player.isDisconnected() || o.isDisconnected()) {
			this.declineTrade();
			return;
		}
		if (TradingConstants.isUntradable(itemID) && player.getPrivileges() <2) {
			player.sendMessage("You can't trade this item.");
			return;
		}
		if (o.tradingWith != player.encodedName
				|| player.tradingWith != o.encodedName) {
			this.declineTrade();
			return;
		}
		player.tradeConfirmed = false;
		o.tradeConfirmed = false;
		if (!player.getActionAssistant().playerHasItem(itemID, 1))
			return;
		if (player.playerItems[fromSlot] - 1 != itemID)
			return;
		if (player.tradingWith == -1 || !player.canOffer) {
			this.declineTrade();
			return;
		}
		if (player.getActionAssistant().getItemAmount(itemID) < amount) {
			amount = player.getActionAssistant().getItemCount(itemID);
			if (amount == 0)
				return;
		}
		if (!player.getActionAssistant().playerHasItem(itemID, amount))
			return;
		if (player.getTradeContainer().freeSlots() < amount)
			if (Item.itemStackable[itemID]) {
				if (player.getTradeContainer().freeSlots() == 0)
					if (!player.getTradeContainer().contains(itemID)) {
						player.sendMessage("You cannot offer anymore items");
						return;
					}
			} else {
				amount = player.getTradeContainer().freeSlots();
				if (amount == 0) {
					player.sendMessage("You cannot offer anymore items");
					return;
				}
			}
		final boolean added = player.getTradeContainer().add(
				Item.create(itemID), amount);
		if (!added)
			return;
		if (Item.itemStackable[itemID])
			player.getActionAssistant().deleteItem(itemID, amount);
		else
			for (int i = 0; i < amount; i++)
				player.getActionAssistant().deleteItem(itemID, 1);
		player.getActionSender().sendItemReset(3322);
		o.getActionSender().sendFrame126(
				"Trading with: "
						+ Misc.capitalizeFirstLetter(player.getUsername())
						+ " who has @gre@"
						+ player.getActionAssistant().freeSlots()
						+ " free slots", 3417);
		o.getActionSender().sendUpdateItems(3416,
				player.getTradeContainer().toArray2());
		player.getActionSender().sendUpdateItems(3415,
				player.getTradeContainer().toArray2());
	}
}
