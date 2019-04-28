package type.common.packet.play;

import io.netty.buffer.ByteBuf;
import type.common.listener.PacketPlayCbListener;
import type.common.packet.Packet;

public class PacketCbPlayLeftUsers implements Packet<PacketPlayCbListener> {

	public int left = 0;

	@Override
	public void decode(ByteBuf buf) throws Exception {
		left = buf.readInt();
	}

	@Override
	public void encode(ByteBuf buf) throws Exception {
		buf.writeInt(left);
	}

	@Override
	public void process(PacketPlayCbListener listener) throws Exception {
		listener.process(this);
	}

}
