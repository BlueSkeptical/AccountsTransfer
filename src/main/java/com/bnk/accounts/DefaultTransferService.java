package com.bnk.accounts;

/**
 * The simplest possible implementation of transfers executor
 */
public class DefaultTransferService implements TransferService {
 
    @Override 
    public void transfer(Account from, Account to, long amount) throws TransferException, NotAuhtorizedException {
            new Transfer(from, to, amount).execute();      
    }
}
