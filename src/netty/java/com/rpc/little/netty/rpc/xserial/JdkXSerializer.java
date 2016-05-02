package com.rpc.little.netty.rpc.xserial;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * jdk 序列化、反序列化器
 * 
 * @author tianmai.fh
 *
 */
public class JdkXSerializer implements IXSerializer {

	private static final IXSerializer instance = new JdkXSerializer();

	public IXSerializer getInstance() {
		return instance;
	}

	@Override
	public <T> byte[] serialize(T t) throws IOException {
		ByteArrayOutputStream bops = new ByteArrayOutputStream();
		ObjectOutputStream oops = new ObjectOutputStream(bops);
		oops.writeObject(t);
		return bops.toByteArray();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
		InputStream bips = new ByteArrayInputStream(bytes);
		ObjectInputStream ips = new ObjectInputStream(bips);
		return (T) ips.readObject();
	}

	@Override
	public byte getXSerializeType() {
		return 1;
	}

}
