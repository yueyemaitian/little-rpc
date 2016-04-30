package com.rpc.little.rawnio.key;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import com.rpc.little.rawnio.multi.NioSocketChannel;

public class ServerKeyProcessor extends SelectionKeyProcessor {

	public void processConnect(SelectionKey key) throws IOException {
		int readyOps = key.readyOps();
		if ((readyOps & SelectionKey.OP_ACCEPT) == 0) {
			return;
		}
		SocketChannel channel = unsafe.accept((ServerSocketChannel) key.channel());
		Selector selector = key.selector();
		channel.configureBlocking(false);
		SelectionKey clientKey = channel.register(selector, SelectionKey.OP_READ);
		NioSocketChannel clientNioChannel = new NioSocketChannel(channel);
		clientKey.attach(clientNioChannel);
		if (initializer != null) {
			initializer.initChannel(clientNioChannel);
		}
	}
}
