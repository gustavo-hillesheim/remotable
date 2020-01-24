package io.hill.remotable.proxy;

import io.hill.remotable.exception.ObjectInstantiationException;
import io.hill.remotable.proxy.invocation.client.RemoteInvocationHandler;
import io.hill.remotable.proxy.invocation.client.RemoteMethodCaller;
import io.hill.remotable.socket.SocketClient;
import io.hill.remotable.utils.ObjectInstantiator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProxyBuilder<T> {

	private ObjectInstantiator objectInstantiator = new ObjectInstantiator();

	private Class<T> targetClass;
	private Class<? extends RemoteMethodCaller> remoteMethodCallerClass;
	private String host;
	private Integer port;

	public ProxyBuilder<T> withTargetClass(Class<T> targetClass) {
		this.targetClass = targetClass;
		return this;
	}

	public ProxyBuilder<T> withRemoteMethodCaller(Class<? extends RemoteMethodCaller> remoteMethodCallerClass) {
		this.remoteMethodCallerClass = remoteMethodCallerClass;
		return this;
	}

	public ProxyBuilder<T> withHost(String host) {
		this.host = host;
		return this;
	}

	public ProxyBuilder<T> withPort(Integer port) {
		this.port = port;
		return this;
	}

	public T build() throws ObjectInstantiationException, IOException {
		SocketClient socketClient = new SocketClient();
		socketClient.connect(host, port);

		T target = objectInstantiator.instantiate(targetClass);
		RemoteMethodCaller remoteMethodCaller = objectInstantiator.instantiate(remoteMethodCallerClass, socketClient);

		return createProxy(target, new RemoteInvocationHandler(target, remoteMethodCaller));
	}

	@SuppressWarnings("unchecked")
	private T createProxy(T target, RemoteInvocationHandler remoteInvocationHandler) {

		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(targetClass);
		enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
			remoteInvocationHandler.invoke(target, method, args);
			return null;
		});

		return (T) enhancer.create();
	}

	public static <T> ProxyBuilder<T> newInstance() {
		return new ProxyBuilder<>();
	}
}
