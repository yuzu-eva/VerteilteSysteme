package gui;

import javax.swing.*;

import java.awt.*;  
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.Exception; 
	
public class Log extends JFrame implements ActionListener{

	     
	    JButton submit;  
	    JPanel newPanel;  
	    JLabel username, passLabel;  
	    final JTextField  user, password;  
	      
	     
	    Log()  
	    {     
	          
	           
	        username = new JLabel();  
	        username.setText("Username");      //set label value for textField1  
	          
	        //create text field to get username from the user  
	        user = new JTextField(15);    //set length of the text  
	  
	        //create label for password  
	        passLabel = new JLabel();  
	        passLabel.setText("Password");      //set label value for textField2  
	          
	        //create text field to get password from the user  
	        password = new JPasswordField(15);    //set length for the password  
	          
	        //create submit button  
	        submit = new JButton("SUBMIT"); //set label to button  
	          
	        //create panel to put form elements  
	        newPanel = new JPanel(new GridLayout(3, 1));  
	        newPanel.add(username);    //set username label to panel  
	        newPanel.add(user);   //set text field to panel  
	        newPanel.add(passLabel);    //set password label to panel  
	        newPanel.add(password);   //set text field to panel  
	        newPanel.add(submit);           //set button to panel  
	          
	        //set border to panel   
	        add(newPanel, BorderLayout.CENTER);  
	          
	        //perform action on button click   
	        submit.addActionListener(this);     //add action listener to button  
	        setTitle("LOGIN");         //set title to the login form  
	    }  
	      
	    //define abstract method actionPerformed() which will be called on button click   
	    public void actionPerformed(ActionEvent ae)     //pass action listener as a parameter  
	    {  
	        String userValue = user.getText();        //get user entered username from the textField1  
	        String passValue = password.getText();        //get user entered pasword from the textField2  
	          
	        //check whether the credentials are authentic or not  
	        if (userValue.equals("Verteilte") && passValue.equals("Systeme")|| userValue.equals("Port") && passValue.equals("Folio"))   {  //if authentic, navigate user to a new page  
	              
	            //create instance of the NewPage  
	            Client c = new Client(); 
	            c.createGUI();
	            
	            PrintWriter schreibt;
	        	BufferedReader liest;
	        	
	        	
	        	//liest = new BufferedReader(new InputStreamReader(user.getInputStream()));
	            
	           
	            //String name2 = user.get;
	              
	            //make page visible to the user  
	            //c.setVisible(true);  
	              
	            //create a welcome label and set it to the new page  
	            //JLabel wel_label = new JLabel("Willkommen: "+userValue);  
	           // page.getContentPane().add(wel_label);  
	        }  
	        else{  
	            //show error message  
	            System.out.println("Nutzername oder Passwort ist nicht korrekt");  
	        }  
	    }  
	}  
	

