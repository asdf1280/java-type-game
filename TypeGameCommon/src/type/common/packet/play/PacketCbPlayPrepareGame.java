package type.common.packet.play;

import io.netty.buffer.ByteBuf;
import type.common.listener.PacketPlayCbListener;
import type.common.packet.Packet;

public class PacketCbPlayPrepareGame implements Packet<PacketPlayCbListener> {

	@Override
	public void decode(ByteBuf buf) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void encode(ByteBuf buf) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void process(PacketPlayCbListener listener) throws Exception {
		listener.process(this);
	}

}
