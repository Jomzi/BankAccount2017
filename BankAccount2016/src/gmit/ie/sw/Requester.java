package gmit.ie.sw;
import java.io.*;
import java.net.*;
import java.util.Scanner;
public class Requester{
	Socket requestSocket;
	ObjectOutputStream out;
 	ObjectInputStream in;
 	String message="";
 	Customer customer;
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
					customer = new Customer (username);
					customer.setPassword(password);
					customer.setName((String)in.readObject());
					customer.setAddress((String)in.readObject());
					customer.setAccountNo((String)in.readObject());
					System.out.println("Name = "+ customer.getName());
					System.out.println("Name = "+ customer.getAddress());
					System.out.println("Name = "+ customer.getAccountNo());
				loggedIn = true;
				}
				else{
					System.out.println("Invalid password!");
					loggedIn = false;
				}
			}
				catch(IOException | ClassNotFoundException ioException){
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
				//update customer details
				System.out.println("Change details? ");
				System.out.println("Current Name is = "+ customer.getName()+",Enter new name(Press return to keep existing name)");
				 Scanner stdin = new Scanner(System.in);
					String newName = stdin.nextLine();
					if(!newName.equals("")){
						customer.setName(newName);
						System.out.println("Set name = "+ customer.getName());
					}
						else{
							System.out.println("Keep existing name = ");
						
					}
				System.out.println("Current Address is = "+ customer.getAddress()+",Enter new name(Press return to keep existing address)");		
				String newAddress = stdin.nextLine();
				if(!newAddress.equals("")){
					customer.setAddress(newAddress);
					System.out.println("Set address = "+ customer.getAddress());
				}
					else{
						System.out.println("Keep existing Address = ");
					
				}
				System.out.println("Current AccountNo is = "+ customer.getAccountNo()+",Enter new AccountNo(Press return to keep existing AccountNo)");		
				String newAccountNo = stdin.nextLine();
				if(!newAccountNo.equals("")){
					customer.setAccountNo(newAccountNo);
					System.out.println("Set AccountNo = "+ customer.getAccountNo());
				}
					else{
						System.out.println("Keep existing AccountNo = ");
					
				}
				System.out.println("Sending customer account details to server");
				try {
					out.writeInt(2);
					out.writeObject(customer.getName());
					out.writeObject(customer.getAddress());
					out.writeObject(customer.getAccountNo());
					out.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(selection == 3){
				System.out.println("Make withdrawal... ");
			}	
			else if(selection == 4){
				System.out.println("Make lodgement... ");
			}	
			else if(selection == 5){
				System.out.println("Quit");
				try{
					out.writeInt(5);
					out.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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