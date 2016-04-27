package com.source.study.rawnio.single;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 接收数据处理器
 * 
 * @author xlemily
 *
 */
public interface ChannelInHandler<T> extends ChannelHandler {
	public T read(ByteBuffer byteBuf, SocketChannel channel) throws IOException;
}
