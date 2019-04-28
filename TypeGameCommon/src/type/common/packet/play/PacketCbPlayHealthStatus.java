package type.common.packet.play;

import io.netty.buffer.ByteBuf;
import type.common.listener.PacketPlayCbListener;
import type.common.packet.Packet;

public class PacketCbPlayHealthStatus implements Packet<PacketPlayCbListener> {

	public double max;
	public double avg;
	public double min;
	@Override
	public void decode(ByteBuf buf) throws Exception {
		max = buf.readDouble();
		avg = buf.readDouble();
		min = buf.readDouble();
	}

	@Override
	public void encode(ByteBuf buf) throws Exception {
		buf.writeDouble(max);
		buf.writeDouble(avg);
		buf.writeDouble(min);
	}

	@Override
	public void process(PacketPlayCbListener listener) throws Exception {
		listener.process(this);
	}

}
