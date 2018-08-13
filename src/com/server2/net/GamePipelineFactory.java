package com.server2.net;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

import com.server2.ChannelHandler;
import com.server2.net.codec.gameserver.RS2Encoder;
import com.server2.net.codec.gameserver.RS2LoginDecoder;

public class GamePipelineFactory implements ChannelPipelineFactory {
	@Override
	public ChannelPipeline getPipeline() {
		final ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("encoder", new RS2Encoder(null));
		pipeline.addLast("logindecoder", new RS2LoginDecoder());
		pipeline.addLast("handler", ChannelHandler.getInstance());
		return pipeline;
	}
}