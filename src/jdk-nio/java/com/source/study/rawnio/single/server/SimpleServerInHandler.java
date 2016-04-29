package com.source.study.rawnio.single.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.source.study.rawnio.handler.ChannelInHandler;

public class SimpleServerInHandler implements ChannelInHandler<String> {
	private static final Logger logger = LogManager.getLogger(SimpleServerInHandler.class);
	private AtomicLong idx = new AtomicLong();
	private ByteBuffer byteBufCache = ByteBuffer.allocate(1024);

	@Override
	public String read(ByteBuffer byteBuf, SocketChannel channel) throws IOException {
		byte[] dst = new byte[byteBuf.remaining()];
		byteBuf.get(dst);
		byteBuf.clear();
		String rst = new String(dst);
		logger.info("Receive data from " + channel.getRemoteAddress() + ": " + rst);
		byteBufCache.clear();
		byteBufCache.put(("received#" + idx.getAndIncrement()).getBytes());
		byteBufCache.flip();
		channel.write(byteBufCache);
		return rst;
	}

	@Override
	public void channelActive(SocketChannel channel) {

	}

	@Override
	public void close(SocketChannel channel) {

	}
}
