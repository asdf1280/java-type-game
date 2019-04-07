package type.server.main;

import static type.common.work.Utils.l;

import java.io.File;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import type.common.work.Utils;

public class Main {
	public static void main(String[] args) throws Exception {
		l.info("ServerBootstrap", "Starting TypeServer.");
		{
			File dbf = new File("database/");
			if (!dbf.exists())
				dbf.mkdirs();
		}

		l.info("ServerBootstrap", "Opening socket...");
		EventLoopGroup boss = new NioEventLoopGroup(5);
		EventLoopGroup work = new NioEventLoopGroup(5);
		try {
			ServerBootstrap sb = new ServerBootstrap();
			sb.group(boss, work).channel(NioServerSocketChannel.class).localAddress(Utils.port)
					.childHandler(new ServerInitializer());

			// Server booting here

			ChannelFuture ff = sb.bind().sync();
			l.info("ServerBootstrap", "Started server.");
			ff.channel().closeFuture().sync();
		} finally {
			boss.shutdownGracefully().syncUninterruptibly();
			work.shutdownGracefully().syncUninterruptibly();
		}
	}
}
