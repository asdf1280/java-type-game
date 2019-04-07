package type.common.handler;

import static type.common.work.Utils.l;

import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class TypeCommonPacketDecryptor extends ByteToMessageDecoder {
	private Cipher cp;

	public TypeCommonPacketDecryptor(SecretKey aes) {
		try {
			cp = Cipher.getInstance("AES");
			cp.init(Cipher.DECRYPT_MODE, aes);
		} catch (Exception e) {
			l.error("Decrypt error");
		}
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		in.markReaderIndex();
		if (in.readableBytes() < 4) {
			in.resetReaderIndex();
			return;
		}
		int i = in.readInt();
		if (in.readableBytes() < i) {
			in.resetReaderIndex();
			return;
		}
		byte[] arr = new byte[i];
		in.readBytes(arr);
		arr = cp.doFinal(arr);
		ByteBuf bb = ctx.alloc().buffer();
		bb.writeInt(arr.length);
		bb.writeBytes(arr);
		out.add(bb);
	}

}
