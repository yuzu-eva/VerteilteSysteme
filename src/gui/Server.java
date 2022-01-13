package gui;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

	// ServerSocket und PrintWriter werden deklariert
	ServerSocket server;
	ArrayList<PrintWriter> list_clientWriter;

	// Level werden deklariert f�r die Anzeige von Nachrichten in der Konsole
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
		// Server gibt Nachrichten auf der Console aus und auch in der GUI.
		// Alle Nachrichten werden zudem noch in einer Textdatei gespeichert, um den 
		// Verlauf beim spaeteren starten des Clients erneut aufzurufen.
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
	
	// Es werden genau zwei Clients akzeptiert, danach wird der ServerSocket geschlossen.
	// Versucht sich ein dritter Client zu verbinden, so empfaengt dieser eine Fehlermeldung.
	public void listenToClients() {
	
		try {
		Socket client1 = server.accept();
		PrintWriter writer1 = new PrintWriter(client1.getOutputStream());
		list_clientWriter.add(writer1);
		Thread clientThread1 = new Thread(new ClientHandler(client1));
		clientThread1.start();
		
		Socket client2 = server.accept();
		PrintWriter writer2 = new PrintWriter(client2.getOutputStream());
		list_clientWriter.add(writer2);
		Thread clientThread2 = new Thread(new ClientHandler(client2));
		clientThread2.start();
		
		server.close();
		} catch (Exception e) {
			e.printStackTrace();
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
	
	// Methode um den Chatverlauf in einer Textdatei zu speichern.
	public void saveChatHistory(String message) throws Exception {
		
		File file = new File("chatHistory.txt");
		BufferedWriter fw = new BufferedWriter(new FileWriter(file, true));
		
		fw.write(message +"\n");
		fw.flush();
		fw.close();
		
	}

}
