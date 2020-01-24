package io.hill.remotable.exception;

public class ObjectInstantiationException extends Exception {

	public ObjectInstantiationException(String message, Object... parameters) {
		super(String.format(message, parameters));
	}
}
