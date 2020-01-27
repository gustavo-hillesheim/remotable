package io.hill.remotable.proxy.server;

import io.hill.remotable.exception.BuildingException;
import io.hill.remotable.exception.ObjectInstantiationException;
import io.hill.remotable.proxy.invocation.RemoteInvocationConverter;
import io.hill.remotable.socket.SocketServer;
import io.hill.remotable.utils.ObjectInstantiator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RemoteServerBuilder<T, S> {

	private RemoteInvocationConverter<S> remoteInvocationConverter;
	private Class<? extends RemoteInvocationConverter<S>> remoteInvocationConverterClass;
	private RemoteMethodInvoker<T> remoteMethodInvoker;
	private T target;
	private Class<T> targetClass;

	private ObjectInstantiator objectInstantiator = new ObjectInstantiator();

	public RemoteServerBuilder<T, S> withRemoteInvocationConverter(RemoteInvocationConverter<S> remoteInvocationConverter) {
		this.remoteInvocationConverter = remoteInvocationConverter;
		return this;
	}

	public RemoteServerBuilder<T, S> withRemoteInvocationConverter(Class<? extends RemoteInvocationConverter<S>> remoteInvocationConverter) {
		this.remoteInvocationConverterClass = remoteInvocationConverter;
		return this;
	}

	public RemoteServerBuilder<T, S> withRemoteMethodInvoker(RemoteMethodInvoker<T> remoteMethodInvoker) {
		this.remoteMethodInvoker = remoteMethodInvoker;
		return this;
	}

	public RemoteServerBuilder<T, S> withTarget(T target) {
		this.target = target;
		return this;
	}

	public RemoteServerBuilder<T, S> withTarget(Class<T> target) {
		this.targetClass = target;
		return this;
	}

	public RemoteServer<T, S> build() throws BuildingException, ObjectInstantiationException {
		this.createTarget();
		this.createRemoteInvocationConverter();
		this.createRemoteMethodInvoker();

		RemoteServer<T, S> remoteServer = new RemoteServer<>();

		remoteServer.setTarget(this.target);
		remoteServer.setRemoteInvocationConverter(this.remoteInvocationConverter);
		remoteServer.setRemoteMethodInvoker(this.remoteMethodInvoker);
		remoteServer.setSocketServer(new SocketServer());

		return remoteServer;
	}

	private void createTarget() throws BuildingException, ObjectInstantiationException {
		if (this.target != null) {
			return;
		}
		if (this.targetClass == null) {
			throw BuildingException.undefinedField("target");
		} else {
			this.target = this.objectInstantiator.instantiate(this.targetClass);
		}
	}

	private void createRemoteInvocationConverter() throws BuildingException, ObjectInstantiationException {
		if (this.remoteInvocationConverter != null) {
			return;
		}
		if (this.remoteInvocationConverterClass == null) {
			throw BuildingException.undefinedField("remoteInvocationConverter");
		} else {
			this.remoteInvocationConverter = this.objectInstantiator.instantiate(this.remoteInvocationConverterClass);
		}
	}

	private void createRemoteMethodInvoker() {
		if (this.remoteMethodInvoker == null) {
			this.remoteMethodInvoker = new RemoteMethodInvoker<>(this.targetClass);
		}
	}

	public static <T, S> RemoteServerBuilder<T, S> newInstance() {
		return new RemoteServerBuilder<>();
	}
}
