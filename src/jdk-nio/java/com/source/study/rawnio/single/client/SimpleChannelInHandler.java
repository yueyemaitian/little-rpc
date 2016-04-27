package com.source.study.rawnio.single.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.source.study.rawnio.single.ChannelInHandler;

public class SimpleChannelInHandler implements ChannelInHandler<String> {

	private static final Logger logger = LogManager.getLogger(SimpleChannelInHandler.class);
	private ByteBuffer byteBuf = ByteBuffer.allocate(1024);
	{
		byteBuf.put("Hello World".getBytes());
		byteBuf.flip();
	}

	@Override
	public String read(ByteBuffer byteBuf, SocketChannel channel) throws IOException {
		String rst = new String(byteBuf.array());
		logger.info("Receive data from " + channel + ": " + rst);
		return rst;
	}

	@Override
	public void channelActive(SocketChannel channel) throws IOException {
		logger.info("Channel active: " + channel);
		channel.write(byteBuf);
	}

}
