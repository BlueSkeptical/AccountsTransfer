package com.bnk.accounts;

import com.bnk.utils.fp.IO;
import com.bnk.utils.fp.Try;

/**
 * An API for transferring value between two accounts
 */
public interface TransferService {
    
    IO<Try<Integer>> transfer(AccountNumber from, AccountNumber to, Value amount);
    
}
