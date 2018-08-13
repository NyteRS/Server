package com.server2.world.map;

import com.server2.model.entity.Entity;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.npc.NPCSize;
import com.server2.world.map.tile.Tile;

/**
 * 
 * @author Rene
 * 
 */
public class TileControl {

	public static int calculateDistance(Entity entity, Entity following) {

		final Tile[] tiles = getTiles(entity);

		final int[] location = currentLocation(entity);
		final int[] pointer = new int[tiles.length];

		int lowestCount = 20, count = 0;

		for (final Tile newTiles : tiles)
			if (newTiles.getTile() == location)
				pointer[count++] = 0;
			else
				pointer[count++] = calculateDistance(newTiles, following);
		for (final int element : pointer)
			if (element < lowestCount)
				lowestCount = element;

		return lowestCount;
	}

	public static int calculateDistance(int[] location, Entity other) {
		final int X = Math.abs(location[0] - other.getAbsX());
		final int Y = Math.abs(location[1] - other.getAbsY());
		return X > Y ? X : Y;
	}

	public static int calculateDistance(int[] location, int[] other) {
		final int X = Math.abs(location[0] - other[0]);
		final int Y = Math.abs(location[1] - other[1]);
		return X > Y ? X : Y;
	}

	public static int calculateDistance(Tile location, Entity other) {
		final int X = Math.abs(location.getTile()[0] - other.getAbsX());
		final int Y = Math.abs(location.getTile()[1] - other.getAbsY());
		return X > Y ? X : Y;
	}

	public static int[] currentLocation(Entity entity) {
		final int[] currentLocation = new int[3];
		if (entity != null) {
			currentLocation[0] = entity.getAbsX();
			currentLocation[1] = entity.getAbsY();
			currentLocation[2] = entity.getHeightLevel();
		}
		return currentLocation;
	}

	public static int[] currentLocation(Tile tileLocation) {

		final int[] currentLocation = new int[3];

		if (tileLocation != null) {
			currentLocation[0] = tileLocation.getTile()[0];
			currentLocation[1] = tileLocation.getTile()[1];
			currentLocation[2] = tileLocation.getTile()[2];
		}
		return currentLocation;
	}

	public static Tile generate(int x, int y, int z) {
		return new Tile(x, y, z);
	}

	public static Tile[] getTiles(Entity entity) {

		int size = 1, tileCount = 0;

		if (entity instanceof NPC)
			size = NPCSize.forId(((NPC) entity).getDefinition().getType());
		final Tile[] tiles = new Tile[size * size];

		if (tiles.length == 1)
			tiles[0] = generate(entity.getAbsX(), entity.getAbsY(),
					entity.getHeightLevel());
		else
			for (int x = 0; x < size; x++)
				for (int y = 0; y < size; y++)
					tiles[tileCount++] = generate(entity.getAbsX() + x,
							entity.getAbsY() + y, entity.getHeightLevel());
		return tiles;
	}

	public static Tile[] getTiles(Entity entity, int[] location) {

		int size = 1, tileCount = 0;

		if (entity instanceof NPC)
			size = NPCSize.forId(((NPC) entity).getDefinition().getType());

		final Tile[] tiles = new Tile[size * size];

		if (tiles.length == 1)
			tiles[0] = generate(location[0], location[1], location[2]);
		else
			for (int x = 0; x < size; x++)
				for (int y = 0; y < size; y++)
					tiles[tileCount++] = generate(location[0] + x, location[1]
							+ y, location[2]);
		return tiles;
	}
}