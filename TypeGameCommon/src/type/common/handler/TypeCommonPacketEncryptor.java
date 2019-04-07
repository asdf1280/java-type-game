package type.common.handler;

import static type.common.work.Utils.l;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class TypeCommonPacketEncryptor extends MessageToByteEncoder<ByteBuf> {
	
	private Cipher cp;
	public TypeCommonPacketEncryptor(SecretKey aes) {
		try {
			cp = Cipher.getInstance("AES");
			cp.init(Cipher.ENCRYPT_MODE, aes);
		} catch (Exception e) {
			e.printStackTrace();
			l.error("Encrypt error");
		}
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
		msg.markReaderIndex();
		if(msg.readableBytes() < 4) {
			msg.resetReaderIndex();
			return;
		}
		int i = msg.readInt();
		if(msg.readableBytes() < i) {
			msg.resetReaderIndex();
			return;
		}
		byte[] arr = new byte[i];
		msg.readBytes(arr);
		arr = cp.doFinal(arr);
		out.writeInt(arr.length);
		out.writeBytes(arr);
	}

}
