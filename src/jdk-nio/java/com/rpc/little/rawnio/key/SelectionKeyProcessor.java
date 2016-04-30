package com.rpc.little.rawnio.key;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rpc.little.rawnio.Unsafe;
import com.rpc.little.rawnio.multi.ChannelInitializer;
import com.rpc.little.rawnio.multi.NioSocketChannel;

public abstract class SelectionKeyProcessor {
	private static final Logger logger = LogManager.getLogger(SelectionKeyProcessor.class);
	protected Unsafe unsafe = new Unsafe();
	protected ChannelInitializer initializer;

	public void setInitializer(ChannelInitializer initializer) {
		this.initializer = initializer;
	}

	public void processSelectionKeys(SelectionKey key) throws IOException {
		logger.info("channel " + key.channel() + ", ops = " + key.readyOps());
		processConnect(key);
		try {
			processRead(key);
		} catch (IOException e) {
			close(key);
		}
		processWrite(key);
	}

	abstract protected void processConnect(SelectionKey key) throws IOException;

	private void close(SelectionKey key) {
		try {
			logger.info("close " + key.channel() + ", ops = " + key.readyOps());
			key.channel().close();
			NioSocketChannel clientNioChannel = (NioSocketChannel) key.attachment();
			clientNioChannel.fireChannelClose();
		} catch (IOException e2) {
			logger.error("failed to close channel " + key.channel(), e2);
		}
	}

	private void processRead(SelectionKey key) throws IOException {
		int readyOps = key.readyOps();
		if ((readyOps & SelectionKey.OP_READ) == 0) {
			return;
		}
		SocketChannel channel = (SocketChannel) key.channel();
		ByteBuffer byteBuf = ByteBuffer.allocate(10240);
		unsafe.read(channel, byteBuf);
		NioSocketChannel clientNioChannel = (NioSocketChannel) key.attachment();
		clientNioChannel.fireChannelRead(byteBuf);
	}

	private void processWrite(SelectionKey key) {
		// int readyOps = key.readyOps();
		// if ((readyOps & SelectionKey.OP_WRITE) == 0) {
		// return;
		// }
	}
}
