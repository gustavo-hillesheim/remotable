package io.hill.remotable;

import io.hill.remotable.exception.MethodInvocationException;
import io.hill.remotable.exception.ObjectInstantiationException;
import io.hill.remotable.proxy.ProxyBuilder;
import io.hill.remotable.proxy.invocation.JsonInvocationConverter;
import io.hill.remotable.proxy.invocation.MethodInvocation;
import io.hill.remotable.proxy.invocation.RemoteInvocationConverter;
import io.hill.remotable.proxy.invocation.client.JsonMethodCaller;
import io.hill.remotable.proxy.invocation.server.RemoteMethodInvoker;
import io.hill.remotable.socket.SocketServer;
import io.hill.remotable.utils.ObjectInstantiator;

import java.io.IOException;

public class Remotable {

	private static ObjectInstantiator objectInstantiator = new ObjectInstantiator();

	public static <T> void openServer(Class<T> clazz, Integer port) throws ObjectInstantiationException, IOException {
		SocketServer server = new SocketServer();
		server.open(port);

		T target = objectInstantiator.instantiate(clazz);
		RemoteInvocationConverter<String> remoteInvocationConverter = new JsonInvocationConverter();
		RemoteMethodInvoker<T> remoteMethodInvoker = new RemoteMethodInvoker<>(target);

		server.onReceiveMessage(message -> {
			MethodInvocation methodInvocation = remoteInvocationConverter.toMethodInvocation(message);
			try {
				remoteMethodInvoker.invoke(methodInvocation, target);
			} catch (MethodInvocationException e) {
				e.printStackTrace();
			}
		});
	}

	public static <T> T connectToServer(Class<T> clazz, String host, Integer port) throws ObjectInstantiationException, IOException {
		return ProxyBuilder.<T>newInstance()
			.withTargetClass(clazz)
			.withRemoteMethodCaller(JsonMethodCaller.class)
			.withHost(host)
			.withPort(port)
			.build();
	}
}
