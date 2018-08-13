package com.server2.content.misc;

import com.server2.content.Achievements;
import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene Roosen A class which handles god capes.
 */
public class GodCapes {

	/**
	 * Gives the cape to the player, also checks if it doesn't already have one.
	 * 
	 * @param client
	 * @param whichCape
	 */
	public static void giveGodCape(final Player client, final int whichCape) {
		if (System.currentTimeMillis() - client.lastCape < 3500)
			return;
		client.lastCape = System.currentTimeMillis();
		AnimationProcessor.addNewRequest(client, 645, 0);

		client.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (whichCape == 1) {
					client.getActionSender()
							.sendMessage(
									"Saradomin has heard your prayers, and rewards you with a cape!");
					client.getActionSender().addItem(2412, 1);
					Achievements.getInstance().complete(client, 39);
				} else if (whichCape == 2) {
					client.getActionSender()
							.sendMessage(
									"Zamorak has heard your prayers, and rewards you with a cape!");
					client.getActionSender().addItem(2414, 1);
					Achievements.getInstance().complete(client, 39);
				} else if (whichCape == 3) {
					client.getActionSender()
							.sendMessage(
									"Guthix has heard your prayers, and rewards you with a cape!");
					client.getActionSender().addItem(2413, 1);
					Achievements.getInstance().complete(client, 39);
				}
				container.stop();
			}

			@Override
			public void stop() {
				// TODO Auto-generated method stub

			}
		}, 5);

	}
}
