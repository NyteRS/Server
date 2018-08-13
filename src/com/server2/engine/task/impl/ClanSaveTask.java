package com.server2.engine.task.impl;

import java.io.FileNotFoundException;

import com.server2.Constants;
import com.server2.GameEngine;
import com.server2.engine.task.Task;
import com.server2.util.XStreamLibrary;
import com.server2.world.Clan;

/**
 * A task that saves updated clans.
 * 
 * @author Rene
 * 
 */
public class ClanSaveTask implements Task {

	@Override
	public void execute(GameEngine context) {
		context.submitWork(new Runnable() {
			@Override
			public void run() {
				for (final Clan clan : Clan.getClans().values())
					if (clan.isSaveRequired()) {
						try {
							XStreamLibrary.save(Constants.DATA_DIRECTORY
									+ "clans/" + clan.getOwner() + ".xml", clan);
						} catch (final FileNotFoundException e) {
							e.printStackTrace();
						}
						clan.setSaveRequired(false);
					}
			}
		});
	}

}