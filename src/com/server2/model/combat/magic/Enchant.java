package com.server2.model.combat.magic;

import com.server2.model.entity.player.Player;

/**
 * 
 * @author Lukas P
 * 
 */
public class Enchant {

	public static int[][] data = {
			{ 1155, 7, 555, 564, 0, 1, 1, 0, 114, 1694, 1727, 17 },
			{ 1165, 27, 556, 564, 0, 3, 1, 0, 114, 1696, 1729, 37 },
			{ 1176, 49, 554, 564, 0, 10, 1, 0, 115, 1698, 1725, 59 },
			{ 1180, 57, 557, 564, 0, 10, 1, 0, 115, 1700, 1731, 67 },
			{ 1187, 68, 557, 555, 564, 15, 15, 1, 116, 1702, 1704, 78 },
			{ 6003, 87, 554, 557, 564, 20, 20, 1, 116, 6581, 6585, 97 }, };

	public static void item(Player client, int id, int itemId, int slot) {
	}

	public static int nextItem(int id, int spellId) {
		int i = 0;
		if (spellId == 1155)
			if (id == 1637)
				i = 2550;
			else if (id == 1656)
				i = 3853;
			else if (id == 1694)
				i = 1727;
		if (spellId == 1165)
			if (id == 1639)
				i = 2552;
			else if (id == 1658)
				i = 3853;
			else if (id == 1694)
				i = 1727;
		if (spellId == 1176)
			if (id == 1641)
				i = 2568;
			else if (id == 1660)
				i = 3853;
			else if (id == 1694)
				i = 1727;
		if (spellId == 1180)
			if (id == 1643)
				i = 2570;
			else if (id == 1662)
				i = 3853;
			else if (id == 1694)
				i = 1727;
		if (spellId == 1187)
			if (id == 1645)
				i = 2572;
			else if (id == 1656)
				i = 3853;
			else if (id == 1694)
				i = 1727;
		return i;
	}
}
