// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	
	if(msg.toString().startsWith("#login") && msg.toString().split(" ").length > 1) {
		  client.setInfo("loginid", ((String) msg).split(" ")[1]);
		  System.out.println("Message received: " + msg + " from null");
		  System.out.println(client.getInfo("loginid") +" has logged on.");
		  this.sendToAllClients(client.getInfo("loginid") +" has logged on.");
	
	}
	else {
		System.out.println("Message received: " + msg + " from " + client.getInfo("loginid"));
		this.sendToAllClients(client.getInfo("loginid") +"> " + msg);
	}
	
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
	this.sendToAllClients("WARNING - The server has stopped listening for connection");
	this.sendToAllClients ("SERVER SHUTTING DOWN! DISCONNECTING!");
	System.out.println
      ("Server has stopped listening for connections.");
  }

  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = DEFAULT_PORT; //Port to listen on
    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
      ServerConsole sc = new ServerConsole(sv);
      sc.accept();
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
  /**
   * Hook method called each time a new client connection is
   * accepted. The default implementation does nothing.
   * @param client the connection connected to the client.
   */
  protected void clientConnected(ConnectionToClient client) {
	  System.out.println("A new client is attempting to connect to the server");
	  
  }

  /**
   * Hook method called each time a client disconnects.
   * The default implementation does nothing. The method
   * may be overridden by subclasses but should remains synchronized.
   *
   * @param client the connection with the client.
   */
  synchronized protected void clientDisconnected(ConnectionToClient client) {
	  this.sendToAllClients(client.getInfo("loginid") + " has disconnected from the server.");
	  System.out.println(client.getInfo("loginid") + " has disconnected.");
  }
  
  synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
	  try {
		client.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
}
  
}
//End of EchoServer class
