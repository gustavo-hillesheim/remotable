package io.hill.remoteclasses.socket;

import java.io.PrintWriter;
import java.net.Socket;

public interface MessageWriter {

	void init(PrintWriter writer, Socket socket);
}
