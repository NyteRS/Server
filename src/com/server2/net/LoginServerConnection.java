package com.server2.net;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.jboss.netty.channel.Channel;

import com.server2.event.impl.PlayerOnlineCalculation;
import com.server2.model.entity.player.Friends;
import com.server2.model.entity.player.Player;
import com.server2.world.PlayerManager;

/**
 * Login server connection handler.
 * 
 * @author Ultimate1
 */

public class LoginServerConnection {

	/**
	 * Represents a login reply.
	 * 
	 * @author Ultimate1
	 */
	private static class LoginResponse {

		private int returnCode = 11;

		public static final LoginResponse NONE = new LoginResponse(11);

		public LoginResponse(int returnCode) {
			this.returnCode = returnCode;
		}

		public Object[] toArray() {
			return new Object[] { returnCode };
		}

	}

	/**
	 * The logger.
	 */
	private static Logger logger = Logger.getLogger(LoginServerConnection.class
			.getName());

	/**
	 * The channel.
	 */
	private Channel channel;

	/**
	 * Login status responses.
	 */
	private final Map<String, LoginResponse> loginResponses = new HashMap<String, LoginResponse>();

	/**
	 * The work service.
	 * 
	 */
	private final ExecutorService workService = Executors
			.newSingleThreadExecutor();

	/**
	 * Adds a friend to the list.
	 * 
	 * @param friendIndex
	 * @param friend
	 * @param name
	 * @return
	 */
	public LoginServerConnection addFriend(int friendIndex, long friend,
			String name) {
		if (channel.isConnected())
			channel.write(new LoginServerPacket(8).writeString(name)
					.writeByte(friendIndex).writeLong(friend));
		return this;
	}

	/**
	 * Finalise the login procedure.
	 * 
	 * @param player
	 * @return
	 */
	public LoginServerConnection finaliseLogin(Player player) {
		if (channel.isConnected())
			channel.write(new LoginServerPacket(14).writeString(
					player.getUsername()).writeShort(0));
		return this;
	}

	/**
	 * Handles a login server packet.
	 * 
	 * @param opcode
	 * @param packet
	 * @param channel
	 */
	public void handlePacket(int opcode, LoginServerPacket packet,
			Channel channel) throws Exception {
		switch (opcode) {
		case 1: {
			final int code = packet.readByte();
			if (code == 2)
				this.channel = channel;
			else {
				logger.warning("Error registering node on login server (code "
						+ code + ").");
				channel.close();
			}
		}
			break;
		case 2: {
			final String name = packet.readString();
			final int returnCode = packet.readByte();
			final LoginResponse resp = new LoginResponse(returnCode);
			synchronized (loginResponses) {
				loginResponses.put(name, resp);
				loginResponses.notifyAll();
			}
		}
			break;
		case 4: {
			final long sender = packet.readLong();
			final String nameOfRecipient = packet.readString();
			final int senderRights = packet.readByte();
			final int messageSize = packet.readInt();
			final byte[] message = new byte[messageSize];
			for (int i = 0; i < message.length; i++)
				message[i] = (byte) packet.readByte();
			final Player recipient = PlayerManager.getSingleton()
					.getPlayerByName(nameOfRecipient);
			if (recipient != null)
				recipient.getActionSender().sendPM(sender, senderRights,
						message);
		}
			break;
		case 5: {
			final String name = packet.readString();
			final long friend = packet.readLong();
			final int world = packet.readByte();
			final Player player = PlayerManager.getSingleton().getPlayerByName(
					name);
			if (player != null)
				player.getActionSender().sendFriend(friend, world);
		}
			break;
		case 6: {
			@SuppressWarnings("unused")
			final int nodeId = packet.readByte();
			@SuppressWarnings("unused")
			final int rights = packet.readByte();
			@SuppressWarnings("unused")
			final String sender = packet.readString();
			@SuppressWarnings("unused")
			final String message = packet.readString();
		}
			break;
		case 7: {
			final int[] count = new int[] { -1, -1 };
			final int activeNodes = packet.readByte();
			for (int i = 0; i < activeNodes; i++) {
				final int nodeId = packet.readByte();
				count[nodeId - 1] = packet.readShort();
			}
		}
			break;
		case 8: {
			final long[] friends = new long[200];
			final byte[] worlds = new byte[200];
			final String name = packet.readString();
			final int chatMode = packet.readByte();
			final int count = packet.readByte();
			for (int i = 0; i < count; i++) {
				friends[i] = packet.readLong();
				worlds[i] = (byte) packet.readByte();
			}
			final Player player = PlayerManager.getSingleton().getPlayerByName(
					name);
			if (player != null)
				Friends.updateFriendsList(friends, worlds, chatMode, player);
			break;

		}
		case 15:
			PlayerOnlineCalculation.playerCount = packet.readShort();
			break;
		}

	}

	/**
	 * Checks if the login server is connected.
	 * 
	 * @return true if the login server is connected.
	 */
	public boolean isConnected() {
		return channel != null && channel.isConnected();
	}

	/**
	 * Gets the login return code for the specified user.
	 * 
	 * @param username
	 * @param password
	 * @param host
	 * @return
	 */
	public Object[] register(String username, String password, String host) {
		if (!isConnected())
			return new Object[] { 8 };

		// Send login request
		final LoginServerPacket request = new LoginServerPacket(7);
		request.writeString(username);
		request.writeString(password);
		request.writeString(host);
		channel.write(request);

		// Wait for a reply
		synchronized (loginResponses) {
			LoginResponse resp = LoginResponse.NONE;
			while (loginResponses.get(username) == null) {
				try {
					loginResponses.wait(15000);
				} catch (final InterruptedException e) {
				}
				break;
			}
			if (loginResponses.containsKey(username)) {
				resp = loginResponses.get(username);
				loginResponses.remove(username);
			}
			return resp.toArray();
		}

	}

	/**
	 * Removes a friend from the list.
	 * 
	 * @param friendIndex
	 * @param name
	 * @return
	 */
	public LoginServerConnection removeFriend(int friendIndex, String name) {
		if (channel.isConnected())
			channel.write(new LoginServerPacket(8).writeString(name)
					.writeByte(friendIndex).writeLong(0));
		return this;
	}

	/**
	 * Requests the status of a friend.
	 * 
	 * @param friend
	 * @param sessionId
	 */
	public void requestFriendStatus(long friend, String name) {
		if (!isConnected())
			return;
		channel.write(new LoginServerPacket(5).writeString(name).writeLong(
				friend));
	}

	/**
	 * Sends a command to the login server
	 * 
	 * @return
	 */
	public LoginServerConnection sendCommand(String command) {
		if (channel.isConnected())
			channel.write(new LoginServerPacket(13).writeString(command));
		return this;
	}

	/**
	 * Sends a global yell packet.
	 * 
	 * @param message
	 * @param name
	 * @return
	 */
	public LoginServerConnection sendGlobalYell1(String message, String name) {
		if (channel.isConnected())
			channel.write(new LoginServerPacket(11).writeString(name)
					.writeString(message));
		return this;
	}

	/**
	 * Forwards a private message to the login server.
	 * 
	 * @param nameOfRecipient
	 * @param message
	 * @param name
	 * @return
	 */
	public LoginServerConnection sendPrivateMessage(String nameOfRecipient,
			byte[] message, String name) {
		if (channel.isConnected()) {
			final LoginServerPacket packet = new LoginServerPacket(6)
					.writeString(name).writeString(nameOfRecipient)
					.writeInt(message.length);
			for (final byte element : message)
				packet.writeByte(element);
			channel.write(packet);
		}
		return this;
	}

	/**
	 * Submits work to the work service.
	 * 
	 * @param runnable
	 */
	public void submit(final Runnable runnable) {
		workService.submit(new Runnable() {
			@Override
			public void run() {
				try {
					runnable.run();
				} catch (final Throwable t) {
					t.printStackTrace();
				}
			}
		});
	}

	/**
	 * Unregister the player.
	 * 
	 * @param name
	 * @return
	 */
	public LoginServerConnection unregister(String name) {
		if (channel.isConnected())
			channel.write(new LoginServerPacket(3).writeString(name));
		return this;
	}

	/**
	 * Updates a player password.
	 * 
	 * @param newPassword
	 * @param name
	 * @return
	 */
	public LoginServerConnection updatePassword(String newPassword, String name) {
		if (channel.isConnected())
			channel.write(new LoginServerPacket(9).writeString(name)
					.writeString(newPassword).writeLong(0));
		return this;
	}

	/**
	 * Updates the player's private chat mode.
	 * 
	 * @param privateChatMode
	 * @param name
	 * @return
	 */
	public LoginServerConnection updatePrivateChatMode(int privateChatMode,
			String name) {
		if (channel.isConnected())
			channel.write(new LoginServerPacket(10).writeString(name)
					.writeByte(privateChatMode));
		return this;
	}

}