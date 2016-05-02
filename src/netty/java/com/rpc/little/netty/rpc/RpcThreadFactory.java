package com.rpc.little.netty.rpc;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public class RpcThreadFactory implements ThreadFactory {

	private String namePrefix;
	private AtomicLong idx;

	public RpcThreadFactory(String namePrefix) {
		super();
		this.namePrefix = namePrefix;
	}

	@Override
	public Thread newThread(Runnable r) {
		return new Thread(r, namePrefix + "#" + idx.incrementAndGet());
	}

}
