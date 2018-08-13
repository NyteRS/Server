package com.server2.model.entity;

/**
 * 
 * @author Rene Represents a single Animation
 */
public class Animation {

	/**
	 * The ID of the Animation
	 */
	private int ID;

	/**
	 * The owner of this Animation
	 */
	private Entity entity;

	/**
	 * The ticks that are left before this Animation will be processed
	 */
	private int ticks;

	/**
	 * Constructs a graphic
	 * 
	 * @param ID
	 * @param entity
	 * @param ticks
	 */
	public Animation(int ID, Entity entity, int ticks) {
		this.ID = ID;
		this.entity = entity;
		this.ticks = ticks;
	}

	/**
	 * @return the ticks
	 */
	public int getDelay() {
		return ticks;
	}

	/**
	 * @return the entity
	 */
	public Entity getEntity() {
		return entity;
	}

	/**
	 * @return the ID
	 */
	public int getID() {
		return ID;
	}

	/**
	 * @param ticks
	 *            the ticks to set
	 */
	public void setDelay(int ticks) {
		this.ticks = ticks;
	}

	/**
	 * @param entity
	 *            the entity to set
	 */
	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	/**
	 * @param ID
	 *            the ID to set
	 */
	public void setID(int ID) {
		this.ID = ID;
	}

}
