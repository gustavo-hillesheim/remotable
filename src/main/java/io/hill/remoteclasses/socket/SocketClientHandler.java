package io.hill.remoteclasses.socket;

public class SocketClientHandler {

	private SocketClient socketClient;
	private SocketServer socketServer;

	SocketClientHandler(SocketClient socketClient, SocketServer socketServer) {

		this.socketClient = socketClient;
		this.socketServer = socketServer;
	}
}
