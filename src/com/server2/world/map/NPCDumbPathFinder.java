package com.server2.world.map;

import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.npc.NPC;
import com.server2.util.Misc;
import com.server2.world.map.tile.Tile;

/**
 * 
 * @author Rene
 * 
 */
public class NPCDumbPathFinder {

	private static final int NORTH = 0, EAST = 1, SOUTH = 2, WEST = 3;

	public static void follow(NPC npc, Entity following) {

		/** the tiles the npc is occupying **/
		final Tile[] npcTiles = TileControl.getTiles(npc);

		/** locations **/
		final int[] npcLocation = TileControl.currentLocation(npc);
		final int[] followingLocation = TileControl.currentLocation(following);

		/** test 4 movements **/
		final boolean[] moves = new boolean[4];

		int dir = -1;// the direction to go.

		final int distance = TileControl.calculateDistance(npc, following);

		if (distance > 16) {
			npc.setFollowing(null);
			return;
		}
		AnimationProcessor.face(npc, following);

		if (npc.getFreezeDelay() > 0)
			return;

		if (distance > 1) {

			/** set all our moves to true **/
			for (int i = 0; i < moves.length; i++)
				moves[i] = true;

			/** remove false moves **/
			if (npcLocation[0] < followingLocation[0]) {
				moves[EAST] = true;
				moves[WEST] = false;
			} else if (npcLocation[0] > followingLocation[0]) {
				moves[WEST] = true;
				moves[EAST] = false;
			} else {
				moves[EAST] = false;
				moves[WEST] = false;
			}
			if (npcLocation[1] > followingLocation[1]) {
				moves[SOUTH] = true;
				moves[NORTH] = false;
			} else if (npcLocation[1] < followingLocation[1]) {
				moves[NORTH] = true;
				moves[SOUTH] = false;
			} else {
				moves[NORTH] = false;
				moves[SOUTH] = false;
			}
			for (final Tile tiles : npcTiles)
				if (tiles.getTile()[0] == following.getAbsX()) { // same x line
					moves[EAST] = false;
					moves[WEST] = false;
				} else if (tiles.getTile()[1] == following.getAbsY()) { // same
																		// y
																		// line
					moves[NORTH] = false;
					moves[SOUTH] = false;
				}
			final boolean[] blocked = new boolean[3];

			if (moves[NORTH] && moves[EAST]) {
				for (final Tile tiles : npcTiles) {
					if (Region.blockedNorth(tiles.getTileX(), tiles.getTileY(),
							tiles.getTileHeight()))
						blocked[0] = true;
					if (Region.blockedEast(tiles.getTileX(), tiles.getTileY(),
							tiles.getTileHeight()))
						blocked[1] = true;
					if (Region.blockedNorthEast(tiles.getTileX(),
							tiles.getTileY(), tiles.getTileHeight()))
						blocked[2] = true;
				}
				if (!blocked[2] && !blocked[0] && !blocked[1])
					dir = 2;
				else if (!blocked[0])
					dir = 0;
				else if (!blocked[1])
					dir = 4;

			} else if (moves[NORTH] && moves[WEST]) {
				for (final Tile tiles : npcTiles) {
					if (Region.blockedNorth(tiles.getTileX(), tiles.getTileY(),
							tiles.getTileHeight()))
						blocked[0] = true;
					if (Region.blockedWest(tiles.getTileX(), tiles.getTileY(),
							tiles.getTileHeight()))
						blocked[1] = true;
					if (Region.blockedNorthWest(tiles.getTileX(),
							tiles.getTileY(), tiles.getTileHeight()))
						blocked[2] = true;
				}
				if (!blocked[2] && !blocked[0] && !blocked[1])
					dir = 14;
				else if (!blocked[0])
					dir = 0;
				else if (!blocked[1])
					dir = 12;
			} else if (moves[SOUTH] && moves[EAST]) {
				for (final Tile tiles : npcTiles) {
					if (Region.blockedSouth(tiles.getTileX(), tiles.getTileY(),
							tiles.getTileHeight()))
						blocked[0] = true;
					if (Region.blockedEast(tiles.getTileX(), tiles.getTileY(),
							tiles.getTileHeight()))
						blocked[1] = true;
					if (Region.blockedSouthEast(tiles.getTileX(),
							tiles.getTileY(), tiles.getTileHeight()))
						blocked[2] = true;
				}
				if (!blocked[2] && !blocked[0] && !blocked[1])
					dir = 6;
				else if (!blocked[0])
					dir = 8;
				else if (!blocked[1])
					dir = 4;
			} else if (moves[SOUTH] && moves[WEST]) {
				for (final Tile tiles : npcTiles) {
					if (Region.blockedSouth(tiles.getTileX(), tiles.getTileY(),
							tiles.getTileHeight()))
						blocked[0] = true;
					if (Region.blockedWest(tiles.getTileX(), tiles.getTileY(),
							tiles.getTileHeight()))
						blocked[1] = true;
					if (Region.blockedSouthWest(tiles.getTileX(),
							tiles.getTileY(), tiles.getTileHeight()))
						blocked[2] = true;
				}
				if (!blocked[2] && !blocked[0] && !blocked[1])
					dir = 10;
				else if (!blocked[0])
					dir = 8;
				else if (!blocked[1])
					dir = 12;

			} else if (moves[NORTH]) {
				dir = 0;
				for (final Tile tiles : npcTiles)
					if (Region.blockedNorth(tiles.getTileX(), tiles.getTileY(),
							tiles.getTileHeight()))
						dir = -1;
			} else if (moves[EAST]) {
				dir = 4;
				for (final Tile tiles : npcTiles)
					if (Region.blockedEast(tiles.getTileX(), tiles.getTileY(),
							tiles.getTileHeight()))
						dir = -1;
			} else if (moves[SOUTH]) {
				dir = 8;
				for (final Tile tiles : npcTiles)
					if (Region.blockedSouth(tiles.getTileX(), tiles.getTileY(),
							tiles.getTileHeight()))
						dir = -1;
			} else if (moves[WEST]) {
				dir = 12;
				for (final Tile tiles : npcTiles)
					if (Region.blockedWest(tiles.getTileX(), tiles.getTileY(),
							tiles.getTileHeight()))
						dir = -1;
			}
		} else if (distance == 0) {
			for (int i = 0; i < moves.length; i++)
				moves[i] = true;
			for (final Tile tiles : npcTiles) {

				if (Region.blockedNorth(tiles.getTileX(), tiles.getTileY(),
						tiles.getTileHeight()))
					moves[NORTH] = false;
				if (Region.blockedEast(tiles.getTileX(), tiles.getTileY(),
						tiles.getTileHeight()))
					moves[EAST] = false;
				if (Region.blockedSouth(tiles.getTileX(), tiles.getTileY(),
						tiles.getTileHeight()))
					moves[SOUTH] = false;
				if (Region.blockedWest(tiles.getTileX(), tiles.getTileY(),
						tiles.getTileHeight()))
					moves[WEST] = false;
			}
			final int randomSelection = Misc.random(3);

			if (moves[randomSelection])
				dir = randomSelection * 4;
			else if (moves[NORTH])
				dir = 0;
			else if (moves[EAST])
				dir = 4;
			else if (moves[SOUTH])
				dir = 8;
			else if (moves[WEST])
				dir = 12;
		}
		if (dir == -1)
			return;
		dir >>= 1;

		if (dir < 0)
			return;
		final int x = Misc.directionDeltaX[dir];
		final int y = Misc.directionDeltaY[dir];
		npc.setWalk(npcLocation[0] + x, npcLocation[1] + y, false);
	}

}