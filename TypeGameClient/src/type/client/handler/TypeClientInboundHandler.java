package type.client.handler;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

import javax.crypto.SecretKey;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import type.client.main.Network;
import type.client.net.ChatCallback;
import type.client.net.MatchCallback;
import type.common.handler.ChannelState;
import type.common.handler.TypeCommonPacketDecryptor;
import type.common.handler.TypeCommonPacketEncryptor;
import type.common.listener.PacketAllCbListener;
import type.common.listener.PacketListener;
import type.common.listener.PacketLoginCbListener;
import type.common.listener.PacketMatchCbListener;
import type.common.listener.PacketPlayCbListener;
import type.common.listener.PacketSingleCbListener;
import type.common.listener.PacketUserCbListener;
import type.common.packet.Packet;
import type.common.packet.all.PacketCbAllSetState;
import type.common.packet.login.PacketCbLoginAuthenticateResult;
import type.common.packet.login.PacketCbLoginEncrypt;
import type.common.packet.login.PacketSbLoginEncrypt;
import type.common.packet.login.PacketSbLoginHandshake;
import type.common.packet.match.PacketCbMatchCanceled;
import type.common.packet.match.PacketCbMatchMatchmakingInfo;
import type.common.packet.user.PacketCbUserLobbyChat;
import type.common.work.Utils;

public class TypeClientInboundHandler extends SimpleChannelInboundHandler<Packet<?>> implements PacketLoginCbListener,
		PacketUserCbListener, PacketAllCbListener, PacketPlayCbListener, PacketSingleCbListener, PacketMatchCbListener {

	public Channel ch = null;
	public ChannelHandlerContext ctx = null;
	public PacketListener pl = null;
	public PacketListener dpl = null;
	public int TypeGUID = 0;

	public void setState(ChannelState state) {
		Network.state = state;
	}

	public ChannelState getState() {
		return Network.state;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Packet<?> msg) throws Exception {
		processPacket(msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ch = ctx.channel();
		this.ctx = ctx;
		setState(ChannelState.LOGIN);
		pl = this;
		dpl = this;

		{
			System.out.println("Sending packets...");
			PacketSbLoginHandshake p1 = new PacketSbLoginHandshake();
			p1.arr = p1.getValidByteArray();
			sendPacket(p1);

			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048, new SecureRandom());
			kp = kpg.generateKeyPair();

			PacketSbLoginEncrypt p2 = new PacketSbLoginEncrypt();
			p2.pk = kp.getPublic();
			sendPacket(p2);
		}
	}

	private KeyPair kp;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void processPacket(Packet msg) throws Exception {
		msg.process(pl);
	}

	@SuppressWarnings("rawtypes")
	public void sendPacket(Packet packet) {
		ctx.writeAndFlush(packet).awaitUninterruptibly().syncUninterruptibly();
	}

	private SecretKey aes;

	@Override
	public void process(PacketCbLoginEncrypt packet) {
		aes = packet.getAesKey(kp.getPrivate());
		kp = null;

		ch.pipeline().addFirst("aesenc", new TypeCommonPacketEncryptor(aes));
		ch.pipeline().addFirst("aesdec", new TypeCommonPacketDecryptor(aes));

		Utils.l.info("ClientHandler", "Encryption activated");
//
//		PacketSbLoginTest p3 = new PacketSbLoginTest();
//		p3.msg = "Hello world";
//		sendPacket(p3);
	}

	@Override
	public void process(PacketCbAllSetState packetCbAllSetState) {
		Network.state = packetCbAllSetState.cs;
		Utils.l.fine("ClientHandler", "SetState from server: " + packetCbAllSetState.cs.toString());
	}

	@Override
	public void process(PacketCbLoginAuthenticateResult packet) {
		// external handler
	}

	public ChatCallback cc = null;

	@Override
	public void process(PacketCbUserLobbyChat packet) {
		if (cc != null) {
			cc.processChat(packet.sender, packet.message);
		}
	}

	public MatchCallback mc = null;

	@Override
	public void process(PacketCbMatchMatchmakingInfo p) {
		if (mc != null) {
			mc.matchInfo(p);
		}
	}

	@Override
	public void process(PacketCbMatchCanceled packetCbMatchCanceled) {
		if (mc != null) {
			mc.matchCanceled(packetCbMatchCanceled);
		}
	}
}
