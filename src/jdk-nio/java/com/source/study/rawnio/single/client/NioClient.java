package com.source.study.rawnio.single.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class NioClient {
	private Selector selector;

	public void init(int port) throws IOException {
		selector = Selector.open();
		SocketChannel sc = SocketChannel.open();
		sc.configureBlocking(false);
		boolean connected = sc.connect(new InetSocketAddress(port));
		if(!connected){
			sc.register(selector, SelectionKey.OP_CONNECT);
		}
		System.out.println("connected = " + connected);
	}

	
	public void shutdown() throws IOException{
		selector.close();
	}
	public static void main(String[] args) throws IOException, InterruptedException {
		NioClient nioClient = new NioClient();
		nioClient.init(11688);
		Thread.sleep(3000);
		//如果这里selector不关闭，直接kill进程，服务端会一直收到op_read事件，但是读不到数据，也不报IOException
//		nioClient.shutdown();
	}
}
