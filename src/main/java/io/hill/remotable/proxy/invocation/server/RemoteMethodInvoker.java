package io.hill.remotable.proxy.invocation.server;

import com.machinezoo.noexception.Exceptions;
import io.hill.remotable.Constants;
import io.hill.remotable.exception.MethodInvocationException;
import io.hill.remotable.proxy.invocation.MethodInvocation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RemoteMethodInvoker<T> {

	private Map<String, Method> methodMap = new HashMap<>();

	public RemoteMethodInvoker(T target) {

		Method[] methods = target.getClass().getMethods();
		Stream.of(methods)
			.filter(method -> Modifier.isPublic(method.getModifiers()))
			.forEach(method -> methodMap.put(method.getName(), method));
	}

	public void invoke(MethodInvocation methodInvocation, T target) throws MethodInvocationException {
		String methodName = methodInvocation.getMethod();
		Object[] arguments = methodInvocation.getArguments();
		String className = target.getClass().getSimpleName();

		if (!methodMap.containsKey(methodName)) {
			throw new MethodInvocationException(Constants.Exceptions.METHOD_NOT_FOUND, methodName, className);
		}

		Method method = methodMap.get(methodName);
		try {
			method.invoke(target, arguments);
		} catch (IllegalArgumentException e) {
			throw new MethodInvocationException(Constants.Exceptions.INVALID_ARGUMENTS, methodName, className, join(arguments));
		} catch (IllegalAccessException e) {
			throw new MethodInvocationException(Constants.Exceptions.METHOD_NOT_FOUND, methodName, className);
		} catch (InvocationTargetException e) {
			throw new MethodInvocationException(Constants.Exceptions.INTERNAL_METHOD_EXCEPTION, methodName, className, e.getMessage());
		} catch (NullPointerException | ExceptionInInitializerError e) {
			e.printStackTrace();
		}
	}

	private String join(Object[] arguments) {
		return Stream.of(arguments)
			.map(Object::toString)
			.collect(Collectors.joining(", "));
	}
}
