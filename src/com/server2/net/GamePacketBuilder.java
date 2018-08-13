package com.server2.net;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import com.server2.net.GamePacket.Type;
import com.server2.util.ChannelBufferUtils;

/**
 * A utility class for building packets.
 * 
 * @author Graham Edgecombe
 * 
 */
public class GamePacketBuilder {

	/**
	 * Bit mask array.
	 */
	public static final int[] BIT_MASK_OUT = new int[32];

	/**
	 * Creates the bit mask array.
	 */
	static {
		for (int i = 0; i < BIT_MASK_OUT.length; i++)
			BIT_MASK_OUT[i] = (1 << i) - 1;
	}

	/**
	 * The opcode.
	 */
	private final int opcode;

	/**
	 * The type.
	 */
	private final Type type;

	/**
	 * The payload.
	 */
	private final ChannelBuffer payload = ChannelBuffers.dynamicBuffer();

	/**
	 * The current bit position.
	 */
	private int bitPosition;

	/**
	 * Creates a raw packet builder.
	 */
	public GamePacketBuilder() {
		this(-1);
	}

	/**
	 * Creates a fixed packet builder with the specified opcode.
	 * 
	 * @param opcode
	 *            The opcode.
	 */
	public GamePacketBuilder(int opcode) {
		this(opcode, Type.FIXED);
	}

	/**
	 * Creates a packet builder with the specified opcode and type.
	 * 
	 * @param opcode
	 *            The opcode.
	 * @param type
	 *            The type.
	 */
	public GamePacketBuilder(int opcode, Type type) {
		this.opcode = opcode;
		this.type = type;
	}

	/**
	 * Finishes bit access.
	 * 
	 * @return The PacketBuilder instance, for chaining.
	 */
	public GamePacketBuilder finishBitAccess() {
		payload.writerIndex((bitPosition + 7) / 8);
		return this;
	}

	/**
	 * Get the length of the payload.
	 * 
	 * @return
	 */
	public int getLength() {
		return payload.writerIndex();
	}

	/**
	 * Checks if this packet builder is empty.
	 * 
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean isEmpty() {
		return payload.writerIndex() == 0;
	}

	/**
	 * Writes a byte.
	 * 
	 * @param b
	 *            The byte to write.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public GamePacketBuilder put(byte b) {
		payload.writeByte(b);
		return this;
	}

	/**
	 * Writes an array of bytes.
	 * 
	 * @param b
	 *            The byte array.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public GamePacketBuilder put(byte[] b) {
		payload.writeBytes(b);
		return this;
	}

	/**
	 * Puts a sequence of bytes in the buffer.
	 * 
	 * @param data
	 *            The bytes.
	 * @param offset
	 *            The offset.
	 * @param length
	 *            The length.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public GamePacketBuilder put(byte[] data, int offset, int length) {
		payload.writeBytes(data, offset, length);
		return this;
	}

	/**
	 * Puts an <code>IoBuffer</code>.
	 * 
	 * @param buf
	 *            The buffer.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public GamePacketBuilder put(ChannelBuffer buffer) {
		final byte[] bytes = new byte[buffer.readableBytes()];
		buffer.markReaderIndex();
		try {
			buffer.readBytes(bytes);
		} finally {
			buffer.resetReaderIndex();
		}
		put(bytes);
		return this;
	}

	/**
	 * Puts {@code numBits} into the buffer with the value {@code value}.
	 * 
	 * @param numBits
	 *            The number of bits to put into the buffer.
	 * @param value
	 *            The value.
	 * @throws IllegalStateException
	 *             if the builder is not in bit access mode.
	 * @throws IllegalArgumentException
	 *             if the number of bits is not between 1 and 31 inclusive.
	 */
	public void putBits(int numBits, int value) {
		if (numBits < 0 || numBits > 32)
			throw new IllegalArgumentException(
					"Number of bits must be between 1 and 32 inclusive");

		int bytePos = bitPosition >> 3;
		int bitOffset = 8 - (bitPosition & 7);
		bitPosition += numBits;

		int requiredSpace = bytePos - payload.writerIndex() + 1;
		requiredSpace += (numBits + 7) / 8;
		payload.ensureWritableBytes(requiredSpace);

		for (; numBits > bitOffset; bitOffset = 8) {
			int tmp = payload.getByte(bytePos);
			tmp &= ~BIT_MASK_OUT[bitOffset];
			tmp |= value >> numBits - bitOffset & BIT_MASK_OUT[bitOffset];
			payload.setByte(bytePos++, tmp);
			numBits -= bitOffset;
		}
		if (numBits == bitOffset) {
			int tmp = payload.getByte(bytePos);
			tmp &= ~BIT_MASK_OUT[bitOffset];
			tmp |= value & BIT_MASK_OUT[bitOffset];
			payload.setByte(bytePos, tmp);
		} else {
			int tmp = payload.getByte(bytePos);
			tmp &= ~(BIT_MASK_OUT[numBits] << bitOffset - numBits);
			tmp |= (value & BIT_MASK_OUT[numBits]) << bitOffset - numBits;
			payload.setByte(bytePos, tmp);
		}
	}

	/**
	 * Puts a type-A byte in the buffer.
	 * 
	 * @param val
	 *            The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public GamePacketBuilder putByteA(byte val) {
		payload.writeByte((byte) (val + 128));
		return this;
	}

	/**
	 * Writes a type-A byte.
	 * 
	 * @param val
	 *            The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public GamePacketBuilder putByteA(int val) {
		payload.writeByte((byte) (val + 128));
		return this;
	}

	/**
	 * Puts a type-C byte in the buffer.
	 * 
	 * @param val
	 *            The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public GamePacketBuilder putByteC(byte val) {
		payload.writeByte((byte) -val);
		return this;
	}

	/**
	 * Writes a type-C byte.
	 * 
	 * @param val
	 *            The value to write.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public GamePacketBuilder putByteC(int val) {
		put((byte) -val);
		return this;
	}

	/**
	 * Puts a type-S byte in the buffer.
	 * 
	 * @param val
	 *            The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public GamePacketBuilder putByteS(byte val) {
		payload.writeByte((byte) (128 - val));
		return this;
	}

	/**
	 * Writes an integer.
	 * 
	 * @param i
	 *            The integer.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public GamePacketBuilder putInt(int i) {
		payload.writeInt(i);
		return this;
	}

	/**
	 * Writes a type-1 integer.
	 * 
	 * @param val
	 *            The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public GamePacketBuilder putInt1(int val) {
		payload.writeByte((byte) (val >> 8));
		payload.writeByte((byte) val);
		payload.writeByte((byte) (val >> 24));
		payload.writeByte((byte) (val >> 16));
		return this;
	}

	/**
	 * Writes a type-2 integer.
	 * 
	 * @param val
	 *            The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public GamePacketBuilder putInt2(int val) {
		payload.writeByte((byte) (val >> 16));
		payload.writeByte((byte) (val >> 24));
		payload.writeByte((byte) val);
		payload.writeByte((byte) (val >> 8));
		return this;
	}

	/**
	 * Writes a little-endian integer.
	 * 
	 * @param val
	 *            The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public GamePacketBuilder putLEInt(int val) {
		payload.writeByte((byte) val);
		payload.writeByte((byte) (val >> 8));
		payload.writeByte((byte) (val >> 16));
		payload.writeByte((byte) (val >> 24));
		return this;
	}

	/**
	 * Writes a little-endian short.
	 * 
	 * @param val
	 *            The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public GamePacketBuilder putLEShort(int val) {
		payload.writeByte((byte) val);
		payload.writeByte((byte) (val >> 8));
		return this;
	}

	/**
	 * Writes a little endian type-A short.
	 * 
	 * @param val
	 *            The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public GamePacketBuilder putLEShortA(int val) {
		payload.writeByte((byte) (val + 128));
		payload.writeByte((byte) (val >> 8));
		return this;
	}

	/**
	 * Writes a long.
	 * 
	 * @param l
	 *            The long.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public GamePacketBuilder putLong(long l) {
		payload.writeLong(l);
		return this;
	}

	/**
	 * Puts a series of reversed bytes in the buffer.
	 * 
	 * @param is
	 *            The source byte array.
	 * @param offset
	 *            The offset.
	 * @param length
	 *            The length.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public GamePacketBuilder putReverse(byte[] is, int offset, int length) {
		for (int i = offset + length - 1; i >= offset; i--)
			payload.writeByte(is[i]);
		return this;
	}

	/**
	 * Puts a series of reversed type-A bytes in the buffer.
	 * 
	 * @param is
	 *            The source byte array.
	 * @param offset
	 *            The offset.
	 * @param length
	 *            The length.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public GamePacketBuilder putReverseA(byte[] is, int offset, int length) {
		for (int i = offset + length - 1; i >= offset; i--)
			putByteA(is[i]);
		return this;
	}

	/**
	 * Writes a RuneScape string.
	 * 
	 * @param string
	 *            The string to write.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public GamePacketBuilder putRS2String(String string) {
		ChannelBufferUtils.writeRS2String(payload, string);
		return this;
	}

	/**
	 * Writes a short.
	 * 
	 * @param s
	 *            The short.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public GamePacketBuilder putShort(int s) {
		payload.writeShort((short) s);
		return this;
	}

	/**
	 * Writes a type-A short.
	 * 
	 * @param val
	 *            The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public GamePacketBuilder putShortA(int val) {
		payload.writeByte((byte) (val >> 8));
		payload.writeByte((byte) (val + 128));
		return this;
	}

	/**
	 * Puts a byte or short for signed use.
	 * 
	 * @param val
	 *            The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public GamePacketBuilder putSignedSmart(int val) {
		if (val >= 128)
			putShort(val + 49152);
		else
			put((byte) (val + 64));
		return this;
	}

	/**
	 * Puts a byte or short.
	 * 
	 * @param val
	 *            The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public GamePacketBuilder putSmart(int val) {
		if (val >= 128)
			putShort(val + 32768);
		else
			put((byte) val);
		return this;
	}

	/**
	 * Puts a 3-byte integer.
	 * 
	 * @param val
	 *            The value.
	 * @return The PacketBuilder instance, for chaining.
	 */
	public GamePacketBuilder putTriByte(int val) {
		payload.writeByte((byte) (val >> 16));
		payload.writeByte((byte) (val >> 8));
		payload.writeByte((byte) val);
		return this;
	}

	/**
	 * Starts bit access.
	 * 
	 * @return The PacketBuilder instance, for chaining.
	 */
	public GamePacketBuilder startBitAccess() {
		bitPosition = payload.writerIndex() * 8;
		return this;
	}

	/**
	 * Converts this PacketBuilder to a packet.
	 * 
	 * @return The Packet object.
	 */
	public GamePacket toPacket() {
		return new GamePacket(opcode, type, payload);
	}

}
