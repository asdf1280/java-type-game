package type.common.packet.login;

import io.netty.buffer.ByteBuf;
import type.common.listener.PacketLoginSbListener;
import type.common.packet.Packet;

public class PacketSbLoginAnonymous implements Packet<PacketLoginSbListener> {

	@Override
	public void decode(ByteBuf buf) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void encode(ByteBuf buf) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void process(PacketLoginSbListener listener) throws Exception {
		listener.process(this);
	}

}
