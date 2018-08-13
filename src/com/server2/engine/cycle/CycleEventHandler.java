package com.server2.engine.cycle;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import com.server2.model.entity.Entity;

/**
 * Handles all of our cycle based events
 * 
 * @author Stuart <RogueX>
 * 
 */
public class CycleEventHandler {

	/**
	 * The instance of this class
	 */
	private static CycleEventHandler instance;

	/**
	 * Returns the instance of this class
	 * 
	 * @return
	 */
	public static CycleEventHandler getInstance() {
		if (CycleEventHandler.instance == null)
			CycleEventHandler.instance = new CycleEventHandler();
		return CycleEventHandler.instance;
	}

	/**
	 * Holds all of our events currently being ran
	 */
	private final Queue<CycleEventContainer> events;

	private final Queue<CycleEventContainer> eventsToAdd;

	/**
	 * Creates a new instance of this class
	 */
	public CycleEventHandler() {
		events = new LinkedList<CycleEventContainer>();
		eventsToAdd = new LinkedList<CycleEventContainer>();
	}

	/**
	 * Add an event to the list
	 * 
	 * @param owner
	 * @param event
	 * @param cycles
	 * @return the container
	 */
	public CycleEventContainer add1Event(Entity owner, CycleEvent event,
			int cycles) {
		final CycleEventContainer container = new CycleEventContainer(owner,
				event, cycles);
		if (owner.getIndex() == -1)
			return container;
		eventsToAdd.add(container);
		return container;
	}

	/**
	 * Returns the amount of events currently running
	 * 
	 * @return amount
	 */
	public int getEventsCount() {
		return events.size();
	}

	/**
	 * Resets all current events
	 */
	public void reset() {
		events.clear();
		eventsToAdd.clear();
		CycleEventHandler.instance = new CycleEventHandler();
	}

	/**
	 * Stops all events which are being ran by the given owner
	 * 
	 * @param owner
	 */

	public void stopEvents(Entity owner) {
		for (final CycleEventContainer c : events)
			if (c != null)
				if (c.getOwner() == owner)
					c.stop();
	}

	/**
	 * Execute and remove events
	 */
	public void tick() {
		// List<CycleEventContainer> remove = new
		// ArrayList<CycleEventContainer>();
		events.addAll(eventsToAdd);
		eventsToAdd.clear();
		for (final Iterator<CycleEventContainer> cycleEvents = events
				.iterator(); cycleEvents.hasNext();) {
			final CycleEventContainer c = cycleEvents.next();
			if (c != null)
				if (c.getOwner() == null || c.getOwner().getIndex() == -1)
					cycleEvents.remove();
				else {
					if (c.needsExecution())
						c.execute();
					if (!c.isRunning())
						cycleEvents.remove();
				}
		}
	}

}