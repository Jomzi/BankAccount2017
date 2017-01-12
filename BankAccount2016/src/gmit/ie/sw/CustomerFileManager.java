package gmit.ie.sw;
import java.util.ArrayList;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class CustomerFileManager {
	
	
private static String dataDirectory = "C:\\Users\\Jomziii\\workspace\\BankAccount2016\\bin\\custData"; 
	private List <String> getCustomerUsernames(){
		
	List <String> username = new ArrayList<String>();
	
		File directory = new File(dataDirectory);
		if(directory.exists()){
			
		   	File[]fileList = directory.listFiles();
		   	int fileCount = 0;
		   	for(int i =0; i< fileList.length; i++){
		   		String fileName = fileList [i].getName();
		   		username.add(fileName);
		   		
		   		
		   	}
	}
		return(username);
}
	public boolean usernameExists (String username){
		List <String> listofUsernames= getCustomerUsernames();
		if(listofUsernames.contains(username)){
			return true;
			
		}
		else{
			return false;
		}
}
	private void writeTransaction(Transaction transaction, BufferedWriter bw){
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		System.out.println("Transaction details = "+ transaction.toString());
		try {
			bw.write(transaction.toString()+"\r\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createCustomerFile(Customer customer){
		BufferedWriter bw = null;
		FileWriter fw = null;
		try{
			String filePath = dataDirectory + "\\" + customer.getUsername();
			File file = new File (filePath);
			
			
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write("Password="+ customer.getPassword()+"\r\n");
			bw.write("Name="+ customer.getName()+"\r\n");
			bw.write("Address="+ customer.getAddress()+"\r\n");
			bw.write("AccountNo="+ customer.getAccountNo()+"\r\n");
			for(Transaction transaction: customer.getList()){
				writeTransaction(transaction, bw);
			}
		}
	    catch (Exception e) {
		e.printStackTrace();
	    }
		finally{
			try{
				if(bw != null)
					bw.close();
				
				if(fw != null)
					fw.close();
			}
			catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	}

	public Customer getCustomer (String username){
		Customer cust = new Customer(username);
		try {
			String[] tokens = null;
			String filePath = dataDirectory + "\\" + username;
			File file = new File (filePath);
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			tokens = parseLine(br);
			cust.setPassword(tokens[1]);
			tokens = parseLine(br);
			cust.setName(tokens[1]);
			tokens = parseLine(br);
			cust.setAddress(tokens[1]);
			tokens = parseLine(br);
			cust.setAccountNo(tokens[1]);
			Transaction trans = null;
			do{
				tokens = parseLine(br);
				if(tokens != null && tokens.length >= 2){
					DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
					Date date = df.parse(tokens[0]);
					if(tokens[1].equals("1")){
						
					trans = new Transaction(1,date);
						
					}
					else if(tokens[1].equals("2")){
						float amount = Float.parseFloat(tokens[2]);
						trans = new Transaction(2,date, amount);
					}
					else if(tokens[1].equals("3")){
						float amount = Float.parseFloat(tokens[2]);
						trans = new Transaction(3,date, amount);
					}
					System.out.println("Transaction details = "+ trans.toString());
				}
				
				}while(tokens != null);
				
			
		
			
			
				
			
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (cust);

	}
	private String[] parseLine (BufferedReader br){
		    String[] tokens = null;
		    String line = "";
			try {
				while(line != null && line.equals("")){
					line = br.readLine();
				}
				
				System.out.println("Read line: "+ line);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    		if(line != null){
		    			tokens = line.split("=");
		    		}
			return tokens;
	}
}

	
