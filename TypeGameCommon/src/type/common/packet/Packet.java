package type.common.packet;

import io.netty.buffer.ByteBuf;
import type.common.listener.PacketListener;

public interface Packet<T extends PacketListener> {
	public void decode(ByteBuf buf) throws Exception;
	public void encode(ByteBuf buf) throws Exception;
	public void process(T listener) throws Exception;
}
