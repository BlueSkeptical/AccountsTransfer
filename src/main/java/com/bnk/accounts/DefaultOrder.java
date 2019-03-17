package com.bnk.accounts;


public class DefaultOrder implements Order {

    private final AccountNumber accountNumber;
    private final Value amount;

    public DefaultOrder( AccountNumber accountNumber, Value amount) {
       this.accountNumber = accountNumber;
       this.amount = amount;
    }
    
    @Override
    public AccountNumber accountNumber() {
        return accountNumber;
    }

    @Override
    public Value amount() {
        return amount;
    }
    
}
