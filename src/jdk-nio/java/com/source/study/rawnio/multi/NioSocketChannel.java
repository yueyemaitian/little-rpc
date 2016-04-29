package com.source.study.rawnio.multi;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import com.source.study.rawnio.handler.ChannelHandler;
import com.source.study.rawnio.handler.ChannelInHandler;

public class NioSocketChannel {

	private List<ChannelInHandler<?>> inHandlers = new ArrayList<>();
	// private ByteBuffer readByteBuffer
	private SocketChannel channel;

	public NioSocketChannel(SocketChannel channel) {
		super();
		this.channel = channel;
	}

	public void channelHandler(ChannelHandler handler) {
		if (handler instanceof ChannelInHandler) {
			this.inHandlers.add((ChannelInHandler<?>) handler);
		}
	}

	public void fireChannelRead(ByteBuffer byteBuf) throws IOException {
		for (ChannelInHandler<?> handler : inHandlers) {
			handler.read(byteBuf, channel);
		}
	}

	public void fireChannelClose() throws IOException {
		for (ChannelInHandler<?> handler : inHandlers) {
			handler.close(channel);
		}
	}
	public void fireChannelActive() throws IOException {
		for (ChannelInHandler<?> handler : inHandlers) {
			handler.channelActive(channel);
		}
	}
	
}
