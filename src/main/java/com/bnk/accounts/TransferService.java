package com.bnk.accounts;

import com.bnk.utils.fp.Try;

/**
 * An API for transferring value between two accounts
 */
public interface TransferService {
    
    Try<Value> transfer(AccountNumber from, AccountNumber to, Value amount);
    
    Account withdraw(Account from, Value amount);
    
    Account deposit(Account to, Value amount);
}
