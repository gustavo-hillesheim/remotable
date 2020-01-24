package io.hill.remotable.exception;

public class ProxyCreationException extends Exception {

	public ProxyCreationException(String message, Object... params) {
		super(String.format(message, params));
	}
}
