package type.common.listener;

import type.common.packet.singleplayer.PacketSbSingleStopPlay;

public interface PacketSingleSbListener extends PacketListener {

	public void process(PacketSbSingleStopPlay packet);

}
