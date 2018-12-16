package com.bnk.accounts;

import java.util.Objects;

/**
 * Inter account transfer logic
 */
public class Transfer {
    
    private final Account from;
    private final Account to;
    private final long amount;
    
    public Transfer(Account from, Account to, long amount)
    {
        Objects.requireNonNull(from);
        this.from = from;
        Objects.requireNonNull(to);
        this.to = to;
        this.amount = amount;
    }
    
    public void execute() throws TransferException, NotAuhtorizedException {
        
        from.withdraw(amount);
        to.deposit(amount);
    }
    
}
