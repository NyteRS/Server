package com.server2.engine.task.impl;

import com.server2.GameEngine;
import com.server2.engine.task.Task;
import com.server2.model.entity.npc.NPC;

/**
 * 
 * @author Rene
 * 
 */
public class NPCResetTask implements Task {

	private final NPC npc;

	public NPCResetTask(NPC npc) {
		this.npc = npc;
	}

	@Override
	public void execute(GameEngine context) {
		npc.clearUpdateFlags();
		npc.direction = -1;
	}

}