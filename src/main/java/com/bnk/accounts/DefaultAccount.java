package com.bnk.accounts;

/**
 * A mutable implementation of an account
 */
public class DefaultAccount implements Account {
    
    private final int id;
    private long balance;
    
    public DefaultAccount(int id, long initilalBalance) {
        this.id = id;
        this.balance = initilalBalance;
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public long balance() {
        return balance;
    }

    @Override
    public void deposit(long value) {
        balance += value;
    }
}
