package io.hill.remotable.socket;

import io.hill.remotable.Constants;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Getter
@Setter
public class SocketServer {

	protected ServerSocket serverSocket;
	protected List<SocketHandlerPool> socketHandlerPools = new ArrayList<>();
	protected List<Thread> handlerPoolThreads = new ArrayList<>();
	protected boolean running;
	protected Thread serverThread;

	private Consumer<String> onReceiveMessageConsumer;

	public void open(Integer port) throws IOException {
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
			SocketHandler socketHandler = new SocketHandler(socket, this);
			addToSocketHandlerPools(socketHandler);
		} catch (IOException e) {

			closeSocket(socket);
			handleAcceptException(e);
		}
	}

	private void addToSocketHandlerPools(SocketHandler socketHandler) {
		for (SocketHandlerPool handlerPool : socketHandlerPools) {
			if (handlerPool.hasSpaceInPool()) {
				handlerPool.addSocketHandler(socketHandler);
				return;
			}
		}
		SocketHandlerPool handlerPool = new SocketHandlerPool(this, Constants.Socket.HANDLER_POOL);
		handlerPool.addSocketHandler(socketHandler);
		socketHandlerPools.add(handlerPool);

		Thread thread = new Thread(handlerPool);
		handlerPoolThreads.add(thread);
		thread.start();
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

			for (SocketHandlerPool handler : socketHandlerPools) {
				handler.stop();
			}
			socketHandlerPools.clear();
			for (Thread handlerPoolThread : handlerPoolThreads) {
				handlerPoolThread.interrupt();
			}
			handlerPoolThreads.clear();

			serverSocket.close();
			serverSocket = null;

			serverThread.interrupt();
			serverThread = null;

			running = false;
		}
	}

	public void sendAll(String message) throws IOException {
		for (SocketHandlerPool handlerPool : socketHandlerPools) {
			for (SocketHandler handler : handlerPool.getHandlers()) {
				handler.send(message);
			}
		}
	}

	public void onReceiveMessage(Consumer<String> messageConsumer) {
		this.onReceiveMessageConsumer = messageConsumer;
	}

	void dispatchReceivedMessage(String message) {
		if (this.onReceiveMessageConsumer != null) {
			this.onReceiveMessageConsumer.accept(message);
		}
	}

	private void handleAcceptException(IOException e) {
		e.printStackTrace();
	}
}
