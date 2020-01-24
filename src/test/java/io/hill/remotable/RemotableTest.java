package io.hill.remotable;

import org.junit.Test;

import static org.junit.Assert.*;

public class RemotableTest {

	@Test
	public void shouldCreateServerConnectToItAndCallMethod() throws Exception {
		Remotable.openServer(TestClass.class, 4040);
		TestClass testClass = Remotable.connectToServer(TestClass.class, "localhost", 4040);
		testClass.test();

		Thread.sleep(1000);
	}
}