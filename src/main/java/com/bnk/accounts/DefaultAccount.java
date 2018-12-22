package com.bnk.accounts;

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
    public Value balance() {
        return balance;
    }

    @Override
    public void deposit(Value value) throws TransferException {
        balance = balance.add(value);
    }
    
    
    @Override
    public void withdraw(Value value) throws TransferException {
        balance = balance.substract(value);
    }

    @Override
    public void transferTo(Account to, Value value) throws TransferException,NotAuhtorizedException {
       transferService.transfer(this, to, value);
    }
    
}
