import javax.swing.*;

class CheckingAccount extends BankAccount
{
    // declaring a private field for overdraft limit
	private double overdraftLimit;
	
    // constructor to initialize the account with account number, username, email, phone number, balance, and overdraft limit
	public CheckingAccount(String accountNum, String userName, String email, String phoneNum, double balance, double overdraftLimit)
	{
		super(accountNum, userName, email, phoneNum, balance);  // calling the superclass constructor to initialize common account details
		this.overdraftLimit = overdraftLimit;  // initializing overdraft limit for this specific account type
	}
	
    // overriding withdraw method to handle withdrawals with overdraft
	@Override
	public boolean withdraw(double amount)
	{
	    // checking if withdrawal is positive and within the balance + overdraft limit
	    if (amount > 0 && amount <= balance + overdraftLimit)
	    {
	        balance -= amount;  // deducting the withdrawal amount from the balance
	        JOptionPane.showMessageDialog(null, "Your withdrawal was successful! Your new balance is: $" + balance);
	        return true;  // success: withdrawal was successful
	    }
	    else
	    {
	        // if withdrawal exceeds balance and overdraft limit
	        JOptionPane.showMessageDialog(null, "Insufficient funds!", "Error", JOptionPane.ERROR_MESSAGE);
	        return false;  // failure: withdrawal unsuccessful due to insufficient funds
	    }
	}

    // overriding displayAccountInfo to show the overdraft limit for a checking account
	@Override
	public void displayAccountInfo()
	{
		super.displayAccountInfo();  // displaying common account information
		JOptionPane.showMessageDialog(null, "Overdraft Limit: $" + overdraftLimit);  // displaying the overdraft limit for the checking account
	}
}
