package io.hill.remotable;

public class Constants {

	public static class Exceptions {

		public static final String NO_CONSTRUCTOR_AVAILABLE = "No public constructor was found for class %s with argument(s) of type(s) %s.";
		public static final String ERROR_WHILE_INSTANTIATING = "An exception occurred while trying to instantiate object of class %s with argument(s) %s.";
		public static final String METHOD_NOT_FOUND = "A public method %s was not found in class %s.";
		public static final String INVALID_ARGUMENTS = "Method %s in class %s could not be invoked using arguments %s.";
		public static final String INTERNAL_METHOD_EXCEPTION = "method %s in class %s threw an exception: %s.";
	}

	public static class Socket {

		public static final Integer HANDLER_POOL = 5;
	}
}
