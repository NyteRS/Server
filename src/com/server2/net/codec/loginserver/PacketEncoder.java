package com.server2.net.codec.loginserver;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import com.server2.net.LoginServerPacket;

public class PacketEncoder extends OneToOneEncoder {

	@Override
	protected Object encode(ChannelHandlerContext arg0, Channel session,
			Object message) throws Exception {
		ChannelBuffer buffer = null;
		if (message instanceof LoginServerPacket) {
			final LoginServerPacket packet = (LoginServerPacket) message;
			buffer = ChannelBuffers.buffer(packet.getLength() + 2);
			buffer.writeShort(packet.getLength());
			buffer.writeBytes(packet.getOutBuffer());
		}
		return buffer;
	}

}