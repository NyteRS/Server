package com.server2.content.minigames.pc;

import com.server2.event.Event;

/**
 * The PestControl tick which is called every 600ms.
 * 
 * @author Ultimate1
 * 
 */

public class PestControlEvent extends Event {

	public PestControlEvent() {
		super(600);
	}

	@Override
	public void execute() {
		PestControl.getInstance().tick();
	}

}