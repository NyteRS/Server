package com.server2.engine.cycle;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.server2.Settings;

/**
 * A class which schedules the execution of {@link Tickable}s.
 * 
 * @author Graham
 */
public final class TickManager implements Runnable {

	/**
	 * A logger used to report error messages.
	 */
	private static final Logger logger = Logger.getLogger(TickManager.class
			.getName());

	/**
	 * The time period, in milliseconds, of a single cycle.
	 */
	private static final int TIME_PERIOD = (int) Settings
			.getLong("sv_cyclerate");

	/**
	 * The {@link ScheduledExecutorService} which schedules calls to the
	 * {@link #run()} method.
	 */
	private final ScheduledExecutorService service = Executors
			.newSingleThreadScheduledExecutor();

	/**
	 * A list of active Ticks.
	 */
	private final List<Tickable> Ticks = new ArrayList<Tickable>();

	/**
	 * A queue of Ticks that still need to be added.
	 */
	private final Queue<Tickable> newTicks = new ArrayDeque<Tickable>();

	/**
	 * Creates and starts the Tick scheduler.
	 */
	public TickManager() {
		service.scheduleAtFixedRate(this, 0, TIME_PERIOD, TimeUnit.MILLISECONDS);
	}

	/**
	 * This method is automatically called every cycle by the
	 * {@link ScheduledExecutorService} and executes, adds and removes
	 * {@link Tickable}s. It should not be called directly as this will lead to
	 * concurrency issues and inaccurate time-keeping.
	 */
	@Override
	public void run() {
		synchronized (newTicks) {
			Tickable Tick;
			while ((Tick = newTicks.poll()) != null)
				Ticks.add(Tick);
		}

		for (final Iterator<Tickable> it = Ticks.iterator(); it.hasNext();) {
			final Tickable Tick = it.next();
			try {
				if (!Tick.tick())
					it.remove();
			} catch (final Throwable t) {
				logger.log(Level.SEVERE, "Exception during Tick execution.", t);
				t.printStackTrace();
			}
		}
	}

	/**
	 * Schedules the specified Tick. If this scheduler has been stopped with the
	 * {@link #terminate()} method the Tick will not be executed or
	 * garbage-collected.
	 * 
	 * @param Tick
	 *            The Tick to schedule.
	 */
	public void submit(final Tickable Tick) {
		if (Tick.isImmediate())
			service.execute(new Runnable() {
				@Override
				public void run() {
					Tick.execute();
				}
			});

		synchronized (newTicks) {
			newTicks.add(Tick);
		}
	}

	/**
	 * Stops the Tick scheduler.
	 */
	public void terminate() {
		service.shutdown();
	}

}