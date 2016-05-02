package com.rpc.little.netty.rpc.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rpc.little.netty.rpc.xserial.IXSerializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * rpc结果数据解析<br>
 * | header | 反序列化类型 | 内容 |<br>
 * | 2 bytes | 1 byte | rest |<br>
 * 
 * @author tianmai.fh
 *
 */
public class RpcDataDecoder extends ByteToMessageDecoder {

	private static Map<Byte, IXSerializer> type2XSerializerMap = new HashMap<>();

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		short header = in.readShort();
		byte deserialType = in.readByte();
		// 如果在这里抛异常了会怎样？客户端能受到异常么
		assert header == 201;
		IXSerializer xSerializer = type2XSerializerMap.get(deserialType);
		out.add(xSerializer.deserialize(in.slice(in.readerIndex(), in.readableBytes()).array()));
	}

}
