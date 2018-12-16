package com.bnk.accounts;

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
     * @throws TransferException if unable to transfer, the balances stay unchanged
     * @throws com.bnk.accounts.NotAuhtorizedException
     */
    void transfer(Account from, Account to, long amount) throws TransferException, NotAuhtorizedException;
}
