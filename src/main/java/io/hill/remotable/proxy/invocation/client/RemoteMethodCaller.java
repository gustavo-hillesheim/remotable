package io.hill.remotable.proxy.invocation.client;

import io.hill.remotable.proxy.invocation.MethodInvocation;
import io.hill.remotable.socket.SocketClient;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class RemoteMethodCaller {

	protected final SocketClient socketClient;

	public abstract void call(MethodInvocation methodInvocation) throws Throwable;
}
