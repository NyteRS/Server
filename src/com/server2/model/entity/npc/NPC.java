package com.server2.model.entity.npc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.server2.InstanceDistributor;
import com.server2.Settings;
import com.server2.content.Achievements;
import com.server2.content.NPCStats;
import com.server2.content.minigames.WarriorsGuild;
import com.server2.content.minigames.pc.PestControl;
import com.server2.content.misc.mobility.NPCTeleportHandler;
import com.server2.content.misc.mobility.TeleportationHandler;
import com.server2.content.misc.pets.PetHandler;
import com.server2.content.quests.HorrorFromTheDeep;
import com.server2.content.randoms.Random;
import com.server2.content.skills.dungeoneering.Dungeoneering;
import com.server2.content.skills.hunter.Hunter;
import com.server2.content.skills.summoning.Summoning;
import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.engine.cycle.Tickable;
import com.server2.engine.task.impl.NPCResetTask;
import com.server2.engine.task.impl.NPCTickTask;
import com.server2.model.PathFinder;
import com.server2.model.combat.CombatEngine;
import com.server2.model.combat.HitExecutor;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.Location;
import com.server2.model.entity.npc.impl.CorporalBeast;
import com.server2.model.entity.npc.impl.Nex;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Areas;
import com.server2.util.Misc;
import com.server2.util.PlayerSaving;
import com.server2.world.NPCManager;
import com.server2.world.PlayerManager;
import com.server2.world.World;
import com.server2.world.map.tile.FollowEngine;
import com.server2.world.map.tile.TileManager;

/**
 * Represents a single NPC
 * 
 * @author Graham & Lukas & Renual
 */
public class NPC extends Entity {

	public static enum HunterState {
		WAITING, INTERESTED
	}

	private final NPCTickTask tickTask = new NPCTickTask(this);
	private final NPCResetTask resetTask = new NPCResetTask(this);
	public NPCTeleportHandler npcTele = new NPCTeleportHandler(this);

	public int c = 0;

	public int jungleS, iceS, desertS;

	public boolean walkingHome;

	public int lastMovementX = 0;

	public int lastMovementY = 0;
	public int mask80var1 = 0;

	public int mask80var2 = 0;

	public boolean mask80update = false;

	public long lastAttack = System.currentTimeMillis();

	/**
	 * The random walk factor.
	 */
	public static final double RANDOM_WALK_FACTOR = 0.2;

	private boolean inCombat;

	/**
	 * NPC slot.
	 */
	public int npcSlot;

	// public Player hftder; // TODO: change
	public int hftder = -1;
	/**
	 * NPC definition.
	 */
	public NPCDefinition definition;
	/**
	 * Walk boundaries.
	 */
	private int x1, x2, y1, y2;
	/**
	 * HP and max hp.
	 */
	public int hp;
	public int maxHP;
	public int healed;

	public CombatType lastCombatType = CombatType.MAGIC;

	public int attacktimer = 5;

	public boolean summoned = false;
	public boolean summoned1 = false;
	public boolean summoned2 = false;

	public boolean summoned3 = false;

	public NPC summonedn1;

	public NPC summonedn2;
	public NPC summonedn3;
	/**
	 * Update flags.
	 */
	public boolean updateRequired = false, animUpdateRequired = false,
			hitUpdateRequired = false, dirUpdateRequired = false;
	/**
	 * Text update data
	 */
	public boolean textUpdateRequired = false;
	public String textUpdate;
	// perhaps eclipse fucked up,
	/**
	 * Integeral update data.
	 */
	public int animNumber;
	public int direction;
	public int direction2;
	public int hit;
	public int hitType;
	public int hitType2;
	public int lastIndex;
	/**
	 * Walking.
	 */
	private boolean isWalking, wasWalking;;
	/**
	 * Raw direction.
	 */
	public int rawDirection = -1;
	/**
	 * Turn player to direction
	 */
	public int focusPointX, focusPointY;
	public boolean turnUpdateRequired;
	// public Player fightCaver;
	public int fightCaver;
	public int[] damageFromPlayers = new int[50];
	/**
	 * List of attackers.
	 */
	private Map<String, Integer> attackers = new HashMap<String, Integer>();

	/**
	 * List of attackers.
	 */
	private Map<String, Integer> previousAttackers = null;

	/**
	 * Primary attacker.
	 */
	// private Player attacker = null;
	private int attacker = -1;
	/**
	 * We're dead.
	 */
	private boolean isDead = false;
	/**
	 * Is dead waiting...
	 */
	public boolean isDeadWaiting = false;
	/**
	 * Timer lol. One of the only ones I use! :O
	 */
	public int isDeadTimer = -1;
	/**
	 * Are we hidden?
	 */
	public boolean isHidden = false;
	/**
	 * Is dead teleporting.
	 */
	public boolean isDeadTeleporting = false;
	/**
	 * Where to respawn.
	 */
	public int spawnAbsX, spawnAbsY;
	public CombatType type;
	public CombatType type2;
	public static WarriorsGuild wg = new WarriorsGuild();

	public static boolean isBoss(int npcType) {
		if (isPestNpc(npcType))
			return true;
		switch (npcType) {
		case 5001:
		case 5002:
		case 4998:
		case 4999:
		case 50:
		case 2881:
		case 2882:
		case 2883:
		case 8349:
		case 8133:
		case 1351:
			return true;
		}
		return false;
	}

	public static boolean isPestNpc(int npcType) {
		switch (npcType) {
		case 3771:
		case 3751:
		case 3776:
		case 3740:
		case 3939:
		case 3741:
		}

		return false;
	}

	public static void newLoginNPC(int id, int x, int y, int z) {
		try {
			final int slot = InstanceDistributor.getNPCManager().freeSlot();
			final NPCDefinition def = InstanceDistributor.getNPCManager().npcDefinitions
					.get(id);
			if (def == null)
				return;
			final NPC npc = new NPC(slot, def, x, y, z);
			npc.setX1(npc.getAbsX());
			npc.setY1(npc.getAbsY());
			npc.setX2(npc.getAbsX());
			npc.setY2(npc.getAbsY());
			InstanceDistributor.getNPCManager().npcMap.put(npc.getNpcId(), npc);
		} catch (final Exception e) {
		}
	}

	/**
	 * 
	 * @param id
	 * @param x
	 * @param y
	 * @param z
	 * @param theOwner
	 *            spawns a npc which will attack TheOwner
	 */
	public static void newNPC(final int id, int x, int y, int z,
			final Entity theOwner) {
		try {
			final int slot = InstanceDistributor.getNPCManager().freeSlot();
			final NPCDefinition def = InstanceDistributor.getNPCManager().npcDefinitions
					.get(id);
			if (def == null)
				return;
			final NPC npc = new NPC(slot, def, x, y, z);
			npc.setX1(npc.getAbsX());
			npc.setY1(npc.getAbsY());
			npc.setX2(npc.getAbsX());
			npc.setY2(npc.getAbsY());
			npc.setWalking(true);
			if (id == 1351)
				npc.hftder = ((Player) theOwner).getIndex();
			if (id != 4278 && id != 4279 && id != 4280 && id != 4281
					&& id != 4282 && id != 4283 && id != 4284)
				if (theOwner != npc || theOwner != null) {
					System.out
							.println("attacker is himself or is null when spawning");
					npc.setTarget(theOwner);
					npc.setOwner(theOwner);
				}
			if (id > 2024 && id < 2031) {
				((Player) theOwner).getActionSender()
						.sendLocationPointerPlayer(1, npc.getNpcId());
				npc.textUpdate = "You dare disturb my rest!";
				npc.textUpdateRequired = true;
			}
			if (id > 4278 && id < 4285) {
				AnimationProcessor.addNewRequest(npc, 4410, 0);
				npc.textUpdate = "I'M ALIVE!";
				npc.textUpdateRequired = true;
				((Player) theOwner).getActionSender()
						.sendLocationPointerPlayer(1, npc.getNpcId());
				World.getWorld().submit(new Tickable(3) {
					@Override
					public void execute() {
						if (theOwner != npc || theOwner != null) {
							System.out
									.println("attacker is himself or is null when spawning");
							npc.setOwner(theOwner);
							npc.setTarget(theOwner);
						}
						stop();
					}
				});
			}
			((Player) theOwner).tempNPCAllocated = npc;
			InstanceDistributor.getNPCManager().npcMap.put(npc.getNpcId(), npc);
		} catch (final Exception e) {
		}
	}

	public static void newNPCWithTempOwner(final int id, final int x,
			final int y, final int z, final Entity theOwner) {
		try {
			final int slot = InstanceDistributor.getNPCManager().freeSlot();
			final NPCDefinition def = InstanceDistributor.getNPCManager().npcDefinitions
					.get(id);
			if (def == null)
				return;
			final NPC npc = new NPC(slot, def, x, y, z);
			npc.setX1(npc.getAbsX());
			npc.setY1(npc.getAbsY());
			npc.setX2(npc.getAbsX());
			npc.setY2(npc.getAbsY());
			npc.setWalking(true);
			npc.setTarget(theOwner);
			if (id == 2745) {
				Player cave = null;

				if (npc.fightCaver != -1)
					cave = PlayerManager.getSingleton().getPlayers()[npc.fightCaver];

				if (cave == null || npc.fightCaver == -1)
					npc.fightCaver = ((Player) theOwner).getIndex();
			}
			if (id > 2024 && id < 2031) {
				((Player) theOwner).getActionSender()
						.sendLocationPointerPlayer(1, npc.getNpcId());
				npc.textUpdate = "You dare disturb my rest!";
				npc.textUpdateRequired = true;
			}
			if (id == 1351)
				npc.hftder = ((Player) theOwner).getIndex();
			if (id > 4278 && id < 4285) {
				AnimationProcessor.addNewRequest(npc, 4410, 0);
				npc.textUpdate = "I'M ALIVE!";
				npc.textUpdateRequired = true;
				npc.setFollowing(theOwner);
				npc.setTarget(theOwner);
				npc.setInCombatWith(theOwner);
				npc.attacker = ((Player) theOwner).getIndex();

				((Player) theOwner).getActionSender()
						.sendLocationPointerPlayer(1, npc.getNpcId());
			}
			if (theOwner instanceof Player)
				((Player) theOwner).tempNPCAllocated = npc;
			boolean b = true;
			while (b) {
				if (InstanceDistributor.getNPCManager().npcMap.put(
						npc.getNpcId(), npc) == null)
					b = false;
				if (InstanceDistributor.getNPCManager().npcMap
						.containsValue(npc))
					b = false;
				else
					b = true;
			}
			final NPC baws = npc;
			World.getWorld().submit(new Tickable(1) {
				@Override
				public void execute() {
					if (!InstanceDistributor.getNPCManager().npcMap
							.containsValue(baws))
						newNPCWithTempOwner(id, x, y, z, theOwner);
					stop();
				}
			});

		} catch (final Exception e) {
			// e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param id
	 * @param x
	 * @param y
	 * @param z
	 */
	public static void newTempNPC(int id, int x, int y, int z) {
		try {
			final int slot = InstanceDistributor.getNPCManager().freeSlot();
			final NPCDefinition def = InstanceDistributor.getNPCManager().npcDefinitions
					.get(id);
			if (def == null)
				return;
			final NPC npc = new NPC(slot, def, x, y, z);
			npc.setX1(npc.getAbsX());
			npc.setY1(npc.getAbsY());
			npc.setX2(npc.getAbsX());
			npc.setY2(npc.getAbsY());
			npc.setWalking(false);
			npc.setWalk(x, y, true);
			InstanceDistributor.getNPCManager().npcMap.put(npc.getNpcId(), npc);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean nexRoom(int x, int y) {
		if (x >= 2451 && x <= 2478 && y >= 4766 && y <= 4792)
			return true;
		return false;
	}

	public static boolean registerNPC(NPC npc) {
		if (npc == null)
			return false;
		final int slot = InstanceDistributor.getNPCManager().freeSlot();
		InstanceDistributor.getNPCManager().npcMap.put(slot, npc);
		npc.setNpcId(slot);
		return true;
	}

	public static void removeDungeoneeringNPC(Entity entity) {
		if (entity != null) {
			final HashMap<Integer, NPC> newMap = new HashMap<Integer, NPC>(
					InstanceDistributor.getNPCManager().getNPCMap());
			for (final Entry<Integer, NPC> entry : newMap.entrySet()) {
				final NPC npc = entry.getValue();
				if (npc.getHeightLevel() == entity.getHeightLevel()
						&& npc.getHeightLevel() != 0) {
					npc.isHidden = true;
					npc.isDead = true;
					removeNPC(npc, 7);
				}
			}
		}
	}

	public static void removeDungeoneeringNPC2(Entity entity) {
		if (entity != null) {
			final HashMap<Integer, NPC> newMap = new HashMap<Integer, NPC>(
					InstanceDistributor.getNPCManager().getNPCMap());
			for (final Entry<Integer, NPC> entry : newMap.entrySet()) {
				final NPC npc = entry.getValue();
				if (npc == null)
					return;
				if (npc.getHeightLevel() != 0)
					if (npc.getHeightLevel() == entity.getIndex() * 4
							|| npc.getHeightLevel() == entity.getIndex() * 4 - 1) {
						npc.isHidden = true;
						npc.isDead = true;
						removeNPC(npc, 8);
					}
			}
		}
	}

	public static void removeGhouls(Entity entity) {
		if (entity != null) {
			final HashMap<Integer, NPC> newMap = new HashMap<Integer, NPC>(
					InstanceDistributor.getNPCManager().getNPCMap());
			for (final Entry<Integer, NPC> entry : newMap.entrySet()) {
				final NPC npc = entry.getValue();
				if (npc.getHeightLevel() == entity.getHeightLevel()
						&& npc.getHeightLevel() != 0) {
					npc.isHidden = true;
					npc.isDead = true;
					removeNPC(npc, 2512);
				}
			}
		}
	}

	public static void removeNex(Entity entity, int id) {
		entity.setTarget(null);
		entity.setFollowing(null);
		entity.setInCombatWith(null);
		TeleportationHandler.addNewRequest(entity, 0, 0, 1, 0);
		if (id == 5001)
			Nex.removed1 = true;
		if (id == 5002)
			Nex.removed2 = true;
		if (id == 5003)
			Nex.removed3 = true;
		if (id == 4999)
			Nex.removed4 = true;

	}

	public static void removeNPC(NPC npc, int a) {
		if (npc == null)
			return;

		npc.setVisible(false);
		npc.isHidden = true;
		npc.isDead = true;
		npc.setHidden(true);

		InstanceDistributor.getNPCManager().npcMap.remove(npc.getNpcId());
		npc = null;

	}

	public static void spawnDarkCore(int id, int x, int y, Entity victim) {
		try {
			NPC.spawnPestSideNPC(id, x, y, 1, victim);
		} catch (final Exception e) {
		}
	}

	/**
	 * 
	 * @param id
	 * @param x
	 * @param y
	 * @param z
	 * @param theOwner
	 *            spawns a npc which will attack TheOwner
	 */
	public static void spawnDungeoneeringNPC(int id, int x, int y, int z,
			Player client) {
		try {
			final NPCDefinition def = InstanceDistributor.getNPCManager().npcDefinitions
					.get(id);
			if (def == null)
				return;
			final int slot = InstanceDistributor.getNPCManager().freeSlot();
			final NPC npc = new NPC(slot, def, x, y, z);
			npc.setX1(npc.getAbsX());
			npc.setY1(npc.getAbsY());
			npc.setX2(npc.getAbsX());
			npc.setY2(npc.getAbsY());
			npc.setWalking(true);
			InstanceDistributor.getNPCManager().npcMap.put(npc.getNpcId(), npc);

		} catch (final Exception e) {
		}
	}

	public static void spawnNexNPC(int id, int x, int y, int z, int health) {
		try {
			final int slot = InstanceDistributor.getNPCManager().freeSlot();
			final NPCDefinition def = InstanceDistributor.getNPCManager().npcDefinitions
					.get(id);
			final NPC npc = new NPC(slot, def, x, y, z);
			npc.setHP(health);
			npc.setX1(npc.getAbsX());
			npc.setY1(npc.getAbsY());
			npc.setX2(npc.getAbsX());
			npc.setY2(npc.getAbsY());
			InstanceDistributor.getNPCManager().npcMap.put(npc.getNpcId(), npc);
		} catch (final Exception e) {
		}
	}

	public static void spawnPestNPC(int id, int x, int y, int z) {
		try {
			final int slot = InstanceDistributor.getNPCManager().freeSlot();
			final NPCDefinition def = InstanceDistributor.getNPCManager().npcDefinitions
					.get(id);
			final NPC npc = new NPC(slot, def, x, y, z);
			npc.setX1(npc.getAbsX());
			npc.setY1(npc.getAbsY());
			npc.setX2(npc.getAbsX());
			npc.setY2(npc.getAbsY());
			npc.setWalking(false);
			npc.setWalk(x, y, true);
			InstanceDistributor.getNPCManager().npcMap.put(npc.getNpcId(), npc);

		} catch (final Exception e) {
		}
	}

	public static void spawnPestSideNPC(int id, int x, int y, int z,
			Entity target23) {
		try {
			final int slot = InstanceDistributor.getNPCManager().freeSlot();
			final NPCDefinition def = InstanceDistributor.getNPCManager().npcDefinitions
					.get(id);
			if (def == null) {
			}
			final NPC npc = new NPC(slot, def, x, y, z);
			npc.setX1(npc.getAbsX());
			npc.setY1(npc.getAbsY());
			npc.setX2(npc.getAbsX());
			npc.setY2(npc.getAbsY());
			npc.setTarget(target23);
			InstanceDistributor.getNPCManager().npcMap.put(npc.getNpcId(), npc);
		} catch (final Exception e) {
		}
	}

	public static void spawnTzhaarCaveNPC(int id, int x, int y, int z,
			Player client) {
		try {
			final int slot = InstanceDistributor.getNPCManager().freeSlot();
			final NPCDefinition def = InstanceDistributor.getNPCManager().npcDefinitions
					.get(id);
			if (def == null) {
				System.out.println("Excuse me mr renual, npc Id : " + id
						+ " is not recognized.");
				return;
			}
			final NPC npc = new NPC(slot, def, x, y, z);
			npc.setX1(npc.getAbsX());
			npc.setY1(npc.getAbsY());
			npc.setX2(npc.getAbsX());
			npc.setY2(npc.getAbsY());
			npc.setWalking(true);
			npc.setTarget(client);
			InstanceDistributor.getNPCManager().npcMap.put(npc.getNpcId(), npc);

		} catch (final Exception e) {
		}
	}

	public Location spawnz = new Location(spawnAbsX, spawnAbsY,
			getHeightLevel());

	public boolean hitUpdateRequired2;

	public int hit2;

	private int deathDelay = 0;

	public long lastForceChat;

	/**
	 * An idea to create more than 1 step walking.
	 */
	private int stepsAllocated = 0;

	private final int[] walkingPaths = new int[2];

	private boolean pathCreated = false;

	/**
	 * Processing.
	 */
	public boolean shit = true;

	public NPCDefinition def;

	private int minoanim;

	private int minogfx;

	public boolean done;
	public boolean preventRest = false;
	private int forceChatValue;

	public boolean pickingUpItem;

	public static boolean hasToAttack;

	private HunterState hunterState = HunterState.WAITING;

	/**
	 * Creates the npc.
	 * 
	 * @param npcSlot
	 *            NPC slot.
	 * @param definition
	 *            NPC definition.
	 * @param absX
	 * @param absY
	 * @param heightLevel
	 */
	public NPC(int npcSlot, NPCDefinition definition, int absX, int absY,
			int heightLevel) {
		this.npcSlot = npcSlot;
		this.definition = definition;
		maxHP = definition.getHealth();
		hp = definition.getHealth();
		setAbsX(absX);
		setAbsY(absY);
		spawnAbsX = absX;
		spawnAbsY = absY;
		setHeightLevel(heightLevel);
		lastIndex = npcSlot;
		setPosition(new Location(absX, absY, heightLevel));
	}

	/**
	 * Creates an npc with no index. The index must be manually set!
	 * 
	 * @param definition
	 * @param location
	 */
	public NPC(NPCDefinition definition, Location location) {
		this(-1, definition, location.getX(), location.getY(), location.getZ());
	}

	/**
	 * Clears the update flags.
	 */
	public void clearUpdateFlags() {
		updateRequired = false;
		textUpdateRequired = false;
		animUpdateRequired = false;
		hitUpdateRequired2 = false;
		hitUpdateRequired = false;
		dirUpdateRequired = false;
		turnUpdateRequired = false;
		mask80update = false;
		if (rawDirection != -1) {
			rawDirection = -1;
			dirUpdateRequired = true;
			updateRequired = true;
		}
		hit = 0;
		direction = -1;
	}

	public void destruct() {
		setAttacker(null);
		inCombat(false);
	}

	/**
	 * Turns an NPC.
	 * 
	 * @param dir
	 */
	public void face(int dir) {
		rawDirection = dir;
		dirUpdateRequired = true;
		updateRequired = true;
	}

	/**
	 * Turns an npc to a Npc.
	 */
	public void faceNpc(int npcId) {
		rawDirection = npcId;
		dirUpdateRequired = true;
		updateRequired = true;
	}

	/**
	 * Turns an NPC to a player.
	 * 
	 * @param playerId
	 */
	public void faceTo(int playerId) {
		rawDirection = 32768 + playerId;
		dirUpdateRequired = true;
		updateRequired = true;
	}

	public void forceChat(String text) {
		textUpdate = text;
		textUpdateRequired = true;

	}

	/**
	 * @return the animNumber
	 */
	public int getAnimNumber() {
		return animNumber;
	}

	/**
	 * @return the attacker
	 */
	public Player getAttacker() {
		return PlayerManager.getSingleton().getPlayers()[attacker];
	}

	/**
	 * @return the attackers
	 */
	public Map<String, Integer> getAttackers() {
		return attackers;
	}

	public int getCloseRandomPlayer(int i, NPC npcs[]) {
		final ArrayList<Integer> players = new ArrayList<Integer>();
		for (int j = 0; j < Settings.getLong("sv_maxclients"); j++) {
			final Player player = PlayerManager.getSingleton().getPlayers()[j];
			if (player == null)
				continue;
			if (goodDistance(player.getAbsX(), player.getAbsY(),
					npcs[i].getAbsX(), npcs[i].getAbsY(), 2 + 1 + 0))
				if (player.getTarget() != null && player.getTarget() != null)
					if (player.heightLevel == npcs[i].heightLevel)
						players.add(player.getIndex());
		}
		if (players.size() > 0)
			return players.get(Misc.random(players.size() - 1));
		else
			return 0;
	}

	/**
	 * @return the definition
	 */
	public NPCDefinition getDefinition() {
		return definition;
	}

	int getFollowDistance() {
		if (getOwner() != null)
			if (getOwner() instanceof Player)
				if (getDefinition().getType() == ((Player) getOwner()).familiarId)
					return 2;
		if (getCombatType() == null)
			return 1;
		else
			switch (getCombatType()) {
			case RANGE:
				return 6;
			case MAGIC:
				return 8;
			default:
				return 1;
			}
	}

	public int getHP() {
		return hp;
	}

	public HunterState getHunterState() {
		return hunterState;
	}

	/**
	 * @return the npcId
	 */
	public int getNpcId() {
		return npcSlot;
	}

	public NPCTeleportHandler getNPCTeleportHandler() {
		return npcTele;
	}

	public NPCResetTask getResetTask() {
		return resetTask;
	}

	public NPCTickTask getTickTask() {
		return tickTask;
	}

	/**
	 * @return the x1
	 */
	public int getX1() {
		return x1;
	}

	/**
	 * @return the x2
	 */
	public int getX2() {
		return x2;
	}

	/**
	 * @return the y1
	 */
	public int getY1() {
		return y1;
	}

	/**
	 * @return the y2
	 */
	public int getY2() {
		return y2;
	}

	public void gfx0(int gfx) {
		mask80var1 = gfx;
		mask80var2 = 65536;
		mask80update = true;
		updateRequired = true;
	}

	public void gfx100(int gfx) {
		mask80var1 = gfx;
		mask80var2 = 6553600;
		mask80update = true;
		updateRequired = true;
	}

	public boolean goodDistance(int objectX, int objectY, int playerX,
			int playerY, int distance) {
		for (int i = 0; i <= distance; i++)
			for (int j = 0; j <= distance; j++)
				if (objectX + i == playerX
						&& (objectY + j == playerY || objectY - j == playerY || objectY == playerY))
					return true;
				else if (objectX - i == playerX
						&& (objectY + j == playerY || objectY - j == playerY || objectY == playerY))
					return true;
				else if (objectX == playerX
						&& (objectY + j == playerY || objectY - j == playerY || objectY == playerY))
					return true;
		return false;
	}

	public boolean gwdCoords() {
		if (heightLevel == 2 || heightLevel == 0 || heightLevel == 1)
			if (getAbsX() >= 2800 && getAbsX() <= 2950 && getAbsY() >= 5200
					&& getAbsY() <= 5400)
				return true;
		return false;
	}

	public void handleAggression(int npcType) {
		if (definition == null || getTarget() != null || isHidden)
			return;
		if (NPCConstants.isAgressive(this))
			for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
				final Player player = PlayerManager.getSingleton().getPlayers()[i];
				if (player == null || player.getTarget() != null)
					continue;
				int d = 4;
				if (getDefinition().getCombat() > 100)
					d = 7;
				if (Misc.distance(getAbsX(), getAbsY(), player.getAbsX(),
						player.getAbsY()) <= d
						&& getHeightLevel() == player.getHeightLevel()
						&& !player.doingTutorial) {
					setTarget(player);
					setFollowing(getTarget());
					setInCombatWith(player);
					break;
				}
			}
	}

	public void handleDeath() {
		final int npcType = getDefinition().getType();
		if (hp <= 0 && maxHP > 0 && !isDead) {
			switch (definition.getType()) {
			case 6142:
			case 6143:
			case 6144:
			case 6145:
				PestControl.getInstance().onDeath(this);
				break;
			}
			if (getTarget() instanceof Player)
				if ((Player) getTarget() != null)
					if (npcType == 10110 || npcType == 10044 || npcType == 9916
							|| npcType == 9934 || npcType == 10064
							|| npcType == 10116 || npcType == 9989) {
						((Player) getTarget()).getActionSender().sendMessage(
								"You have destroyed your boss: @red@"
										+ InstanceDistributor.getNPCManager()
												.getNPCDefinition(npcType)
												.getName() + "@bla@.");
						((Player) getTarget()).bawsed = true;
						Dungeoneering.getInstance().completeDungeon(
								(Player) getTarget());
					}

			if (getTarget() instanceof Player) {
				if (NPCConstants.isFightCaveNpc(npcType))
					killedTzhaar((Player) getTarget(), npcType);

				if (NPCConstants.isFightCaveNpc(npcType))
					killedTzhaar((Player) getTarget(), npcType);
				if (NPCConstants.isBandosNPC(npcType))
					((Player) getTarget()).bandosKc++;
				if (getTarget() instanceof Player)
					if (npcType == ((Player) getTarget()).duoTask) {
						((Player) getTarget()).duoTaskAmount--;
						if (((Player) getTarget()).getDuoPartner() != null)
							((Player) getTarget()).getDuoPartner().duoTaskAmount--;
					}
				if (NPCConstants.isZamorakNPC(npcType))
					((Player) getTarget()).zamorakKc++;
				if (NPCConstants.isSaradominNPC(npcType))
					((Player) getTarget()).saradominKc++;
				if (NPCConstants.isArmaNPC(npcType))
					((Player) getTarget()).armaKc++;
				if (npcType == 3493) {
					((Player) getTarget()).rfdProgress++;
					InstanceDistributor.getRecipeForDisaster().startWaves(
							(Player) getTarget());
				}
				if (npcType == 3494) {
					((Player) getTarget()).rfdProgress++;
					InstanceDistributor.getRecipeForDisaster().startWaves(
							(Player) getTarget());
				}
				if (npcType == 3495) {
					((Player) getTarget()).rfdProgress++;
					InstanceDistributor.getRecipeForDisaster().startWaves(
							(Player) getTarget());
				}
				if (npcType == 3496) {
					((Player) getTarget()).rfdProgress++;
					InstanceDistributor.getRecipeForDisaster().startWaves(
							(Player) getTarget());
				}
				if (npcType == NPCStats.GLACOR || npcType == NPCStats.JAD
						|| npcType == NPCStats.TORMDEMON
						|| npcType == NPCStats.NOMAD
						|| npcType == NPCStats.CORPORAL
						|| npcType == NPCStats.KBD || npcType == NPCStats.NEX
						|| npcType == NPCStats.BARRELCHEST
						|| npcType == NPCStats.AVATAR
						|| npcType == NPCStats.CHAOSELE
						|| npcType == NPCStats.DAGGBOSS1
						|| npcType == NPCStats.DAGGBOSS2
						|| npcType == NPCStats.DAGGBOSS3
						|| npcType == NPCStats.FROSTDRAG)
					((Player) getTarget()).bossesKilled++;
				if (npcType == NPCStats.MITHDRAG)
					((Player) getTarget()).mithKilled++;
				if (npcType == NPCStats.GLACOR)
					((Player) getTarget()).glacorKilled++;
				if (npcType == NPCStats.TORMDEMON)
					((Player) getTarget()).demonsKilled++;
				if (npcType == NPCStats.NOMAD)
					((Player) getTarget()).nomadKilled++;
				if (npcType == NPCStats.KBD)
					((Player) getTarget()).kbdKilled++;
				if (npcType == NPCStats.NEX)
					((Player) getTarget()).nexKilled++;
				if (npcType == NPCStats.BARRELCHEST)
					((Player) getTarget()).barrelKilled++;
				if (npcType == NPCStats.AVATAR)
					((Player) getTarget()).avatarKilled++;
				if (npcType == NPCStats.FROSTDRAG)
					((Player) getTarget()).frostsKilled++;
				if (npcType == NPCStats.CHAOSELE)
					((Player) getTarget()).chaosKilled++;
				if (npcType == NPCStats.DAGGBOSS1
						|| npcType == NPCStats.DAGGBOSS2
						|| npcType == NPCStats.DAGGBOSS3)
					((Player) getTarget()).daggsKilled++;
				if (npcType == NPCStats.ARMABOSS
						|| npcType == NPCStats.ZAMMYBOSS
						|| npcType == NPCStats.BANDOSBOSS)
					((Player) getTarget()).godwarKilled++;
				if ((Player) getTarget() != null
						&& ((Player) getTarget()).floor1()
						|| (Player) getTarget() != null
						&& Areas.bossRoom1(((Player) getTarget()).getPosition()))
					((Player) getTarget()).npcsKilled++;

			}
			if (npcType == NPCStats.CORPORAL) {
				CorporalBeast.spawned = false;
				((Player) getTarget()).corpKilled++;
			}
			if (getTarget() instanceof Player) {
				if (npcType == 3497) {
					((Player) getTarget()).rfdProgress++;
					InstanceDistributor.getRecipeForDisaster().startWaves(
							(Player) getTarget());
				}
				if (npcType == 3491)
					InstanceDistributor.getRecipeForDisaster().completeRFD(
							(Player) getTarget());

			}
			if (InstanceDistributor.getBarrows().isBarrowsBrother(npcType)
					|| NPCConstants.isFightCaveNpc(npcType))
				if (getTarget() != null)
					if (getTarget() instanceof Player) {
						if (npcType == NPCStats.JAD)
							if (fightCaver != -1) {
								final Player cave = PlayerManager
										.getSingleton().getPlayers()[fightCaver];
								if (cave != null) {
									InstanceDistributor.getTzhaarCave()
											.completeJad(cave);
									return;
								}
							}
						InstanceDistributor.getBarrows().onBrothersDead(
								(Player) getTarget(), npcType);
					}

			if (getTarget() != null)
				if (getTarget() instanceof Player)
					((Player) getTarget()).totalKilled++;

			isDead = true;
			deathDelay = 3;
			FollowEngine.resetFollowing(this);
			CombatEngine.resetAttack(this, false);
			setInCombatWith(null);
			setInCombatTimer(0);
			if (npcType == 4278 || npcType == 4279 || npcType == 4280
					|| npcType == 4281 || npcType == 4282 || npcType == 4283
					|| npcType == 4284)
				NPCConstants.drop(getTarget(), this);
		}
		if (isDead && deathDelay < 1)
			if (!isDeadWaiting && !isDeadTeleporting) {
				animNumber = NPCManager.emotions[getDefinition().getType()][2];
				if (getOwner() != null && getOwner() != null)
					if (getOwner() instanceof Player
							&& getOwner() instanceof Player)
						if (npcType != 1383 && npcType != 1384
								&& npcType != 1385 && npcType != 4278
								&& npcType != 4279 && npcType != 4280
								&& npcType != 4281 && npcType != 4282
								&& npcType != 4283 && npcType != 4284)
							removeNPC(this, 789987);
				if (npcType == 4998)
					Nex.wrathDamage(this);
				if (npcType == 1351 && hftder != -1) {
					final Player ss = PlayerManager.getSingleton().getPlayers()[hftder];
					if (ss != null && ss instanceof Player) {
						ss.hftdStage = 6;

						Achievements.getInstance().complete(ss, 26);

						ss.getActionSender()
								.sendMessage(
										"You've now completed the quest : @red@Horror From The Deep.");
						PlayerSaving.savePlayer(ss);

						TeleportationHandler
								.addNewRequest(ss, 2507, 3630, 0, 0);
						HorrorFromTheDeep.getInstance().refreshQuestInterface(
								ss, true, true);

					}
				}

				NPCConstants.drop(getTarget(), this);
				animUpdateRequired = true;
				updateRequired = true;
				isDeadWaiting = true;
				isDeadTimer = 4;

				if (isWalking)
					wasWalking = true;
				isWalking = false;
				if (previousAttackers == null) {
					previousAttackers = attackers;
					attackers = new HashMap<String, Integer>();
					attackers.putAll(previousAttackers);
				}
			} else if (isDeadTeleporting) {
				if (isDeadTimer == 0) {

					isDead = false;
					isHidden = false;
					if (wasWalking)
						isWalking = true;
					hp = maxHP;
					isDeadTeleporting = false;
					isDead = false;
					setAbsX(spawnAbsX);
					setAbsY(spawnAbsY);
					setPosition(new Location(spawnAbsX, spawnAbsY,
							getHeightLevel()));
				}
				isDeadTimer--;
			} else {

				if (isDeadTimer == 0) {

					setTarget(null);
					setFollowing(null);
					animNumber = -1;
					animUpdateRequired = true;
					updateRequired = true;
					isDeadWaiting = false;
					isHidden = true;
					isDeadTimer = definition.getRespawn();
					isDeadTeleporting = true;
					if (getTarget() != null && getTarget().getOwner() != null)
						if (npcType != 1383 && npcType != 1384
								&& npcType != 1385)
							removeNPC(this, 465465);// TODO
					// TODO what? (someone put todo but i don't get what they
					// need to do, so i'm asking what)
				}
				isDeadTimer--;
			}
	}

	public void heal(int amount) {
		hp += amount;
		if (hp + amount > maxHP)
			hp = maxHP;
	}

	/**
	 * Hit the npc.
	 * 
	 * @param combatType
	 * 
	 * @param client
	 * @param hit
	 * @param type
	 */
	public void hit(final Entity attacker, Entity victim, int damage,
			CombatType type) {

		if (attacker == null || victim == null)
			return;
		setAttackHolder(attacker);
		int attackerNpcId = 0;
		if (attacker instanceof NPC)
			attackerNpcId = ((NPC) attacker).getDefinition().getType();
		final int npcId = ((NPC) victim).getDefinition().getType();
		int hitTotal = 0;
		boolean unhittable = false;
		if (InstanceDistributor.getNPCManager().getNPCDefinition(npcId)
				.getName().contains("titan")
				|| InstanceDistributor.getNPCManager().getNPCDefinition(npcId)
						.getName().contains("minotaur"))
			unhittable = true;
		if (attacker instanceof NPC && victim instanceof NPC) {
			if (unhittable)
				return;
			if (summoned1 || summoned2 || summoned3)
				return;
			damage = Misc.random(NPCAttacks.npcArray[attackerNpcId][1]);
			if (npcId == 3782)
				damage = damage / 3;
			if (!hitUpdateRequired) {
				if (attacker instanceof Player)
					((NPC) victim).attackers.put(
							((Player) attacker).getUsername(), hitTotal
									+ damage);
				((NPC) victim).hit = damage;
				((NPC) victim).type = type;
				((NPC) victim).hp = ((NPC) victim).hp - damage;
				final Entity zor = ((NPC) attacker).getOwner();

				if (zor instanceof Player) {
					final int id = ((Player) zor).getID();
					boolean isThere = false;
					for (int i = 0; i < 50; i = i + 2)
						if (damageFromPlayers[i] == id) {
							damageFromPlayers[i + 1] = damageFromPlayers[i + 1]
									+ damage;
							isThere = true;
							break;
						}
					if (!isThere)
						for (int i = 0; i < 50; i = i + 2)
							if (damageFromPlayers[i] == 0) {
								damageFromPlayers[i] = id;
								damageFromPlayers[i + 1] = damage;
								break;
							}

				}
				if (((NPC) victim).hp <= 0)
					((NPC) victim).hp = 0;
				((NPC) victim).hitType = damage > 0 ? 1 : 0;

				((NPC) victim).hitUpdateRequired = true;

				if (npcId != 2745 && npcId != 2743 && npcId != 2631
						&& npcId != 2741)
					AnimationProcessor.createAnimation(victim,
							NPCManager.emotions[npcId][1]);
				victim.setWalkingHome(false);

				setFollowing(attacker);
				/*
				 * if (attacker instanceof Player) { this.following = attacker;
				 * } else { this.following = attacker; }
				 */

			} else if (!hitUpdateRequired2) {
				if (attacker instanceof Player)
					((NPC) victim).attackers.put(
							((Player) attacker).getUsername(), hitTotal
									+ damage);
				((NPC) victim).hit2 = damage;
				((NPC) victim).type2 = type;
				((NPC) victim).hp = ((NPC) victim).hp - damage;

				if (((NPC) victim).hp <= 0)
					((NPC) victim).hp = 0;
				((NPC) victim).hitType2 = damage > 0 ? 1 : 0;

				((NPC) victim).hitUpdateRequired2 = true;
				AnimationProcessor.createAnimation(victim,
						NPCManager.emotions[npcId][1]);
				victim.setWalkingHome(false);
				setFollowing(attacker);
				/*
				 * if (attacker instanceof Player) { this.following = attacker;
				 * } else { this.following = attacker; }
				 */
			}

		}
		if (attacker instanceof Player)
			if (victim instanceof NPC) {
				if (summoned1 || summoned2 || summoned3) {
					((Player) attacker)
							.getActionSender()
							.sendMessage(
									"You can't damage the glacors untill you've slain it's guards.");
					return;
				}
				final int npcType = ((NPC) victim).getDefinition().getType();
				if (((NPC) victim).attackers.get(attacker) != null)
					hitTotal = ((NPC) victim).attackers.get(attacker);
				if (damage > ((NPC) victim).hp)
					damage = ((NPC) victim).hp;
				boolean k = false;

				if (npcType == ((Player) attacker).slayerTask
						|| npcType == ((Player) attacker).duoTask)
					k = true;
				if (((Player) attacker).slayerTask == 8150)
					if (npcType == 8152)
						k = true;
				if (((Player) attacker).slayerTask == 4673)
					if (npcType == 4673 || npcType == 50)
						k = true;
				if (((Player) attacker).slayerTask == 1623)
					if (npcType == 1627 || npcType == 1628 || npcType == 1628)
						k = true;
				if (((Player) attacker).slayerTask == 92)
					if (getDefinition().getName().contains("skeleton")
							|| getDefinition().getName().contains("Skeleton"))
						k = true;
				if (k) {
					if (((Player) attacker).playerEquipment[PlayerConstants.HELM] == 15492
							|| ((Player) attacker).playerEquipment[PlayerConstants.HELM] == 8921
							&& attacker.getCombatType() == CombatType.MELEE)
						damage = (int) (damage * 1.16);
					if (((Player) attacker).playerEquipment[PlayerConstants.HELM] == 15492
							|| ((Player) attacker).playerEquipment[PlayerConstants.HELM] == 15488
							&& attacker.getCombatType() == CombatType.MAGIC)
						damage = (int) (damage * 1.16);
					if (((Player) attacker).playerEquipment[PlayerConstants.HELM] == 15492
							|| ((Player) attacker).playerEquipment[PlayerConstants.HELM] == 15490
							&& attacker.getCombatType() == CombatType.RANGE)
						damage = (int) (damage * 1.16);
				}
				if (getCombatType() == CombatType.MAGIC)
					if (damage >= 700)
						Achievements.getInstance().complete((Player) attacker,
								48);
				if (!hitUpdateRequired) {
					((NPC) victim).attackers.put(
							((Player) attacker).getUsername(), hitTotal
									+ damage);
					((NPC) victim).hit = damage;
					((NPC) victim).type = type;
					((NPC) victim).hp = ((NPC) victim).hp - damage;

					final int id = ((Player) attacker).getID();
					boolean isThere = false;
					for (int i = 0; i < 50; i = i + 2)
						if (damageFromPlayers[i] == id) {
							damageFromPlayers[i + 1] = damageFromPlayers[i + 1]
									+ damage;
							isThere = true;
							break;
						}
					if (!isThere)
						for (int i = 0; i < 50; i = i + 2)
							if (damageFromPlayers[i] == 0) {
								damageFromPlayers[i] = id;
								damageFromPlayers[i + 1] = damage;
								break;
							}
					if (((NPC) victim).hp <= 0)
						((NPC) victim).hp = 0;
					((NPC) victim).hitType = damage > 0 ? 1 : 0;

					((NPC) victim).hitUpdateRequired = true;
					if (npcId != 2745 && npcId != 2743 && npcId != 2631
							&& npcId != 2741)
						AnimationProcessor.createAnimation(victim,
								NPCManager.emotions[npcId][1]);
					victim.setWalkingHome(false);
					setFollowing(attacker);

				} else if (!hitUpdateRequired2) {
					((NPC) victim).attackers.put(
							((Player) attacker).getUsername(), hitTotal
									+ damage);
					((NPC) victim).hit2 = damage;
					((NPC) victim).type2 = type;
					((NPC) victim).hp = ((NPC) victim).hp - damage;

					if (((NPC) victim).hp <= 0)
						((NPC) victim).hp = 0;
					((NPC) victim).hitType2 = damage > 0 ? 1 : 0;

					((NPC) victim).hitUpdateRequired2 = true;
					AnimationProcessor.createAnimation(victim,
							NPCManager.emotions[npcId][1]);
					victim.setWalkingHome(false);

					setFollowing(attacker);
				} else
					HitExecutor.addNewHit(attacker, victim, CombatType.MELEE,
							damage, 0);
				updateRequired = true;
			}

	}

	public boolean inCombat() {
		return inCombat;
	}

	public boolean inCombat(boolean combatStatus) {
		return inCombat = combatStatus;
	}

	public boolean inFloor2BossRoom() {
		return getAbsX() > 1870 && getAbsX() < 1885 && getAbsY() > 4650
				&& getAbsY() < 4665;
	}

	public boolean inWarriorG() {
		return getAbsX() >= 2835 && getAbsX() <= 2877 && getAbsY() >= 3532
				&& getAbsY() <= 3559;
	}

	/**
	 * @return the animUpdateRequired
	 */
	public boolean isAnimUpdateRequired() {
		return animUpdateRequired;
	}

	/**
	 * @return the isDead
	 */
	@Override
	public boolean isDead() {
		return isDead;
	}

	public boolean isDeath() {
		return isDead;
	}

	/**
	 * @return the isHidden
	 */
	public boolean isHidden() {
		return isHidden;
	}

	/**
	 * Do NOT touch, this is correct way.
	 */
	public boolean IsInRange(NPC npc, int MoveX, int MoveY) {
		if (MoveX <= npc.getX1() && MoveX >= npc.getY1()
				&& MoveY <= npc.getX2() && MoveY >= npc.getY2()) {
			if (MoveX == MoveY)
				return false;
			return true;
		}
		return false;
	}

	/**
	 * @return the updateRequired
	 */
	public boolean isUpdateRequired() {
		return updateRequired;
	}

	/**
	 * @return the isWalking
	 */
	public boolean isWalking() {
		return isWalking;
	}

	/**
	 * @return the wasWalking
	 */
	public boolean isWasWalking() {
		return wasWalking;
	}

	private void killedTzhaar(final Player client, int i) {
		client.tzhaarKilled++;
		if (client.tzhaarKilled == client.tzhaarToKill) {
			client.waveNumber++;
			client.getActionSender().sendMessage(
					"Your next wave will start in 6 seconds.");
			client.getPlayerEventHandler().addEvent(new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (client != null)
						InstanceDistributor.getTzhaarCave().spawnNextWave(
								client);
					container.stop();
				}

				@Override
				public void stop() {
					// TODO Auto-generated method stub

				}
			}, 10);

		}
	}

	@Override
	public int mageAttack() {
		return NPCAttacks.npcArray[getDefinition().getType()][3];
	}

	@Override
	public int mageDefense() {
		return NPCAttacks.npcArray[getDefinition().getType()][6];
	}

	@Override
	public int meleeAttack() {
		return NPCAttacks.npcArray[getDefinition().getType()][2];
	}

	@Override
	public int meleeDefense() {
		return NPCAttacks.npcArray[getDefinition().getType()][5];
	}

	public boolean neitiznotCoords() {
		return getAbsX() > 2307 && getAbsX() < 2370 && getAbsY() > 3780
				&& getAbsY() < 3868;
	}

	public void process() {
		final Entity ent = this;
		final int npcType = ((NPC) ent).getDefinition().getType();
		if (npcType == 8948)
			if (Misc.random(15) == 1)
				forceChat("Exchange your armour boxes here!");
		if (npcType == 2290)
			if (System.currentTimeMillis() - lastForceChat > 2400 * 2.5) {
				final int x = forceChatValue;
				switch (x) {
				case 0:
					lastForceChat = System.currentTimeMillis();
					forceChatValue++;
					break;
				case 1:
					forceChat("Did you know most people PK in Edge? Get there by Pking teleports!");
					lastForceChat = System.currentTimeMillis();
					forceChatValue++;
					break;
				case 2:
					forceChat("Did you know I can lock your exp?");
					lastForceChat = System.currentTimeMillis();
					forceChatValue++;
					break;
				case 3:
					lastForceChat = System.currentTimeMillis();
					forceChatValue = 0;
					break;
				}

			}
		if (getDefinition().getHealth() == 0
				&& !PetHandler.isNpcPet(getDefinition().getType())
				&& !Hunter.isHunterNPC(getDefinition().getType())) {
			walkingProcess();
			return;
		}
		if (getDefinition().getHealth() > 0 && hp < 1)
			handleDeath();
		if (npcType == 8152 || npcType == 8150)
			if (!Areas.isArmouredCave(getPosition())) {
				setTarget(null);
				setFollowing(null);
				setWalk(spawnAbsX, spawnAbsY, false);
			}

		if (npcType == 4278 || npcType == 4279 || npcType == 4280
				|| npcType == 4281 || npcType == 4282 || npcType == 4283
				|| npcType == 4284
				|| InstanceDistributor.getBarrows().isBarrowsBrother(npcType)
				|| NPCConstants.isFightCaveNpc(npcType))
			if (getTarget() != null) {
				if (NPCConstants.isFightCaveNpc(npcType) ? TileManager
						.calculateDistance(this, getTarget()) > 100
						: TileManager.calculateDistance(this, getTarget()) > 30
								|| getHeightLevel() != getTarget()
										.getHeightLevel()
								|| getTarget().isDead() || isDead()) {
					if (getTarget() instanceof Player)
						if (npcType == 4284 || npcType == 4279
								|| npcType == 4280 || npcType == 4281
								|| npcType == 4282 || npcType == 4283)
							((Player) getTarget()).spawnedAnimator = false;
					if (InstanceDistributor.getBarrows().isBarrowsBrother(
							npcType))
						if (getTarget() instanceof Player)
							((Player) getTarget()).tempSpawnLock = false;
					removeNPC(this, 3);
				}
			} else if (getTarget() == null)
				removeNPC(this, 1);
		if (getOwner() != null)
			if (getOwner().getTarget() == null
					&& !InstanceDistributor.getBarrows().isBarrowsBrother(
							npcType))
				setFollowing(getOwner());
		if (npcType == 8528)
			if (getTarget() != null)
				if (TileManager.calculateDistance(this, getTarget()) > 30
						|| getHeightLevel() != getTarget().getHeightLevel()) {
					((Player) getTarget()).getActionSender().sendMessage(
							"Nomad fades away....");
					((Player) getTarget()).nomadNeedsSpawn = true;
					removeNPC(this, 2);
				}
		if (isDead()) {
			healed = 0;
			attacktimer = 5;
			summoned1 = false;
			summoned2 = false;
			summoned3 = false;
			summoned = false;
			summonedn1 = null;
			summonedn2 = null;
			summonedn3 = null;
			c = 0;
			if (getDefinition().getType() == 1383) {
				((NPC) getOwner()).summoned1 = false;
				removeNPC(this, 9);
			}
			if (getDefinition().getType() == 1384) {
				((NPC) getOwner()).summoned2 = false;
				removeNPC(this, 8);
			}
			if (getDefinition().getType() == 1385) {
				((NPC) getOwner()).summoned3 = false;
				removeNPC(this, 7);
			}
		}
		if (getTarget() != null && getOwner() != null
				&& getHeightLevel() != getOwner().getHeightLevel()
				&& InstanceDistributor.getBarrows().isBarrowsBrother(npcType)) {
			if (getTarget() instanceof Player)
				((Player) getTarget()).getActionSender().sendMessage(
						"Your barrow brother fades away.");
			final NPC bikboi = this;
			World.getWorld().submit(new Tickable(2) {
				@Override
				public void execute() {
					if (npcType != 1383 && npcType != 1384 && npcType != 1385)
						removeNPC(bikboi, 456123);
					stop();
				}
			});
		}
		if (getOwner() != null)
			if (getTarget() != null)
				if (getTarget() instanceof Player && getOwner() instanceof NPC)
					if (((Player) getTarget()).isDead()
							&& getOwner() == getTarget() && npcType != 10110
							&& npcType != 10044)
						if (npcType != 1383 && npcType != 1384
								&& npcType != 1385)
							removeNPC(this, 150);
		if (getTarget() != null)
			if (TileManager.calculateDistance(this, getTarget()) > 16) {
				setTarget(null);
				setInCombatWith(null);
				setFollowing(null);
				if (getOwner() != null) {
					if (npcType != 1383 && npcType != 1384 && npcType != 1385)
						removeNPC(this, 151);
					if (getOwner() instanceof Player)
						((Player) getOwner()).spawnedAnimator = false;
				}
			}
		final Location spawnPlace = new Location(spawnAbsX, spawnAbsY);
		final Location currentPosition = new Location(getAbsX(), getAbsY());
		if (Misc.distanceTo(spawnPlace, currentPosition, 1) > 10
				&& !isBoss(npcType) && !PetHandler.isNpcPet(npcType)
				&& !Random.isRandomNPC(npcType)) {
			FollowEngine.resetFollowing(this);
			if (getTarget() != null) {
				setTarget(null);
				setInCombatWith(null);
				setFollowing(null);
			}
			setWalk(spawnAbsX, spawnAbsY, false);
		}
		if (attacker != -1) {
			if (npcType == 5001 && !isDead) {
				final Player atk = PlayerManager.getSingleton().getPlayers()[attacker];
				if (atk != null && !Nex.inArea(atk.getAbsX(), atk.getAbsY())) {
					TeleportationHandler.addNewRequest(this, 2464, 4780, 0, 0);
					updateRequired = true;
				}
			}
			if (npcType == 5001 && Nex.isDead) {
				removeNPC(this, 4);
				Nex.removed1 = false;
				updateRequired = true;
			}
		}
		FollowEngine.loop(this);

		handleAggression(npcType);
		setFaceDirection();
		deathDelay--;
		if (getOwner() != null)
			summoningProcess();
		walkingProcess();
	}

	@Override
	public int rangeAttack() {
		return NPCAttacks.npcArray[getDefinition().getType()][4];
	}

	@Override
	public int rangeDefense() {
		return NPCAttacks.npcArray[getDefinition().getType()][7];
	}

	/**
	 * @param animNumber
	 *            the animNumber to set
	 */
	public void setAnimNumber(int animNumber) {
		this.animNumber = animNumber;
	}

	/**
	 * @param animUpdateRequired
	 *            the animUpdateRequired to set
	 */
	public void setAnimUpdateRequired(boolean animUpdateRequired) {
		this.animUpdateRequired = animUpdateRequired;
	}

	/**
	 * @param attacker
	 *            the attacker to set
	 */
	public void setAttacker(Player attacker) {
		this.attacker = attacker.getIndex();
	}

	/**
	 * @param isDead
	 *            the isDead to set
	 */
	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	/**
	 * @param definition
	 *            the definition to set
	 */
	public void setDefinition(NPCDefinition definition) {
		this.definition = definition;
	}

	/**
	 * Sets face directions of NPC
	 */
	public void setFaceDirection() {

		final int id = definition.getType();

		// example
		if (id == 9085)
			turnNpc(getAbsX(), getAbsY() + 1);
		if (id == 2290 || id == 494 || id == 495) {

			if (getAbsX() == 2849 && getAbsY() == 10220)
				turnNpc(getAbsX() + 1, getAbsY() + 1);
			if (getAbsY() == 10205 || getAbsY() == 3492 || getAbsY() == 3353
					|| getAbsY() == 3366 || getAbsY() == 3492)
				if (getAbsY() == 10205 && getAbsX() > 2900) {

				} else
					turnNpc(getAbsX(), getAbsY() + 1);

		}
		if (id == 33 || id == 249 || id == 649 || id == 553 || id == 7951
				|| id == 966 || id == 4476 || id == 278 || id == 8274
				|| id == 3393 || id == 682 || id == 1866) {

			if (getAbsX() == 2990)
				turnNpc(getAbsX() + 1, getAbsY());
			if (getAbsX() == 3277)
				turnNpc(getAbsX() + 1, getAbsY());
			if (getAbsX() == 2631)
				turnNpc(getAbsX() + 1, getAbsY());
		}
		if (id == 494 || id == 495 || id == 590 || id == 6970) {
			if (getAbsX() == 3096 && getAbsY() != 3492)
				turnNpc(getAbsX() - 1, getAbsY());
			if (getAbsX() == 3187)
				turnNpc(getAbsX() - 1, getAbsY());
			if (getAbsX() == 3285)
				turnNpc(getAbsX() - 1, getAbsY());

		}

		// example

	}

	/**
	 * @param isHidden
	 *            the isHidden to set
	 */
	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	public void setHP(int hp) {
		this.hp = hp;
	}

	public void setHunterState(HunterState hunterState) {
		this.hunterState = hunterState;
	}

	/**
	 * @param npcId
	 *            the npcId to set
	 */
	public void setNpcId(int npcId) {
		npcSlot = npcId;
	}

	/**
	 * @param updateRequired
	 *            the updateRequired to set
	 */
	public void setUpdateRequired(boolean updateRequired) {
		this.updateRequired = updateRequired;
	}

	public void setWalk(int tgtX, int tgtY, boolean restrict) {
		if (isDead)
			return;
		if (definition.getType() == 3782 || definition.getType() == 6142
				|| definition.getType() == 6143 || definition.getType() == 6144
				|| definition.getType() == 6145)
			return;
		direction = Misc.direction(getAbsX(), getAbsY(), tgtX, tgtY);
		if (restrict)
			if (tgtX > x1 || tgtX < x2 || tgtY > y1 || tgtY < y2) {
				direction = -1;
				return;
			}
		if (direction != -1) {
			direction >>= 1;
			setAbsX(tgtX);
			setAbsY(tgtY);
			setPosition(new Location(getAbsX(), getAbsY(), heightLevel));
		}
		updateRequired = true;
	}

	/**
	 * @param isWalking
	 *            the isWalking to set
	 */
	public void setWalking(boolean isWalking) {
		this.isWalking = isWalking;
	}

	/**
	 * @param wasWalking
	 *            the wasWalking to set
	 */
	public void setWasWalking(boolean wasWalking) {
		this.wasWalking = wasWalking;
	}

	/**
	 * @param x1
	 *            the x1 to set
	 */
	public void setX1(int x1) {
		this.x1 = x1;
	}

	/**
	 * @param x2
	 *            the x2 to set
	 */
	public void setX2(int x2) {
		this.x2 = x2;
	}

	/**
	 * @param y1
	 *            the y1 to set
	 */
	public void setY1(int y1) {
		this.y1 = y1;
	}

	/**
	 * @param y2
	 *            the y2 to set
	 */
	public void setY2(int y2) {
		this.y2 = y2;
	}

	private void summoningProcess() {
		final Entity ent = this;
		final int npcType = ((NPC) ent).getDefinition().getType();
		if (getOwner() instanceof Player) {
			if (getOwner() != null && hasToAttack
					&& getOwner().getTarget() == null
					&& ((Player) getOwner()).steelTitanSpec
					&& getTarget() == null) {
				((Player) getOwner()).getActionSender().sendMessage(
						"Your familiar doesn't have a target.");
				setFollowing(getOwner());
				((Player) getOwner()).steelTitanSpec = false;

			}
			if (getOwner() != null && hasToAttack
					&& getOwner().getTarget() != null)
				if (getOwner() instanceof Player) {
					if (!((Player) getOwner()).steelTitanSpec) {
						setTarget(getOwner().getTarget());
						setFollowing(getOwner().getTarget());
					}
				} else {
					setTarget(getOwner().getTarget());
					setFollowing(getOwner().getTarget());
				}
			if (getOwner() instanceof Player)
				if (getOwner() != null && hasToAttack
						&& getOwner().getTarget() != null
						&& ((Player) getOwner()).steelTitanSpec)
					if (!multiZone()) {
						((Player) getOwner())
								.getActionSender()
								.sendMessage(
										"Familiars can only help in combat in multi zones.");
						hasToAttack = false;
						((Player) getOwner()).steelTitanSpec = false;
					} else {

						GraphicsProcessor.addNewRequest(this, 1449, 0, 0);
						if (TileManager.calculateDistance(this, getOwner()
								.getTarget()) > 4) {
							if (Misc.random(1) == 1)
								HitExecutor.addNewHit(this, getOwner()
										.getTarget(), CombatType.RANGE, Misc
										.random(24), 5);
							if (Misc.random(1) == 1)
								HitExecutor.addNewHit(this, getOwner()
										.getTarget(), CombatType.RANGE, Misc
										.random(24), 5);
							if (Misc.random(1) == 1)
								HitExecutor.addNewHit(this, getOwner()
										.getTarget(), CombatType.RANGE, Misc
										.random(24), 5);
							if (Misc.random(1) == 1)
								HitExecutor.addNewHit(this, getOwner()
										.getTarget(), CombatType.RANGE, Misc
										.random(24), 5);
						} else {
							if (Misc.random(1) == 1)
								HitExecutor.addNewHit(this, getOwner()
										.getTarget(), CombatType.MELEE, Misc
										.random(24), 5);
							if (Misc.random(1) == 1)
								HitExecutor.addNewHit(this, getOwner()
										.getTarget(), CombatType.MELEE, Misc
										.random(24), 5);
							if (Misc.random(1) == 1)
								HitExecutor.addNewHit(this, getOwner()
										.getTarget(), CombatType.MELEE, Misc
										.random(24), 5);
							if (Misc.random(1) == 1)
								HitExecutor.addNewHit(this, getOwner()
										.getTarget(), CombatType.MELEE, Misc
										.random(24), 5);
						}
						((Player) getOwner()).steelTitanSpec = false;
					}
			if (getOwner() instanceof Player) {
				if (getOwner() != null && hasToAttack
						&& getOwner().getTarget() != null
						&& ((Player) getOwner()).bronzeMinotaurSpec) {
					if (!multiZone()) {
						((Player) getOwner())
								.getActionSender()
								.sendMessage(
										"Familiars can only help in combat in multi zones.");
						hasToAttack = false;
						((Player) getOwner()).bronzeMinotaurSpec = false;
					} else {
						AnimationProcessor.addNewRequest(this, minoanim, 0);
						GraphicsProcessor.addNewRequest(this, minogfx, 0, 0);
						HitExecutor.addNewHit(this, getOwner().getTarget(),
								CombatType.MELEE, Misc.random(10), 5);
						HitExecutor.addNewHit(this, getOwner().getTarget(),
								CombatType.MAGIC, Misc.random(4), 5);

					}
					((Player) getOwner()).bronzeMinotaurSpec = false;

				}
				if (getOwner() != null && hasToAttack
						&& getOwner().getTarget() != null
						&& ((Player) getOwner()).ironMinotaurSpec) {
					if (!multiZone()) {
						((Player) getOwner())
								.getActionSender()
								.sendMessage(
										"Familiars can only help in combat in multi zones.");
						hasToAttack = false;
						((Player) getOwner()).ironMinotaurSpec = false;
					} else {
						AnimationProcessor.addNewRequest(this, minoanim, 0);
						GraphicsProcessor.addNewRequest(this, minogfx, 0, 0);
						HitExecutor.addNewHit(this, getOwner().getTarget(),
								CombatType.MELEE, Misc.random(10), 5);
						HitExecutor.addNewHit(this, getOwner().getTarget(),
								CombatType.MAGIC, Misc.random(8), 5);
					}
					((Player) getOwner()).ironMinotaurSpec = false;
				}
			}
		}
		if (getOwner() instanceof Player) {
			if (getOwner() != null && hasToAttack
					&& getOwner().getTarget() != null
					&& ((Player) getOwner()).steelMinotaurSpec) {
				if (!multiZone()) {
					((Player) getOwner())
							.getActionSender()
							.sendMessage(
									"Familiars can only help in combat in multi zones.");
					hasToAttack = false;
					((Player) getOwner()).steelMinotaurSpec = false;
				} else {
					AnimationProcessor.addNewRequest(this, minoanim, 0);
					GraphicsProcessor.addNewRequest(this, minogfx, 0, 0);
					HitExecutor.addNewHit(this, getOwner().getTarget(),
							CombatType.MELEE, Misc.random(10), 5);
					HitExecutor.addNewHit(this, getOwner().getTarget(),
							CombatType.MAGIC, Misc.random(10), 5);

				}
				((Player) getOwner()).steelMinotaurSpec = false;

			}
			if (getOwner() != null && hasToAttack
					&& getOwner().getTarget() != null
					&& ((Player) getOwner()).mithrilMinotaurSpec) {
				if (!multiZone()) {
					((Player) getOwner())
							.getActionSender()
							.sendMessage(
									"Familiars can only help in combat in multi zones.");
					hasToAttack = false;
					((Player) getOwner()).mithrilMinotaurSpec = false;
				} else {
					AnimationProcessor.addNewRequest(this, minoanim, 0);
					GraphicsProcessor.addNewRequest(this, minogfx, 0, 0);
					HitExecutor.addNewHit(this, getOwner().getTarget(),
							CombatType.MELEE, Misc.random(10), 5);
					HitExecutor.addNewHit(this, getOwner().getTarget(),
							CombatType.MAGIC, Misc.random(12), 5);
				}
				((Player) getOwner()).mithrilMinotaurSpec = false;
			}
			if (getOwner() != null && hasToAttack
					&& getOwner().getTarget() != null
					&& ((Player) getOwner()).adamantMinotaurSpec) {
				if (!multiZone()) {
					((Player) getOwner())
							.getActionSender()
							.sendMessage(
									"Familiars can only help in combat in multi zones.");
					hasToAttack = false;
					((Player) getOwner()).adamantMinotaurSpec = false;
				} else {
					AnimationProcessor.addNewRequest(this, minoanim, 0);
					GraphicsProcessor.addNewRequest(this, minogfx, 0, 0);
					HitExecutor.addNewHit(this, getOwner().getTarget(),
							CombatType.MELEE, Misc.random(10), 5);
					HitExecutor.addNewHit(this, getOwner().getTarget(),
							CombatType.MAGIC, Misc.random(16), 5);
				}
				((Player) getOwner()).adamantMinotaurSpec = false;
			}
			if (getOwner() != null && hasToAttack
					&& getOwner().getTarget() != null
					&& ((Player) getOwner()).runeMinotaurSpec) {
				if (!multiZone()) {
					((Player) getOwner())
							.getActionSender()
							.sendMessage(
									"Familiars can only help in combat in multi zones.");
					hasToAttack = false;
					((Player) getOwner()).runeMinotaurSpec = false;
				} else {
					AnimationProcessor.addNewRequest(this, minoanim, 0);
					GraphicsProcessor.addNewRequest(this, minogfx, 0, 0);
					HitExecutor.addNewHit(this, getOwner().getTarget(),
							CombatType.MELEE, Misc.random(10), 5);
					HitExecutor.addNewHit(this, getOwner().getTarget(),
							CombatType.MAGIC, Misc.random(20), 5);
				}
				((Player) getOwner()).runeMinotaurSpec = false;
			}
			if (getOwner() != null && hasToAttack
					&& getOwner().getTarget() != null
					&& ((Player) getOwner()).ironTitanSpec) {
				if (!multiZone()) {
					((Player) getOwner())
							.getActionSender()
							.sendMessage(
									"Familiars can only help in combat in multi zones.");
					hasToAttack = false;
					((Player) getOwner()).ironTitanSpec = false;
				} else {

					GraphicsProcessor.addNewRequest(this, 1450, 0, 0);
					HitExecutor.addNewHit(this, getOwner().getTarget(),
							CombatType.MELEE, Misc.random(24), 5);
					HitExecutor.addNewHit(this, getOwner().getTarget(),
							CombatType.MELEE, Misc.random(23), 5);
					HitExecutor.addNewHit(this, getOwner().getTarget(),
							CombatType.MELEE, Misc.random(23), 5);
				}
				((Player) getOwner()).ironTitanSpec = false;
			}
		}
		final boolean summoningAlreadyDone = false;
		if (getOwner() != null)
			if (getOwner() instanceof Player)
				if (getDefinition().getType() == ((Player) getOwner()).familiarId)
					if (getOwner() != null
							&& !((Player) getOwner()).disconnected)
						if (TileManager.calculateDistance(this, getOwner()) > 10
								&& System.currentTimeMillis()
										- ((Player) getOwner()).lastRespawn > 2000) {
							removeNPC(this, 1231321);
							Summoning.getInstance().respawnFamiliar(
									(Player) getOwner());
							return;
						}
		if (getOwner() != null)
			if (getOwner() instanceof Player)
				if (!multiZone()
						&& getDefinition().getType() == ((Player) getOwner()).familiarId)
					if (getTarget() != null) {
						setTarget(null);
						setInCombatWith(null);
					}
		if (!summoningAlreadyDone)
			if (getOwner() != null)
				if (TileManager.calculateDistance(this, getOwner()) > 30)
					if (npcType != 1383 && npcType != 1384 && npcType != 1385)
						removeNPC(this, 153);
	}

	public void teleport(int x, int y) {
		absX = x;
		absY = y;
		npcTele.teleportNpc(teleportToX, teleportToY, teleportToX, y);
	}

	/**
	 * Turns npc to
	 * 
	 * @param x
	 * @param y
	 */
	public void turnNpc(int x, int y) {
		focusPointX = 2 * x + 1;
		focusPointY = 2 * y + 1;
		updateRequired = true;
		turnUpdateRequired = true;

	}

	private void walkingProcess() {
		if (getHunterState().equals(HunterState.INTERESTED))
			return;
		final Entity ent = this;
		final int npcType = ((NPC) ent).getDefinition().getType();
		if (pickingUpItem)
			return;
		if (stepsAllocated < 1 || walkingPaths[0] == 0 && walkingPaths[1] == 0)
			pathCreated = false;
		if (getX1() > 0 && getX2() > 0 && getY1() > 0 && getY2() > 0) {
			if (ent.getFreezeDelay() > 0) {
				updateRequired = true;
				return;
			}
			if (definition.getType() != 8842 && definition.getType() != 8841
					&& definition.getType() != 8842
					&& !definition.getName().equals("Fishing spot")) {
				if (ent.getTarget() == null && !pathCreated)
					if (Misc.random(4) == 1) {
						int MoveX = Misc.random(1);
						int MoveY = Misc.random(1);
						final int Rnd = Misc.random2(4);
						if (Rnd == 1) {
							MoveX = -MoveX;
							MoveY = -MoveY;
						} else if (Rnd == 2)
							MoveX = -MoveX;
						else if (Rnd == 3)
							MoveY = -MoveY;
						if (IsInRange(this, getAbsX() + MoveX, getAbsY()
								+ MoveY) == true) {
							stepsAllocated = Misc.random(10);
							walkingPaths[0] = MoveX;
							walkingPaths[1] = MoveY;
							pathCreated = true;
						}
					}
				if (pathCreated && stepsAllocated > 0
						&& ent.getTarget() == null) {
					if (Misc.random(10) == 1) {
						int MoveX = Misc.random(1);
						int MoveY = Misc.random(1);
						final int Rnd = Misc.random2(4);
						if (Rnd == 1) {
							MoveX = -MoveX;
							MoveY = -MoveY;
						} else if (Rnd == 2)
							MoveX = -MoveX;
						else if (Rnd == 3)
							MoveY = -MoveY;
						if (IsInRange(this, getAbsX() + MoveX, getAbsY()
								+ MoveY) == true) {
							walkingPaths[0] = MoveX;
							walkingPaths[1] = MoveY;
						}
					}
					stepsAllocated--;
					final int[] nextStep = new int[3];
					nextStep[0] = getAbsX() + walkingPaths[0];
					nextStep[1] = getAbsY() + walkingPaths[1];
					nextStep[2] = getHeightLevel();
					if (IsInRange(this, nextStep[0], nextStep[1]) == false) {
						walkingPaths[0] = 0;
						walkingPaths[1] = 0;
						stepsAllocated = 0;
						pathCreated = false;
					} else {
						final boolean canMove = true;
						if (canMove) {
							if (PathFinder.canNPCMove(getAbsX(), getAbsY(),
									getHeightLevel(), walkingPaths[0],
									walkingPaths[1])
									|| neitiznotCoords()
									|| gwdCoords()
									|| npcType == 5073
									|| inWarriorG()
									|| nexRoom(getAbsX(), getAbsY()))
								setWalk(nextStep[0], nextStep[1], false);
							else {
							}
						} else {
							walkingPaths[0] = 0;
							walkingPaths[1] = 0;
							stepsAllocated = 0;
							pathCreated = false;
						}
					}
				}
			}
		}
		if (preventRest)
			preventRest = false;
		setPosition(new Location(getAbsX(), getAbsY(), getHeightLevel()));
		updateRequired = true;
	}
}