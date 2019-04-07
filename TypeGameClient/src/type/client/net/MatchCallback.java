package type.client.net;

import type.common.packet.match.PacketCbMatchCanceled;
import type.common.packet.match.PacketCbMatchMatchmakingInfo;

public abstract class MatchCallback {
	public abstract void matchInfo(PacketCbMatchMatchmakingInfo packet);
	public abstract void matchCanceled(PacketCbMatchCanceled packet);
}
