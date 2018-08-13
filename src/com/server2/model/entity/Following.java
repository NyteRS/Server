package com.server2.model.entity;

import com.server2.InstanceDistributor;
import com.server2.model.PathFinder;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;
import com.server2.world.PlayerManager;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class Following {

	public static void followNpc(Player c) {
		if (InstanceDistributor.getNPCManager().getNPC(c.followId2) == null
				|| InstanceDistributor.getNPCManager().getNPC(c.followId2)
						.isDead()) {
			c.followId2 = 0;
			return;
		}
		if (c.getFreezeDelay() > 0)
			return;
		if (c.isDead() || c.hitpoints <= 0)
			return;

		final int otherX = InstanceDistributor.getNPCManager()
				.getNPC(c.followId2).getAbsX();
		final int otherY = InstanceDistributor.getNPCManager()
				.getNPC(c.followId2).getAbsY();
		final boolean bowDistance = Misc.goodDistance(otherX, otherY,
				c.getAbsX(), c.getAbsY(), 8);
		final boolean rangeWeaponDistance = Misc.goodDistance(otherX, otherY,
				c.getAbsX(), c.getAbsY(), 4);
		final boolean sameSpot = c.absX == otherX && c.absY == otherY;
		if (!Misc.goodDistance(otherX, otherY, c.getAbsX(), c.getAbsY(), 25)) {
			c.followId2 = 0;
			return;
		}

		if ((c.usingRangeWeapon || c.mageFollow || c.npcIndex > 0)
				&& bowDistance && !sameSpot)
			return;

		if (c.usingRangeWeapon && rangeWeaponDistance && !sameSpot)
			return;

		AnimationProcessor.face(c,
				InstanceDistributor.getNPCManager().getNPC(c.followId2));
		if (otherX == c.absX && otherY == c.absY) {
			final int r = Misc.random(3);
			final int x = c.absX;
			final int y = c.absY;
			switch (r) {
			case 0:
				c.forceMovement(new Location(x, y - 1));
				break;
			case 1:
				c.forceMovement(new Location(x, y + 1));
				break;
			case 2:
				c.forceMovement(new Location(x + 1, y));
				break;
			case 3:
				c.forceMovement(new Location(x - 1, y));
				break;

			}
		} else if (c.isRunning2) {
			if (otherY > c.getAbsY() && otherX == c.getAbsX())
				playerWalk(c, otherX, otherY - 1);
			else if (otherY < c.getAbsY() && otherX == c.getAbsX())
				playerWalk(c, otherX, otherY + 1);
			else if (otherX > c.getAbsX() && otherY == c.getAbsY())
				playerWalk(c, otherX - 1, otherY);
			else if (otherX < c.getAbsX() && otherY == c.getAbsY())
				playerWalk(c, otherX + 1, otherY);
			else if (otherX < c.getAbsX() && otherY < c.getAbsY())
				playerWalk(c, otherX + 1, otherY + 1);
			else if (otherX > c.getAbsX() && otherY > c.getAbsY())
				playerWalk(c, otherX - 1, otherY - 1);
			else if (otherX < c.getAbsX() && otherY > c.getAbsY())
				playerWalk(c, otherX + 1, otherY - 1);
			else if (otherX > c.getAbsX() && otherY < c.getAbsY())
				playerWalk(c, otherX + 1, otherY - 1);
		} else if (otherY > c.getAbsY() && otherX == c.getAbsX())
			playerWalk(c, otherX, otherY - 1);
		else if (otherY < c.getAbsY() && otherX == c.getAbsX())
			playerWalk(c, otherX, otherY + 1);
		else if (otherX > c.getAbsX() && otherY == c.getAbsY())
			playerWalk(c, otherX - 1, otherY);
		else if (otherX < c.getAbsX() && otherY == c.getAbsY())
			playerWalk(c, otherX + 1, otherY);
		else if (otherX < c.getAbsX() && otherY < c.getAbsY())
			playerWalk(c, otherX + 1, otherY + 1);
		else if (otherX > c.getAbsX() && otherY > c.getAbsY())
			playerWalk(c, otherX - 1, otherY - 1);
		else if (otherX < c.getAbsX() && otherY > c.getAbsY())
			playerWalk(c, otherX + 1, otherY - 1);
		else if (otherX > c.getAbsX() && otherY < c.getAbsY())
			playerWalk(c, otherX - 1, otherY + 1);
		AnimationProcessor.face(c,
				InstanceDistributor.getNPCManager().getNPC(c.followId2));
	}

	public static void followPlayer(Player c) {
		if (c.followId == -1)
			return;
		if (PlayerManager.getSingleton().players[c.followId] == null
				|| PlayerManager.getSingleton().players[c.followId].isDead()) {
			resetFollow(c);
			return;
		}
		if (c.getFreezeDelay() > 0)
			return;
		if (c.isDead() || c.playerLevel[3] <= 0)
			return;

		final int otherX = PlayerManager.getSingleton().players[c.followId]
				.getAbsX();
		final int otherY = PlayerManager.getSingleton().players[c.followId]
				.getAbsY();

		final boolean sameSpot = c.absX == otherX && c.absY == otherY;

		final boolean rangeWeaponDistance = Misc.goodDistance(otherX, otherY,
				c.getAbsX(), c.getAbsY(), 4);
		final boolean bowDistance = Misc.goodDistance(otherX, otherY,
				c.getAbsX(), c.getAbsY(), 6);
		final boolean mageDistance = Misc.goodDistance(otherX, otherY,
				c.getAbsX(), c.getAbsY(), 7);

		final boolean castingMagic = (c.autoCasting && c.getTarget() != null
				|| c.spellId > 0 || c.getAutoCastId() > 0)
				&& mageDistance;
		final boolean playerRanging = c.usingRangeWeapon && rangeWeaponDistance;
		final boolean playerBowOrCross = c.usingRangeWeapon && bowDistance;

		if (!Misc.goodDistance(otherX, otherY, c.getAbsX(), c.getAbsY(), 25)) {
			c.followId = 0;
			resetFollow(c);
			return;
		}
		AnimationProcessor.face(c,
				PlayerManager.getSingleton().players[c.followId]);
		if (!sameSpot)
			if (c.getIndex() > 0) {
				if (c.usingSpecial() && (playerRanging || playerBowOrCross)) {
					c.stopMovement();
					return;
				}
				if (castingMagic || playerRanging || playerBowOrCross) {
					c.stopMovement();
					return;
				}
			}
		final int x = c.absX;
		final int y = c.absY;
		if (otherX == c.absX && otherY == c.absY) {
			final int r = Misc.random(3);
			switch (r) {
			case 0:
				c.forceMovement(new Location(x, y - 1));
				break;
			case 1:
				c.forceMovement(new Location(x, y + 1));
				break;
			case 2:
				c.forceMovement(new Location(x + 1, y));
				break;
			case 3:
				c.forceMovement(new Location(x - 1, y));
				break;
			}
		} else if (c.isRunning2) {
			if (otherY > c.getAbsY() && otherX == c.getAbsX())
				playerWalk(c, otherX, otherY - 1);
			else if (otherY < c.getAbsY() && otherX == c.getAbsX())
				playerWalk(c, otherX, otherY + 1);
			else if (otherX > c.getAbsX() && otherY == c.getAbsY())
				playerWalk(c, otherX - 1, otherY);
			else if (otherX < c.getAbsX() && otherY == c.getAbsY())
				playerWalk(c, otherX + 1, otherY);
			else if (otherX < c.getAbsX() && otherY < c.getAbsY())
				playerWalk(c, otherX + 1, otherY + 1);
			else if (otherX > c.getAbsX() && otherY > c.getAbsY())
				playerWalk(c, otherX - 1, otherY - 1);
			else if (otherX < c.getAbsX() && otherY > c.getAbsY())
				playerWalk(c, otherX + 1, otherY - 1);
			else if (otherX > c.getAbsX() && otherY < c.getAbsY())
				playerWalk(c, otherX + 1, otherY - 1);
		} else if (otherY > c.getAbsY() && otherX == c.getAbsX())
			playerWalk(c, otherX, otherY - 1);
		else if (otherY < c.getAbsY() && otherX == c.getAbsX())
			playerWalk(c, otherX, otherY + 1);
		else if (otherX > c.getAbsX() && otherY == c.getAbsY())
			playerWalk(c, otherX - 1, otherY);
		else if (otherX < c.getAbsX() && otherY == c.getAbsY())
			playerWalk(c, otherX + 1, otherY);
		else if (otherX < c.getAbsX() && otherY < c.getAbsY())
			playerWalk(c, otherX + 1, otherY + 1);
		else if (otherX > c.getAbsX() && otherY > c.getAbsY())
			playerWalk(c, otherX - 1, otherY - 1);
		else if (otherX < c.getAbsX() && otherY > c.getAbsY())
			playerWalk(c, otherX + 1, otherY - 1);
		else if (otherX > c.getAbsX() && otherY < c.getAbsY())
			playerWalk(c, otherX - 1, otherY + 1);
		AnimationProcessor.face(c,
				PlayerManager.getSingleton().players[c.followId]);
	}

	public static void playerWalk(Player c, int x, int y) {
		PathFinder.getPathFinder().findRoute(c, x, y, true, 1, 1);
	}

	public static void resetFollow(Player c) {
		c.followId = -1;
	}

}
