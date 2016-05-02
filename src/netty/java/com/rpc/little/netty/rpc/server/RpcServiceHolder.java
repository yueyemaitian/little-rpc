package com.rpc.little.netty.rpc.server;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
public class RpcServiceHolder {

	private final ConcurrentHashMap<String, Method> name2MethodMap = new ConcurrentHashMap<String, Method>();

	public void publishServiceMethod(String fullClazzName, String methodName, Method method) {
		name2MethodMap.put(buildServiceKey(fullClazzName, methodName), method);
	}

	public static String buildServiceKey(String fullClazzName, String methodName) {
		return fullClazzName + "#" + methodName;
	}

	public Method getServiceMethod(String fullClazzName, String methodName) {
		return name2MethodMap.get(buildServiceKey(fullClazzName, methodName));
	}
}
