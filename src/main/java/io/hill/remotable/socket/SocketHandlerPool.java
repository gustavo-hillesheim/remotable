package io.hill.remotable.socket;

import com.machinezoo.noexception.Exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SocketHandlerPool implements Runnable {

	private final SocketServer server;
	private final List<SocketHandler> socketHandlers;
	private final Integer poolSize;
	private boolean running;

	SocketHandlerPool(SocketServer server, Integer poolSize) {
		this.server = server;
		this.socketHandlers = new ArrayList<>();
		this.poolSize = poolSize;
	}

	public void run() {
		running = true;
		while (running) {
			verifySocketHandlers();
		}
	}

	private synchronized void verifySocketHandlers() {
		socketHandlers
			.stream()
			.filter(Exceptions.wrap().predicate(SocketHandler::hasMessages))
			.map(Exceptions.wrap().function(SocketHandler::read))
			.forEach(server::dispatchReceivedMessage);
	}

	synchronized void stop() {
		running = false;
	}

	synchronized List<SocketHandler> getHandlers() {
		return socketHandlers
			.stream()
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
	}

	synchronized void addSocketHandler(SocketHandler socketHandler) {
		if (socketHandlers.size() < poolSize) {
			socketHandlers.add(socketHandler);
		}
	}

	synchronized void removeSocketHandler(SocketHandler socketHandler) {
		socketHandlers.remove(socketHandler);
		if (socketHandlers.isEmpty()) {
			stop();
		}
	}

	synchronized boolean hasSpaceInPool() {
		return socketHandlers.size() < poolSize;
	}
}
