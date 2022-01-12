package chatprogram;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	ServerSocket serverSocket;

	public Server(ServerSocket serverSocket) throws IOException {
		this.serverSocket = serverSocket;
	}

	public void startServer() {
		try {
			while (!serverSocket.isClosed()) {
				Socket socket = serverSocket.accept();
				System.out.println("Neuer Client verbunden");
				Handler handler = new Handler(socket);

				Thread thread = new Thread(handler);
				thread.start();
			}
		} catch (IOException e) {
		}
	}

	public void closeServerSocket() {
		try {
			if (serverSocket != null) {
				serverSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {

		ServerSocket serverSocket = new ServerSocket(5554);
		Server server = new Server(serverSocket);
		server.startServer();
	}
}


