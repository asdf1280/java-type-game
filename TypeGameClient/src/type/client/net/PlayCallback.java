package type.client.net;

import type.client.handler.TypeClientInboundHandler;
import type.common.listener.PacketPlayCbListener;
import type.common.packet.play.PacketCbPlayGameOver;

public abstract class PlayCallback implements PacketPlayCbListener {
	public TypeClientInboundHandler handle;

	@Override
	public void process(PacketCbPlayGameOver packet) {
		process0(packet);
		handle.pl = handle.dpl;
	}

	public abstract void process0(PacketCbPlayGameOver packet);
}
