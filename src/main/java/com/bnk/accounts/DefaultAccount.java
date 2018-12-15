package com.bnk.accounts;

/**
 * A mutable implementation of an account
 */
public class DefaultAccount implements Account {
    
    private final int id;
    private long balance;
    
    public static long MAX_VALUE = Long.MAX_VALUE;
    
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
    public void deposit(long value) throws TransferException {
        if (MAX_VALUE - value < balance) {
            throw new TransferException("Transfer will cause account overflow");
        }
        balance += value;
    }
    
    
    @Override
    public void withdraw(long value) throws TransferException {
        if (balance - value < 0) {
            throw new TransferException("Not enough value on the account");
        }
        balance -= value;
    }
    
}
