package type.common.packet.login;

import java.util.Arrays;

import io.netty.buffer.ByteBuf;
import type.common.listener.PacketLoginSbListener;
import type.common.packet.Packet;
import type.common.work.Utils;

public class PacketSbLoginHandshake implements Packet<PacketLoginSbListener> {

	public byte[] arr;
	
	@Override
	public void decode(ByteBuf buf) throws Exception {
		arr = Utils.getByteArray(buf);
	}

	@Override
	public void encode(ByteBuf buf) throws Exception {
		Utils.writeByteArray(buf, arr);
	}
	
	public boolean isValidUser() {
		return Arrays.equals(arr, getValidByteArray());
	}
	public byte[] getValidByteArray() {
		return new byte[] {5, 15, 26, 12, 97, 103};
	}

	@Override
	public void process(PacketLoginSbListener listener) throws Exception {
		listener.process(this);
	}

}
