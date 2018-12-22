package com.bnk.accounts;

import com.bnk.utils.Result;
import java.util.Objects;
import java.util.function.Function;

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
    
    public Result<Void> execute() {
        return isAllowed(amount).mapValue(allowed -> allowed ? transfer() : new Result.Fail<>(new TransferException("Transfer is not allowed")));
    }
    
    private Result<Boolean> isAllowed(Value amount) {
        Result<Boolean> result =  canWithdraw(amount).mapValue(cw -> canDeposit(amount).mapValue(cd -> new Result.Success<>(cw && cd)));
        return result;
    }
    
    private Result<Boolean> canWithdraw(Value amount) {
        return from.balance().mapValue(v -> new Result.Success<>(v.substract(amount).compareTo(Value.ZERO) >= 0));
    }
    
    private  Result<Boolean> canDeposit(Value amount) {
        return to.balance().mapValue(v -> new Result.Success<>(v.compareTo(Value.MAX.substract(amount)) < 0));
    }
    
    private Result<Void> transfer() {
        from.withdraw(amount);
        to.deposit(amount);
        
        return new Result.Success<>();
    }
}
