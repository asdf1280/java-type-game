package type.client.net;

import type.common.packet.match.PacketCbMatchCanceled;
import type.common.packet.match.PacketCbMatchStarted;

public abstract class MatchCallback {
	public abstract void matchInfo(PacketCbMatchStarted packet);
	public abstract void matchCanceled(PacketCbMatchCanceled packet);
}
