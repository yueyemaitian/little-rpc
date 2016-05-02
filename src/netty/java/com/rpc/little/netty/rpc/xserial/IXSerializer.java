package com.rpc.little.netty.rpc.xserial;

import java.io.IOException;

/**
 * 序列化、反序列化器
 * 
 * @author tianmai.fh
 *
 */
public interface IXSerializer {

	public byte getXSerializeType();

	public <T> byte[] serialize(T t) throws IOException;

	public <T> T deserialize(byte[] bytes) throws IOException, ClassNotFoundException;
}
