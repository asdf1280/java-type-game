package type.common.packet.all;

import io.netty.buffer.ByteBuf;
import type.common.listener.PacketAllSbListener;
import type.common.packet.Packet;

public class PacketSbAllClose implements Packet<PacketAllSbListener> {

	@Override
	public void decode(ByteBuf buf) throws Exception {
		
	}

	@Override
	public void encode(ByteBuf buf) throws Exception {
		
	}

	@Override
	public void process(PacketAllSbListener listener) throws Exception {
		listener.process(this);
	}

}
