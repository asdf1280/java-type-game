package type.common.listener;

import type.common.packet.match.PacketSbMatchCancel;

public interface PacketMatchSbListener extends PacketListener {

	public void process(PacketSbMatchCancel packet);
	
}
