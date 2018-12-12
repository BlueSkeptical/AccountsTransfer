package com.bnk.accounts;

/**
 * Created by ThinkPad on 12/12/2018.
 */
public interface TransferService {
    void transfer( int from, int to, long amount) throws TransferException;
}
