package com.server2.model.entity.player;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.entity.Entity;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class PlayerEventHandler {

	/**
	 * Default client
	 */
	private final Entity client;

	/**
	 * The events this player has running
	 */
	private final Queue<CycleEventContainer> events = new LinkedList<CycleEventContainer>();

	/**
	 * The events to add for next tick
	 */
	private final Queue<CycleEventContainer> eventsToAdd = new LinkedList<CycleEventContainer>();;

	/**
	 * Reconstructs the class for a new player
	 */
	public PlayerEventHandler(Entity client) {
		this.client = client;
	}

	/**
	 * Add an event
	 * 
	 * @param owner
	 * @param event
	 * @param cycles
	 */
	public CycleEventContainer addEvent(CycleEvent event, int cycles) {
		final CycleEventContainer container = new CycleEventContainer(client,
				event, cycles);
		if (client.getIndex() == -1)
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
		if (eventsToAdd.size() > 0) {
			events.addAll(eventsToAdd);
			eventsToAdd.clear();
		}
		for (final Iterator<CycleEventContainer> cycleEvents = events
				.iterator(); cycleEvents.hasNext();) {
			final CycleEventContainer c = cycleEvents.next();
			if (c != null)
				if (c.getOwner() == null
						|| ((Player) c.getOwner()).disconnected)
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
