package chatprogram;

import java.net.*;
import java.io.*;

public class Handler implements Runnable {
	private Socket client;

	public Handler(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {

		try {
			// Streams

			OutputStream out = client.getOutputStream();
			PrintWriter writer = new PrintWriter(out);

			InputStream in = client.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));

			// Stream end

			String s = null;

			while ((s = reader.readLine()) != null) {

				writer.write(s + "\n");
				writer.flush();
				System.out.println("Empfangen von Client: " + s);

			}
			writer.close();
			reader.close();

			client.close();

		} catch (Exception e) {

		}
	}

}
