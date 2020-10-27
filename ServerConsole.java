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
		  public ServerConsole(String host, int port) 
		  {
			echoServer = new EchoServer(port);
		    try 
		    {
		      echoServer.listen();      
		    } 
		    catch(IOException e) 
		    {
		      System.out.println("Error: Could not start server");
		    }
		    
		    try {
		    	client = new ChatClient(null, host, port, this);
		    }
		    catch(IOException e) {
		    	System.out.println("Error: could not start server");
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

		        if (message.startsWith("#")) {
		        	handleClientFunctions(message);
		        	continue;
		        }
		        if (!client.isConnected()) {
		        	System.out.println("Failed to send message due to the server not nbeing connected");
		        }
		        
		        client.handleMessageFromClientUI("SERVER MSG> " + message);
		      }
		    } 
		    catch (Exception ex) 
		    {
		      System.out.println
		        ("Unexpected error while reading from console!");
		    }
		  }

		  private void handleClientFunctions(String message) {
			if (message.equals("#quit")) {
				try {
					echoServer.close();
				}
				catch(IOException e) {
					System.out.println("Error: could not close server");
				}
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
				
				System.out.println("Server has been closed");
			}
			else if (message.equals("#setPort")) {
				if (echoServer.isListening()) {
					System.out.println("Unable to set a new port while the server is listening");
					return;
				}
				if (message.split(" ").length < 2) {
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


		/**
		   * This method displays all messages sent to the server on the console.
		   *
		   * @param message The string to be displayed.
		   */
		  public void display(String message) 
		  {
		    System.out.println("> " + message);
		  }

		  
		  //Class methods ***************************************************
		  
		  /**
		   * This method is responsible for the creation of the server-side UI.
		   * @param args port argument to specify the port number which both the
		   * server and server console will listen.
		   */
		  public static void main(String[] args) 
		  {
		    String host = "localhost";
		    int port = 0;
		    
		    try
		    {
		      port = Integer.parseInt(args[0]); //get port number from command line
		    }
		    catch(Exception e) //use default if the integer arg could not be parsed
		    {
		      port = DEFAULT_PORT;
		    }
		    ServerConsole chat = new ServerConsole(host, port);
		    chat.accept();  //Wait for server-side messaging
		  }
		}
