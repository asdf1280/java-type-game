package type.client.net;

import type.client.handler.TypeClientInboundHandler;
import type.common.packet.match.PacketCbMatchCanceled;
import type.common.packet.match.PacketCbMatchFound;
import type.common.packet.match.PacketCbMatchStarted;

public abstract class MatchCallback {
	public abstract void matchInfo(PacketCbMatchStarted packet);

	public abstract void matchCanceled(PacketCbMatchCanceled packet);

	public abstract void matchFound(PacketCbMatchFound packet, TypeClientInboundHandler handle);

	public PlayCallback playHandler = null;
}
