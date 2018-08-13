package com.server2.world;

import com.server2.Settings;
import com.server2.model.entity.Projectile;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;
import com.server2.world.map.Region;

/**
 * Handles global actions
 * 
 * @author Rene
 */

public class GlobalActions {

	public void sendAnimationReset(Player client) {
		for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
			final Player p = PlayerManager.getSingleton().getPlayers()[i];
			if (p == null)
				continue;
			if (!p.isActive || p.disconnected)
				continue;
			final Player c = p;
			if (c.withinDistance(client.getAbsX(), client.getAbsY(), 60))
				c.getActionSender().sendAnimationReset();
		}
	}

	public void sendFarmingObject(int objectID, int objectX, int objectY,
			int objectHeight, int objectFace, int objectType, int playerID) {
		for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
			final Player p = PlayerManager.getSingleton().getPlayers()[i];
			if (p == null)
				continue;
			if (!p.isActive || p.disconnected)
				continue;
			if (p.getHeightLevel() != objectHeight)
				continue;
			final Player c = p;
			if (c.withinDistance(objectX, objectY, 25) && c.ID == playerID)
				c.getActionSender().sendObject(objectID, objectX, objectY,
						objectHeight, objectFace, objectType);
		}
	}

	public void sendMessage(String message) {
		for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
			final Player p = PlayerManager.getSingleton().getPlayers()[i];
			if (p == null)
				continue;
			if (!p.isActive || p.disconnected)
				continue;
			final Player c = p;
			c.getActionSender().sendMessage(message);
		}
	}

	public void sendObject(int objectID, int objectX, int objectY,
			int objectHeight, int objectFace, int objectType) {
		for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
			final Player p = PlayerManager.getSingleton().getPlayers()[i];
			if (p == null)
				continue;
			if (!p.isActive || p.disconnected)
				continue;
			if (p.getHeightLevel() != objectHeight)
				continue;
			final Player c = p;
			if (c.withinDistance(objectX, objectY, 32)
					&& p.getHeightLevel() == objectHeight)
				c.getActionSender().sendObject(objectID, objectX, objectY,
						objectHeight, objectFace, objectType);
			if (objectID == -1)
				Region.removeClipping(objectX, objectY, objectHeight,
						Integer.MAX_VALUE);
		}

	}

	public void sendProjectile(Player client, int absY, int absX, int offsetY,
			int offsetX, int proID, int startHeight, int endHeight, int speed,
			int lockon) {
		for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
			final Player p = PlayerManager.getSingleton().getPlayers()[i];
			if (p == null)
				continue;
			if (!p.isActive || p.disconnected)
				continue;
			final Player c = p;
			if (client.withinDistance(c))
				c.getActionSender().sendProjectile(absY, absX, offsetY,
						offsetX, proID, startHeight, endHeight, speed, lockon);
		}
	}

	public void sendProjectile(Player client, int absY, int absX, int offsetY,
			int offsetX, int proID, int startHeight, int endHeight, int speed,
			int angle, int lockon) {
		for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
			final Player p = PlayerManager.getSingleton().getPlayers()[i];
			if (p == null)
				continue;
			if (!p.isActive || p.disconnected)
				continue;
			final Player c = p;
			if (client.withinDistance(c))
				c.getActionSender().sendProjectile(absY, absX, offsetY,
						offsetX, proID, startHeight, endHeight, speed, angle,
						lockon);
		}
	}

	/**
	 * Sends a projectile to everybody in the local region.
	 * 
	 * @param projectile
	 */
	public void sendProjectile(Projectile projectile) {
		for (final Player player : PlayerManager.getSingleton().getPlayers()) {
			if (player == null)
				continue;
			final int distance = Misc.getDistance(projectile.getCaster()
					.getPosition(), player.getPosition());
			if (distance >= 16
					|| player.getPosition().getZ() != projectile.getCaster()
							.getPosition().getZ())
				continue;
			player.getActionSender().sendProjectile(projectile);
		}
	}

	public void sendRiot(String message) {
		for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
			final Player p = PlayerManager.getSingleton().getPlayers()[i];
			if (p == null)
				continue;
			if (!p.isActive || p.disconnected)
				continue;
			final Player c = p;
			c.getActionSender().sendMessage(
					"[<shad=45A9FF>RIOT WARS</shad>@bla@] " + message);
		}
	}
}