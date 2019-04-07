package type.common.listener;

import type.common.packet.login.PacketCbLoginAuthenticateResult;
import type.common.packet.login.PacketCbLoginEncrypt;

public interface PacketLoginCbListener extends PacketListener {

	void process(PacketCbLoginEncrypt packetCbLoginEncrypt);

	void process(PacketCbLoginAuthenticateResult packetCbLoginAuthenticateResult);

}
