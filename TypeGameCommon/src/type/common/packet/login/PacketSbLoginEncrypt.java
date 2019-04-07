package type.common.packet.login;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import io.netty.buffer.ByteBuf;
import type.common.listener.PacketLoginSbListener;
import type.common.packet.Packet;
import type.common.work.Utils;

public class PacketSbLoginEncrypt implements Packet<PacketLoginSbListener> {

	public PublicKey pk;
	
	@Override
	public void decode(ByteBuf buf) throws Exception {
		pk = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Utils.getByteArray(buf)));
	}

	@Override
	public void encode(ByteBuf buf) throws Exception {
		Utils.writeByteArray(buf, pk.getEncoded());
	}

	@Override
	public void process(PacketLoginSbListener listener) throws Exception {
		listener.process(this);
	}

}
