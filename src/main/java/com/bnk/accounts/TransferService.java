package com.bnk.accounts;


public interface TransferService {
    
    /**
     * Transfers value between two accounts
     * 
     * @param from id of the source Account
     * @param to id of the destination Account
     * @param amount value to transfer
     * @throws TransferException if unable to transfer, the balances stay unchanged
     */
    void transfer(int from, int to, long amount) throws TransferException;
}
