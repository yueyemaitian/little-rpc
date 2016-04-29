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

import com.source.study.rawnio.handler.ChannelHandler;
import com.source.study.rawnio.handler.ChannelInHandler;

public class NioClient {
	private Selector selector;
	private static final Logger logger = LogManager.getLogger(NioClient.class);
	private List<ChannelInHandler<?>> inHandlers = new ArrayList<ChannelInHandler<?>>();

	public void init(int port) throws IOException {
		selector = Selector.open();
		SocketChannel sc = SocketChannel.open();
		sc.configureBlocking(false);
		boolean connected = sc.connect(new InetSocketAddress(port));
		sc.register(selector, SelectionKey.OP_CONNECT);//& SelectionKey.OP_READ 这里不能两个事件一起注册，会导致接收不到事件
//		sc.regi
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
		SocketChannel channel = (SocketChannel) key.channel();
		if(!channel.finishConnect()){
			channel.close();
			throw new IOException("failed to finish connect");
		}
		// 这里必须把op_connect时间反注册了，否则会一直出发op_connect事件；同时注册READ事件
		key.interestOps((key.interestOps() & ~SelectionKey.OP_CONNECT) | SelectionKey.OP_READ);
		key.attach(ByteBuffer.allocate(10 * 1024));
		for (ChannelInHandler<?> handler : inHandlers) {
			handler.channelActive(channel);
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
		if(byteBuffer.remaining() == 0){
			return;
		}
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
		nioClient.addHandler(new SimpleClientInHandler());
		nioClient.init(11688);
	}
}
