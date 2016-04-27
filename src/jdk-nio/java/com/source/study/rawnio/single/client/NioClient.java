package com.source.study.rawnio.single.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.source.study.rawnio.single.ChannelHandler;
import com.source.study.rawnio.single.ChannelInHandler;

public class NioClient {
	private Selector selector;
	private static final Logger logger = LogManager.getLogger(NioClient.class);
	private List<ChannelInHandler<?>> inHandlers = new ArrayList<ChannelInHandler<?>>();

	public void init(int port) throws IOException {
		selector = Selector.open();
		SocketChannel sc = SocketChannel.open();
		sc.configureBlocking(false);
		boolean connected = sc.connect(new InetSocketAddress(port));
		sc.register(selector, SelectionKey.OP_CONNECT);
		logger.info("connected = " + connected);
		processKeys();
	}

	public void addHandler(ChannelHandler handler) {
		if (handler instanceof ChannelInHandler) {
			inHandlers.add((ChannelInHandler<?>) handler);
		}

	}

	private void processKeys() throws IOException {
		while (selector.select() > 0) {
			Set<SelectionKey> keys = selector.selectedKeys();
			processKeys(keys);
		}
	}

	private void processKeys(Set<SelectionKey> keys) {
		Iterator<SelectionKey> iter = keys.iterator();
		while (iter.hasNext()) {
			SelectionKey key = iter.next();
			iter.remove();// FIXME: 为什么要remove掉
			try {
				processConnect(key);
				processRead(key);
				processWrite(key);
			} catch (IOException e) {
				logger.error("encounter exception when process selectionkey", e);
			}
		}
	}

	private void processConnect(SelectionKey key) throws IOException {
		int readyOps = key.readyOps();
		if ((SelectionKey.OP_CONNECT & readyOps) == 0) {
			return;
		}
		logger.info("op_connect fired");
		// 这里必须把op_connect时间反注册了，否则会一直出发op_connect事件
		key.interestOps(key.interestOps() & ~SelectionKey.OP_CONNECT);
		key.attach(ByteBuffer.allocate(10 * 1024));
		for (ChannelInHandler<?> handler : inHandlers) {
			handler.channelActive((SocketChannel) key.channel());
		}
	}

	private void processRead(SelectionKey key) throws IOException {
		int readyOps = key.readyOps();
		if ((SelectionKey.OP_READ & readyOps) == 0) {
			return;
		}
		SocketChannel channel = (SocketChannel) key.channel();
		ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
		byteBuffer.clear();
		while (channel.read(byteBuffer) > 0) {
		}
		byteBuffer.flip();
		// channel.read(dst);
		for (ChannelInHandler<?> inHandler : inHandlers) {
			inHandler.read(byteBuffer, channel);
		}

	}

	private void processWrite(SelectionKey key) {
		// int readyOps = key.readyOps();
	}

	public void shutdown() throws IOException {
		selector.close();
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		NioClient nioClient = new NioClient();
		nioClient.init(11688);
		nioClient.addHandler(new SimpleChannelInHandler());
		Thread.sleep(3000);
		// 如果这里selector不关闭，直接kill进程，服务端会一直收到op_read事件，但是读不到数据，也不报IOException
		// nioClient.shutdown();
	}
}
