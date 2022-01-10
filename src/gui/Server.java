package gui;

import java.io.*;
import java.net.*;
import java.util.*;



public class Server {
	
	//ServerSocket und PrintWriter werden erstellt
	ServerSocket server;
	ArrayList<PrintWriter> list_clientWriter;
	
	//Level werden erstellt für die Anzeige von Nachrichten ind er Konsole
	final int LEVEL_ERROR = 1;
	final int LEVEL_NORMAL = 0;
	
	public static void main(String[] args) {
		
		//Neue Instanz des Servers wird erzeugt
		//Solange der Server läuft, hört er den Clients zu und überträgt Nachrichten
		Server s = new Server();
		if (s.runServer()) {
			s.listenToClients();
		}else {
			//Do nothing
		}
		
	}
	
	//Holt sich den InputStream des Clients
	public class ClientHandler implements Runnable {
		
		Socket client;
		BufferedReader reader;
		
		public ClientHandler(Socket client) {
			try {
				this.client = client;
				reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			}catch(IOException e) {
				e.printStackTrace();
				
			}
			
		}
		
		//Solange Nachrichten von Clients gesendet werden, gibt der Server sie weiter.
		//Server gibt Nachrichten normal auf der Console aus und auch in der GUI
		@Override
		public void run() {
			
			String nachricht;
			
			try {
				while((nachricht = reader.readLine()) != null) {
					
					appendTextToConsole("Vom Client: \n" + nachricht, LEVEL_NORMAL);
					sendToAllClients(nachricht);
					
				}
				
			} catch (IOException e) {
				e.printStackTrace();
				
			}
			
		}
		
		
	}
	//Der Server nimmt alle Client-Verbindungen an, holt sich den OUtputStream und eröffnet einen Thread
	
	public void listenToClients() {
		while(true) {
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
	
	//Wenn der Server gestartet wird, zeigt die Konsole dies im Level Error
	//Sollte der Server nicht gestartet werden können, wird die andere Nachricht ausgegeben
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
	
	//Text wird auf der Konsoke ausgegeben
	public void appendTextToConsole(String message, int level) {
		if(level == LEVEL_ERROR) {
			System.err.println(message + "\n");
		}else {
			System.out.println(message + "\n");
		}
		
	}
	
	//Der Iterator zählt die Nachrichten und solange noch eine Nachricht gesendet wird,
	// wird diese an alle Clients gecshickt
	public void sendToAllClients(String message) throws IOException {
		Iterator it = list_clientWriter.iterator();
		
		while(it.hasNext()) {
			PrintWriter writer = (PrintWriter) it.next();
			writer.println(message);
			writer.flush();
			
			//Der Versuch, alle Nachrichten in einer TXT-Datei zu speichern
			File file = new File("qwert.txt");
			BufferedWriter filewriter = new BufferedWriter(new FileWriter(file, true));
			Iterator iterator = list_clientWriter.iterator();
			while(iterator.hasNext()) {
				PrintWriter filewriter2 = (PrintWriter) iterator.next();
				filewriter.write(message);
				filewriter.flush();
			
			}
		}
		
	}
	

}
