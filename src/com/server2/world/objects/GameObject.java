package com.server2.world.objects;

import com.server2.model.entity.Location;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Faris
 */
public class GameObject {

	public enum Face {
		/**
		 * Has the face value 0
		 */
		WEST,
		/**
		 * Has the face value -1
		 */
		NORTH,
		/**
		 * Has the face value -2
		 */
		EAST,
		/**
		 * Has the face value -3
		 */
		SOUTH
	}

	/**
	 * Integer associated with scheduling the removal of the object
	 */
	public static final int SET_DUE_REMOVAL = 0;

	/**
	 * The location of the object
	 */
	private final Location location;

	/**
	 * The id of the object
	 */
	private int objectId;

	/**
	 * The replacement id of the object
	 */
	private final int replacementId;

	/**
	 * The creator of the object
	 */
	private final Player owner;

	/**
	 * How long the object will exist
	 */
	private int lifeCycle;

	/**
	 * Used to flag whether the object is spawned publicly
	 */
	public boolean publicObject;

	/**
	 * Stores the given face direction of the object
	 */
	private final int faceDirection;

	/**
	 * Stores the given object type
	 */
	private final int objectType;

	/**
	 * Does this object need to be removed?
	 */
	private boolean needsRemoval;

	/**
	 * Constructs a new game object
	 * 
	 * @param objectID
	 * @param objectX
	 * @param objectY
	 * @param objectHeight
	 * @param objectFace
	 * @param objectType
	 */
	public GameObject(int objectID, int objectX, int objectY, int objectHeight,
			Face objectFace, int objectType) {
		location = new Location(objectX, objectY, objectHeight);
		objectId = objectID;
		replacementId = -1;
		lifeCycle = -1;
		owner = null;
		faceDirection = getObjectFace(objectFace);
		this.objectType = objectType;
		needsRemoval = false;
	}

	/**
	 * Resubmits the instance with the correct syntax to initialize the fiels
	 */
	public GameObject(int objectID, Location position, Face objectFace,
			int objectType) {
		this(objectID, position.getX(), position.getY(), position.getZ(),
				objectFace, objectType);
	}

	/**
	 * Constructs a new game object
	 * 
	 * @param location
	 * @param objectId
	 * @param replacementId
	 */
	public GameObject(Location location, int objectId, int replacementId,
			int lifeCycle, int faceDirection, int objectType, Player owner) {
		this.location = location;
		this.objectId = objectId;
		this.replacementId = replacementId;
		this.faceDirection = faceDirection;
		this.lifeCycle = lifeCycle;
		this.owner = owner;
		this.objectType = objectType;
		needsRemoval = false;
	}

	/**
	 * Decrements the object life cycle
	 */
	public void decreaseLifeCycle() {
		lifeCycle--;
	}

	/**
	 * @return the faceDirection
	 */
	public int getFaceDirection() {
		return faceDirection;
	}

	/**
	 * Returns how long the object will exist
	 * 
	 * @return
	 */
	public int getLifeCycle() {
		return lifeCycle;
	}

	/**
	 * Returns the objects location
	 * 
	 * @return
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Returns the objects facing direction as dictated by the face object
	 * 
	 * @param face
	 *            is the pre assigned face value
	 * @return the integer linked to the respective face value
	 */
	public int getObjectFace(Face face) {
		if (face == Face.SOUTH)
			return -3;
		if (face == Face.EAST)
			return -2;
		if (face == Face.NORTH)
			return -1;
		return 0;
	}

	/**
	 * Returns the objects id
	 * 
	 * @return
	 */
	public int getObjectId() {
		return objectId;
	}

	/**
	 * @return the objectType
	 */
	public int getObjectType() {
		return objectType;
	}

	/**
	 * @return the owner
	 */
	public Player getOwner() {
		return owner;
	}

	/**
	 * Returns the replacement object id
	 * 
	 * @return
	 */
	public int getReplacementId() {
		return replacementId;
	}

	/**
	 * Does this object need to be removed?
	 * 
	 * @return
	 */
	public boolean needsRemoval() {
		return needsRemoval;
	}

	/**
	 * Sets the objects lifeCycle to an integer which will schedule it for
	 * removal in the object manager
	 */
	public void scheduleRemoval() {
		lifeCycle = SET_DUE_REMOVAL;
		ObjectManager.removeObject(this);
	}

	/**
	 * Set this object to be deleted
	 */
	public void setNeedsRemoval(boolean needsRemoval) {
		this.needsRemoval = needsRemoval;
	}

	/**
	 * Sets the object ID
	 * 
	 * @param id
	 */
	public void setObjectId(int id) {
		objectId = id;
	}

}