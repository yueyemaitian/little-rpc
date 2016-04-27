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
import java.util.concurrent.atomic.AtomicLong;

import com.source.study.rawnio.single.ChannelHandler;
import com.source.study.rawnio.single.ChannelInHandler;

public class NioServer {

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
			Set<SelectionKey> keys = selector.selectedKeys();
			Iterator<SelectionKey> iter = keys.iterator();
			while (iter.hasNext()) {
				SelectionKey key = iter.next();
				iter.remove();
				System.out.println("receive key:" + key.channel() + ", op: " + key.interestOps());
				processSelectionKey(key);
			}
		}
	}

	public void processSelectionKey(SelectionKey key) throws IOException {
		int ops = key.readyOps();
		if ((ops & SelectionKey.OP_ACCEPT) > 0) {
			accept(key);
		}
		if ((ops & SelectionKey.OP_READ) > 0) {
			read(key);
			((SocketChannel)key.channel()).isOpen();
			((SocketChannel)key.channel()).isConnected();
			((SocketChannel)key.channel()).validOps();
		}
		//close的时候
		if((ops & SelectionKey.OP_READ) > 0){
			if(!key.channel().isOpen()){
//				key.channel().close();
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
		channel.finishConnect();
		System.out.println("receive on channel:" + channel);
	}

	public static void main(String[] args) throws InterruptedException {
		NioServer nioServer = new NioServer();
		try {
			nioServer.init(11688);

			nioServer.addHandler(new ChannelInHandler<String>() {
				private AtomicLong idx = new AtomicLong();

				@Override
				public String read(ByteBuffer byteBuf, SocketChannel channel) throws IOException {
					String rst = new String(byteBuf.array());
					byteBuf.clear();
					byteBuf.put(new String("received#" + idx.getAndIncrement()).getBytes());
					byteBuf.flip();
					channel.write(byteBuf);
					byteBuf.clear();
					return rst;
				}

				@Override
				public void channelActive(SocketChannel channel) {
					
				}
			});
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
