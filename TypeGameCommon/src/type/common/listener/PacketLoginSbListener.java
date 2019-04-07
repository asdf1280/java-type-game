package type.common.listener;

import type.common.packet.login.PacketSbLoginAnonymous;
import type.common.packet.login.PacketSbLoginAuthenticate;
import type.common.packet.login.PacketSbLoginEncrypt;
import type.common.packet.login.PacketSbLoginHandshake;
import type.common.packet.login.PacketSbLoginTest;

public interface PacketLoginSbListener extends PacketListener {
	public void process(PacketSbLoginEncrypt packetSbLoginEncrypt);

	public void process(PacketSbLoginHandshake packetSbLoginHandshake);

	public void process(PacketSbLoginTest packetSbLoginTest);

	public void process(PacketSbLoginAuthenticate packetSbLoginAuthenticate);

	public void process(PacketSbLoginAnonymous packet);

}
