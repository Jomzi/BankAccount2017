package gmit.ie.sw;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Transaction {
	//1 = login
	//2 = withdrawal
	//3 = lodgement
	int type = 0;
	public Transaction(int type, Date timeStamp, float amount) {
		super();
		this.type = type;
		this.timeStamp = timeStamp;
		this.amount = amount;
	}
	public Transaction(int type, Date timeStamp) {
		this.type = type;
		this.timeStamp = timeStamp;
	}
	public String toString(){
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		if(type == 1){
			return df.format(timeStamp)+"="+type;
		}
			
		return df.format(timeStamp)+"="+type+"="+amount;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	Date timeStamp = null; 
    float amount= 0;
    
}
