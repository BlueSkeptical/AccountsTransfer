package com.bnk.accounts;

import java.util.Objects;

/**
 * Inter account transfer logic
 */
public class Transfer {
    
    private final Account from;
    private final Account to;
    private final Value amount;
    
    public Transfer(Account from, Account to, Value amount)
    {
        Objects.requireNonNull(from);
        this.from = from;
        Objects.requireNonNull(to);
        this.to = to;
        this.amount = amount;
    }
    
    public void execute() throws TransferException, NotAuhtorizedException {
        
        if (from.balance().substract(amount).compareTo(Value.ZERO) < 0) {
            throw new TransferException("Not enough value on the account");
        } 
        
        if (to.balance().compareTo(Value.MAX.substract(amount)) > 0) {
            throw new TransferException("Transfer will cause account overflow");
        }
        
        
        from.withdraw(amount);
        to.deposit(amount);
    }
    
}
