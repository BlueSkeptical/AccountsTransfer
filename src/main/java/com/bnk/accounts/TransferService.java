package com.bnk.accounts;

import lajkonik.fp.IO;

/**
 * An API for transferring value between two accounts
 */
public interface TransferService {
    
    IO<Integer> transfer(AccountNumber from, AccountNumber to, Value amount);
    
}
