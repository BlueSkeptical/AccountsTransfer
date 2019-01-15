package com.bnk.accounts;

import com.bnk.utils.Result;

public class DefaultAccount implements Account {
    
    public static long MAX_VALUE = Long.MAX_VALUE;
    
    private final int id;
    private final OwnerName ownerName;
    private Value balance;
    
    
    public DefaultAccount(int id, OwnerName ownerName, Value initilalBalance) {
        this.id = id;
        this.ownerName = ownerName;
        this.balance = initilalBalance;
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public OwnerName ownerName() {
        return ownerName;
    }
    
        @Override
    public Value balance() {
        return balance;
    }
    
    @Override
    public String toString() {
        return "Account # " + id + " Name: " + ownerName.firstName + " " + ownerName.secondName;
    }

    @Override
    public void setBalance(Value amount) {
       balance = amount;
    }
}
