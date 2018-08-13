package com.server2.net;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public class LoginServerPacket {

	private byte[] inBuffer;
	private ChannelBuffer outBuffer;
	private int caret;

	public LoginServerPacket(byte[] inBuffer) {
		this.inBuffer = inBuffer;
	}

	public LoginServerPacket(int opcode) {
		outBuffer = ChannelBuffers.dynamicBuffer();
		writeByte(opcode);
	}

	public int getLength() {
		return outBuffer.writerIndex();
	}

	public ChannelBuffer getOutBuffer() {
		return outBuffer;
	}

	public int readByte() {
		return inBuffer[caret++] & 0xff;
	}

	public int readInt() {
		return readShort() << 16 | readShort();
	}

	public long readLong() {
		long value = 0;
		value |= (long) readByte() << 56L;
		value |= (long) readByte() << 48L;
		value |= (long) readByte() << 40L;
		value |= (long) readByte() << 32L;
		value |= (long) readByte() << 24L;
		value |= (long) readByte() << 16L;
		value |= (long) readByte() << 8L;
		value |= readByte();
		return value;
	}

	public int readShort() {
		return readByte() << 8 | readByte();
	}

	public String readString() {
		int c;
		final StringBuilder builder = new StringBuilder();
		while ((c = readByte()) != 10)
			builder.append((char) c);
		return builder.toString();
	}

	public LoginServerPacket writeByte(int i) {
		outBuffer.writeByte(i);
		return this;
	}

	public LoginServerPacket writeInt(int i) {
		outBuffer.writeInt(i);
		return this;
	}

	public LoginServerPacket writeLong(long l) {
		outBuffer.writeLong(l);
		return this;
	}

	public LoginServerPacket writeShort(int i) {
		outBuffer.writeShort(i);
		return this;
	}

	public LoginServerPacket writeString(String s) {
		for (final byte b : s.getBytes())
			writeByte(b);
		writeByte(10);
		return this;
	}

}