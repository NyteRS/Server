package com.server2.net.codec.gameserver;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.SecureRandom;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;

import com.server2.model.PlayerDetails;
import com.server2.net.GamePacketBuilder;
import com.server2.net.codec.gameserver.RS2LoginDecoder.LoginState;
import com.server2.util.ChannelBufferUtils;
import com.server2.util.ISAACCipher;
import com.server2.util.NameUtils;
import com.server2.world.World;

public class RS2LoginDecoder extends ReplayingDecoder<LoginState> {

	public enum LoginState {
		STAGE_ONE, STAGE_TWO
	}

	private static final SecureRandom RANDOM = new SecureRandom();

	public RS2LoginDecoder() {
		super(LoginState.STAGE_ONE);
	}

	@Override
	protected Object decode(ChannelHandlerContext context, Channel channel,
			ChannelBuffer buffer, LoginState loginState) throws Exception {
		switch (loginState) {
		case STAGE_ONE:
			if (buffer.readableBytes() < 2)
				throw new Exception("Not enough data in login block!"); // TODO:
																		// Flood
																		// Protect
			final int request = buffer.readUnsignedByte();
			@SuppressWarnings("unused")
			final int nameHash = buffer.readByte();
			if (request != 14)
				throw new Exception("Invalid login request: reqiest=" + request
						+ ".");
			channel.write(new GamePacketBuilder().putLong(0).put((byte) 0)
					.putLong(RANDOM.nextLong()).toPacket());
			checkpoint(LoginState.STAGE_TWO);
		case STAGE_TWO:
			if (buffer.readableBytes() < 2)
				throw new Exception("Not enough data in login block!");
			final int loginType = buffer.readByte();
			if (loginType != 16 && loginType != 18)
				throw new Exception("Invalid login type: loginType="
						+ loginType + ".");

			final int blockLength = buffer.readUnsignedByte();
			if (blockLength > buffer.readableBytes())
				throw new Exception("Not enough data in login block!");

			final int magicId = buffer.readUnsignedByte();
			if (magicId != 255)
				throw new Exception("Invalid magic id!");

			final int version = buffer.readUnsignedShort();

			@SuppressWarnings("unused")
			final int lowMemory = buffer.readByte();

			for (int i = 0; i < 9; i++)
				buffer.readInt(); // check crcs TODO: add this functionality in
									// with update server

			final int rsaLength = buffer.readUnsignedByte();
			// rsaLength = buffer.readUnsignedByte();
			// System.out.println("rsa block length " + rsaLength + ", total " +
			// blockLength);
			final byte[] encBlock = new byte[rsaLength];
			buffer.readBytes(encBlock);
			final ByteBuffer decryptedBlock = ByteBuffer.wrap(new BigInteger(
					encBlock)/*
							 * .modPow(Constants.PRIVATE_RSA_EXPONENT,
							 * Constants.PRIVATE_RSA_MODULUS)
							 */.toByteArray());

			final int rsaOpcode = decryptedBlock.get(); // actually, this is
														// used to verify that
														// the rsa block has
														// been decrypted
														// correctly.

			if (rsaOpcode != 10)
				throw new Exception("Invalid RSA opcode: rsaOpcode="
						+ rsaOpcode + ".");

			// System.out.println("rsa block length " + rsaLength + ", dec: " +
			// decryptedBlock.remaining());

			final long clientKey = decryptedBlock.getLong();
			final long serverKey = decryptedBlock.getLong();

			final int[] isaacSeed = { (int) (clientKey >> 32), (int) clientKey,
					(int) (serverKey >> 32), (int) serverKey };
			final ISAACCipher inCipher = new ISAACCipher(isaacSeed);

			for (int i = 0; i < isaacSeed.length; i++)
				isaacSeed[i] += 50;

			final ISAACCipher outCipher = new ISAACCipher(isaacSeed);

			@SuppressWarnings("unused")
			final int uid = decryptedBlock.getInt(); // thank god this isn't
														// being used
			// right now, less code to rip out
			// and replace.
			final String username = NameUtils.formatDisplayName(
					ChannelBufferUtils.readRS2String(decryptedBlock)).trim();
			final String password = ChannelBufferUtils
					.readRS2String(decryptedBlock);
			World.getWorld().loadPlayer(
					new PlayerDetails(context.getChannel(), username, password,
							version, inCipher, outCipher));
			channel.getPipeline().replace("logindecoder", "decoder",
					new RS2Decoder(inCipher));
			channel.getPipeline().replace("encoder", "encoder",
					new RS2Encoder(outCipher));
			if (buffer.readable())
				return new Object[] { buffer.readBytes(buffer.readableBytes()) };
			else
				return null;
		default:
			return null;
		}
	}

}