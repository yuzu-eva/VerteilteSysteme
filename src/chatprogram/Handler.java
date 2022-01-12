package chatprogram;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Handler implements Runnable {
	public static ArrayList<Handler> handlers = new ArrayList<>();

	private Socket socket;
	private BufferedReader reader;
	private BufferedWriter writer;
	private String clientUsername;

	public Handler(Socket socket) {
		try {
			this.socket = socket;
			this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.clientUsername = reader.readLine(); //Erste Eingabe ist Username
			handlers.add(this); //Fügt Username zu Liste hinzu
			broadcastMessage("Server: " + clientUsername + " ist dem Chat beigetreten.");
		} catch (IOException e) {
			closeEverything(socket, reader, writer);
		}
	}

	@Override
	public void run() {
		String messageFromClient;

		while (socket.isConnected()) {
			try {
				messageFromClient = reader.readLine();
				broadcastMessage(messageFromClient);
			} catch (IOException e) {
				closeEverything(socket, reader, writer);
				break;
			}
		}

	}

	public void broadcastMessage(String messageToSend) {
		for (Handler handler : handlers) {
			try {
				if (!handler.clientUsername.equals(clientUsername)) {
					handler.writer.write(messageToSend);
					handler.writer.newLine();
					handler.writer.flush();
				}
			} catch (IOException e) {
				closeEverything(socket, reader, writer);
			}
		}
	}

	public void removeHandler() {
		handlers.remove(this);
		broadcastMessage("Server: " + clientUsername + " hat den Chat verlassen.");

	}

	public void closeEverything(Socket socket, BufferedReader reader, BufferedWriter writer) {
		removeHandler();
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
}
