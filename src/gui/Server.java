package gui;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Server {

	// ServerSocket und PrintWriter werden erstellt
	ServerSocket server;
	ArrayList<PrintWriter> list_clientWriter;

	// Level werden erstellt f�r die Anzeige von Nachrichten ind er Konsole
	final int LEVEL_ERROR = 1;
	final int LEVEL_NORMAL = 0;

	public static void main(String[] args) {

		// Neue Instanz des Servers wird erzeugt
		// Solange der Server l�uft, h�rt er den Clients zu und �bertr�gt Nachrichten
		Server s = new Server();
		if (s.runServer()) {
			s.listenToClients();
		} else {
			// Do nothing
		}

	}

	// Holt sich den InputStream des Clients
	public class ClientHandler implements Runnable {

		Socket client;
		BufferedReader reader;

		public ClientHandler(Socket client) {
			try {
				this.client = client;
				reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();

			}

		}

		// Solange Nachrichten von Clients gesendet werden, gibt der Server sie weiter.
		// Server gibt Nachrichten normal auf der Console aus und auch in der GUI.
		// Alle Nachrichten werden zudem noch in einer Textdatei gespeichert, um den 
		// Verlauf beim spaeteren starten des Clients  erneut aufzurufen.
		@Override
		public void run() {

			String nachricht;

			try {

				while ((nachricht = reader.readLine()) != null) {
					appendTextToConsole("Vom Client: \n" + nachricht, LEVEL_NORMAL);
					saveChatHistory(nachricht);
					sendToAllClients(nachricht);
				}

			} catch (IOException e) {
				e.printStackTrace();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
	// Der Server nimmt alle Client-Verbindungen an, holt sich den OutputStream und
	// er�ffnet einen Thread

	public void listenToClients() {
		while (true) {
			try {
				Socket client = server.accept();

				PrintWriter writer = new PrintWriter(client.getOutputStream());
				list_clientWriter.add(writer);

				Thread clientThread = new Thread(new ClientHandler(client));
				clientThread.start();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	// Wenn der Server gestartet wird, zeigt die Konsole dies im Level Error
	// Sollte der Server nicht gestartet werden k�nnen, wird die andere Nachricht
	// ausgegeben
	public boolean runServer() {
		try {
			server = new ServerSocket(5555);
			appendTextToConsole("Server wurde gestartet", LEVEL_ERROR);

			list_clientWriter = new ArrayList<PrintWriter>();
			return true;
		} catch (IOException e) {
			appendTextToConsole("Server konnte nicht gestartet werden!", LEVEL_ERROR);
			e.printStackTrace();
			return false;
		}

	}

	// Text wird auf der Konsole ausgegeben
	public void appendTextToConsole(String message, int level) {
		if (level == LEVEL_ERROR) {
			System.err.println(message + "\n");
		} else {
			System.out.println(message + "\n");
		}

	}

	// Der Iterator z�hlt die Nachrichten und solange noch eine Nachricht gesendet
	// wird, wird diese an alle Clients geschickt.
	public void sendToAllClients(String message) throws IOException {
		Iterator<PrintWriter> it = list_clientWriter.iterator();

		while (it.hasNext()) {
			PrintWriter pw = (PrintWriter) it.next();
			pw.println(message);
			pw.flush();

		}

	}
	
	public void saveChatHistory(String message) throws Exception {
		// txt-file und Writer um den Chatverlauf zu speichern.
		File file = new File("chatHistory.txt");
		BufferedWriter fw = new BufferedWriter(new FileWriter(file, true));
		
		fw.write(message +"\n");
		fw.flush();
		fw.close();
		
	}

}
