package com.server2.model.entity;

/**
 * 
 * @author Rene Represents A Single Graphic
 */

public class Graphics {

	/**
	 * The ID of the graphic
	 */
	private int ID;

	/**
	 * The owner of this graphic
	 */
	private Entity entity;

	/**
	 * The ticks that are left before this graphic will be processed
	 */
	private int ticks;

	/**
	 * The graphics height
	 */
	private boolean tallGfx;

	/**
	 * Constructs a graphic
	 * 
	 * @param ID
	 * @param entity
	 * @param ticks
	 */
	public Graphics(int ID, Entity entity, int ticks, boolean tallGfx) {
		this.ID = ID;
		this.entity = entity;
		this.ticks = ticks;
		this.tallGfx = tallGfx;
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
	 * @return the tallGfx
	 */
	public boolean isTallGfx() {
		return tallGfx;
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

	/**
	 * @param tallGfx
	 *            the tallGfx to set
	 */
	public void setTallGfx(boolean tallGfx) {
		this.tallGfx = tallGfx;
	}

}
