package type.server.main;

import static type.common.work.Utils.l;

import java.net.InetSocketAddress;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
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
		ChannelPipeline pipe = ch.pipeline();
//		pipe.addLast("compressencode", ZlibCodecFactory.newZlibEncoder(ZlibWrapper.GZIP, 5));
//		pipe.addLast("compressdecode", ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP));
		pipe.addLast("inboudnsplit", new TypeCommonPacketSplitter());
		pipe.addLast("prepend", new TypeCommonPacketPrepender());
		pipe.addLast("encode", new TypeServerPacketEncoder());
		pipe.addLast("decode", new TypeServerPacketDecoder());
		pipe.addLast("inboundpriority", priority);
		pipe.addLast("inboundhandle", handle);
	}

}
