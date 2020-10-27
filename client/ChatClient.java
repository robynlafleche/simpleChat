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
    
    this.isServer = isServer;
    
    if (username == null) {
    	System.out.println("Error: username was not specified");
    	connectionclosed();
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
      sendToServer(message);
      if {
      	(!isConnected() connectionException(new IOException());
      }
    }  
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
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
  protected void connectionException(Exception exception) {
	  clientUI.display("Connection to the server has been terminated. Now closing the program.");
	  connectionClosed();
  }
  
  /**
   * This method closes the connection after an unexpected termination error occurs.
   */
  protected void connectionClosed() {
	  clientUI.display("Connection closed");
	  quit();
  }
}
//End of ChatClient class
