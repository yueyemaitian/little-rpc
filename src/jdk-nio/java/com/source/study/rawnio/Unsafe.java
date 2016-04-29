package com.source.study.rawnio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Unsafe {

	public SocketChannel accept(ServerSocketChannel serverChannel) throws IOException {
		SocketChannel channel = serverChannel.accept();
		return channel;
	}

	public int read(SocketChannel channel, ByteBuffer byteBuf) throws IOException {

		int totalByteCnt = 0;
		int byteCnt = 0;
		while ((byteCnt = channel.read(byteBuf)) > 0) {
			totalByteCnt += byteCnt;
		}
		byteBuf.flip();
		return totalByteCnt;
	}

	public int write(SocketChannel channel, ByteBuffer byteBuf) throws IOException {
		return channel.write(byteBuf);
	}

}
