package io.hill.remoteclasses.socket;

import java.net.Socket;
import java.util.function.Supplier;

public interface MessageReader<T> {

	void init(Socket socket, Supplier<T> onMessage);
	T read(Socket socket);
}
