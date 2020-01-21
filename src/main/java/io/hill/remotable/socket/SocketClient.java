package io.hill.remotable.socket;

import lombok.NoArgsConstructor;

import java.io.*;
import java.net.Socket;

@NoArgsConstructor
public class SocketClient {

	private Socket socket;
	private BufferedReader reader;
	private BufferedWriter writer;

	public void connect(String host, Integer port) throws IOException {

		socket = new Socket(host, port);
		setupSocket(socket);
	}

	protected void setupSocket(Socket socket) throws IOException {

		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	}

	public void send(String message) throws IOException {
		writer.write(message + '\n');
		writer.flush();
	}

	public String read() throws IOException {
		return reader.readLine();
	}

	public boolean hasMessages() throws IOException {
		return reader.ready();
	}

	public synchronized void waitForMessages() throws IOException {
		while(!hasMessages()) {}
	}

	public void disconnect() throws IOException {

		if (isConnected()) {
			reader.close();
			reader = null;

			writer.close();
			writer = null;

			socket.close();
			socket = null;
		}
	}

	public boolean isConnected() {
		return socket != null && socket.isConnected();
	}
}
