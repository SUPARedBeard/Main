//Josh Pitts
//CPT 236
//Lab 6


import javax.swing.*;

abstract class BankAccount 
{
    // declaring protected fields for account information
    protected String accountNum;
    protected String userName;
    protected String email;
    protected String phoneNum;
    protected double balance;
    
    // constructor to initialize account information
    public BankAccount(String accountNum, String userName, String email, String phoneNum, double balance)
    {
        this.accountNum = accountNum;  // initializing account number
        this.userName = userName;      // initializing username
        this.email = email;            // initializing email
        this.phoneNum = phoneNum;      // initializing phone number
        this.balance = balance;        // initializing balance
    }
    
    // method to deposit an amount to the account
    public void deposit(double amount)
    {
        if (amount > 0) // checking if the deposit amount is positive
        {
            balance += amount; // adding the deposit amount to balance
            JOptionPane.showMessageDialog(null, "Your deposit was successful! Your new balance is: $" + balance);
        }
        else
        {
            JOptionPane.showMessageDialog(null,  "Invalid deposit attempt!", "Error", JOptionPane.ERROR_MESSAGE); // error if amount is non-positive
        }
    }
    
    // method to withdraw an amount from the account
    public boolean withdraw(double amount)
    {
        if (amount <= 0) { // checking if withdrawal amount is positive
            JOptionPane.showMessageDialog(null, "Withdrawal amount must be positive!", "Error", JOptionPane.ERROR_MESSAGE);
            return false; // return false if amount is not positive
        } else if (amount > balance) { // checking if there are enough funds
            JOptionPane.showMessageDialog(null, "Insufficient funds!", "Error", JOptionPane.ERROR_MESSAGE);
            return false; // return false if not enough funds
        } else {
            balance -= amount; // subtracting withdrawal amount from balance
            return true; // return true if withdrawal is successful
        }
    }
    
    // method to get the current balance of the account
    public double getBalance() 
    {
        return balance; // returning the current balance
    }
     
    // method to display account information
    public void displayAccountInfo()
    {
        // displaying account information in a message box
        JOptionPane.showMessageDialog(null, 
                "Account number: " + accountNum + "\n" +
                "Username: " + userName + "\n" +
                "Email: " + email + "\n" +
                "Phone Number: " + phoneNum + "\n" +
                "Balance: $" + balance, "Account Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
