package com.server2.event.impl;

import com.server2.Settings;
import com.server2.content.minigames.FightPits;
import com.server2.event.Event;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class FightPitsEvent extends Event {

	public FightPitsEvent() {
		super(Settings.getLong("sv_cyclerate"));
	}

	@Override
	public void execute() {
		FightPits.tick();

	}

}
