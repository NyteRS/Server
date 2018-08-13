package com.server2.model;

import java.util.ArrayDeque;
import java.util.Deque;

import com.server2.model.entity.Direction;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Location;
import com.server2.model.entity.player.Player;

/**
 * A queue of {@link Direction}s which a {@link Character} will follow.
 * 
 * @author Graham Edgecombe
 */
public final class WalkingQueue {

	/**
	 * Represents a single point in the queue.
	 * 
	 * @author Graham Edgecombe
	 */
	private static final class Point {

		/**
		 * The point's position.
		 */
		private final Location position;

		/**
		 * The direction to walk to this point.
		 */
		private final Direction direction;

		/**
		 * Creates a point.
		 * 
		 * @param position
		 *            The position.
		 * @param direction
		 *            The direction.
		 */
		public Point(Location position, Direction direction) {
			this.position = position;
			this.direction = direction;
		}

		@Override
		public String toString() {
			return Point.class.getName() + " [direction=" + direction
					+ ", position=" + position + "]";
		}

	}

	/**
	 * The maximum size of the queue. If any additional steps are added, they
	 * are discarded.
	 */
	private static final int MAXIMUM_SIZE = 50;

	/**
	 * The character whose walking queue this is.
	 */
	private final Entity character;

	/**
	 * The queue of directions.
	 */
	private final Deque<Point> points = new ArrayDeque<Point>();

	/**
	 * Flag indicating if this queue (only) should be ran.
	 */
	private boolean runQueue;

	/**
	 * Run toggle (button in client).
	 */
	private boolean runToggled = true;

	/**
	 * Creates a walking queue for the specified character.
	 * 
	 * @param character
	 *            The character.
	 */
	public WalkingQueue(Entity character) {
		this.character = character;
	}

	/**
	 * Adds the first step to the queue, attempting to connect the server and
	 * client position by looking at the previous queue.
	 * 
	 * @param clientConnectionPosition
	 *            The first step.
	 * @return {@code true} if the queues could be connected correctly,
	 *         {@code false} if not.
	 */
	public boolean addFirstStep(Location clientConnectionPosition) {
		points.clear();
		addStep(clientConnectionPosition, true);
		return true;
	}

	/**
	 * Adds a step.
	 * 
	 * @param x
	 *            The x coordinate of this step.
	 * @param y
	 *            The y coordinate of this step.
	 * @param heightLevel
	 * @param flag
	 */
	private void addStep(int x, int y, int heightLevel, boolean flag) {
		if (points.size() >= MAXIMUM_SIZE)
			return;
		final Point last = getLast();

		final int deltaX = x - last.position.getX();
		final int deltaY = y - last.position.getY();

		final Direction direction = Direction.fromDeltas(deltaX, deltaY);
		if (direction != Direction.NONE) {
			final boolean checkStep = character instanceof Player;
			// TODO area checks for areas which do not support clipping
			boolean validStep = true;
			if (flag && checkStep)
				validStep = checkStep(last.position, deltaX, deltaY);
			if (validStep)
				points.add(new Point(new Location(x, y, heightLevel), direction));
			else {
				character.setDirections(Direction.NONE, Direction.NONE);
				return;
			}
		}
	}

	/**
	 * Adds a step to the queue.
	 * 
	 * @param step
	 *            The step to add.
	 * @oaram flag
	 */
	public void addStep(Location step, boolean flag) {
		final Point last = getLast();

		final int x = step.getX();
		final int y = step.getY();

		int deltaX = x - last.position.getX();
		int deltaY = y - last.position.getY();

		final int max = Math.max(Math.abs(deltaX), Math.abs(deltaY));
		for (int i = 0; i < max; i++) {
			if (deltaX < 0)
				deltaX++;
			else if (deltaX > 0)
				deltaX--;

			if (deltaY < 0)
				deltaY++;
			else if (deltaY > 0)
				deltaY--;

			addStep(x - deltaX, y - deltaY, step.getZ(), flag);
		}
	}

	/**
	 * Checks walk steps
	 * 
	 * @param position
	 * @param diffX
	 * @param diffY
	 * @return
	 */
	public boolean checkStep(Location position, int diffX, int diffY) {
		return /*
				 * Region.getClipping(position.getX(), position.getY(),
				 * character.getHeightLevel(), diffX, diffY)
				 */true; // Gives loads of issues
	}

	/**
	 * Gets the last point.
	 * 
	 * @return The last point.
	 */
	private Point getLast() {
		final Point last = points.peekLast();
		if (last == null)
			return new Point(character.getPosition(), Direction.NONE);
		return last;
	}

	/**
	 * @return true if the player is moving.
	 */
	public boolean isMoving() {
		return !points.isEmpty();
	}

	/**
	 * Checks if any running flag is set.
	 * 
	 * @return <code>true</code. if so, <code>false</code> if not.
	 */
	public boolean isRunning() {
		return runToggled || runQueue;
	}

	/**
	 * Gets the running queue flag.
	 * 
	 * @return The running queue flag.
	 */
	public boolean isRunningQueue() {
		return runQueue;
	}

	/**
	 * Gets the run toggled flag.
	 * 
	 * @return The run toggled flag.
	 */
	public boolean isRunningToggled() {
		return runToggled;
	}

	/**
	 * Called every pulse, updates the queue.
	 */
	public void pulse() {
		Location teleportPosition = null;
		if (character.teleportToX != -1 && character.teleportToY != -1)
			teleportPosition = new Location(character.teleportToX,
					character.teleportToY, character.heightLevel);
		if (teleportPosition != null) {
			reset();
			character.didTeleport = true;
			character.setPosition(teleportPosition);
			character.resetTeleportTarget();
		} else {
			Direction first = Direction.NONE;
			Direction second = Direction.NONE;
			Point next = points.poll();
			if (next != null) {
				first = next.direction;
				Location position = next.position;
				if (runQueue || runToggled) {
					next = points.poll();
					if (next != null) {
						second = next.direction;
						position = next.position;
					}
				}
				character.setPosition(position);
			}
			character.setDirections(first, second);
		}

		// Check for a region change.
		final int diffX = character.getPosition().getX()
				- character.getLastKnownRegion().getRegionX() * 8;
		final int diffY = character.getPosition().getY()
				- character.getLastKnownRegion().getRegionY() * 8;
		boolean changed = false;
		if (diffX < 16)
			changed = true;
		else if (diffX >= 88)
			changed = true;
		if (diffY < 16)
			changed = true;
		else if (diffY >= 88)
			changed = true;
		if (changed)
			character.mapRegionDidChange = true;
	}

	/**
	 * Resets the movement.
	 */
	public void reset() {
		reset(true);
	}

	/**
	 * Resets the movement.
	 * 
	 * @param removeMiniflag
	 */
	public void reset(boolean removeMiniflag) {
		points.clear();
	}

	/**
	 * Sets the running queue flag.
	 * 
	 * @param running
	 *            The running queue flag.
	 */
	public void setRunningQueue(boolean running) {
		runQueue = running;
	}

	/**
	 * Sets the run toggled flag.
	 * 
	 * @param runToggled
	 *            The run toggled flag.
	 */
	public void setRunningToggled(boolean runToggled) {
		this.runToggled = runToggled;
	}

	/**
	 * Gets the size of the queue.
	 * 
	 * @return The size of the queue.
	 */
	public int size() {
		return points.size();
	}

}