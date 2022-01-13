package gui;

import javax.swing.*;

import java.awt.*;  
import java.awt.event.*; 
	
public class Log extends JFrame implements ActionListener{

	    //GUI-Elemente erstellen
	    JButton eingabe;  
	    JPanel newPanel;
	    JLabel benutzername, passwordFeld;  
	    final JTextField  user, password;  
	      
	     
	    Log()  
	    {     
	          
	        //GUI erzeugen
	        benutzername = new JLabel();  
	        benutzername.setText("Benutzername");       
	        
	        user = new JTextField(15);    
	       
	        passwordFeld = new JLabel();  
	        passwordFeld.setText("Passwort");      
	          
	        password = new JPasswordField(15);    
	          
	        //Eingabe-Button erstellen
	        eingabe = new JButton("Eingabe"); 
	          
	        //Panel f�r die Elemente erzeugen  
	        newPanel = new JPanel(new GridLayout(3, 1));
	        newPanel.add(benutzername);    
	        newPanel.add(user);   
	        newPanel.add(passwordFeld);    
	        newPanel.add(password);    
	        newPanel.add(eingabe);           
	          
	        //BorderLayout f�r das Panel festlegen  
	        add(newPanel, BorderLayout.CENTER);  
	        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	        setLocation((int)(dim.width/2.5), (int)(dim.height/2.5));
	           
	        eingabe.addActionListener(this);      
	        setTitle("Login");    
	        
	    }  
	      
	      
	    public void actionPerformed(ActionEvent ae)      
	    {  
	        String userValue = user.getText();        
	        String passValue = password.getText();         
	          
	        //Nutzerdaten pr�fen und zum Chat weiterleiten
	        if (userValue.equals("Alice") && passValue.equals("123")|| userValue.equals("Bob") && passValue.equals("456"))   {   
	              
	            //Instanz des Clients erzeugen, Benutzername wird an den Client ueberliefert.
	            Client c = new Client(); 
	            c.createGUI(userValue);
	            
	            // Schliesst Login-Fenster nach erfolgreichem Einloggen.
	            JComponent comp = (JComponent) ae.getSource();
	            Window win = SwingUtilities.getWindowAncestor(comp);
	            win.dispose();
	        	 
	        }  
	        else{  
	              
	            System.out.println("Nutzername oder Passwort ist nicht korrekt");  
	        }  
	    }  
	}  
	

