package io.hill.remotable.exception;

public class MethodInvocationException extends Exception {

	public MethodInvocationException(String message, Object... parameters) {
		super(String.format(message, parameters));
	}
}
