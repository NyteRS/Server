package com.server2.event.impl;

//import com.server2.content.minigames.CastleWars;
import com.server2.Settings;
import com.server2.event.Event;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class CastlewarsEvent extends Event {

	public CastlewarsEvent() {
		super(Settings.getLong("sv_cyclerate"));
	}

	@Override
	public void execute() {
		// CastleWars.getInstance().init();
	}

}
