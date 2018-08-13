package com.server2.engine.cycle;

import com.server2.model.entity.Entity;

/**
 * The wrapper for our event
 * 
 * @author Stuart <RogueX>
 * 
 */

public class CycleEventContainer {

	/**
	 * Event owner
	 */
	private final Entity owner;

	/**
	 * Is the event running or not
	 */
	private boolean isRunning;

	/**
	 * The amount of cycles per event execution
	 */
	private int tick;

	/**
	 * The actual event
	 */
	private final CycleEvent event;

	/**
	 * The current amount of cycles passed
	 */
	private int cyclesPassed;

	/**
	 * Sets the event containers details
	 * 
	 * @param owner
	 *            , the owner of the event
	 * @param event
	 *            , the actual event to run
	 * @param tick
	 *            , the cycles between execution of the event
	 */
	public CycleEventContainer(Entity owner, CycleEvent event, int tick) {
		this.owner = owner;
		this.event = event;
		isRunning = true;
		cyclesPassed = 0;
		this.tick = tick;
	}

	/**
	 * Execute the contents of the event
	 */
	public void execute() {
		if (this != null && event != null)
			event.execute(this);
		else {
			isRunning = false;
			event.stop();
		}
	}

	/**
	 * Returns the owner of the event
	 * 
	 * @return
	 */
	public Entity getOwner() {
		return owner;
	}

	/**
	 * Is the event running?
	 * 
	 * @return true yes false no
	 */
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * Does the event need to be ran?
	 * 
	 * @return true yes false no
	 */
	public boolean needsExecution() {
		if (++cyclesPassed >= tick) {
			cyclesPassed = 0;
			return true;
		}
		return false;
	}

	/**
	 * Set the amount of cycles between the execution
	 * 
	 * @param tick
	 */
	public void setTick(int tick) {
		this.tick = tick;
	}

	/**
	 * Stop the event from running
	 */
	public void stop() {
		isRunning = false;
		event.stop();
	}

}