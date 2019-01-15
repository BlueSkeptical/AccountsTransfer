package com.bnk.accounts;

import com.bnk.utils.Result;

/**
 * An  
 */
public class DefaultTransferService implements TransferService {
 
    private final AccountsRepository repository;
    
    public DefaultTransferService(AccountsRepository repository) {
       this.repository = repository; 
    }
    
    @Override 
    public Result<Result.Void> transfer(AccountNumber from, AccountNumber to, Value amount) {
        return repository.account(from).with(repository.account(to), f -> t -> new Transfer(f,t, amount).execute());
     
    }
}
