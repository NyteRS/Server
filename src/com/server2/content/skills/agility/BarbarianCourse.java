package com.server2.content.skills.agility;

import com.server2.model.entity.player.Player;

/**
 * 
 * @author Jordon Barber Handles the Barbarian Course used to train Agility.
 * 
 */

public class BarbarianCourse {

	private static final int LOGBALANCE = 762;
	private static final int CLIMBNET = 828;
	private static final int CLIMBDOWN = 827;
	private static final int LEDGEBALANCE = 756;
	private static final int CLIMBWALL = 839;

	public static void performAction(Player client, int objectID) {
		switch (objectID) {
		case 2294: // Barbarian Log Balance
			if (client.absX == 2551 && client.absY == 3546)
				client.getActionAssistant().startAnimation(LOGBALANCE);
			break;
		case 2284: // Barbarian Obstacle net
			client.getActionAssistant().startAnimation(CLIMBNET);
			break;
		case 2302: // Barbarian Balancing ledge
			client.getActionAssistant().startAnimation(LEDGEBALANCE);
			break;
		case 3205: // Ladder
			client.getActionAssistant().startAnimation(CLIMBDOWN);
			break;
		case 1948: // Crumbling Walls
			client.getActionAssistant().startAnimation(CLIMBWALL);
			break;

		}
	}

}
