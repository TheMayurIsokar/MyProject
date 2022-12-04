package chatapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

	Socket socket;
	BufferedReader input;
	PrintWriter output;

	public Client() {

		try {
			System.out.println("Sending request to server");
			socket = new Socket("127.0.0.1", 2278);
			System.out.println("Done");
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream());
			startReading();
			starWriting();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void startReading() {

		// thread-read karke deta rahega

		Runnable r1 = () -> {
			System.out.println("Reader Started....");
			try {
				while (true) {

					String message = input.readLine();
					if (message.equals("by")) {
						System.out.println("Server terminated the chat");
						socket.close();
						break;
					}
					System.out.println("Server :" + message);

				}
			} catch (IOException e) {

				// e.printStackTrace();
				System.out.println("Connection closed");
			}
		};
		new Thread(r1).start();

	}

	public void starWriting() {

		// thread-usere se data lega or then send bhi karega client tak

		Runnable r2 = () -> {
			System.out.println("Writer started...");
			try {
				while (!socket.isClosed()) {

					BufferedReader input2 = new BufferedReader(new InputStreamReader(System.in));
					String content = input2.readLine();
					output.println(content);
					output.flush();
					if (content.equals("by")) {
						socket.close();
						break;
					}

				}
				System.out.println("Connection closed");

			} catch (IOException e) {

				e.printStackTrace();
			}

		};
		new Thread(r2).start();

	}

	public static void main(String[] args) {
		System.out.println("This is Clienr... ");
		new Client();
	}

}
