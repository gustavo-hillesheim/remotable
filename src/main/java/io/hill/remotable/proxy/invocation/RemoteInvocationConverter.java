package io.hill.remotable.proxy.invocation;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class RemoteInvocationConverter<T> {

	public abstract T fromMethodInvocation(MethodInvocation methodInvocation);
	public abstract MethodInvocation toMethodInvocation(T source);
}
