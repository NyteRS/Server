package com.server2.net;

import org.jboss.netty.buffer.ChannelBuffer;

import com.server2.util.ChannelBufferUtils;

/**
 * Represents a single packet.
 * 
 * @author Graham Edgecombe
 * 
 */
public class GamePacket {

	/**
	 * The type of packet.
	 * 
	 * @author Graham Edgecombe
	 * 
	 */
	public enum Type {

		/**
		 * A fixed size packet where the size never changes.
		 */
		FIXED,

		/**
		 * A variable packet where the size is described by a byte.
		 */
		VARIABLE,

		/**
		 * A variable packet where the size is described by a word.
		 */
		VARIABLE_SHORT;

	}

	/**
	 * The opcode.
	 */
	private final int opcode;

	/**
	 * The type.
	 */
	private Type type;

	/**
	 * The length of the payload.
	 */
	private int length;

	/**
	 * The payload.
	 */
	private final ChannelBuffer payload;

	/**
	 * Creates a packet.
	 * 
	 * @param payload
	 */
	public GamePacket(ChannelBuffer payload) {
		opcode = -1;
		this.payload = payload;
	}

	/**
	 * Creates a packet.
	 * 
	 * @param opcode
	 *            The opcode.
	 * @param type
	 *            The type.
	 * @param payload
	 *            The payload.
	 */
	public GamePacket(int opcode, Type type, ChannelBuffer payload) {
		this.opcode = opcode;
		this.type = type;
		length = payload.readableBytes();
		this.payload = payload;
	}

	/**
	 * Reads a single byte.
	 * 
	 * @return A single byte.
	 */
	public byte get() {
		return payload.readByte();
	}

	/**
	 * Reads several bytes.
	 * 
	 * @param b
	 *            The target array.
	 */
	public void get(byte[] b) {
		payload.readBytes(b);
	}

	/**
	 * Reads a series of bytes.
	 * 
	 * @param is
	 *            The target byte array.
	 * @param offset
	 *            The offset.
	 * @param length
	 *            The length.
	 */
	public void get(byte[] is, int offset, int length) {
		for (int i = 0; i < length; i++)
			is[offset + i] = payload.readByte();
	}

	/**
	 * Reads a byte.
	 * 
	 * @return A single byte.
	 */
	public byte getByte() {
		return get();
	}

	/**
	 * Reads a type A byte.
	 * 
	 * @return A type A byte.
	 */
	public byte getByteA() {
		return (byte) (get() - 128);
	}

	/**
	 * Reads a type C byte.
	 * 
	 * @return A type C byte.
	 */
	public byte getByteC() {
		return (byte) -get();
	}

	/**
	 * Gets a type S byte.
	 * 
	 * @return A type S byte.
	 */
	public byte getByteS() {
		return (byte) (128 - get());
	}

	/**
	 * Reads an integer.
	 * 
	 * @return An integer.
	 */
	public int getInt() {
		return payload.readInt();
	}

	/**
	 * Reads a V1 integer.
	 * 
	 * @return A V1 integer.
	 */
	public int getInt1() {
		final byte b1 = payload.readByte();
		final byte b2 = payload.readByte();
		final byte b3 = payload.readByte();
		final byte b4 = payload.readByte();
		return b3 << 24 & 0xFF | b4 << 16 & 0xFF | b1 << 8 & 0xFF | b2 & 0xFF;
	}

	/**
	 * Reads a V2 integer.
	 * 
	 * @return A V2 integer.
	 */
	public int getInt2() {
		final int b1 = payload.readByte() & 0xFF;
		final int b2 = payload.readByte() & 0xFF;
		final int b3 = payload.readByte() & 0xFF;
		final int b4 = payload.readByte() & 0xFF;
		return b2 << 24 & 0xFF | b1 << 16 & 0xFF | b4 << 8 & 0xFF | b3 & 0xFF;
	}

	/**
	 * Gets the length.
	 * 
	 * @return The length.
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Reads a little-endian short.
	 * 
	 * @return A little-endian short.
	 */
	public int getLEShort() {
		int i = payload.readByte() & 0xFF | (payload.readByte() & 0xFF) << 8;
		if (i > 32767)
			i -= 0x10000;
		return i;
	}

	/**
	 * Reads a little-endian type A short.
	 * 
	 * @return A little-endian type A short.
	 */
	public int getLEShortA() {
		int i = payload.readByte() - 128 & 0xFF
				| (payload.readByte() & 0xFF) << 8;
		if (i > 32767)
			i -= 0x10000;
		return i;
	}

	/**
	 * Reads a long.
	 * 
	 * @return A long.
	 */
	public long getLong() {
		return payload.readLong();
	}

	/**
	 * Gets the opcode.
	 * 
	 * @return The opcode.
	 */
	public int getOpcode() {
		return opcode;
	}

	/**
	 * Gets the payload.
	 * 
	 * @return The payload.
	 */
	public ChannelBuffer getPayload() {
		return payload;
	}

	/**
	 * Reads a series of bytes in reverse.
	 * 
	 * @param is
	 *            The target byte array.
	 * @param offset
	 *            The offset.
	 * @param length
	 *            The length.
	 */
	public void getReverse(byte[] is, int offset, int length) {
		for (int i = offset + length - 1; i >= offset; i--)
			is[i] = payload.readByte();
	}

	/**
	 * Reads a series of type A bytes in reverse.
	 * 
	 * @param is
	 *            The target byte array.
	 * @param offset
	 *            The offset.
	 * @param length
	 *            The length.
	 */
	public void getReverseA(byte[] is, int offset, int length) {
		for (int i = offset + length - 1; i >= offset; i--)
			is[i] = getByteA();
	}

	/**
	 * Reads a RuneScape string.
	 * 
	 * @return The string.
	 */
	public String getRS2String() {
		return ChannelBufferUtils.readRS2String(payload);
	}

	/**
	 * Reads a short.
	 * 
	 * @return A short.
	 */
	public int getShort() {
		return payload.readShort();
	}

	/**
	 * Reads a type A short.
	 * 
	 * @return A type A short.
	 */
	public int getShortA() {
		int i = (payload.readByte() & 0xFF) << 8 | payload.readByte() - 128
				& 0xFF;
		if (i > 32767)
			i -= 0x10000;
		return i;
	}

	/**
	 * Gets a signed smart.
	 * 
	 * @return The signed smart.
	 */
	public int getSignedSmart() {
		final int peek = payload.getByte(payload.readerIndex());
		if (peek < 128)
			return (get() & 0xFF) - 64;
		else
			return (getShort() & 0xFFFF) - 49152;
	}

	/**
	 * Gets a smart.
	 * 
	 * @return The smart.
	 */
	public int getSmart() {
		final int peek = payload.getByte(payload.readerIndex());
		if (peek < 128)
			return get() & 0xFF;
		else
			return (getShort() & 0xFFFF) - 32768;
	}

	/**
	 * Gets a 3-byte integer.
	 * 
	 * @return The 3-byte integer.
	 */
	public int getTriByte() {
		return payload.readByte() << 16 & 0xFF | payload.readByte() << 8 & 0xFF
				| payload.readByte() & 0xFF;
	}

	/**
	 * Gets the type.
	 * 
	 * @return The type.
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Reads an unsigned byte.
	 * 
	 * @return An unsigned byte.
	 */
	public int getUnsignedByte() {
		return payload.readUnsignedByte();
	}

	/**
	 * Reads an unsigned short.
	 * 
	 * @return An unsigned short.
	 */
	public int getUnsignedShort() {
		return payload.readUnsignedShort();
	}

	/**
	 * Checks if this packet is raw. A raw packet does not have the usual
	 * headers such as opcode or size.
	 * 
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean isRaw() {
		return opcode == -1;
	}

}