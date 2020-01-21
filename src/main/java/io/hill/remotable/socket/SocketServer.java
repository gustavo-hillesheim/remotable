package io.hill.remotable.socket;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SocketServer {

	protected ServerSocket serverSocket;
	protected List<SocketHandler> socketHandlers = new ArrayList<>();
	protected boolean running;
	protected Thread serverThread;

	public void listen(Integer port) throws IOException {

		if (!running) {

			serverSocket = new ServerSocket(port);
			serverThread = new Thread(this::run);
			serverThread.start();
			running = true;
		}
	}

	protected void run() {

		while (running) {

			acceptConnections();
		}
	}

	private void acceptConnections() {

		Socket socket = null;
		try {
			socket = this.serverSocket.accept();
			SocketHandler socketClientHandler = new SocketHandler(socket, this);
			socketHandlers.add(socketClientHandler);
		} catch (IOException e) {

			closeSocket(socket);
			handleAcceptException(e);
		}
	}

	private void closeSocket(Socket socket) {

		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void close() throws IOException {

		if (running) {

			for (SocketHandler handler : socketHandlers) {
				handler.disconnect();
			}
			socketHandlers.clear();

			serverSocket.close();
			serverSocket = null;

			serverThread.interrupt();
			serverThread = null;

			running = false;
		}
	}

	public void sendAll(String message) throws IOException {

		for (SocketHandler handler : socketHandlers) {
			handler.send(message);
		}
	}

	private void handleAcceptException(IOException e) {
		e.printStackTrace();
	}
}
