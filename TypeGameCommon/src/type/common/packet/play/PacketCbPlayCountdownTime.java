package type.common.packet.play;

import io.netty.buffer.ByteBuf;
import type.common.listener.PacketPlayCbListener;
import type.common.packet.Packet;

public class PacketCbPlayCountdownTime implements Packet<PacketPlayCbListener> {

	public int seconds;
	public int joined;

	@Override
	public void decode(ByteBuf buf) throws Exception {
		seconds = buf.readInt();
		joined = buf.readInt();
	}

	@Override
	public void encode(ByteBuf buf) throws Exception {
		buf.writeInt(seconds);
		buf.writeInt(joined);
	}

	@Override
	public void process(PacketPlayCbListener listener) throws Exception {
		listener.process(this);
	}

}
