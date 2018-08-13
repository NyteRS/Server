package com.server2.content.skills.firemaking;

import java.util.HashMap;
import java.util.Map;

import com.server2.content.Achievements;
import com.server2.content.anticheat.BankCheating;
import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.entity.Location;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Areas;
import com.server2.world.map.Region;
import com.server2.world.objects.GameObject;
import com.server2.world.objects.ObjectManager;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class Firemaking {

	private static enum LightableLog {
		NORMAL(1511, 1, 40, 50), ACHEY(2862, 1, 40, 100), OAK(1521, 15, 60, 150), WILLOW(
				1519, 30, 90, 200), TEAK(6333, 35, 105, 250), ARTIC_PINE(10810,
				42, 125, 300), MAPLE(1517, 45, 135, 350), MAHOGANY(6332, 50,
				157, 400), EUCALYPTUS(12581, 58, 193, 450), YEW(1515, 60, 202,
				500), MAGIC(1513, 76, 303, 550);

		private final int logId;
		private final int level;
		private final int experience;

		private static Map<Integer, LightableLog> lightableLogs = new HashMap<Integer, LightableLog>();

		static {
			for (final LightableLog log : LightableLog.values())
				lightableLogs.put(log.logId, log);
		}

		private LightableLog(int log, int level, int experience, int ticks) {
			logId = log;
			this.level = level;
			this.experience = experience;
		}

		public int getLevel() {
			return level;
		}

	}

	private static final int TINDERBOX = 590;
	private static final int IGNITE = 733;

	private static final String IGNITE_MESSAGE = "You attempt to light the logs...";
	private static final String BURN_MESSAGE = "The fire catches and the logs begin to burn.";

	private static boolean ableToFiremake(Player c) {
		if (c.isBusy())
			return false;
		if (Areas.isEdgeville(c.getPosition()) || Areas.inMiniGame(c)
				|| BankCheating.getInstance().inBank(c)) {
			c.getActionSender()
					.sendMessage(
							"You cannot do this in this area, please use varrock for example");
			return false;
		}
		if (!c.getActionAssistant().playerHasItem(TINDERBOX)) {
			c.getActionSender().sendMessage(
					"You need a tinderbox to light a fire!");
			return false;
		}
		return true;
	}

	private static int getIgnitionDelay(Player c, LightableLog log) {
		int delay = 4;

		if (c.isBusy())
			delay = 1;

		if (c.playerLevel[PlayerConstants.FIREMAKING] - log.level < 5)
			delay += 3;
		return delay;
	}

	public static void startFire(final Player c, int itemUsed, int usedWith) {
		int logItem;
		if (itemUsed == TINDERBOX)
			logItem = usedWith;
		else if (usedWith == TINDERBOX)
			logItem = itemUsed;
		else
			return;
		if (System.currentTimeMillis() - c.lastFiremake < 600)
			return;
		c.lastFiremake = System.currentTimeMillis();

		final int[] coords = new int[2];
		coords[0] = c.absX;
		coords[1] = c.absY;
		if (Region.getClipping(coords[0], coords[1], c.heightLevel) == Region.SOLID_TILE) {
			c.sendMessage("You cannot light a fire here.");
			return; // run it
		}
		final LightableLog log = LightableLog.lightableLogs.get(logItem);
		if (log != null) {
			if (c.playerLevel[PlayerConstants.FIREMAKING] < log.getLevel()) {
				c.getActionSender().sendMessage(
						"You need a Firemaking level of " + log.getLevel()
								+ " to light that log.");
				return;
			}
			if (!ableToFiremake(c))
				return;
			c.resetWalkingQueue();
			c.setBusy(true);
			final int delay = getIgnitionDelay(c, log);
			if (delay > 1)
				c.getActionAssistant().startAnimation(IGNITE);
			c.getActionSender().sendMessage(IGNITE_MESSAGE);
			c.getPlayerEventHandler().addEvent(new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					container.stop();
					c.getActionAssistant().startAnimation(65535);
					c.getPlayerEventHandler().addEvent(new CycleEvent() {
						@Override
						public void execute(CycleEventContainer container) {
							c.getActionSender().sendMessage(BURN_MESSAGE);
							ObjectManager.submitPublicObject(new GameObject(
									new Location(coords[0], coords[1], c
											.getHeightLevel()), 2732, -1, 50,
									-1, 10, c));
							container.stop();
						}

						@Override
						public void stop() {

						}
					}, 1);
				}

				@Override
				public void stop() {
					// TODO Auto-generated method stub

				}
			}, delay);
			int moveX = 0;
			if (Region.canMove(c.absX, c.absY, c.absX - 1, c.absY,
					c.heightLevel, 1, 1)) {
				moveX = c.getAbsX() - 1;
				c.forceMovement(new Location(c.getAbsX() - 1, c.getAbsY()));
			} else if (Region.canMove(c.absX, c.absY, c.absX + 1, c.absY,
					c.heightLevel, 1, 1)) {
				moveX = c.getAbsX() + 1;
				c.forceMovement(new Location(c.getAbsX() + 1, c.getAbsY()));
			} else if (Region.canMove(c.absX, c.absY, c.absX, c.absY - 1,
					c.heightLevel, 1, 1)) {
				moveX = c.getAbsY() - 1;
				c.forceMovement(new Location(c.getAbsX(), c.getAbsY() - 1));
			} else {
				moveX = c.getAbsY() + 1;
				c.forceMovement(new Location(c.getAbsX(), c.getAbsY() + 1));
			}
			c.viewToX = moveX;
			c.viewToY = c.getAbsY();

			c.getActionAssistant().addSkillXP(
					log.experience * PlayerConstants.FIREMAKING_MULTIPLIER,
					PlayerConstants.FIREMAKING);
			c.getActionAssistant().deleteItem(log.logId, 1);
			if (log.logId == 1513) {
				c.progress[46]++;
				if (c.progress[46] >= 100)
					Achievements.getInstance().complete(c, 46);
				else
					Achievements.getInstance().turnYellow(c, 46);
			}
			c.setBusy(false);
		}
	}

}