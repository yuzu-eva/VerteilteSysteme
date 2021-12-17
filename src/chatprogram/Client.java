package chatprogram;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
	public static void main(String[] args) {

		Scanner eingabe = new Scanner(System.in);

		try {

			Socket client = new Socket("localhost", 5555);
			System.out.println("Client gestartet!");

			// Stream

			OutputStream out = client.getOutputStream();
			PrintWriter writer = new PrintWriter(out);

			InputStream in = client.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));

			// Stream end

			System.out.print("Eingabe: ");
			String anServer = eingabe.nextLine();

			writer.write(anServer + "\n");
			writer.flush();

			String s = null;

			while ((s = reader.readLine()) != null) {

				System.out.println("Empfangen vom Server: " + s);

			}

			// flush() zum Aktualisieren
			reader.close();
			writer.close();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
