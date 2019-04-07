package type.common.listener;

import type.common.packet.match.PacketCbMatchCanceled;
import type.common.packet.match.PacketCbMatchStarted;

public interface PacketMatchCbListener extends PacketListener {

	public void process(PacketCbMatchStarted packetCbMatchMatchmakingInfo);

	public void process(PacketCbMatchCanceled packetCbMatchCanceled);

}
