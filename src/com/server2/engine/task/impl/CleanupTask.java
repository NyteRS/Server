package com.server2.engine.task.impl;

import com.server2.GameEngine;
import com.server2.Settings;
import com.server2.event.Event;
import com.server2.world.World;

/**
 * Performs garbage collection and finalization.
 * 
 * @author Graham Edgecombe
 * 
 */
public class CleanupTask extends Event {

	public CleanupTask() {
		super(Settings.getLong("sv_cyclerate") * 500);
	}

	@Override
	public void execute() {
		final GameEngine context = World.getWorld().getEngine();
		context.submitWork(new Runnable() {
			@Override
			public void run() {
				System.gc();
				System.runFinalization();
			}
		});

	}

}