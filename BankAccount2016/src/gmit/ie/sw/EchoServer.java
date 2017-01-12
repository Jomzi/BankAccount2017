package gmit.ie.sw;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.File;
import java.util.Calendar;
import java.util.concurrent.*;
import java.io.*;
import java.nio.file.*;
import java.nio.channels.*;
import static java.nio.file.StandardOpenOption.*;
import java.nio.*;
public class EchoServer {
  public static void main(String[] args) throws Exception {
	  if(args.length != 1){
		  System.out.println("usage " + "java -cp .:./oop.jar ie.gmit.sw.Server 7777 ");
		  
	  }
	  else{
    ServerSocket m_ServerSocket = new ServerSocket(Integer.parseInt(args[0]),10);
    int id = 0;
    while (true) {
    	System.out.println("Waiting for a connection...");
      Socket clientSocket = m_ServerSocket.accept();
      ClientServiceThread cliThread = new ClientServiceThread(clientSocket, id++);
      cliThread.start();
    }
  }
}
}
class ClientServiceThread extends Thread {
  Socket clientSocket;
  String message;
  int clientID = -1;
  boolean running = true;
  ObjectOutputStream out;
  ObjectInputStream in;
  CustomerFileManager customerFileManager ;
  boolean loggedIn = false;
  Customer customer = null;
  ClientServiceThread(Socket s, int i) {
    clientSocket = s;
    clientID = i;
    
  }

  void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
			System.out.println("client> " + msg);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
  public void run() {
    System.out.println("Accepted Client : ID - " + clientID + " : Address - "
        + clientSocket.getInetAddress().getHostName());
    try 
    {
    	out = new ObjectOutputStream(clientSocket.getOutputStream());
		out.flush();
		in = new ObjectInputStream(clientSocket.getInputStream());
		System.out.println("Accepted Client : ID - " + clientID + " : Address - "
		      + clientSocket.getInetAddress().getHostName());
		customerFileManager = new CustomerFileManager();
			
		
		//sendMessage("Connection successful");
		String operation;
		do{
		
			try {
				int op = in.readInt();
				System.out.println("op: = " + op);
				
				if(loggedIn)
				{
					processCustomerOption(op);
				}
				else{
					processMainOption(op, customerFileManager);
				}
			}
			catch(IOException io){
				System.err.println("Data received in unknown format");
			}
			
			operation = "0";
			Thread.sleep(200);
			
    	}while(operation == ("0"));
      
		System.out.println("Ending Client : ID - " + clientID + " : Address - "
		      + clientSocket.getInetAddress().getHostName());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  public void processMainOption (int op, CustomerFileManager customerFileManager){
	  System.out.println("Processing main option code" + op); 
	  try{
	  String username = (String) in.readObject();
	  String password = (String) in.readObject();
	  
	  System.out.println("Print username & password: " + username +" "+ password);
	  username = username + ".cust";
	  if(customerFileManager.usernameExists(username)){
		  customer = customerFileManager.getCustomer(username);
		  if(customer.getPassword() .equals (password)){
		  System.out.println("Access Granted ");
		  //test login
		  System.out.println("Sending login success");
		  out.writeInt(1);
		  out.writeObject(customer.getName());
		  out.writeObject(customer.getAddress());
		  out.writeObject(customer.getAccountNo());
		  out.flush();
		  loggedIn = true;
		  Calendar cal = Calendar.getInstance();
		  Transaction loginTrans = new Transaction(1,cal.getTime());
		  customer.getList().add(loginTrans);
		  }
		  else{
			  System.out.println("Invalid password! ");  
			  System.out.println("Sending login failure");
			  out.writeInt(0);
			  out.flush();
			  loggedIn = false;
			  }
		  
		  
	  }
	  else{
		  System.out.println("Invalid username! ");
	  }
	 
	  }
  catch(IOException ioException){
		ioException.printStackTrace();
	}
	  catch(ClassNotFoundException ioException){
			ioException.printStackTrace();
		}
  }
  public void processCustomerOption (int op){
	  System.out.println("Processing customer option code" + op);
	  if(op == 2){
		  System.out.println("Updating customer details ");
		  try {
			customer.setName((String)in.readObject());
			customer.setAddress((String)in.readObject());
			customer.setAccountNo((String)in.readObject());
			System.out.println("New name= "+ customer.getName());
			System.out.println("New address= "+ customer.getAddress());
			System.out.println("New accountNo= "+ customer.getAccountNo());
			customerFileManager.createCustomerFile(customer);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	  else if(op == 5){
		  customerFileManager.createCustomerFile(customer);
	  }
  }
}