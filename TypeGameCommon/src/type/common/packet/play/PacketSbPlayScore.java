package type.common.packet.play;

import io.netty.buffer.ByteBuf;
import type.common.listener.PacketPlaySbListener;
import type.common.packet.Packet;

public class PacketSbPlayScore implements Packet<PacketPlaySbListener> {

	public int score;

	@Override
	public void decode(ByteBuf buf) throws Exception {
		score = buf.readInt();
	}

	@Override
	public void encode(ByteBuf buf) throws Exception {
		buf.writeInt(score);
	}

	@Override
	public void process(PacketPlaySbListener listener) throws Exception {
		listener.process(this);
	}

}
