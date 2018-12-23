package com.bnk.accounts;

import com.bnk.utils.Result;

/**
 * An  
 */
public class DefaultTransferService implements TransferService {
 
    @Override 
    public Result<Result.Void> transfer(Account from, Account to, Value amount) {
        return new Transfer(from, to, amount).execute();      
    }
}
