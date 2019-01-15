package com.bnk.accounts;

import com.bnk.utils.Result;
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
    
    public Result<Result.Void> execute() {
        return isAllowed(amount) ? transfer() : new Result.Fail<>(new TransferException("Transfer is not allowed"));
    }
    
    private boolean isAllowed(Value amount) {
        boolean result =  canWithdraw(amount) && canDeposit(amount);
        return result;
    }
    
    private boolean canWithdraw(Value amount) {
        return from.balance().substract(amount).compareTo(Value.ZERO) >= 0;
    }
    
    private  boolean canDeposit(Value amount) {
        return to.balance().compareTo(Value.MAX.substract(amount)) < 0;
    }
    
    private Result<Result.Void> transfer() {
        from.setBalance( from.balance().substract(amount));
        to.setBalance(to.balance().add(amount));
        
        return new Result.Success<>();
    }
}
