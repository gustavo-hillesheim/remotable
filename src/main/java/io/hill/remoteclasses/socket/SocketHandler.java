package io.hill.remoteclasses.socket;

import java.io.*;
import java.net.Socket;

public class SocketHandler {

	private Socket client;
	private SocketServer server;
	private BufferedReader reader;
	private BufferedWriter writer;

	SocketHandler(Socket client, SocketServer server) throws IOException {

		this.client = client;
		this.server = server;
		setupClient(client);
	}

	protected void setupClient(Socket socket) throws IOException {

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

			client.close();
			client = null;

			server = null;
		}
	}

	public boolean isConnected() {
		return client != null && client.isConnected();
	}
}
