package com.server2.net.codec.gameserver;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import com.server2.Constants;
import com.server2.model.entity.player.Player;
import com.server2.net.GamePacket;
import com.server2.util.ISAACCipher;
import com.server2.world.World;

public class RS2Decoder extends FrameDecoder {

	private final ISAACCipher decrypter;

	public RS2Decoder(ISAACCipher decrypter) {
		this.decrypter = decrypter;
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		/*
		 * Get the player assositated with the session.
		 */
		final Player player = World.getWorld().getLocalChannels().get(channel);
		if (player == null || !buffer.readable())
			return null;

		/*
		 * Fetch any cached opcodes and sizes, reset to -1 if not present.
		 */
		int opcode = player.getOpcode();
		int size = player.getPacketLength();

		/*
		 * If the opcode is not present.
		 */
		if (opcode == -1)
			/*
			 * Check if it can be read.
			 */
			if (buffer.readableBytes() >= 1) {
				/*
				 * Read and decrypt the opcode.
				 */
				opcode = buffer.readUnsignedByte();
				opcode = opcode - decrypter.getNextValue() & 0xFF;

				/*
				 * Find the packet size.
				 */
				size = Constants.PACKET_SIZES[opcode];

				/*
				 * Set the cached opcode and size.
				 */
				player.setOpcode(opcode);
				player.setPacketLength(size);
			} else {
				/*
				 * We need to wait for more data.
				 */
				buffer.discardReadBytes();
				return null;
			}

		/*
		 * If the packet is variable-length.
		 */
		if (size == -1)
			/*
			 * Check if the size can be read.
			 */
			if (buffer.readableBytes() >= 1) {
				/*
				 * Read the packet size and cache it.
				 */
				size = buffer.readUnsignedByte();
				if (size == 0)
					return ChannelBuffers.buffer(0);
				player.setPacketLength(size);
			} else {
				/*
				 * We need to wait for more data.
				 */
				buffer.discardReadBytes();
				return null;
			}

		/*
		 * If the packet payload (data) can be read.
		 */
		if (buffer.readableBytes() >= size) {
			/*
			 * Read it.
			 */
			final byte[] data = new byte[size];
			buffer.readBytes(data);

			/*
			 * Reset the cached opcode and sizes.
			 */
			player.setOpcode(-1);
			player.setPacketLength(-1);

			/*
			 * Produce the packet object.
			 */
			return new GamePacket(opcode, null,
					ChannelBuffers.copiedBuffer(data));
		}
		return null;
	}

}