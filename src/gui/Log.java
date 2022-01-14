package gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.EventObject;

public class Log extends JFrame implements ActionListener {

	// GUI-Elemente erstellen
	JButton eingabe;
	JPanel newPanel;
	JLabel benutzername, passwordFeld;
	final JTextField user, password;

	Log() {

		// GUI erzeugen
		benutzername = new JLabel();
		benutzername.setText("Benutzername");

		user = new JTextField(15);
		user.addKeyListener(new LoginPressEnterListener());

		passwordFeld = new JLabel();
		passwordFeld.setText("Passwort");

		password = new JPasswordField(15);
		password.addKeyListener(new LoginPressEnterListener());

		// Eingabe-Button erstellen
		eingabe = new JButton("Eingabe");

		// Panel fuer die Elemente erzeugen
		newPanel = new JPanel(new GridLayout(3, 1));
		newPanel.add(benutzername);
		newPanel.add(user);
		newPanel.add(passwordFeld);
		newPanel.add(password);
		newPanel.add(eingabe);

		// BorderLayout fuer das Panel festlegen
		add(newPanel, BorderLayout.CENTER);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((int) (dim.width / 2.5), (int) (dim.height / 2.5));

		eingabe.addActionListener(this);
		setTitle("Login");

	}

	public void actionPerformed(ActionEvent ae) {

		// Prueft Nutzerdaten
		if (isLoginValid()) {

			// Instanz des Clients erzeugen, Benutzername wird an den Client ueberliefert.
			Client c = new Client();
			c.createGUI(user.getText());

			// Schliesst Login-Fenster nach erfolgreichem Einloggen.
			closeLoginForm(ae);

		} else {

			System.out.println("Nutzername oder Passwort ist nicht korrekt");
		}
	}

	// Methode um Nutzerdaten zu pruefen. Gibt 'true' oder 'false' zurueck.
	public boolean isLoginValid() {
		String userValue = user.getText();
		String passValue = password.getText();
		return (userValue.equals("Alice") && passValue.equals("123")
				|| userValue.equals("Bob") && passValue.equals("456"));
	}

	// Methode zum Schliessen den Login-Fensters.
	public void closeLoginForm(EventObject e) {
		JComponent comp = (JComponent) e.getSource();
		Window win = SwingUtilities.getWindowAncestor(comp);
		win.dispose();
	}

	// Login wird durch das Druecken von Enter bestaetigt
	public class LoginPressEnterListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent arg0) {
			if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
				try {
					if (isLoginValid()) {

						Client c = new Client();
						c.createGUI(user.getText());

						closeLoginForm(arg0);
					} else {
						System.out.println("Nutzername oder Passwort ist nicht korrekt");
					}
				} catch (Exception e) {
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
}
