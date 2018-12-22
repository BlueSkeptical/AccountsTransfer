package com.bnk.accounts;

import com.bnk.utils.Result;

/**
 * The simplest possible implementation of transfers executor
 */
public class DefaultTransferService implements TransferService {
 
    @Override 
    public Result<com.bnk.utils.Void> transfer(Account from, Account to, Value amount) {
        return new Transfer(from, to, amount).execute();      
    }
}
