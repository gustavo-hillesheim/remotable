package io.hill.remoteclasses.socket;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class SocketServerTest {

	@Test
	public void shouldConnectSocketToServerAndSendMessagesBetweenThem() throws IOException, InterruptedException {

		System.out.println();
		final String serverMessage = "Hello from server";
		final String clientMessage = "Hello from client";

		SocketServer server = new SocketServer();
		server.listen(4000);
		assertTrue(server.isRunning());

		SocketClient client = new SocketClient();
		client.connect("127.0.0.1", 4000);

		Thread.sleep(10);

		assertEquals(1, server.getSocketHandlers().size());
		SocketHandler clientHandler = server.getSocketHandlers().get(0);

		server.sendAll(serverMessage);
		client.waitForMessages();
		assertTrue(client.hasMessages());
		assertEquals(serverMessage, client.read());
		assertFalse(client.hasMessages());

		client.send(clientMessage);
		clientHandler.waitForMessages();
		assertTrue(clientHandler.hasMessages());
		assertEquals(clientMessage, clientHandler.read());
		assertFalse(clientHandler.hasMessages());

		server.close();

		assertFalse(server.isRunning());
		assertFalse(clientHandler.isConnected());
		assertEquals(0, server.getSocketHandlers().size());
	}
}