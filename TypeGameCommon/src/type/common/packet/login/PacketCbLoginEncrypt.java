package type.common.packet.login;

import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import io.netty.buffer.ByteBuf;
import type.common.listener.PacketLoginCbListener;
import type.common.packet.Packet;
import type.common.work.Utils;

public class PacketCbLoginEncrypt implements Packet<PacketLoginCbListener> {
	
	private byte[] aeskey;
	
	public void setAesKey(PublicKey pk, byte[] aes) {
		try {
			Cipher ch = Cipher.getInstance("RSA");
			ch.init(Cipher.ENCRYPT_MODE, pk);
			aeskey = ch.doFinal(aes);
		} catch (Exception e) {
			
		}
	}
	
	public SecretKey getAesKey(PrivateKey pk) {
		try {
			Cipher ch = Cipher.getInstance("RSA");
			ch.init(Cipher.DECRYPT_MODE, pk);
			return new SecretKeySpec(ch.doFinal(aeskey), "AES");
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public void decode(ByteBuf buf) throws Exception {
		aeskey = Utils.getByteArray(buf);
	}

	@Override
	public void encode(ByteBuf buf) throws Exception {
		Utils.writeByteArray(buf, aeskey);
	}

	@Override
	public void process(PacketLoginCbListener listener) throws Exception {
		listener.process(this);
	}

}
