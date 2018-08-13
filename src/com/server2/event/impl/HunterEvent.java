package com.server2.event.impl;

import com.server2.content.skills.hunter.TrapExecution;
import com.server2.event.Event;

/**
 * 
 * @author Lukas Pinckers
 * 
 */
public class HunterEvent extends Event {

	public HunterEvent() {
		super(600);
	}

	@Override
	public void execute() {
		TrapExecution.tick();

	}

}
