package example.rmi;

import example.rmi.socket.SocketClient;

public class ServerCaller implements Server {

	private SocketClient socket;

	public ServerCaller(SocketClient socket) {

		this.socket = socket;
	}

	public void countCalls() {

		socket.callMethod("countCalls");
	}
}
