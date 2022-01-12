package chatprogram;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client {

	private Socket socket;
	private BufferedReader reader;
	private BufferedWriter writer;
	private String username;

	public Client(Socket socket, String username) {
		try {
			this.socket = socket;
			this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.username = username;
		} catch (IOException e) {
			closeEverything(socket, reader, writer);
		}
	}

	public void sendMessage() {
		try {
			writer.write(username);
			writer.newLine();
			writer.flush();

			Scanner scanner = new Scanner(System.in);
			while (socket.isConnected()) {
				String messageToSend = scanner.nextLine();
				writer.write(username + ": " + messageToSend);
				writer.newLine();
				writer.flush();
			}
		} catch (IOException e) {
			closeEverything(socket, reader, writer);
		}
	}

	public void listenForMessage() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String msgFromGroupChat;

				while (socket.isConnected()) {
					try {
						msgFromGroupChat = reader.readLine();
						System.out.println(msgFromGroupChat);
					} catch (IOException e) {
						closeEverything(socket, reader, writer);
					}
				}

			}

		}).start();
	}

	public void closeEverything(Socket socket, BufferedReader reader, BufferedWriter writer) {
		try {
			if (reader != null) {
				reader.close();
			}
			if (writer != null) {
				writer.close();
			}
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Bitte Username eingeben: ");
		String username = scanner.nextLine();
		Socket socket = new Socket("localhost", 5554);
		Client client = new Client(socket, username);
		client.listenForMessage();
		client.sendMessage();
	}
}
