package com.server2.world.map;

public class ByteStream {

	private final byte[] buffer;
	private int offset;

	public ByteStream(byte[] buffer) {
		this.buffer = buffer;
		offset = 0;
	}

	public byte getByte() {
		return buffer[offset++];
	}

	public int getInt() {
		return (getUByte() << 24) + (getUByte() << 16) + (getUByte() << 8)
				+ getUByte();
	}

	public long getLong() {
		return (getUByte() << 56) + (getUByte() << 48) + (getUByte() << 40)
				+ (getUByte() << 32) + (getUByte() << 24) + (getUByte() << 16)
				+ (getUByte() << 8) + getUByte();
	}

	public int getUByte() {
		return buffer[offset++] & 0xff;
	}

	public int getUShort() {
		return (getUByte() << 8) + getUByte();
	}

	public int getUSmart() {
		final int i = buffer[offset] & 0xff;
		if (i < 128)
			return getUByte();
		else
			return getUShort() - 32768;
	}

	public int length() {
		return buffer.length;
	}

	public byte[] read(int length) {
		final byte[] b = new byte[length];
		for (int i = 0; i < length; i++)
			b[i] = buffer[offset++];
		return b;
	}

	public void setOffset(int position) {
		offset = position;
	}

	public void setOffset(long position) {
		offset = (int) position;
	}

	public void skip(int length) {
		offset += length;
	}

}