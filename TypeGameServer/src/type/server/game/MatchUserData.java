package type.server.game;

import type.common.handler.ChannelState;
import type.common.listener.PacketPlaySbListener;
import type.common.packet.all.PacketCbAllSetState;
import type.common.packet.match.PacketCbMatchFound;
import type.common.packet.play.PacketCbPlayCountdownTime;
import type.common.packet.play.PacketCbPlayGameMessage;
import type.common.packet.play.PacketCbPlayGameOver;
import type.common.packet.play.PacketCbPlayHealthStatus;
import type.common.packet.play.PacketCbPlayLeftUsers;
import type.common.packet.play.PacketCbPlayPrepareGame;
import type.common.packet.play.PacketCbPlaySetHealth;
import type.common.packet.play.PacketCbPlaySetMaxHealth;
import type.common.packet.play.PacketSbPlayScore;
import type.server.handler.TypeServerInboundHandler;

public class MatchUserData extends PlayerData implements PacketPlaySbListener {
	public TypeServerInboundHandler handle;

	@Override
	public void initCon() {
		PacketCbMatchFound p1 = new PacketCbMatchFound();
		handle.sendPacket(p1);

		PacketCbAllSetState p = new PacketCbAllSetState();
		p.cs = ChannelState.PLAY;
		handle.sendPacket(p);
		handle.setState(ChannelState.PLAY);
		handle.pl = this;
	}

	@Override
	public void message(String msg) {
		PacketCbPlayGameMessage p = new PacketCbPlayGameMessage();
		p.message = msg;
		handle.sendPacket(p);
	}

	@Override
	public boolean isOnline() {
		return handle.ch.isActive();
	}

	@Override
	public void playSignal(double h) {
		// TODO play start packet
		System.out.println("Prepare packet for user");
		PacketCbPlaySetMaxHealth p = new PacketCbPlaySetMaxHealth();
		p.maxhealth = h;
		handle.sendPacket(p);

		System.out.println("Send first packet");

		PacketCbPlaySetHealth p2 = new PacketCbPlaySetHealth();
		p2.health = h;
		handle.sendPacket(p2);

		PacketCbPlayPrepareGame p1 = new PacketCbPlayPrepareGame();
		handle.sendPacket(p1);
	}

	@Override
	public void setHealth(double h) {
		// TODO add health packet
		PacketCbPlaySetHealth p = new PacketCbPlaySetHealth();
		p.health = h;
		handle.sendPacket(p);
	}

	@Override
	public void gameOver(int rank, int maxUsers) {
		// TODO game over packet
		PacketCbPlayGameOver p = new PacketCbPlayGameOver();
		p.maxPlayers = maxUsers;
		p.rank = rank;
		handle.sendPacket(p);

		PacketCbAllSetState p1 = new PacketCbAllSetState();
		p1.cs = ChannelState.USER;
		handle.sendPacket(p1);

		handle.setState(ChannelState.USER);
		handle.pl = handle.dpl;
	}

	@Override
	public void leftUsers(int count) {
		PacketCbPlayLeftUsers p = new PacketCbPlayLeftUsers();
		p.left = count;
		handle.sendPacket(p);
	}

	@Override
	public void process(PacketSbPlayScore packet) {
		if (worker != null)
			worker.score(packet.score);
	}

	@Override
	public void countDown(int seconds, int joined) {
		PacketCbPlayCountdownTime p = new PacketCbPlayCountdownTime();
		p.seconds = seconds;
		p.joined = joined;
		handle.sendPacket(p);
	}

	@Override
	public void userStatus(double max, double avg, double min) {
		PacketCbPlayHealthStatus p = new PacketCbPlayHealthStatus();
		p.max = max;
		p.avg = avg;
		p.min = min;
		handle.sendPacket(p);
	}
}
