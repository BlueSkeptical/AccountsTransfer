package sample.accounts.ws;

import sample.accounts.AccountNumber;
import sample.accounts.Value;

public final class TransferCommand {
    
    public final AccountNumber fromAccountNumber;
    public final AccountNumber toAccountNumber;
    public final Value amount;
    
    
    public TransferCommand(AccountNumber fromAccountNumber, AccountNumber toAccountNumber, Value amount) {
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.amount = amount;
    }
    
    
}
