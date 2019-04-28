package type.client.net;

import type.client.main.ClientInitializer;
import type.common.handler.HandleQueueGeneric;
import type.common.packet.all.PacketSbAllClose;
import type.common.packet.login.PacketCbLoginAuthenticateResult;
import type.common.packet.login.PacketSbLoginAnonymous;
import type.common.packet.login.PacketSbLoginAuthenticate;
import type.common.packet.match.PacketSbMatchCancel;
import type.common.packet.play.PacketSbPlayScore;
import type.common.packet.singleplayer.PacketSbSingleStopPlay;
import type.common.packet.user.PacketSbUserLobbyChat;
import type.common.packet.user.PacketSbUserPlaySingle;
import type.common.packet.user.PacketSbUserStartMatchmake;
import type.common.work.Sha512Utils;
import type.common.work.Utils;

public class TypeNetworkWorker extends NetworkWorker {

	public static TypeNetworkWorker newInstance(boolean succeed, ClientInitializer ci) {
		TypeNetworkWorker tnw = new TypeNetworkWorker();
		tnw.succeed = succeed;
		tnw.ci = ci;
		return tnw;
	}

	private ClientInitializer ci;

	private boolean succeed = false;

	@Override
	public boolean isSucceed() {
		return succeed;
	}

	private TypeNetworkWorker() {
		// Constructor
	}

	@Override
	public void closeConnection() {
		if (ci == null) {
			Utils.l.severe("TypeNetworkWorker", "Trying to call closeConnection() without server connection.");
		}
		ci.handle.sendPacket(new PacketSbAllClose());
	}

	@Override
	public void login(String id, String pw, boolean register, Runnable succCallback, Runnable failCallback) {
		String npw = Sha512Utils.shaencode(pw);
		Utils.l.info("TypeNetworkWorker", "Logging in with " + id + ", " + pw.substring(0, 20));
		PacketSbLoginAuthenticate p = new PacketSbLoginAuthenticate();
		p.username = id;
		p.password = npw;
		p.register = register;

		ci.priority.addQueue(PacketCbLoginAuthenticateResult.class,
				new HandleQueueGeneric<PacketCbLoginAuthenticateResult>() {

					@Override
					public void run(PacketCbLoginAuthenticateResult p) {
						if ((p.logged && !register) || (p.registered && register)) {
							if (register) {
								login(id, pw, false, succCallback, failCallback);
							}
							succCallback.run();
						} else {
							failCallback.run();
						}
					}
				});

		ci.handle.sendPacket(p);
	}

	@Override
	public void singleToggle(boolean single) {
		if (single) {
			PacketSbUserPlaySingle p = new PacketSbUserPlaySingle();
			ci.handle.sendPacket(p);
		} else {
			PacketSbSingleStopPlay p = new PacketSbSingleStopPlay();
			ci.handle.sendPacket(p);
		}
	}

	@Override
	public void anonymousLogin() {
		PacketSbLoginAnonymous p = new PacketSbLoginAnonymous();
		ci.handle.sendPacket(p);
	}

	@Override
	public void setChatCallback(ChatCallback ccb) {
		ci.handle.cc = ccb;
	}

	@Override
	public void chat(String msg) {
		Utils.l.info("TypeNetworkWorker", "Chatting: " + msg);
		PacketSbUserLobbyChat p = new PacketSbUserLobbyChat();
		p.message = msg;
		ci.handle.sendPacket(p);
	}

	@Override
	public void user_matchmake_start() {
		PacketSbUserStartMatchmake p = new PacketSbUserStartMatchmake();
		ci.handle.sendPacket(p);
	}

	@Override
	public void user_matchmake_cancel() {
		PacketSbMatchCancel p = new PacketSbMatchCancel();
		ci.handle.sendPacket(p);
	}

	@Override
	public void setMatchCallback(MatchCallback mcb) {
		ci.handle.mc = mcb;
	}

	@Override
	public void play_score(int score) {
		if (score <= 0)
			return;
		PacketSbPlayScore p = new PacketSbPlayScore();
		p.score = score;
		ci.handle.sendPacket(p);
	}
}