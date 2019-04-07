package type.common.handler;

import static type.common.work.Utils.l;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class TypeCommonPacketSplitter extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		in.markReaderIndex();
		if(in.readableBytes() < 4) {
			l.fine("[Splitter] Not enough bytes to read integer.");
			in.resetReaderIndex();
			return;
		}
		int i = in.readInt();
		l.fine("[Splitter] Packet length: " + i);
		l.fine("[Splitter] Available bytes from client: " + in.readableBytes());
		if(in.readableBytes() < i) {
			l.fine("[Splitter] Fragmented packet detected. Waiting for next part...");
			in.resetReaderIndex();
			return;
		}
		l.fine("[Splitter] Writing packet.");
		out.add(in.readBytes(i));
	}

}
