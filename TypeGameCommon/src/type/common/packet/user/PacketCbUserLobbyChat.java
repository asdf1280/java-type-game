package type.common.packet.user;

import io.netty.buffer.ByteBuf;
import type.common.listener.PacketUserCbListener;
import type.common.packet.Packet;
import type.common.work.Utils;

public class PacketCbUserLobbyChat implements Packet<PacketUserCbListener> {

	public String sender;
	public String message;
	
	@Override
	public void decode(ByteBuf buf) throws Exception {
		sender = Utils.getString(buf);
		message = Utils.getString(buf);
	}

	@Override
	public void encode(ByteBuf buf) throws Exception {
		Utils.writeString(buf, sender);
		Utils.writeString(buf, message);
	}

	@Override
	public void process(PacketUserCbListener listener) throws Exception {
		listener.process(this);
	}

}
