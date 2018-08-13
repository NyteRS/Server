package com.server2.net;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

import com.server2.ChannelHandler;
import com.server2.net.codec.loginserver.PacketDecoder;
import com.server2.net.codec.loginserver.PacketEncoder;

public class LoginServerPipelineFactory implements ChannelPipelineFactory {

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		final ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("encoder", new PacketEncoder());
		pipeline.addLast("decoder", new PacketDecoder());
		pipeline.addLast("handler", ChannelHandler.getInstance());
		return pipeline;
	}

}