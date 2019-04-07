package type.common.listener;

import type.common.packet.user.PacketSbUserLobbyChat;
import type.common.packet.user.PacketSbUserPlaySingle;
import type.common.packet.user.PacketSbUserStartMatchmake;

public interface PacketUserSbListener extends PacketListener {

	public void process(PacketSbUserPlaySingle packetSbUserPlaySingle);

	public void process(PacketSbUserLobbyChat packetSbUserLobbyChat);

	public void process(PacketSbUserStartMatchmake packetSbUserStartMatchmake);
}
