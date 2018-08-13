package com.server2.model.entity.player;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.server2.model.entity.GroundItem;
import com.server2.model.entity.Location;
import com.server2.util.Misc;

public class GroundItemDistribution {

	private final Player client;

	private int processedItems = 0;

	private int deletionQueueItems = 0;

	private final ConcurrentLinkedQueue<GroundItem> groundItemQueue = new ConcurrentLinkedQueue<GroundItem>();

	private final ConcurrentLinkedQueue<GroundItem> deletionQueue = new ConcurrentLinkedQueue<GroundItem>();

	public GroundItemDistribution(Player client) {
		this.client = client;
	}

	public ConcurrentLinkedQueue<GroundItem> getDeletionQueue() {
		return deletionQueue;
	}

	public ConcurrentLinkedQueue<GroundItem> getGroundItemQueue() {
		return groundItemQueue;
	}

	public void tick() { // Sends all queued grounditems with a limitation of 50
							// per gametick
		if (groundItemQueue.isEmpty() && deletionQueue.isEmpty())
			return;

		while (deletionQueue.peek() != null && deletionQueueItems < 30) {
			final GroundItem itemToProcess = deletionQueue.poll();
			if (itemToProcess != null) {
				final Location loc = new Location(client.getAbsX(),
						client.getAbsY(), client.getHeightLevel());
				if (Misc.goodDistance(loc, itemToProcess.getPosition(), 60))
					client.getActionSender().removeGroundItem(itemToProcess);
				deletionQueueItems++;
			}
		}

		while (groundItemQueue.peek() != null && processedItems < 30) {
			final GroundItem itemToProcess = groundItemQueue.poll();

			if (itemToProcess != null) {

				final Location loc = new Location(client.getAbsX(),
						client.getAbsY(), client.getHeightLevel());

				if (Misc.goodDistance(loc, itemToProcess.getPosition(), 60)) {// &&
																				// GroundItemManager.getInstance().itemExists(client,
																				// itemToProcess.getItem().getId(),
																				// itemToProcess.getPosition().getX(),
																				// itemToProcess.getPosition().getY()))
																				// {

					client.getActionSender().createGroundItem(itemToProcess);
					processedItems++;
				}
			}
		}

		deletionQueueItems = 0;
		processedItems = 0; // Wait for next tick
	}
}
