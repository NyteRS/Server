package com.server2.content.misc.mobility;

import com.server2.content.JailSystem;
import com.server2.content.minigames.FightPits;
import com.server2.content.quests.Christmas;
import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.combat.CombatEngine;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.Location;
import com.server2.model.entity.player.Player;
import com.server2.util.Areas;

/**
 * 
 * @author Rene Roosen & Lukas
 * 
 */
public class PlayerTeleportHandler {

	/**
	 * The default Player instance
	 */
	private final Player player;

	/**
	 * Constructs a new player's instance.
	 */
	public PlayerTeleportHandler(Player player) {
		this.player = player;
	}

	/**
	 * Can the user teleport?
	 */
	public boolean canTeleport() {
		if (Christmas.instance.inWinter(player)) {
			player.getDM().sendNpcChat2("Pahaha you fool!",
					"How dare you try and escape now?!", Christmas.SNOWMAN,
					"Ghost of Christmas");
			return false;
		}
		if (Christmas.instance.inGhost(player)) {
			player.getDM().sendNpcChat2("Pahaha you fool!",
					"How dare you try and escape me?!", Christmas.GHOST,
					"Ghost of Christmas");
			return false;
		}
		if (Christmas.instance.inRat(player)) {
			player.getDM().sendNpcChat2("Pahaha you fool!",
					"How dare you try and escape me?!",
					Christmas.DRAGONSNOWMAN, "Ghost of Christmas");
			return false;
		}
		if (Christmas.instance.inPuppet(player)) {
			player.getDM().sendNpcChat2("Pahaha you fool!",
					"How dare you try and escape me?!",
					Christmas.PIRATESNOWMAN, "Ghost of Christmas");
			return false;
		}
		if (Christmas.instance.inGnome(player)) {
			player.getDM().sendNpcChat2("Pahaha you fool!",
					"How dare you try and escape me?!", Christmas.DWARFSNOWMAN,
					"Ghost of Christmas");
			return false;
		}
		if (player.isDead())
			return false;
		if (Areas.inTutorial(player.getPosition())) {
			player.getActionSender()
					.sendMessage(
							"@dre@You cannot teleport away from the tutorial@bla@,@dre@ please talk to the Lumbridge Sage@bla@.");
			player.getActionSender().sendMessage(
					"@dre@You can find him in the next room!");
			player.getActionSender().sendWindowsRemoval();
			return false;
		}
		if (player.isBusy()) {
			player.getActionSender().sendWindowsRemoval();
			return false;
		}
		if (player.inEvent)
			return false;
		if (Areas.inMiniGame(player)) {
			player.getActionSender().sendWindowsRemoval();
			player.getActionSender().sendMessage(
					"You cannot teleport out of a minigame!");
			return false;
		}
		if (player.getWildernessLevel() > 20 && !player.inTeleportableRoom()
				&& !player.inTeleportable()) {
			player.getActionSender().sendWindowsRemoval();
			player.getActionSender()
					.sendMessage(
							"A magical force keeps you from teleporting above level 20 wilderness.");
			return false;
		}
		if (JailSystem.inJail(player)) {
			player.getActionSender().sendWindowsRemoval();
			player.getActionSender().sendMessage(
					"You cannot teleport out of jail!");
			return false;
		}
		if (player.teleBlock) {
			player.getActionSender().sendWindowsRemoval();
			player.getActionSender().sendMessage(
					"A magical force prevents you from teleporting!");
			return false;
		}
		if (player.isSkilling()) {
			player.getActionSender().sendWindowsRemoval();
			player.getActionSender().sendMessage(
					"You cannot teleport whilst skilling.");
			return false;
		}

		return true;
	}

	/**
	 * Completes a glory teleport
	 */
	public void doGlory(int teleportToX, int teleportToY, int teleportHeight) {
		// Do state updates
		player.getActionSender().sendWindowsRemoval();
		player.enteredGwdRoom = false;
		player.resetFaceDirection();
		player.resetWalkingQueue();
		player.setBusyTimer(7);

		// Send animation & gfx
		player.getActionAssistant().createPlayerGfx(1684, 0, true);
		player.getActionAssistant().sendAnimation(9603);

		// Delay the teleport 6 ticks
		forceDelayTeleport(teleportToX, teleportToY, teleportHeight, 6);
	}

	/**
	 * Completes a Ring of dueling teleport
	 */
	public void doRoD(int teleportToX, int teleportToY, int teleportHeight) {

		// State updates
		player.getActionSender().sendWindowsRemoval();
		player.enteredGwdRoom = false;
		player.resetFaceDirection();
		player.resetWalkingQueue();
		player.setBusyTimer(8);

		// Send animation & gfx
		player.getActionAssistant().createPlayerGfx(1684, 0, true);
		player.getActionAssistant().sendAnimation(9603);

		// Delay the teleport 6 ticks
		forceDelayTeleport(teleportToX, teleportToY, teleportHeight, 6);
	}

	/**
	 * Performs a force teleport with a delay
	 */
	public void forceDelayTeleport(final int teleportToX,
			final int teleportToY, final int teleportHeight, final int delay) {
		if (delay == 0) {
			if (player.getTarget() != null) {
				player.getTarget().setInCombatWith(null);
				player.getTarget().setTarget(null);
				player.getTarget().setFollowing(null);
				player.setInCombatWith(null);
				player.setTarget(null);
				player.setFollowing(null);

			}
			player.stopMovement();
			player.getActionSender().sendAnimationReset();
			player.teleportToX = teleportToX;
			player.teleportToY = teleportToY;
			player.setHeightLevel(teleportHeight);
			player.getActionSender().sendWindowsRemoval();
			player.actionSet = false;

			player.currentActivity = null;
		} else
			player.getPlayerEventHandler().addEvent(new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					player.teleportToX = teleportToX;
					player.teleportToY = teleportToY;
					player.setHeightLevel(teleportHeight);
					player.getActionSender().sendWindowsRemoval();
					if (player.getTarget() != null) {
						player.getTarget().setInCombatWith(null);
						player.getTarget().setTarget(null);
						player.getTarget().setFollowing(null);
						player.setInCombatWith(null);
						player.setTarget(null);
						player.setFollowing(null);

					}
					player.getActionSender().sendAnimationReset();
					player.stopMovement();

					player.actionSet = false;
					player.currentActivity = null;
					container.stop();

				}

				@Override
				public void stop() {

				}
			}, delay);
	}

	/**
	 * Performs a force teleport
	 * 
	 * @param teleportToX
	 * @param teleportToY
	 */
	public void forceTeleport(int teleportToX, int teleportToY, int heightLevel) {
		player.setBusyTimer(2);
		player.teleportToX = teleportToX;
		player.teleportToY = teleportToY;
		player.setHeightLevel(heightLevel);
		player.getActionSender().sendWindowsRemoval();
	}

	public Location getLocationForName(String name) {

		if (name.equals("HOME"))
			return new Location(player.getHomeAreaX(), player.getHomeAreaY());
		else if (name.equals("VARROCK"))
			return new Location(3212, 3424);
		else if (name.equals("CAMELOT"))
			return new Location(2757, 3478);
		else if (name.equals("FALADOR"))
			return new Location(2964, 3378);
		else if (name.equals("LUMBRIDGE"))
			return new Location(3222, 3218);
		else if (name.equals("ARDOUGNE"))
			return new Location(2662, 3307);

		return null;
	}

	public void leverTeleport(final int teleportToX, final int teleportToY,
			int teleportToHeight) {
		if (canTeleport()) {
			resetStates();
			AnimationProcessor.addNewRequest(player, 2140, 0);
			AnimationProcessor.addNewRequest(player, 8939, 1);
			GraphicsProcessor.addNewRequest(player, 1577, 0, 1);
			teleportToHeight = 0;
			final int heightLevel = teleportToHeight;
			player.setBusy(true);
			player.getPlayerEventHandler().addEvent(new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					AnimationProcessor.addNewRequest(player, 8941, 0);
					GraphicsProcessor.addNewRequest(player, 1577, 0, 0);
					player.teleportToX = teleportToX;
					player.teleportToY = teleportToY;
					player.setHeightLevel(heightLevel);
					CombatEngine.resetAttack(player, false);
					container.stop();
				}

				@Override
				public void stop() {
					// TODO Auto-generated method stub

				}
			}, 4);
		}
		resetTeleport();
	}

	/**
	 * Performs a few state updates to the teleporting user.
	 */
	private void resetStates() {
		if (FightPits.inWaitingRoom(player))
			FightPits.removeFromWaitingRoom(player);

		player.setInvulnerability(9);
		player.setStopRequired();
		player.tempSpawnLock = false;
		player.resetFaceDirection();
		player.resetWalkingQueue();
		player.getActionSender().sendWindowsRemoval();
		player.enteredGwdRoom = false;
	}

	/**
	 * Resets the player's last teleporting stages.
	 */
	private void resetTeleport() {
		CombatEngine.resetAttack(player, false);
		for (int i = 0; i < player.teleport.length; i++)
			player.teleport[i] = false;
		player.teleportConfig = -1;
	}

	/**
	 * Handles a single teleport request.
	 * 
	 * @param player
	 */
	public void teleport(final int teleportToX, final int teleportToY,
			int teleportToHeight) {
		if (canTeleport()) { // Can the user teleport?

			resetStates();
			AnimationProcessor.addNewRequest(player, teleportationDetails()[0],
					0);
			GraphicsProcessor.addNewRequest(player, teleportationDetails()[1],
					0, 0);

			// Perform any height modifications if needed.
			if (teleportToX == 2871 && teleportToY == 5320)
				teleportToHeight = 2;
			else if (teleportToX == 2289 && teleportToY == 3139)
				teleportToHeight = 0;
			else if (teleportToX == 2649 && teleportToY == 4569)
				teleportToHeight = 1;
			else if (teleportToX == 3591 && teleportToY == 3490)
				teleportToHeight = 0;
			else
				teleportToHeight = 0;
			final int heightLevel = teleportToHeight;
			player.setBusy(true);
			// Finalize the teleport
			player.getPlayerEventHandler().addEvent(new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					player.getActionAssistant().createPlayerGfx(
							teleportationDetails()[3], 0, true);
					player.teleportToX = teleportToX;
					player.teleportToY = teleportToY;
					player.setHeightLevel(heightLevel);
					CombatEngine.resetAttack(player, false);
					container.stop();// Stop the event because it's not
										// repeating
				}

				@Override
				public void stop() {
					// TODO Auto-generated method stub

				}
			}, player.getSpellBook() <= 1 ? 3 : 6);
			// And finally send last animation
			player.getPlayerEventHandler().addEvent(new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (player.getSpellBook() == 1)
						player.getActionAssistant().sendAnimation(
								teleportationDetails()[2]);
					else
						player.getActionSender().sendAnimationReset();
					CombatEngine.resetAttack(player, false);
					player.setBusy(false);

					container.stop(); // Stop the event because it's not
										// repeating.
				}

				@Override
				public void stop() {
					// TODO Auto-generated method stub

				}
			}, player.getSpellBook() <= 1 ? 4 : 6);
		}
		resetTeleport();
	}

	/**
	 * Which animation and graphic should we use?
	 */
	private int[] teleportationDetails() {
		final int[] data = new int[4];

		if (player.getSpellBook() == 0 || player.getSpellBook() == 1) {
			data[0] = 8939;
			data[1] = 1576;
			data[2] = 8941;
			data[3] = 1577;
		} else if (player.getSpellBook() == 2) {
			data[0] = 9599;
			data[1] = 1681;
			// data[2] = 9013;
		} else if (player.getSpellBook() == 3) {
			data[0] = 9606;
			data[1] = 1685;
			// data[2] = 9013;
		}
		return data;
	}

	public void teleTab(int itemID, String location) {
		final Location position = getLocationForName(location);
		if (position == null)
			return;
		if (canTeleport()
				&& System.currentTimeMillis() - player.lastTeleTab > 1800) {
			player.getWalkingQueue().reset();
			player.setBusyTimer(3);
			player.getActionAssistant().deleteItem(itemID, 1);
			player.getActionAssistant().startAnimation(4731);
			GraphicsProcessor.addNewRequest(player, 678, 0, 0);
			player.getPlayerTeleportHandler().forceDelayTeleport(
					position.getX(), position.getY(), 0, 3);
			player.lastTeleTab = System.currentTimeMillis();
		}
	}

}
