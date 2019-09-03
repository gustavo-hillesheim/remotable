package example.rmi;

import example.rmi.socket.SocketServer;

public class ClientCaller implements Client {

	private SocketServer socket;

	public ClientCaller(SocketServer socket) {

		this.socket = socket;
	}

	public void countCalls() {

		socket.callMethod("countCalls");
	}
}
