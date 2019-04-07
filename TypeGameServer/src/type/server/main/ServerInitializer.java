package type.server.main;

import static type.common.work.Utils.l;

import java.net.InetSocketAddress;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import type.common.handler.TypeCommonPacketPrepender;
import type.common.handler.TypeCommonPacketSplitter;
import type.common.handler.TypeCommonPriorityHandler;
import type.server.handler.TypeServerInboundHandler;
import type.server.handler.TypeServerPacketDecoder;
import type.server.handler.TypeServerPacketEncoder;

public class ServerInitializer extends ChannelInitializer<NioSocketChannel> {

	@Override
	protected void initChannel(NioSocketChannel ch) throws Exception {
		l.info("New connection from " + ((InetSocketAddress) ch.remoteAddress()).getHostName());
		TypeServerInboundHandler handle = new TypeServerInboundHandler();
		TypeCommonPriorityHandler priority = new TypeCommonPriorityHandler();
		ch.pipeline().addLast("inboudnsplit", new TypeCommonPacketSplitter())
				.addLast("prepend", new TypeCommonPacketPrepender()).addLast("encode", new TypeServerPacketEncoder())
				.addLast("decode", new TypeServerPacketDecoder()).addLast("inboundpriority", priority)
				.addLast("inboundhandle", handle);
	}

}
