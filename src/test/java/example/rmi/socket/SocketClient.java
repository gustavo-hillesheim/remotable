package example.rmi.socket;

import example.rmi.ClientImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClient implements SocketCaller, Runnable {


	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;
	private boolean running;
	private boolean started;
	private Thread clientThread;
	private ClientImpl clientImpl;

	public SocketClient(String host, Integer port) throws IOException {

		this.clientImpl = new ClientImpl();
		this.socket = new Socket(host, port);
	}

	public void run() {

		String message = "";
		while (running && !this.socket.isClosed()) {

			try {

				while(reader.ready()) {

					message = reader.readLine();

					System.out.println(
						String.format("Mensagem recebida do server: '%s'",
							message)
					);
					if (message.equals("method:countCalls"))
						this.clientImpl.countCalls();
				}
			} catch (IOException e) {

				System.out.println("Erro ao receber mensagem");
				e.printStackTrace();
			}
		}
	}

	public void start() {

		if (!started) {

			try {

				writer = new PrintWriter(socket.getOutputStream());
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				running = true;
				started = true;
				clientThread = new Thread(this);
				clientThread.start();
			} catch (IOException e) {

				System.out.println("Não foi possível iniciar o client");
				e.printStackTrace();
			}
		}
	}

	public void disconnect() {

		if (started) {

			running = false;
			if (this.clientThread != null)
				this.clientThread.interrupt();
			this.clientThread = null;

			try {
				reader.close();
			} catch (IOException e) {

				System.out.println("Não foi possível fechar o reader");
				e.printStackTrace();
			}

			writer.close();
			try {
				socket.close();
			} catch (IOException e) {
				System.out.println("Não foi possível fechar o socket");
				e.printStackTrace();
			}
			writer = null;
			reader = null;
			socket = null;
 		}
	}

	public void callMethod(String methodName) {

		System.out.println(String.format("Calling Server '%s' method", methodName));
		this.writer.println("method:" + methodName);
		this.writer.flush();
	}
}
