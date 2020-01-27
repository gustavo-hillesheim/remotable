package io.hill.remotable.proxy.client;

import io.hill.remotable.proxy.invocation.MethodInvocation;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@RequiredArgsConstructor
public class RemoteInvocationHandler implements InvocationHandler {

	private final Object target;
	private final RemoteMethodCaller remoteMethodCaller;

	@Override
	public Object invoke(Object proxy, Method method, Object... args) throws Throwable {
		MethodInvocation methodInvocation = new MethodInvocation();
		methodInvocation.setMethod(method.getName());
		methodInvocation.setArguments(args);
		remoteMethodCaller.call(methodInvocation);

		return null;
	}
}
