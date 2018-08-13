package com.server2.engine.task;

import com.server2.GameEngine;

/**
 * A task is a class which carries out a unit of work.
 * 
 * @author Rene
 * 
 */
public interface Task {

	/**
	 * Executes the task. The general contract of the execute method is that it
	 * may take any action whatsoever.
	 * 
	 * @param context
	 *            The game engine this task is being executed in.
	 */
	public void execute(GameEngine context);

}
