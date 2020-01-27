package io.hill.remotable.proxy.client;

import io.hill.remotable.exception.BuildingException;
import io.hill.remotable.exception.ObjectInstantiationException;
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

	private T target;
	private Class<T> targetClass;
	private RemoteMethodCaller remoteMethodCaller;
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

	public T build() throws BuildingException, ObjectInstantiationException, IOException {
		this.createTarget();
		this.createRemoteMethodCaller();

		return createProxy(this.target, new RemoteInvocationHandler(this.target, this.remoteMethodCaller));
	}

	private void createTarget() throws BuildingException, ObjectInstantiationException {
		if (targetClass == null) {
			throw BuildingException.undefinedField("targetClass");
		} else {
			this.target = this.objectInstantiator.instantiate(targetClass);
		}
	}

	private void createRemoteMethodCaller() throws BuildingException, ObjectInstantiationException, IOException {
		if (remoteMethodCaller != null) {
			return;
		}
		if (remoteMethodCallerClass == null) {
			throw BuildingException.undefinedField("remoteMethodCaller");
		} else {
			SocketClient socketClient = new SocketClient();
			socketClient.connect(host, port);
			this.remoteMethodCaller = this.objectInstantiator.instantiate(remoteMethodCallerClass, socketClient);
		}
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
