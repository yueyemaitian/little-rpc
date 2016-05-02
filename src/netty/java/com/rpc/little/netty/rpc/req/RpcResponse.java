package com.rpc.little.netty.rpc.req;

public class RpcResponse {
	private long seqNo;
	private Object result;

	public long getSeqNo() {
		return seqNo;
	}

	public Object getResult() {
		return result;
	}

	public RpcResponse(long seqNo, Object result) {
		super();
		this.seqNo = seqNo;
		this.result = result;
	}

}
