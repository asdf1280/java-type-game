package type.common.listener;

import type.common.packet.user.PacketCbUserLobbyChat;

public interface PacketUserCbListener extends PacketListener {

	public void process(PacketCbUserLobbyChat packet);
}
