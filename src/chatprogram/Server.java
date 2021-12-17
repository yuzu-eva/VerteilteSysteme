package chatprogram;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	public static void main(String[] args) {

		ExecutorService executor = Executors.newFixedThreadPool(30);

		ServerSocket server;
		try {
			server = new ServerSocket(5555);
			System.out.println("Server gestartet!");

			while (true) {

				try {

					Socket client = server.accept();

					executor.execute(new Handler(client));
					// Thread t = new Thread(new Handler(client));
					// t.start();

				} catch (IOException e) {

					e.printStackTrace();

				}
			}

		} catch (IOException e1) {

			e1.printStackTrace();

		}
	}

}
