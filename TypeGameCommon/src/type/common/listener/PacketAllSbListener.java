package type.common.listener;

import type.common.packet.all.PacketSbAllClose;

public interface PacketAllSbListener extends PacketListener {

	void process(PacketSbAllClose packetSbAllClose);

}
