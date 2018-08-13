package com.server2.world;

import java.util.HashMap;
import java.util.Map;

import com.server2.Settings;
import com.server2.event.impl.PlayerOnlineCalculation;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;

/**
 * Player manager
 * 
 * @author Daiki
 * @author Graham
 * 
 */
public class PlayerManager {
	public static Player getDuelOpponent(Player client) {
		return client.getDuelOpponent();
	}

	private int playerSlotSearchStart = 1;
	public Player[] players = new Player[(int) Settings
			.getLong("sv_maxclients")];
	private static PlayerManager singleton = null;

	public static PlayerManager getSingleton() {
		if (singleton == null)
			singleton = new PlayerManager();
		return singleton;
	}

	public Map<String, Player> playerNameMap;

	public Map<Long, Player> playerNameLongMap;

	public PlayerManager() {
		for (int i = 0; i < Settings.getLong("sv_maxclients"); i++)
			players[i] = null;
		playerNameMap = new HashMap<String, Player>();
		playerNameLongMap = new HashMap<Long, Player>();
	}

	public boolean addClient(final Player newClient) {
		if (newClient == null)
			return false;
		final int slot = getFreeSlot();
		newClient.setIndex(slot);
		if (players[slot] == null)
			players[slot] = newClient;
		else
			players[getFreeSlot()] = newClient;
		Clan.leave(newClient);

		playerNameMap.put(newClient.getUsername(), newClient);
		playerNameLongMap.put(Misc.playerNameToInt64(newClient.getUsername()),
				newClient);
		playerSlotSearchStart = slot + 1;
		newClient.encodedName = Misc.playerNameToInt64(newClient.getUsername());
		if (playerSlotSearchStart > Settings.getLong("sv_maxclients"))
			playerSlotSearchStart = 1;
		return true;
	}

	public int getFreeSlot() {
		for (int i = 1; i < Settings.getLong("sv_maxclients"); i++)
			if (players[i] == null)
				return i;
		return -1;
	}

	public Player getPlayerByName(String name) {
		for (final Player p : PlayerManager.getSingleton().getPlayers()) {
			if (p == null)
				continue;
			if (p.getUsername().equalsIgnoreCase(name))
				return p;
		}
		return null;
	}

	public Player getPlayerByNameLong(long name) {
		return playerNameLongMap.get(name);
	}

	public int getPlayerCount() {
		return PlayerOnlineCalculation.playerCount;
	}

	public Player[] getPlayers() {
		return players;
	}

	public boolean isPlayerOn(String playerName) {
		playerName = playerName.replaceAll("_", " ");
		return getPlayerByName(playerName) != null;
	}

	public void kick(String name) {
		for (final Player p : players) {
			if (p == null)
				continue;
			if (!p.isActive || p.disconnected)
				continue;
			if (p.getUsername().equalsIgnoreCase(name)) {
				p.kick();
				p.disconnected = true;
			}
		}
	}

	public void removePlayer(Player p) {
		playerNameMap.remove(p.getUsername());
		playerNameLongMap.remove(Misc.playerNameToInt64(p.getUsername()));

		for (int i = 0; i < PlayerManager.getSingleton().getPlayers().length; i++) {
			if (getPlayers()[i] == null)
				continue;
			if (PlayerManager.getSingleton().getPlayers()[i] == p) {
				getPlayers()[i] = null;
				break;
			}
		}
	}

	public void shutdown() {
		for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
			if (players[i] == null)
				continue;
			players[i].destruct();
			players[i] = null;
		}
		playerNameMap.clear();
		playerNameLongMap.clear();
	}
}