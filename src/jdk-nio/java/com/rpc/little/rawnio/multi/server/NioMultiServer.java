package com.rpc.little.rawnio.multi.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rpc.little.rawnio.key.SelectionKeyProcessor;
import com.rpc.little.rawnio.key.ServerKeyProcessor;
import com.rpc.little.rawnio.multi.ChannelInitializer;
import com.rpc.little.rawnio.multi.NioSocketChannel;
import com.rpc.little.rawnio.single.server.SimpleServerInHandler;

public class NioMultiServer {
	private static final Logger logger = LogManager.getLogger(NioMultiServer.class);
	private SelectionKeyProcessor processor = new ServerKeyProcessor();
	
	private Selector selector;
	private int port;

	public NioMultiServer(int port) {
		super();
		this.port = port;
	}

	public void handler(ChannelInitializer initializer) {
		processor.setInitializer(initializer);
	}

	public void start() throws IOException {
		ServerSocketChannel ssc = ServerSocketChannel.open();
		selector = Selector.open();
		ssc.configureBlocking(false);
		ssc.register(selector, SelectionKey.OP_ACCEPT);
		ssc.bind(new InetSocketAddress(port));
		processSelectionKeys();
	}
	
	public void shutdown(){
		if(selector == null){
			return;
		}
		try {
			selector.close();
		} catch (IOException e) {
			logger.error("shutdown server error", e);
		}
	}

	public void processSelectionKeys() throws IOException {
		while(selector.select() > 0){
			Set<SelectionKey> keys = selector.selectedKeys();
			Iterator<SelectionKey> iter = keys.iterator();
			while(iter.hasNext()){
				SelectionKey key = iter.next();
				iter.remove();
				processor.processSelectionKeys(key);
			}
		}
	}

	public static void main(String[] args) {
		NioMultiServer server = new NioMultiServer(11699);
		try {
			server.handler(new ChannelInitializer() {
				@Override
				public void initChannel(NioSocketChannel socketChannel) {
					socketChannel.channelHandler(new SimpleServerInHandler());
				}
			});
			server.start();
		} catch (IOException e) {
			logger.error("encount error:",e);
			server.shutdown();
		}
	}

}
