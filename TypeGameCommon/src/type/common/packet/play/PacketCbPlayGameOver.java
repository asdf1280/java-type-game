package type.common.packet.play;

import io.netty.buffer.ByteBuf;
import type.common.listener.PacketPlayCbListener;
import type.common.packet.Packet;

public class PacketCbPlayGameOver implements Packet<PacketPlayCbListener> {

	public int rank;
	public int maxPlayers = 0;

	@Override
	public void decode(ByteBuf buf) throws Exception {
		rank = buf.readInt();
		maxPlayers = buf.readInt();
	}

	@Override
	public void encode(ByteBuf buf) throws Exception {
		buf.writeInt(rank);
		buf.writeInt(maxPlayers);
	}

	@Override
	public void process(PacketPlayCbListener listener) throws Exception {
		listener.process(this);
	}

}
