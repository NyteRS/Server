package com.server2.world.map.tile;

/**
 * 
 * @author Killamess
 * 
 */

public class Tile {

	private final int[] pointer = new int[3];

	public Tile(int x, int y, int z) {
		pointer[0] = x;
		pointer[1] = y;
		pointer[2] = z;
	}

	public int[] getTile() {
		return pointer;
	}

	public int getTileHeight() {
		return pointer[2];
	}

	public int getTileX() {
		return pointer[0];
	}

	public int getTileY() {
		return pointer[1];
	}

}
