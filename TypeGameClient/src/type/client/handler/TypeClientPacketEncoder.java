package type.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import type.client.main.Network;
import type.common.handler.PacketDirection;
import type.common.packet.Packet;
import type.common.work.Utils;

public class TypeClientPacketEncoder extends MessageToByteEncoder<Packet<?>> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Packet<?> msg, ByteBuf out) throws Exception {
		Utils.l.info("ClientEncoder", "Writing packet class: " + msg.getClass());
		int pid = Network.state.m.get(PacketDirection.SERVERBOUND).inverse().get(msg.getClass());
		Utils.l.info("ClientEncoder", "Writing packet: " + msg.getClass().getSimpleName() + "(0x"
				+ Integer.toHexString(pid) + ") " + Utils.serialize(msg));
		out.writeInt(pid);
		msg.encode(out);
		ctx.flush();
	}

}
