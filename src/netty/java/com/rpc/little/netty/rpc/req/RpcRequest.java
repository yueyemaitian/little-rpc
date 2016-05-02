package com.rpc.little.netty.rpc.req;

public class RpcRequest {
	private long seqNo;
	private String clazzName;
	private String methodName;
	private Object[] paramValues;

	public RpcRequest(long seqNo, String clazzName, String methodName, Object[] paramValues) {
		super();
		this.seqNo = seqNo;
		this.clazzName = clazzName;
		this.methodName = methodName;
		this.paramValues = paramValues;
	}

	public String getClazzName() {
		return clazzName;
	}

	public String getMethodName() {
		return methodName;
	}

	public Object[] getParamValues() {
		return paramValues;
	}

	public long getSeqNo() {
		return seqNo;
	}

}
