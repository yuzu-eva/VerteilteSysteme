package gui;

import javax.swing.JOptionPane;

import grundprogramm.Login1;

public class LoginFormDemo {

	
	 public static void main(String arg[])  
	    {  
	        try  
	        {  
	            //Instanz von Log wird erstellt 
	        	Log form = new Log();  
	            form.setSize(300,100);   
	            form.setVisible(true);   
	        }  
	        catch(Exception e)  
	        {     
	              
	            JOptionPane.showMessageDialog(null, e.getMessage());  
	        }  
	    }  
	
}
