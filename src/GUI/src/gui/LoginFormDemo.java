package gui;

import javax.swing.JOptionPane;

import grundprogramm.Login1;

public class LoginFormDemo {

	
	 public static void main(String arg[])  
	    {  
	        try  
	        {  
	            //create instance of the CreateLoginForm  
	        	Log form = new Log();  
	            form.setSize(300,100);  //set size of the frame  
	            form.setVisible(true);  //make form visible to the user  
	        }  
	        catch(Exception e)  
	        {     
	            //handle exception   
	            JOptionPane.showMessageDialog(null, e.getMessage());  
	        }  
	    }  
	
}
