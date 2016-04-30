package com.rpc.little.netty.ping.msg;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class RcvReturnMsgHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("channelRead then write:" + msg);
		ctx.channel().writeAndFlush(msg);
		super.channelRead(ctx, msg);
	}

}
