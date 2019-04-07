package type.common.packet.singleplayer;

import io.netty.buffer.ByteBuf;
import type.common.listener.PacketSingleSbListener;
import type.common.packet.Packet;

public class PacketSbSingleStopPlay implements Packet<PacketSingleSbListener> {

	@Override
	public void decode(ByteBuf buf) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void encode(ByteBuf buf) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void process(PacketSingleSbListener listener) throws Exception {
		listener.process(this);
	}

}
