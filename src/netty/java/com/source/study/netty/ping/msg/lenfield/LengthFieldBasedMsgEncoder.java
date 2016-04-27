package com.source.study.netty.ping.msg.lenfield;

@SuppressWarnings("unused")
public class LengthFieldBasedMsgEncoder {
	private byte[] msgHeader;
	private int lengthFieldOffset;

	private int lengthFieldLength;
	private int lengthFieldAdjustment;
}
