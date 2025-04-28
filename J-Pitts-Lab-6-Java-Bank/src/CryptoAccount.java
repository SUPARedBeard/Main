import javax.swing.*;

class CryptoAccount extends BankAccount
{
    // declaring private fields for cryptocurrency balance and type
	private double cryptoBalance;
	private String cryptoType;
	
    // constructor to initialize account with account number, user details, balance, crypto balance, and crypto type
	public CryptoAccount(String accountNum, String userName, String email, String phoneNum, double balance, double cryptoBalance, String cryptoType)
	{
		super(accountNum, userName, email, phoneNum, balance);  // calling superclass constructor to initialize common account details
		this.cryptoBalance = cryptoBalance;  // initializing cryptocurrency balance for crypto account
		this.cryptoType = cryptoType;  // initializing type of cryptocurrency (e.g., Bitcoin, Ethereum)
	}
	
    // overriding displayAccountInfo to show cryptocurrency balance and type along with standard account details
	@Override
	public void displayAccountInfo()
	{
		super.displayAccountInfo();  // displaying common account information
		// displaying additional information specific to crypto accounts: crypto balance and type
		JOptionPane.showMessageDialog(null, "Your Crypto Balance is: " + cryptoBalance + " " + cryptoType);
	}
}
