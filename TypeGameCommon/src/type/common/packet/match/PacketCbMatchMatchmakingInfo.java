package type.common.packet.match;

import io.netty.buffer.ByteBuf;
import type.common.listener.PacketMatchCbListener;
import type.common.packet.Packet;
import type.common.work.Utils;

public class PacketCbMatchMatchmakingInfo implements Packet<PacketMatchCbListener> {
	
	public String leftTime = "";

	@Override
	public void decode(ByteBuf buf) throws Exception {
		leftTime = Utils.getString(buf);
	}

	@Override
	public void encode(ByteBuf buf) throws Exception {
		Utils.writeString(buf, leftTime);
	}

	@Override
	public void process(PacketMatchCbListener listener) throws Exception {
		listener.process(this);
	}

}
