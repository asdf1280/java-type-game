package type.common.listener;

import type.common.packet.play.PacketCbPlayCountdownTime;
import type.common.packet.play.PacketCbPlayGameMessage;
import type.common.packet.play.PacketCbPlayGameOver;
import type.common.packet.play.PacketCbPlayHealthStatus;
import type.common.packet.play.PacketCbPlayLeftUsers;
import type.common.packet.play.PacketCbPlayPrepareGame;
import type.common.packet.play.PacketCbPlaySetHealth;
import type.common.packet.play.PacketCbPlaySetMaxHealth;

public interface PacketPlayCbListener extends PacketListener {

	public void process(PacketCbPlayGameMessage packet);

	public void process(PacketCbPlayGameOver packet);

	public void process(PacketCbPlayLeftUsers packet);

	public void process(PacketCbPlaySetHealth packet);

	public void process(PacketCbPlaySetMaxHealth packet);

	public void process(PacketCbPlayPrepareGame packetCbPlayPrepareGame);

	public void process(PacketCbPlayCountdownTime packetCbPlayCountdownTime);

	public void process(PacketCbPlayHealthStatus packetCbPlayHeathStatus);
}
