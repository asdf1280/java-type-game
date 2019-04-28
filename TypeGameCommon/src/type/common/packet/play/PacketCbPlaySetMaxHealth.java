package type.common.packet.play;

import io.netty.buffer.ByteBuf;
import type.common.listener.PacketPlayCbListener;
import type.common.packet.Packet;

public class PacketCbPlaySetMaxHealth implements Packet<PacketPlayCbListener> {

	public double maxhealth = 0;

	@Override
	public void decode(ByteBuf buf) throws Exception {
		maxhealth = buf.readDouble();
	}

	@Override
	public void encode(ByteBuf buf) throws Exception {
		buf.writeDouble(maxhealth);
	}

	@Override
	public void process(PacketPlayCbListener listener) throws Exception {
		listener.process(this);
	}

}
