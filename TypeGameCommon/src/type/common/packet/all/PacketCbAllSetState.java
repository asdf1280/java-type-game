package type.common.packet.all;

import io.netty.buffer.ByteBuf;
import type.common.handler.ChannelState;
import type.common.listener.PacketAllCbListener;
import type.common.packet.Packet;
import type.common.work.Utils;

public class PacketCbAllSetState implements Packet<PacketAllCbListener> {

	public ChannelState cs = ChannelState.LOGIN;

	@Override
	public void decode(ByteBuf buf) {
		cs = ChannelState.valueOf(Utils.getString(buf));
	}

	@Override
	public void encode(ByteBuf buf) {
		Utils.writeString(buf, cs.name());
	}

	@Override
	public void process(PacketAllCbListener listener) throws Exception {
		listener.process(this);
	}

}
