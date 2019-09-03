package example.rmi;

public class ClientImpl implements Client {

	public static int calls = 0;

	public void countCalls() {
		calls++;
	}
}
