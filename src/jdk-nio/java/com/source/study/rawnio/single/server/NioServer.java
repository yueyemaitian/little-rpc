package com.source.study.rawnio.single.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.source.study.rawnio.handler.ChannelHandler;
import com.source.study.rawnio.handler.ChannelInHandler;

public class NioServer {
	private static final Logger logger = LogManager.getLogger(NioServer.class);
	private Selector selector;
	private List<ChannelInHandler<?>> inHandlers = new ArrayList<ChannelInHandler<?>>();

	public void addHandler(ChannelHandler handler) {
		if (handler instanceof ChannelInHandler) {
			inHandlers.add((ChannelInHandler<?>) handler);
		}
	}

	public void init(int port) throws IOException {
		ServerSocketChannel ssc = ServerSocketChannel.open();
		selector = Selector.open();
		ssc.configureBlocking(false);
		ssc.bind(new InetSocketAddress(port), 1024);
		ssc.register(selector, SelectionKey.OP_ACCEPT);
	}

	public void shutdown() throws IOException {
		selector.close();
	}

	public void listen() throws IOException {
		while (selector.select() > 0) {
			// 客户端断了的时候，为何服务端会触发两次OP_READ事件
			processKeys(selector.selectedKeys());
		}
	}

	private void processKeys(Set<SelectionKey> keys) throws IOException {
		Iterator<SelectionKey> iter = keys.iterator();
		while (iter.hasNext()) {
			SelectionKey key = iter.next();
			iter.remove();
			try {
				processSelectionKey(key);
			} catch (IOException e) {// 如果抛出IOException 很可能是客户端已经close了或者进程被kill
				logger.error("Failed to process selection key： " + key, e);
				closeIfNecessary(key);
			}
		}
	}

	private void closeIfNecessary(SelectionKey key) {
		SocketChannel channel = (SocketChannel) key.channel();
		if (!channel.isOpen() || !channel.isConnected()) {
			return;
		}
		try {
			channel.close();
		} catch (IOException e) {
			logger.error("Failed to close channel", e);
		}
	}

	public void processSelectionKey(SelectionKey key) throws IOException {
		int ops = key.readyOps();
		if ((ops & SelectionKey.OP_ACCEPT) > 0) {
			accept(key);
		}
		// client close的时候，也会触发op_read事件
		if ((ops & SelectionKey.OP_READ) > 0) {
			read(key);
		}

		if ((ops & SelectionKey.OP_READ) > 0) {
			if (!key.channel().isOpen()) {
				// key.channel().close();
			}
		}

	}

	public void read(SelectionKey key) throws IOException {
		SocketChannel sc = (SocketChannel) key.channel();
		// 如果这里一次读不完怎么办
		// sc.read(dst)
		ByteBuffer byteBuf = (ByteBuffer) key.attachment();
		while (sc.read(byteBuf) > 0) {
		}
		byteBuf.flip();
		if(byteBuf.remaining() == 0){
			return;
		}
		handleReadData(byteBuf, sc);
	}

	public void handleReadData(ByteBuffer byteBuf, SocketChannel sc) throws IOException {
		for (ChannelInHandler<?> inHandler : inHandlers) {
			inHandler.read(byteBuf, sc);
		}
	}

	public void accept(SelectionKey key) throws IOException {
		// netty里头，如何获得netty的channel对象
		ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
		SocketChannel channel = ssc.accept();
		ByteBuffer byteBuf = ByteBuffer.allocate(1024 * 8);
		channel.configureBlocking(false);
		SelectionKey chKey = channel.register(selector, SelectionKey.OP_READ);
		chKey.attach(byteBuf);// attach一块buf，避免重复申请
		// channel.finishConnect();//服务端调用这个方法是没意义的
		logger.info("receive an channel:" + channel);
	}

	public static void main(String[] args) throws InterruptedException {
		NioServer nioServer = new NioServer();
		try {
			nioServer.init(11688);

			nioServer.addHandler(new SimpleServerInHandler());
			nioServer.listen();
			Thread.sleep(10000000);
		} catch (IOException e) {
			e.printStackTrace();
			try {
				nioServer.shutdown();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
