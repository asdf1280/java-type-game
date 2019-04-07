package type.client.main;

import static type.common.work.Utils.l;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import type.client.net.NetworkWorker;
import type.client.net.TypeNetworkWorker;
import type.common.handler.ChannelState;
import type.common.work.Holder;
import type.common.work.Utils;

public class Network {
	public static ChannelState state = null;

	public static void connect(String addr) {
		connect(addr, Utils.port);
	}

	private static void connect(String addr, int port) {
		l.info("[ClientBootstrap] Initializing connection...");
		EventLoopGroup group = new NioEventLoopGroup(3);
		try {
			Bootstrap bootstrap = new Bootstrap();
			ClientInitializer initializer = new ClientInitializer();
			bootstrap.group(group).remoteAddress(addr, port).channel(NioSocketChannel.class).handler(initializer);

			l.info("[ClientBootstrap] Connecting to server...");

			ChannelFuture channelFuture = bootstrap.connect().syncUninterruptibly();
			l.severe("[ClientBootstrap] Connected to server.");

			Channel clientCh = channelFuture.channel();
			clientCh.closeFuture().awaitUninterruptibly();
		} catch (Exception e) {
			l.severe("[ClientBootstrap] Connection error");
			e.printStackTrace();
			System.exit(0);
		} finally {
			group.shutdownGracefully().syncUninterruptibly();
		}
	}

	public static NetworkWorker connectServer(String addr, int port) {
		Holder<Boolean> hb = new Holder<>(false);
		Holder<NetworkWorker> nw = new Holder<>();
		new Thread(new Runnable() {

			@Override
			public void run() {
				l.info("[ClientBootstrap] Initializing connection...");
				EventLoopGroup group = new NioEventLoopGroup(3);
				try {
					Bootstrap bootstrap = new Bootstrap();
					ClientInitializer initializer = new ClientInitializer();
					bootstrap.group(group).remoteAddress(addr, port).channel(NioSocketChannel.class)
							.handler(initializer);

					l.info("[ClientBootstrap] Connecting to server...");

					ChannelFuture channelFuture = bootstrap.connect().syncUninterruptibly();
					l.severe("[ClientBootstrap] Connected to server.");

					while(!initializer.handle.ch.pipeline().names().contains("aesdec")) {
						Utils.sleep(200);
					}
					nw.value = TypeNetworkWorker.newInstance(true, initializer);
					hb.value = true;

					Channel clientCh = channelFuture.channel();
					clientCh.closeFuture().awaitUninterruptibly();
				} catch (Exception e) {
					l.severe("[ClientBootstrap] Connection error");
					e.printStackTrace();
					
					nw.value = TypeNetworkWorker.newInstance(false, null);
					hb.value = true;
				} finally {
					group.shutdownGracefully().syncUninterruptibly();
				}
			}
		}).start();

		while (!hb.value) {
			Utils.sleep(100);
		}
		return nw.value;
	}

	public static void main(String[] args) {
		connect("127.0.0.1");
	}
}
