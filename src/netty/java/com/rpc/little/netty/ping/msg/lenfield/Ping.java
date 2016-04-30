package com.rpc.little.netty.ping.msg.lenfield;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class Ping {

	public static void main(String[] args) throws InterruptedException {
		EventLoopGroup worker = new NioEventLoopGroup(2);
		try {
			Bootstrap bs = new Bootstrap();
			bs.group(worker).channel(NioSocketChannel.class);
			bs.handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel channel) throws Exception {
//1					channel.pipeline().addLast(new PingByteBufHandler());
//					channel.pipeline().addLast(new Byte2MsgHandler());
//					channel.pipeline().addLast(new Msg2ByteHandler());
//					channel.pipeline().addLast(new RcvReturnMsgHandler());
					channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535,0,4));
				}
			});
			ChannelFuture ch = bs.connect("127.0.0.1", 8001).sync();
			Channel channel = ch.channel();
			channel.writeAndFlush("hello world");
			channel.closeFuture().sync();
		} finally {
			worker.shutdownGracefully();
		}
	}
}
