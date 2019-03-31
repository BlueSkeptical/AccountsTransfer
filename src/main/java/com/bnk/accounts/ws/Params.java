package com.bnk.accounts.ws;

import com.bnk.accounts.AccountNumber;
import com.bnk.accounts.Value;

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
