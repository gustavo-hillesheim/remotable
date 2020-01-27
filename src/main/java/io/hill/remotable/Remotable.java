package io.hill.remotable;

import io.hill.remotable.exception.BuildingException;
import io.hill.remotable.exception.ObjectInstantiationException;
import io.hill.remotable.proxy.client.JsonMethodCaller;
import io.hill.remotable.proxy.client.ProxyBuilder;
import io.hill.remotable.proxy.invocation.JsonInvocationConverter;
import io.hill.remotable.proxy.server.RemoteServerBuilder;

import java.io.IOException;

public class Remotable {

	public static <T> void openServer(Class<T> clazz, Integer port) throws BuildingException, ObjectInstantiationException, IOException {
		RemoteServerBuilder.<T, String>newInstance()
			.withTarget(clazz)
			.withRemoteInvocationConverter(JsonInvocationConverter.class)
			.build()
			.open(port);
	}

	public static <T> T connectToServer(Class<T> clazz, String host, Integer port) throws BuildingException, ObjectInstantiationException, IOException {
		return ProxyBuilder.<T>newInstance()
			.withTargetClass(clazz)
			.withRemoteMethodCaller(JsonMethodCaller.class)
			.withHost(host)
			.withPort(port)
			.build();
	}
}
