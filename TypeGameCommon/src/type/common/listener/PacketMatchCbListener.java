package type.common.listener;

import type.common.packet.match.PacketCbMatchCanceled;
import type.common.packet.match.PacketCbMatchMatchmakingInfo;

public interface PacketMatchCbListener extends PacketListener {

	public void process(PacketCbMatchMatchmakingInfo packetCbMatchMatchmakingInfo);

	public void process(PacketCbMatchCanceled packetCbMatchCanceled);

}
