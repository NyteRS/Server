package com.server2.content.minigames;

import java.util.ArrayList;
import java.util.List;

import com.server2.Constants;
import com.server2.content.Achievements;
import com.server2.engine.cycle.Tickable;
import com.server2.model.Item;
import com.server2.model.entity.Location;
import com.server2.model.entity.player.Equipment;
import com.server2.model.entity.player.Player;
import com.server2.util.Areas;
import com.server2.util.Misc;
import com.server2.world.World;
import com.server2.world.objects.GameObject;
import com.server2.world.objects.ObjectManager;

/**
 * Castle wars minigames
 * 
 * @author Ultimate1
 */

public class CastleWars {

	private interface GameConstants {

		public int MAXIMUM_PLAYERS = 100;

		public long WAIT_DURATION = 60000;

		public int GAME_DURATION = 20 * 60000;

		public int FLAG_STATUS_SAFE = 0;

		public int FLAG_STATUS_TAKEN = 1;

		public int FLAG_STATUS_DROPPED = 2;

		public int DOOR_STATUS_LOCKED = 0;

		public int DOOR_STATUS_UNLOCKED = 1;

		public int ROCK_STATUS_COLASPED = 0;

		public int CATAPULT_STATUS_DESTROYED = 1;

		public Location CASTLE_WAR_BANK = new Location(2440, 3089, 0);

		public GameObject SARADOMIN_FLAG_OBJECT = new GameObject(4902, 2429,
				3074, 3, GameObject.Face.EAST, 10);

		public GameObject EMPTY_SARADOMIN_FLAG_OBJECT = new GameObject(4377,
				2429, 3074, 3, GameObject.Face.EAST, 10);

		public GameObject ZAMORAK_FLAG_OBJECT = new GameObject(4903, 2370,
				3133, 3, GameObject.Face.WEST, 10);

		public GameObject EMPTY_ZAMORAK_FLAG_OBJECT = new GameObject(4378,
				2370, 3133, 3, GameObject.Face.WEST, 10);

		public Item SARADOMIN_FLAG = new Item(4037);

		public Item ZAMORAK_FLAG = new Item(4039);

		public Item BANDAGES = new Item(4049);

	}

	public class Team {

		private final String name;

		private final List<Player> pendingParticipants;

		private final List<Player> participants;

		private final Location waitingRoom;

		private final Location base;

		private final int cloakId;

		private int flagStatus = GameConstants.FLAG_STATUS_SAFE;

		private boolean sideDoorUnlocked;

		private boolean mainDoorUnlocked;

		private int doorHealth = 100;

		private int score = 0;

		private GameObject flagObject;

		private GameObject emptyFlagObject;

		private Item flag;

		public Team(String name, int cloakId, Location waitingRoom,
				Location base) {
			this.name = name;
			participants = new ArrayList<Player>();
			pendingParticipants = new ArrayList<Player>();
			this.waitingRoom = waitingRoom;
			this.base = base;
			this.cloakId = cloakId;
		}

		public void addToTeam(Player player) {
			participants.add(player);
			player.teleport(base);
			player.castleWarsing = true;
		}

		public Location getBase() {
			return base;
		}

		public int getCloakId() {
			return cloakId;
		}

		public int getDoorHealth() {
			return doorHealth;
		}

		public GameObject getEmptyFlagObject() {
			return emptyFlagObject;
		}

		public Item getFlag() {
			return flag;
		}

		public GameObject getFlagObject() {
			return flagObject;
		}

		public int getFlagStatus() {
			return flagStatus;
		}

		public int getInterfaceConfiguration() {
			int config = 0;
			if (this == zamorak) {
				config = doorHealth;
				config += 128 * (sideDoorUnlocked ? GameConstants.DOOR_STATUS_UNLOCKED
						: GameConstants.DOOR_STATUS_LOCKED);
				config += 256 * GameConstants.ROCK_STATUS_COLASPED;
				config += 512 * GameConstants.ROCK_STATUS_COLASPED;
				config += 1024 * GameConstants.CATAPULT_STATUS_DESTROYED;
				config += 2097152 * flagStatus;
				config += 16777216 * score;
			} else if (this == saradomin) {
				config = doorHealth;
				config += 128 * (sideDoorUnlocked ? GameConstants.DOOR_STATUS_UNLOCKED
						: GameConstants.DOOR_STATUS_LOCKED);
				config += 256 * GameConstants.ROCK_STATUS_COLASPED;
				config += 512 * GameConstants.ROCK_STATUS_COLASPED;
				config += 1024 * GameConstants.CATAPULT_STATUS_DESTROYED;
				config += 2097152 * zamorak.flagStatus;
				config += 16777216 * zamorak.score;
			}
			return config;
		}

		public int getScore() {
			return score;
		}

		public void joinWaitingRoom(Player player) {
			final int helmId = player.playerEquipment[Equipment.SLOT_HEAD];
			final int capeId = player.playerEquipment[Equipment.SLOT_CAPE];
			if (!Constants.CWARS || !Constants.MINIGAME) {
				player.sendMessage("Minigames are currently disabled!");
				return;
			}
			if (capeId != -1 || helmId != -1)
				player.getActionSender().sendMessage(
						"Please remove your hat and cape before entering.");
			else if (pendingParticipants.size() >= GameConstants.MAXIMUM_PLAYERS)
				player.getActionSender().sendMessage(
						"The " + name + " team is full!");
			else {
				if (pendingParticipants.contains(player))
					return;
				pendingParticipants.add(player);
				player.teleport(waitingRoom);
				player.setCastleWarsTeam(this);
				player.getActionSender().sendMessage(
						"You join the " + name + " team.");
				player.getEquipment().setEquipment(cloakId, 1,
						Equipment.SLOT_CAPE);
				player.appearanceUpdateRequired = true;
			}
		}

		public void leaveWaitingRoom(Player player) {
			if (!pendingParticipants.contains(player))
				return;
			pendingParticipants.remove(player);
			player.getActionSender().sendWalkableInterface(-1);
			player.getActionSender().sendMessage(
					"You leave the " + name + " team.");
			player.teleport(GameConstants.CASTLE_WAR_BANK);
			player.setCastleWarsTeam(null);
			removeGameItems(player);
		}

		public void removeFromTeam(Player player) {
			participants.remove(player);
			player.setCastleWarsTeam(null);
			player.teleport(GameConstants.CASTLE_WAR_BANK);
			player.castleWarsing = false;
		}

		public void reset() {

			// Reset team attributes for the next game.
			flagStatus = GameConstants.FLAG_STATUS_SAFE;
			sideDoorUnlocked = false;
			mainDoorUnlocked = false;
			doorHealth = 100;
			score = 0;

			// Teleport out all players and remove all game items.
			for (final Player player : participants) {

				// Remove all game items.
				removeGameItems(player);

				// Teleport out of the game.
				player.teleport(GameConstants.CASTLE_WAR_BANK);

				// Reset player options.
				// player.getClient().sendPlayerOptions();

				// Remove the castle wars interface.
				player.getActionSender().sendWalkableInterface(-1);

				// The player no longer has a team.
				player.setCastleWarsTeam(null);

			}

			// Clear the list of participants for the next game.
			participants.clear();

		}

		public void setEmptyFlagObject(GameObject emptyFlagObject) {
			this.emptyFlagObject = emptyFlagObject;
		}

		public void setFlag(Item flag) {
			this.flag = flag;
		}

		public void setFlagObject(GameObject flagObject) {
			this.flagObject = flagObject;
		}

		public void setFlagStatus(int flagStatus) {
			this.flagStatus = flagStatus;
		}

	}

	private static CastleWars INSTANCE = new CastleWars();

	public static CastleWars getInstance() {
		return INSTANCE;
	}

	public final Team saradomin = new Team("Saradomin", 4041, new Location(
			2382, 9488, 0), new Location(2427, 3076, 1));
	public final Team zamorak = new Team("Zamorak", 4042, new Location(2423,
			9522, 0), new Location(2372, 3131, 1));

	private final List<Player> lobby = new ArrayList<Player>();
	private final List<Player> participants = new ArrayList<Player>();
	private long waitStartedAt = System.currentTimeMillis();

	private long gameStartedAt = System.currentTimeMillis();

	private boolean gameInProgress = false;

	private CastleWars() {

		saradomin.setFlagObject(GameConstants.SARADOMIN_FLAG_OBJECT);
		saradomin.setEmptyFlagObject(GameConstants.EMPTY_SARADOMIN_FLAG_OBJECT);
		saradomin.setFlag(GameConstants.SARADOMIN_FLAG);

		zamorak.setFlagObject(GameConstants.ZAMORAK_FLAG_OBJECT);
		zamorak.setEmptyFlagObject(GameConstants.EMPTY_ZAMORAK_FLAG_OBJECT);
		zamorak.setFlag(GameConstants.ZAMORAK_FLAG);

	}

	public void dropFlag(final Player player, final int weaponId) {

		// If the saradomin flag was dropped.
		if (weaponId == saradomin.getFlag().getId()) {
			for (final Player other : saradomin.participants)
				if (other != null) {
					// Remove the location pointer of the player.
					other.getActionSender().sendLocationPointerPlayer(0, 0);

					// Create a location pointer where the object is to be
					// dropped
					other.getActionSender().sendLocationPointer(2,
							player.getCoordinates().getX(),
							player.getCoordinates().getY(), 180);
				}
			saradomin.setFlagStatus(GameConstants.FLAG_STATUS_DROPPED);
		}

		// If the zamorak flag was dropped.
		if (weaponId == zamorak.getFlag().getId()) {
			for (final Player other : zamorak.participants)
				if (other != null) {
					// Remove the location pointer of the player.
					other.getActionSender().sendLocationPointerPlayer(0, 0);

					// Create a location pointer where the object is to be
					// dropped
					other.getActionSender().sendLocationPointer(2,
							player.getCoordinates().getX(),
							player.getCoordinates().getY(), 180);
				}
			zamorak.setFlagStatus(GameConstants.FLAG_STATUS_DROPPED);
		}

		// Drop the flag on floor.
		if (!player.disconnected)
			player.getEquipment().deleteEquipment(Equipment.SLOT_WEAPON);

		{
			// Create the world object.
			final GameObject flag = new GameObject(weaponId == saradomin
					.getFlag().getId() ? 4900 : 4901, player.getPosition(),
					GameObject.Face.NORTH, 10);

			// Register the global object.
			ObjectManager.submitPublicObject(flag);

			// Schedule the flag to be removed as soon as picked up.
			World.getWorld().submit(new Tickable(1) {
				@Override
				public void execute() {

					// If the saradomin flag was picked up.
					if (weaponId == saradomin.getFlag().getId())
						if (saradomin.getFlagStatus() == GameConstants.FLAG_STATUS_TAKEN
								|| saradomin.getFlagStatus() == GameConstants.FLAG_STATUS_SAFE) {
							ObjectManager.submitPublicObject(new GameObject(-1,
									flag.getLocation().getX(), flag
											.getLocation().getY(), flag
											.getLocation().getZ(),
									GameObject.Face.NORTH, 10)); // TODO
							stop();
						}

					// If the zamorak flag was picked up.
					if (weaponId == zamorak.getFlag().getId())
						if (zamorak.getFlagStatus() == GameConstants.FLAG_STATUS_TAKEN
								|| zamorak.getFlagStatus() == GameConstants.FLAG_STATUS_SAFE) {
							ObjectManager.submitPublicObject(new GameObject(-1,
									flag.getLocation().getX(), flag
											.getLocation().getY(), flag
											.getLocation().getZ(),
									GameObject.Face.NORTH, 10));
							stop();
						}

				}
			});
		}

	}

	public Team getTeamInstance(Team t) {
		if (t == saradomin)
			return saradomin;
		return zamorak;
	}

	public void handleDroppedFlag(Player player, final Team base,
			final Team otherTeam) {

		// Do nothing if we are dead.
		if (player.isDead())
			return;

		// Get our weapon.
		final int weaponId = player.playerEquipment[Equipment.SLOT_WEAPON];

		// Stop! We already have a flag in our hands.
		if (weaponId == saradomin.getFlag().getId()
				|| weaponId == zamorak.getFlag().getId())
			return;

		if (base.getFlagStatus() == GameConstants.FLAG_STATUS_DROPPED) {

			// Remove hint for Zamorak players.
			for (final Player other : base.participants)
				other.getActionSender().sendLocationPointer(0, 0, 0, 0);

			if (player.getCastleWarsTeam() == saradomin && base == saradomin
					&& Areas.isAtSaradominBase(player.getCoordinates())) {
				base.setFlagStatus(GameConstants.FLAG_STATUS_SAFE);
				return;
			}

			if (player.getCastleWarsTeam() == zamorak && base == zamorak
					&& Areas.isAtZamorakBase(player.getCoordinates())) {
				base.setFlagStatus(GameConstants.FLAG_STATUS_SAFE);
				return;
			}

			// Equip the flag.
			base.setFlagStatus(GameConstants.FLAG_STATUS_TAKEN);
			player.getEquipment().setEquipment(base.getFlag().getId(), 1,
					Equipment.SLOT_WEAPON);
			player.appearanceUpdateRequired = true;
			player.progress[67]++;
			if (player.progress[67] >= 10)
				Achievements.getInstance().complete(player, 67);
			else
				Achievements.getInstance().turnYellow(player, 67);

			// Hint team of the flag where-abouts.
			for (final Player other : base.participants)
				other.getActionSender().sendLocationPointerPlayer(10,
						player.getIndex());

		}

	}

	public void handleEmptyFlagPost(Player player, final Team base,
			final Team otherTeam) {

		// Do nothing if we are dead.
		if (player.isDead())
			return;

		// Get our weapon.
		final int weaponId = player.playerEquipment[Equipment.SLOT_WEAPON];

		// If the enemy is trying to take the flag.
		if (player.getCastleWarsTeam() == otherTeam)
			player.getActionSender().sendMessage(
					"The flag has already been taken.");
		else // If we are returning the flag.
		if (weaponId == base.getFlag().getId()) {

			// Return the flag.
			base.setFlagStatus(GameConstants.FLAG_STATUS_SAFE);
			player.getActionSender()
					.sendMessage("You return your team's flag.");
			player.getEquipment().deleteEquipment(Equipment.SLOT_WEAPON);
			player.appearanceUpdateRequired = true;
		}
	}

	public void handleFlagPost(Player player, final Team base,
			final Team otherTeam) {

		// Do nothing if we are dead.
		if (player.isDead())
			return;

		// Get our weapon.
		final int weaponId = player.playerEquipment[Equipment.SLOT_WEAPON];

		// Handle scoring a point.
		if (player.getCastleWarsTeam() == base)
			// Score!
			if (weaponId == otherTeam.getFlag().getId()) {
				otherTeam.setFlagStatus(GameConstants.FLAG_STATUS_SAFE);
				player.getActionSender().sendMessage(
						"You score a point for your team!");
				player.getEquipment().deleteEquipment(Equipment.SLOT_WEAPON);
				for (final Player z : otherTeam.participants)
					z.getActionSender().sendLocationPointer(0, 0, 0, 0);
				player.appearanceUpdateRequired = true;
				base.score++;
				return;
			}

		// If we are not trying to get our own flag.
		if (player.getCastleWarsTeam() != base)
			if (base.getFlagStatus() == GameConstants.FLAG_STATUS_SAFE) {

				// Stop! We already have a flag in our hands.
				if (weaponId == otherTeam.getFlag().getId())
					return;

				// You can't get the flag if you have a weapon on the weapon
				// slot!
				if (weaponId != -1) {
					player.getActionSender()
							.sendMessage(
									"Please free your hands before attempting to take the flag.");
					return;
				}

				// Set the player's weapon as the enemy flag.
				player.getEquipment().setEquipment(base.getFlag().getId(), 1,
						Equipment.SLOT_WEAPON);
				player.appearanceUpdateRequired = true;

				// Mark the enemy's flag as taken.
				base.setFlagStatus(GameConstants.FLAG_STATUS_TAKEN);

				// Display an empty flag stand.
				ObjectManager.submitPublicObject(base.getEmptyFlagObject());

				// Hint the enemy that the flag has been taken.
				for (final Player other : base.participants)
					other.getActionSender().sendLocationPointerPlayer(10,
							player.getIndex());

				// Schedule the flag to return to its original form.
				World.getWorld().submit(new Tickable(1) {
					@Override
					public void execute() {
						if (base.flagStatus == GameConstants.FLAG_STATUS_SAFE) {
							ObjectManager.submitPublicObject(base
									.getFlagObject());
							stop();
						}
					}
				});

			}
	}

	public void handleObject(Player player, int type, int objectId,
			int objectX, int objectY) {
		if (player.getCastleWarsTeam() == null)
			switch (objectId) {
			case 4387:
				saradomin.joinWaitingRoom(player);
				break;
			case 4388:
				zamorak.joinWaitingRoom(player);
				break;
			case 4408:
				if (Misc.random(1) == 0)
					saradomin.joinWaitingRoom(player);
				else
					zamorak.joinWaitingRoom(player);
				break;
			}
		else {
			if (type == 2) {
				switch (objectId) {
				case 4423: // Saradomin base - main door 1
				case 4424: // Saradomin base - main door 2
				{
					final int weaponId = player.playerEquipment[Equipment.SLOT_WEAPON];
					if (weaponId != -1) {
						saradomin.doorHealth -= Misc.random(25);
						if (saradomin.doorHealth <= 0) {
							ObjectManager.submitPublicObject(new GameObject(
									new Location(2427, 3088, 0), 4423, -1, -1,
									zamorak.mainDoorUnlocked ? -1 : 0, 0,
									player));
							ObjectManager.submitPublicObject(new GameObject(
									new Location(2427, 3088, 0), 4424, -1, -1,
									zamorak.mainDoorUnlocked ? -1 : -2, 0,
									player));
							saradomin.doorHealth = 0;
						}
						player.getActionAssistant().startAnimation(
								Equipment.getAttackEmote(player), 15);
					}
				}
					break;
				case 4427: // Zamorak base - main door 1
				case 4428: // Zamorak base - main door 2
				{
					final int weaponId = player.playerEquipment[Equipment.SLOT_WEAPON];
					if (weaponId != -1) {
						zamorak.doorHealth -= Misc.random(25);
						if (zamorak.doorHealth <= 0) {
							ObjectManager.submitPublicObject(new GameObject(
									new Location(2373, 3119, 0), 4427, -1, -1,
									zamorak.mainDoorUnlocked ? -3 : -2, 0,
									player));
							ObjectManager.submitPublicObject(new GameObject(
									new Location(2373, 3119, 0), 4428, -1, -1,
									zamorak.mainDoorUnlocked ? -3 : 0, 0,
									player));
							zamorak.doorHealth = 0;
						}
						player.getActionAssistant().startAnimation(
								Equipment.getAttackEmote(player), 15);
					}
				}
					break;
				}
				return;
			}
			switch (objectId) {
			case 4411: // Handle stepping stones.
				handleSteppingStone(player, player.getCoordinates(),
						new Location(objectX, objectY));
				break;
			case 4389: // Saradomin base - leave waiting room.
			case 4390: // Zamorak base - leave waiting room.
				player.getCastleWarsTeam().leaveWaitingRoom(player);
				break;

			case 4458:
				final Item item = GameConstants.BANDAGES;
				player.getActionSender().addItem(item.getId(), item.getCount());
				break;
			case 4406: // Saradomin exit portal
			case 4407: // Zamorak exit portal
				break;
			case 4471: // Saradomin base - trapdoor to base
			{
				if (player.getCastleWarsTeam() != saradomin)
					return;
				final int weaponId = player.playerEquipment[Equipment.SLOT_WEAPON];
				if (weaponId == saradomin.flag.getId()
						|| weaponId == zamorak.flag.getId())
					return;
				player.teleport(new Location(2429, 3075, 1));
			}
				break;
			case 4472: // Zamorak base - trapdoor to base
			{
				if (player.getCastleWarsTeam() != zamorak)
					return;
				final int weaponId = player.playerEquipment[Equipment.SLOT_WEAPON];
				if (weaponId == saradomin.flag.getId()
						|| weaponId == zamorak.flag.getId())
					return;
				player.teleport(new Location(2370, 3132, 1));
			}
				break;
			case 1747: // Saradomin base - ladder going up
				switch (player.getCoordinates().getZ()) {
				case 0:
					player.teleport(new Location(2421, 3074, 1));
					break;
				}
				break;
			case 4911: // Saradomin base - ladder going down
				switch (player.getCoordinates().getZ()) {
				case 1:
					player.teleport(new Location(2421, 3074, 0));
					break;
				}
				break;
			case 6280: // Saradomin base - upstairs ladder from base.
				player.teleport(new Location(2429, 3075, 2));
				break;
			case 6281: // Zamorak base - upstairs ladder from base.
				player.teleport(new Location(2370, 3132, 2));
				break;
			case 4469: // Saradomin base - energy barriers
			{
				if (player.getCastleWarsTeam() != saradomin)
					return;
				final int weaponId = player.playerEquipment[Equipment.SLOT_WEAPON];
				if (weaponId == saradomin.getFlag().getId()
						|| weaponId == zamorak.getFlag().getId())
					return;
				if (player.getCoordinates().getX() == 2422
						&& player.getCoordinates().getY() == 3076)
					player.walkTo(player.getCoordinates().getX() + 1, player
							.getCoordinates().getY(), false);
				else if (player.getCoordinates().getX() == 2423
						&& player.getCoordinates().getY() == 3076)
					player.walkTo(player.getCoordinates().getX() - 1, player
							.getCoordinates().getY(), false);
				else if (player.getCoordinates().getX() == 2426
						&& player.getCoordinates().getY() == 3080)
					player.walkTo(player.getCoordinates().getX(), player
							.getCoordinates().getY() + 1, false);
				else if (player.getCoordinates().getX() == 2426
						&& player.getCoordinates().getY() == 3081)
					player.walkTo(player.getCoordinates().getX(), player
							.getCoordinates().getY() - 1, false);
			}
				break;
			case 4470: // Zamorak base - energy barriers
			{
				if (player.getCastleWarsTeam() != zamorak)
					return;
				final int weaponId = player.playerEquipment[Equipment.SLOT_WEAPON];
				if (weaponId == saradomin.getFlag().getId()
						|| weaponId == zamorak.getFlag().getId())
					return;
				if (player.getCoordinates().getX() == 2376
						&& player.getCoordinates().getY() == 3131)
					player.walkTo(player.getCoordinates().getX() + 1, player
							.getCoordinates().getY(), false);
				else if (player.getCoordinates().getX() == 2377
						&& player.getCoordinates().getY() == 3131)
					player.walkTo(player.getCoordinates().getX() - 1, player
							.getCoordinates().getY(), false);
				else if (player.getCoordinates().getX() == 2373
						&& player.getCoordinates().getY() == 3127)
					player.walkTo(player.getCoordinates().getX(), player
							.getCoordinates().getY() - 1, false);
				else if (player.getCoordinates().getX() == 2373
						&& player.getCoordinates().getY() == 3126)
					player.walkTo(player.getCoordinates().getX(), player
							.getCoordinates().getY() + 1, false);
			}
				break;
			case 4415: // Both bases - going downstairs.
				if (Areas.isAtSaradominBase(player.getCoordinates())) {
					if (System.currentTimeMillis()
							- player.lastCastleWarsMovement < 1000)
						return;
					switch (player.getCoordinates().getZ()) {
					case 1:
						player.teleport(new Location(2419, 3077, 0));
						player.lastCastleWarsMovement = System
								.currentTimeMillis();
						break;
					case 2:
						player.teleport(new Location(2427, 3081, 1));
						player.lastCastleWarsMovement = System
								.currentTimeMillis();
						break;
					case 3:
						player.teleport(new Location(2425, 3077, 2));
						player.lastCastleWarsMovement = System
								.currentTimeMillis();
						break;
					}
				} else
					switch (player.getCoordinates().getZ()) {
					case 1:
						player.teleport(new Location(2380, 3130, 0));
						player.lastCastleWarsMovement = System
								.currentTimeMillis();
						break;
					case 2:
						player.teleport(new Location(2372, 3126, 1));
						player.lastCastleWarsMovement = System
								.currentTimeMillis();
						break;
					case 3:
						player.teleport(new Location(2374, 3130, 2));
						player.lastCastleWarsMovement = System
								.currentTimeMillis();
						break;
					}
				break;
			case 4417: // Saradomin base - going up stairs.
				if (System.currentTimeMillis() - player.lastCastleWarsMovement < 1000)
					return;
				switch (player.getCoordinates().getZ()) {
				case 0:
					player.teleport(new Location(2420, 3080, 1));
					player.lastCastleWarsMovement = System.currentTimeMillis();
					break;
				case 1:
					player.teleport(new Location(2430, 3080, 2));
					player.lastCastleWarsMovement = System.currentTimeMillis();
					break;
				case 2:
					player.teleport(new Location(2426, 3074, 3));
					player.lastCastleWarsMovement = System.currentTimeMillis();
					break;
				}
				break;
			case 4418: // Zamorak base - going up stairs.
				if (System.currentTimeMillis() - player.lastCastleWarsMovement < 1000)
					return;
				switch (player.getCoordinates().getZ()) {
				case 0:
					player.teleport(new Location(2379, 3127, 1));
					player.lastCastleWarsMovement = System.currentTimeMillis();
					break;
				case 1:
					player.teleport(new Location(2369, 3127, 2));
					player.lastCastleWarsMovement = System.currentTimeMillis();
					break;
				case 2:
					player.teleport(new Location(2373, 3133, 3));
					player.lastCastleWarsMovement = System.currentTimeMillis();
					break;
				}
				break;
			case 4419: // Saradomin base - outside stairs.
				if (player.getCoordinates().getX() == 2417
						&& player.getCoordinates().getY() == 3077)
					player.teleport(new Location(2416, 3074, 0));
				else if (player.getCoordinates().getX() == 2416
						&& player.getCoordinates().getY() == 3074)
					player.teleport(new Location(2417, 3077, 0));
				break;
			case 4420: // Zamorak base - outside stairs.
				if (player.getCoordinates().getX() == 2382
						&& player.getCoordinates().getY() == 3130)
					player.teleport(new Location(2383, 3133, 0));
				else if (player.getCoordinates().getX() == 2383
						&& player.getCoordinates().getY() == 3133)
					player.teleport(new Location(2382, 3130, 0));
				break;
			case 4465: // Saradomin base - open door.
			{
				if (player.getCastleWarsTeam() != saradomin)
					return;
				saradomin.sideDoorUnlocked = !saradomin.sideDoorUnlocked;
				ObjectManager.submitPublicObject(new GameObject(new Location(
						objectX, objectY, 0), objectId, -1, -1,
						zamorak.sideDoorUnlocked ? -3 : -0, 0, player));
			}
				break;
			case 4467: // Zamorak base - unlock door.
			case 4468: // Zamorak base - lock door.
			{
				if (player.getCastleWarsTeam() != zamorak)
					return;
				zamorak.sideDoorUnlocked = !zamorak.sideDoorUnlocked;
				ObjectManager.submitPublicObject(new GameObject(new Location(
						objectX, objectY, 0), zamorak.sideDoorUnlocked ? 4468
						: 4467, -1, -1, zamorak.sideDoorUnlocked ? -1 : -2, 0,
						player));
			}
				break;
			case 4423: // Saradomin base - open door 1
			case 4424: // Saradomin base - open door 2
			case 4425: // Saradomin base - close door 1
			case 4426: // Saradomin base - close door 1
			{
				if (player.getCastleWarsTeam() != saradomin)
					return;
				saradomin.mainDoorUnlocked = !saradomin.mainDoorUnlocked;
				ObjectManager.submitPublicObject(new GameObject(new Location(
						2426, 3088, 0), saradomin.mainDoorUnlocked ? 4423
						: 4425, -1, -1, saradomin.mainDoorUnlocked ? -1 : 0, 0,
						player));
				ObjectManager.submitPublicObject(new GameObject(new Location(
						2427, 3088, 0), saradomin.mainDoorUnlocked ? 4424
						: 4426, -1, -1, saradomin.mainDoorUnlocked ? -1 : -2,
						0, player));

			}
				break;
			case 4427: // Zamorak base - open door 1
			case 4428: // Zamorak base - open door 2
			case 4429: // Zamorak base - close door 1
			case 4430: // Zamorak base - close door 2
			{
				if (player.getCastleWarsTeam() != zamorak)
					return;
				zamorak.mainDoorUnlocked = !zamorak.mainDoorUnlocked;

				final GameObject object = new GameObject(
						zamorak.mainDoorUnlocked ? 4427 : 4429, 2373, 3119, 0,
						zamorak.mainDoorUnlocked ? GameObject.Face.SOUTH
								: GameObject.Face.EAST, 0);
				ObjectManager.submitPublicObject(object);
				final GameObject object2 = new GameObject(
						zamorak.mainDoorUnlocked ? 4428 : 4430, 2372, 3119, 0,
						zamorak.mainDoorUnlocked ? GameObject.Face.SOUTH
								: GameObject.Face.WEST, 0);
				ObjectManager.submitPublicObject(object2);
			}
				break;
			case 4902: // Saradomin base - take flag
			{
				handleFlagPost(player, saradomin, zamorak);
			}
				break;
			case 4377: // Saradomin base - empty flag.
			{
				handleEmptyFlagPost(player, saradomin, zamorak);
			}
				break;
			case 4903: // Zamorak base - take flag
			{
				handleFlagPost(player, zamorak, saradomin);
			}
				break;
			case 4378: // Zamorak base - empty flag.
			{
				handleEmptyFlagPost(player, zamorak, saradomin);
			}
				break;
			case 4900: // Saradomin flag ground object.
			{
				handleDroppedFlag(player, saradomin, zamorak);
			}
				break;
			case 4901: // Zamorak flag ground object.
			{
				handleDroppedFlag(player, zamorak, saradomin);
			}
				break;
			}
		}
	}

	public void handleSteppingStone(Player player, Location playerPosition,
			Location objectPosition) {
		final int i = objectPosition.getX() - playerPosition.getX();
		final int j = objectPosition.getY() - playerPosition.getY();
		if (Misc.getDistance(playerPosition, objectPosition) <= 1)
			player.walkTo(player.getCoordinates().getX() + i, player
					.getCoordinates().getY() + j, false);
	}

	public void init() {
		// Elapsed game and waiting time.
		final long elapsedWaitTime = System.currentTimeMillis() - waitStartedAt;
		final long elaspedGameTime = System.currentTimeMillis() - gameStartedAt;
		final int x = (int) (GameConstants.GAME_DURATION - elaspedGameTime) / 1000;

		// Calculate the time until the next game
		final int secondsToBegin = (int) (GameConstants.WAIT_DURATION - elapsedWaitTime) / 1000;
		int minutesToGame = secondsToBegin / 60;
		if (minutesToGame <= 0)
			minutesToGame = 0;

		// Add in all the saradomin players..
		for (final Player player : saradomin.pendingParticipants)
			lobby.add(player);

		// Add in all the zamorak players...
		for (final Player player : zamorak.pendingParticipants)
			lobby.add(player);

		if (lobby.size() < 2 && !gameInProgress) {
			waitStartedAt = System.currentTimeMillis();
			return;
		}

		if (!Constants.MINIGAME || !Constants.CWARS)
			return;

		// Update game interface!
		for (final Player player : lobby) {
			player.getActionSender().sendWalkableInterface(11479);
			player.getActionSender().sendConfig(380,
					gameInProgress ? minutesToGame + x / 60 : minutesToGame);
		}

		lobby.clear();

		if (!gameInProgress) {

			if (elapsedWaitTime >= x + GameConstants.WAIT_DURATION) {

				if (!Constants.CWARS)
					return;

				// Move player from the waiting room into the game.
				for (final Player player : saradomin.pendingParticipants)
					saradomin.addToTeam(player);
				saradomin.pendingParticipants.clear();

				// Move player from the waiting room into the game.
				for (final Player player : zamorak.pendingParticipants)
					zamorak.addToTeam(player);
				zamorak.pendingParticipants.clear();

				// A new game has been started.
				gameStartedAt = waitStartedAt = System.currentTimeMillis();
				gameInProgress = true;
			}

		} else if (elaspedGameTime <= GameConstants.GAME_DURATION) {

			// The time until the game ends.
			final int secondsToEnd = (int) (GameConstants.GAME_DURATION - elaspedGameTime) / 1000;
			final int minutesToEnd = secondsToEnd / 60;

			// Add in all the saradomin players..
			for (final Player player : saradomin.participants)
				participants.add(player);

			// Add in all the zamorak players..
			for (final Player player : zamorak.participants)
				participants.add(player);

			// Update the game interface.
			for (final Player player : participants) {

				// Send the game interface.
				player.getActionSender().sendWalkableInterface(11344);

				// Display how much game time is remaining.
				player.getActionSender().sendConfig(380, minutesToEnd);

				// Update game interface.
				updateGameInterface(player);

			}

			// We are finished with the list, clear it for the next
			// cycle!
			participants.clear();

		} else {
			saradomin.reset();
			zamorak.reset();
			gameInProgress = false;
		}

	}

	public void loadObjects(Player player) {
		player.getActionSender().sendObject(-1, 2409, 9503, 0, -1, 10);
		player.getActionSender().sendObject(-1, 2401, 9494, 0, -1, 10);
		player.getActionSender().sendObject(-1, 2391, 9501, 0, -1, 10);
		player.getActionSender().sendObject(-1, 2400, 9512, 0, -1, 10);
		player.getActionSender().sendObject(-1, 3158, 3951, 0, -1, 10);
	}

	public void onDeath(Player player) {
		final int weaponId = player.playerEquipment[Equipment.SLOT_WEAPON];
		if (weaponId != -1)
			if (weaponId == 4037 || weaponId == 4039)
				dropFlag(player, weaponId);
		player.teleport(player.getCastleWarsTeam().getBase());
		player.setHeightLevel(1);
	}

	public void onDisconnect(final Player player) {

		// Get our weapon.
		final int weaponId = player.playerEquipment[Equipment.SLOT_WEAPON];

		// ..if its a flag drop it!
		if (weaponId == saradomin.getFlag().getId()
				|| weaponId == zamorak.getFlag().getId())
			dropFlag(player, weaponId);

		// Remove the player from the team.
		getTeamInstance(player.getCastleWarsTeam()).removeFromTeam(player);

		// Finally teleport the player out of the game.
		player.setCoordinates(GameConstants.CASTLE_WAR_BANK);

	}

	public void removeGameItems(Player player) {

		// Ids of all game equipment.
		final int[] equipment = new int[] { 4041, 4042, 4044, 4037, 4039 };

		// Remove the game equipment.
		for (int i = 0; i < player.playerEquipment.length; i++) {
			final int id = player.playerEquipment[i];
			for (final int element : equipment)
				if (id == element)
					player.getEquipment().deleteEquipment(i);
		}

		// Remove inventory items
		for (final int id : player.playerItems)
			for (final int element : equipment)
				if (id == element)
					player.getActionAssistant().deleteItem(id, 1);

		player.appearanceUpdateRequired = true;
	}

	/**
	 * @author Usaclub
	 */
	@SuppressWarnings("unused")
	public void updateGameInterface(Player player) {
		/**
		 * The configuration to send.
		 */
		int config = 0;
		/**
		 * Rock 1 statuses.
		 */
		final boolean saraRock1 = true;
		final boolean zammyRock1 = true;
		final boolean saraRock2 = true;
		final boolean zammyRock2 = true;
		/**
		 * Catapult statuses.
		 */
		final boolean zammyCatapult = true;
		final boolean saraCatapult = true;
		/**
		 * Flag statuses.
		 */
		final boolean zamorakFlagDropped = zamorak.flagStatus == GameConstants.FLAG_STATUS_DROPPED;
		final boolean saradominFlagDropped = saradomin.flagStatus == GameConstants.FLAG_STATUS_DROPPED;
		final boolean zamorakFlagTaken = zamorak.flagStatus == GameConstants.FLAG_STATUS_TAKEN;
		final boolean saradominFlagTaken = saradomin.flagStatus == GameConstants.FLAG_STATUS_TAKEN;
		/**
		 * Update the zamorak interface.
		 */
		config = zamorak.doorHealth;
		if (player.getCastleWarsTeam() == zamorak ? zamorak.sideDoorUnlocked
				: saradomin.sideDoorUnlocked)
			config += 128;
		if (!saraRock1)
			config += 256;
		if (!saraRock2)
			config += 512;
		if (player.getCastleWarsTeam() == zamorak ? !zammyCatapult
				: !saraCatapult)
			config += 1024;
		if (player.getCastleWarsTeam() == zamorak ? zamorakFlagDropped
				: saradominFlagDropped)
			config += 2097152 * 2;
		else if (player.getCastleWarsTeam() == zamorak ? zamorakFlagTaken
				: saradominFlagTaken)
			config += 2097152 * 1;
		config += 16777216 * (player.getCastleWarsTeam() == zamorak ? zamorak.score
				: saradomin.score);
		player.getActionSender().sendConfig(
				player.getCastleWarsTeam() == zamorak ? 377 : 378, config);
		/**
		 * Update the saradomin interface.
		 */
		config = saradomin.doorHealth;
		if (player.getCastleWarsTeam() == zamorak ? zamorak.sideDoorUnlocked
				: saradomin.sideDoorUnlocked)
			config += 128;
		if (!zammyRock1)
			config += 256;
		if (!zammyRock2)
			config += 512;
		if (!saraCatapult)
			config += 1024;
		if (player.getCastleWarsTeam() == zamorak ? saradominFlagDropped
				: zamorakFlagDropped)
			config += 2097152 * 2;
		else if (player.getCastleWarsTeam() == zamorak ? saradominFlagTaken
				: zamorakFlagTaken)
			config += 2097152 * 1;
		config += 16777216 * (player.getCastleWarsTeam() == zamorak ? saradomin.score
				: zamorak.score);
		player.getActionSender().sendConfig(
				player.getCastleWarsTeam() == zamorak ? 378 : 377, config);
	}

}