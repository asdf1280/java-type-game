package type.common.packet.play;

import io.netty.buffer.ByteBuf;
import type.common.listener.PacketPlayCbListener;
import type.common.packet.Packet;
import type.common.work.Utils;

public class PacketCbPlayGameMessage implements Packet<PacketPlayCbListener> {

	public String message = "";
	@Override
	public void decode(ByteBuf buf) throws Exception {
		message = Utils.getString(buf);
	}

	@Override
	public void encode(ByteBuf buf) throws Exception {
		Utils.writeString(buf, message);
	}

	@Override
	public void process(PacketPlayCbListener listener) throws Exception {
		listener.process(this);
	}

}
