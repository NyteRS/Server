package com.server2.model.entity;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

import com.server2.engine.cycle.Tickable;
import com.server2.model.WalkingQueue;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.npc.NPCAttacks;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Areas;
import com.server2.world.World;

/**
 * Represents an entity (player or NPC).
 * 
 * @author Rene
 */
public abstract class Entity {
	public enum CombatType {
		MELEE, RANGE, MAGIC, RECOIL, CANNON, NOTHING, THIEF, POISON
	}

	/*
	 * NOTICE: KEEP ALL THE VARIABLES UP HERE. IF YOU FIND ANY OUT OF PLACE,
	 * MOVE THEM UP HERE.
	 */
	private int retaliateDelay;
	private int index = -1;
	private WeakReference<Entity> owner;
	private Location position; // TODO: use this instead of absx absy and
								// heightlevel
	private WeakReference<Entity> attackerHolder;
	private WeakReference<Entity> following;
	private boolean walkingHome;
	private WeakReference<Entity> inCombatWith; // note that we can't really use
												// integer referencing here
												// without doing some bit hacker
												// stuff which is really not
												// worth it.
	private int inCombatWithTimer;
	private boolean visible = true;
	public NPC tempNPCAllocated;
	private final List<Integer> localPlayers = new LinkedList<Integer>();
	private final List<NPC> localNPCs = new LinkedList<NPC>();
	private final int[] teleportTo = new int[2];
	public int mapRegionX, mapRegionY = 0; // TODO: use position istead of this
	private int invulnerability;
	private boolean hasHit = false;
	private int deadLock;
	private final boolean hasHitTarget[] = new boolean[9];
	private int freezeDelay;
	private int multiHitDelay;
	private int holdingSpell;
	private int holdingSpellDelay;
	private WeakReference<Entity> holdingSpellTarget;
	public int teleportToX = -1, teleportToY = -1; // TODO: make this into a
													// Location
	public boolean didTeleport = true;
	public boolean mapRegionDidChange = true;
	public long lastFreeze;
	private Direction primaryDirection = Direction.NONE;
	private Direction secondaryDirection = Direction.NONE;
	private Location lastKnownRegion = new Location(0, 0);
	private final WalkingQueue walkingQueue = new WalkingQueue(this);
	private boolean beingForcedToWalk;
	public int absY;
	public int absX; // TODO: use position instead of this
	public long unFreeze;
	public int heightLevel;
	private boolean usingSpecial;
	private WeakReference<Entity> target = null;
	private int attackDelay;
	private boolean isBusy;
	private int busyTimer;
	@SuppressWarnings("unchecked")
	private final WeakReference<Entity>[] multiList = new WeakReference[9];
	private boolean charged = false;
	private CombatType combatType = null; // TODO: take away all the null values
											// and replace it with a new combat
											// type "none"

	private int vengTimer;

	public Entity() {

	}

	public int deductBusyTimer() {
		return busyTimer--;
	}

	/**
	 * @param combatDelay
	 *            . the combatDelay to set.
	 */
	public void deductCombatDelay() {
		attackDelay--;
	}

	public void deductInCombatWithTimer() {
		inCombatWithTimer--;
	}

	public void deductVengTimer() {
		vengTimer -= 1;
	}

	public boolean floor1() {
		return getAbsX() > 3190 && getAbsX() < 3270 && getAbsY() > 9275
				&& getAbsY() < 9340;
	}

	public boolean floor2() {
		return getAbsX() > 1856 && getAbsX() < 1930 && getAbsY() > 4609
				&& getAbsY() < 4675;
	}

	public boolean floor3() {
		if (getAbsX() > 3047 || getAbsX() < 3062)
			if (getAbsY() > 4981 && getAbsY() < 4992)
				return false;
		return getAbsX() < 3070 && getAbsX() > 2940 && getAbsY() > 4985
				&& getAbsY() < 5120;

	}

	/**
	 * Forces the entity to walk to the specified position.
	 * 
	 * @param position
	 */
	public void forceMovement(final Location position) {
		if (beingForcedToWalk)
			return;

		if (getFreezeDelay() > 0)
			return;

		walkingQueue.reset();
		walkingQueue.addStep(position, false);
		final long startedAt = System.currentTimeMillis();
		World.getWorld().submit(new Tickable(1) // TODO: get rid of this, move
												// it.
				{
					@Override
					public void execute() {
						final boolean tooLong = System.currentTimeMillis()
								- startedAt > 30000;
						if (tooLong || absX == position.getX()
								&& absY == position.getY())
							stop();
					}

					@Override
					public void stop() {
						beingForcedToWalk = false;
						super.stop();
					}
				});
	}

	/**
	 * @return the absX
	 */
	public int getAbsX() {
		return absX;
	}

	/**
	 * @return the absY
	 */
	public int getAbsY() {
		return absY;
	}

	public Entity getAttackHolder() {
		try {
			return attackerHolder.get();
		} catch (final Exception ex) {
			return null;
		}
	}

	public int getBusyTimer() {
		return busyTimer;
	}

	/**
	 * @return the combatDelay.
	 */
	public int getCombatDelay() {
		return attackDelay;
	}

	/**
	 * @return the combatType
	 */
	public CombatType getCombatType() {
		return combatType;
	}

	public Location getCoordinates() {
		return position;
	}

	public int getDeadLock() {
		return deadLock;
	}

	public Entity getFollowing() {
		try {
			return following.get();
		} catch (final Exception ex) {
			return null;
		}
	}

	/**
	 * @return the freezeDelay
	 */
	public int getFreezeDelay() {
		return freezeDelay;
	}

	public boolean[] getHasHitTarget() {
		return hasHitTarget;
	}

	/**
	 * @return the heightLevel
	 */
	public int getHeightLevel() {
		return heightLevel;
	}

	/**
	 * @return the holdingSpell
	 */
	public int getHoldingSpell() {
		return holdingSpell;
	}

	/**
	 * @return the holdingSpellDelay
	 */
	public int getHoldingSpellDelay() {
		return holdingSpellDelay;
	}

	/**
	 * @return the holdingSpellTarget
	 */
	public Entity getHoldingSpellTarget() {
		if (holdingSpellTarget == null)
			return null;

		return holdingSpellTarget.get();
	}

	/*
	 * public boolean isDead() { if (this instanceof Player) { Player p =
	 * (Player)this; return p.isDead(); } else if (this instanceof NPC) { NPC n
	 * = (NPC)this; return n.isDead(); } return false; }
	 */

	/**
	 * @return the inCombatWith
	 */
	public Entity getInCombatWith() {
		try {
			return inCombatWith.get();
		} catch (final Exception ex) {
			return null;
		}
	}

	/**
	 * @return the inCombatWithTimer
	 */
	public int getInCombatWithTimer() {
		return inCombatWithTimer;
	}

	public int getIndex() {
		return index;
	}

	public int getInvulnerability() {
		return invulnerability;
	}

	public Location getLastKnownRegion() {
		return lastKnownRegion;
	}

	public List<NPC> getLocalNPCs() {
		return localNPCs;
	}

	public List<Integer> getLocalPlayers() {
		return localPlayers;
	}

	public int getLockonIndex() {
		return this instanceof NPC ? index + 1 : -index - 1;
	}

	public int getMultiHitDelay() {
		return multiHitDelay;
	}

	public Entity getMultiList(int slot) {
		if (multiList[slot] != null)
			return multiList[slot].get();

		return null;
	}

	public Entity getOwner() {
		if (owner == null)
			return null;

		return owner.get();
	}

	public Location getPosition() {
		return position;
	}

	public Direction getPrimaryDirection() {
		return primaryDirection;
	}

	public int getRetaliateDelay() {
		return retaliateDelay;
	}

	public Direction getSecondaryDirection() {
		return secondaryDirection;
	}

	public Entity getTarget() {
		try {
			return target.get();
		} catch (final Exception ex) {
			return null;
		}
	}

	public int getTeleportToX() {
		return teleportTo[0];
	}

	public int getTeleportToY() {
		return teleportTo[1];
	}

	public int getVengTimer() {
		return vengTimer;
	}

	public WalkingQueue getWalkingQueue() {
		return walkingQueue;
	}

	public boolean hasHit() {
		return hasHit;
	}

	public boolean inBanditRoom() {
		return getAbsX() < 3166 && getAbsX() > 3155 && getAbsY() > 2974
				&& getAbsY() < 2991;
	}

	public boolean inBase() {
		return getAbsX() > 2551 && getAbsX() < 2582 && getAbsY() > 3450
				&& getAbsY() < 3485;
	}

	public final boolean inRiotWars() {
		return getAbsX() >= 2138 && getAbsX() <= 2164 && getAbsY() >= 5091
				&& getAbsY() <= 5106;
	}

	public final boolean inRiotWarsLobby() {
		return getAbsX() >= 2637 && getAbsX() <= 2665 && getAbsY() >= 4490
				&& getAbsY() <= 4535;
	}

	/**
	 * @return true if the entity is being forced to walk.
	 */
	public boolean isBeingForcedToWalk() {
		return beingForcedToWalk;
	}

	public boolean isBusy() {
		return isBusy;
	}

	public boolean isCharged() {
		return charged;
	}

	public abstract boolean isDead(); // just leaving the commented code below
										// so you can see why i did this. btw
										// this is all i had to change to make
										// it to work.

	public boolean isInDZone() {
		return getAbsX() > 2460 && getAbsX() < 2700 && getAbsY() > 3800
				&& getAbsY() < 3950;
	}

	public boolean isInJail() {
		return getAbsX() > 2774 && getAbsX() < 2777 && getAbsY() > 2795
				&& getAbsY() < 2803;
	}

	public boolean isMoving() {
		return walkingQueue.isMoving();
	}

	public boolean isPlayer() {
		if (this instanceof Player)
			return true;

		return false;
	}

	public boolean isVisible() {
		return visible;
	}

	public abstract int mageAttack();

	public abstract int mageDefense();

	public int mageLevel() {
		if (this instanceof Player)
			return ((Player) this).playerLevel[PlayerConstants.MAGIC];
		else
			return NPCAttacks.npcArray[((NPC) this).getDefinition().getType()][3];
	}

	public abstract int meleeAttack();

	public abstract int meleeDefense();

	public boolean multiZone() {
		if (floor1() || floor2() || floor3()
				|| Areas.inFrostMulti(getPosition()))
			return true;
		if (Areas.isApeAtollGuard(getPosition())
				|| Areas.isArmouredCave(getPosition())
				|| Areas.isRockLobster(getPosition()))
			return true;
		if (Areas.Corp(getPosition()))
			return true;
		if (heightLevel == 2 || heightLevel == 0 || heightLevel == 1) {
			if (getAbsX() >= 2800 && getAbsX() <= 2950 && getAbsY() >= 5200
					&& getAbsY() <= 5400)
				return true;
			if (getAbsX() >= 2620 && getAbsX() <= 2680 && getAbsY() <= 4610
					&& getAbsY() >= 4540)
				return true;
			if (getAbsX() >= 2450 && getAbsX() <= 2475 && getAbsY() >= 4760
					&& getAbsY() <= 4795)
				return true;
			if (inBanditRoom())
				return true;
		}

		return getAbsX() >= 3136 && getAbsX() <= 3327 && getAbsY() >= 3519
				&& getAbsY() <= 3607 || getAbsX() >= 3190 && getAbsX() <= 3327
				&& getAbsY() >= 3648 && getAbsY() <= 3839 || getAbsX() >= 3200
				&& getAbsX() <= 3390 && getAbsY() >= 3840 && getAbsY() <= 3967
				|| getAbsX() >= 2992 && getAbsX() <= 3007 && getAbsY() >= 3912
				&& getAbsY() <= 3967 || getAbsX() >= 2946 && getAbsX() <= 2959
				&& getAbsY() >= 3816 && getAbsY() <= 3831 || getAbsX() >= 3008
				&& getAbsX() <= 3199 && getAbsY() >= 3856 && getAbsY() <= 3903
				|| getAbsX() >= 3008 && getAbsX() <= 3071 && getAbsY() >= 3600
				&& getAbsY() <= 3711 || getAbsX() >= 3072 && getAbsX() <= 3327
				&& getAbsY() >= 3608 && getAbsY() <= 3647 || getAbsX() >= 2624
				&& getAbsX() <= 2690 && getAbsY() >= 2550 && getAbsY() <= 2619
				|| getAbsX() >= 2371 && getAbsX() <= 2422 && getAbsY() >= 5062
				&& getAbsY() <= 5117 || getAbsX() >= 2896 && getAbsX() <= 2927
				&& getAbsY() >= 3595 && getAbsY() <= 3630 || getAbsX() >= 2892
				&& getAbsX() <= 2932 && getAbsY() >= 4435 && getAbsY() <= 4464
				|| getAbsX() >= 2695 && getAbsX() <= 2745 && getAbsY() >= 9800
				&& getAbsY() <= 9838 || getAbsX() >= 2256 && getAbsX() <= 2287
				&& getAbsY() >= 4680 && getAbsY() <= 4711 || getAbsX() >= 2500
				&& getAbsX() <= 2535 && getAbsY() >= 4625 && getAbsY() <= 4660
				|| getAbsX() >= 2830 && getAbsX() <= 2870 && getAbsY() >= 3795
				&& getAbsY() <= 3840 || getAbsX() >= 2685 && getAbsX() <= 2815
				&& getAbsY() >= 9090 && getAbsY() <= 9155 || getAbsX() >= 3255
				&& getAbsX() <= 3298 && getAbsY() >= 2924 && getAbsY() <= 2947
				|| getAbsX() >= 2350 && getAbsX() <= 2450 && getAbsY() >= 3050
				&& getAbsY() <= 3150 || getAbsX() >= 2690 && getAbsX() <= 2720
				&& getAbsY() >= 3701 && getAbsY() <= 3730

				||

				getAbsX() >= 2570 && getAbsX() <= 2674 && getAbsY() >= 4627
				&& getAbsY() <= 4800

		;
	}

	public boolean neitiznot() {
		return getAbsX() > 2307 && getAbsX() < 2370 && getAbsY() > 3780
				&& getAbsY() < 3868;
	}

	public abstract int rangeAttack();

	public abstract int rangeDefense();

	/**
	 * Resets teleport target
	 */
	public void resetTeleportTarget() {
		teleportToX = -1;
		teleportToY = -1;
	}

	/**
	 * @param absX
	 *            the absX to set
	 */
	public void setAbsX(int absX) {
		this.absX = absX;
	}

	/**
	 * @param absY
	 *            the absY to set
	 */
	public void setAbsY(int absY) {
		this.absY = absY;
	}

	public void setAttackHolder(Entity e) {
		if (e == null)
			attackerHolder = null;

		attackerHolder = new WeakReference<Entity>(e);
	}

	public void setBusy(boolean type) {
		isBusy = type;
	}

	public void setBusyTimer(int newTime) {
		busyTimer = newTime;
		isBusy = newTime > 0;
	}

	public void setCharged(boolean type) {
		charged = type;
	}

	/**
	 * @param combatDelay
	 *            . the combatDelay to set.
	 */
	public int setCombatDelay(int newDelay) {
		return attackDelay = newDelay;
	}

	/**
	 * @param CombatType
	 *            . the combatType to set.
	 */
	public CombatType setCombatType(CombatType newType) {
		return combatType = newType;
	}

	public void setCoordinates(Location position) {
		setPosition(position);
	}

	public void setDeadLock(int ticks) {
		deadLock = ticks;
	}

	public void setDirections(Direction primaryDirection,
			Direction secondaryDirection) {
		this.primaryDirection = primaryDirection;
		this.secondaryDirection = secondaryDirection;
	}

	public void setFollowing(Entity following) {
		if (following == null) {
			this.following = null;
			return;
		}

		this.following = new WeakReference<Entity>(following);
	}

	/**
	 * @param freezeDelay
	 *            the freezeDelay to set
	 */
	public void setFreezeDelay(int freezeDelay) {
		this.freezeDelay = freezeDelay;
	}

	public void setHasHit(boolean hasHit) {
		this.hasHit = hasHit;
	}

	public void setHasHitTarget(int slot, boolean val) {
		hasHitTarget[slot] = val;
	}

	/**
	 * @param heightLevel
	 *            the heightLevel to set
	 */
	public void setHeightLevel(int heightLevel) {
		this.heightLevel = heightLevel;
	}

	/**
	 * @param holdingSpell
	 *            the holdingSpell to set
	 */
	public void setHoldingSpell(int holdingSpell) {
		this.holdingSpell = holdingSpell;
	}

	/**
	 * @param holdingSpellDelay
	 *            the holdingSpellDelay to set
	 */
	public void setHoldingSpellDelay(int holdingSpellDelay) {
		this.holdingSpellDelay = holdingSpellDelay;
	}

	/**
	 * @param holdingSpellTarget
	 *            the holdingSpellTarget to set
	 */
	public void setHoldingSpellTarget(Entity holdingSpellTarget) {
		if (holdingSpellTarget == null)
			this.holdingSpellTarget = null;

		this.holdingSpellTarget = new WeakReference<Entity>(holdingSpellTarget);
	}

	public void setInCombatTimer(int i) {
		inCombatWithTimer = 0;
	}

	/**
	 * @param inCombatWith
	 *            the inCombatWith to set
	 */
	public void setInCombatWith(Entity inCombatWith) {
		if (inCombatWith == null)
			this.inCombatWith = null;

		this.inCombatWith = new WeakReference<Entity>(inCombatWith);
	}

	/**
	 * @param inCombatWithTimer
	 *            the inCombatWithTimer to set
	 */
	public void setInCombatWithTimer() {
		if (getInCombatWith() instanceof Player && this instanceof Player) {
			if (((Player) this).getWildernessLevel() <= 8)
				inCombatWithTimer = 20;
			else
				inCombatWithTimer = 13;
		} else
			inCombatWithTimer = 10;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setInvulnerability(int ticks) {
		invulnerability = ticks;
	}

	public void setLastKnownRegion(Location lastKnownRegion) {
		this.lastKnownRegion = lastKnownRegion;
	}

	public void setMultiHitDelay(int multiHitDelay) {
		this.multiHitDelay = multiHitDelay;
	}

	public void setMultiList(Entity ent, int slot) {
		multiList[slot] = new WeakReference<Entity>(ent);
	}

	public void setOwner(Entity e) {
		if (e == null)
			owner = null;

		owner = new WeakReference<Entity>(e);
	}

	public void setPosition(Location position) {
		this.position = position;
		setAbsX(position.getX());
		setAbsY(position.getY());
		// this.setHeightLevel(position.getZ());
	}

	public void setRetaliateDelay(int retaliateDelay) {
		this.retaliateDelay = retaliateDelay;
	}

	public void setTarget(Entity id) {
		if (id == null) {
			target = null;
			return;
		}
		target = new WeakReference<Entity>(id);
	}

	public int setTeleportCordinate(int slot, int cord) {
		return teleportTo[slot] = cord;
	}

	public void setUsingSpecial(boolean newType) {
		usingSpecial = newType;
	}

	public void setVengTimer() {
		vengTimer = 60;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setWalkingHome(boolean value) {
		walkingHome = value;
	}

	public boolean usingSpecial() {
		return usingSpecial;
	}

	public boolean walkingHome() {
		return walkingHome;
	}

	public boolean withinDistance(Entity other) {
		if (position.getZ() != other.getPosition().getZ())
			return false;
		final int deltaX = other.getPosition().getX() - position.getX();
		final int deltaY = other.getPosition().getY() - position.getY();
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}
}