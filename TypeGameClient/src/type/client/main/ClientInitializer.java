package type.client.main;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import type.client.handler.TypeClientInboundHandler;
import type.client.handler.TypeClientPacketDecoder;
import type.client.handler.TypeClientPacketEncoder;
import type.common.handler.TypeCommonPacketPrepender;
import type.common.handler.TypeCommonPacketSplitter;
import type.common.handler.TypeCommonPriorityHandler;

public class ClientInitializer extends ChannelInitializer<NioSocketChannel> {

	public TypeCommonPriorityHandler priority = new TypeCommonPriorityHandler();
	public TypeClientInboundHandler handle = new TypeClientInboundHandler();

	@Override
	protected void initChannel(NioSocketChannel ch) throws Exception {
		ChannelPipeline pipe = ch.pipeline();
		pipe.addLast("split", new TypeCommonPacketSplitter());
		pipe.addLast("prepend", new TypeCommonPacketPrepender());
		pipe.addLast("encode", new TypeClientPacketEncoder());
		pipe.addLast("decode", new TypeClientPacketDecoder());
		pipe.addLast("priority", priority);
		pipe.addLast("inbound", handle);
	}

}
