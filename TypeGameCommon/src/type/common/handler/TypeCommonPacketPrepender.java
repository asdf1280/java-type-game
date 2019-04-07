package type.common.handler;

import static type.common.work.Utils.l;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class TypeCommonPacketPrepender extends MessageToByteEncoder<ByteBuf> {

	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
		msg.markReaderIndex();
		int i = msg.readableBytes();
		l.fine("[Prepender] Writing to processor: " + i);
		out.writeInt(i);
		out.writeBytes(msg.readBytes(i));
	}

}
