package type.common.packet.match;

import io.netty.buffer.ByteBuf;
import type.common.listener.PacketMatchCbListener;
import type.common.packet.Packet;
import type.common.work.Utils;

public class PacketCbMatchCanceled implements Packet<PacketMatchCbListener> {

	public String msg;
	public boolean clientCaused = false;

	@Override
	public void decode(ByteBuf buf) throws Exception {
		msg = Utils.getString(buf);
		clientCaused = buf.readBoolean();
	}

	@Override
	public void encode(ByteBuf buf) throws Exception {
		Utils.writeString(buf, msg);
		buf.writeBoolean(clientCaused);
	}

	@Override
	public void process(PacketMatchCbListener listener) throws Exception {
		listener.process(this);
	}

}
