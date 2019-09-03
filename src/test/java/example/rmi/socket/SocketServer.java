package example.rmi.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer implements SocketCaller, Runnable {


	private ServerSocket serverSocket;
	private boolean started;
	private ClientHandler client;

	public SocketServer(Integer port) throws IOException {

		this.serverSocket = new ServerSocket(port);
	}

	public void run() {

		try {

			Socket client = serverSocket.accept();
			this.client = new ClientHandler(client, this);
			this.client.start();
		} catch (IOException e) {
			System.out.println("Não foi possível conectar com o cliente");
			e.printStackTrace();
		}
	}

	public void start() {

		if (!started) {

			started = true;
			this.run();
		}
	}

	public void stop() {

		if (started) {

			if (this.client != null)
				this.client.disconnect();
			started = false;
			try {
				this.serverSocket.close();
			} catch (IOException e) {
				System.out.println("Não foi possível fechar o servidor");
				e.printStackTrace();
			}
		}
	}

	public void callMethod(String methodName) {

		System.out.println(
			String.format("Calling Client '%s' method", methodName));
		this.client.callMethod(methodName);
	}
}
