package gui;

import javax.swing.*;

import java.awt.*;  
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.Exception; 
	
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
	          
	        //Panel für die Elemente erzeugen  
	        newPanel = new JPanel(new GridLayout(3, 1));  
	        newPanel.add(benutzername);    
	        newPanel.add(user);   
	        newPanel.add(passwordFeld);    
	        newPanel.add(password);    
	        newPanel.add(eingabe);           
	          
	        //BorderLayout für das Panel festlegen  
	        add(newPanel, BorderLayout.CENTER);  
	          
	           
	        eingabe.addActionListener(this);      
	        setTitle("Login");          
	    }  
	      
	      
	    public void actionPerformed(ActionEvent ae)      
	    {  
	        String userValue = user.getText();        
	        String passValue = password.getText();         
	          
	        //Nutzerdaten prüfen und zum Chat weiterleiten
	        if (userValue.equals("Verteilte") && passValue.equals("Systeme")|| userValue.equals("Port") && passValue.equals("Folio"))   {   
	              
	            //Instanz des Clients erzeugen 
	            Client c = new Client(); 
	            c.createGUI();
	            
	            PrintWriter schreibt;
	        	BufferedReader liest;
	        	
	        	 
	        }  
	        else{  
	              
	            System.out.println("Nutzername oder Passwort ist nicht korrekt");  
	        }  
	    }  
	}  
	

