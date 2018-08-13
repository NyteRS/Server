package com.server2.event.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.server2.InstanceDistributor;
import com.server2.Settings;
import com.server2.content.anticheat.XLogPrevention;
import com.server2.engine.task.ConsecutiveTask;
import com.server2.engine.task.ParallelTask;
import com.server2.engine.task.Task;
import com.server2.event.Event;
import com.server2.model.combat.HitProcessor;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.world.PlayerManager;
import com.server2.world.World;

/**
 * An event which starts player update tasks.
 * 
 * @author Graham Edgecombe
 * 
 */
public class UpdateEvent extends Event {
	public UpdateEvent() {
		super(Settings.getLong("sv_cyclerate"));
	}

	@Override
	public void execute() {
		HitProcessor.process();
		final List<Task> tickTasks = new ArrayList<Task>();
		final List<Task> updateTasks = new ArrayList<Task>();
		final List<Task> resetTasks = new ArrayList<Task>();
		final Map<Integer, NPC> npcs = InstanceDistributor.getNPCManager()
				.getNPCMap();

		for (final NPC npc : npcs.values()) {
			tickTasks.add(npc.getTickTask());
			resetTasks.add(npc.getResetTask());
		}

		final PlayerManager manager = PlayerManager.getSingleton();

		for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
			if (manager.getPlayers()[i] == null)
				continue;

			final Player player = manager.getPlayers()[i];

			if ((!player.getChannel().isConnected() || player.disconnected)
					&& XLogPrevention.canLogout(player))
				World.getWorld().unregister(player);
			else {
				tickTasks.add(player.getTickTask());
				updateTasks.add(player.getUpdateTask());
				resetTasks.add(player.getResetTask());
			}
		}

		final Task tickTask = new ParallelTask(tickTasks.toArray(new Task[0]));
		final Task updateTask = new ParallelTask(
				updateTasks.toArray(new Task[0]));
		final Task resetTask = new ParallelTask(resetTasks.toArray(new Task[0]));

		World.getWorld().submit(
				new ConsecutiveTask(tickTask, updateTask, resetTask));
	}

}