package gui;

import javax.swing.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

import gui.Log;

public class Client {

	// Alle Elemente der GUI werden erstellt
	JFrame clientFrame;
	JPanel clientPanel;
	JTextArea textPane = new JTextArea();

	// textPane.setText("");
	// StyledDocument doc = textPane.getDocument();
	// SimpleAttributeSet keyWord = new SimpleAttributeSet();

	JTextField textField_ClientMessages;
	JButton button_SendMessage;
	JButton button_emoji_laugh;
	JButton button_emoji_smile;
	JButton button_emoji_blank;
	JButton button_emoji_crying;
	JButton button_emoji_plane;
	static JTextField textField_Username;

	Socket client;
	PrintWriter writer;
	BufferedReader reader;
	Date date;

	// Key und IV zum Ver-/Entschluesseln der Nachrichten.
	private static final String secretKey = "GqHUYGzzgVeQezZZEPJwSw==";
	private static final String initialValue = "ImLWb42jXO8VnvkQ";

	public static void main(String[] args) {

		// Neue Instanz der Klasse Client wird erstellt, fuer die eine GUI erstellt wird
		Client c = new Client();
		c.createGUI("");
	}

	// Erstellung der GUI
	public void createGUI(String uName) {
		clientFrame = new JFrame("Chatprogramm");
		clientFrame.setSize(800, 600);

		clientPanel = new JPanel();

		textPane = new JTextArea();
		// textPane.setEditable(false);
		textPane.setFont(new Font("SansSerif", Font.PLAIN, 12));

		textField_ClientMessages = new JTextField(38);
		textField_ClientMessages.addKeyListener(new SendPressEnterListener());
		textField_ClientMessages.setFont(new Font("SansSerif", Font.PLAIN, 12));

		button_SendMessage = new JButton("Senden");
		button_SendMessage.addActionListener(new SendButtonListener());

		button_emoji_laugh = new JButton("\uD83D\uDE02");
		button_emoji_laugh.addActionListener(new Klick_Emoji1_Listener());

		button_emoji_smile = new JButton("\uD83D\uDE00");
		button_emoji_smile.addActionListener(new Klick_Emoji2_Listener());

		button_emoji_blank = new JButton("\uD83D\uDE11");
		button_emoji_blank.addActionListener(new Klick_Emoji3_Listener());

		button_emoji_crying = new JButton("\uD83D\uDE2D");
		button_emoji_crying.addActionListener(new Klick_Emoji4_Listener());

		button_emoji_plane = new JButton("\u2708");
		button_emoji_plane.addActionListener(new Klick_Emoji5_Listener());

		textField_Username = new JTextField(10);
		textField_Username.setFont(new Font("SansSerif", Font.PLAIN, 10));
		textField_Username.setText(uName);
		;

		JScrollPane scrollPane_Messages = new JScrollPane(textPane);
		scrollPane_Messages.setPreferredSize(new Dimension(700, 500));
		scrollPane_Messages.setMinimumSize(new Dimension(700, 500));
		scrollPane_Messages.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_Messages.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		if (!connectToServer()) {
			// Evtl. Connect-Label in GUI und Console
		}

		// Thread wird erstellt
		Thread t = new Thread(new MessagesFromServerListener());
		t.start();

		// GUI-Elemete werden in clientPanel zusammengefasst
		clientPanel.add(scrollPane_Messages);
		clientPanel.add(textField_Username);
		clientPanel.add(textField_ClientMessages);
		clientPanel.add(button_SendMessage);
		clientPanel.add(button_emoji_laugh);
		clientPanel.add(button_emoji_smile);
		clientPanel.add(button_emoji_blank);
		clientPanel.add(button_emoji_crying);
		clientPanel.add(button_emoji_plane);

		// Es wird ein BorderLayout hinzugef�gt
		clientFrame.getContentPane().add(BorderLayout.CENTER, clientPanel);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		// Erlaubt das Schliessen der Applikation durch das rote X
		clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		clientFrame.setLocation((int) (dim.width / 3.5), (int) (dim.height / 7));
		clientFrame.setVisible(true);

	}

	// Client im localhost verbindet sich mit dem Server
	public boolean connectToServer() {
		try {
			client = new Socket("localhost", 5555);
			reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			writer = new PrintWriter(client.getOutputStream());
			appendTextMessages("Netzwerkverbindung hergestellt");
			retrieveChatHistory();

			return true;
		} catch (Exception e) {
			appendTextMessages("Netzwerkverbindung nicht m�glich");
			e.printStackTrace();

			return false;
		}
	}

	// Methode zum Aufrufen des Chatverlaufs.
	public void retrieveChatHistory() throws Exception {
		AESEncryptor aes = new AESEncryptor();
		aes.initFromStrings(secretKey, initialValue);

		File file = new File("chatHistory.txt");
		Scanner scan = new Scanner(file);

		while (scan.hasNextLine()) {
			String decryptedHistory = aes.decrypt(scan.nextLine());
			appendTextMessages(decryptedHistory);
		}

		scan.close();

	}

	public void sendMessageToServer() throws Exception {

		// Encryptor-Object initialisieren
		AESEncryptor aes = new AESEncryptor();
		aes.initFromStrings(secretKey, initialValue);

		// Zeitstempel wird erstellt
		SimpleDateFormat date = new SimpleDateFormat("HH:mm");
		String timeStamp = date.format(new Date());

		// Bugfix: Neue Zeile; neue Nachricht
		// System.out.println(textField_ClientMessages);

		// Nachricht wird mit AES/GCM-Algorithmus verschluesselt und gesendet
		String encryptedMessage = aes.encrypt(
				timeStamp + " Uhr | " + textField_Username.getText() + ": " + textField_ClientMessages.getText());
		writer.println(encryptedMessage);
		writer.flush();

		// Eingabefeld wird wieder leer und der Client kann direkt weiterschreiben.
		textField_ClientMessages.setText("");
		textField_ClientMessages.requestFocus();

	}

	public void appendTextMessages(String message) {
		// Nachricht wird angezeigt
		textPane.append(message + "\n");
		// try {
		// Document doc =

		// doc.insertString(doc.getLength(), message, null);
		// } catch(BadLocationException exc) {
		// exc.printStackTrace();
		// }
		// textPane.setText(message + "\n");

	}

	// Nachricht wird durch das Druecken von Enter versendet
	public class SendPressEnterListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent arg0) {
			if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
				try {
					sendMessageToServer();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {

		}

		@Override
		public void keyTyped(KeyEvent arg0) {
		}

	}

	// Nachricht wird durch das Druecken des Buttons "Senden" verschickt
	public class SendButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				sendMessageToServer();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}

	// Emoji wird bei Klick des Buttons eingefuegt
	public class Klick_Emoji1_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e1) {
			textField_ClientMessages.setText(textField_ClientMessages.getText() + "\uD83D\uDE02");
			// textField_ClientMessages.setText("\uD83D\uDE02");
			// sendMessageToServer();
		}
	}

	// Emoji wird bei Klick des Buttons eingefuegt
	public class Klick_Emoji2_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e1) {
			textField_ClientMessages.setText(textField_ClientMessages.getText() + "\uD83D\uDE00");
			// sendMessageToServer();
		}
	}

	// Emoji wird bei Klick des Buttons eingefuegt
	public class Klick_Emoji3_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e1) {
			textField_ClientMessages.setText(textField_ClientMessages.getText() + "\uD83D\uDE11");
			// sendMessageToServer();
		}
	}

	// Emoji wird bei Klick des Buttons eingefuegt
	public class Klick_Emoji4_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e1) {
			textField_ClientMessages.setText(textField_ClientMessages.getText() + "\uD83D\uDE2D");
			// sendMessageToServer();
		}
	}

	// Emoji wird bei Klick des Buttons eingefuegt
	public class Klick_Emoji5_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e1) {
			textField_ClientMessages.setText(textField_ClientMessages.getText() + "\u2708");
			// sendMessageToServer();
		}
	}

	// Nachrichten werden so lange empfangen, wie noch Nachrichten gesendet werden
	public class MessagesFromServerListener implements Runnable {

		@Override
		public void run() {
			String message;

			// Encryptor-Object initialisieren
			AESEncryptor aes = new AESEncryptor();
			aes.initFromStrings(secretKey, initialValue);

			try {
				while ((message = reader.readLine()) != null) {
					// Die vom Server empfangene Nachricht wird entschluesselt und ausgegeben.
					String decryptedMessage = aes.decrypt(message);
					appendTextMessages(decryptedMessage);

				}
			} catch (Exception e) {
				appendTextMessages("Nachricht konnte nicht empfangen werden");
				e.printStackTrace();
			}
		}
	}

}
