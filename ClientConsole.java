// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;

import client.*;
import common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version September 2020
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;
  
  
  
  /**
   * Scanner to read from the console
   */
  Scanner fromConsole; 

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String username, String host, int port) 
  {
    try 
    {
      client= new ChatClient(username, host, port, this);
      
      
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
    
    // Create scanner object to read from console
    fromConsole = new Scanner(System.in); 
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    try
    {

      String message;

      while (true) 
      {
        message = fromConsole.nextLine();
    	if (message.startsWith("#")) {
    		 handleClientFunctions (message);
    		 continue;
    	}
    	else {
    		client.handleMessageFromClientUI(message);
    	}
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }
  
  /**
   * This method implements the different commands specified by the user
   */
  private void handleClientFunctions(String msg) {
	  
	  if (msg.equals("#quit")) {
		  disconnectFromServer();
		  System.out.println("Quitting the program");
		  client.quit();
	  }
	  else if (msg.equals("#logoff")) {
		  disconnectFromServer();
	  }
	  else if (msg.startsWith("#sethost")) {
		  if(client.isConnected()) {
			  System.out.println("You must log out of current session before setting a host, please use #logout.");
		  }
		  if (msg.split(" ").length < 2) {
			  System.out.println("Missing argument, there was no host specified");
			  return;
		  }
		  
		  String nHost = msg.split(" ")[1];
		  client.setHost(nHost);
		  System.out.println("The host was set to " + nHost);
	  }
	  else if (msg.startsWith("#setport")) {
		  if(client.isConnected()) {
			  System.out.println("You must log out of current session before setting a port, please use #logout.");
		  }
		  if (msg.split(" ").length < 2) {
			  System.out.println("Missing argument, there was no host specified");
			  return;
		  }
		  
		  try {
			  int nPort = Integer.parseInt(msg.split(" ")[1]);
			  client.setPort(nPort);
			  System.out.println("The port was set to " + nPort);			  
		  }
		  catch(NumberFormatException e) {
			  System.out.println("The port entered was not valid, please enter an integer representing the port.");
		  }
	  }
	  
	  else if (msg.equals("#login")) {
		  if (client.isConnected()) {
			  System.out.println("You need to be logged off before you can attempt to login again");
			  return;
		  }
		  
		  try {
			  client.openConnection();
		  }catch(IOException e) {
			  System.out.println("Couldn't connect to the server. Please try to connect again.");
		  }
	  }
	  else if (msg.equals("#gethost")) {
		  System.out.println("Current host: " + client.getHost());
	  }
	  else if (msg.equals("#getport")) {
		  System.out.println("Current port: " + client.getPort());
	  }
	  else {
		  System.out.println("Please enter a valid command");
	  }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("> " + message);
  }
  /**
   * This method disconnects the client from the server but does not close the program.
   */
  private void disconnectFromServer() {
	  try {
		  System.out.println("Disconnecting from the server");
		  client.closeConnection();
		  System.out.println("Disconnected from the server successfully");
	  } catch (IOException e) {
		  System.out.println("Unable to connect from the server");
	  }
	  
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
    String host = "";
    String username = null;
    int port = 0;
    
    try
    {
      username = args[0];
      host = args[1]; //get host from command line
      port = Integer.parseInt(args[2]); //get port number from command line
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
    	if(args.length == 1) host = "localhost";
    	port = DEFAULT_PORT;
    }
    catch(NumberFormatException e) //use default if the integer arg could not be parsed
    {
      port = DEFAULT_PORT;
    }
    ClientConsole chat= new ClientConsole(username, host, port);
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
