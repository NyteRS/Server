package com.server2.util;

import java.nio.ByteBuffer;

import org.jboss.netty.buffer.ChannelBuffer;

public class ChannelBufferUtils {
	public static String readRS2String(ByteBuffer payload) {
		final StringBuilder builder = new StringBuilder();
		byte b = 0;

		while (payload.hasRemaining() && (b = payload.get()) != 10)
			builder.append((char) b);

		return builder.toString();
	}

	public static String readRS2String(ChannelBuffer payload) {
		final StringBuilder bldr = new StringBuilder();
		byte b;
		while (payload.readable() && (b = payload.readByte()) != 10)
			bldr.append((char) b);
		return bldr.toString();
	}

	public static void writeRS2String(ChannelBuffer payload, String string) {
		payload.writeBytes(string.getBytes());
		payload.writeByte((byte) 10);
	}

}
