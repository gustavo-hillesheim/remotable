package io.hill.remotable.proxy.server;

import io.hill.remotable.exception.MethodInvocationException;
import io.hill.remotable.proxy.invocation.MethodInvocation;
import io.hill.remotable.proxy.invocation.RemoteInvocationConverter;
import io.hill.remotable.socket.SocketServer;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;

@Setter
@NoArgsConstructor
public class RemoteServer<T, S> {

	private SocketServer socketServer;
	private RemoteInvocationConverter<S> remoteInvocationConverter;
	private RemoteMethodInvoker<T> remoteMethodInvoker;
	private T target;

	public void open(Integer port) throws IOException {

		socketServer.onReceiveMessage(this::onReceiveMessage);
		socketServer.open(port);
	}

	private void onReceiveMessage(String message) {

		MethodInvocation methodInvocation = remoteInvocationConverter.toMethodInvocation((S) message);
		try {
			remoteMethodInvoker.invoke(methodInvocation, target);
		} catch (MethodInvocationException e) {
			e.printStackTrace();;
		}
	}
}
