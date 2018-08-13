package com.server2.engine.cycle.impl;

import com.server2.Server;
import com.server2.Settings;
import com.server2.model.entity.player.Player;
import com.server2.world.PlayerManager;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class GameSavingProcess implements Runnable {

	@Override
	public void run() {
		while (true) {
			Server.setMinutesCounter(Server.getMinutesCounter() + 1);
			for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
				final Player thePlayer = PlayerManager.getSingleton()
						.getPlayers()[i];
				if (thePlayer == null || thePlayer.disconnected)
					continue;

				final Player player = thePlayer;
				player.getAllotment().doCalculations();
				player.getFlowers().doCalculations();
				player.getHerbs().doCalculations();
				player.getHops().doCalculations();
				player.getBushes().doCalculations();
				player.getTrees().doCalculations();
				player.getFruitTrees().doCalculations();
				player.getSpecialPlantOne().doCalculations();
				player.getSpecialPlantTwo().doCalculations();
				// PlayerSaving.savePlayer(player);
			}
			try {
				Thread.sleep(30000);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}
}
