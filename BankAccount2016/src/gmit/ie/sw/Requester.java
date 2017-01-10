package gmit.ie.sw;
import java.io.*;
import java.net.*;
import java.util.Scanner;
public class Requester{
	Socket requestSocket;
	ObjectOutputStream out;
 	ObjectInputStream in;
 	String message="";
 	String ipaddress;
 	int port;
 	String downloadDir;
 	boolean loggedIn = false;
    Config config = null;
	Requester(){}
	void run()
	{
		config = new Config ("config.xml");
		config.parse();
		connectServer();
		int selection = 0;
		boolean exiting = false;
		
		
	
		
		
	
		while (!exiting){
			
			if(loggedIn)
			{
				selection = showCustomerMenu();
			}
			else{
				selection = showMenu();
			}
			
			if(loggedIn)
			{
				processCustomerOption(selection);
			}
			else{
				processMainOption(selection);
			}
			
			
			
		}
			
	}
		
		public void processMainOption(int selection){
			
			if(selection == 1){
				//Do login
				System.out.println("Preforming login... ");
				System.out.println("Username");
				
                Scanner stdin = new Scanner(System.in);
				String username = stdin.nextLine();
				
				
				System.out.println("Password");
				
				String password = stdin.nextLine();
				
				try{
					
				out.writeInt(selection);
				out.writeObject(username);
				out.writeObject(password);
				out.flush();
				int loginSuccess = in.readInt();
				System.out.println("Received login status");
				if(loginSuccess > 0){
				
				loggedIn = true;
				}
				else{
					loggedIn = false;
				}
			}
				catch(IOException ioException){
					ioException.printStackTrace();
				}
			}
			
			else if(selection == 2){
				return;
			}
				
			
		}
		
        public void processCustomerOption(int selection){
			
			if(selection == 1){
				//view transaction
				System.out.println("View transactions... ");
			}
			else if(selection == 2){
				System.out.println("Change details? ");
			}
			else if(selection == 3){
				System.out.println("Make withdrawal... ");
			}	
			else if(selection == 4){
				System.out.println("Make lodgement... ");
			}	
			else if(selection == 5){
				System.out.println("Quit");
			}	
			else{
				System.out.println("Invalid option!");
			}
			
		}
	void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
			System.out.println("client>" + msg);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	public static void main(String args[])
	{
		Requester client = new Requester();
		client.run();
	}
	
	public int showMenu(){
		
		
		
			System.out.println("1 Login");
			System.out.println("2 Quit");
			
			Scanner stdin = new Scanner(System.in);
			
			return stdin.nextInt();
		
				
		
		
	}
	
	public int showCustomerMenu(){
		
		
		
		System.out.println("1 View Transactions");
		System.out.println("2 Change Details");
		System.out.println("3 Make withdrawal");
		System.out.println("4 Make lodgement");
		System.out.println("5 Quit");
		
		Scanner stdin = new Scanner(System.in);
		
		return stdin.nextInt();
	
			
	
	
}

	
	public void connectServer(){
		
		Scanner stdin = new Scanner(System.in);
		try{
			//1. creating a socket to connect to the server
			//System.out.println("Please Enter your IP Address");
			//ipaddress = stdin.next();
			ipaddress = config.getServerHost();
			port = Integer.parseInt(config.getServerPort());
			
			requestSocket = new Socket(ipaddress, port);
			System.out.println("Connected to "+ipaddress+" in port" + port);
			//2. get Input and Output streams
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			System.out.println("Hello");
			
			//3: Communicating with the server
			
			
		}
		catch(UnknownHostException unknownHost){
			System.err.println("You are trying to connect to an unknown host!");
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	private void closeConnection(){
		try{
			in.close();
			out.close();
			requestSocket.close();
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	
}