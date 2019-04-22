package sample.accounts.ws;

import sample.accounts.AccountNumber;
import sample.accounts.Value;

public final class Params {
    
    public final AccountNumber fromAccountNumber;
    public final AccountNumber toAccountNumber;
    public final Value amount;
    
    
    public Params(AccountNumber fromAccountNumber, AccountNumber toAccountNumber, Value amount) {
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.amount = amount;
    }
    
    
}
