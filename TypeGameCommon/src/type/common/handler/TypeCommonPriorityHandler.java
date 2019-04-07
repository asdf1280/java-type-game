package type.common.handler;

import java.util.HashMap;
import java.util.LinkedList;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import type.common.packet.Packet;
import type.common.work.Utils;

public class TypeCommonPriorityHandler extends ChannelInboundHandlerAdapter {
	@SuppressWarnings("rawtypes")
	private HashMap<Class<? extends Packet<?>>, LinkedList<HandleQueueGeneric>> queue = new HashMap<>();
	
	public void addQueue(Class<? extends Packet<?>> clazz, HandleQueueGeneric<?> run) {
		if(!queue.containsKey(clazz)) {
			queue.put(clazz, new LinkedList<>());
		}
		queue.get(clazz).addLast(run);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(queue.containsKey(msg.getClass())) {
			if(queue.get(msg.getClass()).size() > 0)
				Utils.l.fine("[Priority] Invoking reserved method...");
			queue.get(msg.getClass()).removeFirst().run(msg);
		}else {
			super.channelRead(ctx, msg);
		}
	}
}
