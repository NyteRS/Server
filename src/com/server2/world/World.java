package com.server2.world;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelLocal;

import com.server2.GameEngine;
import com.server2.InstanceDistributor;
import com.server2.Server;
import com.server2.Settings;
import com.server2.content.anticheat.XLogPrevention;
import com.server2.content.skills.dungeoneering.Dungeoneering;
import com.server2.content.skills.hunter.Hunter;
import com.server2.engine.cycle.TickManager;
import com.server2.engine.cycle.Tickable;
import com.server2.engine.task.Task;
import com.server2.engine.task.impl.CleanupTask;
import com.server2.event.EventManager;
import com.server2.event.impl.CastlewarsEvent;
import com.server2.event.impl.DonationReminderEvent;
import com.server2.event.impl.EntityCleanupEvent;
import com.server2.event.impl.FightPitsEvent;
import com.server2.event.impl.GroundItemEvent;
import com.server2.event.impl.HunterEvent;
import com.server2.event.impl.PlayerOnlineCalculation;
import com.server2.event.impl.ShopEvent;
import com.server2.event.impl.UpdateEvent;
import com.server2.event.impl.VoteReminderEvent;
import com.server2.model.PlayerDetails;
import com.server2.model.combat.additions.MultiCannon;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.net.GamePacketBuilder;
import com.server2.net.LoginServerConnection;
import com.server2.sql.database.SQLDatabaseConnection;
import com.server2.util.BanProcessor;
import com.server2.util.BlockingExecutorService;
import com.server2.util.LoginResponse;
import com.server2.util.Misc;
import com.server2.util.PlayerLoading;
import com.server2.util.PlayerSaving;

public class World {
	public static void connectDatabase() {
		gameDatabase.initialise("game");
	}

	public static SQLDatabaseConnection getGameDatabase() {
		return gameDatabase;
	}

	private GameEngine engine = null;
	private EventManager eventManager = null;
	private final ChannelLocal<Player> localChannels = new ChannelLocal<Player>();
	private final BlockingExecutorService backgroundLoader = new BlockingExecutorService(
			Executors.newSingleThreadExecutor());
	private final TickManager tickManager = new TickManager();
	private static final World INSTANCE = new World();

	private static final LoginServerConnection loginServerConnection = new LoginServerConnection();

	private static final SQLDatabaseConnection gameDatabase = new SQLDatabaseConnection(
			Settings.getString("ds_usr"), Settings.getString("ds_pwd"),
			Settings.getString("ds_addr"), 1);

	public static LoginServerConnection getLoginServerConnection() {
		return loginServerConnection;
	}

	public static World getWorld() {
		return INSTANCE;
	}

	public BlockingExecutorService getBackgroundLoader() {
		return backgroundLoader;
	}

	public GameEngine getEngine() {
		return engine;
	}

	public EventManager getEventManager() {
		return eventManager;
	}

	public ChannelLocal<Player> getLocalChannels() {
		return localChannels;
	}

	public void init(GameEngine engine) {
		this.engine = engine;
		eventManager = new EventManager(engine);
		eventManager.submit(new UpdateEvent());
		eventManager.submit(new GroundItemEvent());
		eventManager.submit(new ShopEvent());
		eventManager.submit(new FightPitsEvent());
		eventManager.submit(new CastlewarsEvent());
		eventManager.submit(new HunterEvent());
		eventManager.submit(new EntityCleanupEvent());
		eventManager.submit(new PlayerOnlineCalculation());
		eventManager.submit(new DonationReminderEvent());
		eventManager.submit(new VoteReminderEvent());
		eventManager.submit(new CleanupTask());
	}

	public void loadPlayer(final PlayerDetails pd) {
		submit(new Runnable() {
			@Override
			public void run() {
				final Player client = new Player(pd.getSession());
				final String connectedFrom = ((InetSocketAddress) pd
						.getSession().getRemoteAddress()).getAddress()
						.getHostAddress();
				int code = 2;
				final String name = pd.getName().toLowerCase();
				if (pd.getVersion() < Settings.getLong("cl_minversion"))
					code = LoginResponse.LOGIN_RESPONSE_UPDATED;
				else if (pd.getName().length() < 3 || pd.getName().length() > 12)
					code = LoginResponse.LOGIN_RESPONSE_INVALID_CREDENTIALS;
				// else if (!pd.getPassword().equals(client.getPassword())) //
				// to fix invalid username and login, being called too early?
				// code = LoginResponse.LOGIN_RESPONSE_INVALID_CREDENTIALS;
				else if (Server.updateServer)
					code = LoginResponse.LOGIN_RESPONSE_SERVER_BEING_UPDATED;
				else if (PlayerManager.getSingleton().getPlayerCount() >= Settings
						.getLong("sv_maxclients"))
					code = LoginResponse.LOGIN_RESPONSE_WORLD_FULL;
				else if (PlayerManager.getSingleton().isPlayerOn(
						pd.getName().toLowerCase()))
					code = LoginResponse.LOGIN_RESPONSE_ACCOUNT_ONLINE;
				if (name.contains("  ") || name.contains("	")
						|| name.contains("	 ") || name.contains("	  ")
						|| name.contains("		") || name.contains("		 ")
						|| name.contains("		  ") || name.contains("			")
						|| name.contains("			 ") || name.contains("			  ")
						|| name.contains("				") || name.contains("mod")
						|| name.contains("owner") || name.contains("admin")
						|| name.contains("Mod") || name.contains("Admin")
						|| name.contains("Owner"))
					code = LoginResponse.LOGIN_RESPONSE_INVALID_CREDENTIALS;

				if (!name.matches("[A-Za-z0-9 ]+"))
					code = LoginResponse.LOGIN_RESPONSE_INVALID_CREDENTIALS;

				if (name.length() > 12)
					code = LoginResponse.LOGIN_RESPONSE_INVALID_CREDENTIALS;
				if (pd.getPassword().contains("'")
						|| pd.getPassword().contains("`"))
					code = LoginResponse.LOGIN_RESPONSE_INVALID_CREDENTIALS;

				Object[] loginServerResponse = null;
				if (code == 2) {
					loginServerResponse = World.getLoginServerConnection()
							.register(name, pd.getPassword().trim(),
									connectedFrom);
					code = (Integer) loginServerResponse[0];
				}

				if (code != 2) {

					final GamePacketBuilder response = new GamePacketBuilder();
					response.put((byte) code);
					pd.getSession().write(response.toPacket())
							.addListener(new ChannelFutureListener() {
								@Override
								public void operationComplete(ChannelFuture arg0)
										throws Exception {
									arg0.getChannel().close();
								}
							});
				} else {

					if (pd.getName().equalsIgnoreCase("Vendetta")) {
						System.out.println("What the fuck.");
					}
					// Send login response
					final GamePacketBuilder response = new GamePacketBuilder();
					response.put((byte) code);
					if (code == 2) {

						response.put((byte) client.getPrivileges()).put(
								(byte) 0);
						client.write(response.toPacket());
						// Log in
						client.setUsername(pd.getName());
						PlayerLoading.loadPlayerCredentials(client);
						try {
							if (pd.getPassword() != null
									&& client.getPassword() != null
									&& client.getPassword() != ""
									&& !client.getPassword().equals(
											pd.getPassword())
									|| BanProcessor.checkPlayerBanned(client
											.getUsername())
									|| BanProcessor
											.checkPlayerIP(connectedFrom))
								client.getChannel()
										.write(response.toPacket())
										.addListener(
												new ChannelFutureListener() {
													@Override
													public void operationComplete(
															ChannelFuture arg0)
															throws Exception {
														arg0.getChannel()
																.close();
													}
												});
							else {
								client.setPassword(pd.getPassword());
								client.connectedFrom = connectedFrom;
								localChannels.set(pd.getSession(), client);
								PlayerManager.getSingleton().addClient(client);
								loginServerConnection.finaliseLogin(client);
								PlayerLoading.loadPlayer(client);

							}
						} catch (final Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
	}

	/*
	 * public static String getCustomStackTrace(Throwable aThrowable) { // add
	 * the class name and any message passed to constructor final StringBuilder
	 * result = new StringBuilder("Duh Hello ");
	 * result.append(aThrowable.toString()); final String NEW_LINE =
	 * System.getProperty("line.separator"); result.append(NEW_LINE);
	 * 
	 * // add each element of the stack trace for (StackTraceElement element :
	 * aThrowable.getStackTrace()) { result.append(element);
	 * result.append(NEW_LINE); } return result.toString(); }
	 */

	public void sendGlobalMessage(String message) {
		for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
			if (PlayerManager.getSingleton().getPlayers()[i] == null)
				continue;

			PlayerManager.getSingleton().getPlayers()[i].getActionSender()
					.sendMessage(Misc.capitalizeFirstLetter(message));
		}
	}

	public void submit(Runnable runnable) {
		engine.submitWork(runnable);
	}

	public void submit(Task task) {
		engine.pushTask(task);
	}

	public void submit(Tickable tickable) {
		tickManager.submit(tickable);
	}

	public boolean unregister(final Player player) {
		if (!XLogPrevention.canLogout(player))
			return false;
		Player client = player;
		player.gameTime += System.currentTimeMillis() - player.loggedInAt;
		if (client.getExtraData().containsKey("shop"))
			client.getExtraData().remove("shop");
		if (client.tradingWith != -1)
			client.getTrading().declineTrade();
		// picks up any pets the player contains
		if (client.pet != null)
			client.pet.pickUpEmergancy(client);
		Clan.leave(client);
		// Cancel all world ticks
		client.setStopRequired();

		// Stop all running cycleevents
		client.getPlayerEventHandler().stopEvents(player);

		// Does the user have a cannon?

		MultiCannon.getInstance().onDisconnect(client);

		// Does the user have any Thzaar monsters runnin'?

		InstanceDistributor.getTzhaarCave().onDisconnect(client);

		// Does the player need to stop a dungeon?
		Dungeoneering.getInstance().onDisconnect(client);
		// Remove the familiar if the player has one
		if (client.getFamiliar() != null)
			NPC.removeNPC(client.getFamiliar(), 17000);

		// Remove hunter traps.
		Hunter.getInstance().onDisconnect(client);
		// Now save the player
		PlayerSaving.savePlayer(player);

		// And then remove the player from the world..
		client.destruct();
		client.getChannel().close();
		localChannels.remove(client.getChannel());
		loginServerConnection.unregister(player.getUsername());
		PlayerManager.getSingleton().removePlayer(player);
		client = null;
		return true;
	}
}