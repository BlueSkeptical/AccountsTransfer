package com.bnk.accounts;

/**
 * An API for transferring value between two accounts
 */
public interface TransferService {
    
    Value transfer(AccountNumber from, AccountNumber to, Value amount);
    
    Account withdraw(Account from, Value amount);
    
    Account deposit(Account to, Value amount);
}
