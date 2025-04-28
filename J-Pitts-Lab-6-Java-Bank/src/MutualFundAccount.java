import javax.swing.*;

class MutualFundAccount extends BankAccount
{
    // declaring private fields for investment balance and risk level
	private double investmentBalance;
	private String riskLevel;
	
    // constructor to initialize account with account number, user details, balance, investment balance, and risk level
	public MutualFundAccount(String accountNum, String userName, String email, String phoneNum, double balance, double investmentBalance, String riskLevel) 
	{
        super(accountNum, userName, email, phoneNum, balance);  // calling superclass constructor to initialize common account details
        this.investmentBalance = investmentBalance;  // initializing investment balance for mutual fund account
        this.riskLevel = riskLevel;  // initializing risk level for the mutual fund account
	}
	
    // overriding displayAccountInfo to show investment balance and risk level for the mutual fund account
	@Override
	public void displayAccountInfo()
	{
		super.displayAccountInfo();  // displaying common account information
		// displaying additional information specific to mutual fund accounts: investment balance and risk level
		JOptionPane.showMessageDialog(null, "Your Investment Balance is: $" + investmentBalance + "\nYour Risk Level is: " + riskLevel);
	}
}
