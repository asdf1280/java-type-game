package type.common.packet.login;

import io.netty.buffer.ByteBuf;
import type.common.listener.PacketLoginSbListener;
import type.common.packet.Packet;
import type.common.work.Utils;

public class PacketSbLoginTest implements Packet<PacketLoginSbListener> {

	public String msg;

	@Override
	public void decode(ByteBuf buf) throws Exception {
		msg = Utils.getString(buf);
	}

	@Override
	public void encode(ByteBuf buf) throws Exception {
		Utils.writeString(buf, msg);
	}

	@Override
	public void process(PacketLoginSbListener listener) throws Exception {
		listener.process(this);
	}

}
