package com.rpc.little.netty.ping.msg;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class Byte2MsgHandler extends ByteToMessageDecoder {

	private AtomicInteger msgIdx = new AtomicInteger(0);
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		int count = in.readableBytes();
		byte[] dst = new byte[count];
		in.readBytes(dst);
		out.add(new String(dst)+ "#" + msgIdx.getAndIncrement());
		System.out.println("decode:" + out.get(out.size() - 1));
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("channelRead:" + msg);
		super.channelRead(ctx, msg);
	}

}
