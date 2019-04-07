package type.common.packet.user;

import io.netty.buffer.ByteBuf;
import type.common.listener.PacketUserSbListener;
import type.common.packet.Packet;
import type.common.work.Utils;

public class PacketSbUserLobbyChat implements Packet<PacketUserSbListener> {

	public String message;
	@Override
	public void decode(ByteBuf buf) throws Exception {
		message = Utils.getString(buf);
	}

	@Override
	public void encode(ByteBuf buf) throws Exception {
		Utils.writeString(buf, message);
	}

	@Override
	public void process(PacketUserSbListener listener) throws Exception {
		listener.process(this);
	}

}
