package com.rpc.little.netty.rpc.handler.server;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.rpc.little.netty.rpc.RpcThreadFactory;
import com.rpc.little.netty.rpc.req.RpcRequest;
import com.rpc.little.netty.rpc.req.RpcResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class RpcBizInvokeHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		RpcRequest rpcReq = (RpcRequest) msg;
		ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 200, 30L, TimeUnit.MINUTES,
				new ArrayBlockingQueue<Runnable>(1024), new RpcThreadFactory("rpc-worker"),
				new RejectedExecutionHandler() {
					@Override
					public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
						
					}

				});
		Future<RpcResponse> future = executor.submit(new RpcWroker(rpcReq));
//		future.
	}

}
