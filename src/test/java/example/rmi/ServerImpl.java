package example.rmi;

public class ServerImpl implements Server {

	public static int calls = 0;

	public void countCalls() {
		calls++;
	}
}
