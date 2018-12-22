package com.bnk.accounts;

import com.bnk.utils.Result;

public class DefaultAccount implements Account {
    
    public static long MAX_VALUE = Long.MAX_VALUE;
    
    private final int id;
    private final TransferService transferService;
    
    private Value balance;
    
    
    public DefaultAccount(int id, Value initilalBalance, TransferService transferService) {
        this.id = id;
        this.balance = initilalBalance;
        this.transferService = transferService;
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public Result<Value> balance() {
        return new Result.Success<Value>(balance);
    }

    @Override
    public Result<Result.Void> deposit(Value value) {
        balance = balance.add(value);
        return new Result.Success<>();
    }
    
    
    @Override
    public Result<Result.Void> withdraw(Value value){
        balance = balance.substract(value);
        return new Result.Success<>();
    }

    @Override
    public Result<Result.Void> transferTo(Account to, Value value) {
       return transferService.transfer(this, to, value);
    }
    
}
