package com.server2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.server2.content.constants.LotterySystem;
import com.server2.content.minigames.pc.PestControl;
import com.server2.content.misc.Starters;
import com.server2.engine.cycle.impl.GameSavingProcess;
import com.server2.model.entity.npc.NPCSize;
import com.server2.model.entity.player.commands.CommandManager;
import com.server2.model.entity.player.packets.PacketManager;
import com.server2.sql.SQLDataLoader;
import com.server2.sql.database.SQLDatabaseConnection;
import com.server2.util.ExceptionHandler;
import com.server2.util.Log;
import com.server2.util.MACAddressFetcher;
import com.server2.world.Clan;
import com.server2.world.GlobalActions;
import com.server2.world.ItemManager;
import com.server2.world.NPCManager;
import com.server2.world.PlayerManager;
import com.server2.world.ShopManager;
import com.server2.world.World;
import com.server2.world.XMLManager;
import com.server2.world.map.MapLoader;
import com.server2.world.map.ObjectDefinition;
import com.server2.world.map.Region;
import com.server2.world.objects.ObjectManager;
import com.server2.world.objects.ObjectSystem;

/**
 * @author Rene
 * @author Supah Fly
 */
public class Server {
	/* coolguy */
	public static boolean multiValve = true;
	public static int bonusExp = 1;
	public static boolean snowMode = false;
	public static boolean updateServer = false;
	private static Server INSTANCE = new Server();
	protected static long start = 0;
	private static boolean shutdownServer = false;
	private static long minutesCounter;

	public static Server getInstance() {
		return INSTANCE;
	}
	/**
	 * The connection to the game server.
	 */
	private final SQLDatabaseConnection database = new SQLDatabaseConnection("root2", "qEsC2auPXTF9HVDA", "localhost/server2", 2);

	public static int getJVMSize() {
		final Runtime runtime = Runtime.getRuntime();
		return (int) ((runtime.totalMemory() - runtime.freeMemory()) / 1024L);
	}

	public static long getMinutesCounter() {
		return minutesCounter;
	}

	public static PlayerManager getPlayerManager() {
		return PlayerManager.getSingleton();
	}

	public static String getUptime() {
		final long uptime = (System.currentTimeMillis() - start) / 1000;
		final int minutes = (int) (uptime / 60);

		if (minutes == 0 || minutes == 1)
			return minutes + " min";
		else
			return minutes + " mins";
	}

	private static void initContinuousEvents() {
		new Thread(new GameSavingProcess()).start();
	}

	public static boolean isDebugEnabled() {
		return Settings.getBoolean("sv_dbg");
	}

	public static boolean isRunningOnProtosPC() {
		return true;
	}

	public static boolean isShutdown() {
		return isShutdownServer();
	}

	public static boolean isShutdownServer() {
		return shutdownServer;
	}

	public static void main(String[] args) {

		MACAddressFetcher.initialise();

		String path = "";

		for (final String arg : args)
			path += " " + arg;

		path = path.trim();

		if (args.length == 1)
			try {
				final File file = new File(path);
				if (!file.exists()) {
					System.out.println("Please specify a valid settings file!");
					return;
				}
				Settings.load(file);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		else {
			System.out.println("Usage: Server <path to config file>");
			return;
		}
		try {
			start = System.currentTimeMillis();
			System.setOut(new Log(System.out));
			System.setErr(new Log(System.err));
			Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
			start = System.currentTimeMillis();
			System.out.println("Loading " + Settings.getString("sv_name")
					+ " (World " + Settings.getLong("sv_nodeid") + ") ...");
			System.out.println("MAC Address : " + MACAddressFetcher.result());
			NPCSize.load();
			System.out.println("Loading Object Definitions...");
			ObjectDefinition.load();
			System.out.println("Loading Clipping Regions...");
			Region.load();
			Starters.appendStarters();
			Starters.appendStarters2();
			System.out.println("Loading clans...");
			Clan.load();
			System.out.println("Initializing Handlers...");
			setShopManager(new ShopManager());
			setNpcManager(new NPCManager());
			setObjectManager(new ObjectManager());
			setItemManager(new ItemManager());
			setGlobalActions(new GlobalActions());
			PacketManager.loadAllPackets();
			CommandManager.loadAllCommands();
			//Server.getInstance().startMinutesCounter();
			World.connectDatabase();
			System.out.println("Loading All XML & SQL depedencies...");
			SQLDataLoader.initailise();
			XMLManager.load();
			LotterySystem.getInstance().initiateSystem();
			initContinuousEvents();
			System.out.println("Loading Map Objects...");
			MapLoader.initialize();
			ObjectSystem.registerAllObjects();
			World.getWorld();
			new RS2Server().bind().start();
			PestControl.getInstance();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void setGlobalActions(GlobalActions globalActions) {
		InstanceDistributor.globalActions = globalActions;
	}

	public static void setItemManager(ItemManager itemManager) {
		InstanceDistributor.itemManager = itemManager;
	}

	public static void setMinutesCounter(long minutesCounter) {
		if (Server.isDebugEnabled())
			return;

		Server.minutesCounter = minutesCounter;
		try {

			final File f = new File(Constants.CHARACTER_DIRECTORY
					+ "minutes.log");

			if (!f.exists())
				f.createNewFile();

			final BufferedWriter minuteCounter = new BufferedWriter(
					new FileWriter(f));
			minuteCounter.write(Long.toString(getMinutesCounter()));
			minuteCounter.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public static void setNpcManager(NPCManager npcManager) {
		InstanceDistributor.npcManager = npcManager;
	}

	public static void setObjectManager(ObjectManager objectManager) {
		InstanceDistributor.objectManager = objectManager;
	}

	public static void setShopManager(ShopManager shopManager) {
		InstanceDistributor.shopManager = shopManager;
	}

	public static void setShutdown(boolean shutdown) {
		setShutdownServer(shutdown);
	}

	public static void setShutdownServer(boolean shutdownServer) {
		Server.shutdownServer = shutdownServer;
	}

	private void startMinutesCounter() {
		if (Settings.getBoolean("sv_dbg"))
			// We don't need this in debug mode
			return;
		try {
			final File f = new File(Constants.CHARACTER_DIRECTORY
					+ "minutes.log");

			if (!f.exists())
				f.createNewFile();

			final BufferedReader minuteFile = new BufferedReader(
					new FileReader(f));
			Server.minutesCounter = Integer.parseInt(minuteFile.readLine());
			minuteFile.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

}