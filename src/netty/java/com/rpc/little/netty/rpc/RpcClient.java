package com.rpc.little.netty.rpc;

import com.rpc.little.netty.rpc.handler.RpcDataDecoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.concurrent.GenericFutureListener;

public class RpcClient {
	Bootstrap bs;

	public void init() {
		EventLoopGroup worker = new NioEventLoopGroup();
		bs = new Bootstrap();
		bs.group(worker).channel(NioSocketChannel.class);
		bs.option(ChannelOption.SO_BACKLOG, 1024);
		bs.option(ChannelOption.SO_SNDBUF, 1024 * 1024);
		bs.option(ChannelOption.SO_RCVBUF, 1024 * 1024);
		bs.option(ChannelOption.SO_REUSEADDR, true);
		bs.option(ChannelOption.SO_TIMEOUT, 60 * 1000);
		bs.handler(new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel ch) throws Exception {
				//4 bytes的长度
				ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024 * 1024 * 1024, 0, 4, 0, 4),
						new RpcDataDecoder());
			}
		});
	}

	public void connect(String hostname, int port) {
		ChannelFuture future = bs.connect(hostname, port);
		future.addListener(new GenericFutureListener<ChannelFuture>() {

			@Override
			public void operationComplete(ChannelFuture future) throws Exception {

			}
		});
	}
}
