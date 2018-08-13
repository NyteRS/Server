package com.server2.world.map.tile;

import com.server2.model.PathFinder;
import com.server2.model.combat.additions.Life;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.world.map.NPCDumbPathFinder;

/**
 * 
 * @author Killamess & Lukas
 * 
 */

public class FollowEngine {

	public static boolean canMove(Entity entity, Entity following) {

		final Player client = (Player) entity;

		if (client.isDead())
			return true;

		final int requiredDistance = requiredDistance(entity.getCombatType());

		if (TileManager.calculateDistance(entity, following) <= requiredDistance)
			return false;

		return true;
	}

	public static void findRoute(int x, int y, Player p) {
		PathFinder.getPathFinder().findRoute(p, x, y, true, 1, 1);
	}

	public static int getNextFollowingDirection(Entity entity, Entity following) {
		return -1;
	}

	public static void loop(Entity entity) {
		if (entity == null || !Life.isAlive(entity)
				|| entity.getFollowing() == null)
			return;
		if (entity.getFreezeDelay() > 0)
			return;
		final Entity following = entity.getFollowing();
		if (following == null || !Life.isAlive(following)) {
			resetFollowing(entity);
			return;
		}
		// ((NPC) entity).follow(following);
		NPCDumbPathFinder.follow((NPC) entity, following);
	}

	public static boolean needsToStop(Entity entity, Entity following) {
		if (!(entity instanceof Player))
			return true;

		final Player client = (Player) entity;

		if (client.isDead())
			return true;
		final int[] location2 = TileManager.currentLocation(following);

		final Tile[] npcTiles = TileManager.getTiles(following);

		final int[] tileDistance = new int[npcTiles.length];

		int lowestCount = 20;

		int count = 0;

		for (final Tile tiles : npcTiles)
			if (tiles.getTile() != location2)
				tileDistance[count++] = TileManager.calculateDistance(tiles,
						entity);
			else
				tileDistance[count++] = 0;
		for (final int element : tileDistance)
			if (element < lowestCount)
				lowestCount = element;
		final int actualDistance = lowestCount;

		if (entity.getTarget() == null
				|| entity.getCombatType() == CombatType.MELEE)
			return actualDistance == 1;
		else if (entity.getCombatType() == CombatType.MAGIC
				|| entity.getCombatType() == CombatType.RANGE)
			return actualDistance <= 8;

		return false;
	}

	public static int requiredDistance(CombatType x) {
		int zehDistance = 1;
		if (x == CombatType.RANGE)
			zehDistance = 6;
		else if (x == CombatType.MAGIC)
			zehDistance = 8;

		return zehDistance;
	}

	public static void resetFollowing(Entity entity) {
		if (entity != null) {
			entity.setFollowing(null);
			if (entity instanceof Player) {
				if (((Player) entity).followId > 0)
					((Player) entity).followId = -1;
				if (((Player) entity).followId2 > 0)
					((Player) entity).followId2 = -1;
			}
		}
	}

}
