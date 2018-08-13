package com.server2.engine.task.impl;

import com.server2.GameEngine;
import com.server2.engine.task.Task;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene
 * 
 */
public class PlayerResetTask implements Task {

	private final Player player;

	public PlayerResetTask(Player player) {
		this.player = player;
	}

	@Override
	public void execute(GameEngine context) {
		player.clearUpdateFlags();
		player.didTeleport = false;
		player.mapRegionDidChange = false;
	}

}