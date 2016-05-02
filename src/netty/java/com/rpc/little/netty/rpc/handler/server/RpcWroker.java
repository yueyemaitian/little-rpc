package com.rpc.little.netty.rpc.handler.server;

import java.util.concurrent.Callable;

import com.rpc.little.netty.rpc.req.RpcRequest;
import com.rpc.little.netty.rpc.req.RpcResponse;

public class RpcWroker implements Callable<RpcResponse> {

	private RpcRequest rpcRequest;
	
	public RpcWroker(RpcRequest rpcRequest) {
		super();
		this.rpcRequest = rpcRequest;
	}

	@Override
	public RpcResponse call() throws Exception {
		String targetClazzName = rpcRequest.getClazzName();
		String methodName = rpcRequest.getMethodName();
		Object[] paramValues = rpcRequest.getParamValues();
		return null;
	}

}
