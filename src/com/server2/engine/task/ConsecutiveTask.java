package com.server2.engine.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.server2.GameEngine;

/**
 * A task which executes a group of tasks in a guaranteed sequence.
 * 
 * @author Rene
 * 
 */
public class ConsecutiveTask implements Task {

	/**
	 * The tasks.
	 */
	private final Collection<Task> tasks;

	/**
	 * Creates the consecutive task.
	 * 
	 * @param tasks
	 *            The child tasks to execute.
	 */
	public ConsecutiveTask(Task... tasks) {
		final List<Task> taskList = new ArrayList<Task>();
		for (final Task task : tasks)
			taskList.add(task);
		this.tasks = Collections.unmodifiableCollection(taskList);
	}

	@Override
	public void execute(GameEngine context) {
		for (final Task task : tasks)
			task.execute(context);
	}

}
