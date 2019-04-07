package type.common.packet.user;

import io.netty.buffer.ByteBuf;
import type.common.listener.PacketUserSbListener;
import type.common.packet.Packet;

public class PacketSbUserStartMatchmake implements Packet<PacketUserSbListener> {

	@Override
	public void decode(ByteBuf buf) throws Exception {
	}

	@Override
	public void encode(ByteBuf buf) throws Exception {
	}

	@Override
	public void process(PacketUserSbListener listener) throws Exception {
		listener.process(this);
	}

}
