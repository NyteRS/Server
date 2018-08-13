package com.server2.model.combat.magic;

import java.util.ArrayList;
import java.util.List;

import com.server2.model.entity.player.Player;
import com.server2.world.XMLManager;
import com.server2.world.XMLManager.Spell;
import com.server2.world.XMLManager.Teleport;

/**
 * 
 * @author Rene
 * 
 */
public class Magic {

	public static List<Player> playersProcessing = new ArrayList<Player>();

	public static boolean inQueue(Player client) {
		return playersProcessing.contains(client);
	}

	public static Spell spell(int id) {
		for (final Spell s : XMLManager.spells)
			if (s.getId() == id)
				return s;
		return null;
	}

	public static Teleport teleport(int id) {
		for (final Teleport t : XMLManager.teleports)
			if (t.getId() == id)
				return t;
		return null;
	}
}
