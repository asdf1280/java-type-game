package type.common.packet.match;

import io.netty.buffer.ByteBuf;
import type.common.listener.PacketMatchCbListener;
import type.common.packet.Packet;

public class PacketCbMatchStarted implements Packet<PacketMatchCbListener> {

	@Override
	public void decode(ByteBuf buf) throws Exception {
	}

	@Override
	public void encode(ByteBuf buf) throws Exception {
	}

	@Override
	public void process(PacketMatchCbListener listener) throws Exception {
		listener.process(this);
	}
}
