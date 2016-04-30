package com.rpc.little.rawnio.multi;

public interface ChannelInitializer {
	
	public void initChannel(NioSocketChannel socketChannel);
}
