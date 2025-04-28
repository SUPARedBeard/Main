//Josh Pitts
//CPT 236
//Lab 6


import javax.swing.*;

public class BankingSystem 
{
    public static void main(String[] args)
    {
        BankAccount account = null;
        
        //presenting a dialog for selecting account type
        String[] options = {"Savings Account", "Checking Account", "Mutual Fund Account", "Crypto Account"};
        int choice = JOptionPane.showOptionDialog(null, "Select Account Type:", "Banking System",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        
        //input loop to get a valid 7-digit account number
        String accountNum;
        while (true) 
        {
            accountNum = JOptionPane.showInputDialog("Please Enter Account Number (7 digits):");
            if (accountNum.matches("\\d{7}")) 
            {  //check if account number is exactly 7 digits
                break; 
            } 
            else 
            {
                JOptionPane.showMessageDialog(null, "Invalid account number! Must be exactly 7 digits.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        //input for username
        String userName = JOptionPane.showInputDialog("Please Enter Username:");
        
        //input loop to get a valid Gmail address
        String email;
        while (true) 
        {
            email = JOptionPane.showInputDialog("Please Enter Email (must have domain name ex. @gmail.com):");
            if (email.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$")) 
            {  //validating email format
                break;
            } 
            else 
            {
                JOptionPane.showMessageDialog(null, "Invalid email! Must be a valid @gmail.com address.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        //input loop to get a valid phone number
        String phoneNum;
        while (true) {
            phoneNum = JOptionPane.showInputDialog("Please Enter Phone Number (10 digits):");
            if (phoneNum.matches("\\d{10}")) {  //check if phone number is exactly 10 digits
                break; 
            } else {
                JOptionPane.showMessageDialog(null, "Invalid phone number! Must be exactly 10 digits.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        //input for initial deposit
        double balance = Double.parseDouble(JOptionPane.showInputDialog("Please enter initial deposit amount:"));
        
        //creating account based on user choice
        switch (choice)
        {
            case 0:
                double interestRate = Double.parseDouble(JOptionPane.showInputDialog("Please Enter Interest Rate (%):"));
                account = new SavingsAccount(accountNum, userName, email, phoneNum, balance, interestRate);
                break;
            
            case 1:
                double overdraftLimit = Double.parseDouble(JOptionPane.showInputDialog("Please Enter Overdraft Limit:"));
                account = new CheckingAccount(accountNum, userName, email, phoneNum, balance, overdraftLimit);
                break;
            
            case 2:
                double investmentBalance = Double.parseDouble(JOptionPane.showInputDialog("Please Enter Investment Balance:"));
                String riskLevel = JOptionPane.showInputDialog("Please Enter Risk Level (Low/Medium/High):");
                account = new MutualFundAccount(accountNum, userName, email, phoneNum, balance, investmentBalance, riskLevel);
                break;
                
            case 3:
                double cryptoBalance = Double.parseDouble(JOptionPane.showInputDialog("Please Enter Crypto Balance:"));
                String cryptoType = JOptionPane.showInputDialog("Please Enter Crypto Type(Bitcoin, Ethereum, DOGE, etc.):");
                account = new CryptoAccount(accountNum, userName, email, phoneNum, balance, cryptoBalance, cryptoType);
                break;
        }
        
        //if account was created, proceed with transactions
        if (account != null)
        {
            while(true)
            {
                //options for different transactions
                String[] transactions = {"Deposit", "Withdraw", "View Account Info", "Exit"};
                int transactionChoice = JOptionPane.showOptionDialog(null, "Please Select Transaction Type:", "Banking System",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, transactions, transactions[0]);
                
                //handle Deposit transaction
                if(transactionChoice == 0)
                {
                    while (true) 
                    {
                        try 
                        {
                            String input = JOptionPane.showInputDialog("Enter Deposit Amount:");
                            if (input == null) { //if user presses Cancel, exit loop
                                break;
                            }

                            double depositAmount = Double.parseDouble(input);

                            if (depositAmount <= 0) 
                            {
                                JOptionPane.showMessageDialog(null, "Deposit must be a positive number!", "Error", JOptionPane.ERROR_MESSAGE);
                            } 
                            else 
                            {
                                account.deposit(depositAmount);
                                JOptionPane.showMessageDialog(null, "Deposit successful! New Balance: $" + account.getBalance());
                                break; //exit loop when valid input is received
                            }
                        } 
                        catch (NumberFormatException e) 
                        {
                            JOptionPane.showMessageDialog(null, "Invalid input! Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                //handle Withdrawal transaction
                else if(transactionChoice == 1)
                {
                    while (true) 
                    {
                        try 
                        {
                            String input = JOptionPane.showInputDialog("Enter Withdrawal Amount:");
                            if (input == null) 
                            { //if user presses Cancel, exit loop
                                break;
                            }

                            double withdrawAmount = Double.parseDouble(input);

                            if (account.withdraw(withdrawAmount)) 
                            {
                                JOptionPane.showMessageDialog(null, "Withdrawal successful! New Balance: $" + account.getBalance());
                                break; //exit loop when valid input is received
                            }
                        } 
                        catch (NumberFormatException e) 
                        {
                            JOptionPane.showMessageDialog(null, "Invalid input! Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                //view account info
                else if(transactionChoice == 2)
                {
                    account.displayAccountInfo();
                }
                //exit the system
                else
                {
                    JOptionPane.showMessageDialog(null, "Thank You For Banking With Us, Have A Great Day!");
                    break;
                }
            }
        }
    }
}
