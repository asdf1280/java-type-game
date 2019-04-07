package type.common.packet.match;

import io.netty.buffer.ByteBuf;
import type.common.listener.PacketMatchSbListener;
import type.common.packet.Packet;

public class PacketSbMatchCancel implements Packet<PacketMatchSbListener> {

	@Override
	public void decode(ByteBuf buf) throws Exception {
	}

	@Override
	public void encode(ByteBuf buf) throws Exception {
	}

	@Override
	public void process(PacketMatchSbListener listener) throws Exception {
		listener.process(this);
	}

}
