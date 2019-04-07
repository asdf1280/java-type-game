package type.common.packet.login;

import io.netty.buffer.ByteBuf;
import type.common.listener.PacketLoginSbListener;
import type.common.packet.Packet;
import type.common.work.Utils;

public class PacketSbLoginAuthenticate implements Packet<PacketLoginSbListener> {

	public boolean register;
	public String username;
	public String password;

	@Override
	public void decode(ByteBuf buf) throws Exception {
		username = Utils.getString(buf);
		password = Utils.getString(buf);
		register = buf.readBoolean();
	}

	@Override
	public void encode(ByteBuf buf) throws Exception {
		Utils.writeString(buf, username);
		Utils.writeString(buf, password);
		buf.writeBoolean(register);
	}

	@Override
	public void process(PacketLoginSbListener listener) throws Exception {
		listener.process(this);
	}

}
