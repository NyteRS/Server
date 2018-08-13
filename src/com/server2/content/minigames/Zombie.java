package com.server2.content.minigames;

import java.util.ArrayList;
import java.util.List;

import com.server2.model.entity.player.Player;

/**
 * 
 * @author Paymon
 * 
 */

public class Zombie {

	public int waveCount = 0;
	public int nextWave = 10;
	public int numberOfZombies = waveCount * 100;
	private static List<String> playersInRoom = new ArrayList<String>();

	public static void add(Player c) {
		if (c == null)
			return;
		final int i = playersInRoom.size();
		if (i >= 4)
			return;
		if (playersInRoom.contains(c.getUsername()))
			return;
		playersInRoom.add(c.getUsername());

	}

	public static void remove(Player c) {

		if (c == null)
			return;
		if (playersInRoom.contains(c.getUsername()))
			playersInRoom.remove(c.getUsername());
	}

	//

	// BOSSES

}
