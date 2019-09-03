package example.rmi;

import example.rmi.socket.SocketClient;
import example.rmi.socket.SocketServer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class RMITest {


	@Test
	public void shouldOpenAndCloseServerAndClient() {

		try {
			SocketServer server = new SocketServer(4200);
			openClientInAnotherThread(server);
			server.start();
		} catch (Exception e) {

			e.printStackTrace();
			Assert.fail("Could not open server");
		}
	}

	private void openClientInAnotherThread(SocketServer server) {

		new Thread(() -> {
			try {
				Thread.sleep(1000);
				SocketClient client = new SocketClient("localhost", 4200);
				client.start();
				client.disconnect();
				server.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	@Test
	public void shouldCallClientCountCallsMethod() {

		try {

			SocketServer server = new SocketServer(4201);
			Assert.assertEquals(0, ClientImpl.calls);
			openClientCallerInAnotherThread(server);
			server.start();
			Assert.assertEquals(1, ClientImpl.calls);
		} catch (Exception e) {

			e.printStackTrace();
			Assert.fail("Could not open server");
		}
	}

	private void openClientCallerInAnotherThread(SocketServer server) {

		new Thread(() -> {

			try {
				Thread.sleep(1000);
				SocketClient client = new SocketClient("localhost", 4201);
				client.start();

				ClientCaller clientCaller = new ClientCaller(server);
				clientCaller.countCalls();

				Thread.sleep(500);
				client.disconnect();
				server.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	@Test
	public void shouldCallServerCountCallsMethod() {

		try {

			Assert.assertEquals(0, ServerImpl.calls);
			SocketServer server = new SocketServer(4202);
			openCalledClientInAnotherThread(server);
			server.start();
			Assert.assertEquals(1, ServerImpl.calls);
		} catch (IOException e) {

			e.printStackTrace();
			Assert.fail("Could not open server");
		}
	}

	private void openCalledClientInAnotherThread(SocketServer server) {

		new Thread(() -> {

			try {

				Thread.sleep(1000);
				SocketClient client = new SocketClient("localhost", 4202);
				client.start();

				ServerCaller serverCaller = new ServerCaller(client);
				serverCaller.countCalls();

				Thread.sleep(500);
				client.disconnect();
				server.stop();

			} catch (Exception e) {

				e.printStackTrace();
				Assert.fail("Could not execute client method");
			}
		}).start();
	}
}
