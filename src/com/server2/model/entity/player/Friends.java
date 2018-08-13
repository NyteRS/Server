package com.server2.model.entity.player;

import com.server2.net.LoginServerConnection;
import com.server2.util.Misc;
import com.server2.world.World;

/**
 * A class that handles private messaging.
 * 
 * @author Ultimate1
 */

public class Friends {

	/**
	 * The maximum amount of friends.
	 */
	private static final int MAX_FRIENDS = 200;

	/**
	 * The maximum amount of ignores.
	 */
	private static final int MAX_IGNORES = 100;

	/**
	 * An instance of the login server.
	 */
	private static LoginServerConnection loginServer = World
			.getLoginServerConnection();

	/**
	 * The global private message index.
	 */
	private static int privateMessageIndex = 1;

	/**
	 * Adds a friend to the friends list.
	 * 
	 * @param friend
	 * @param player
	 */
	public static void addFriend(final long friend, final Player player) {
		final long[] friends = player.getFriends();
		if (size(friends) >= MAX_FRIENDS)
			return;
		final int i = contains(friends, friend);
		if (i != -1)
			return;
		final int index = getSlot(friends);
		player.getFriends()[index] = friend;
		loginServer.submit(new Runnable() {
			@Override
			public void run() {
				loginServer.addFriend(index, friend, player.getUsername());
				loginServer.requestFriendStatus(friend, player.getUsername());
			}
		});
	}

	/**
	 * Adds a name to the player's ignore list.
	 * 
	 * @param l
	 * @param player
	 */
	public static void addToIgnore(long l, final Player player) {
		final long[] ignores = player.getIgnores();
		if (size(ignores) >= MAX_IGNORES)
			return;
		final int i = contains(ignores, l);
		if (i != -1)
			return;
		player.getIgnores()[getSlot(ignores)] = l;
	}

	/**
	 * Checks if the contains the specified name.
	 * 
	 * @param list
	 * @param name
	 * @return
	 */
	public static int contains(long[] list, long name) {
		for (int i = 0; i < list.length; i++)
			if (list[i] == name)
				return i;
		return -1;
	}

	/**
	 * Gets the amount of players in the specified list.
	 * 
	 * @param list
	 * @return
	 */
	public static int getAmount(long[] list) {
		int count = 0;
		for (final long element : list) {
			if (element <= 0)
				continue;
			count++;
		}
		return count;
	}

	/**
	 * @return the global private message index.
	 */
	public static int getMessageIndex() {
		return privateMessageIndex++;
	}

	/**
	 * Gets a free slot in the specified list.
	 * 
	 * @param list
	 * @return
	 */
	private static int getSlot(long[] list) {
		for (int i = 0; i < list.length; i++)
			if (list[i] == 0)
				return i;
		return -1;
	}

	/**
	 * Removes a friend from the friend list.
	 * 
	 * @param friend
	 * @param player
	 */
	public static void removeFriend(long friend, final Player player) {
		final long[] friends = player.getFriends();
		final int index = contains(friends, friend);
		if (index == -1)
			return;
		player.getFriends()[index] = 0;
		loginServer.submit(new Runnable() {
			@Override
			public void run() {
				loginServer.removeFriend(index, player.getUsername());
			}
		});
	}

	/**
	 * Remove a name from the player's ignore list.
	 * 
	 * @param l
	 * @param player
	 */
	public static void removeFromIgnore(long l, final Player player) {
		final long[] ignores = player.getIgnores();
		final int i = contains(ignores, l);
		if (i == -1) {
			player.sendMessage(Misc.longToPlayerName(l)
					+ " is already on your ignore list");
			return;
		}
		player.getIgnores()[i] = 0;
	}

	/**
	 * Gets the size of the specified list.
	 * 
	 * @param array
	 * @return
	 */
	private static int size(long[] array) {
		int size = 0;
		for (final long element : array)
			if (element != 0)
				size++;
		return size;
	}

	/**
	 * Updates the player's friend list with the friends from login server.
	 * 
	 * @param friends
	 * @param worlds
	 * @param chatMode
	 * @param player
	 */
	public static void updateFriendsList(long[] friends, byte[] worlds,
			int chatMode, Player player) {
		player.friends = friends;
		player.privateChatMode = chatMode;
		player.getActionSender().sendPMStatus(2);
		player.getActionSender().sendChatOptions(0, chatMode, 0);
		for (int i = 0; i < friends.length; i++) {
			if (friends[i] <= 0)
				continue;
			player.getActionSender().sendFriend(friends[i], worlds[i]);
		}
	}

}