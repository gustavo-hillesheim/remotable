package io.hill.remotable.exception;

import io.hill.remotable.Constants;

public class BuildingException extends Exception {

	public BuildingException(String message) { super(message); }

	public static BuildingException undefinedField(String field) {
		return new BuildingException(String.format(Constants.Exceptions.UNDEFINED_FIELD, field));
	}
}
