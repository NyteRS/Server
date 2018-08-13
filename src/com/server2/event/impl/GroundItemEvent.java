package com.server2.event.impl;

import com.server2.Settings;
import com.server2.event.Event;
import com.server2.world.GroundItemManager;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class GroundItemEvent extends Event {

	public GroundItemEvent() {
		super(Settings.getLong("sv_cyclerate"));
	}

	@Override
	public void execute() {
		GroundItemManager.getInstance().tick();
	}

}
