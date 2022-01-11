package grundprogramm;

import javax.swing.*;
import java.net.*;
import java.util.Date;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class Client {

	JFrame clientFrame;
	JPanel clientPanel;
	JTextArea textArea_Messages;
	JTextField textField_ClientMessages;
	JButton button_SendMessage;
	JTextField textField_Username;
	
	Socket client;
	PrintWriter writer;
	BufferedReader reader;
	Date date;
	
	
	
	
	public static void main(String[] args) {
		
		Client c = new Client();
		c.createGUI();
		
		
		
				
	}
	
	public void createGUI() {
		clientFrame = new JFrame("Chatprogramm");
		clientFrame.setSize(800, 600);
		
		clientPanel = new JPanel();
		
		textArea_Messages = new JTextArea();
		textArea_Messages.setEditable(false);
		
		textField_ClientMessages = new JTextField(38);
		textField_ClientMessages.addKeyListener(new SendPressEnterListener());
		
		button_SendMessage = new JButton("Senden");
		button_SendMessage.addActionListener(new SendButtonListener());
		
		textField_Username = new JTextField(10);
		
		JScrollPane scrollPane_Messages = new JScrollPane(textArea_Messages);
		scrollPane_Messages.setPreferredSize(new Dimension(700, 500));
		scrollPane_Messages.setMinimumSize(new Dimension(700, 500));
		scrollPane_Messages.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_Messages.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		if(!connectToServer()) {
			//Evtl. Connect-Label in GUI und Console 
		}
		
		Thread t = new Thread(new MassegesFromServerListener());
		t.start();
		
		clientPanel.add(scrollPane_Messages);
		clientPanel.add(textField_Username);
		clientPanel.add(textField_ClientMessages);
		clientPanel.add(button_SendMessage);
		
		clientFrame.getContentPane().add(BorderLayout.CENTER, clientPanel);
		
		clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		clientFrame.setVisible(true);
		
		
		
		
	}
	
	public boolean connectToServer() {
		try {
			client = new Socket("192.168.178.32", 5555);
			reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			writer = new PrintWriter(client.getOutputStream());
			appendTextMessages("Netzwerkverbindung hergestellt");
			
			return true;
		} catch (Exception e) {
			appendTextMessages ("Netzwerkverbindung nicht möglich");
			e.printStackTrace();
			
			return false;
		}
	}
	
	public void sendMessageToServer() {
		
			
		
		writer.println(date + ": " + textField_Username.getText() + ": " + textField_ClientMessages.getText());
		writer.flush();
		
		textField_ClientMessages.setText("");
		textField_ClientMessages.requestFocus();
		
		
		
	
	}
	
	public void appendTextMessages(String message) {
		textArea_Messages.append(message + "\n");
	}
	
	public class SendPressEnterListener implements KeyListener {
		
		@Override
		public void keyPressed(KeyEvent arg0) {
			if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
				sendMessageToServer();
				
				 
				  
			}
		}
		
		@Override
		public void keyReleased(KeyEvent arg0) {
			
			
		}
		
		@Override
		public void keyTyped(KeyEvent arg0) {}
		
		
		
	}
	
	public class SendButtonListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			sendMessageToServer();
			
			 
			    
		}
	}
	
	public class MassegesFromServerListener implements Runnable {
		
		@Override
		public void run() {
			String message;
			
			try {
				while((message = reader.readLine()) != null) {
					appendTextMessages(message);
					
				}
			} catch (IOException e) {
				appendTextMessages("Nachricht konnte nicht empfangen werden");
				e.printStackTrace();
			}
		}
	}
	
	
	
}

