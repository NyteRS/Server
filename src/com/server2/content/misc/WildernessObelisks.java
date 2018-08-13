package com.server2.content.misc;

import com.server2.Settings;
import com.server2.content.misc.mobility.TeleportationHandler;
import com.server2.engine.cycle.Tickable;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;
import com.server2.world.PlayerManager;
import com.server2.world.World;
import com.server2.world.objects.GameObject;
import com.server2.world.objects.GameObject.Face;
import com.server2.world.objects.ObjectManager;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class WildernessObelisks {

	/**
	 * Instances the WildernessObelisks class
	 */
	public static WildernessObelisks INSTANCE = new WildernessObelisks();

	/**
	 * Gets the WildernessObelisks Instance.
	 */
	public static WildernessObelisks getInstance() {
		return INSTANCE;
	}

	/**
	 * A longArray holding the timers for the obelisks
	 */
	private long ob1, ob2, ob3, ob4, ob5;

	/**
	 * An intArray holding all possible X Values.
	 */
	private static int X[] = { 3156, 3307, 3106, 3219, 2980 };

	/**
	 * An intArray holding all possible Y values.
	 */
	private static int Y[] = { 3620, 3916, 3794, 3656, 3866 };

	/**
	 * A method which activates an obelisk
	 */
	public void activateObelisk(int objectID) {

		switch (objectID) {
		case 14831:
			if (System.currentTimeMillis() - ob1 > 6000) {
				ob1 = System.currentTimeMillis();
				ObjectManager.submitPublicObject(new GameObject(14825, 3305,
						3914, 0, Face.NORTH, 10));
				ObjectManager.submitPublicObject(new GameObject(14825, 3305,
						3918, 0, Face.NORTH, 10));
				ObjectManager.submitPublicObject(new GameObject(14825, 3309,
						3918, 0, Face.NORTH, 10));
				ObjectManager.submitPublicObject(new GameObject(14825, 3309,
						3914, 0, Face.NORTH, 10));
				int random = Misc.random(X.length - 1);
				while (random == 1)
					random = Misc.random(X.length - 1);
				final int baws = random;
				World.getWorld().submit(new Tickable(10) {
					@Override
					public void execute() {
						ObjectManager.submitPublicObject(new GameObject(14831,
								3305, 3914, 0, Face.NORTH, 10));
						ObjectManager.submitPublicObject(new GameObject(14831,
								3305, 3918, 0, Face.NORTH, 10));
						ObjectManager.submitPublicObject(new GameObject(14831,
								3309, 3918, 0, Face.NORTH, 10));

						ObjectManager.submitPublicObject(new GameObject(14831,
								3309, 3914, 0, Face.NORTH, 10));
						for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
							final Player p = PlayerManager.getSingleton()
									.getPlayers()[i];
							if (p == null)
								continue;

							if (p.inObelisk1() && !p.teleBlock) {
								p.setTarget(null);
								p.setFollowing(null);
								AnimationProcessor.addNewRequest(p, 1816, 1);
								GraphicsProcessor.addNewRequest(p, 342, 4, 0);
								TeleportationHandler.addNewRequest(p, X[baws]
										- 1 + Misc.random(2), Y[baws] - 1
										+ Misc.random(2), 0, 4);
								AnimationProcessor.addNewRequest(p, 715, 6);
							}
						}
						stop();
					}
				});
			}
			break;

		case 14830:
			if (System.currentTimeMillis() - ob2 > 6000) {
				ob2 = System.currentTimeMillis();
				ObjectManager.submitPublicObject(new GameObject(14825, 3217,
						3654, 0, Face.NORTH, 10));
				ObjectManager.submitPublicObject(new GameObject(14825, 3221,
						3654, 0, Face.NORTH, 10));
				ObjectManager.submitPublicObject(new GameObject(14825, 3221,
						3658, 0, Face.NORTH, 10));
				ObjectManager.submitPublicObject(new GameObject(14825, 3217,
						3658, 0, Face.NORTH, 10));
				int random2 = Misc.random(X.length - 1);
				while (random2 == 3)
					random2 = Misc.random(X.length - 1);
				final int baws2 = random2;
				World.getWorld().submit(new Tickable(10) {
					@Override
					public void execute() {
						ObjectManager.submitPublicObject(new GameObject(14830,
								3217, 3654, 0, Face.NORTH, 10));
						ObjectManager.submitPublicObject(new GameObject(14830,
								3221, 3654, 0, Face.NORTH, 10));
						ObjectManager.submitPublicObject(new GameObject(14830,
								3221, 3658, 0, Face.NORTH, 10));
						ObjectManager.submitPublicObject(new GameObject(14830,
								3217, 3658, 0, Face.NORTH, 10));
						for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
							final Player p = PlayerManager.getSingleton()
									.getPlayers()[i];
							if (p == null)
								continue;

							if (p.inObelisk2() && !p.teleBlock) {
								p.setTarget(null);
								p.setFollowing(null);
								AnimationProcessor.addNewRequest(p, 1816, 1);
								GraphicsProcessor.addNewRequest(p, 342, 4, 0);
								TeleportationHandler.addNewRequest(p, X[baws2]
										- 1 + Misc.random(2), Y[baws2] - 1
										+ Misc.random(2), 0, 4);
								AnimationProcessor.addNewRequest(p, 715, 6);
							}
						}
						stop();

					}
				});
			}
			break;

		case 14829:
			if (System.currentTimeMillis() - ob3 > 6000) {
				ob3 = System.currentTimeMillis();
				ObjectManager.submitPublicObject(new GameObject(14825, 3154,
						3618, 0, Face.NORTH, 10));
				ObjectManager.submitPublicObject(new GameObject(14825, 3158,
						3618, 0, Face.NORTH, 10));
				ObjectManager.submitPublicObject(new GameObject(14825, 3158,
						3622, 0, Face.NORTH, 10));
				ObjectManager.submitPublicObject(new GameObject(14825, 3154,
						3622, 0, Face.NORTH, 10));
				int random4 = Misc.random(X.length - 1);
				while (random4 == 0)
					random4 = Misc.random(X.length - 1);
				final int baws4 = random4;
				World.getWorld().submit(new Tickable(10) {
					@Override
					public void execute() {
						for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
							final Player p = PlayerManager.getSingleton()
									.getPlayers()[i];
							if (p == null)
								continue;

							if (p.inObelisk4() && !p.teleBlock) {
								p.setTarget(null);
								p.setFollowing(null);
								AnimationProcessor.addNewRequest(p, 1816, 1);
								GraphicsProcessor.addNewRequest(p, 342, 4, 0);
								TeleportationHandler.addNewRequest(p, X[baws4]
										- 1 + Misc.random(2), Y[baws4] - 1
										+ Misc.random(2), 0, 4);
								AnimationProcessor.addNewRequest(p, 715, 6);
								ObjectManager
										.submitPublicObject(new GameObject(
												14829, 3154, 3618, 0,
												Face.NORTH, 10));
								ObjectManager
										.submitPublicObject(new GameObject(
												14829, 3158, 3618, 0,
												Face.NORTH, 10));
								ObjectManager
										.submitPublicObject(new GameObject(
												14829, 3158, 3622, 0,
												Face.NORTH, 10));
								ObjectManager
										.submitPublicObject(new GameObject(
												14829, 3154, 3622, 0,
												Face.NORTH, 10));
							}
						}
						stop();
					}
				});
			}
			break;

		case 14828:
			if (System.currentTimeMillis() - ob4 > 6000) {
				ob4 = System.currentTimeMillis();
				ObjectManager.submitPublicObject(new GameObject(14825, 3104,
						3792, 0, Face.NORTH, 10));
				ObjectManager.submitPublicObject(new GameObject(14825, 3108,
						3792, 0, Face.NORTH, 10));
				ObjectManager.submitPublicObject(new GameObject(14825, 3108,
						3796, 0, Face.NORTH, 10));
				ObjectManager.submitPublicObject(new GameObject(14825, 3104,
						3796, 0, Face.NORTH, 10));
				int random5 = Misc.random(X.length - 1);

				while (random5 == 2)
					random5 = Misc.random(X.length - 1);
				final int baws5 = random5;
				World.getWorld().submit(new Tickable(10) {
					@Override
					public void execute() {
						ObjectManager.submitPublicObject(new GameObject(14828,
								3104, 3792, 0, Face.NORTH, 10));
						ObjectManager.submitPublicObject(new GameObject(14828,
								3108, 3792, 0, Face.NORTH, 10));
						ObjectManager.submitPublicObject(new GameObject(14828,
								3108, 3796, 0, Face.NORTH, 10));
						ObjectManager.submitPublicObject(new GameObject(14828,
								3104, 3796, 0, Face.NORTH, 10));
						for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
							final Player p = PlayerManager.getSingleton()
									.getPlayers()[i];
							if (p == null)
								continue;

							if (p.inObelisk5() && !p.teleBlock) {
								p.setTarget(null);
								p.setFollowing(null);
								AnimationProcessor.addNewRequest(p, 1816, 1);
								GraphicsProcessor.addNewRequest(p, 342, 4, 0);
								TeleportationHandler.addNewRequest(p, X[baws5]
										- 1 + Misc.random(2), Y[baws5] - 1
										+ Misc.random(2), 0, 4);
								AnimationProcessor.addNewRequest(p, 715, 6);
							}
						}
						stop();
					}
				});
			}
			break;

		case 14827:

			break;

		case 14826:
			if (System.currentTimeMillis() - ob5 > 6000) {
				ob5 = System.currentTimeMillis();
				ObjectManager.submitPublicObject(new GameObject(14825, 2978,
						3864, 0, Face.NORTH, 10));
				ObjectManager.submitPublicObject(new GameObject(14825, 2982,
						3864, 0, Face.NORTH, 10));
				ObjectManager.submitPublicObject(new GameObject(14825, 2982,
						3868, 0, Face.NORTH, 10));
				ObjectManager.submitPublicObject(new GameObject(14825, 2978,
						3868, 0, Face.NORTH, 10));
				int random3 = Misc.random(X.length - 1);
				while (random3 == 5)
					random3 = Misc.random(X.length - 1);
				final int baws3 = random3;
				World.getWorld().submit(new Tickable(10) {
					@Override
					public void execute() {
						ObjectManager.submitPublicObject(new GameObject(14826,
								2978, 3864, 0, Face.NORTH, 10));
						ObjectManager.submitPublicObject(new GameObject(14826,
								2982, 3864, 0, Face.NORTH, 10));
						ObjectManager.submitPublicObject(new GameObject(14826,
								2982, 3868, 0, Face.NORTH, 10));
						ObjectManager.submitPublicObject(new GameObject(14826,
								2978, 3868, 0, Face.NORTH, 10));
						for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
							final Player p = PlayerManager.getSingleton()
									.getPlayers()[i];
							if (p == null)
								continue;

							if (p.inObelisk3() && !p.teleBlock) {
								p.setTarget(null);
								p.setFollowing(null);
								AnimationProcessor.addNewRequest(p, 1816, 1);
								GraphicsProcessor.addNewRequest(p, 342, 4, 0);
								TeleportationHandler.addNewRequest(p, X[baws3]
										- 1 + Misc.random(2), Y[baws3] - 1
										+ Misc.random(2), 0, 4);
								AnimationProcessor.addNewRequest(p, 715, 6);

							}
						}
						stop();

					}
				});
			}
			break;
		}

	}

}
