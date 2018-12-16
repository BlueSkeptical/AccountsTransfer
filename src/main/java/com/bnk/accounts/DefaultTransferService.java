package com.bnk.accounts;


public class DefaultTransferService implements TransferService {
 
    @Override 
    public void transfer(Account from, Account to, long amount) throws TransferException, NotAuhtorizedException {
            new Transfer(from, to, amount).execute();      
    }
}
