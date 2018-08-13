package com.server2.model.combat.additions;

import com.server2.InstanceDistributor;
import com.server2.Settings;
import com.server2.content.Achievements;
import com.server2.content.skills.hunter.Hunter;
import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.combat.HitExecutor;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.Location;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.npc.NPCAttacks;
import com.server2.model.entity.npc.impl.BossAgression;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Areas;
import com.server2.util.Misc;
import com.server2.world.PlayerManager;
import com.server2.world.XMLManager;
import com.server2.world.map.Region;
import com.server2.world.map.tile.TileManager;
import com.server2.world.objects.GameObject;
import com.server2.world.objects.GameObject.Face;
import com.server2.world.objects.ObjectManager;

/**
 * 
 * @author Lukas Pinckers
 * 
 */
public class MultiCannon {

	/** This is so you don't get trouble with static references **/
	/**
	 * Instances the MultiCannon class
	 */
	private static MultiCannon INSTANCE = new MultiCannon();

	/**
	 * Gets the MultiCannon instance.
	 */
	public static MultiCannon getInstance() {
		return INSTANCE;
	}

	/**
	 * And array that contains all the players that have their cannon currently
	 * set up
	 */
	private final Player[] playersCannoning = new Player[200];

	/**
	 * holds the maximum time in milliseconds a cannon can be active
	 */
	private final long MAX_TIME = 1500000;

	/**
	 * Adds a client to the playersCannoning array
	 * 
	 * @param client
	 */
	public void addtoPlayersCannoning(Player client) {
		for (int i = 0; i < 200; i++)
			if (playersCannoning[i] == null) {
				playersCannoning[i] = client;

				client.spot = i;
				break;
			}
	}

	/**
	 * Check if there is no object already
	 * 
	 * @param objectX
	 * @param objectY
	 * @param c
	 * @return
	 */
	private boolean canLay(Player c, int objectX, int objectY) {
		final int[] location = { objectX, objectY };
		if (!forbidden(c, location))
			// System.out.println("1");
			return false;
		for (final GameObject object : XMLManager.objects) {
			final int[] other = { object.getLocation().getX(),
					object.getLocation().getY() };
			if (object != null && object.getObjectId() != -1
					&& TileManager.calculateDistance(location, other) <= 3)
				// System.out.println("2");
				return false;
		}

		final int x = c.getAbsX();
		final int y = c.getAbsY();
		for (final GameObject object : ObjectManager.ingameObjects) {
			if (object == null)
				continue;
			if (object.getLocation().getX() == x
					&& object.getLocation().getY() == y)
				return false;
			return true;
		}

		/*
		 * if(!noMapObject(c, location[0], location[1])){
		 * System.out.println("3"); return false; }
		 */

		return true;
	}

	/**
	 * 
	 * @param client
	 * @return true if playercannoning array contains the client
	 */
	private boolean containsClient(Player client) {
		for (int i = 0; i < 200; i++)
			if (playersCannoning[i] != null && playersCannoning[i] == client)
				return true;
		return false;
	}

	public void fillCannon(Player client) {
		if (!client.firing) {
			client.getActionSender().sendMessage(
					"Your cannon isn't firing, simply click it to start.");
			return;
		}
		if (client.cannonBalls >= 30) {
			client.getActionSender().sendMessage(
					"Your cannon is already fully loaded with cannon balls.");
			return;
		}
		if (!client.getActionAssistant().playerHasItem(2, 1)) {
			client.getActionSender().sendMessage(
					"You need cannonballs as ammo for your cannon.");
			return;
		}
		int amount = 0;
		final int needed = 30 - client.cannonBalls;
		final int has = client.getActionAssistant().getItemAmount(2);
		if (has >= needed)
			amount = needed;
		else if (has < needed)
			amount = has;
		if (amount > 30)
			amount = 30;
		client.cannonBalls = client.cannonBalls + amount;
		client.getActionAssistant().deleteItem(2, amount);

	}

	/**
	 * 
	 * @param client
	 * @param X
	 * @param Y
	 */
	public void fire(Player client, int X, int Y) {
		if (!isClientsCannon(client, X, Y)) {
			client.getActionSender().sendMessage("That is not your cannon!");
			return;
		}
		if (client.firing) {
			client.getActionSender().sendMessage(
					"Your cannon is already firing.");
			return;
		}
		if (client.cannonBalls <= 0) {
			int cannonBalls = 30;
			if (client.getActionAssistant().getItemAmount(2) < 30)
				cannonBalls = client.getActionAssistant().getItemAmount(2);
			if (!client.getActionAssistant().playerHasItem(2, 1)) {
				client.getActionSender().sendMessage(
						"You need cannonballs as ammo for your cannon.");
				return;
			}
			client.cannonBalls = cannonBalls;
			client.getActionAssistant().deleteItem(2, cannonBalls);
			client.firing = true;

		}
	}

	/**
	 * Checks if the players can lay his cannon at the location
	 * 
	 * @param c
	 * @param location
	 * @return
	 */
	private boolean forbidden(Player c, int[] location) {
		final Location pos = c.getPosition();
		if (BossAgression.nexRoom(c.getAbsX(), c.getAbsY(), c.getHeightLevel()))
			return false;
		if (Region.getClipping(c.getAbsX(), c.getAbsY(), c.heightLevel) == Region.SOLID_TILE)
			return false; // run it
		if (Hunter.getInstance().inSnareArea(c))
			return false;
		if (Hunter.getInstance().inBoxArea(c))
			return false;
		if (c.inWilderness())
			// System.out.println("x1");
			return false;
		if (Areas.isInDuelArenaFight(pos))
			// System.out.println("x2");
			return false;
		if (Areas.isAOD(pos))
			return false;
		if (Areas.isInEdge(pos))
			// System.out.println("x3");
			return false;
		if (c.gwdCoords())
			// System.out.println("x4");
			return false;
		if (BossAgression.nexRoom(location[0], location[1], c.getHeightLevel()))
			// System.out.println("x5");
			return false;
		if (Areas.isInSlayerTower(pos))
			// System.out.println("x6");
			return false;
		if (Areas.isInFremDung(pos))
			// System.out.println("x7");
			return false;
		if (Areas.isInBrimhavenDung(pos))
			// System.out.println("x8");
			return false;
		if (Areas.isInTaverlyDung(pos))
			// System.out.println("x9");
			return false;
		if (Areas.isInMysteriousDung(pos))
			// System.out.println("x10");
			return false;
		if (Areas.isWaterCave(pos))
			// System.out.println("x11");
			return false;
		if (Areas.isGlacors(pos))
			// System.out.println("x11");
			return false;
		if (Areas.isMonkey(pos))
			// System.out.println("x11");
			return false;
		if (Areas.isApeAtollGuard(pos))
			return false;
		return true;
	}

	/**
	 * Boolean that checks if the player has all the items to set up a dwarf
	 * multicannon
	 * 
	 * @param client
	 * @return
	 */
	private boolean hasItems(Player client) {
		if (client.getActionAssistant().isItemInBag(6)
				&& client.getActionAssistant().isItemInBag(8)
				&& client.getActionAssistant().isItemInBag(10)
				&& client.getActionAssistant().isItemInBag(12))
			return true;
		return false;
	}

	/**
	 * Checks whether the coords is in the range of the loop
	 * 
	 * @param cannonX
	 * @param cannonY
	 * @param cannonZ
	 * @param X
	 * @param Y
	 * @param Z
	 * @param rotation
	 * @return
	 */
	// look here's the checking
	public boolean inRange(int cannonX, int cannonY, int cannonZ, int X, int Y,
			int Z, int rotation) {
		if (cannonZ != Z)
			return false;
		if (cannonX == X && cannonY == Y)
			return false;

		/** north */
		if (rotation == 1)
			if (X == cannonX || X == cannonX + 1 || X == cannonX - 1)
				if (Y == cannonY + 1 || Y == cannonY + 2 || Y == cannonY + 3
						|| Y == cannonY + 4 || Y == cannonY + 5
						|| Y == cannonY + 6)
					return true;
		/** north east */
		if (rotation == 2) {
			if (X == cannonX + 4 && Y == cannonY + 4)
				return true;
			if (X == cannonX + 3 && Y == cannonY + 3)
				return true;
			if (X == cannonX + 2 && Y == cannonY + 2)
				return true;
			if (X == cannonX + 1 && Y == cannonY + 1)
				return true;
			if (X == cannonX + 2 && Y == cannonY + 3)
				return true;
			if (X == cannonX + 3 && Y == cannonY + 2)
				return true;
			if (X == cannonX + 4 && Y == cannonY + 3)
				return true;
			if (X == cannonX + 3 && Y == cannonY + 4)
				return true;
			if (X == cannonX + 5 && Y == cannonY + 4)
				return true;
			if (X == cannonX + 4 && Y == cannonY + 5)
				return true;
		}
		/** east */
		if (rotation == 3)
			if (Y == cannonY || Y == cannonY + 1 || Y == cannonY - 1)
				if (X == cannonX + 1 || X == cannonX + 2 || X == cannonX + 3
						|| X == cannonX + 4 || X == cannonX + 5
						|| X == cannonX + 6)
					return true;

		/** south east */
		if (rotation == 4) {
			if (X == cannonX + 1 && Y == cannonY - 1)
				return true;
			if (X == cannonX + 2 && Y == cannonY - 2)
				return true;
			if (X == cannonX + 3 && Y == cannonY - 3)
				return true;
			if (X == cannonX + 4 && Y == cannonY - 4)
				return true;
			if (X == cannonX + 5 && Y == cannonY - 4)
				return true;
			if (X == cannonX + 4 && Y == cannonY - 5)
				return true;
			if (X == cannonX + 4 && Y == cannonY - 3)
				return true;
			if (X == cannonX + 3 && Y == cannonY - 4)
				return true;
			if (X == cannonX + 3 && Y == cannonY - 2)
				return true;
			if (X == cannonX + 2 && Y == cannonY - 3)
				return true;
		}

		/** south */
		if (rotation == 5)
			if (X == cannonX || X == cannonX + 1 || X == cannonX - 1)
				if (Y == cannonY - 1 || Y == cannonY - 2 || Y == cannonY - 3
						|| Y == cannonY - 4 || Y == cannonY - 5
						|| Y == cannonY - 6)
					return true;

		/** south west */
		if (rotation == 6) {
			if (X == cannonX - 1 && Y == cannonY - 1)
				return true;
			if (X == cannonX - 2 && Y == cannonY - 2)
				return true;
			if (X == cannonX - 3 && Y == cannonY - 3)
				return true;
			if (X == cannonX - 4 && Y == cannonY - 4)
				return true;
			if (X == cannonX - 5 && Y == cannonY - 4)
				return true;
			if (X == cannonX - 4 && Y == cannonY - 5)
				return true;
			if (X == cannonX - 4 && Y == cannonY - 3)
				return true;
			if (X == cannonX - 3 && Y == cannonY - 4)
				return true;
			if (X == cannonX - 3 && Y == cannonY - 2)
				return true;
			if (X == cannonX - 2 && Y == cannonY - 3)
				return true;
		}

		/** west */
		if (rotation == 7)
			if (Y == cannonY || Y == cannonY + 1 || Y == cannonY - 1)
				if (X == cannonX - 1 || X == cannonX - 2 || X == cannonX - 3
						|| X == cannonX - 4 || X == cannonX - 5
						|| X == cannonX - 6)
					return true;

		/** north west */
		if (rotation == 8) {
			if (X == cannonX - 1 && Y == cannonY + 1)
				return true;
			if (X == cannonX - 2 && Y == cannonY + 2)
				return true;
			if (X == cannonX - 3 && Y == cannonY + 3)
				return true;
			if (X == cannonX - 4 && Y == cannonY + 4)
				return true;
			if (X == cannonX - 5 && Y == cannonY + 4)
				return true;
			if (X == cannonX - 4 && Y == cannonY + 5)
				return true;
			if (X == cannonX - 4 && Y == cannonY + 3)
				return true;
			if (X == cannonX - 3 && Y == cannonY + 4)
				return true;
			if (X == cannonX - 3 && Y == cannonY + 2)
				return true;
			if (X == cannonX - 2 && Y == cannonY + 3)
				return true;
		}

		return false;
	}

	/**
	 * 
	 * @param client
	 * @param X
	 * @param Y
	 * @return true if the cannon the client clicked on belongs to the him
	 */
	public boolean isClientsCannon(Player client, int X, int Y) {
		if (client.cannon != null)
			if (client.cannon.getLocation().getX() == X
					&& client.cannon.getLocation().getY() == Y)
				return true;
		return false;
	}

	/**
	 * Handles the laying down of the cannon
	 * 
	 * @param c
	 */

	public void lay(Player c) {

		if (c.dwarfStage != 8)
			c.sendMessage("@dre@Your cannon will be stronger if you complete the Dwarf Cannon Quest");

		if (c.setUp || containsClient(c)) {
			c.getActionSender().sendMessage("You have already setup a cannon.");
			return;
		}

		if (otherCannon(c, c.getAbsY(), c.getAbsY(), c.getHeightLevel())) {
			c.getActionSender().sendMessage(
					"You cannot setup your cannon so close to another cannon.");
			return;
		}
		if (!canLay(c, c.getAbsY(), c.getAbsY())) {
			c.getActionSender().sendMessage(
					"You cannot setup your cannon here.");
			return;
		}
		if (c.getAbsY() < 3735 && c.getAbsX() > 2650 && c.getAbsY() > 3710
				&& c.getAbsX() < 2690) {
			c.getActionSender().sendMessage(
					"You cannot setup your cannon here.");
			c.getActionSender()
					.sendMessage(
							"If you want to cannon rock crabs, please talk to Lord Daquarius.");
			return;
		}
		if (!hasItems(c)) {
			c.getActionSender()
					.sendMessage(
							"You do all the items required to set up a dwarf multicannon");
			return;
		}
		if (!c.multiZone()) {
			c.getActionSender()
					.sendMessage(
							"@red@ Since this is a single combat zone, your cannon will only attack monster that you are attacking");
			c.getActionSender()
					.sendMessage(
							"@red@If you want to use your cannon effectively, it is adviced to go to a multicombat zone like dagannoths or frost dragons.");
		}
		if (System.currentTimeMillis() - c.cannonDelay < 5000) {
			c.getActionSender().sendMessage(
					"You have to wait for a bit before you can do this again.");
			return;
		}
		final int X = c.getAbsX();
		final int Y = c.getAbsY();
		final int Z = c.getHeightLevel();
		final Player client = c;
		client.cannonDelay = System.currentTimeMillis();
		client.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (client.stage == 0
						&& client.getActionAssistant().isItemInBag(6)) {
					AnimationProcessor.addNewRequest(client, 827, 0);
					client.getActionAssistant().deleteItem(6, 1);
					client.cannon = new GameObject(7, X, Y, Z, Face.NORTH, 10);
					ObjectManager.submitPublicObject(client.cannon);
					client.cannon.scheduleRemoval();
					client.setUpBase = true;
					client.setUpTime = System.currentTimeMillis();
					addtoPlayersCannoning(client);
					client.stage++;
				} else if (client.stage == 1 && client.setUpBase
						&& client.getActionAssistant().isItemInBag(8)) {
					AnimationProcessor.addNewRequest(client, 827, 0);
					client.getActionAssistant().deleteItem(8, 1);
					client.cannon.scheduleRemoval();
					client.cannon = new GameObject(8, X, Y, Z, Face.NORTH, 10);
					ObjectManager.submitPublicObject(client.cannon);
					client.setUpStand = true;
					client.stage++;
				} else if (client.stage == 2 && client.setUpBase
						&& client.setUpStand
						&& client.getActionAssistant().isItemInBag(10)) {
					AnimationProcessor.addNewRequest(client, 827, 0);
					client.getActionAssistant().deleteItem(10, 1);
					client.cannon.scheduleRemoval();
					client.cannon = new GameObject(9, X, Y, Z, Face.NORTH, 10);
					ObjectManager.submitPublicObject(client.cannon);
					client.setUpBarrels = true;
					client.stage++;
				} else if (client.stage == 3 && client.setUpBase
						&& client.setUpStand && client.setUpBarrels
						&& client.getActionAssistant().isItemInBag(12)) {
					AnimationProcessor.addNewRequest(client, 827, 0);
					client.getActionAssistant().deleteItem(12, 1);
					client.cannon.scheduleRemoval();
					client.cannon = new GameObject(6, X, Y, Z, Face.NORTH, 10);
					ObjectManager.submitPublicObject(client.cannon);
					client.setUpFurnace = true;
					Achievements.getInstance().complete(client, 20);
					client.setUp = true;
					client.stage = 0;
					container.stop();

				} else {
					container.stop();
					return;
				}
			}

			@Override
			public void stop() {
				// TODO Auto-generated method stub

			}
		}, 2);
	}

	/**
	 * Check if there is no mapObject at the place the player wants the lay his
	 * cannon
	 * 
	 * @param client
	 * @param X
	 * @param Y
	 * @return
	 */
	public boolean noMapObject(Player client, int X, int Y) {
		if (!Region.canMove(X, Y, X + 1, Y, client.getHeightLevel(), 1, 1))
			return false;
		if (!Region.canMove(X, Y, X - 1, Y, client.getHeightLevel(), -1, 1))
			return false;
		if (!Region.canMove(X, Y, X, Y + 1, client.getHeightLevel(), 1, 1))
			return false;
		if (!Region.canMove(X, Y, X, Y - 1, client.getHeightLevel(), 1, 1))
			return false;
		if (!Region.canMove(X, Y, X + 1, Y + 1, client.getHeightLevel(), 1, 1))
			return false;
		if (!Region.canMove(X, Y, X - 1, Y - 1, client.getHeightLevel(), 1, 1))
			return false;
		return true;
	}

	public void onDisconnect(Player client) {
		if (client.cannon != null) {
			client.cannon.scheduleRemoval();
			removeFromPlayersCannoning(client);
		}
	}

	private boolean otherCannon(Player client, int x, int y, int z) {
		for (int i = 0; i < 200; i++)
			if (playersCannoning[i] != null)
				if (playersCannoning[i].cannon != null)
					if (playersCannoning[i].cannon.getLocation().getZ() == z) {
						final int[] l1 = { x, y };
						final int[] l2 = {
								playersCannoning[i].cannon.getLocation().getX(),
								playersCannoning[i].cannon.getLocation().getY() };
						if (TileManager.calculateDistance(l1, l2) < 4)
							return true;
					}
		return false;
	}

	/**
	 * Handles the picking up of the cannon
	 * 
	 * @param client
	 * @param X
	 * @param Y
	 */
	public void pickup(Player client, int X, int Y) {
		if (!isClientsCannon(client, X, Y)) {
			client.getActionSender().sendMessage("That is not your cannon!");
			return;
		}
		int neededSlots = 4;
		if (client.cannonBalls > 0)
			neededSlots = 5;
		if (client.getActionAssistant().freeSlots() < neededSlots) {
			client.getActionSender()
					.sendMessage(
							"You do not have enough space in your inventory to pick up the cannon.");
			return;
		}
		AnimationProcessor.addNewRequest(client, 827, 0);
		client.getActionSender().addItem(6, 1);
		client.getActionSender().addItem(8, 1);
		client.getActionSender().addItem(10, 1);
		client.getActionSender().addItem(12, 1);
		if (neededSlots == 5)
			client.getActionSender().addItem(2, client.cannonBalls);
		client.cannon.scheduleRemoval();
		client.cannonBalls = 0;
		client.setUp = false;
		client.rotation = 0;
		client.setUpBase = false;
		client.setUpFurnace = false;
		client.setUpBarrels = false;
		client.setUpStand = false;
		client.rotation = 1;
		client.firing = false;
		client.setUpTime = 0;
		client.cannon = null;
		client.messageGiven = false;
		removeFromPlayersCannoning(client);

	}

	/**
	 * Lets all the cannons fire etc.
	 */
	public void process(Player client) {

		/*
		 * for (int i = 0; i < 200; i++) { if (playersCannoning[i] == null) {
		 * continue; } else { Player client = playersCannoning[i];
		 */
		if (client.setUpTime + MAX_TIME < System.currentTimeMillis() + 300000
				&& !client.messageGiven) {
			client.messageGiven = true;
			client.getActionSender().sendMessage(
					"@red@ Your cannon is about to decay!");
		}
		if (client.setUpTime + MAX_TIME < System.currentTimeMillis()) {
			client.getActionSender().sendMessage(
					"@red@ Your cannon has decayed!");
			client.cannon.scheduleRemoval();
			client.cannonBalls = 0;
			client.setUp = false;
			client.rotation = 0;
			client.setUpBase = false;
			client.setUpFurnace = false;
			client.setUpBarrels = false;
			client.setUpStand = false;
			client.firing = false;
			client.setUpTime = 0;
			client.cannon = null;
			client.rotation = 1;
			client.messageGiven = false;
			removeFromPlayersCannoning(client);
			return;
		}
		boolean stop = false;
		if (client.firing) {

			switch (client.rotation) {
			case 1: // north
				if (client.cannonBalls <= 0) {
					client.getActionSender().sendMessage(
							"Your cannon has run out of ammo!");
					stop = true;
					client.firing = false;
					client.cannonBalls = 0;
				}
				client.getActionSender().objectAnim(
						client.cannon.getLocation().getX(),
						client.cannon.getLocation().getY(), 516, 10, -1);

				break;
			case 2: // north-east
				client.getActionSender().objectAnim(
						client.cannon.getLocation().getX(),
						client.cannon.getLocation().getY(), 517, 10, -1);
				break;
			case 3: // east
				client.getActionSender().objectAnim(
						client.cannon.getLocation().getX(),
						client.cannon.getLocation().getY(), 518, 10, -1);
				break;
			case 4: // south-east
				client.getActionSender().objectAnim(
						client.cannon.getLocation().getX(),
						client.cannon.getLocation().getY(), 519, 10, -1);
				break;
			case 5: // south
				client.getActionSender().objectAnim(
						client.cannon.getLocation().getX(),
						client.cannon.getLocation().getY(), 520, 10, -1);
				break;
			case 6: // south-west
				client.getActionSender().objectAnim(
						client.cannon.getLocation().getX(),
						client.cannon.getLocation().getY(), 521, 10, -1);
				break;
			case 7: // west
				client.getActionSender().objectAnim(
						client.cannon.getLocation().getX(),
						client.cannon.getLocation().getY(), 514, 10, -1);
				break;
			case 8: // north-west
				client.getActionSender().objectAnim(
						client.cannon.getLocation().getX(),
						client.cannon.getLocation().getY(), 515, 10, -1);

				break;
			}
			if (stop)
				return;
			else {
				client.rotation++;
				if (client.rotation >= 9)
					client.rotation = 1;
				int shots = 0;

				if (client.cannonBalls > 0)
					for (final NPC npc : InstanceDistributor.getNPCManager()
							.getNPCMap().values()) {
						if (npc == null)
							continue;
						if (!npc.multiZone() || !client.multiZone())
							if (npc != client.getTarget())
								continue;
						if (npc.getOwner() == client)
							continue;
						if (npc.getDefinition().getHealth() < 2)
							continue;
						if (npc.getOwner() != null)
							continue;
						if (shots < 2 && !npc.isDead() && npc != null)
							if (inRange(client.cannon.getLocation().getX(),
									client.cannon.getLocation().getY(),
									client.cannon.getLocation().getZ(),
									npc.getAbsX(), npc.getAbsY(),
									npc.getHeightLevel(), client.rotation))
								if (client.cannonBalls > 0) {
									int damage = Misc.random(15)

									;
									if (Misc.random(5) == 1)
										damage = damage + Misc.random(15);

									final int defence = NPCAttacks.npcArray[npc
											.getDefinition().getType()][7];
									final int ranged = client.playerLevel[PlayerConstants.RANGE];
									if (Misc.random(defence) > Misc
											.random(ranged) + 75)
										damage = 0;

									if (client.dwarfStage != 8) {
										if (damage != 0)
											damage = (int) (damage * 0.5);
										if (Misc.random(2) == 1)
											damage = 0;
									}
									final int absX = client.cannon
											.getLocation().getX();
									final int absY = client.cannon
											.getLocation().getY();
									final int lockon = npc.getNpcId() + 1;
									final int angle = 0;
									final int offsetX = (absX - npc.getAbsX())
											* -1;
									final int offsetY = (absY - npc.getAbsY())
											* -1;
									final int proID = 53;
									final int startHeight = 42;
									final int endHeight = 20;
									final int speed = 75;
									for (int x = 0; x < Settings
											.getLong("sv_maxclients"); x++) {
										final Player p = PlayerManager
												.getSingleton().getPlayers()[x];
										if (p == null)
											continue;
										if (!p.isActive || p.disconnected)
											continue;

										if (p.withinDistance(client))
											p.getActionSender().sendProjectile(
													absY, absX, offsetY,
													offsetX, proID,
													startHeight, endHeight,
													speed, angle, lockon);
									}
									HitExecutor.addNewHit(client, npc,
											CombatType.CANNON, damage, 2);
									client.getActionAssistant().addSkillXP(
											damage * client.multiplier / 2,
											PlayerConstants.RANGE);
									client.cannonBalls--;
									client.progress[84]++;
									if (client.progress[84] >= 5000)
										Achievements.getInstance().complete(
												client, 84);
									else
										Achievements.getInstance().turnYellow(
												client, 84);
									shots++;
								}
					}

			}

		}
	}

	/**
	 * Removes a client from the playersCannoning array
	 * 
	 * @param client
	 */
	public void removeFromPlayersCannoning(Player client) {
		playersCannoning[client.spot] = null;

	}
}