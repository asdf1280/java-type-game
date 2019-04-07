package type.common.packet.login;

import io.netty.buffer.ByteBuf;
import type.common.listener.PacketLoginCbListener;
import type.common.packet.Packet;
import type.common.work.Utils;

public class PacketCbLoginAuthenticateResult implements Packet<PacketLoginCbListener> {

	public boolean registered = false;
	public boolean logged = false;
	public String nick = "";
	
	@Override
	public void decode(ByteBuf buf) throws Exception {
		registered = buf.readBoolean();
		logged = buf.readBoolean();
		nick = Utils.getString(buf);
	}

	@Override
	public void encode(ByteBuf buf) throws Exception {
		buf.writeBoolean(registered);
		buf.writeBoolean(logged);
		Utils.writeString(buf, nick);
	}

	@Override
	public void process(PacketLoginCbListener listener) throws Exception {
		listener.process(this);
	}

}
