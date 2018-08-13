package com.server2.model.combat;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class HitProcessor {

	/**
	 * All the QueuedHits go in here
	 */

	public static LinkedList<Hit> queuedHits = new LinkedList<Hit>();

	/**
	 * The Queue to add on the next tick
	 */
	public static ConcurrentLinkedQueue<Hit> hitsToAdd = new ConcurrentLinkedQueue<Hit>();

	/**
	 * Process all queued Hits.
	 */
	public static void process() {

		// Copy the hitsToAdd over
		queuedHits.addAll(hitsToAdd);

		// Delete the hitsToAdd
		hitsToAdd.clear();

		final LinkedList<Hit> tempList = new LinkedList<Hit>(queuedHits);

		for (final Hit hit : tempList) {
			if (hit == null)
				continue;

			if (hit.getDelay() > 0)
				hit.setDelay(hit.getDelay() - 1);
			if (hit.getDelay() <= 0) {
				// Execute the hit.
				HitExecutor.performHit(hit);
				// Remove it from the queue.
				queuedHits.remove(hit);
			}
		}
		tempList.clear();

	}

}
