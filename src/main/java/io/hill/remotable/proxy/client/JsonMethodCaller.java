package io.hill.remotable.proxy.client;

import io.hill.remotable.proxy.invocation.JsonInvocationConverter;
import io.hill.remotable.proxy.invocation.MethodInvocation;
import io.hill.remotable.socket.SocketClient;

import java.io.IOException;

public class JsonMethodCaller extends RemoteMethodCaller {

	private JsonInvocationConverter converter = new JsonInvocationConverter();

	public JsonMethodCaller(SocketClient socketClient) {
		super(socketClient);
	}

	public void call(MethodInvocation methodInvocation) throws IOException {

		String methodInvocationJson = converter.fromMethodInvocation(methodInvocation);
		socketClient.send(methodInvocationJson);
	}
}
