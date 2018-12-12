package com.bnk.accounts;

/**
 * Created by ThinkPad on 12/12/2018.
 */
public class DefaultAccount implements Account {
    
    private long balance;
    
    public DefaultAccount(long initilalBalance) {
        balance = initilalBalance;
    }

    @Override
    public int id() {
        return 0;
    }

    @Override
    public long balance() {
        return balance;
    }

    @Override
    public void deposit(long value) {
        balance+=value;
    }
}
