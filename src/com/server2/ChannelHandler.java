package com.server2;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import com.server2.engine.task.impl.GameMessageTask;
import com.server2.net.GamePacket;
import com.server2.net.LoginServerPacket;
import com.server2.world.World;

/**
 * The <code>ChannelHandler</code>.
 * 
 * @author Rene
 */

public class ChannelHandler extends SimpleChannelUpstreamHandler {

	private static final ChannelHandler INSTANCE = new ChannelHandler();

	private static final ConcurrentHashMap<String, AtomicInteger> connections = new ConcurrentHashMap<String, AtomicInteger>();

	public static ChannelHandler getInstance() {
		return INSTANCE;
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent event)
			throws Exception {
		final Channel channel = ctx.getChannel();
		final String ip = ((InetSocketAddress) channel.getRemoteAddress())
				.getAddress().getHostAddress();
		final AtomicInteger count = connections.get(ip);
		if (count != null)
			count.decrementAndGet();
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx,
			ChannelStateEvent event) {
		final Channel channel = ctx.getChannel();

		if (channel != null) {
			final String ip = ((InetSocketAddress) channel.getRemoteAddress())
					.getAddress().getHostAddress();
			AtomicInteger count = connections.get(ip);

			if (count == null) {
				count = new AtomicInteger(0);
				connections.putIfAbsent(ip, count);
			}

			final int c = count.incrementAndGet();

			if (c > Settings.getLong("sv_connthrottle")) {
				// System.out.println("Connection rejected from " + ip +
				// ": CONNECTION_LIMIT_EXCEEDED");
				channel.close();
				return;
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		ctx.getChannel().close();
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent event)
			throws Exception {
		final Object message = event.getMessage();
		if (message == null)
			return;
		if (message instanceof GamePacket)
			World.getWorld().submit(
					new GameMessageTask(ctx.getChannel(), message));
		else if (message instanceof LoginServerPacket) {
			final LoginServerPacket packet = (LoginServerPacket) message;
			final int opcode = packet.readByte();
			try {
				World.getLoginServerConnection().handlePacket(opcode, packet,
						ctx.getChannel());
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}

}