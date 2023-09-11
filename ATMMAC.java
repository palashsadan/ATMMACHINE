package ATMTASK1;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 * this class is used for storing user details.
 */
class User{
	private String userId;
	private String pin;
	private double balance;
	
public User(String userId, String pin){
	this.userId = userId;
	this.pin = pin;
	this.balance = 0.0;
}

public User(String userId){
	this.userId = userId;
}


public String getUserId(){
	return userId;
}

public String getPin(){
	return pin;
}

public double getBalance(){
	return balance;
}
}

/**
 * this class is used to store any transaction ongoing, irrespective of user.
 */
class Transaction{
	private String type;
	private double amount;
	private String date;

public Transaction(String type,double amount, String date)  {
	this.type = type;
	this.amount = amount;
	this.date = date;
}

@Override
public String toString() {
	return date + " - " + type + ": $" + amount;
}
}

/**
 * this class contains business logic of AtmMachine.
 */
class Atmop{
	private User user;
	private double balance;
	private TransactionHistory transactionHistory;

public Atmop(User user, double initialBalance) {
	this.user = user;
	this.balance = initialBalance;
	this.transactionHistory = new TransactionHistory();
}

public Atmop(User user) {
	this.user = user;
	this.transactionHistory = new TransactionHistory();
}

public boolean authenticate(String userId, String pin){
	return user.getUserId().equals(userId) && user.getPin().equals(pin);
}

public void deposit(double amount) {
	if (amount > 0){
		balance += amount;
		System.out.println("Amount Deposited Successfully \n =====Current Balance is :$ " + balance+"=====");
		transactionHistory.addTransaction("Deposited : ",+ amount);
	} else{
		System.out.println("Invalid deposit amount");
	}
}

public void withdraw(double amount) {
	if (amount > 0 && amount <= balance){
		balance -= amount;
		System.out.println("Amount Withdrawn Successfully \n =====Current Balance is :$ " + balance+"=====");
		transactionHistory.addTransaction("Withdrawal :" ,+ amount);
	} else {
		System.out.println("Insufficient funds or invalid withdrawal amount");
	}
}

public void transfer (Atmop recipient , double amount){
	if (amount > 0 && amount <= balance) {
		balance -= amount;
		System.out.println("Amount Transfered Successfully \n =====Current Balance is :$ " + balance+"=====");
		transactionHistory.addTransaction("Transfer to " + recipient.user.getUserId(), amount);
	} else {
		System.out.println(" Insufficient funds or invalid withdrawal amount ");
	}
} 

public void printTranscationHistory() {
	transactionHistory.printTranscations();
}

public void quit(){
	System.out.println("==========Thank you for Banking with us.========== \n =============== Have a Good Day! ===============");
	System.exit(0);
}

	
}


/**
 * this class is used to maintain history of multiple transactions.
 */
class TransactionHistory {
	private ArrayList<Transaction> transactions;

public TransactionHistory() {
	this.transactions = new ArrayList<>();
}

public void addTransaction(String type, double amount) {
	SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String date = dateformat.format(new Date());
	transactions.add(new Transaction(type, amount, date));
}

public void printTranscations() {
	System.out.println("Transaction History : ");
	for (Transaction transaction : transactions) {
		System.out.println(transaction);
	}
}
}

/**
 * this is the main class (controller class).
 */
public class ATMMAC {
	public static void main(String[] args) {
		// User and password is hardcoded here for authentication.
		// in future this can be replaced by db call.
		User user = new User ("12345", "6789");
		Atmop atm = new Atmop(user, 1000.0);
		
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("Enter Account Number : ");
			String userId = scanner.nextLine();
			System.out.println("Enter Pin Number : ");
			String pin = scanner.nextLine();

			if (atm.authenticate(userId, pin)){
				System.out.println("Welcome, " + user.getUserId()+ "!");
				while (true){
					System.out.println("\n \n ATM Menu: \n");
					System.out.println("1: Deposit Money");
					System.out.println("2: Withdraw Money");
					System.out.println("3: Transfer");
					System.out.println("4: Transaction Histroy");
					System.out.println("5: Exit");

					System.out.println("\n Enter your Choice: ");
					int choice = scanner.nextInt();

					switch (choice) {
						case 1:
						System.out.println("Enter the Deposit Amount");
						double depositAmount = scanner.nextDouble();
						atm.deposit(depositAmount);
						break;
					case 2: 
						System.out.println("Enter the Withdraw Amount");
						double withdrawAmount = scanner.nextDouble();
						atm.withdraw(withdrawAmount);
						break;
					case 3:
						System.out.println("Enter the recipient Account Number: ");
						String recipientId = scanner.next();
						
						System.out.println("Enter the Transfer Amount");
						double transferAmount = scanner.nextDouble();
						User user2 = new User (recipientId);
						Atmop atm2 = new Atmop(user2);
						atm.transfer(atm2, transferAmount);
						break;
					case 4:
						atm.printTranscationHistory();
						break;
					case 5:
						atm.quit();
					default :
						System.out.println("\n Invalid choice. Please try again. ");
					}

				}
			} else {
				System.out.println("Authentication failed. please try again. ");
			}
		} 
	}

}
