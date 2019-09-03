package io.hill.remoteclasses.socket;

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

	protected ServerSocket socket;
	protected List<SocketClientHandler> clientHandlers = new ArrayList<>();
	protected boolean started;
	protected Thread serverThread;

	public void listen(Integer port) throws IOException {

		if (!started) {

			socket = new ServerSocket(port);
			started = true;
			serverThread = new Thread(this::run);
		}
	}

	protected void run() {

		while (started) {

			try {

				Socket socket = acceptClient();
				SocketClientHandler socketClientHandler = createHandler(socket);
				clientHandlers.add(socketClientHandler);
			} catch (IOException e) {

				handleAcceptException(e);
			}
		}
	}

	private Socket acceptClient() throws IOException {

		return this.socket.accept();
	}

	private SocketClientHandler createHandler(Socket socket) {

		return new SocketClientHandler(createClient(socket), this);
	}

	private SocketClient createClient(Socket socket) {

		return new SocketClient(socket);
	}

	private void handleAcceptException(IOException e) {}
}
