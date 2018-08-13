package com.server2.engine.task.impl;

import com.server2.GameEngine;
import com.server2.engine.task.Task;
import com.server2.model.combat.CombatEngine;
import com.server2.model.entity.npc.NPC;

/**
 * A task which performs pre-update tasks for an NPC.
 * 
 * @author Rene
 * 
 */

public class NPCTickTask implements Task {

	private final NPC npc;

	public NPCTickTask(NPC npc) {
		this.npc = npc;
	}

	@Override
	public void execute(GameEngine context) {
		npc.process();
		CombatEngine.mainProcessor(npc);
	}

}