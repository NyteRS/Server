package com.server2.net.codec.gameserver;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import com.server2.net.GamePacket;
import com.server2.util.ISAACCipher;

public class RS2Encoder extends OneToOneEncoder {

	private final ISAACCipher encrypter;

	public RS2Encoder(ISAACCipher encrypter) {
		this.encrypter = encrypter;
	}

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel,
			Object message) throws Exception {
		if (message instanceof GamePacket) {
			final GamePacket packet = (GamePacket) message;
			if (packet.isRaw())
				return packet.getPayload();
			else {
				/*
				 * Get the packet attributes.
				 */
				final int opcode = packet.getOpcode();
				final GamePacket.Type type = packet.getType();
				final int length = packet.getLength();

				/*
				 * Compute the required size for the buffer.
				 */
				int finalLength = length + 1;
				switch (type) {
				case VARIABLE:
					finalLength++;
					break;
				case VARIABLE_SHORT:
					finalLength += 2;
					break;
				default:
					break;
				}

				/*
				 * Create the buffer and write the opcode (and length if the
				 * packet is variable-length).
				 */
				final ChannelBuffer buffer = ChannelBuffers.buffer(finalLength);
				buffer.writeByte((byte) opcode + encrypter.getNextValue());
				switch (type) {
				case VARIABLE:
					buffer.writeByte((byte) length);
					break;
				case VARIABLE_SHORT:
					buffer.writeShort((short) length);
					break;
				default:
					break;
				}

				/*
				 * Write the packet
				 */
				buffer.writeBytes(packet.getPayload());
				return buffer;
			}
		}
		return null;
	}

}