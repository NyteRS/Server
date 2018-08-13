package com.server2.content.minigames;

import java.util.Date;

import com.server2.Constants;
import com.server2.InstanceDistributor;
import com.server2.content.Achievements;
import com.server2.content.TradingConstants;
import com.server2.content.anticheat.DupePrevention;
import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.Container;
import com.server2.model.Container.Type;
import com.server2.model.Item;
import com.server2.model.combat.additions.Specials;
import com.server2.model.combat.magic.AutoCast;
import com.server2.model.entity.Location;
import com.server2.model.entity.player.Equipment;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.Player.DuelStage;
import com.server2.util.Logger;
import com.server2.util.Misc;
import com.server2.world.PlayerManager;

/**
 * A class that handles the Duel Arena.
 * 
 * @author Rene
 */

public class DuelArena {

	/**
	 * An instance of the DuelArena.
	 */
	private static DuelArena INSTANCE = new DuelArena();

	/**
	 * The position you go to when you lose a duel.
	 */
	public static final Location DEATH_RESPAWN_POINT = new Location(
			3363 + Misc.random(7), 3270 - Misc.random(7));

	/**
	 * The position you go to when you win a duel.
	 */
	private static final Location WIN_RESPAWN_POINT = new Location(
			3363 + Misc.random(7), 3270 - Misc.random(7));

	/**
	 * Duel arena rule ids.
	 */
	public static final int[] RULE_IDS = { 1, 2, 16, 32, 64, 128, 256, 512,
			1024, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288,
			2097152, 8388608, 16777216, 67108864, 134217728 };

	/**
	 * Duel Rule Names
	 */
	public static final String[] RULE_NAMES = new String[] {
			"You cannot forfeit the duel.", "You cannot move.",
			"You cannot use ranged attacks.", "You cannot use melee attacks.",
			"You cannot use magic attacks.", "You cannot use drinks.",
			"You cannot use food.", "You cannot use prayer." };

	/**
	 * Duel arena locations.
	 */
	public static final Location[] SPAWN_POSITIONS = {
			new Location(3338, 3248), new Location(3335, 3253),
			new Location(3340, 3250), new Location(3343, 3247),
			new Location(3345, 3250), new Location(3345, 3255),
			new Location(3350, 3254), new Location(3354, 3254),
			new Location(3354, 3251), new Location(3353, 3247),
			new Location(3341, 3257) };

	/**
	 * Spawn location when playing in an arena with the obstacle rule enabled.
	 */
	public static final Location[] OBSTACLE_SPAWN_POSITION = {
			new Location(3382, 3247), new Location(3378, 3256),
			new Location(3375, 3249), new Location(3368, 3251) };

	/**
	 * Gets the equipment slot for a rule.
	 * 
	 * @param ruleId
	 * @return
	 */
	public static int getEquipmentSlotForRule(int ruleId) {
		int slot = ruleId - 11;
		switch (ruleId) {
		case 17:
			slot += 1;
			break;
		case 18:
		case 19:
			slot += 2;
			break;
		case 20:
		case 21:
			slot += 3;
			break;
		}
		return slot;
	}

	/**
	 * @return an instance of the duel arena.
	 */
	public static DuelArena getInstance() {
		return INSTANCE;
	}

	/**
	 * 
	 * @param button
	 * @return
	 */
	public static int getRuleForButton(int button) {
		switch (button) {
		case 6696: // no forfeit
		case 6721:
			return 0;
		case 6704: // TODO no movement
		case 6722:
			return 1;
		case 6698: // no range
		case 6725:
			return 2;
		case 6699: // no melee
		case 6726:
			return 3;
		case 6697: // no mage
		case 6727:
			return 4;
		case 6701: // no drinks
		case 6728:
			return 5;
		case 6702: // no food
		case 6729:
			return 6;
		case 6703: // no prayer
		case 6730:
			return 7;
		case 6731: // TODO obstacles
		case 6732:
			return 8;
		case 669: // TODO fun weapons
		case 670:
			return 9;
		case 7817: // no special attacks
		case 7816:
			return 10;
		case 13813: // no helmet
			return 11;
		case 13814: // no cape
			return 12;
		case 13815: // no amulet
			return 13;
		case 13817: // no weapon
			return 14;
		case 13818: // no body
			return 15;
		case 13819: // no shield
			return 16;
		case 13820: // no legs
			return 17;
		case 13823: // no gloves
			return 18;
		case 13822: // no boots
			return 19;
		case 13821: // no rings
			return 20;
		case 13816: // no arrows
			return 21;
		}
		return -1;
	}

	/**
	 * Accepts stage one of the duel
	 * 
	 * @param player
	 */
	public void acceptStageOne(Player player) {
		final Player other = PlayerManager.getDuelOpponent(player);
		if (other == null)
			return;
		if (player.getDuelRules()[1] && player.getDuelRules()[8]) {
			player.getActionSender().sendString(
					"You cannot have no movement and obstacles.", 6684);

			other.getActionSender().sendString(
					"You cannot have no movement and obstacles.", 6684);

			return;
		}
		if (other.getActionAssistant().freeSlots() < calculateRequiredSpace(other)) {
			player.getActionSender().sendString(
					"Your opponent doesn't have enough space.", 6684);

			other.getActionSender().sendString("You don't have enough space.",
					6684);

			return;
		}
		if (player.getActionAssistant().freeSlots() < calculateRequiredSpace(player)) {
			other.getActionSender().sendString(
					"Your opponent doesn't have enough space.", 6684);

			player.getActionSender().sendString("You don't have enough space.",
					6684);

			return;
		}
		player.setDuelStage(DuelStage.ACCEPT);
		if (!other.getDuelStage().equals(DuelStage.ACCEPT)) {
			player.getActionSender().sendString("Waiting for other player...",
					6684);

			other.getActionSender().sendString("Other player accepted.", 6684);

		} else {
			player.getActionSender().sendInterfaceInventory(6412, 197);
			other.getActionSender().sendInterfaceInventory(6412, 197);
			player.setDuelStage(DuelStage.SECOND_DUEL_WINDOW);
			other.setDuelStage(DuelStage.SECOND_DUEL_WINDOW);
			showSecondScreen(player);
			showSecondScreen(other);
		}
	}

	/**
	 * Accept the duel!
	 * 
	 * @param player
	 */
	public void acceptStageTwo(Player player) {
		if (!player.getDuelStage().equals(DuelStage.SECOND_DUEL_WINDOW))
			return;
		final Player other = PlayerManager.getDuelOpponent(player);
		if (other == null) {
			player.getActionSender().sendMessage(
					"The other player is not available anymore.");
			declineDuel(player);
			return;
		}
		player.setDuelStage(DuelStage.ACCEPT);
		if (!other.getDuelStage().equals(DuelStage.ACCEPT)) {
			player.getActionSender().sendString("Waiting for other player...",
					6571);

			other.getActionSender().sendString("Other player accepted.", 6571);

		} else {
			player.setDuelStage(DuelStage.INSIDE_DUEL);
			other.setDuelStage(DuelStage.INSIDE_DUEL);
			player.getActionSender().sendMessage("You accept the duel.");
			other.getActionSender().sendMessage("You accept the duel.");
			player.getActionSender().sendWindowsRemoval();
			other.getActionSender().sendWindowsRemoval();
			startDuel(player, other);
			startDuel(other, player);
		}
	}

	/**
	 * Awards the winner of hte items with the stakes.
	 * 
	 * @param winner
	 * @param loser
	 */
	public void awardWin(Player winner, Player loser) {
		DupePrevention.checkDupe(winner);
		DupePrevention.checkDupe(loser);
		winner.setDeadLock(5);
		loser.setDeadLock(5);
		loser.isDueling = false;
		winner.isDueling = false;
		// Reset the stats of the winner.
		winner.restoreStats();
		winner.setDuelStage(DuelStage.WAITING);
		winner.getActionSender().sendLocationPointerPlayer(0, 0);
		winner.teleport(DuelArena.WIN_RESPAWN_POINT);
		loser.setDuelOpponent(null);
		winner.setDuelOpponent(null);
		winner.getPrayerHandler().resetAllPrayers();
		winner.setSpecialAmount(100);
		loser.setSpecialAmount(100);
		AutoCast.turnOff(winner);
		AutoCast.turnOff(loser);
		winner.setFreezeDelay(0);
		loser.setFreezeDelay(0);
		loser.getPrayerHandler().resetAllPrayers();
		winner.poisonDelay = -1;
		winner.poisonDamage = -1;
		loser.poisonDelay = -1;
		loser.poisonDamage = -1;
		winner.duelingTarget = -1;
		loser.duelingTarget = -1;
		loser.finalDuelOpponent = -1;
		winner.finalDuelOpponent = -1;
		winner.duelRequested = false;
		loser.duelRequested = false;
		// Show the win interface.
		winner.getActionSender().sendString(
				Integer.toString(loser.getCombatLevel()), 6839);

		winner.getActionSender().sendString(loser.getUsername(), 6840);

		winner.getActionSender().sendUpdateItems(6822,
				loser.getDuel().toArray());
		winner.getActionSender().sendInterface(6733);

		// Give back my items.
		for (final Item item : winner.getDuel().toArray()) {
			if (item == null)
				continue;
			winner.getActionSender().addItem(item.getId(), item.getCount());
		}
		winner.getDuel().clear();

		// Get the looser's items
		for (final Item item : loser.getDuel().toArray()) {
			if (item == null)
				continue;
			winner.getActionSender().addItem(item.getId(), item.getCount());
			if (InstanceDistributor.getItemManager().getItemDefinition(
					item.getId()) != null) {
				final String name = InstanceDistributor.getItemManager()
						.getItemDefinition(item.getId()).getName();
				final int amount = item.getCount();
				if (name != null) {
					final Date date = new Date();
					Logger.write(
							"["
									+ date
									+ "] "
									+ Misc.capitalizeFirstLetter(loser
											.getUsername())
									+ " ["
									+ loser.connectedFrom
									+ "] lost: "
									+ name
									+ " amount: "
									+ amount
									+ " to the user: "
									+ Misc.capitalizeFirstLetter(winner
											.getUsername()) + " ["
									+ winner.connectedFrom + "]", "duels/"
									+ loser.getUsername());
					Logger.write(
							"["
									+ date
									+ "] "
									+ Misc.capitalizeFirstLetter(winner
											.getUsername())
									+ " ["
									+ winner.connectedFrom
									+ "] won: "
									+ name
									+ " amount: "
									+ amount
									+ " from the user: "
									+ Misc.capitalizeFirstLetter(loser
											.getUsername()) + "["
									+ loser.connectedFrom + "]", "duels/"
									+ winner.getUsername());
				}
			}
		}
		loser.getDuel().clear();
		Achievements.getInstance().complete(winner, 34);
	}

	/**
	 * Calculates the amount of spaces required for the duel.
	 * 
	 * @param player
	 * @return
	 */
	public int calculateRequiredSpace(Player player) {
		final Player other = PlayerManager.getDuelOpponent(player);
		;
		if (other == null)
			return Integer.MAX_VALUE;
		return player.getDuelArmorSpacesRequired() + other.getDuel().size();
	}

	/**
	 * Checks if the player is allow to equip in accordance to the rules.
	 * 
	 * @param item
	 * @param targetSlot
	 * @param player
	 * @return
	 */
	public boolean checkEquipment(int itemId, int targetSlot, Player player) {
		for (int i = 11; i <= 21; i++) {
			final int j = DuelArena.getEquipmentSlotForRule(i);
			if (player.getDuelRules()[i])
				if (j == targetSlot)
					return false;
			if (j == Equipment.SLOT_WEAPON)
				if (player.getEquipment().twoHanded(itemId)
						&& player.getDuelRules()[16])
					return false;
		}
		return true;
	}

	/**
	 * Declines the duel.
	 * 
	 * @param player
	 */
	public void declineDuel(Player player) {
		if (player.getDuelStage() == DuelStage.WAITING)
			return;
		final Player other = PlayerManager.getDuelOpponent(player);
		if (other == null)
			return;
		if (player.getDuelStage().equals(DuelStage.INSIDE_DUEL)
				|| other.getDuelStage().equals(DuelStage.INSIDE_DUEL))
			return;
		if (other.getDuelStage().equals(DuelStage.SEND_REQUEST_ACCEPT)
				|| other.getDuelStage().equals(DuelStage.SECOND_DUEL_WINDOW))
			other.getActionSender().sendMessage(
					"Other player has declined the duel.");
		player.setDuelStage(DuelStage.WAITING);
		other.setDuelStage(DuelStage.WAITING);
		player.getActionSender().sendWindowsRemoval();
		other.getActionSender().sendWindowsRemoval();
		player.resetFaceDirection();
		other.resetFaceDirection();
		player.duelingTarget = -1;
		other.duelingTarget = -1;
		other.setDuelOpponent(null);
		player.setDuelOpponent(null);
		returnItems(player, other);
	}

	/**
	 * Handles a duel arena duel request.
	 * 
	 * @param player
	 * @param other
	 */
	public void handleDuelRequest(Player player, Player other) {
		if (System.currentTimeMillis() - player.lastDeath < 2000
				|| System.currentTimeMillis() - other.lastDeath < 2000)
			return;
		if (!Constants.DUEL || !Constants.MINIGAME) {
			player.sendMessage("Dueling has currently been disabled!");
			return;
		}
		if (player.getUsername().equalsIgnoreCase("five grand"))
			return;
		if (other.getUsername().equalsIgnoreCase("five grand"))
			return;
		if (player.familiarId > 0) {
			player.getActionSender().sendMessage(
					"You cannot use familiars in the duel arena.");
			return;
		}
		if (other.familiarId > 0) {
			player.getActionSender().sendMessage(
					"Your opponent has a familiar, you cannot duel him.");
			return;
		}
		if (other.getDuelStage() == DuelStage.SEND_REQUEST_ACCEPT) {
			player.getActionSender().sendMessage(
					"Your opponent is currently in a duel.");
			return;
		}
		if (!player.getDuelStage().equals(DuelStage.WAITING))
			declineDuel(player);
		player.duelingTarget = other.encodedName;
		player.duelRequested = true;
		if (player.getDuelStage() == DuelStage.WAITING
				&& other.getDuelStage() == DuelStage.WAITING
				&& player.duelRequested && other.duelRequested
				&& player.duelingTarget == other.encodedName
				&& other.duelingTarget == player.encodedName) {
			if (Misc.goodDistance(player.getAbsX(), player.getAbsY(),
					other.getAbsX(), other.getAbsY(), 1)) {
				player.setDuelStage(DuelStage.SEND_REQUEST_ACCEPT);
				other.setDuelStage(DuelStage.SEND_REQUEST_ACCEPT);
				refresh(player, other, new int[] { 0, 1, 2 });
				openDuelInterface(player, other);
				openDuelInterface(other, player);
				player.setDuelOpponent(other);
				other.setDuelOpponent(player);
			} else
				player.sendMessage("You need to get closer to your opponent to start the duel.");
		} else {
			player.getActionSender().sendMessage("Sending duel request...");
			other.getActionSender().sendMessage(
					Misc.capitalizeFirstLetter(player.getUsername())
							+ ":duelreq:");

		}
	}

	/**
	 * Handle the forfeit object.
	 * 
	 * @param player
	 */
	public void handleForfeit(Player player) {
		/*
		 * if(player.logoutDelay > 0 || player.getTarget() != null) {
		 * player.getActionSender
		 * ().sendMessage("You need to be out of combat for atleast 10 seconds."
		 * ); return; } player.isDueling = false; final Player opponent =
		 * player.getDuelOpponent(); player.restoreStats();
		 * player.setDuelStage(DuelStage.WAITING);
		 * player.getActionSender().sendLocationPointerPlayer(0, 0);
		 * player.teleport(DuelArena.DEATH_RESPAWN_POINT);
		 * player.setDuelOpponent(null); if(opponent != null) {
		 * opponent.isDueling = false; player.duelDeathPrevention =
		 * System.currentTimeMillis(); opponent.duelDeathPrevention =
		 * System.currentTimeMillis(); this.awardWin(opponent, player); }
		 */
		player.getActionSender().sendMessage("This is being added later.");
	}

	/**
	 * Called when the player dies in a duel.
	 * 
	 * @param player
	 */
	public void onDeath(Player player) {
		player.isDueling = false;
		final Player opponent = PlayerManager.getDuelOpponent(player);

		player.setDuelStage(DuelStage.WAITING);
		player.getActionSender().sendLocationPointerPlayer(0, 0);
		player.teleport(DuelArena.DEATH_RESPAWN_POINT);

		if (opponent != null)
			awardWin(opponent, player);

		player.setDuelOpponent(null);
	}

	/**
	 * Called upon a client's disconnection.
	 * 
	 * @param player
	 */
	public void onDisconnect(Player player) {
		final Player opponent = PlayerManager.getDuelOpponent(player);

		if (opponent == null)
			declineDuel(player);
		else
			awardWin(opponent, player);
	}

	/**
	 * Opens the duel interface.
	 * 
	 * @param player
	 */
	public void openDuelInterface(Player player, Player other) {
		player.getActionSender().sendString(
				"Dueling with: " + other.getUsername(), 6671);

		player.getActionSender().sendString("", 6684);

		player.getActionSender().sendString("", 6571);

		final Container temp = new Container(Type.STANDARD, 12);
		for (int i = 0; i < temp.capacity(); i++)
			temp.add(new Item(player.playerEquipment[i],
					player.playerEquipmentN[i]), i);
		player.getActionSender().sendUpdateItems(13824, temp.toArray());
		for (int i = 0; i < player.getDuelRules().length; i++)
			player.getDuelRules()[i] = false;
		updateDuelOptions(player);
		player.getActionSender().sendInterface(6575);
		player.getActionSender().sendInterfaceInventory(6575, 3321);
	}

	/**
	 * Refreshes items
	 * 
	 * @param player
	 * @param other
	 * @param toRefresh
	 */
	public void refresh(Player player, Player other, int[] toRefresh) {
		for (final int element : toRefresh)
			switch (element) {
			case 0: // Update inventory (default)
				player.getActionSender().sendItemReset();
				other.getActionSender().sendItemReset();
				break;
			case 1: // Update inventory (duel open)
				player.getActionSender().sendItemReset(3322);
				other.getActionSender().sendItemReset(3322);
				break;
			case 2: // Update stake offers
				player.getActionSender().sendUpdateItems(6669,
						player.getDuel().toArray());
				other.getActionSender().sendUpdateItems(6669,
						other.getDuel().toArray());
				player.getActionSender().sendUpdateItems(6670,
						other.getDuel().toArray());
				other.getActionSender().sendUpdateItems(6670,
						player.getDuel().toArray());
				break;
			}
	}

	/**
	 * Remove player equipment in accordance to the set rules.
	 * 
	 * @param player
	 */
	public void removePlayerEquipment(Player player) {
		for (int i = 11; i <= 21; i++) {
			final int slot = DuelArena.getEquipmentSlotForRule(i);
			if (!player.getDuelRules()[i])
				continue;
			final int id = player.playerEquipment[Equipment.SLOT_WEAPON];
			if (slot == Equipment.SLOT_SHIELD)
				if (id != -1 && player.getEquipment().twoHanded(id))
					player.getEquipment().removeItem(id, Equipment.SLOT_WEAPON);
			player.getEquipment().removeItem(id, slot);

		}
	}

	/**
	 * Removes an item from the stakes.
	 * 
	 * @param player
	 * @param slot
	 * @param duelItem
	 * @param amount
	 */
	public void removeStakedItem(Player player, int slot, int duelItem,
			int amount) {
		if (amount == 0) {
			player.getActionSender().sendMessage(
					"You cannot have an amount of 0.");
			return;
		}

		final Player other = PlayerManager.getDuelOpponent(player);
		;
		if (other == null
				|| player.getDuelStage().equals(DuelStage.INSIDE_DUEL)) {
			player.getActionSender().sendMessage(
					"You cannot do this right now.");
			return;
		}
		if (other == null
				|| player.getDuelStage().equals(DuelStage.SECOND_DUEL_WINDOW))
			return;

		player.setDuelStage(DuelStage.SEND_REQUEST_ACCEPT);
		other.setDuelStage(DuelStage.SEND_REQUEST_ACCEPT);
		player.getActionSender().sendString("", 6684);

		other.getActionSender().sendString("", 6684);

		final Item item = player.getDuel().get(slot);
		if (item == null || item.getId() != duelItem)
			return; // invalid packet, or client out of sync
		int transferAmount = player.getDuel().getCount(duelItem);
		if (transferAmount >= amount)
			transferAmount = amount;
		else if (transferAmount == 0)
			return; // invalid packet, or client out of sync

		if (Item.itemStackable[duelItem] || Item.itemIsNote[duelItem]) { // try
																			// nows
			if (player.getActionSender().addItem(duelItem, transferAmount))
				player.getDuel().remove(new Item(item.getId(), transferAmount));
		} else
			for (int i = 0; i < transferAmount; i++)
				if (player.getActionSender().addItem(duelItem, 1))
					player.getDuel().remove(new Item(item.getId())); // GOTTA
																		// ADD
																		// DIS
																		// METHOD

		player.getDuel().shift();
		refresh(player, other, new int[] { 0, 1, 2 });
	}

	/**
	 * Declines the duel if the player walks away.
	 * 
	 * @param player
	 */
	public void resetOnWalk(Player player) {
		if (PlayerManager.getDuelOpponent(player) != null
				|| player.getDuelStage().equals(DuelStage.WAITING)
				|| player.getDuelStage().equals(DuelStage.INSIDE_DUEL))
			return;

		declineDuel(player);
	}

	/**
	 * Gives back items to both parties.
	 * 
	 * @param player
	 * @param other
	 */
	public void returnItems(Player player, Player other) {
		for (final Item item : player.getDuel().toArray()) {
			if (item == null)
				continue;
			player.getActionSender().addItem(item.getId(), item.getCount());
		}
		player.getDuel().clear();
		for (final Item item : other.getDuel().toArray()) {
			if (item == null)
				continue;
			other.getActionSender().addItem(item.getId(), item.getCount());
		}
		other.getDuel().clear();

	}

	/**
	 * Shows the duel confirm screen.
	 * 
	 * @param player
	 */
	public void showSecondScreen(Player player) {
		final Player otherPlayer = PlayerManager.getDuelOpponent(player);
		;
		if (otherPlayer == null)
			return;
		final StringBuilder myStake = new StringBuilder();
		boolean empty = true;
		for (int i = 0; i < 28; i++) {
			final Item item = player.getDuel().get(i);
			String prefix = "";
			if (item != null) {
				empty = false;
				if (item.getCount() >= 100 && item.getCount() < 1000000)
					prefix = "@cya@" + item.getCount() / 1000 + "K @whi@("
							+ item.getCount() + ")";
				else if (item.getCount() >= 1000000)
					prefix = "@gre@" + item.getCount() / 1000000
							+ " million @whi@(" + item.getCount() + ")";
				else
					prefix = "" + item.getCount();
				myStake.append(item.getDefinition().getName());
				myStake.append(" x ");
				myStake.append(prefix);
				myStake.append("\\n");
			}
		}
		if (empty)
			myStake.append("Absolutely nothing!");
		player.getActionSender().sendString(myStake.toString(), 6516);

		final StringBuilder opponentStake = new StringBuilder();
		empty = true;
		for (int i = 0; i < 28; i++) {
			final Item item = otherPlayer.getDuel().get(i);
			String prefix = "";
			if (item != null) {
				empty = false;
				if (item.getCount() >= 100 && item.getCount() < 1000000)
					prefix = "@cya@" + item.getCount() / 1000 + "K @whi@("
							+ item.getCount() + ")";
				else if (item.getCount() >= 1000000)
					prefix = "@gre@" + item.getCount() / 1000000
							+ " million @whi@(" + item.getCount() + ")";
				else
					prefix = "" + item.getCount();
				opponentStake.append(item.getDefinition().getName());
				opponentStake.append(" x ");
				opponentStake.append(prefix);
				opponentStake.append("\\n");
			}
		}
		if (empty)
			opponentStake.append("Absolutely nothing!");
		player.getActionSender().sendString(opponentStake.toString(), 6517);

		player.getActionSender().sendString("", 8242);

		for (int i = 8238; i <= 8253; i++)
			player.getActionSender().sendString("", i);
		player.getActionSender()
				.sendString("Hitpoints will be restored.", 8250);

		player.getActionSender().sendString("Boosted stats will be restored.",
				8238);

		int lineNumber = 8239;
		for (int i = 10; i < player.getDuelRules().length; i++)
			if (player.getDuelRules()[i]) {
				player.getActionSender().sendString(
						"Some worn items will be taken off.", lineNumber);

				lineNumber++;
				break;
			}
		if (player.getDuelRules()[8])
			player.getActionSender().sendString(
					"There will be obstacles in the arena.", lineNumber);
		lineNumber = 8242;
		for (int i = 0; i < 8; i++)
			if (player.getDuelRules()[i]) {
				player.getActionSender().sendString(RULE_NAMES[i], lineNumber);

				lineNumber++;
			}
		player.getActionSender().sendInterfaceInventory(6412, 197);
	}

	/**
	 * Add an item to the stakes.
	 * 
	 * @param player
	 * @param slot
	 * @param duelItem
	 * @param amount
	 */
	public void stakeItem(Player player, int slot, int duelItem, int amount) {
		if (amount == 0) {
			player.getActionSender().sendMessage(
					"You cannot have an amount of 0.");
			return;
		}

		final Player other = PlayerManager.getDuelOpponent(player);
		if (other == null
				|| player.getDuelStage().equals(DuelStage.SECOND_DUEL_WINDOW)) {
			System.out.println(other == null ? "other = null" : "duelstage");
			return;
		}

		if (player.playerItems[slot] - 1 != duelItem)
			return; // invalid packet, or client out of sync
		if (TradingConstants.isUntradable(duelItem)) {
			player.getActionSender().sendMessage("You cannot stake this item.");
			return;
		}

		player.setDuelStage(DuelStage.SEND_REQUEST_ACCEPT);
		other.setDuelStage(DuelStage.SEND_REQUEST_ACCEPT);
		player.getActionSender().sendString("", 6684);

		other.getActionSender().sendString("", 6684);

		int transferAmount = player.getActionAssistant()
				.getItemAmount(duelItem);
		if (transferAmount >= amount)
			transferAmount = amount;
		else if (transferAmount == 0)
			return; // invalid packet, or client out of sync

		if (player.getDuel().add(new Item(duelItem, transferAmount), -1))
			player.getActionAssistant().deleteItem2(duelItem, transferAmount);

		refresh(player, other, new int[] { 0, 1, 2 });
	}

	/**
	 * Start the duel!
	 * 
	 * @param player
	 * @param other
	 */
	public void startDuel(final Player player, Player other) {
		player.duelStart = System.currentTimeMillis();
		other.duelStart = System.currentTimeMillis();
		player.isDueling = true;
		other.isDueling = true;
		player.overLoaded = false;
		other.overLoaded = false;
		Specials.turnOff(player);
		Specials.turnOff(other);
		player.restoreStats();
		other.restoreStats();
		AutoCast.turnOff(player);
		AutoCast.turnOff(other);
		player.setSpecialAmount(100);
		other.setSpecialAmount(100);
		player.skullTimer = 0;
		player.skulledOn = -1;
		player.setWildernessLevel(-1);
		player.setFreezeDelay(0);
		other.skullTimer = 0;
		other.skulledOn = -1;
		other.setWildernessLevel(-1);
		other.setFreezeDelay(0);
		player.setVeng(false);
		other.setVeng(false);
		Specials.updateSpecialBar(player);
		Specials.updateSpecialBar(other);
		player.resetFaceDirection();
		player.setDuelOpponent(other);
		removePlayerEquipment(player);
		player.getPrayerHandler().resetAllPrayers();
		other.getPrayerHandler().resetAllPrayers();
		removePlayerEquipment(other);
		teleportToArena(player, other);
		player.canBeAttacked = false;
		player.finalDuelOpponent = player.duelingTarget;
		player.getActionSender()
				.sendLocationPointerPlayer(10, other.getIndex());
		player.gameStarts = 3;
		player.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {

				if (player.gameStarts > 0)
					player.getActionAssistant().forceText(
							Integer.toString(player.gameStarts));
				else {
					player.getActionAssistant().forceText("FIGHT!");
					player.canBeAttacked = true;
					container.stop();
					return;
				}
				player.gameStarts--;
			}

			@Override
			public void stop() {
				// TODO Auto-generated method stub

			}
		}, 2);

	}

	/**
	 * Handles the teleporting in case of no movement.
	 * 
	 * @param player
	 * @param other
	 */
	public void teleportToArena(Player player, Player other) {
		if (player.getDuelRules()[8]) {
			final int rIndex1 = Misc.random(OBSTACLE_SPAWN_POSITION.length - 1);
			int rIndex2 = Misc.random(OBSTACLE_SPAWN_POSITION.length - 1);
			while (rIndex1 == rIndex2)
				rIndex2 = Misc.random(OBSTACLE_SPAWN_POSITION.length - 1);
			player.teleport(OBSTACLE_SPAWN_POSITION[rIndex1]);
			other.teleport(OBSTACLE_SPAWN_POSITION[rIndex2]);
		} else if (player.getDuelRules()[1]) {
			final int rIndex1 = Misc.random(SPAWN_POSITIONS.length - 1);
			player.teleport(SPAWN_POSITIONS[rIndex1]);
			other.teleport(SPAWN_POSITIONS[rIndex1].transform(1, 0, 0));
		} else {
			final int rIndex1 = Misc.random(SPAWN_POSITIONS.length - 1);
			int rIndex2 = Misc.random(SPAWN_POSITIONS.length - 1);
			while (rIndex1 == rIndex2)
				rIndex2 = Misc.random(SPAWN_POSITIONS.length - 1);
			player.teleport(SPAWN_POSITIONS[rIndex1]);
			other.teleport(SPAWN_POSITIONS[rIndex2]);
		}
	}

	/**
	 * Handles the toggle of rules.
	 * 
	 * @param button
	 * @param player
	 */
	public void toggleDuelRule(int button, Player player) {
		final int ruleId = getRuleForButton(button);
		if (ruleId == -1)
			return;
		final Player other = PlayerManager.getDuelOpponent(player);
		if (other == null)
			return;
		if (player.getDuelStage().equals(DuelStage.INSIDE_DUEL)
				|| other.getDuelStage().equals(DuelStage.INSIDE_DUEL))
			return;

		player.getDuelRules()[ruleId] = !player.getDuelRules()[ruleId];
		updateDuelOptions(player);
		other.getDuelRules()[ruleId] = !other.getDuelRules()[ruleId];
		updateDuelOptions(other);

		player.setDuelStage(DuelStage.SEND_REQUEST_ACCEPT);
		other.setDuelStage(DuelStage.SEND_REQUEST_ACCEPT);
		player.getActionSender().sendString("", 6684);

		other.getActionSender().sendString("", 6684);

	}

	/**
	 * Updates the duel options.
	 * 
	 * @param player
	 */
	public void updateDuelOptions(Player player) {
		int interfaceConfig = 0;
		for (int i = 0; i < player.getDuelRules().length; i++)
			interfaceConfig += player.getDuelRules()[i] ? RULE_IDS[i] : 0;
		player.getActionSender().sendFrame87(286, interfaceConfig);
	}

}