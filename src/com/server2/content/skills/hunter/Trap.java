package com.server2.content.skills.hunter;

import com.server2.world.objects.GameObject;

/**
 * 
 * @author Rene
 */
public class Trap {

	/**
	 * The possible states a trap can be in
	 */
	public static enum TrapState {

		SET, FALLEN, CAUGHT, BAITING
	}

	/**
	 * The WorldObject linked to this HunterObject
	 */
	private GameObject gameObject;

	/**
	 * Whether the trap is baited or not
	 */
	private boolean baited;

	/**
	 * The amount of ticks this object should stay for
	 */
	private int ticks;
	/**
	 * This trap's state
	 */
	private TrapState trapState;

	/**
	 * Reconstructs a new Trap
	 * 
	 * @param object
	 * @param state
	 */
	public Trap(GameObject object, TrapState state, int ticks) {
		gameObject = object;
		trapState = state;
		this.ticks = ticks;
		baited = false;
	}

	/**
	 * Gets the GameObject
	 */
	public GameObject getGameObject() {
		return gameObject;
	}

	/**
	 * @return the ticks
	 */
	public int getTicks() {
		return ticks;
	}

	/**
	 * Gets a trap's state
	 */
	public TrapState getTrapState() {
		return trapState;
	}

	/**
	 * @return the baited
	 */
	public boolean isBaited() {
		return baited;
	}

	/**
	 * @param baited
	 *            the baited to set
	 */
	public void setBaited(boolean baited) {
		this.baited = baited;
	}

	/**
	 * Sets the GameObject
	 * 
	 * @param gameObject
	 */
	public void setGameObject(GameObject gameObject) {
		this.gameObject = gameObject;
	}

	/**
	 * @param ticks
	 *            the ticks to set
	 */
	public void setTicks(int ticks) {
		this.ticks = ticks;
	}

	/**
	 * Sets a trap's state
	 * 
	 * @param state
	 */
	public void setTrapState(TrapState state) {
		trapState = state;
	}
}
