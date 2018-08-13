package com.server2.content.misc;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.net.GamePacketBuilder;

/**
 * 
 * @author Hoodlum
 * 
 */
public class MoneyVault {

	/**
	 * The amount of money stored
	 */
	public long stored = 0;

	/**
	 * The item id of the coins
	 */
	public final int coins = 995;

	/**
	 * checks if player is storing
	 */
	private boolean storing;

	/**
	 * checks if player is removing
	 */
	public boolean removing;

	/**
	 * the amount entered
	 */
	public int amount = 0;

	// Default player instance
	private final Player player;

	/**
	 * Reconstructs the player
	 * 
	 * @param player
	 */

	public MoneyVault(Player player) {
		this.player = player;
	}

	public void deposit(int a, boolean force) {
		if (a <= 0)
			return;
		if (a + player.getVault().stored > Long.MAX_VALUE)
			return;
		if (!player.getActionAssistant().playerHasItem(995, a))
			return;
		if (!force)
			player.getActionAssistant().deleteItem(995, a);
		player.getVault().stored += a;
		player.getActionSender().sendMessage(
				"You add " + a + " coins into your vault!");
		player.getActionSender().sendWindowsRemoval();
	}

	public void examine() {
		player.getActionSender().sendMessage(
				"You currently have " + player.getVault().stored
						+ " coins in your money vault.");
	}

	public boolean getRemoving() {
		return removing;
	}

	/**
	 * Get the total amount stored
	 * 
	 * @return the amount stored
	 */
	public long getStored() {
		return stored;
	}

	public boolean getStoring() {
		return storing;
	}

	public void handleOptions(int option) {
		switch (option) {
		case 0:
			examine();
			player.getActionSender().sendWindowsRemoval();
			break;
		case 1:
			player.write(new GamePacketBuilder(27).toPacket());
			player.getVault().storing = true;
			break;
		case 2:
			player.write(new GamePacketBuilder(27).toPacket());
			player.getVault().removing = true;
			break;
		}
	}

	public void withdraw(int a) {
		player.write(new GamePacketBuilder(27).toPacket());
		if (amount <= 0)
			return;
		else if (a > Integer.MAX_VALUE)
			return;
		else if (amount + player.getActionAssistant().getItemAmount(995) > Integer.MAX_VALUE) {
			player.getActionSender().sendMessage(
					"You cannot carry that many coins in your inventory.");
			return;
		} else if (player.getVault().stored == 0) {
			player.getActionSender().sendFrame126(
					"There is no money in your vault..", 358);
			return;
		} else if (player.getVault().stored
				+ player.getActionAssistant().getItemAmount(995) > Integer.MAX_VALUE)
			player.getActionSender().sendMessage(
					"You cannot carry that many coins!");
		else if (player.getActionAssistant().playerHasItem(995))
			player.getVault().stored -= a;
		if (player.getActionAssistant().getItemAmount(995) >= Integer.MAX_VALUE)
			player.getActionSender().addItem(995,
					PlayerConstants.MAX_ITEM_AMOUNT);
		player.getActionSender().addItem(995, amount);
	}

}
