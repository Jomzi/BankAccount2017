package gmit.ie.sw;
import java.util.ArrayList;
import java.util.*;

public class Customer {
	private String name;
	private String address;
	private String accountNo;
	private String username;
	public Customer(String username) {
		super();
		this.username = username;
	}
	private String password;
	private List<Transaction> list = new ArrayList<Transaction>();
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<Transaction> getList() {
		return list;
	}
	public void setList(List<Transaction> list) {
		this.list = list;
	}
	
}
