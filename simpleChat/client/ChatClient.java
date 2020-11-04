// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  
  /**
   * username string variable allowing to store the name specified by the user
   */
  String username;
  
  
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param username The username is to be reffered to
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String username, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    
    
    if (username == null) {
    	System.out.println("ERROR - No login ID specified.  Connection aborted.");
    	quit();
    }
    
    this.username = username;
    this.clientUI = clientUI;
    openConnection();
    sendToServer("#login " + username);
    }
    
  

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
      
      if (message.startsWith("#")) {
 		 handleClientFunctions (message);
 		 return;
      }
      sendToServer(message);
    }  
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  /**
   * This method implements the different commands specified by the user
   */
  private void handleClientFunctions(String msg) {
	  
	  
	  if (msg.equals("#quit")) {
		  quit();
	  }
	  else if (msg.equals("#logoff")) {
		  try {
			closeConnection();
		} catch (IOException e) {
			System.out.println("Unable to disconnect from server");
		}
	  }
	  else if (msg.startsWith("#sethost")) {
		  if (isConnected()) {
			  System.out.println("You must be disconnected before setting a new host");
		  }
		  else if (msg.split(" ").length != 2) {
			  System.out.println("Missing argument, there was no host specified");
			  return;
		  }
		  
		  setHost(msg.split(" ")[1]);
		  System.out.println("Host set to: " + msg.split(" ")[1]);
	  }
	  else if (msg.startsWith("#setport")) {
		  if (isConnected()) {
			  System.out.println("You must be disconnected before setting a new port");
		  }
		  else if (msg.split(" ").length != 2) {
			  System.out.println("Missing argument, there was no host specified");
			  return;
		  }
		  
		  try {
			  int nPort = Integer.parseInt(msg.split(" ")[1]);
			  setPort(nPort);
			  System.out.println("Port set to: " + nPort);			  
		  }
		  catch(NumberFormatException e) {
			  System.out.println("The port entered was not valid, please enter an integer representing the port.");
		  }
	  }
	  
	  else if (msg.startsWith("#login")) {
		  if (isConnected()) {
			  System.out.println("You need to be logged off before you can attempt to login again");
			  return;
		  }
		  
		  try {
			  setLoginID(msg.split(" ")[1]);
			  openConnection();
			  sendToServer("#loginID " + getLoginID());
		  }catch(IOException e) {
			  System.out.println("Cannot open connection. Awaiting command.");
		  }
	  }
	  else if (msg.equals("#gethost")) {
		  System.out.println("Current host: " + getHost());
	  }
	  else if (msg.equals("#getport")) {
		  System.out.println("Current port: " + getPort());
	  }
	  else {
		  System.out.println("Please enter a valid command");
	  }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }

  /**
   * Method called each time an exception is thrown by the client's thread 
   * indicating that the client is waiting for messages from the server. This
   * method may be overridden by subclasses.
   * 
   * @param exception the exception raised 
   */
  @Override
  protected void connectionException(Exception exception) {
	  clientUI.display("Abnormal termination of connection.");
	  quit();
  }
  
	/**
	 * Hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overriden by subclasses to
	 * perform special processing such as cleaning up and terminating, or
	 * attempting to reconnect.
	 */
    @Override
	protected void connectionClosed() {
		clientUI.display("Connection Closed");
	}
    
	//Assigns the value of input username to the username
	public void setLoginID(String username) {
		this.username = username;
	}
	
	//returns the value of the username
	public String getLoginID() {
		return username;
	}
}
//End of ChatClient class
