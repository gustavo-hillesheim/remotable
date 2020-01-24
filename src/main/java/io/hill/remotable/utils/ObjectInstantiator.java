package io.hill.remotable.utils;

import io.hill.remotable.Constants;
import io.hill.remotable.exception.ObjectInstantiationException;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ObjectInstantiator {

	public <T> T instantiate(Class<T> clazz, Object... arguments) throws ObjectInstantiationException {
		Class<?>[] argumentsClasses = getArgumentsClasses(arguments);
		String className = clazz.getCanonicalName();

		try {
			return clazz.getConstructor(argumentsClasses).newInstance(arguments);
		} catch (NoSuchMethodException | IllegalAccessException | InstantiationException e) {

			throw new ObjectInstantiationException(Constants.Exceptions.NO_CONSTRUCTOR_AVAILABLE, className, joinClasses(argumentsClasses));
		} catch (InvocationTargetException e) {

			throw new ObjectInstantiationException(Constants.Exceptions.ERROR_WHILE_INSTANTIATING, className, joinClasses(argumentsClasses));
		}
	}

	private Class<?>[] getArgumentsClasses(Object... arguments) {
		return Stream.of(arguments)
			.<Class<?>>map(Object::getClass)
			.toArray(Class[]::new);
	}

	private String joinClasses(Class<?>[] arguments) {
		return Stream.of(arguments)
			.map(Class::getSimpleName)
			.collect(Collectors.joining(", "));
	}
}
