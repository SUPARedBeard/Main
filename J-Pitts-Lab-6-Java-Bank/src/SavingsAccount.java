import javax.swing.*;

class SavingsAccount extends BankAccount
{
    // declaring a private field for interest rate
	private double interestRate;
	
    // constructor to initialize the account with account number, username, email, phone number, balance, and interest rate
	public SavingsAccount(String accountNum, String userName, String email, String phoneNum, double balance, double interestRate)
	{
		super(accountNum, userName, email, phoneNum, balance);  // calling the superclass constructor to initialize common account details
		this.interestRate = interestRate;  // initializing interest rate for this specific account type
	}
	
    // overriding displayAccountInfo to show the additional interest rate for a savings account
	@Override
	public void displayAccountInfo()
	{
		super.displayAccountInfo();  // displaying common account information
		JOptionPane.showMessageDialog(null, "Your interest rate is: " + interestRate + "%");  // displaying the interest rate for the savings account
	}
}
