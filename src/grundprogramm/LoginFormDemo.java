package grundprogramm;

import javax.swing.JOptionPane;

public class LoginFormDemo {

	
	 public static void main(String arg[])  
	    {  
	        try  
	        {  
	            //create instance of the CreateLoginForm  
	        	Login1 form = new Login1();  
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
