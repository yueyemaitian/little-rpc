package com.source.study.rawnio.multi.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.source.study.rawnio.key.ClientKeyProcessor;
import com.source.study.rawnio.key.SelectionKeyProcessor;
import com.source.study.rawnio.multi.ChannelInitializer;
import com.source.study.rawnio.multi.NioSocketChannel;
import com.source.study.rawnio.single.client.SimpleClientInHandler;

public class NioMultiClient {
	private static final Logger logger = LogManager.getLogger(NioMultiClient.class);
	private Selector selector;
	private SelectionKeyProcessor processor = new ClientKeyProcessor();

	public void init() throws IOException {
		selector = Selector.open();

	}

	public void handler(ChannelInitializer initializer) {
		processor.setInitializer(initializer);
	}

	public void start(String hostname, int port) throws IOException {
		SocketChannel channel = SocketChannel.open();
		channel.configureBlocking(false);
		channel.register(selector, SelectionKey.OP_CONNECT);
		channel.connect(new InetSocketAddress(hostname, port));
		// 应该独立
		processSelectionKeys();
	}

	private void shutdown() throws IOException {
		selector.close();
	}

	private void processSelectionKeys() throws IOException {
		while (selector.select() > 0) {//服务端down掉，这里会==0
			Set<SelectionKey> keys = selector.selectedKeys();
			for (SelectionKey key : keys) {
				processor.processSelectionKeys(key);
			}
		}
	}

	public static void main(String[] args) {
		NioMultiClient client = new NioMultiClient();
		try {
			client.init();
			client.handler(new ChannelInitializer() {

				@Override
				public void initChannel(NioSocketChannel socketChannel) {
					socketChannel.channelHandler(new SimpleClientInHandler());
				}
			});
			client.start("127.0.0.1", 11699);
		} catch (IOException e) {
			logger.error(e);
			try {
				client.shutdown();
			} catch (IOException e1) {
				logger.error("failed to shutdown client", e);
			}
		}
	}

}
