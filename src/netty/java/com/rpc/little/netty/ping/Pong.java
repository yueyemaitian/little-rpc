package com.rpc.little.netty.ping;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import com.rpc.little.netty.ping.msg.Byte2MsgHandler;
import com.rpc.little.netty.ping.msg.Msg2ByteHandler;
import com.rpc.little.netty.ping.msg.RcvReturnMsgHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Pong {
	public static void main(String[] args) throws InterruptedException {
		EventLoopGroup boss = new NioEventLoopGroup(2,new ThreadFactory(){
			private final AtomicInteger idx = new AtomicInteger();
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r,"netty-boss#" + idx.incrementAndGet());
			}});
		EventLoopGroup worker = new NioEventLoopGroup(2,new ThreadFactory(){
			private final AtomicInteger idx = new AtomicInteger();
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r,"netty-worker#" + idx.incrementAndGet());
			}});
		try {
			ServerBootstrap sb = new ServerBootstrap();
			sb.group(boss, worker).channel(NioServerSocketChannel.class);
//			sb.handler(Channel)
			sb.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel channel) throws Exception {
					channel.pipeline().addLast(new Byte2MsgHandler());
					channel.pipeline().addLast(new Msg2ByteHandler());
					channel.pipeline().addLast(new Msg2ByteHandler());
					channel.pipeline().addLast(new RcvReturnMsgHandler());
				}
			});
			sb.option(ChannelOption.SO_BACKLOG, 1024);
			sb.childOption(ChannelOption.SO_KEEPALIVE, true);

			ChannelFuture ch = sb.bind(8001).sync();
			ch.channel().closeFuture().sync();
		} finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
}
