package com.source.study.rawnio.key;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import com.source.study.rawnio.multi.NioSocketChannel;

public class ClientKeyProcessor extends SelectionKeyProcessor {
	protected void processConnect(SelectionKey key) throws IOException {
		int readyOps = key.readyOps();
		if ((readyOps & SelectionKey.OP_CONNECT) == 0) {
			return;
		}
		SocketChannel channel = (SocketChannel) key.channel();
		NioSocketChannel clientNioChannel = new NioSocketChannel(channel);
		key.attach(clientNioChannel);
		if (!channel.finishConnect()) {
			throw new IOException("channel.finishConnect() = false, channel:" + channel);
		}
		key.interestOps((readyOps & ~SelectionKey.OP_CONNECT) | SelectionKey.OP_READ);
		if (initializer != null) {
			initializer.initChannel(clientNioChannel);
		}
		clientNioChannel.fireChannelActive();
	}
}
