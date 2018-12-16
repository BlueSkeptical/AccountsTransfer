package com.bnk.accounts;

/**
 * An implementation of an account, 
 * with the balance that cannot be negative
 */
public class DefaultAccount implements Account {
    
    public static long MAX_VALUE = Long.MAX_VALUE;
    
    private final int id;
    private long balance;
    private TransferService transferService;
    
    public DefaultAccount(int id, long initilalBalance, TransferService transferService) {
        this.id = id;
        this.balance = initilalBalance;
        this.transferService = transferService;
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

    @Override
    public void transferTo(Account to, long value) throws TransferException,NotAuhtorizedException {
       transferService.transfer(this, to, value);
    }
    
}
