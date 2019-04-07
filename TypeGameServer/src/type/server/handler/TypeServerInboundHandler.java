package type.server.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.InetSocketAddress;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import type.common.handler.ChannelState;
import type.common.handler.TypeCommonPacketDecryptor;
import type.common.handler.TypeCommonPacketEncryptor;
import type.common.listener.PacketAllSbListener;
import type.common.listener.PacketListener;
import type.common.listener.PacketLoginSbListener;
import type.common.listener.PacketMatchSbListener;
import type.common.listener.PacketPlaySbListener;
import type.common.listener.PacketSingleSbListener;
import type.common.listener.PacketUserSbListener;
import type.common.packet.Packet;
import type.common.packet.all.PacketCbAllSetState;
import type.common.packet.all.PacketSbAllClose;
import type.common.packet.login.PacketCbLoginAuthenticateResult;
import type.common.packet.login.PacketCbLoginEncrypt;
import type.common.packet.login.PacketSbLoginAnonymous;
import type.common.packet.login.PacketSbLoginAuthenticate;
import type.common.packet.login.PacketSbLoginEncrypt;
import type.common.packet.login.PacketSbLoginHandshake;
import type.common.packet.login.PacketSbLoginTest;
import type.common.packet.match.PacketCbMatchCanceled;
import type.common.packet.match.PacketCbMatchStarted;
import type.common.packet.match.PacketSbMatchCancel;
import type.common.packet.singleplayer.PacketSbSingleStopPlay;
import type.common.packet.user.PacketCbUserLobbyChat;
import type.common.packet.user.PacketSbUserLobbyChat;
import type.common.packet.user.PacketSbUserPlaySingle;
import type.common.packet.user.PacketSbUserStartMatchmake;
import type.common.work.AttributeSaver;
import type.common.work.Sha512Utils;
import type.common.work.Utils;
import type.server.data.UserData;
import type.server.game.MatchMaking;
import type.server.game.MatchUserData;

public class TypeServerInboundHandler extends SimpleChannelInboundHandler<Packet<?>> implements PacketLoginSbListener,
		PacketUserSbListener, PacketAllSbListener, PacketPlaySbListener, PacketSingleSbListener, PacketMatchSbListener {

	public Channel ch = null;
	public ChannelHandlerContext ctx = null;
	public PacketListener pl = null;
	public PacketListener dpl = null;
	public int TypeGUID = 0;
	private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	public void setState(ChannelState state) {
		Utils.getChannelAttr(AttributeSaver.state, ctx.channel()).set(state);
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
		channels.add(ch);
		System.out.println("Channel activated");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void processPacket(Packet msg) throws Exception {
		Utils.l.info("ServerHandler", "Processing packet: " + msg.getClass().getSimpleName());
		msg.process(pl);
	}

	public SecretKey aes;

	@Override
	public void process(PacketSbLoginEncrypt packet) {
		try {
			KeyGenerator kg = KeyGenerator.getInstance("AES");
			kg.init(256);
			aes = kg.generateKey();

			PacketCbLoginEncrypt p1 = new PacketCbLoginEncrypt();
			p1.setAesKey(packet.pk, aes.getEncoded());
			sendPacket(p1);

			ch.pipeline().addFirst("aesenc", new TypeCommonPacketEncryptor(aes));
			ch.pipeline().addFirst("aesdec", new TypeCommonPacketDecryptor(aes));
		} catch (Exception e) {
			Utils.l.error("Wrong algorithm");
			e.printStackTrace();
			ctx.close().awaitUninterruptibly();
		}
	}

	@Override
	public void process(PacketSbLoginHandshake packet) {
		if (!packet.isValidUser()) {
			ctx.close().awaitUninterruptibly();
		}
	}

	@Override
	public void process(PacketSbLoginTest packetSbLoginTest) {
		Utils.l.info("Message: " + packetSbLoginTest.msg);
	}

	@SuppressWarnings("rawtypes")
	public void sendPacket(Packet packet) {
		ctx.writeAndFlush(packet).awaitUninterruptibly().syncUninterruptibly();
	}

	@Override
	public void process(PacketSbAllClose packetSbAllClose) {
		ctx.close().awaitUninterruptibly();
	}

	@Override
	public void process(PacketSbLoginAuthenticate packet) {
		String id = packet.username;
		String pw = packet.password;
		pw = Sha512Utils.shaencode(pw);
		boolean reg = packet.register;
		if (reg) { // Register
			if (!Utils.isFilenameValid("database/users/" + id + ".json")) {
				loginFail();
				return;
			}
			if (new File("database/users/" + id + ".json").exists()) {
				loginFail();
				return;
			}
			File f = new File("database/users/" + id + ".json");
			UserData nd = new UserData();
			nd.id = id;
			nd.password = pw;
			nd.nickname = id;
			Gson g = Utils.g;
			try {
				g.toJson(nd, new FileWriter(f));
			} catch (Exception e) {
				loginFail();
				return;
			}
			PacketCbLoginAuthenticateResult p = new PacketCbLoginAuthenticateResult();
			p.logged = false;
			p.registered = true;
			p.nick = nd.nickname;
			sendPacket(p);
		} else {
			if (!Utils.isFilenameValid("database/users/" + id + ".json")) {
				loginFail();
				return;
			}
			File f = new File("database/users/" + id + ".json");
			if (!f.exists()) {
				loginFail();
				return;
			}
			UserData nd = null;
			try {
				nd = Utils.g.fromJson(new FileReader(f), UserData.class);
			} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
				loginFail();
				return;
			}
			if (!nd.password.equals(pw)) {
				loginFail();
				return;
			}
			PacketCbLoginAuthenticateResult p = new PacketCbLoginAuthenticateResult();
			p.logged = true;
			p.registered = false;
			p.nick = nd.nickname;
			sendPacket(p);

			switchToUser();
		}
	}

	private void loginFail() {
		PacketCbLoginAuthenticateResult p = new PacketCbLoginAuthenticateResult();
		p.logged = false;
		p.registered = false;
		p.nick = "";
		sendPacket(p);
	}

	public void checkDb() {
		File dbf1 = new File("database/");
		dbf1.mkdirs();

		File dbf2 = new File("database/users");
		dbf2.mkdirs();
	}

	@Override
	public void process(PacketSbLoginAnonymous packet) {
		switchToUser();
	}

	@Override
	public void process(PacketSbUserPlaySingle packetSbUserPlaySingle) {
		PacketCbAllSetState p1 = new PacketCbAllSetState();
		p1.cs = ChannelState.SINGLEPLAY;
		sendPacket(p1);

		setState(ChannelState.SINGLEPLAY);
	}

	@Override
	public void process(PacketSbSingleStopPlay packet) {
		switchToUser();
	}

	private void switchToUser() {
		PacketCbAllSetState p1 = new PacketCbAllSetState();
		p1.cs = ChannelState.USER;
		sendPacket(p1);

		setState(ChannelState.USER);
	}
	
	public final String nickSalt = Long.toString(System.nanoTime());

	@Override
	public void process(PacketSbUserLobbyChat packet) {
		PacketCbUserLobbyChat p = new PacketCbUserLobbyChat();
		p.sender = getName();
		p.message = packet.message;
		for(Channel cah : channels) {
			if(cah.attr(AttributeSaver.state).get() == ChannelState.USER) {
				cah.writeAndFlush(p).awaitUninterruptibly();
			}
		}
	}

	public String getName() {
		return Sha512Utils.shaencode(((InetSocketAddress)ch.remoteAddress()).getHostName() + nickSalt).substring(0, 6);
	}
	
	MatchUserData mud = null;

	@Override
	public void process(PacketSbUserStartMatchmake packetSbUserStartMatchmake) {
		PacketCbAllSetState p = new PacketCbAllSetState();
		p.cs = ChannelState.MATCH;
		sendPacket(p);
		setState(ChannelState.MATCH);
		
		PacketCbMatchStarted p1 = new PacketCbMatchStarted();
		sendPacket(p1);
		
		mud = new MatchUserData();
		mud.handle = this;
		
	}

	@Override
	public void process(PacketSbMatchCancel packet) {
		MatchMaking.queue.remove(mud);
		mud = null;
		
		PacketCbMatchCanceled p1 = new PacketCbMatchCanceled();
		p1.clientCaused = true;
		p1.msg = "CLIENT CAUSED";
		sendPacket(p1);
		
		PacketCbAllSetState p = new PacketCbAllSetState();
		p.cs = ChannelState.USER;
		sendPacket(p);
		setState(ChannelState.USER);
	}
}