package type.common.listener;

import type.common.packet.all.PacketCbAllSetState;

public interface PacketAllCbListener extends PacketListener {

	public void process(PacketCbAllSetState packetCbAllSetState);
}
