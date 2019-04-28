package type.common.listener;

import type.common.packet.play.PacketSbPlayScore;

public interface PacketPlaySbListener extends PacketListener {

	public void process(PacketSbPlayScore packet);
}
