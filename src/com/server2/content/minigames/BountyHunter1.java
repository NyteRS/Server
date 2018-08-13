package com.server2.content.minigames;

import java.util.ArrayList;
import java.util.List;

import com.server2.InstanceDistributor;
import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.combat.CombatEngine;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;
import com.server2.world.PlayerManager;

/**
 * 
 * @author Hoodlum
 * 
 *         A class which handles the bounty hunter minigame.
 */
public class BountyHunter1 {
	/**
	 * The low level crater players
	 */
	public static List<Player> lowLevelCrater = new ArrayList<Player>();

	/**
	 * The medium level crater players
	 */
	public static List<Player> mediumLevelCrater = new ArrayList<Player>();

	/**
	 * The high level crater players
	 */
	public static List<Player> highLevelCrater = new ArrayList<Player>();

	/**
	 * The brown skull
	 */
	private final static int BROWN_SKULL = 2;

	/**
	 * The silver skull
	 */
	private final static int SILVER_SKULL = 3;

	/**
	 * The green skull
	 */
	private final static int GREEN_SKULL = 4;

	/**
	 * The blue skull
	 */
	private final static int BLUE_SKULL = 5;

	/**
	 * The red skull
	 */
	private final static int RED_SKULL = 6;

	/**
	 * The entering coordinates
	 */
	public static final int[][] ENTER = { { 3163, 3696 }, { 3171, 3701 },
			{ 3180, 3708 }, { 3181, 3720 }, { 3171, 3737 }, { 3170, 3746 },
			{ 3163, 3753 }, { 3147, 3758 }, { 3135, 3758 }, { 3121, 3754 },
			{ 3110, 3747 }, { 3091, 3735 }, { 3086, 3717 }, { 3091, 3706 },
			{ 3096, 3692 }, { 3101, 3682 }, { 3108, 3671 }, { 3124, 3665 },
			{ 3138, 3669 }, { 3146, 3681 }, };

	/**
	 * Gets the reward
	 * 
	 * @param player
	 *            the player
	 * @return the reward
	 */
	/*
	 * public static int getPlayerReward(Player player) { int itemId =
	 * itemId.getId(); int carrying =
	 * getTotalCarrying(InstanceDistributor.getGeneralStore() .itemPrice(itemId)
	 * * 0.75); int reward = carrying / 10; if (reward < 10000) { reward =
	 * 10000; } return reward; }
	 */

	/**
	 * The leaving coordinates
	 */
	public static final int[][] LEAVE = { { 3152, 3672 }, { 3158, 3680 },
			{ 3164, 3685 }, };

	public static void bountyBoard(Player player) {

	}

	public static boolean checkInCombat(Player player) {
		if (player.inBountyHunterCombat()) {
			final Player p2 = PlayerManager.getSingleton().getPlayers()[player
					.getIndex()];
			if (p2 == null)
				return false;
			if (p2.getInCombatWith() != null
					&& p2.getInCombatWith().getIndex() != player.getIndex()) {
				player.sendMessage("You cannot attack this player as he is already in combat and not your target!");
				player.stopMovement();
				CombatEngine.resetAttack(player, true);
				return false;
			}
			if (p2.getInCombatWith() != null
					&& p2.getInCombatWith().getIndex() != player.ID) {
				final Player p3 = PlayerManager.getSingleton().getPlayers()[p2
						.getInCombatWith().getIndex()];
				if (p3 != null)
					if (p2.getInCombatWith() != null
							&& player.bountyTarget != p2.ID
							&& p2.bountyTarget != player.ID
							&& player.getInCombatWith().getIndex() != p2.ID) {
						player.stopMovement();
						CombatEngine.resetAttack(player, true);
						player.sendMessage("That player is already in combat!");
						return false;
					}

			}
			if (PlayerManager.getSingleton().getPlayers()[player.getIndex()].publicCrater != player.publicCrater) {
				player.sendMessage("You cannot attack this combat level range!");
				player.stopMovement();
				CombatEngine.resetAttack(player, true);
				return false;
			}
		}
		if (player.inBountySafe()) {
			player.stopMovement();
			CombatEngine.resetAttack(player, true);
			return false;
		}
		return true;
	}

	private static void decreaseLeavePenalty(Player player) {
		player.leavePenalty -= 3;
		if (player.leavePenalty == 0) {
			player.killedWrongTarget = false;
			player.sendMessage("You are no longer effected by the penalty.");
		}
	}

	private static void decreasePickupPenalty(Player player) {
		player.pickupPenalty -= 3;
		if (player.pickupPenalty == 0) {
			player.killedWrongTarget = false;
			player.sendMessage("You are no longer effected by the penalty.");
		}
	}

	public static boolean enterCave(final Player player, final int cave) {
		if (System.currentTimeMillis() - player.getLastAction() < 50000)
			return false;
		for (int i = 14485; i < 30500; i++)
			if (player.playerEquipment[0] == i
					|| player.playerEquipment[1] == i
					|| player.playerEquipment[2] == i
					|| player.playerEquipment[3] == i
					|| player.playerEquipment[4] == i
					|| player.playerEquipment[5] == i
					|| player.playerEquipment[6] == i
					|| player.playerEquipment[7] == i
					|| player.playerEquipment[8] == i
					|| player.playerEquipment[9] == i
					|| player.getActionAssistant().playerHasItem(i)) {
				player.sendMessage("You cannot bring "
						+ InstanceDistributor.getItemManager()
								.getItemDefinition(i).getName()
						+ " into the bounty hunter arena!");
				return false;
			}
		final double a = player.enterTime * .6;
		if (player.enterTime > 0) {
			player.getActionSender().sendMessage(
					"You must wait " + (int) a
							+ " before entering the crater again!");
			if (!player.counting)
				reEnterTime(player, player.enterTime);
			return false;
		}
		switch (cave) {
		case 0:
			if (player.getCombatLevel() > 55) {
				player.getActionSender().sendMessage(
						"You cannot enter this crater!");
				player.getActionSender().sendMessage(
						"You need a combat level of 55 or lower to enter.");
				return false;
			}
			lowLevelCrater.add(player);
			break;
		case 1:
			if (player.getCombatLevel() > 100 || player.getCombatLevel() < 50) {
				player.getActionSender().sendMessage(
						"You cannot enter this crater!");
				player.getActionSender()
						.sendMessage(
								"You need a combat level ranging from 50 to 100 to enter.");
				return false;
			}
			mediumLevelCrater.add(player);
			break;
		case 2:
			if (player.getCombatLevel() < 95) {
				player.getActionSender().sendMessage(
						"You cannot enter this crater!");
				player.getActionSender().sendMessage(
						"You need a combat level of 95 or greater to enter.");
				return false;
			}
			highLevelCrater.add(player);
			break;
		}
		final int coord = Misc.random(ENTER.length - 1);
		final int x = coord;
		player.getActionAssistant().sendAnimation(7376);
		if (player.pickupPenalty > 0)
			startPickupPenalty(player);
		if (player.leavePenalty > 0)
			startLeavePenalty(player);
		player.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				player.teleportToX = ENTER[x][0];
				player.teleportToY = ENTER[x][1];
				player.heightLevel = 0;
				player.headIcon = getPlayerSkull(player);
				player.updateRequired = true;
				player.getActionAssistant().sendAnimation(7377);
				// player.getActionSender().sendMessage("Total carrying wealth: "+getTotalCarrying(player.w)+"");
				findTarget(player);
				container.stop();
				writeInterface(player);
			}

			@Override
			public void stop() {

			}

		}, 1);
		return true;
	}

	public static void findTarget(final Player player) {
		giveTarget(player);
		if (player.findingTarget)
			return;
		player.findingTarget = true;
		player.getPlayerEventHandler().addEvent(new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				if (!player.inBountySafe()) {
					if (player.bountyTarget == -1)
						giveTarget(player);
				} else {
					player.findingTarget = false;
					container.stop();
				}

			}

			@Override
			public void stop() {

			}

		}, 25);
	}

	/**
	 * Gets the skull
	 * 
	 * @param player
	 *            the player
	 * @return the skull id
	 */
	public static int getPlayerSkull(final Player player) {
		final int carrying = getTotalCarrying();
		return carrying < 100000 ? BROWN_SKULL : carrying < 500000
				&& carrying > 100000 ? SILVER_SKULL : carrying < 1000000
				&& carrying > 500000 ? BLUE_SKULL : carrying < 5000000
				&& carrying > 1000000 ? GREEN_SKULL : carrying < 10000000
				&& carrying > 5000000 ? BLUE_SKULL : carrying > 10000000
				|| carrying < -1 ? RED_SKULL : BROWN_SKULL;
	}

	/**
	 * Gets the total amount carrying
	 * 
	 * @param player
	 *            the player
	 * @return the total amount
	 */
	public static int getTotalCarrying() {
		return InstanceDistributor.getGeneralStore().itemPrice(0);
	}

	public static void giveTarget(Player player) {
		player.bountyTarget = -1;
		if (highLevelCrater.contains(player)) {
			if (highLevelCrater.size() > 1)
				player.bountyTarget = highLevelCrater.get(Misc
						.random(highLevelCrater.size() - 1)).ID;
		} else if (mediumLevelCrater.contains(player)) {
			if (mediumLevelCrater.size() > 1)
				player.bountyTarget = highLevelCrater.get(Misc
						.random(mediumLevelCrater.size() - 1)).ID;
		} else if (lowLevelCrater.contains(player))
			if (lowLevelCrater.size() > 1)
				player.bountyTarget = lowLevelCrater.get(Misc
						.random(lowLevelCrater.size() - 1)).ID;
		if (player.bountyTarget > 0) {
			final Player other = PlayerManager.getSingleton().getPlayers()[player.bountyTarget];
			if (other != null) {
				if (other.inBountySafe())
					player.bountyTarget = -1;
				if (other.inBountyHunterCombat())
					player.bountyTarget = -1;
			} else
				player.bountyTarget = -1;
		}
		if (player.bountyTarget == -1 && player.bountyTarget == player.ID) {
			player.bountyTarget = -1;
			player.getActionSender().sendLocationPointerPlayer(-1, -1);
		} else
			player.getActionSender().sendLocationPointerPlayer(10,
					player.bountyTarget);

	}

	public static void leaveCrater(final Player player) {
		if (System.currentTimeMillis() - player.getLastAction() < 50000)
			return;
		// player.getLastAction() = System.currentTimeMillis(); TODO HERE
		if (player.leavePenalty > 0)
			player.getActionSender().sendMessage(
					"You cannot leave with a penalty!");
		boolean enter = false;
		for (final int[] element : ENTER)
			if (player.absX == element[0] && player.absY == element[1])
				enter = true;
		if (!enter)
			return;
		player.getActionAssistant().sendAnimation(7376);
		reEnterTime(player, 50);
		if (player.publicCrater < 0)
			player.publicCrater = 0;
		for (final Player p : PlayerManager.getSingleton().getPlayers()) {
			final Player p2 = p;
			if (p2 != null)
				if (p2.inBountyHunterCombat())
					if (p2.bountyTarget == player.ID) {
						player.sendMessage("Your target has left the area! You will be assigned a new one.");
						giveTarget(p2);
					}
		}
		if (lowLevelCrater.contains(player.getIndex()))
			lowLevelCrater.remove(player.getIndex());
		else if (mediumLevelCrater.contains(player.getIndex()))
			mediumLevelCrater.remove(player.getIndex());
		else if (highLevelCrater.contains(player.getIndex()))
			highLevelCrater.remove(player.getIndex());
		player.getPlayerEventHandler().addEvent(new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				player.teleportToX = LEAVE[player.publicCrater][0];
				player.teleportToY = LEAVE[player.publicCrater][1];
				player.heightLevel = 0;
				player.getActionAssistant().sendAnimation(7377);
				container.stop();
			}

			@Override
			public void stop() {

			}

		}, 1);
		player.headIcon = -1;
		player.updateRequired = true;
	}

	public static boolean logoutInBountyHunter(Player player) {
		if (player.pickupPenalty > 0 || player.leavePenalty > 0) {
			player.sendMessage("You cannot logout with a penalty!");
			return false;
		}
		for (final Player p : PlayerManager.getSingleton().getPlayers()) {
			final Player p2 = p;
			if (p2 != null)
				if (p2.inBountyHunterCombat()) {
					p2.sendMessage("Your target has logged out! You will be assigned a new one shortly.");
					giveTarget(player);
				}
		}
		if (lowLevelCrater.contains(player.ID))
			lowLevelCrater.remove(player.ID);
		else if (mediumLevelCrater.contains(player.ID))
			mediumLevelCrater.remove(player.ID);
		else if (highLevelCrater.contains(player.ID))
			highLevelCrater.remove(player.ID);
		return true;
	}

	public static void reEnterTime(final Player player, int time) {
		player.enterTime = time;
		player.counting = true;
		player.getPlayerEventHandler().addEvent(new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				player.enterTime--;
				if (player.enterTime < 0) {
					player.counting = false;
					container.stop();
				}
			}

			@Override
			public void stop() {

			}

		}, 1);
	}

	public static void refreshBountyHunter(Player player) {
		boolean canContinue = true;
		if (player.enterTime > 0)
			reEnterTime(player, player.enterTime);
		if (player.pickupPenalty > 0)
			startPickupPenalty(player);
		if (player.leavePenalty > 0)
			startLeavePenalty(player);
		switch (player.publicCrater) {
		case 0:
			lowLevelCrater.add(player);
			break;
		case 1:
			mediumLevelCrater.add(player);
			break;
		case 2:
			highLevelCrater.add(player);
			break;
		default:
			player.teleportToX = 3163;
			player.teleportToY = 3671;
			player.heightLevel = 0;
			canContinue = false;
			break;
		}
		if (!canContinue)
			return;
		player.headIcon = getPlayerSkull(player);
		player.updateRequired = true;
		findTarget(player);
	}

	public static void resetBountyHuner(Player player) {
		player.bountyTarget = -1;
		player.leavePenalty = 0;
		player.publicCrater = -1;
		player.killedWrongTarget = false;
		// TODO Show option
		player.getActionSender().sendLocationPointerPlayer(-1, -1);

	}

	public static void rogueBoard(Player player) {

	}

	public static void startLeavePenalty(final Player player) {
		player.pickupPenalty = -1;
		player.pickupDec = false;
		player.leavePenalty = 180;
		player.sendMessage("You are now under the effect of the leave penalty! Which means you cannot leave the");
		player.sendMessage("bounty hunter for 3 minutes.");
		if (player.leaveDec)
			return;
		player.leaveDec = true;
		player.getPlayerEventHandler().addEvent(new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				decreaseLeavePenalty(player);
				if (player.leavePenalty <= 0) {
					player.leaveDec = false;
					container.stop();
				}
			}

			@Override
			public void stop() {

			}

		}, 5);

	}

	public static void startPickupPenalty(final Player player) {
		player.pickupPenalty = 180;
		player.sendMessage("You are now under the effect of the pick up penalty! Picking up any item will put you");
		player.sendMessage("under the leave penalty.");
		if (player.pickupDec)
			return;
		player.pickupDec = true;
		player.getPlayerEventHandler().addEvent(new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				decreasePickupPenalty(player);
				if (player.pickupPenalty <= 0) {
					player.pickupDec = false;
					container.stop();
				}

			}

			@Override
			public void stop() {

			}

		}, 5);
	}

	public static void teleToTarget(Player player) {
		if (!(player.bountyTarget > 0)) {
			player.sendMessage("You do not have a target.");
			return;
		}
		if (player.getInCombatWith() != null) {
			player.sendMessage("You cannot use this spell in combat!");
			return;
		}
		final Player other = PlayerManager.getSingleton().getPlayers()[player.bountyTarget];
		if (other == null)
			return;
		final int x = other.absX + Misc.random(3);
		final int y = other.absY + Misc.random(3);
		if (player.inBountyHunterCombat(x, y))
			player.teleport(x, y, 0);
		else if (other.inBountyHunterCombat())
			player.teleport(other.absX, other.absY, 0);

	}

	public static void writeInterface(Player player) {
		// player.getActionSender().sendWalkableInterface(25347);
		if (player.bountyTarget > 0) {
			final Player target = PlayerManager.getSingleton().getPlayers()[player.bountyTarget];
			if (target != null) {
				final String targetName = Misc.formatUsername(target.username);
				player.getActionSender().sendFrame126(targetName, 25350);
			}
		} else
			player.getActionSender().sendFrame126("Nobody!", 25350);
		player.getActionSender().sendFrame126(
				""
						+ (player.leavePenalty > 0 ? "Can't leave for:"
								: player.pickupPenalty > 0 ? "Pickup penalty:"
										: "") + "", 25351);
		final int time = player.pickupPenalty > 0 ? player.leavePenalty : 0;
		if (time > 0)
			player.getActionSender().sendFrame126("" + time + " Sec", 25352);
		else
			player.getActionSender().sendFrame126("", 25352);
		if (player.bountyTarget == -1 || player.bountyTarget == player.ID) {
			player.bountyTarget = -1;
			player.sendMessage("No target has been found in the area.");
			player.sendMessage("Searching...");
		}
	}

}