package com.bnk.accounts;


public interface Account {
    
    int id();

    long balance() throws NotAuhtorizedException;

    void deposit(long value) throws NotAuhtorizedException, TransferException;
    
    void withdraw(long value) throws NotAuhtorizedException, TransferException;
    
    void transferTo(Account to, long value) throws NotAuhtorizedException, TransferException;
}
