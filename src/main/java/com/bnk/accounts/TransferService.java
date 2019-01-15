package com.bnk.accounts;

import com.bnk.utils.Result;

/**
 * An API for transferring value between two accounts
 */
public interface TransferService {
    
    /**
     * Transfers value from one account to an another
     * 
     * @param from id of the source Account
     * @param to id of the destination Account
     * @param amount value to transfer
     * @return 
     */
    Result<Result.Void> transfer(AccountNumber from, AccountNumber to, Value amount);
}
