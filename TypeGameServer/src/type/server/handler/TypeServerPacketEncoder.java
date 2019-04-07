package type.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import type.common.handler.PacketDirection;
import type.common.packet.Packet;
import type.common.work.AttributeSaver;
import type.common.work.Utils;

public class TypeServerPacketEncoder extends MessageToByteEncoder<Packet<?>> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Packet<?> msg, ByteBuf out) throws Exception {
		int pid = Utils.getChannelAttr(AttributeSaver.state, ctx.channel()).get().m.get(PacketDirection.CLIENTBOUND).inverse().get(msg.getClass());
		Utils.l.info("ServerEncoder", "Writing packet: " + msg.getClass().getSimpleName() + "(0x" + Integer.toHexString(pid) + ") " + Utils.serialize(msg));
		out.writeInt(pid);
		msg.encode(out);
		ctx.flush();
	}

}
