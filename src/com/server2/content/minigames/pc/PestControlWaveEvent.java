package com.server2.content.minigames.pc;

import java.util.Queue;

import com.server2.event.Event;

/**
 * An event that handles the spawning of a new wave.
 * 
 * @author Ultimate1
 * 
 */

public class PestControlWaveEvent extends Event {

	private final Queue<PestControlWave> waves;

	public PestControlWaveEvent() {
		super(30000);
		waves = PestControl.getInstance().getWaves();
	}

	@Override
	public void execute() {
		final PestControlWave wave = waves.poll();
		if (wave == null)
			stop();
		else {
			wave.spawn();
			setDelay(wave.getDelayUntilNextWave());
		}
	}

}