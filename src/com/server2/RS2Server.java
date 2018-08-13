package com.server2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.server2.net.GamePipelineFactory;
import com.server2.net.LoginServerPacket;
import com.server2.net.LoginServerPipelineFactory;
import com.server2.world.World;

/**
 * Starts everything else including Netty and the <code>GameEngine</code>.
 * 
 * @author Graham Edgecombe
 * @author Ultimate1
 */
public class RS2Server {
	/**
	 * Gets the <code>GameEngine</code>.
	 * 
	 * @return The game engine.
	 */
	public static GameEngine getEngine() {
		return engine;
	}

	private final ExecutorService networkExecutor = Executors
			.newCachedThreadPool();
	private final ServerBootstrap gameBootstrap = new ServerBootstrap();
	private final ClientBootstrap loginServerBootstrap = new ClientBootstrap();
	private static final GameEngine engine = new GameEngine();

	private InetSocketAddress addr = null;

	/**
	 * Creates the server and the <code>GameEngine</code> and initializes the
	 * <code>World</code>.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs loading the world.
	 * @throws ClassNotFoundException
	 *             if a class the world loads was not found.
	 * @throws IllegalAccessException
	 *             if a class loaded by the world was not accessible.
	 * @throws InstantiationException
	 *             if a class loaded by the world was not created.
	 */
	public RS2Server() throws IOException, ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		World.getWorld().init(engine);
		gameBootstrap.setFactory(new NioServerSocketChannelFactory(
				networkExecutor, networkExecutor));
		gameBootstrap.setPipelineFactory(new GamePipelineFactory());

		loginServerBootstrap.setFactory(new NioClientSocketChannelFactory(
				networkExecutor, networkExecutor));
		loginServerBootstrap
				.setPipelineFactory(new LoginServerPipelineFactory());

		// Shut down gracefully...
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				gameBootstrap.releaseExternalResources();
				loginServerBootstrap.releaseExternalResources();
			}
		});

	}

	public RS2Server bind() throws Exception {
		// adjusts offset based on default port and node id. node id 1 means
		// 43594 + 1 - 1
		final String host = Settings.getString("sv_bindaddr").split(":")[0];
		final int port = Integer.parseInt(Settings.getString("sv_bindaddr")
				.split(":")[1]);
		gameBootstrap.bind(addr = new InetSocketAddress(host, port));
		final String lsHost = Settings.getString("ls_addr").split(":")[0];
		final int lsPort = Integer.parseInt(Settings.getString("ls_addr")
				.split(":")[1]);
		final ChannelFuture future = loginServerBootstrap
				.connect(new InetSocketAddress(lsHost, lsPort));
		future.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) {
				final Channel channel = future.getChannel();
				System.out.println("Connection to login server: "
						+ (channel.isConnected() ? "SUCCESSFUL" : "FAILED")
						+ "!");
				if (channel.isConnected())
					channel.write(new LoginServerPacket(1).writeByte(
							(byte) Settings.getLong("sv_nodeid")).writeString(
							Settings.getString("ls_pwd")));
			}
		});
		return this;
	}

	/**
	 * Starts the <code>GameEngine</code>.
	 * 
	 * @throws ExecutionException
	 *             if an error occured during background loading.
	 */
	public void start() throws ExecutionException {
		if (World.getWorld().getBackgroundLoader().getPendingTaskAmount() > 0) {
			System.out
					.println("Waiting for pending background loading tasks...");
			World.getWorld().getBackgroundLoader().waitForPendingTasks();
		}

		World.getWorld().getBackgroundLoader().shutdown();
		engine.start();
		System.out.println("Boot time : "
				+ (System.currentTimeMillis() - Server.start) / 1000
				+ " seconds. " + Settings.getString("sv_name")
				+ " is now accepting connections on " + addr);
	}

}