package example.rmi.socket;

import example.rmi.ServerImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {


	private PrintWriter writer;
	private BufferedReader reader;
	private Socket client;
	private boolean running;
	private boolean started;
	private ServerImpl serverImpl;
	private SocketServer server;

	public ClientHandler(Socket client, SocketServer server) {

		this.client = client;
		this.server = server;
		this.serverImpl = new ServerImpl();
	}

	public void run() {

		String message = "";
		while (running && !this.client.isClosed()) {

			try {

				while(reader.ready()) {

					message = reader.readLine();

					System.out.println(
						String.format(
							"Mensagem recebida do cliente: '%s'",
							message));

					if (message.equals("method:countCalls"))
						this.serverImpl.countCalls();
				}
			} catch (IOException e) {

				System.out.println("Erro ao ler entrada");
				e.printStackTrace();
			}
		}
	}

	public void start() {

		if (!started) {

			try {

				writer = new PrintWriter(client.getOutputStream());
				reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
				running = true;
				started = true;
				this.run();
			} catch (IOException e) {

				System.out.println("Não foi possível iniciar o client handler");
				e.printStackTrace();
			}
		}
	}

	public void disconnect() {

		if (started) {

			started = false;
			running = false;

			try {

				reader.close();
			} catch (IOException e) {

				System.out.println("Não foi possível fechar o reader");
				e.printStackTrace();
			}

			writer.close();

			try {
				client.close();
			} catch (IOException e) {
				System.out.println("Não foi possível fechar o client");
				e.printStackTrace();
			}

			reader = null;
			writer = null;
			client = null;
		}
	}

	public void callMethod(String methodName) {

		this.writer.println("method:" + methodName);
		this.writer.flush();
	}
}
