//This class implements the modifications necessary for Exercise 2 
//to allow for a global messaging feature for the server-side user

import java.io.IOException;
import java.util.Scanner;
import client.ChatClient;
import common.*;

/**
 * This class implements the ChatIF user-interface for the 
 * server-side end user to be able to communicate with all 
 * the other clients. Similar to ClientConsole
 * 
 * @author Robyn Lafleche
 */

public class ServerConsole implements ChatIF{
		  //Class variables *************************************************
		  
		  /**
		   * The default port to listen on.
		   */
		  final public static int DEFAULT_PORT = 5555;
		  
		  //Instance variables **********************************************
		  
		  /**
		   * The instance of the client that created this ConsoleChat.
		   */
		  ChatClient client;
		  
		  
		  
		  /**
		   * Scanner to read from the server-side user input
		   */
		  Scanner serverIO; 
		  
		  EchoServer echoServer;

		  
		  //Constructors ****************************************************

		  /**
		   * Constructs an instance of the ServerConsole UI.
		   *
		   * @param host The host address.
		   * @param port The port to listen on.
		   */
		  public ServerConsole(EchoServer sv) 
		  {
			echoServer = sv;
		    try 
		    {
		      echoServer.listen();      
		    } 
		    catch(IOException e) 
		    {
		      System.out.println("Error: Could not start server");
		    }
		    
		    serverIO = new Scanner(System.in); 
		  }

		  
		  //Instance methods ************************************************

		/**
		   * This method waits for input from the user.  Once it is 
		   * received, it sends a concatonated SERVER MSG> string to notify 
		   * the users the message originates from the server.
		   */
		  public void accept() 
		  {
		    try
		    {

		      String message;

		      while (true) 
		      {
		        message = serverIO.nextLine();

		        if (message.charAt(0) == '#') {
		    		 handleClientFunctions (message);
		    	}
		        
				echoServer.sendToAllClients("SERVER MSG> " + message);
				display("SERVER MSG> " + message);
		      }
		    } 
		    catch (Exception ex) 
		    {
		      System.out.println
		        ("Unexpected error while reading from console! " + ex);
		    }
		  }

		  private void handleClientFunctions(String message) {
			if (message.equals("#quit")) {
				System.exit(0);
			}
			else if (message.equals("#stop")) {
				if (!echoServer.isListening()) {
					System.out.println("Server has alrready been stopped");
					return;
				}
				echoServer.stopListening();
			}
			else if (message.equals("#close")) {
				try {
					echoServer.close();
				}catch(IOException e) {}
			}
			else if (message.startsWith("#setport")) {
				if (echoServer.isListening()) {
					System.out.println("Unable to set a new port while the server is listening");
					return;
				}
				if (message.split(" ").length != 2) {
					System.out.println("Missing argument, #setport <port> required");
					return;
				}
				
				try {
					int p = Integer.parseInt(message.split(" ")[1]);
					echoServer.setPort(p);
					System.out.println("New port has been set");
				} catch(NumberFormatException e) {
					System.out.println("Error: <port> entered must be an Integer");
				}
			}
			else if (message.equals("#start")) {
				if(echoServer.isListening()) {
					System.out.println("Server is already listening for connections");
					return;
				}
				
				try {
					echoServer.listen();
				}catch(IOException e) {
					System.out.println("Unable to start the server");
				}
			}
			else if (message.equals("#getport")) {
				System.out.println("The server is running on port " + echoServer.getPort());
			}
			else {
				System.out.println("Command is invalid, please enter a valid command");
			}
		}


		@Override
		public void display(String message) {
			// TODO Auto-generated method stub
			
		}
}
